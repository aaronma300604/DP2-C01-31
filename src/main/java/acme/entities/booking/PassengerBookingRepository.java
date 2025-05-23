
package acme.entities.booking;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface PassengerBookingRepository extends AbstractRepository {

	@Query("select p FROM PassengerBooking p WHERE p.booking.id = :bookingId and p.passenger.id = :passengerId")
	PassengerBooking relationPassengerInBooking(int bookingId, int passengerId);

}
