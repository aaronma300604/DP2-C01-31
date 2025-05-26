
package acme.features.customer.booking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.entities.flight.Flight;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerBookingRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :id ")
	List<Booking> findBookingsByCustomerId(int id);

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select f from Flight f where f.draftMode = false ")
	List<Flight> findFlights();

	@Query("select p.passenger from PassengerBooking p where p.booking.id = :bookingId")
	List<Passenger> findPassengerByBookingId(int bookingId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlightById(int flightId);

	@Query("select p from PassengerBooking p where p.booking.id = :bookingId")
	List<PassengerBooking> findPassengerBookingByBookingId(int bookingId);

}
