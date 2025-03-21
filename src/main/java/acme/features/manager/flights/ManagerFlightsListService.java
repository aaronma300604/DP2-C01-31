
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerFlightsListService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private ManagerFlightsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Flight> flights;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findMyFlights(managerId);

		super.getBuffer().addData(flights);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost");
		dataset.put("airlineName", flight.getAirline().getName());
		dataset.put("origin", flight.getOrigin() != null ? flight.getOrigin().getName() : flight.getOrigin());
		dataset.put("destination", flight.getDestination() != null ? flight.getDestination().getName() : flight.getDestination());

		super.getResponse().addData(dataset);
	}
}
