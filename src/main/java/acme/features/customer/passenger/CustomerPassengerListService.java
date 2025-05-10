
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerListService extends AbstractGuiService<Customer, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractService<Authenticated, Customer> ---------------------------


	@Override
	public void authorise() {
		boolean authorised = true;

		if (!super.getRequest().getData().isEmpty()) {
			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int bookingId = super.getRequest().getData("bookingId", int.class);
			Booking booking = this.repository.findBookingById(bookingId);

			boolean invalid = booking == null || booking.getCustomer().getId() != customerId;
			authorised = !invalid;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int customerId;
		int bookingId;
		boolean isFromBooking = false;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			passengers = this.repository.findPassengersByCustomerId(customerId);
		else {
			bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.repository.findPassengersByBookingId(bookingId);
			super.getResponse().addGlobal("bookingId", bookingId);
			if (super.getRequest().hasData("isFromBooking"))
				isFromBooking = super.getRequest().getData("isFromBooking", boolean.class);
			super.getResponse().addGlobal("isFromBooking", isFromBooking);
		}
		super.getBuffer().addData(passengers);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		dataset.put("isFromBooking", false);

		super.getResponse().addData(dataset);
	}

}
