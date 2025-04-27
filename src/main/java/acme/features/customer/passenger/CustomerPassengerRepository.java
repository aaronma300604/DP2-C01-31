
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.PassengerBooking;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerRepository extends AbstractRepository {

	@Query("select p from Passenger p where p.customer.id = :customerId ")
	List<Passenger> findPassengersByCustomerId(int customerId);

	@Query("select p.passenger from PassengerBooking p where p.booking.id = :bookingId ")
	List<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id ")
	Passenger findPassengerById(int id);

	@Query("select p from PassengerBooking p where p.passenger.id = :passengerId")
	List<PassengerBooking> findPassengerBookingByPassengerId(int passengerId);

}
