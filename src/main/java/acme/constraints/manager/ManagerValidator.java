
package acme.constraints.manager;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.employee.AirlineManager;
import acme.realms.employee.ManagerRepository;

@Validator
public class ManagerValidator extends AbstractValidator<ValidManager, AirlineManager> {

	@Autowired
	private ManagerRepository repository;


	@Override
	protected void initialise(final ValidManager annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final AirlineManager manager, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (manager == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctEmployeeCode;
			String code = manager.getEmployeeCode();
			String initials = this.getInitials(manager);

			correctEmployeeCode = code != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", code) && code.startsWith(initials);

			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.airline_manager.employee_code.message");
		}

		{
			boolean uniqueManager;
			AirlineManager existingManager;

			existingManager = this.repository.findManagerByCode(manager.getEmployeeCode());
			uniqueManager = existingManager == null || existingManager.equals(manager);

			super.state(context, uniqueManager, "employeeCode", "acme.validation.manager.duplicated-employee-code.message");
		}

		result = !super.hasErrors(context);

		return result;
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
