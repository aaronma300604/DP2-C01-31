
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;
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
		boolean mine;

		technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();
		mine = super.getRequest().getData("mine", boolean.class);

		records = mine ? this.repository.findMyRecords(technicianId) : this.repository.findAvailableRecords(technicianId);

		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		assert record != null;
		Dataset dataset;

		dataset = super.unbindObject(record, "id", "date", "maintenanceStatus", "nextInspection");
		dataset.put("aircraft", record.getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}
