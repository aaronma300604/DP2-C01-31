
package acme.features.technician.task;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianTasksPublishService extends AbstractGuiService<Technician, Task> {

	@Autowired
	TechnicianTasksRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int taskId;
		Task task;
		Technician technician;
		if (!super.getRequest().hasData("id"))
			status = false;
		else
			try {
				taskId = super.getRequest().getData("id", int.class);
				task = this.repository.findTask(taskId);
				technician = task == null ? null : task.getTechnician();
				status = technician != null && task.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);
			} catch (Exception e) {
				status = false;
			}

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
		super.bindObject(task, "type", "description", "priority", "estimatedDuration");
	}

	@Override
	public void validate(final Task task) {

		super.state(task.isDraftMode(), "*", "acme.validation.task.cant-be-publish.message");
	}

	@Override
	public void perform(final Task task) {
		task.setDraftMode(false);
		this.repository.save(task);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(TaskType.class, task.getType());

		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration", "draftMode");
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);
	}
}
