
package acme.features.customer.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------
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
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		int customerId;
		List<Flight> allFlights;
		SelectChoices travelClassChoices;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		allFlights = this.repository.findFlights();
		choices = SelectChoices.from(allFlights, "tag", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClassType.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "price", "lastCreditCardNibble");
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flight", choices);
		dataset.put("travelClass", travelClassChoices);
		dataset.put("bookingId", booking.getId());

		super.getResponse().addData(dataset);
	}

}
