
package acme.entities.passenger;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PassengerRepository extends AbstractRepository {

	@Query("select count(p) > 0 FROM Passenger p WHERE p.passportNumber = :passportNumber")
	Boolean existsByPassportNumber(String passportNumber);

}
