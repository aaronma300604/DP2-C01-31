
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "promotionCode", unique = true)
})
public class Service extends AbstractEntity {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------
	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50, message = "{acme.validation.text.length.1-50}")
	String						name;

	@Mandatory
	@ValidUrl
	@Automapped
	String						picture;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, message = "{acme.validation.positive.number}")
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
}
