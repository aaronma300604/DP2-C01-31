
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerShowService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerPassengerRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int passengerId;
		Passenger passenger;
		Customer customer;

		passengerId = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(passengerId);
		customer = passenger == null ? null : passenger.getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) || passenger != null && !passenger.isDraftMode();

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		this.getBuffer().addData(passenger);

	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;

		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");

		super.getResponse().addData(dataset);

	}

}
