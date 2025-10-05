
package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.employee.AirlineManager;

@GuiService
public class AuthenticatedManagerUpdateService extends AbstractGuiService<Authenticated, AirlineManager> {

	@Autowired
	private AuthenticatedManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AirlineManager manager;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		manager = this.repository.findManagerByUserAccountId(userAccountId);

		super.getBuffer().addData(manager);
	}

	@Override
	public void bind(final AirlineManager manager) {
		int airlineId;
		Airline airline;

		airlineId = super.getRequest().getData("airline", int.class);
		airline = this.repository.findAirlineById(airlineId);

		super.bindObject(manager, "experience", "birth", "link");
		manager.setAirline(airline);
	}

	@Override
	public void validate(final AirlineManager manager) {
		AirlineManager oldManager = this.repository.findManager(manager.getId());
		if (manager.getAirline() != null && !manager.getAirline().equals(oldManager.getAirline())) {
			boolean canChange;
			canChange = this.repository.findDraftModeLegsByManager(manager.getId()).isEmpty();

			super.state(canChange, "*", "acme.validation.manager.cantChange");
		}
	}

	@Override
	public void perform(final AirlineManager manager) {
		this.repository.save(manager);

	}

	@Override
	public void unbind(final AirlineManager manager) {
		Dataset dataset;
		SelectChoices airlineChoices;
		List<Airline> airlines;

		airlines = this.repository.findAllAirlines();
		airlineChoices = SelectChoices.from(airlines, "name", manager.getAirline());

		dataset = super.unbindObject(manager, "experience", "birth", "link");
		dataset.put("airline", airlineChoices.getSelected().getKey());
		dataset.put("airlines", airlineChoices);

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}
}
