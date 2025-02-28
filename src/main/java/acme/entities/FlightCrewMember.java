
package acme.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class FlightCrewMember extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@Automapped
	@ValidString(pattern = "^[A-Z]{2-3}\\d{6}$")
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidString(pattern = "^\\+?\\d{6,15}$")
	private String				phoneNumber;

	@Mandatory
	@Automapped
	@ValidString(min = 0, max = 255)
	private String				languageSkills;

	@Mandatory
	@Automapped
	private AvaliabilityStatus	abStat;

	@Mandatory
	@Automapped
	private String				airline;

	@Mandatory
	@Automapped
	private Integer				salary;

	@Optional
	@Automapped
	private Integer				experienceYears;

	//Relationships

	@Mandatory
	@Valid
	@OneToMany
	private ActivityLog			activityLog;

	@Mandatory
	@Valid
	@OneToMany
	private FlightAssignment	flightAssignment;
}
