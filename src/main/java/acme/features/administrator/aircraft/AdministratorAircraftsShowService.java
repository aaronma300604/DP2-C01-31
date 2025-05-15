
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
public class AdministratorAircraftsShowService extends AbstractGuiService<Administrator, Aircraft> {

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
	public void unbind(final Aircraft aircraft) {
		Dataset dataset;
		SelectChoices choices;
		Collection<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		choices = SelectChoices.from(airlines, "name", aircraft.getAirline());

		dataset = super.unbindObject(aircraft, "model", "registrationNumber", "capacity", "cargo", //
			"active", "details");
		dataset.put("confirmation", false);
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);

		super.getResponse().addData(dataset);
	}
}
