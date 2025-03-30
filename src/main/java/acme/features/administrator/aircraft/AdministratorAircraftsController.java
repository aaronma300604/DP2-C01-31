
package acme.features.administrator.aircraft;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.aircraft.Aircraft;

@GuiController
public class AdministratorAircraftsController extends AbstractGuiController<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftsListService		listService;

	@Autowired
	private AdministratorAircraftsShowService		showService;

	@Autowired
	private AdministratorAircraftsCreateService		createService;

	@Autowired
	private AdministratorAircraftsUpdateService		updateService;

	@Autowired
	private AdministratorAircraftsDisableService	disableService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("disable", "update", this.disableService);
	}
}
