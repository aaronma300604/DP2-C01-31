
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
	private ManagerFlightsListService	listService;

	@Autowired
	private ManagerFlightsShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}

}
