
package acme.features.customer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;
import acme.features.customer.passengerBooking.CustomerPassengerBookingDeleteService;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {

	@Autowired
	private CustomerBookingRepository				repository;
	@Autowired
	private CustomerPassengerBookingDeleteService	passengerBookingService;


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

					boolean invalidFlight = flightId != 0 && (flight == null || !availableFlights.contains(flight));
					if (invalidFlight)
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

		super.bindObject(booking, "travelClass", "lastCreditCardNibble", "locatorCode");
		booking.setFlight(flight);
		booking.setDraftMode(true);

	}

	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
		List<PassengerBooking> relation = this.repository.findPassengerBookingByBookingId(booking.getId());
		relation.stream().forEach(pb -> this.passengerBookingService.perform(pb));
		this.repository.delete(booking);

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
