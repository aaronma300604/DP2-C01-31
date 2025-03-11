
package acme.realms.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.datatypes.Phone;
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
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$")
	@Column(unique = true)
	String						licenseNumber;

	@Mandatory
	@Automapped
	@Valid
	Phone						phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	String						specialisation;

	@Mandatory
	@Automapped
	boolean						hasPassedHealthCheck;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 75)
	Integer						yearsOfExperience;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	String						certifications;

	//Derived Attributes ------------------------

	//RelationShips------------------------------

}
