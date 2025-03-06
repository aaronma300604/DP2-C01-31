
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidManager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidManager
public class AirlineManager extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Mandatory
	private String				employeeCode;

	@Mandatory
	@Automapped
	@Min(0)
	private Integer				experience;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				birth;

	@Automapped
	@Optional
	@ValidUrl
	private String				link;
}
