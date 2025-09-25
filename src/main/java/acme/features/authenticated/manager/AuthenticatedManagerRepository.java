
package acme.features.authenticated.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;
import acme.realms.employee.AirlineManager;

@Repository
public interface AuthenticatedManagerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(final int id);

	@Query("select m from AirlineManager m where m.employeeCode = :employeeCode")
	Optional<AirlineManager> findManagerByEmployeeCode(String employeeCode);

	@Query("select a from Airline a")
	List<Airline> findAllAirlines();

	@Query("select m from AirlineManager m where m.id = :id")
	AirlineManager findManager(final int id);

	@Query("select m from AirlineManager m where m.userAccount.id = :userAccountId")
	AirlineManager findManagerByUserAccountId(final int userAccountId);

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

	@Query("select l from Leg l where l.draftMode = true and l.flight.manager.id = :managerId")
	List<Leg> findDraftModeLegsByManager(int managerId);
}
