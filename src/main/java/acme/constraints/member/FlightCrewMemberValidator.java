
package acme.constraints.member;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.realms.employee.FlightCrewMember;

@Validator
public class FlightCrewMemberValidator extends AbstractValidator<ValidFlightCrewMember, FlightCrewMember> {

	@Override
	protected void initialise(final ValidFlightCrewMember annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final FlightCrewMember fcMember, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (fcMember == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctEmployeeCode;
			String code = fcMember.getEmployeeCode();
			String initials = this.getInitials(fcMember);

			correctEmployeeCode = code != null && Pattern.matches("^[A-Z]{2,3}\\d{6}$", code) && code.startsWith(initials);

			super.state(context, correctEmployeeCode, "employeeCode", "acme.validation.member.employee_code.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

	private String getInitials(final FlightCrewMember member) {
		StringBuilder res;
		String name = member.getIdentity().getName();
		String surname = member.getIdentity().getSurname();
		res = new StringBuilder();
		res.append(name.strip().charAt(0));
		String[] surnames = surname.strip().split("\\s+");
		res.append(surnames[0].charAt(0));
		if (surnames.length > 1)
			res.append(surnames[surnames.length - 1].charAt(0));
		return res.toString().toUpperCase();
	}

}
