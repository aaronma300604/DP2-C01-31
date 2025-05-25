
package acme.features.customer.booking;

import java.util.Arrays;
import java.util.List;

import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean authorised = false;

		if (super.getRequest().hasData("id")) {
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.repository.findBookingById(bookingId);

			if (booking != null) {
				Customer customer = booking.getCustomer();
				boolean isOwned = super.getRequest().getPrincipal().hasRealm(customer);
				boolean isDraft = booking.isDraftMode();
				authorised = isOwned && isDraft;

				if (authorised && super.getRequest().hasData("flight")) {
					int flightId = super.getRequest().getData("flight", int.class);
					Flight flight = this.repository.findFlightById(flightId);
					List<Flight> availableFlights = this.repository.findFlights();

					boolean invalidFlightId = flightId != 0 && (flight == null || !availableFlights.contains(flight));
					if (invalidFlightId)
						authorised = false;
				}
				if (authorised && super.getRequest().hasData("travelClass")) {
					String aTravelClass = super.getRequest().getData("travelClass", String.class);
					if (aTravelClass == null || aTravelClass.trim().isEmpty() || Arrays.stream(TravelClassType.values()).noneMatch(s -> s.name().equals(aTravelClass)) && !aTravelClass.equals("0"))
						authorised = false;
				}
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		this.getBuffer().addData(booking);

	}

	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "travelClass", "lastCreditCardNibble");
		booking.setFlight(flight);
	}

	@Override
	public void validate(final Booking booking) {

		String lastCreditCardNibble = booking.getLastCreditCardNibble();
		List<Passenger> passengers = this.repository.findPassengerByBookingId(booking.getId());
		boolean canBePublishFlight = booking.getFlight() != null;
		//Para poder ser publicado debe tener al menos un pasajero publicado
		boolean canBePublishPassenger = passengers.isEmpty() || passengers.stream().allMatch(p -> !p.isDraftMode());
		boolean canBePublishLastCreditCard = !StringHelper.isBlank(lastCreditCardNibble);
		boolean almenosunpasajero = passengers.size() >= 1;

		boolean canBePublished = canBePublishPassenger && canBePublishLastCreditCard;

		super.state(canBePublished, "*", "acme.validation.booking.cant-be-publish.message");
		super.state(canBePublishFlight, "flight", "acme.validation.booking.cant-be-publish-flight.message");
		super.state(almenosunpasajero, "*", "acme.validation.booking.cant-be-publish-passenger.message");

	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		List<Flight> allFlights;
		SelectChoices choices;
		SelectChoices travelClassChoices;

		allFlights = this.repository.findFlights();
		choices = SelectChoices.from(allFlights, "tag", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClassType.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode");
		dataset.put("price", booking.price());
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("travelClasses", travelClassChoices);
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		super.getResponse().addData(dataset);

	}

}
