
package acme.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidEmail;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.constraints.Phone;
import acme.constraints.ValidShortText;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Airline extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidShortText
	private String				name;

	@Mandatory
	@Automapped
	@Length(min = 3, max = 3)
	@Column(unique = true)
	@Pattern(regexp = "^[A-Z]{2}X$", message = "IATA code must be a unique three-uppercase-letter identifier")
	private String				iata;

	@Mandatory
	@Automapped
	@ValidUrl
	private String				website;

	@Mandatory
	@Automapped
	@Valid
	private AirlineType			type;

	@Mandatory
	@Automapped
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				foundation;

	@Optional
	@Automapped
	@ValidEmail
	private String				email;

	@Optional
	@Automapped
	@Valid
	private Phone				phone;
}
