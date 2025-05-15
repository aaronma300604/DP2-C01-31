
package acme.features.administrator.airport;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.airport.OperationalScope;

@GuiService
public class AdministratorAirportsCreateService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Airport airport;

		airport = new Airport();

		super.getBuffer().addData(airport);
	}

	@Override
	public void bind(final Airport airport) {
		super.bindObject(airport, "name", "iata", "operationalScope", "city", "country", //
			"website", "email", "phone");
	}

	@Override
	public void validate(final Airport airport) {
		boolean uniqueIata;
		Airport existingAirport;

		existingAirport = this.repository.findAirportByIATACode(airport.getIata());
		uniqueIata = existingAirport == null || existingAirport.equals(airport);
		super.state(uniqueIata, "iata", "acme.validation.airport.duplicated-iata.message");

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Airport airport) {
		this.repository.save(airport);
	}

	@Override
	public void unbind(final Airport airport) {
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(OperationalScope.class, airport.getOperationalScope());

		dataset = super.unbindObject(airport, "name", "iata", "operationalScope", "city", //
			"country", "website", "email", "phone");
		dataset.put("operationalScopes", choices);
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
