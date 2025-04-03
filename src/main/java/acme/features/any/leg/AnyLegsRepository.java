
package acme.features.any.leg;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface AnyLegsRepository extends AbstractRepository {

	@Query("select l from Leg l where l.flight.id = :flightId and l.draftMode = false")
	List<Leg> findLegsByFlight(int flightId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select l from Leg l where l.id = :legId")
	Leg findLegById(int legId);
}
