
package acme.features.administrator.aircraft;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;

@GuiService
public class AdministratorAircraftsUpdateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int aircraftId;
		Aircraft aircraft;

		aircraftId = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		status = aircraft != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Aircraft aircraft;
		int id;

		id = super.getRequest().getData("id", int.class);
		aircraft = this.repository.findAircraftById(id);

		super.getBuffer().addData(aircraft);
	}

	@Override
	public void bind(final Aircraft aircraft) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(aircraft, "model", "registrationNumber", "capacity", "cargo", "active", //
			"details");
		aircraft.setAirline(airline);
	}

	@Override
	public void validate(final Aircraft aircraft) {
		boolean canBeDisable;
		List<Leg> legsByAircraft;

		Aircraft oldAircraft = this.repository.findAircraftById(aircraft.getId());
		if (!aircraft.isActive() && oldAircraft.isActive()) {
			Date date = MomentHelper.getCurrentMoment();
			legsByAircraft = this.repository.findLegsByAircraft(aircraft.getId(), date);
			canBeDisable = legsByAircraft.isEmpty();
			super.state(canBeDisable, "*", "acme.validation.aircraft.cant-be-disable");
		}

		boolean canChangeAirline;

		canChangeAirline = this.repository.findDraftModeLegsByAircraft(aircraft.getId()).isEmpty();
		super.state(canChangeAirline, "*", "acme.validation.aircraft.cant-change-airline");

		boolean uniqueRegistrationNumber;
		Aircraft existingAircraft;

		existingAircraft = this.repository.findAircraftsByRegistrationNumber(aircraft.getRegistrationNumber());
		uniqueRegistrationNumber = existingAircraft == null || existingAircraft.equals(aircraft);
		super.state(uniqueRegistrationNumber, "registrationNumber", "acme.validation.aircraft.duplicated-registration-number.message");

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Aircraft aircraft) {
		this.repository.save(aircraft);
	}

	@Override
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargo", "active", //
			"details");

		dataset.put("confirmation", false);
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}
}
