
package acme.features.manager.flights;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;
import acme.realms.employee.AirlineManager;

@GuiController
public class ManagerFlightsController extends AbstractGuiController<AirlineManager, Flight> {

	@Autowired
	private ManagerFlightsListService		listService;

	@Autowired
	private ManagerFlightsShowService		showService;

	@Autowired
	private ManagerFlightsCreateService		createService;

	@Autowired
	private ManagerFlightsUpdateService		updateService;

	@Autowired
	private ManagerFlightsDeleteService		deleteService;

	@Autowired
	private ManagerFlightsPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
