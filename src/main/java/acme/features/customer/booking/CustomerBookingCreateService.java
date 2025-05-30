
package acme.features.customer.booking;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorised = true;
		if (super.getRequest().hasData("id")) {
			int bookingId = super.getRequest().getData("id", int.class);
			if (bookingId != 0)
				authorised = false;
			else if (super.getRequest().getMethod().equals("POST")) {
				int flightId = super.getRequest().getData("flight", int.class);
				Flight flight = this.repository.findFlightById(flightId);
				List<Flight> allFlights = this.repository.findFlights();

				boolean flightInvalid = flightId != 0 && flight == null;
				boolean flightNotAvailable = flight != null && !allFlights.contains(flight);

				authorised = !(flightInvalid || flightNotAvailable);

				String aTravelClass = super.getRequest().getData("travelClass", String.class);
				if (aTravelClass == null || aTravelClass.trim().isEmpty() || Arrays.stream(TravelClassType.values()).noneMatch(s -> s.name().equals(aTravelClass)) && !aTravelClass.equals("0"))
					authorised = false;

			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setCustomer(customer);

		super.getBuffer().addData(booking);

	}
	@Override
	public void bind(final Booking booking) {
		int flightId;
		Flight flight;
		Date purchaseMoment = MomentHelper.getCurrentMoment();

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);

		super.bindObject(booking, "travelClass", "lastCreditCardNibble", "locatorCode");
		booking.setFlight(flight);
		booking.setPurchaseMoment(purchaseMoment);
		booking.setDraftMode(true);

	}
	@Override
	public void validate(final Booking booking) {
		;
	}

	@Override
	public void perform(final Booking booking) {
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
