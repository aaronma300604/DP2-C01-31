
package acme.features.administrator.passenger;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorPassengerRepository extends AbstractRepository {

	@Query("select p.passenger from PassengerBooking p where p.booking.id = :bookingId ")
	List<Passenger> findPassengersByBookingId(int bookingId);

	@Query("select p from Passenger p where p.id = :id ")
	Passenger findPassengerById(int id);

}
