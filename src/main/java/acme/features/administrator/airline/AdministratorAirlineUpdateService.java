
package acme.features.administrator.airline;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.airline.AirlineType;

@GuiService
public class AdministratorAirlineUpdateService extends AbstractGuiService<Administrator, Airline> {

	@Autowired
	private AdministratorAirlineRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(airlineId);
		status = airline != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airline airline;
		int id;

		id = super.getRequest().getData("id", int.class);
		airline = this.repository.findAirlineById(id);

		super.getBuffer().addData(airline);
	}

	@Override
	public void bind(final Airline airline) {

		super.bindObject(airline, "name", "iata", "website", "type", "foundation", //
			"email", "phone");

	}

	@Override
	public void validate(final Airline airline) {
		boolean uniqueIata;
		Airline existingAirline;

		existingAirline = this.repository.findAirlineByIata(airline.getIata());
		uniqueIata = existingAirline == null || existingAirline.equals(airline);
		super.state(uniqueIata, "iata", "acme.validation.airline.duplicated-iata.message");

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airline airline) {
		this.repository.save(airline);
	}

	@Override
	public void unbind(final Airline airline) {
		Dataset dataset;
		SelectChoices typeChoices;

		typeChoices = SelectChoices.from(AirlineType.class, airline.getType());

		dataset = super.unbindObject(airline, "name", "iata", "website", "type", "foundation", "email", "phone");
		dataset.put("confirmation", false);
		dataset.put("type", typeChoices.getSelected().getKey());
		dataset.put("types", typeChoices);

		super.getResponse().addData(dataset);
	}
}
