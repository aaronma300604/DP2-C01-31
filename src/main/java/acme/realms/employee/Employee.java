
package acme.realms.employee;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Transient;

import acme.client.components.basis.AbstractRole;
import acme.client.components.basis.AbstractSquad;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Employee extends AbstractSquad {

	private static final long serialVersionUID = 1L;

	// AbstractSquad interface ------------------------------------------------


	@Transient
	@Override
	public Set<Class<? extends AbstractRole>> getMembers() {
		Set<Class<? extends AbstractRole>> result;

		result = Set.of(AirlineManager.class, FlightCrewMember.class, Technician.class);

		return result;
	}
}
