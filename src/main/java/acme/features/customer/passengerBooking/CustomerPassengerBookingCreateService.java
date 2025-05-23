
package acme.features.customer.passengerBooking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerBookingCreateService extends AbstractGuiService<Customer, PassengerBooking> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerPassengerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorised = true;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		int bookingId = super.getRequest().getData("bookingId", int.class);
		Booking booking = this.repository.findBookingById(bookingId);

		boolean invalidBooking = booking == null || booking.getCustomer().getId() != customerId || !booking.isDraftMode();
		if (invalidBooking)
			authorised = false;
		else if (super.getRequest().getMethod().equals("POST")) {
			int passengerId = super.getRequest().getData("passenger", int.class);
			Passenger passenger = this.repository.findPassengerById(passengerId);
			List<Passenger> myPassengers = this.repository.findPublishedPassengersFromId(customerId);

			boolean passengerInvalid = passengerId != 0 && (passenger == null || !myPassengers.contains(passenger));
			authorised = !passengerInvalid;
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		PassengerBooking passengerBooking;

		passengerBooking = new PassengerBooking();

		super.getBuffer().addData(passengerBooking);

	}

	@Override
	public void bind(final PassengerBooking passengerBooking) {
		int passengerId;
		int bookingId;
		Booking booking;
		Passenger passenger;

		passengerId = super.getRequest().getData("passenger", int.class);
		bookingId = super.getRequest().getData("booking", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		booking = this.repository.findBookingById(bookingId);

		if (booking == null || passenger == null)
			super.state(false, booking == null ? "booking" : "passenger", booking == null ? "acme.validation.booking.booking-null.message" : "acme.validation.booking.passenger-null.message");
		else {
			super.bindObject(passengerBooking, "passenger", "booking");
			passengerBooking.setPassenger(passenger);
			passengerBooking.setBooking(booking);

		}

	}

	@Override

	public void validate(final PassengerBooking passengerBooking) {
		if (passengerBooking.getBooking() != null)
			super.state(passengerBooking.getBooking().isDraftMode(), "booking", "acme.validation.booking.booking-publish.message");
	}

	@Override
	public void perform(final PassengerBooking passengerBooking) {
		this.repository.save(passengerBooking);
	}

	@Override
	public void unbind(final PassengerBooking passengerBooking) {
		Dataset dataset;
		SelectChoices passengerChoices;
		SelectChoices bookingChoices;
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		List<Booking> allBookingByCustomerId;
		List<Passenger> allPassengerByCustomerId;

		allBookingByCustomerId = this.repository.findBookingByCustomerId(customerId);
		allPassengerByCustomerId = this.repository.findPassengerByCustomerId(customerId);

		bookingChoices = SelectChoices.from(allBookingByCustomerId, "locatorCode", passengerBooking.getBooking());
		passengerChoices = SelectChoices.from(allPassengerByCustomerId, "passportNumber", passengerBooking.getPassenger());

		dataset = super.unbindObject(passengerBooking);
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookingChoices", bookingChoices);
		dataset.put("passengerChoices", passengerChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
