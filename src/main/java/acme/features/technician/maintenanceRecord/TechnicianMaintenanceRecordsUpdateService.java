
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
import acme.realms.employee.Technician;

@GuiService
public class TechnicianMaintenanceRecordsUpdateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int recordId;
		MaintenanceRecord record;
		Technician technician;

		recordId = super.getRequest().getData("id", int.class);
		record = this.repository.findRecord(recordId);
		technician = record == null ? null : record.getTechnician();
		status = record != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

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
	public void bind(final MaintenanceRecord record) {
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);

		super.bindObject(record, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes");
		record.setAircraft(aircraft);
	}

	@Override
	public void validate(final MaintenanceRecord record) {
		boolean confirmation;
		boolean nextInspectionIsAfterDate;
		boolean availableCurrency;

		List<String> currencies;
		currencies = this.repository.finAllCurrencies();
		String currency;
		Date date;
		Date nextInspection;

		currency = super.getRequest().getData("estimatedCost", String.class).substring(0, 3).toUpperCase();
		date = super.getRequest().getData("date", Date.class);
		nextInspection = super.getRequest().getData("nextInspection", Date.class);
		nextInspectionIsAfterDate = nextInspection.after(date);
		confirmation = super.getRequest().getData("confirmation", boolean.class);
		availableCurrency = currencies.contains(currency);

		super.state(availableCurrency, "estimatedCost", "acme.validation.invalid-currency.message");
		super.state(nextInspectionIsAfterDate, "nextInspection", "acme.validation.next-inspection.update.message");
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final MaintenanceRecord record) {
		this.repository.save(record);
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
		dataset.put("confirmation", false);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("maintenanceStatuses", statusChoices);

		super.getResponse().addData(dataset);
	}

}
