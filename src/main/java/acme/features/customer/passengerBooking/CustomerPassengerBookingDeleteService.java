
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
public class CustomerPassengerBookingDeleteService extends AbstractGuiService<Customer, PassengerBooking> {

	//Internal state---------------------------------------------------------

	@Autowired
	private CustomerPassengerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status = false;

		if (super.getRequest().hasData("id")) {
			int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int passengerBookingId = super.getRequest().getData("id", int.class);

			if (passengerBookingId != 0) {
				PassengerBooking passengerBooking = this.repository.findPassengerBookingById(passengerBookingId);

				if (passengerBooking != null && passengerBooking.getPassenger() != null && passengerBooking.getBooking() != null) {
					Passenger passenger = passengerBooking.getPassenger();
					Booking booking = passengerBooking.getBooking();

					boolean sameCustomer = booking.getCustomer().getId() == customerId && passenger.getCustomer().getId() == customerId;

					boolean inCustomerPassengers = this.repository.findPassengerByCustomerId(customerId).contains(passenger);
					boolean inCustomerBookings = this.repository.findBookingByCustomerId(customerId).contains(booking);

					status = sameCustomer && inCustomerPassengers && inCustomerBookings;
				}
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		int passengerbookingId;
		PassengerBooking passengerBooking;

		passengerbookingId = super.getRequest().getData("id", int.class);
		passengerBooking = this.repository.findPassengerBookingById(passengerbookingId);

		this.getBuffer().addData(passengerBooking);

	}
	@Override
	public void validate(final PassengerBooking passengerBooking) {
		super.state(passengerBooking.getBooking().isDraftMode(), "booking", "acme.validation.booking.booking-publish.message");
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
	public void perform(final PassengerBooking passengerBooking) {
		this.repository.delete(passengerBooking);
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
