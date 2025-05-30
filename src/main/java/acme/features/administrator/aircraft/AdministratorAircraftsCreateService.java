
package acme.features.administrator.aircraft;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;

@GuiService
public class AdministratorAircraftsCreateService extends AbstractGuiService<Administrator, Aircraft> {

	@Autowired
	private AdministratorAircraftsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Aircraft aircraft;

		aircraft = new Aircraft();

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
