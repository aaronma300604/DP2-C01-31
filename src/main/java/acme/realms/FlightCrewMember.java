
package acme.realms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import acme.constraints.Phone;
import acme.entities.Airline;
import acme.entities.AvaliabilityStatus;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	//TODO: ask about the first letters of the name restriction
	@Mandatory
	@Column(unique = true)
	@Automapped
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@Automapped
	@Valid
	private Phone				phone;

	@Mandatory
	@Automapped
	@ValidString(min = 1, max = 255)
	private String				languageSkills;

	@Mandatory
	@Automapped
	@Valid
	private AvaliabilityStatus	abStat;

	@Mandatory
	@Automapped
	@Valid
	private Money				salary;

	@Optional
	@Automapped
	@Min(value = 0)
	private Integer				experienceYears;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
