
package acme.entities.review;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
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
	@ValidNumber(min = 0, max = 10, integer = 2, fraction = 2)
	Double						score;

	@Optional
	@Automapped
	boolean						isRecommended;

	//Derived Attributes ------------------------
}
