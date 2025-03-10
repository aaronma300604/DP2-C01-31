
package acme.entities.aircraft;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

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
	@ValidNumber(integer = 50, min = 1)
	@Column(unique = true)
	private Integer				registrationNumber;

	@Mandatory
	@Automapped
	@Min(0)
	private Integer				capacity;

	@Mandatory
	@Automapped
	@DecimalMin(value = "2.0")
	@DecimalMax(value = "50.0")
	private Double				cargo;

	@Mandatory
	@Automapped
	private Boolean				isActive;

	@Optional
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				details;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;
}
