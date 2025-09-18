
package acme.features.manager.legs;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerLegsPublishService extends AbstractGuiService<AirlineManager, Leg> {

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

			if (leg == null) {
				manager = null;
				status = false;
			} else {
				manager = leg.getFlight().getManager();
				status = super.getRequest().getPrincipal().hasRealm(manager) && leg.isDraftMode();
			}

			if (status && super.getRequest().hasData("flight") && super.getRequest().hasData("aircraft") //
				&& super.getRequest().hasData("origin") && super.getRequest().hasData("destination")) {
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
				if (flight != null)
					status = flights.contains(flight);
				if (aircraft != null)
					status = status && aircrafts.contains(aircraft);
				if (origin != null)
					status = status && airports.contains(origin);
				if (destination != null)
					status = status && airports.contains(destination);
			}

			super.getResponse().setAuthorised(status);
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

		super.bindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "status");
		leg.setFlight(flight);
		leg.setAircraft(aircraft);
		leg.setOrigin(origin);
		leg.setDestination(destination);
	}

	@Override
	public void validate(final Leg leg) {
		Date now = MomentHelper.getCurrentMoment();
		boolean departureInFuture;
		boolean arrivalAfterDeparture;

		if (leg.getScheduledDeparture() != null && leg.getScheduledArrival() != null) {
			if (leg.getScheduledDeparture().after(now))
				departureInFuture = true;
			else
				departureInFuture = false;
			if (leg.getScheduledArrival().after(leg.getScheduledDeparture()))
				arrivalAfterDeparture = true;
			else
				arrivalAfterDeparture = false;
			super.state(departureInFuture, "scheduledDeparture", "acme.validation.leg.scheduledDeparture");
			super.state(arrivalAfterDeparture, "scheduledArrival", "acme.validation.leg.scheduledArrival");

			Date departureWithInterval = MomentHelper.deltaFromMoment(leg.getScheduledDeparture(), 5, ChronoUnit.MINUTES);
			if (MomentHelper.isBefore(leg.getScheduledArrival(), departureWithInterval))
				super.state(false, "scheduledArrival", "acme.validation.leg.dates.difference.message");

			List<Leg> publishedLegs = this.repository.findAllPublishedLegs();
			for (Leg publishedLeg : publishedLegs)
				if (publishedLeg.getAircraft().equals(leg.getAircraft()))
					if (MomentHelper.isBefore(publishedLeg.getScheduledDeparture(), leg.getScheduledArrival()) //
						&& MomentHelper.isAfter(publishedLeg.getScheduledArrival(), leg.getScheduledDeparture()) //
						|| MomentHelper.isEqual(publishedLeg.getScheduledDeparture(), leg.getScheduledDeparture()) && MomentHelper.isEqual(publishedLeg.getScheduledArrival(), leg.getScheduledArrival())) {
						super.state(false, "aircraft", "acme.validation.leg.same.aircraft.message");
						break;
					}

			List<Leg> publishedLegsByFlight = this.repository.findPublishedLegsByFlight(leg.getFlight().getId());
			for (Leg publishedLeg : publishedLegsByFlight)
				if (MomentHelper.isBefore(publishedLeg.getScheduledDeparture(), leg.getScheduledArrival()) //
					&& MomentHelper.isAfter(publishedLeg.getScheduledArrival(), leg.getScheduledDeparture())) {
					super.state(false, "scheduledDeparture", "acme.validation.leg.overlap.message");
					super.state(false, "scheduledArrival", "acme.validation.leg.overlap.message");
					break;
				}
		}

		if (leg.getDestination() != null && leg.getDestination().equals(leg.getOrigin())) {
			super.state(false, "origin", "acme.validation.leg.same.origin");
			super.state(false, "destination", "acme.validation.leg.same.destination");
		}

		List<Leg> orderedLegs = this.repository.findOrderedLegsByFlightAndThisLeg(leg.getFlight().getId(), leg.getId());
		int index = orderedLegs.indexOf(leg);
		if (index != -1 && leg.getFlight().isSelfTransfer() && leg.getDestination() != null && leg.getOrigin() != null) {
			if (index > 0) {
				Leg previous = orderedLegs.get(index - 1);
				if (!leg.getOrigin().equals(previous.getDestination()))
					super.state(false, "origin", "acme.validation.leg.origin");
			}

			if (index < orderedLegs.size() - 1) {
				Leg next = orderedLegs.get(index + 1);
				if (!leg.getDestination().equals(next.getOrigin()))
					super.state(false, "destination", "acme.validation.leg.destination");
			}
		}
	}

	@Override
	public void perform(final Leg leg) {
		leg.setDraftMode(false);
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
