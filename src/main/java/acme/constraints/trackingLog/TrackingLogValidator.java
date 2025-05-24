
package acme.constraints.trackingLog;

import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import acme.entities.claim.AcceptanceStatus;
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

		boolean isValid = true;

		Double percentage = trackingLog.getResolutionPercentage();
		AcceptanceStatus status = trackingLog.getAccepted();

		if (percentage == null || status == null)
			return false;

		Claim claim = trackingLog.getClaim();
		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);
			double highestPercentage = highestTrackingLog.getResolutionPercentage();
			int highestIteration = highestTrackingLog.getIteration();

			boolean isLast100 = highestPercentage == 100.0;
			boolean isLastFirstIteration = highestIteration == 1;

			if (isLast100) {
				if (isLastFirstIteration) {
					if (percentage != 100.0) {
						context.disableDefaultConstraintViolation();
						context.buildConstraintViolationWithTemplate("Resolution percentage must be exactly 100 because the latest log has completed the claim (100%).").addPropertyNode("resolutionPercentage").addConstraintViolation();
						isValid = false;
					}
				} else {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate("No more tracking logs can be created for this claim because the resolution is already 100% and the last iteration was not the first.").addPropertyNode("resolutionPercentage")
						.addConstraintViolation();
					isValid = false;
				}
			} else if (percentage <= highestPercentage) {
				context.disableDefaultConstraintViolation();
				String errorMessage = String.format("Resolution percentage must be strictly higher than the current highest percentage (%.2f) for claim ID %d.", highestPercentage, claim.getId());
				context.buildConstraintViolationWithTemplate(errorMessage).addPropertyNode("resolutionPercentage").addConstraintViolation();
				isValid = false;
			}
		}

		if (percentage < 100.0 && status != AcceptanceStatus.PENDING) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Status must be PENDING if resolution percentage is less than 100%.").addPropertyNode("accepted").addConstraintViolation();
			isValid = false;
		}

		if (percentage == 100.0 && status == AcceptanceStatus.PENDING) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Status cannot be PENDING if resolution percentage is 100%.").addPropertyNode("accepted").addConstraintViolation();
			isValid = false;
		}

		return isValid;
	}

}
