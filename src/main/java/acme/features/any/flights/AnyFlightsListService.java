
package acme.features.any.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;

@GuiService
public class AnyFlightsListService extends AbstractGuiService<Any, Flight> {

	@Autowired
	private AnyFlightsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Flight> flights;

		flights = this.repository.findFlightsPublished();

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost");
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("origin", flight.getOrigin() != null ? flight.getOrigin().getName() : null);
		dataset.put("destination", flight.getDestination() != null ? flight.getDestination().getName() : null);

		super.getResponse().addData(dataset);
	}
}
