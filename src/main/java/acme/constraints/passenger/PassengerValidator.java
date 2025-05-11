
package acme.constraints.passenger;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.passenger.Passenger;
import acme.entities.passenger.PassengerRepository;

@Validator
public class PassengerValidator extends AbstractValidator<ValidPassenger, Passenger> {

	@Autowired
	private PassengerRepository repository;


	@Override
	protected void initialise(final ValidPassenger annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Passenger passenger, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (passenger == null || passenger.getPassportNumber() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			if (passenger.getPassportNumber() != null) {
				boolean existsPassengerWithSamePassportNumber;
				existsPassengerWithSamePassportNumber = this.repository.existsByPassportNumber(passenger.getPassportNumber());

				super.state(context, existsPassengerWithSamePassportNumber, "passport", "acme.validation.passport.duplicated_passport_number.message");
			}
			{
				if (!StringHelper.matches(passenger.getPassportNumber(), "^[A-Z0-9]{6,9}$"))
					super.state(context, false, "passport", "acme.validation.passenger.passport_number.message");
			}

		}

		result = !super.hasErrors(context);

		return result;
	}

}
