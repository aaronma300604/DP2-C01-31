
package acme.realms.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.constraints.phone.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Technician extends AbstractRole {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	@Column(unique = true)
	String						licenseNumber;

	@Mandatory
	@Automapped
	@ValidPhone
	String						phone;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						specialisation;

	@Mandatory
	@Automapped
	@Valid
	Boolean						hasPassedHealthCheck;

	@Mandatory
	@Automapped
	@Valid
	@Min(0)
	@Max(75)
	Integer						yearsOfExperience;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	String						certifications;

	//Derived Attributes ------------------------

	//RelationShips------------------------------

}
