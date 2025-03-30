
package acme.entities.booking;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.entities.passenger.Passenger;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PassengerBooking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Passenger			passenger;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;

	@Mandatory
	@Automapped
	private boolean				draftMode;

}
