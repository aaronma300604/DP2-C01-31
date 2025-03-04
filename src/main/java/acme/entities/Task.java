
package acme.entities;

import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidString;

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
	@Min(0)
	@Max(10)
	@Valid
	Integer						priority;

	@Mandatory
	@Automapped
	@Min(1)
	@Valid
	Integer						estimatedDuration;

	//Derived Attributes ------------------------

	//Relationships------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Technician					technician;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	MaintenanceRecord			maintenanceRecord;
}
