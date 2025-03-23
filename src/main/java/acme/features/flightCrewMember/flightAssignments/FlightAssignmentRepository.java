
package acme.features.flightCrewMember.flightAssignments;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("select fs from FlightAssignment where j.flightCrewMember.id = :id")
	Collection<FlightAssignment> findAssignmentsByCrewMemberId(int id);

}
