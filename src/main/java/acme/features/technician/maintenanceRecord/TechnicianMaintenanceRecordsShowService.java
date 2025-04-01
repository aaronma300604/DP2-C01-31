
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianMaintenanceRecordsShowService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintenanceRecord record;
		Technician technician;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecord(recordId);
		technician = record == null ? null : record.getTechnician();
		status = technician != null && super.getRequest().getPrincipal().hasRealm(technician) || record != null && !record.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		MaintenanceRecord record;
		int id;

		id = super.getRequest().getData("id", int.class);
		record = this.repository.findRecord(id);

		super.getBuffer().addData(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices aircraftChoices;
		SelectChoices statusChoices;

		List<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		statusChoices = SelectChoices.from(MaintenanceStatus.class, record.getMaintenanceStatus());

		dataset = super.unbindObject(record, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("maintenanceStatuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}
}
