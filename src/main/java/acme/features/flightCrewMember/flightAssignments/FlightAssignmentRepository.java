
package acme.features.flightCrewMember.flightAssignments;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.realms.employee.AvaliabilityStatus;
import acme.realms.employee.FlightCrewMember;

@Repository
public interface FlightAssignmentRepository extends AbstractRepository {

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id")
	Collection<FlightAssignment> findAssignmentsByCrewMemberId(@Param("id") int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.id = :flightAssignmentId")
	FlightAssignment findFa(final int flightAssignmentId);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.leg.scheduledArrival > :currentDate")
	Collection<FlightAssignment> findAssignmentsByMemberIdUnCompletedLegs(@Param("currentDate") Date currentDate, @Param("id") int id);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.leg.scheduledArrival < :currentDate")
	Collection<FlightAssignment> findAssignmentsByMemberIdCompletedLegs(@Param("currentDate") Date currentDate, @Param("id") int id);

	@Query("SELECT fcm FROM FlightCrewMember fcm WHERE fcm.abStat = :status")
	List<FlightCrewMember> findMembersByStatus(AvaliabilityStatus status);

	@Query("SELECT fcm From FlightCrewMember fcm WHERE fcm.id = :fcmId")
	FlightCrewMember findMemberById(int fcmId);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival > :currentDate")
	List<Leg> findUpcomingLegs(@Param("currentDate") Date currentDate);

	@Query("SELECT l FROM Leg l")
	List<Leg> findAllLegs();

	@Query("SELECT al FROM ActivityLog al WHERE al.flightAssignment.id = :faId")
	List<ActivityLog> findActivityLogsByFa(int faId);

	@Query("SELECT l FROM Leg l WHERE l.scheduledArrival < :currentDate")
	List<Leg> findPreviousLegs(@Param("currentDate") Date currentDate);

	@Query("SELECT l from Leg l WHERE l.id = :legId")
	Leg findLegById(int legId);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE (fa.leg.scheduledDeparture < :legArrival AND fa.leg.scheduledArrival > :legDeparture) AND fa.leg.id <> :legId AND fa.flightCrewMember.id = :id and fa.draftMode = false")
	List<Leg> findSimultaneousLegsByMember(@Param("legDeparture") Date legDeparture, @Param("legArrival") Date legArrival, @Param("legId") int legId, @Param("id") int id);

	@Query("SELECT fa from FlightAssignment fa WHERE fa.leg = :leg and fa.duty = :duty and fa.draftMode = false")
	List<FlightAssignment> findFlightAssignmentsByLegAndDuty(@Param("leg") Leg leg, @Param("duty") Duty duty);

	@Query("SELECT fa FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id")
	List<FlightAssignment> findLegsAndAssignmentsByMemberId(@Param("id") int id);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id AND fa.draftMode = false")
	List<Leg> findLegsAndAssignmentsByMemberIdPublished(@Param("id") int id);

	@Query("SELECT fa.leg FROM FlightAssignment fa WHERE fa.flightCrewMember.id = :id")
	List<Leg> findLegsAssignedToMemberById(int id);

}
