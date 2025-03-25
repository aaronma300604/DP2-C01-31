
package acme.constraints.leg;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.leg.Leg;
import acme.entities.leg.LegRepository;

@Validator
public class LegValidator extends AbstractValidator<ValidLeg, Leg> {

	@Autowired
	private LegRepository repository;


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
			if (leg.getFlight() != null) {
				boolean correctFlightNumber;
				String number = leg.getFlightNumber();
				String iataCode = leg.getFlight().getAirline().getIata().strip();

				correctFlightNumber = number != null && Pattern.matches("^" + iataCode + "\\d{4}$", number);

				super.state(context, correctFlightNumber, "flightNumber", "acme.validation.leg.flight_number.message");
			}
			{
				boolean uniqueLeg;
				Leg existingLeg;

				existingLeg = this.repository.findLegByFlightNumber(leg.getFlightNumber());
				uniqueLeg = existingLeg == null || existingLeg.equals(leg);

				super.state(context, uniqueLeg, "flightNumber", "acme.validation.leg.duplicated-flight-number.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}
}
