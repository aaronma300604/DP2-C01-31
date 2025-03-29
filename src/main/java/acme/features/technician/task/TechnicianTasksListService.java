
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
import acme.entities.task.TaskType;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianTasksListService extends AbstractGuiService<Technician, Task> {

	@Autowired
	private TechnicianTasksRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Task> tasks;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		tasks = this.repository.findMyTasks(technicianId);

		super.getBuffer().addData(tasks);
	}

	@Override
	public void unbind(final Task task) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(TaskType.class, task.getType());
		dataset = super.unbindObject(task, "type", "description", "priority", "estimatedDuration");
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);
	}
}
