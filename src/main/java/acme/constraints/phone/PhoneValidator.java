
package acme.constraints.phone;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;

@Validator
public class PhoneValidator extends AbstractValidator<ValidPhone, String> {

	@Override
	protected void initialise(final ValidPhone annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final String phone, final ConstraintValidatorContext context) {
		// HINT: phone can be null
		assert context != null;

		boolean result;
		boolean isNull;

		isNull = phone == null || phone == null;

		if (!isNull) {
			String number;
			boolean inRange;

			number = phone;
			inRange = number != null && Pattern.matches("^\\+?\\d{6,15}$", number);
			super.state(context, inRange, "number", "acme.validation.phone.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
