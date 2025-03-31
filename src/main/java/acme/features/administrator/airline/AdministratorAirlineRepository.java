
package acme.features.administrator.airline;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;

@Repository
public interface AdministratorAirlineRepository extends AbstractRepository {

	@Query("SELECT air from Airline air")
	List<Airline> findAllAirlines();

	@Query("SELECT air from Airline air WHERE air.id = :airlineId")
	Airline findAirlineById(int airlineId);

}
