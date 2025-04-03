
package acme.features.manager.legs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.airport.Airport;
import acme.entities.claim.Claim;
import acme.entities.flight.Flight;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface ManagerLegsRepository extends AbstractRepository {

	@Query("select l from Leg l where l.manager.id = :managerId and l.flight.airline.id = :airlineId order by l.scheduledDeparture")
	List<Leg> findMyLegs(int managerId, int airlineId);

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

	@Query("select a.airline from AirlineManager a where a.id = :managerId")
	Airline findAirlineByManager(int managerId);

	@Query("select c from Claim c where c.leg.id = :legId")
	List<Claim> findClaimsByLeg(int legId);

	@Query("select l from TrackingLog l where l.claim.id = :claimId")
	List<TrackingLog> findLogsByClaim(int claimId);
}
