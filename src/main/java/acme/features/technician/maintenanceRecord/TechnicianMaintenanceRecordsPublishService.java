
package acme.features.technician.maintenanceRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianMaintenanceRecordsPublishService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
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
	public void bind(final MaintenanceRecord record) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(record, "maintenanceStatus", "nextInspection", "estimatedCost", "notes");
		record.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		boolean canBePublished = false;
		boolean nextInspectionIsAfterDate;
		boolean availableCurrency;

		List<Task> tasks;
		List<String> currencies;

		tasks = this.repository.findTasksByRecord(record.getId());
		currencies = this.repository.finAllCurrencies();
		String currency;
		Date date;
		Date nextInspection;

		currency = super.getRequest().getData("estimatedCost", String.class).substring(0, 3).toUpperCase();
		date = super.getRequest().getData("date", Date.class);
		nextInspection = super.getRequest().getData("nextInspection", Date.class);

		nextInspectionIsAfterDate = nextInspection.after(date);
		availableCurrency = currencies.contains(currency);
		if (!tasks.isEmpty())
			canBePublished = tasks.stream().allMatch(t -> !t.isDraftMode());

		super.state(canBePublished, "*", "acme.validation.maintenance-record.cant-be-publish.message");
		super.state(availableCurrency, "estimatedCost", "acme.validation.invalid-currency.message");
		super.state(nextInspectionIsAfterDate, "nextInspection", "acme.validation.next-inspection.update.message");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		record.setDraftMode(false);
		this.repository.save(record);
	}

	@Override
	public void unbind(final MaintenanceRecord record) {
		Dataset dataset;
		SelectChoices aircraftChoices;
		SelectChoices statusChoices;
		Aircraft aircraft;
		int aircraftId;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		aircraftChoices = SelectChoices.from(List.of(aircraft), "registrationNumber", record.getAircraft());
		statusChoices = SelectChoices.from(MaintenanceStatus.class, record.getMaintenanceStatus());

		dataset = super.unbindObject(record, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("maintenanceStatuses", statusChoices);

		super.getResponse().addData(dataset);
	}

}
