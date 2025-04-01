
package acme.features.administrator.maintenanceRecord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@GuiService
public class AdministratorMaintenanceRecordsListService extends AbstractGuiService<Administrator, MaintenanceRecord> {

	@Autowired
	private AdministratorMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<MaintenanceRecord> records;

		records = this.repository.findAvailableRecords();

		super.getBuffer().addData(records);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;

		dataset = super.unbindObject(record, "id", "date", "maintenanceStatus", "nextInspection");
		dataset.put("aircraft", record.getAircraft().getRegistrationNumber());

		super.getResponse().addData(dataset);
	}
}
