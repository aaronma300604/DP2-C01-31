
package acme.features.agent.trackingLogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import acme.entities.claim.AcceptanceStatus;
import acme.entities.trackingLog.TrackingLog;

public class TrackingLogValidatorUtil {

	public static String validateStatusConsistency(final TrackingLog log) {
		Double percentage = log.getResolutionPercentage();
		AcceptanceStatus status = log.getAccepted();

		if (percentage == null)
			return null;

		if (percentage < 100.0 && status != AcceptanceStatus.PENDING)
			return "acme.validation.trackinglog.status-must-be-pending";

		if (percentage == 100.0 && status == AcceptanceStatus.PENDING)
			return "acme.validation.trackinglog.status-cannot-be-pending";

		return null;

	}

	public static List<AcceptanceStatus> allowedStatuses(final TrackingLog log, final List<TrackingLog> otherTrackingLogs) {
		if (otherTrackingLogs != null && !otherTrackingLogs.isEmpty()) {
			TrackingLog highest = otherTrackingLogs.get(0);
			double highestPercentage = highest.getResolutionPercentage();
			int highestIteration = highest.getIteration();

			boolean isLast100 = highestPercentage == 100.0;
			boolean isLastFirstIteration = highestIteration == 1;

			if (isLast100)
				if (!isLastFirstIteration)
					return Collections.emptyList();
				else
					return Collections.singletonList(highest.getAccepted());
		}

		return new ArrayList<>(Arrays.asList(AcceptanceStatus.values()));
	}
}
