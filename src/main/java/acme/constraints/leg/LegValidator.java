
package acme.constraints.leg;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Override
	protected void initialise(final ValidLeg annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Leg leg, final ConstraintValidatorContext context) {
		// HINT: job can be null
		assert context != null;

		boolean result;

		if (leg == null)//^ABX\d{4}$
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean correctFlightNumber;
			String number = leg.getFlightNumber();
			String iataCode = leg.getFlight().getAirline().getIata().strip();

			correctFlightNumber = number != null && Pattern.matches("^" + iataCode + "\\d{4}$", number);

			super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.flight_number.message");
		}

		result = !super.hasErrors(context);

		return result;
	}
}
