
package acme.features.manager.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface ManagerFlightsRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.id = :managerId")
	List<Flight> findMyFlights(final int managerId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlight(final int flightId);
}
