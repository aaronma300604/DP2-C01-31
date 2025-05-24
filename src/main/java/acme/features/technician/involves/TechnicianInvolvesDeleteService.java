
package acme.features.technician.involves;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianInvolvesDeleteService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		int recordId;
		MaintenanceRecord record;
		Technician technician;
		boolean status;

		if (!super.getRequest().hasData("recordId"))
			status = false;
		else {
			recordId = super.getRequest().getData("recordId", int.class);
			record = this.repository.findRecordById(recordId);
			technician = record == null ? null : record.getTechnician();
			status = technician != null && super.getRequest().getPrincipal().hasRealm(technician) && record.isDraftMode();

			if (super.getRequest().hasData("task")) {
				int taskId = super.getRequest().getData("task", int.class);
				Task task = this.repository.findTaskById(taskId);
				List<Task> available = this.repository.findValidTasksToUnlink(recordId);

				if (task == null && taskId != 0)
					status = false;
				else if (task != null && !available.contains(task))
					status = false;
			}
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {

		Involves object;
		Integer maintenanceRecordId;
		MaintenanceRecord maintenanceRecord;

		maintenanceRecordId = super.getRequest().getData("recordId", int.class);
		maintenanceRecord = this.repository.findRecordById(maintenanceRecordId);

		object = new Involves();
		object.setMaintenanceRecord(maintenanceRecord);
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Involves involves) {
	}

	@Override
	public void validate(final Involves involves) {
		List<Task> tasks;
		tasks = this.repository.findValidTasksToUnlink(involves.getMaintenanceRecord().getId());

		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.repository.findTaskById(taskId);
		super.state(task != null && tasks.contains(task), "task", "technician.involves.form.error.no-task-to-unlink");
	}

	@Override
	public void perform(final Involves involves) {
		int taskId = super.getRequest().getData("task", int.class);

		Task task = this.repository.findTaskById(taskId);
		MaintenanceRecord maintenanceRecord = involves.getMaintenanceRecord();
		this.repository.delete(this.repository.findInvolvesByMaintenanceRecordAndTask(maintenanceRecord, task));
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		List<Task> tasks;
		int recordId = involves.getMaintenanceRecord().getId();

		tasks = this.repository.findValidTasksToUnlink(recordId);
		taskChoices = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("recordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);

		super.getResponse().addData(dataset);
	}

}
