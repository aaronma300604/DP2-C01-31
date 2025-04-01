
package acme.entities.trackingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.trackingLog.ValidTrackingLog;
import acme.entities.claim.AcceptanceStatus;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTrackingLog
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				lastUpdate;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				stepUndergoing;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0.0, max = 100.0)
	private Double				resolutionPercentage;

	@Mandatory
	@Automapped
	@Valid
	private AcceptanceStatus	accepted;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				resolution;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;

}
