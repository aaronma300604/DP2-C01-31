
package acme.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidString;
import acme.realms.Client;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Review extends AbstractEntity {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------
	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						reviewerName;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				moment;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						subject;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	String						text;

	@Optional
	@Automapped
	@Min(0)
	@Max(10)
	@Digits(integer = 2, fraction = 2)
	@Valid
	Double						score;

	@Optional
	@Automapped
	@Valid
	Boolean						isRecommended;

	//Derived Attributes ------------------------

	//RelationShips------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Client						reviewer;

}
