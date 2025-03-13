
package acme.constraints.technician;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.employee.Technician;

@Validator
public class TechnicianValidator extends AbstractValidator<ValidTechnician, Technician> {

	@Override
	protected void initialise(final ValidTechnician annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Technician technician, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (technician == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctLicenseNumber;
			String code = technician.getLicenseNumber();
			String initials = this.getInitials(technician);

			correctLicenseNumber = code != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", code) && code.startsWith(initials);

			super.state(context, correctLicenseNumber, "*", "acme.validation.airline_manager.employee_code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

	private String getInitials(final Technician technician) {
		StringBuilder res;
		String name = technician.getIdentity().getName();
		String surname = technician.getIdentity().getSurname();
		res = new StringBuilder();
		res.append(name.strip().charAt(0));
		String[] surnames = surname.strip().split("\\s+");
		res.append(surnames[0].charAt(0));
		if (surnames.length > 1)
			res.append(surnames[surnames.length - 1].charAt(0));
		return res.toString().toUpperCase();
	}

}
