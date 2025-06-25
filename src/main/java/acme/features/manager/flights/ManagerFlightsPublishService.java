
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerFlightsPublishService extends AbstractGuiService<AirlineManager, Flight> {

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

			if (flight == null) {
				manager = null;
				status = false;
			} else {
				manager = flight.getManager();
				status = super.getRequest().getPrincipal().hasRealm(manager) && flight.isDraftMode();
			}

			super.getResponse().setAuthorised(status);
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
	public void bind(final Flight flight) {
		super.bindObject(flight, "tag", "cost", "description", "selfTransfer");
	}

	@Override
	public void validate(final Flight flight) {
		boolean availableCurrency;
		List<String> currencies;
		if (flight.getCost() != null) {
			currencies = this.repository.finAllCurrencies();
			String currency = flight.getCost().getCurrency();
			availableCurrency = currencies.contains(currency);

			super.state(availableCurrency, "cost", "acme.validation.invalid-currency.message");
		}

		boolean canBePublish = false;
		List<Leg> legs = this.repository.findLegsByFlight(flight.getId());
		if (!legs.isEmpty())
			canBePublish = legs.stream().allMatch(l -> !l.isDraftMode());
		super.state(canBePublish, "*", "acme.validation.flight.cant-be-publish.message");

		boolean notPastLegs = true;
		for (Leg leg : legs)
			if (MomentHelper.isBefore(leg.getScheduledDeparture(), MomentHelper.getCurrentMoment()))
				notPastLegs = false;
		super.state(notPastLegs, "*", "acme.validation.manager.flights.past-legs-message");
	}

	@Override
	public void perform(final Flight flight) {
		flight.setDraftMode(false);
		this.repository.save(flight);
	}

	@Override
	public void unbind(final Flight flight) {
		Dataset dataset;

		dataset = super.unbindObject(flight, "tag", "cost", "description", "selfTransfer", "draftMode");
		dataset.put("origin", flight.getOrigin() != null ? flight.getOrigin().getName() : flight.getOrigin());
		dataset.put("destination", flight.getDestination() != null ? flight.getDestination().getName() : flight.getDestination());
		dataset.put("scheduledDeparture", flight.getScheduledDeparture());
		dataset.put("scheduledArrival", flight.getScheduledArrival());
		dataset.put("layovers", flight.getLayovers());

		super.getResponse().addData(dataset);
	}
}
