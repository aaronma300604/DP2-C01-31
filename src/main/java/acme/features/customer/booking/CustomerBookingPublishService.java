
package acme.features.customer.booking;

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
		boolean status;
		int bookingId;
		Booking booking;
		Customer customer;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		customer = booking == null ? null : booking.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) || booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);
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

		boolean canBePublishPassenger = passengers.isEmpty() || passengers.stream().allMatch(p -> !p.isDraftMode());
		boolean canBePublishLastCreditCard = !StringHelper.isBlank(lastCreditCardNibble);

		boolean canBePublished = canBePublishPassenger && canBePublishLastCreditCard;

		super.state(canBePublished, "*", "acme.validation.booking.cant-be-publish.message");
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

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditCardNibble", "draftMode");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("travelClasses", travelClassChoices);
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		super.getResponse().addData(dataset);

	}

}
