
package acme.entities.maintenanceRecord;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.entities.aircraft.Aircraft;
import acme.realms.employee.Technician;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "draftMode"), @Index(columnList = "nextInspection"), @Index(columnList = "date")
})
public class MaintenanceRecord extends AbstractEntity {

	//Serialisation version  -----------------------------------------
	private static final long	serialVersionUID	= 1L;

	//Attributes -------------------------------------------

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	Date						date;

	@Mandatory
	@Automapped
	@Valid
	MaintenanceStatus			maintenanceStatus;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment
	Date						nextInspection;

	@Mandatory
	@Automapped
	@ValidMoney
	Money						estimatedCost;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255, message = "{acme.validation.text.length.1-255}")
	String						notes;

	@Mandatory
	@Automapped
	private boolean				draftMode;

	//Derived Attributes ------------------------

	//Relationships------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Aircraft					aircraft;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Technician					technician;
}
