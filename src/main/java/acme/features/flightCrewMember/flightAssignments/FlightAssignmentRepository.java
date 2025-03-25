
package acme.features.flightCrewMember.flightAssignments;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flightAssignment.FlightAssignment;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findAssignmentsByCrewMemberId(@Param("id") int id);

}
