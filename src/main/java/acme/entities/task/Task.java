
package acme.entities.task;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.realms.employee.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Task extends AbstractEntity {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------

	@Mandatory
	@Automapped
	@Valid
	TaskType					type;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	String						description;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10)
	Integer						priority;

	@Mandatory
	@Automapped
	@ValidNumber(min = 1, max = 1000)
	Integer						estimatedDuration;

	//Derived Attributes ------------------------

	//Relationships------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Technician					technician;

}
