
package acme.entities.claim;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Claim extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				date;

	@Mandatory
	@Automapped
	@ValidEmail
	private String				email;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				description;

	@Mandatory
	@Automapped
	@Valid
	private ClaimType			type;


	@Transient
	public AcceptanceStatus getAccepted() {
		ClaimRepository repository = SpringHelper.getBean(ClaimRepository.class);

		List<TrackingLog> trackingLogs = repository.getTrackingLogsByResolutionOrder(this.getId());

		if (trackingLogs != null && !trackingLogs.isEmpty()) {
			TrackingLog highestResolutionTrackingLog = trackingLogs.get(0);
			return highestResolutionTrackingLog.getAccepted();
		} else
			return AcceptanceStatus.PENDING;
	}


	@Mandatory
	@Automapped
	private boolean			draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AssistanceAgent	assistanceAgent;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Leg				leg;

}
