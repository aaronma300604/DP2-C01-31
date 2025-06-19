
package acme.features.administrator.airport;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;

@Repository
public interface AdministratorAirportsRepository extends AbstractRepository {

	@Query("select a from Airport a")
	List<Airport> findAllAirports();

	@Query("select a from Airport a where a.id = :airportId")
	Airport findAirportById(int airportId);

	@Query("select a from Airport a where a.iata = :iataCode")
	Airport findAirportByIATACode(String iataCode);
}
