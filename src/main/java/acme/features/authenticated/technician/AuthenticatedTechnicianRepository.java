
package acme.features.authenticated.technician;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.employee.Technician;

@Repository
public interface AuthenticatedTechnicianRepository extends AbstractRepository {

	@Query("select t from Technician t where t.userAccount.id = :id")
	Technician findOneTechnicianByUserAccountId(int id);

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(int id);

	@Query("select t from Technician t where t.licenseNumber = :licenseNumber  ")
	Optional<Technician> findTechnicianByLicenseNumber(String licenseNumber);

}
