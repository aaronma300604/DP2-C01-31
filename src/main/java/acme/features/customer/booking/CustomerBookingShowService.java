
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
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerBookingShowService extends AbstractGuiService<Customer, Booking> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			int bookingId = super.getRequest().getData("id", int.class);
			Booking booking = this.repository.findBookingById(bookingId);
			Customer customer = booking != null ? booking.getCustomer() : null;

			boolean isCustomer = customer != null && super.getRequest().getPrincipal().hasRealm(customer);
			boolean anyBooking = booking != null;

			status = isCustomer && anyBooking;
		}

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
		List<Passenger> passenger;
		boolean existsAnyPassenger = true;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		allFlights = this.repository.findFlights();
		choices = SelectChoices.from(allFlights, "tag", booking.getFlight());
		travelClassChoices = SelectChoices.from(TravelClassType.class, booking.getTravelClass());
		passenger = this.repository.findPassengerByBookingId(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode");
		dataset.put("price", booking.price());
		dataset.put("flight", choices.getSelected().getKey());
		dataset.put("flights", choices);
		dataset.put("travelClasses", travelClassChoices);
		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
		dataset.put("bookingId", booking.getId());
		if (passenger.isEmpty())
			existsAnyPassenger = false;
		dataset.put("existsAnyPassenger", existsAnyPassenger);
		dataset.put("isFromBooking", true);

		super.getResponse().addData(dataset);
	}

}
