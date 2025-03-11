
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aircraft extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 50)
	private String				model;

	@Mandatory
	@ValidString(min = 1, max = 50)
	@Column(unique = true)
	private String				registrationNumber;

	@Mandatory
	@Automapped
	@ValidNumber(min = 0)
	private Integer				capacity;

	@Mandatory
	@Automapped
	@ValidNumber(min = 2000.0, max = 50000.0)
	private Double				cargo;

	@Mandatory
	@Automapped
	private boolean				isActive;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;
}
