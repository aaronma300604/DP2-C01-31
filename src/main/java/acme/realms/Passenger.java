
package acme.realms;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.entities.Booking;
import acme.entities.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Passenger extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				name;

	@Mandatory
	@Automapped
	@ValidEmail()
	private String				email;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,9}$")
	@Automapped
	private String				passportNumber;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				dateOfBirth;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				specialNeeds;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Booking				booking;

}
