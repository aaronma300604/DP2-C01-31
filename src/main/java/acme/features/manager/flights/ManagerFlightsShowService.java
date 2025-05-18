
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerFlightsShowService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private ManagerFlightsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		AirlineManager manager;

		Integer nullValue = super.getRequest().getData("id", Integer.class);
		if (nullValue == null)
			super.getResponse().setAuthorised(false);
		else {
			flightId = super.getRequest().getData("id", int.class);
			flight = this.repository.findFlight(flightId);

			if (flight == null)
				super.getResponse().setAuthorised(false);
			else {
				manager = flight.getManager();
				status = super.getRequest().getPrincipal().hasRealm(manager) || !flight.isDraftMode();

				super.getResponse().setAuthorised(status);
			}
		}
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlight(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices choices;
		List<Airline> airline;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		if (!flight.isDraftMode())
			airline = this.repository.findAllAirline();
		else
			airline = List.of(this.repository.findAirlineByManager(managerId));
		choices = SelectChoices.from(airline, "name", flight.getAirline());

		dataset = super.unbindObject(flight, "tag", "cost", "description", "selfTransfer", "draftMode");
		dataset.put("airline", choices.getSelected().getKey());
		dataset.put("airlines", choices);
		dataset.put("origin", flight.getOrigin() != null ? flight.getOrigin().getName() : flight.getOrigin());
		dataset.put("destination", flight.getDestination() != null ? flight.getDestination().getName() : flight.getDestination());
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}
}
