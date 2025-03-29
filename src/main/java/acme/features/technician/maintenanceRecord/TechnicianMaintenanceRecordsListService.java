
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
public class TechnicianMaintenanceRecordsListService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	private TechnicianMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<MaintenanceRecord> records;
		int technicianId;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		records = this.repository.findMyRecords(technicianId);

		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices statusChoices;
		SelectChoices aircraftChoices;
		List<Aircraft> aircrafts;

		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		statusChoices = SelectChoices.from(MaintenanceStatus.class, record.getMaintenanceStatus());

		dataset = super.unbindObject(record, "date", "maintenanceStatus", "nextInspection");
		dataset.put("maintenanceStatuses", statusChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}
}
