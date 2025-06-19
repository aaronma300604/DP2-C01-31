
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
		if (super.getRequest().hasData("id")) {
			int passengerBookingId = super.getRequest().getData("id", int.class);
			if (passengerBookingId != 0)
				authorised = false;
			else if (super.getRequest().getMethod().equals("POST")) {
				int bookingId = super.getRequest().getData("booking", int.class);
				int passengerId = super.getRequest().getData("passenger", int.class);
				Booking booking = this.repository.findBookingById(bookingId);
				Passenger passenger = this.repository.findPassengerById(passengerId);
				List<Booking> allowedBookings = this.repository.findBookingByCustomerId(customerId);
				List<Passenger> allowedPassengers = this.repository.findPassengerByCustomerId(customerId);
				if (booking == null && bookingId != 0 || booking != null && !allowedBookings.contains(booking))
					authorised = false;
				if (passenger == null && passengerId != 0 || passenger != null && !allowedPassengers.contains(passenger))
					authorised = false;
			}
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
		super.bindObject(passengerBooking, "passenger", "booking");
		passengerBooking.setPassenger(passenger);
		passengerBooking.setBooking(booking);

	}

	@Override

	public void validate(final PassengerBooking passengerBooking) {
		if (passengerBooking.getBooking() != null && passengerBooking.getPassenger() != null) {
			super.state(passengerBooking.getBooking().isDraftMode(), "booking", "acme.validation.booking.booking-publish.message");

			if (passengerBooking.getPassenger() != null)
				super.state(!passengerBooking.getPassenger().isDraftMode(), "passenger", "acme.validation.booking.booking-publish.message");
			if (passengerBooking.getBooking() != null && passengerBooking.getPassenger() != null) {
				boolean permission = true;
				PassengerBooking existing;
				int bookingId = passengerBooking.getBooking().getId();
				int passengerId = passengerBooking.getPassenger().getId();

				existing = this.repository.relationPassengerInBooking(bookingId, passengerId);

				if (existing != null) {
					permission = false;
					super.state(permission, "*", "acme.validation.booking.duplicated_passenger_booking.message");
				}
			}
		}
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
