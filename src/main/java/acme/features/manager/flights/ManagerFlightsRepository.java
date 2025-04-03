
package acme.features.manager.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.booking.Booking;
import acme.entities.booking.PassengerBooking;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;

@Repository
public interface ManagerFlightsRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.id = :managerId and f.airline.id = :airlineId")
	List<Flight> findMyFlights(final int managerId, int airlineId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlight(final int flightId);

	@Query("select m.airline from AirlineManager m where m.id = :managerId")
	Airline findAirlineByManager(int managerId);

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> findLegsByFlight(int flightId);

	@Query("select b from Booking b where b.flight.id = :flightId")
	List<Booking> findBookingByFlight(int flightId);

	@Query("select sc.currency from SystemConfig sc")
	List<String> finAllCurrencies();

	@Query("select pb from PassengerBooking pb where pb.booking.id = :bookingId")
	List<PassengerBooking> findPassengerBookingByBookingId(int bookingId);
}
