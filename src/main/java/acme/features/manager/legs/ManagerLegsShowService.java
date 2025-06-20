
package acme.features.manager.legs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerLegsShowService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private ManagerLegsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		AirlineManager manager;

		Integer nullValue = super.getRequest().getData("id", Integer.class);
		if (nullValue == null)
			super.getResponse().setAuthorised(false);
		else {
			legId = super.getRequest().getData("id", int.class);
			leg = this.repository.findLeg(legId);

			if (leg == null)
				super.getResponse().setAuthorised(false);
			else {
				manager = leg.getFlight().getManager();
				status = super.getRequest().getPrincipal().hasRealm(manager) || !leg.isDraftMode();
				super.getResponse().setAuthorised(status);
			}
		}
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLeg(id);

		super.getBuffer().addData(leg);
	}

	@Override
	public void unbind(final Leg leg) {
		SelectChoices statusChoices;
		SelectChoices flightsChoices;
		SelectChoices aircraftChoices;
		SelectChoices originChoices;
		SelectChoices destinationChoices;
		Dataset dataset;
		List<Flight> flights;
		List<Aircraft> aircrafts;
		List<Airport> airports;
		int managerId;

		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		if (!leg.isDraftMode()) {
			flights = this.repository.findAllFlights();
			aircrafts = this.repository.findAllAircrafts();
		} else {
			managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			flights = this.repository.findFlightsByManager(managerId);
			aircrafts = this.repository.findActiveAircraftsByManager(managerId);
		}
		flightsChoices = SelectChoices.from(flights, "tag", leg.getFlight());
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());

		airports = this.repository.findAllAirports();
		originChoices = SelectChoices.from(airports, "name", leg.getOrigin());
		destinationChoices = SelectChoices.from(airports, "name", leg.getDestination());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status", "draftMode");
		dataset.put("duration", leg.getDuration());
		dataset.put("statuses", statusChoices);
		dataset.put("flight", flightsChoices.getSelected().getKey());
		dataset.put("flights", flightsChoices);
		dataset.put("aircraft", aircraftChoices.getSelected().getKey());
		dataset.put("aircrafts", aircraftChoices);
		dataset.put("origin", originChoices.getSelected().getKey());
		dataset.put("origins", originChoices);
		dataset.put("destination", destinationChoices.getSelected().getKey());
		dataset.put("destinations", destinationChoices);

		super.getResponse().addData(dataset);
	}
}
