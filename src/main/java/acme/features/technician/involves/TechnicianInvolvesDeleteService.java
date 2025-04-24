
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

		recordId = super.getRequest().getData("recordId", int.class);
		record = this.repository.findRecordById(recordId);
		technician = record.getTechnician();
		status = record != null && technician != null && super.getRequest().getPrincipal().hasRealm(technician);
		System.out.println("Authorised");
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
		System.out.println("Loaded");
		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Involves involves) {
		System.out.println("binding...");
	}

	@Override
	public void validate(final Involves involves) {
		System.out.println("Validating...");
		List<Task> tasks;
		tasks = this.repository.findValidTasksToUnlink(involves.getMaintenanceRecord().getId());

		int taskId = super.getRequest().getData("task", int.class);
		Task task = this.repository.findTaskById(taskId);
		System.out.println("Validated");
		super.state(task != null && tasks.contains(task), "task", "technician.involves.form.error.no-task-to-unlink");
	}

	@Override
	public void perform(final Involves involves) {
		System.out.println("Performing...");
		int taskId = super.getRequest().getData("task", int.class);

		Task task = this.repository.findTaskById(taskId);
		MaintenanceRecord maintenanceRecord = involves.getMaintenanceRecord();
		System.out.println("Performed");
		this.repository.delete(this.repository.findInvolvesByMaintenanceRecordAndTask(maintenanceRecord, task));
	}

	@Override
	public void unbind(final Involves involves) {
		System.out.println("Unbinding...");
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
