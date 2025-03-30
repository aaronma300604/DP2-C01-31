
package acme.features.technician.maintenanceRecord;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.realms.employee.Technician;

@GuiController
public class TechnicianMaintenanceRecordsController extends AbstractGuiController<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordsListService		listService;

	@Autowired
	private TechnicianMaintenanceRecordsShowService		showService;

	@Autowired
	private TechnicianMaintenanceRecordsCreateService	createService;

	@Autowired
	private TechnicianMaintenanceRecordsUpdateService	updateService;

	@Autowired
	private TechnicianMaintenanceRecordsPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
