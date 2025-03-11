
package acme.entities.trackingLog;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.claim.Claim;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				lastUpdate;

	@Optional
	@Automapped
	@ValidString(max = 50)
	private String				stepUndergoing;

	@Mandatory
	@Automapped
	private Integer				resolutionPercentage;

	@Optional
	@Automapped
	private Boolean				finallyAccepted;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				resolution;

	@Mandatory
	@Valid
	@OneToOne(optional = false)
	private Claim				claim;
}
