
package acme.realms.employee;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.phone.ValidPhone;
import acme.constraints.technician.ValidTechnician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidTechnician
public class Technician extends AbstractRole {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------

	@Mandatory
	@Column(unique = true)
	String						licenseNumber;

	@Mandatory
	@Automapped
	@ValidPhone
	String						phone;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50, message = "{acme.validation.text.length.1-50}")
	String						specialisation;

	@Mandatory
	@Automapped
	boolean						hasPassedHealthCheck;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 75, message = "{acme.validation.technician.years-of-experience}")
	Integer						yearsOfExperience;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255, message = "{acme.validation.text.length.1-255}")
	String						certifications;

	//Derived Attributes ------------------------

	//RelationShips------------------------------

}
