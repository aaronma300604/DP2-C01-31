
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Passenger> passengers;
		int customerId;
		int bookingId;

		customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().getData().isEmpty())
			passengers = this.repository.findPassengersByCustomerId(customerId);
		else {
			bookingId = super.getRequest().getData("bookingId", int.class);
			passengers = this.repository.findPassengersByBookingId(bookingId);
		}
		super.getBuffer().addData(passengers);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds");

		super.getResponse().addData(dataset);
	}

}
