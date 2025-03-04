
package acme.entities;

import java.util.Date;

import javax.persistence.ManyToOne;
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
	MaintenanceStatus			status;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment //TODO: force this to be a future moment
	Date						nextInspection;

	@Mandatory
	@Automapped
	@ValidMoney
	Money						estimatedCost;

	@Optional
	@Automapped
	@ValidString(max = 255)
	String						notes;

	//Derived Attributes ------------------------

	//Relationships------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	Aircraft					aircraft;
}
