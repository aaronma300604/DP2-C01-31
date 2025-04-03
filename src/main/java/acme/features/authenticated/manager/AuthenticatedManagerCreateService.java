
package acme.features.authenticated.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.RandomHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.Airline;
import acme.realms.employee.AirlineManager;

@GuiService
public class AuthenticatedManagerCreateService extends AbstractGuiService<Authenticated, AirlineManager> {

	@Autowired
	private AuthenticatedManagerRepository repository;


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(AirlineManager.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		AirlineManager manager;
		int userAccountId;
		UserAccount userAccount;
		AirlineManager extremelyRareToHappenCheckManager;
		String employeeCode;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		manager = new AirlineManager();
		manager.setUserAccount(userAccount);

		do {
			employeeCode = "";
			StringBuilder codeBuilder;
			codeBuilder = new StringBuilder();
			codeBuilder.append(this.getInitials(manager));
			Integer numbers = RandomHelper.nextInt(0, 999999);
			codeBuilder.append(String.format("%06d", numbers));
			employeeCode = codeBuilder.toString().toUpperCase();

			extremelyRareToHappenCheckManager = this.repository.findManagerByEmployeeCode(employeeCode).orElse(null);

		} while (extremelyRareToHappenCheckManager != null);

		manager.setEmployeeCode(employeeCode);

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
		;
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

	private String getInitials(final AirlineManager manager) {
		StringBuilder res;
		String name = manager.getIdentity().getName();
		String surname = manager.getIdentity().getSurname();
		res = new StringBuilder();
		res.append(name.strip().charAt(0));
		String[] surnames = surname.strip().split("\\s+");
		res.append(surnames[0].charAt(0));
		if (surnames.length > 1)
			res.append(surnames[surnames.length - 1].charAt(0));
		return res.toString().toUpperCase();
	}
}
