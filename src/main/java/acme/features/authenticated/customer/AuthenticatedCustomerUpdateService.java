
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.helpers.PrincipalHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.client.Customer;

@GuiService
public class AuthenticatedCustomerUpdateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuthenticatedCustomerRepository repository;
	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Customer customer;
		int userAccountId;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		customer = this.repository.findCustomerByUserAccountId(userAccountId);

		super.getBuffer().addData(customer);
	}

	@Override
	public void bind(final Customer customer) {
		super.bindObject(customer, "phone", "address", "city", "country", "points");
	}

	@Override
	public void validate(final Customer customer) {
		;
	}

	@Override
	public void perform(final Customer customer) {
		this.repository.save(customer);
	}
	@Override
	public void unbind(final Customer customer) {
		Dataset dataset;

		dataset = super.unbindObject(customer, "phone", "address", "city", "country", "points");

		super.getResponse().addData(dataset);
	}

	@Override
	public void onSuccess() {
		if (super.getRequest().getMethod().equals("POST"))
			PrincipalHelper.handleUpdate();
	}

}
