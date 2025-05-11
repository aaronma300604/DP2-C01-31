
package acme.features.authenticated.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.components.principals.UserAccount;
import acme.client.repositories.AbstractRepository;
import acme.realms.client.Customer;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select ua from UserAccount ua where ua.id = :id")
	UserAccount findOneUserAccountById(final int id);

	@Query("select c from Customer c where c.identifier = :identifier")
	Optional<Customer> findCustomerByIdentifier(String identifier);

	@Query("select c from Customer c where c.userAccount.id = :userAccountId")
	Customer findCustomerByUserAccountId(final int userAccountId);
}
