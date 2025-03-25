
package acme.features.administrator.airport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airport.Airport;

@GuiController
public class AdministratorAirportsController extends AbstractGuiController<Administrator, Airport> {

	@Autowired
	private AdministratorAirportsListService	listService;

	@Autowired
	private AdministratorAirportsShowService	showService;

	@Autowired
	private AdministratorAirportsCreateService	createService;

	@Autowired
	private AdministratorAirportsUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
	}
}
