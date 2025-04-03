
package acme.constraints.passengerBooking;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.entities.booking.PassengerBooking;
import acme.entities.booking.PassengerBookingRepository;

@Validator
public class PassengerBookingValidator extends AbstractValidator<ValidPassengerBooking, PassengerBooking> {

	@Autowired
	private PassengerBookingRepository repository;


	@Override
	protected void initialise(final ValidPassengerBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final PassengerBooking passengerBooking, final ConstraintValidatorContext context) {

		assert context != null;

		boolean result;

		if (passengerBooking == null || passengerBooking.getBooking() == null || passengerBooking.getPassenger() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			boolean existsPassengerBookingRelation;
			existsPassengerBookingRelation = this.repository.existsPassengerInBooking(passengerBooking.getBooking().getId(), passengerBooking.getPassenger().getId());

			super.state(context, existsPassengerBookingRelation, "booking", "acme.validation.booking.duplicated_passenger_booking.message");
		}

		result = !super.hasErrors(context);

		return result;
	}

}
