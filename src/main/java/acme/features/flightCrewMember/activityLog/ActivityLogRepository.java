
package acme.features.flightCrewMember.activityLog;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@Repository
public interface ActivityLogRepository extends AbstractRepository {

	@Query("SELECT fa from FlightAssignment fa WHERE fa.id = :id")
	FlightAssignment findAssignmentById(int id);

	@Query("SELECT al from ActivityLog al WHERE al.flightAssignment.id = :id")
	List<ActivityLog> findLogsByFlightAssignment(int id);

	@Query("select fa from FlightAssignment fa")
	List<FlightAssignment> findAllFlightAssignments();

	@Query("SELECT al from ActivityLog al WHERE al.id = :id")
	ActivityLog findLogById(int id);

	@Query("SELECT fcm from FlightCrewMember fcm WHERE fcm.id = :id")
	FlightCrewMember findMemberById(int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.leg.scheduledArrival < :currentDate")
	List<FlightAssignment> findAssignmentsByMemberIdCompletedLegs(@Param("currentDate") Date currentDate, @Param("id") int id);
}
