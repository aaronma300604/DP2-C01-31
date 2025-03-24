
package acme.features.authenticated.customer;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Authenticated;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.realms.client.Customer;

@GuiController
public class AuthenticatedCustomerController extends AbstractGuiController<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedCustomerListService listService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);

	}

}
