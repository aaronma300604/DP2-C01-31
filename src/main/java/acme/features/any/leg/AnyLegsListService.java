
package acme.features.any.leg;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@GuiService
public class AnyLegsListService extends AbstractGuiService<Any, Leg> {

	@Autowired
	private AnyLegsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Flight flight;

		masterId = super.getRequest().getData("masterId", int.class);
		flight = this.repository.findFlightById(masterId);
		status = flight != null && !flight.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<Leg> legs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		legs = this.repository.findLegsByFlight(masterId);

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("duration", leg.getDuration());
		dataset.put("origin", leg.getOrigin().getName());
		dataset.put("destination", leg.getDestination().getName());
		super.addPayload(dataset, leg, "flightNumber");

		super.getResponse().addData(dataset);
	}
}
