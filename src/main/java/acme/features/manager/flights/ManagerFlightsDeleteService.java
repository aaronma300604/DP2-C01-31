
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.passenger.Passenger;
import acme.features.manager.legs.ManagerLegsDeleteService;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerFlightsDeleteService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private ManagerFlightsRepository	repository;

	@Autowired
	private ManagerLegsDeleteService	legsDeleteService;


	@Override
	public void authorise() {
		boolean status;
		int flightId;
		Flight flight;
		AirlineManager manager;

		flightId = super.getRequest().getData("id", int.class);
		flight = this.repository.findFlight(flightId);
		manager = flight == null ? null : flight.getManager();
		status = flight != null && flight.isDraftMode() && super.getRequest().getPrincipal().hasRealm(manager);

		super.getResponse().setAuthorised(status);
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
	public void bind(final Flight flight) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(flight, "tag", "cost", "description", "selfTransfer");
		flight.setAirline(airline);
	}

	@Override
	public void validate(final Flight flight) {
		;
	}

	@Override
	public void perform(final Flight flight) {
		List<Passenger> passengers;
		List<Leg> legs;

		passengers = this.repository.findPassengersByFlight(flight.getId());
		legs = this.repository.findLegsByFlight(flight.getId());
		this.repository.deleteAll(passengers);
		legs.stream().forEach(l -> this.legsDeleteService.perform(l));
		this.repository.delete(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;
		SelectChoices choices;
		Airline airline;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		airline = this.repository.findAirlineByManager(managerId);
		choices = SelectChoices.from(List.of(airline), "name", flight.getAirline());

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
