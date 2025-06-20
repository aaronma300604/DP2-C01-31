
package acme.realms.employee;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ManagerRepository extends AbstractRepository {

	@Query("select m from AirlineManager m where m.employeeCode = :employeeCode")
	AirlineManager findManagerByCode(String employeeCode);
}
