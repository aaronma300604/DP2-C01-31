
package acme.features.technician.maintenanceRecord;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianMaintenanceRecordsCreateService extends AbstractGuiService<Technician, MaintenanceRecord> {

	@Autowired
	TechnicianMaintenanceRecordsRepository repository;


	@Override
	public void authorise() {
		String method = super.getRequest().getMethod();
		boolean authorised = true;

		if (method.equals("POST")) {
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			Aircraft aircraft = this.repository.findAircraftById(aircraftId);
			List<Aircraft> available = this.repository.findAllAircrafts();

			if (aircraft == null && aircraftId != 0)
				authorised = false;
			else if (aircraft != null && !available.contains(aircraft))
				authorised = false;
		}

		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {
		MaintenanceRecord record;
		Technician technician;

		technician = (Technician) super.getRequest().getPrincipal().getActiveRealm();

		record = new MaintenanceRecord();
		record.setDraftMode(true);
		record.setTechnician(technician);

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

		boolean nextInspectionIsFuture = false;
		boolean availableCurrency;
		List<String> currencies;
		currencies = this.repository.finAllCurrencies();
		String currency;
		Date nextInspection = null;

		currency = super.getRequest().getData("estimatedCost", String.class);
		currency = currency.length() >= 3 ? currency.substring(0, 3).toUpperCase() : currency;
		try {
			nextInspection = super.getRequest().getData("nextInspection", Date.class);
		} catch (Exception e) {
			super.state(false, "*", "acme.validation.invalid-date-format");
		}
		nextInspectionIsFuture = nextInspection != null ? nextInspection.after(MomentHelper.getCurrentMoment()) : false;
		availableCurrency = currencies.contains(currency);

		super.state(availableCurrency, "estimatedCost", "acme.validation.invalid-currency.message");
		super.state(nextInspectionIsFuture, "nextInspection", "acme.validation.next-inspection.create.message");
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
