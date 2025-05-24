
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
		int recordId;
		MaintenanceRecord record;
		Technician technician;
		boolean status;
		String method = super.getRequest().getMethod();

		if (!super.getRequest().hasData("recordId"))
			status = false;
		else {
			recordId = super.getRequest().getData("recordId", int.class);
			record = this.repository.findRecordById(recordId);
			technician = record == null ? null : record.getTechnician();
			status = record != null && technician != null && super.getRequest().getPrincipal().hasRealm(technician) && record.isDraftMode();

			if (method.equals("POST")) {
				int taskId = super.getRequest().getData("task", int.class);
				Task task = this.repository.findTaskById(taskId);

				if (task == null && taskId != 0)
					status = false;
				else if (task != null && //
					(technician == null || !this.repository.findAllSelectableTasks(technician.getId(), recordId).contains(task)))
					status = false;
			}
		}
		super.getResponse().setAuthorised(status);
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
		int recordId = involves.getMaintenanceRecord().getId();

		tasks = this.repository.findAllSelectableTasks(technicianId, recordId);
		taskChoices = SelectChoices.from(tasks, "id", involves.getTask());

		dataset = super.unbindObject(involves);
		dataset.put("recordId", involves.getMaintenanceRecord().getId());
		dataset.put("task", taskChoices.getSelected().getKey());
		dataset.put("tasks", taskChoices);

		super.getResponse().addData(dataset);
	}
}
