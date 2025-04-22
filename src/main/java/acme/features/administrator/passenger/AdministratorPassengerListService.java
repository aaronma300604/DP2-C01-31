
package acme.features.administrator.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;

@GuiService
public class AdministratorPassengerListService extends AbstractGuiService<Administrator, Passenger> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorPassengerRepository repository;

	// AbstractService<Administrator, Passenger> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int bookingId;
		boolean isFromBooking = false;
		bookingId = super.getRequest().getData("bookingId", int.class);
		passengers = this.repository.findPassengersByBookingId(bookingId);
		super.getResponse().addGlobal("bookingId", bookingId);
		if (super.getRequest().hasData("isFromBooking"))
			isFromBooking = super.getRequest().getData("isFromBooking", boolean.class);
		super.getResponse().addGlobal("isFromBooking", isFromBooking);
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
