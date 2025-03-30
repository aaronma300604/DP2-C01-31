
package acme.features.customer.passenger;

import org.springframework.beans.factory.annotation.Autowired;

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
		super.getResponse().setAuthorised(true);
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

}
