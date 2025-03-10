
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.entities.airport.Airport;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------
	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						name;

	@Mandatory
	@ValidUrl
	@Automapped
	String						picture;

	@Mandatory
	@Automapped
	@Min(0)
	@Valid
	Integer						avgDwellTime;

	@Optional
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{4}-[0-9]{2}$")
	String						promotionCode;

	@Optional
	@Automapped
	@ValidMoney
	Money						discountApplied;

	//Derived Attributes ------------------------

	//RelationShips------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Airport						airport;

}
