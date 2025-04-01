
package acme.constraints.trackingLog;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.trackingLog.TrackingLog;

public class TrackingLogValidator implements ConstraintValidator<ValidTrackingLog, TrackingLog> {

	@Autowired
	private ClaimRepository claimRepository;


	@Override
	public boolean isValid(final TrackingLog trackingLog, final ConstraintValidatorContext context) {
		if (trackingLog == null)
			return true;

		Claim claim = trackingLog.getClaim();
		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			if (trackingLog.getResolutionPercentage() < highestTrackingLog.getResolutionPercentage()) {
				String errorMessage = String.format("Resolution percentage %.2f must be higher than the highest resolution percentage %.2f for claim ID %d", trackingLog.getResolutionPercentage(), highestTrackingLog.getResolutionPercentage(),
					claim.getId());

				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode("resolutionPercentage").addConstraintViolation();

				return false;
			}
		}

		return true;
	}
}
