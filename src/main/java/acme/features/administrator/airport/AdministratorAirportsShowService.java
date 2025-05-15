
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
public class AdministratorAirportsShowService extends AbstractGuiService<Administrator, Airport> {

	@Autowired
	private AdministratorAirportsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int airportId;
		Airport airport;

		airportId = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(airportId);
		status = airport != null;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Airport airport;
		int id;

		id = super.getRequest().getData("id", int.class);
		airport = this.repository.findAirportById(id);

		super.getBuffer().addData(airport);
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
