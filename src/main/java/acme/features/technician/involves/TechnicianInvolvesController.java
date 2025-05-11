
package acme.features.technician.involves;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.maintenanceRecord.Involves;
import acme.realms.employee.Technician;

@GuiController
public class TechnicianInvolvesController extends AbstractGuiController<Technician, Involves> {

	@Autowired
	private TechnicianInvolvesCreateService	createService;
	@Autowired
	private TechnicianInvolvesDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);

	}

}
