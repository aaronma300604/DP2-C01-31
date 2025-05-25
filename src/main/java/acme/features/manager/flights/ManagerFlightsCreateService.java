
package acme.features.manager.flights;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerFlightsCreateService extends AbstractGuiService<AirlineManager, Flight> {

	@Autowired
	private ManagerFlightsRepository repository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();
		if (method.equals("POST")) {
			int airlineId = super.getRequest().getData("airline", int.class);
			int managerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Airline airline = this.repository.findAirlineById(airlineId);
			AirlineManager manager = this.repository.findManagerById(managerId);
			if (airline != null)
				status = manager.getAirline().getId() == airlineId;
		}
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Flight flight;
		AirlineManager manager;

		manager = (AirlineManager) super.getRequest().getPrincipal().getActiveRealm();

		flight = new Flight();
		flight.setDraftMode(true);
		flight.setManager(manager);

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
		boolean availableCurrency;
		List<String> currencies;
		currencies = this.repository.finAllCurrencies();
		String currency;
		currency = super.getRequest().getData("cost", String.class);
		currency = currency.length() >= 3 ? currency.substring(0, 3).toUpperCase() : currency;
		availableCurrency = currencies.contains(currency);

		super.state(availableCurrency, "cost", "acme.validation.invalid-currency.message");
	}

	@Override
	public void perform(final Flight flight) {
		this.repository.save(flight);
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
