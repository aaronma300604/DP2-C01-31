
package acme.constraints.booking;

import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.validation.AbstractValidator;
import acme.client.components.validation.Validator;
import acme.client.helpers.StringHelper;
import acme.entities.booking.Booking;
import acme.entities.booking.BookingRepository;

@Validator
public class BookingValidator extends AbstractValidator<ValidBooking, Booking> {

	@Autowired
	private BookingRepository repository;


	@Override
	protected void initialise(final ValidBooking annotation) {
		assert annotation != null;
	}

	@Override
	public boolean isValid(final Booking booking, final ConstraintValidatorContext context) {
		assert context != null;

		boolean result;

		if (booking == null || booking.getCustomer() == null)
			super.state(context, false, "*", "javax.validation.constraints.NotNull.message");
		else {
			String lastNibble = booking.getLastCreditCardNibble();
			if (lastNibble == null)
				lastNibble = "";
			if (!StringHelper.matches(lastNibble, "\\d{4}|"))
				super.state(context, false, "lastCreditCardNibble", "acme.validation.booking.invalid-nibble.message");
		}
		result = !super.hasErrors(context);

		return result;
	}

}
