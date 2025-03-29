
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
public class TechnicianInvolvesCreateService extends AbstractGuiService<Technician, Involves> {

	@Autowired
	TechnicianInvolvesRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Involves involves;
		MaintenanceRecord record;
		int recordId;

		recordId = super.getRequest().getData("recordId", int.class);
		record = this.repository.findRecordById(recordId);

		involves = new Involves();
		involves.setMaintenanceRecord(record);

		super.getBuffer().addData(involves);
	}

	@Override
	public void bind(final Involves involves) {
		int taskId;
		Task task;

		taskId = super.getRequest().getData("task", int.class);

		task = this.repository.findTaskById(taskId);

		involves.setTask(task);
		super.bindObject(involves);

	}

	@Override
	public void validate(final Involves involves) {
		;
	}

	@Override
	public void perform(final Involves involves) {
		this.repository.save(involves);
	}

	@Override
	public void unbind(final Involves involves) {
		Dataset dataset;
		SelectChoices taskChoices;
		List<Task> tasks;
		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		tasks = this.repository.findAllSelectableTasks(technicianId);
		taskChoices = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("recordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);

		super.getResponse().addData(dataset);
	}
}
