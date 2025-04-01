
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.Involves;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianTasksDeleteService extends AbstractGuiService<Technician, Task> {

	@Autowired
	TechnicianTasksRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Task task;
		Technician technician;

		taskId = super.getRequest().getData("id", int.class);
		task = this.repository.findTask(taskId);
		technician = task == null ? null : task.getTechnician();
		status = task != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		Task task;
		int id;
		id = super.getRequest().getData("id", int.class);
		task = this.repository.findTask(id);

		super.getBuffer().addData(task);
	}

	@Override
	public void bind(final Task task) {
		assert task != null;
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {
		assert task != null;
	}

	@Override
	public void perform(final Task task) {
		assert task != null;
		List<Involves> involves;
		involves = this.repository.findInvolvesByTask(task.getId());

		if (involves != null && !involves.isEmpty())
			this.repository.deleteAll(involves);

		this.repository.delete(task);
	}

	@Override
	public void unbind(final Task task) {
		assert task != null;
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);
	}
}
