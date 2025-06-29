
package acme.realms.employee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;

import acme.client.components.basis.AbstractRole;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.constraints.member.ValidFlightCrewMember;
import acme.constraints.phone.ValidPhone;
import acme.entities.airline.Airline;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@ValidFlightCrewMember
@Table(indexes = {
	@Index(columnList = "abStat")
})
public class FlightCrewMember extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@Column(unique = true)
	@ValidString(pattern = "^[A-Z]{2,3}\\d{6}$", message = "{acme.validation.member.employee_code.message}")
	@Automapped
	private String				employeeCode;

	@Mandatory
	@Automapped
	@ValidPhone
	private String				phone;

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
	@ValidMoney
	private Money				salary;

	@Optional
	@Automapped
	@ValidNumber(min = 0, max = 120)
	private Integer				experienceYears;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Airline				airline;

}
