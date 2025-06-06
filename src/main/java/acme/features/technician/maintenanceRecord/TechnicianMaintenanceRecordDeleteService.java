
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianMaintenanceRecordDeleteService extends AbstractGuiService<Technician, MaintenanceRecord> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private TechnicianMaintenanceRecordsRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int mrId;
		MaintenanceRecord mr;
		Technician technician;

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			mrId = super.getRequest().getData("id", int.class);
			mr = this.repository.findRecord(mrId);

			technician = mr == null ? null : mr.getTechnician();
			status = mr != null && mr.isDraftMode() && this.getRequest().getPrincipal().hasRealm(technician);

			if (super.getRequest().hasData("aircraft")) {
				int aircraftId = super.getRequest().getData("aircraft", int.class);
				Aircraft aircraft = this.repository.findAircraftById(aircraftId);
				List<Aircraft> available = this.repository.findAllAircrafts();

				if (aircraft == null && aircraftId != 0)
					status = false;
				else if (aircraft != null && !available.contains(aircraft))
					status = false;
			}
		}

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		MaintenanceRecord maintenanceRecord;
		int id;

		id = super.getRequest().getData("id", int.class);
		maintenanceRecord = this.repository.findRecord(id);

		super.getBuffer().addData(maintenanceRecord);
	}

	@Override
	public void bind(final MaintenanceRecord maintenanceRecord) {

		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(maintenanceRecord, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes");

		maintenanceRecord.setAircraft(aircraft);
	}
	@Override
	public void validate(final MaintenanceRecord maintenanceRecord) {
		;
	}
	@Override
	public void perform(final MaintenanceRecord maintenanceRecord) {
		List<Involves> involves;

		involves = this.repository.findInvolvesByMaintenanceRecord(maintenanceRecord.getId());
		this.repository.deleteAll(involves);
		this.repository.delete(maintenanceRecord);
	}
	@Override
	public void unbind(final MaintenanceRecord maintenanceRecord) {
		Dataset dataset;
		SelectChoices aircraftChoices;
		SelectChoices statusChoices;
		Integer taskCount;

		List<Aircraft> aircrafts;

		taskCount = this.repository.countAvailableTasks(maintenanceRecord.getTechnician().getId());
		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", maintenanceRecord.getAircraft());
		statusChoices = SelectChoices.from(MaintenanceStatus.class, maintenanceRecord.getMaintenanceStatus());

		dataset = super.unbindObject(maintenanceRecord, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("maintenanceStatuses", statusChoices);
		dataset.put("aircraft", !aircrafts.isEmpty() ? aircraftChoices.getSelected().getKey() : "No aircrafts available");
		dataset.put("emptyAircrafts", aircrafts.isEmpty());
		dataset.put("emptyTasks", taskCount <= 0);
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);

	}

}
