
package acme.features.any.flights;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;

@GuiService
public class AnyFlightsShowService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Flight flight;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);
		status = flight != null && !flight.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		int id;

		id = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlightById(id);

		super.getBuffer().addData(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "selfTransfer", "cost", "description");
		dataset.put("manager", flight.getManager().getIdentity().getName());
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("origin", flight.getOrigin() != null ? flight.getOrigin().getName() : null);
		dataset.put("destination", flight.getDestination() != null ? flight.getDestination().getName() : null);
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}
}
