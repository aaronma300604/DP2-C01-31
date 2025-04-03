
package acme.features.any.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;

@Repository
public interface AnyFlightsRepository extends AbstractRepository {

	@Query("select f from Flight f where f.draftMode = false")
	List<Flight> findFlightsPublished();

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);
}
