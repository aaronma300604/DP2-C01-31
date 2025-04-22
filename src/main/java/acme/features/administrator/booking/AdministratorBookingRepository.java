
package acme.features.administrator.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;

@Repository
public interface AdministratorBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.draftMode = false")
	List<Booking> findBookingsPublished();

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select f from Flight f where f.draftMode = false ")
	List<Flight> findFlights();

	@Query("select p.passenger from PassengerBooking p where p.booking.id = :bookingId")
	List<Passenger> findPassengerByBookingId(int bookingId);
}
