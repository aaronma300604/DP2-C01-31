
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
public class CustomerPassengerBookingUpdateService extends AbstractGuiService<Customer, PassengerBooking> {

	//Internal state---------------------------------------------------------

	@Autowired
	private CustomerPassengerBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int passengerBookingId;
		PassengerBooking passengerBooking;
		Customer customer;

		passengerBookingId = super.getRequest().getData("id", int.class);
		passengerBooking = this.repository.findPassengerBookingById(passengerBookingId);
		customer = passengerBooking == null ? null : passengerBooking.getPassenger().getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) || passengerBooking != null && passengerBooking.isDraftMode();

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

		dataset = super.unbindObject(passengerBooking, "draftMode");
		dataset.put("booking", bookingChoices.getSelected().getKey());
		dataset.put("bookingChoices", bookingChoices);
		dataset.put("passengerChoices", passengerChoices);
		dataset.put("passenger", passengerChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
