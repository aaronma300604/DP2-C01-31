
package acme.features.manager.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerLegsListService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private ManagerLegsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Leg> legs;
		Airline airline;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airline = this.repository.findAirlineByManager(managerId);
		legs = this.repository.findMyLegs(managerId, airline.getId());

		super.getBuffer().addData(legs);
	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival");
		dataset.put("origin", leg.getOrigin().getName());
		dataset.put("destination", leg.getDestination().getName());

		super.getResponse().addData(dataset);
	}
}
