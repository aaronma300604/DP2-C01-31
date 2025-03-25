
package acme.entities.flight;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				tag;

	@Mandatory
	@Automapped
	private boolean				selfTransfer;

	@Mandatory
	@Automapped
	@ValidMoney(min = 0.01)
	private Money				cost;

	@Optional
	@Automapped
	@ValidString(min = 1)
	private String				description;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private AirlineManager		manager;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;


	@Transient
	public Date getScheduledDeparture() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		Leg firstLeg = legs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getScheduledDeparture() : null;
	}

	@Transient
	public Date getScheduledArrival() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		Date scheduledArrival = null;
		if (!legs.isEmpty())
			scheduledArrival = legs.get(legs.size() - 1).getScheduledArrival();
		return scheduledArrival;
	}

	@Transient
	public Integer getLayovers() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		return legs.size();
	}

	@Transient
	public Airport getOrigin() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		Leg firstLeg = legs.stream().findFirst().orElse(null);
		return firstLeg != null ? firstLeg.getOrigin() : null;
	}

	@Transient
	public Airport getDestination() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		Airport destination = null;
		if (!legs.isEmpty())
			destination = legs.get(legs.size() - 1).getDestination();
		return destination;
	}

	@Transient
	public boolean isDraftMode() {
		FlightRepository repository = SpringHelper.getBean(FlightRepository.class);
		List<Leg> legs = repository.legsOfFlight(this.getId());
		if (legs.isEmpty())
			return false;
		return legs.stream().allMatch(Leg::isDraftMode);
	}
}
