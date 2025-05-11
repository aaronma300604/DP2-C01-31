
package acme.features.authenticated.customer;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Authenticated;
import acme.client.components.principals.UserAccount;
import acme.client.helpers.PrincipalHelper;
import acme.client.helpers.RandomHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.realms.client.Customer;

@GuiService
public class AuthenticatedCustomerCreateService extends AbstractGuiService<Authenticated, Customer> {

	// Internal state ---------------------------------------------------------
	@Autowired
	private AuthenticatedCustomerRepository repository;
	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Customer customer;
		int userAccountId;
		UserAccount userAccount;
		Customer existingCustomerWithIdentifier;
		String customerIdentifier;

		userAccountId = super.getRequest().getPrincipal().getAccountId();
		userAccount = this.repository.findOneUserAccountById(userAccountId);

		customer = new Customer();
		customer.setUserAccount(userAccount);
		do {
			customerIdentifier = "";
			StringBuilder codeBuilder;
			codeBuilder = new StringBuilder();
			codeBuilder.append(this.getInitials(customer));
			Integer numbers = RandomHelper.nextInt(0, 999999);
			codeBuilder.append(String.format("%06d", numbers));
			customerIdentifier = codeBuilder.toString().toUpperCase();

			existingCustomerWithIdentifier = this.repository.findCustomerByIdentifier(customerIdentifier).orElse(null);

		} while (existingCustomerWithIdentifier != null);

		customer.setIdentifier(customerIdentifier);

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

	private String getInitials(final Customer customer) {
		StringBuilder res;
		String name = customer.getIdentity().getName();
		String surname = customer.getIdentity().getSurname();
		res = new StringBuilder();
		res.append(name.strip().charAt(0));
		String[] surnames = surname.strip().split("\\s+");
		res.append(surnames[0].charAt(0));
		if (surnames.length > 1)
			res.append(surnames[surnames.length - 1].charAt(0));
		return res.toString().toUpperCase();
	}

}
