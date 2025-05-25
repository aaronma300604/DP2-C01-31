
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.passenger.Passenger;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerCreateService extends AbstractGuiService<Customer, Passenger> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private CustomerPassengerRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean authorised = true;
		if (super.getRequest().hasData("id")) {
			int passengerId = super.getRequest().getData("id", int.class);
			if (passengerId != 0)
				authorised = false;
			else if (super.getRequest().getMethod().equals("POST")) {
				Passenger passenger = this.repository.findPassengerById(passengerId);
				if (passenger != null)
					authorised = false;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void load() {
		Passenger passenger;
		Customer customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		passenger = new Passenger();
		passenger.setCustomer(customer);

		super.getBuffer().addData(passenger);

	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds");
		passenger.setDraftMode(true);

	}

	@Override
	public void perform(final Passenger passenger) {
		this.repository.save(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}

}
