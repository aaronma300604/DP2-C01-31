
package acme.features.authenticated.customer;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;

@Repository
public interface AuthenticatedCustomerRepository extends AbstractRepository {

	@Query("select cb.booking from CustomerBooking cb where cb.customer.id = :id ")
	List<Booking> findBookingsByCustomerId(int id);

}
