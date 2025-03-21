
package acme.features.manager.legs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airport.Airport;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegsRepository extends AbstractRepository {

	@Query("select l from Leg l where l.manager.id = :managerId order by l.scheduledDeparture")
	List<Leg> findMyLegs(int managerId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLeg(final int legId);

	@Query("select f from Flight f where f.manager.id = :managerId")
	List<Flight> findFlightsByManager(int managerId);

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircrafts();

	@Query("select a from Airport a")
	List<Airport> findAllAirports();
}
