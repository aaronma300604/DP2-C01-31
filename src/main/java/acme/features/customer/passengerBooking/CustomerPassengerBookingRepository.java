
package acme.features.customer.passengerBooking;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.entities.passenger.Passenger;

@Repository
public interface CustomerPassengerBookingRepository extends AbstractRepository {

	@Query("select p from PassengerBooking p where p.passenger.customer.id = :id")
	List<PassengerBooking> findPassengerBookingsByCustomerId(int id);

	@Query("select p from PassengerBooking p where p.id = :passengerBookingId")
	PassengerBooking findPassengerBookingById(int passengerBookingId);

	@Query("select b from Booking b where b.id = :bookingId")
	Booking findBookingById(int bookingId);

	@Query("select p from Passenger p where p.id = :passengerId")
	Passenger findPassengerById(int passengerId);

	@Query("select b from Booking b where b.customer.id = :customerId")
	List<Booking> findBookingByCustomerId(int customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = false")
	List<Passenger> findPassengerByCustomerId(int customerId);

	@Query("select p from Passenger p where p.customer.id = :customerId and p.draftMode = false")
	List<Passenger> findPublishedPassengersFromId(int customerId);

	@Query("select p FROM PassengerBooking p WHERE p.booking.id = :bookingId and p.passenger.id = :passengerId")
	PassengerBooking relationPassengerInBooking(int bookingId, int passengerId);

}
