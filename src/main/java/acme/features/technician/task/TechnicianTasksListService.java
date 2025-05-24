
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianTasksListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTasksRepository repository;


	@Override
	public void authorise() {
		int recordId;
		MaintenanceRecord record;
		Technician technician;
		boolean status;

		if (super.getRequest().hasData("recordId"))
			try {
				recordId = super.getRequest().getData("recordId", int.class);
			} catch (Exception e) {
				recordId = -1;
			}
		else
			recordId = -1;

		if (recordId == -1)
			super.getResponse().setAuthorised(true);
		else {
			record = this.repository.findRecord(recordId);
			technician = record == null ? null : record.getTechnician();
			status = technician != null && (super.getRequest().getPrincipal().hasRealm(technician) || !record.isDraftMode());
			super.getResponse().addGlobal("draftMode", record == null ? null : record.isDraftMode());
			super.getResponse().setAuthorised(status);
		}
	}

	@Override
	public void load() {
		List<Task> tasks;
		int technicianId;
		boolean mine;
		int recordId;
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (super.getRequest().hasData("recordId"))
			try {
				recordId = super.getRequest().getData("recordId", int.class);
			} catch (Exception e) {
				recordId = -1;
			}
		else
			recordId = -1;

		if (recordId == -1) {
			if (super.getRequest().hasData("mine"))
				try {
					mine = super.getRequest().getData("mine", boolean.class);
				} catch (Exception e) {
					mine = true;
				}
			else
				mine = false;
			tasks = mine ? this.repository.findMyTasks(technicianId) : this.repository.findAvailableTasks(technicianId);
		} else
			tasks = this.repository.findTasksByRecord(recordId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;

		dataset = super.unbindObject(task, "type", "priority", "estimatedDuration");

		super.getResponse().addData(dataset);
	}
}
