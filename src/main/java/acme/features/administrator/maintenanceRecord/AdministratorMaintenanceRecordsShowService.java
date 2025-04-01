
package acme.features.administrator.maintenanceRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordsShowService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintenanceRecord record;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecord(recordId);
		status = super.getRequest().getPrincipal().hasRealmOfType(Administrator.class) || record != null && !record.isDraftMode();

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

		dataset = super.unbindObject(record, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", record.getAircraft());

		super.getResponse().addData(dataset);
	}
}
