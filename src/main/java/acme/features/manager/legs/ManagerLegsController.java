
package acme.features.manager.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@GuiController
public class ManagerLegsController extends AbstractGuiController<AirlineManager, Leg> {

	@Autowired
	private ManagerLegsListService		listService;

	@Autowired
	private ManagerLegsShowService		showService;

	@Autowired
	private ManagerLegsCreateService	createService;

	@Autowired
	private ManagerLegsPublishService	publishService;

	@Autowired
	private ManagerLegsUpdateService	updateService;

	@Autowired
	private ManagerLegsDeleteService	deleteService;


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
