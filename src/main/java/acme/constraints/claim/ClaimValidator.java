
package acme.constraints.claim;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.leg.Leg; // Assuming this is the class for "leg"

public class ClaimValidator implements ConstraintValidator<ValidClaim, Claim> {

	@Autowired
	private ClaimRepository claimRepository;


	@Override
	public boolean isValid(final Claim claim, final ConstraintValidatorContext context) {
		Leg leg = claim.getLeg();

		if (leg == null)
			return false;

		Date scheduledArrival = leg.getScheduledArrival();

		LocalDate scheduledArrivalLocalDate = scheduledArrival.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		if (scheduledArrivalLocalDate.isAfter(LocalDate.now()))
			return false;

		return true;
	}
}
