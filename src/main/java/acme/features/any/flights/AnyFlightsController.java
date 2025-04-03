
package acme.features.any.flights;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flight.Flight;

@GuiController
public class AnyFlightsController extends AbstractGuiController<Any, Flight> {

	@Autowired
	private AnyFlightsListService	listService;

	@Autowired
	private AnyFlightsShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
