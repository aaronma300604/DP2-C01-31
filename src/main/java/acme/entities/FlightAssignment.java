
package acme.entities;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;

public class FlightAssignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	private Duty				duty;

	@Mandatory
	@Automapped
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Automapped
	private CurrentStatus		currentStatus;

	@Optional
	@Automapped
	@ValidString(max = 255)
	private String				remarks;

}
