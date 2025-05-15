
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
public class ManagerLegsUpdateService extends AbstractGuiService<AirlineManager, Leg> {

	@Autowired
	private ManagerLegsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int legId;
		Leg leg;
		AirlineManager manager;

		legId = super.getRequest().getData("id", int.class);
		leg = this.repository.findLeg(legId);

		if (leg == null) {
			manager = null;
			status = false;
		} else {
			manager = leg.getManager();
			status = super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();
		}

		if (status) {
			int flightId = super.getRequest().getData("flight", int.class);
			int aircraftId = super.getRequest().getData("aircraft", int.class);
			int originId = super.getRequest().getData("origin", int.class);
			int destinationId = super.getRequest().getData("destination", int.class);
			Flight flight = this.repository.findFlightById(flightId);
			Aircraft aircraft = this.repository.findAircraftById(aircraftId);
			Airport origin = this.repository.findAirportById(originId);
			Airport destination = this.repository.findAirportById(destinationId);

			List<Flight> flights = this.repository.findFlightsByManager(manager.getId());
			List<Aircraft> aircrafts = this.repository.findActiveAircraftsByManager(manager.getId());
			List<Airport> airports = this.repository.findAllAirports();
			if (flight != null && aircraft != null //
				&& origin != null && destination != null)
				status = flights.contains(flight) && aircrafts.contains(aircraft) //
					&& airports.contains(origin) && airports.contains(destination);
		}

		super.getResponse().setAuthorised(status);
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
	public void bind(final Leg leg) {
		int flightId;
		int aircraftId;
		int originId;
		int destinationId;
		Flight flight;
		Aircraft aircraft;
		Airport origin;
		Airport destination;

		flightId = super.getRequest().getData("flight", int.class);
		flight = this.repository.findFlightById(flightId);
		aircraftId = super.getRequest().getData("aircraft", int.class);
		aircraft = this.repository.findAircraftById(aircraftId);
		originId = super.getRequest().getData("origin", int.class);
		origin = this.repository.findAirportById(originId);
		destinationId = super.getRequest().getData("destination", int.class);
		destination = this.repository.findAirportById(destinationId);

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status");
		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setOrigin(origin);
		leg.setDestination(destination);
	}

	@Override
	public void validate(final Leg leg) {
		;
	}

	@Override
	public void perform(final Leg leg) {
		this.repository.save(leg);
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
		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flights = this.repository.findFlightsByManager(managerId);
		flightsChoices = SelectChoices.from(flights, "tag", leg.getFlight());
		aircrafts = this.repository.findActiveAircraftsByManager(managerId);
		aircraftChoices = SelectChoices.from(aircrafts, "registrationNumber", leg.getAircraft());
		airports = this.repository.findAllAirports();
		originChoices = SelectChoices.from(airports, "name", leg.getOrigin());
		destinationChoices = SelectChoices.from(airports, "name", leg.getDestination());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status", "draftMode");
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
