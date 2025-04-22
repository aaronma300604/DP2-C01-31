
package acme.features.administrator.booking;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;

@GuiService
public class AdministratorBookingShowService extends AbstractGuiService<Administrator, Booking> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AdministratorBookingRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int bookingId;
		Booking booking;

		bookingId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(bookingId);
		status = booking != null && !booking.isDraftMode();

		super.getResponse().setAuthorised(status);

	}
	@Override
	public void validate(final Booking booking) {

	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		this.getBuffer().addData(booking);

	}

	//	@Override
	//	public void unbind(final Booking booking) {
	//		Dataset dataset;
	//		SelectChoices choices;
	//		List<Flight> allFlights;
	//		SelectChoices travelClassChoices;
	//		List<Passenger> passenger;
	//		boolean existsAnyPassenger = true;
	//
	//		allFlights = this.repository.findFlights();
	//		choices = SelectChoices.from(allFlights, "tag", booking.getFlight());
	//		travelClassChoices = SelectChoices.from(TravelClassType.class, booking.getTravelClass());
	//		passenger = this.repository.findPassengerByBookingId(booking.getId());
	//
	//		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode");
	//		dataset.put("price", booking.price());
	//		dataset.put("flight", choices.getSelected().getKey());
	//		dataset.put("flights", choices);
	//		dataset.put("travelClasses", travelClassChoices);
	//		dataset.put("travelClass", travelClassChoices.getSelected().getKey());
	//		dataset.put("bookingId", booking.getId());
	//		if (passenger.isEmpty())
	//			existsAnyPassenger = false;
	//		dataset.put("existsAnyPassenger", existsAnyPassenger);
	//		dataset.put("isFromBooking", true);
	//
	//		super.getResponse().addData(dataset);
	//	}
	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices choices;
		List<Flight> allFlights;
		SelectChoices travelClassChoices;
		List<Passenger> passenger;
		boolean existsAnyPassenger = true;

		passenger = this.repository.findPassengerByBookingId(booking.getId());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCreditCardNibble", "draftMode");
		dataset.put("price", booking.price());
		dataset.put("flight", booking.getFlight());
		dataset.put("travelClass", booking.getTravelClass());
		dataset.put("bookingId", booking.getId());
		if (passenger.isEmpty())
			existsAnyPassenger = false;
		dataset.put("existsAnyPassenger", existsAnyPassenger);
		dataset.put("isFromBooking", true);

		super.getResponse().addData(dataset);
	}

}
