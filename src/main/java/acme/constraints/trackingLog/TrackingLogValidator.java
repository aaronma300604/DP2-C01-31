
package acme.constraints.trackingLog;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.claim.ClaimRepository;
import acme.entities.trackingLog.TrackingLog;

public class TrackingLogValidator implements ConstraintValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private ClaimRepository claimRepository;


	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		return true;
	}

}
