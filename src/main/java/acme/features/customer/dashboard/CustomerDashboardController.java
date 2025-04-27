
package acme.features.customer.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.forms.manager.Dashboard;
import acme.realms.client.Customer;

@GuiController
public class CustomerDashboardController extends AbstractGuiController<Customer, Dashboard> {

	@Autowired
	private CustomerDashboardShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}

}
