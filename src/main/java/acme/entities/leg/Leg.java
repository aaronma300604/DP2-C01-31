
package acme.entities.leg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.constraints.leg.ValidLeg;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode"), //
	@Index(columnList = "draftMode,flight_id"), //
	@Index(columnList = "draftMode,flight_id,id"), //
	@Index(columnList = "draftMode,flight_id,id,scheduledDeparture"), //
	@Index(columnList = "flightNumber", unique = true), //
	@Index(columnList = "status"), //
	@Index(columnList = "flight_id"), //
	@Index(columnList = "aircraft_id,draftMode,scheduledDeparture,scheduledArrival")
})
@ValidLeg
public class Leg extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledDeparture;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	private Date				scheduledArrival;

	@Mandatory
	@Automapped
	@Valid
	private LegStatus			status;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Flight				flight;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Aircraft			aircraft;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airport				origin;

	@Mandatory
	@ManyToOne(optional = false)
	@Valid
	private Airport				destination;


	@Transient
	public double getDuration() {
		double res = 0;
		long duration = 0;
		if (this.getScheduledArrival() != null && this.getScheduledDeparture() != null)
			duration = this.getScheduledArrival().getTime() - this.getScheduledDeparture().getTime();
		res = (double) duration / (1000 * 60 * 60);
		return res;
	}
}
