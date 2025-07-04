
package acme.features.agent.legs;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.activityLog.ActivityLog;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;

@Repository
public interface AgentLegsRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.manager.id = :managerId order by l.scheduledDeparture")
	List<Leg> findMyLegs(int managerId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLeg(final int legId);

	@Query("select f from Flight f")
	List<Flight> findAllFlights();

	@Query("select f from Flight f where f.manager.id = :managerId and f.draftMode = true")
	List<Flight> findFlightsByManager(int managerId);

	@Query("select a from Aircraft a where a.active = true and a.airline.id in (select m.airline.id from AirlineManager m where m.id = :managerId)")
	List<Aircraft> findActiveAircraftsByManager(int managerId);

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircrafts();

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select a from Airport a where a.id = :airportId")
	Airport findAirportById(int airportId);

	@Query("select a from FlightAssignment a where a.leg.id = :legId")
	List<FlightAssignment> findAssigmentsByLeg(int legId);

	@Query("select a from ActivityLog a where a.flightAssignment.leg.id = :legId")
	List<ActivityLog> findActivityLogsByLeg(int legId);

	@Query("select l from Leg l")
	List<Leg> findAllLegs();

	@Query("select l from Leg l where l.draftMode = false")
	List<Leg> findAllPublishedLegs();

	@Query("select l from Leg l where l.draftMode = false and l.scheduledArrival < :now")
	List<Leg> findAllPublishedAndOccurredLegs(@Param("now") Date now);
}
