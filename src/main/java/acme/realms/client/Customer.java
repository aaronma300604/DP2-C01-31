
package acme.realms.client;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.phone.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@Automapped
	private String				identifier;

	@Mandatory
	@Automapped
	@ValidPhone
	private String				phone;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				address;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				city;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				country;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 500000)
	private Integer				points;

}
