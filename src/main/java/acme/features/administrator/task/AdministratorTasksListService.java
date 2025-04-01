
package acme.features.administrator.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.task.Task;

@GuiService
public class AdministratorTasksListService extends AbstractGuiService<Administrator, Task> {

	@Autowired
	private AdministratorTasksRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Task> tasks;
		int recordId;
		recordId = super.getRequest().getData("recordId", int.class);

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
