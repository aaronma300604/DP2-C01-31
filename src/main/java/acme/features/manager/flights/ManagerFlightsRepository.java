
package acme.features.manager.flights;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airline.Airline;
import acme.entities.flight.Flight;
import acme.entities.leg.Leg;
import acme.entities.passenger.Passenger;

@Repository
public interface ManagerFlightsRepository extends AbstractRepository {

	@Query("select f from Flight f where f.manager.id = :managerId")
	List<Flight> findMyFlights(final int managerId);

	@Query("select f from Flight f where f.id = :flightId")
	Flight findFlight(final int flightId);

	@Query("select m.airline from AirlineManager m where m.id = :managerId")
	Airline findAirlineByManager(int managerId);

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

	@Query("select l from Leg l where l.flight.id = :flightId")
	List<Leg> findLegsByFlight(int flightId);

	@Query("select p from Passenger p where p.flight.id = :flightId")
	List<Passenger> findPassengersByFlight(int flightId);

	@Query("select sc.currency from SystemConfig sc")
	List<String> finAllCurrencies();
}
