
package acme.features.customer.passengerBooking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerBookingListService extends AbstractGuiService<Customer, PassengerBooking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerPassengerBookingRepository repository;

	// AbstractService<Authenticated, Customer> ---------------------------


	@Override
	public void authorise() {
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean isOwner = booking != null && booking.getCustomer().getId() == customerId;
		super.getResponse().setAuthorised(isOwner);
	}

	@Override
	public void load() {
		List<PassengerBooking> passengerBookings;
		int customerId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		passengerBookings = this.repository.findPassengerBookingsByCustomerId(customerId);

		super.getBuffer().addData(passengerBookings);
	}

	@Override
	public void unbind(final PassengerBooking passengerBookings) {
		Dataset dataset;

		dataset = super.unbindObject(passengerBookings, "passenger.passportNumber", "booking.locatorCode");

		super.getResponse().addData(dataset);
	}

}
