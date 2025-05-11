
package acme.entities.task;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(indexes = {
	@Index(columnList = "draftMode")
})
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
	@ValidString(min = 1, max = 255, message = "{acme.validation.text.length.1-255}")
	String						description;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0, max = 10, message = "{acme.validation.task.priority}")
	Integer						priority;

	@Mandatory
	@Automapped
	@ValidNumber(min = 1, max = 1000, message = "{acme.validation.task.duration}")
	Integer						estimatedDuration;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	//Derived Attributes ------------------------

	//Relationships------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Technician					technician;

}
