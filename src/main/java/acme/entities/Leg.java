
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	//TODO: composed of the airline's IATA code followed by four digits
	private String				flightNumber;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	private Integer				duration;

	@Mandatory
	@Automapped
	@Valid
	private LegStatus			status;

	@Mandatory
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@ManyToOne(optional = false)
	private Aircraft			aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	private Airport				origin;

	@Mandatory
	@ManyToOne(optional = false)
	private Airport				destination;
}
