
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

		if (!super.getRequest().hasData("id"))
			status = false;
		else {
			recordId = super.getRequest().getData("id", int.class);
			record = this.repository.findRecord(recordId);
			technician = record == null ? null : record.getTechnician();
			status = record != null && record.isDraftMode() && super.getRequest().getPrincipal().hasRealm(technician);

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

		boolean nextInspectionIsAfterDate;
		boolean availableCurrency;

		List<String> currencies;
		currencies = this.repository.finAllCurrencies();
		String currency;
		Date date = null;
		Date nextInspection = null;

		currency = super.getRequest().getData("estimatedCost", String.class);
		currency = currency.length() >= 3 ? currency.substring(0, 3).toUpperCase() : currency;

		try {
			date = super.getRequest().getData("date", Date.class);
			nextInspection = super.getRequest().getData("nextInspection", Date.class);
		} catch (Exception e) {
			super.state(false, "*", "acme.validation.invalid-date-format");
		}

		nextInspectionIsAfterDate = nextInspection != null && date != null ? nextInspection.after(date) : false;
		availableCurrency = currencies.contains(currency);

		super.state(availableCurrency, "estimatedCost", "acme.validation.invalid-currency.message");
		super.state(nextInspectionIsAfterDate, "nextInspection", "acme.validation.next-inspection.update.message");
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
		Integer taskCount;

		List<Aircraft> aircrafts;

		taskCount = this.repository.countAvailableTasks(record.getTechnician().getId());
		aircrafts = this.repository.findAllAircrafts();
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", record.getAircraft());
		statusChoices = SelectChoices.from(MaintenanceStatus.class, record.getMaintenanceStatus());

		dataset = super.unbindObject(record, "date", "maintenanceStatus", "nextInspection", "estimatedCost", "notes", "draftMode");
		dataset.put("maintenanceStatuses", statusChoices);
		dataset.put("aircraft", !aircrafts.isEmpty() ? aircraftChoices.getSelected().getKey() : "No aircrafts available");
		dataset.put("emptyAircrafts", aircrafts.isEmpty());
		dataset.put("emptyTasks", taskCount <= 0);
		dataset.put("aircrafts", aircraftChoices);

		super.getResponse().addData(dataset);
	}

}
