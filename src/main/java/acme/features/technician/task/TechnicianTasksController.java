
package acme.features.technician.task;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiController
public class TechnicianTasksController extends AbstractGuiController<Technician, Task> {

	@Autowired
	private TechnicianTasksListService		listService;

	@Autowired
	private TechnicianTasksShowService		showService;

	@Autowired
	private TechnicianTasksCreateService	createService;

	@Autowired
	private TechnicianTasksUpdateService	updateService;

	@Autowired
	private TechnicianTasksPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
