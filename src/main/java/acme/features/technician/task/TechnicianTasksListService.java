
package acme.features.technician.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;
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
		boolean mine;
		int recordId;
		recordId = super.getRequest().getData("recordId", int.class);
		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		if (recordId == -1) {
			mine = super.getRequest().getData("mine", boolean.class);
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
