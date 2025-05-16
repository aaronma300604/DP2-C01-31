
package acme.features.administrator.aircraft;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.airline.Airline;
import acme.entities.leg.Leg;

@Repository
public interface AdministratorAircraftsRepository extends AbstractRepository {

	@Query("select a from Aircraft a where a.registrationNumber = :registrationNumber")
	Aircraft findAircraftsByRegistrationNumber(String registrationNumber);

	@Query("select a from Aircraft a")
	List<Aircraft> findAllAircrafts();

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(int aircraftId);

	@Query("select a from Airline a")
	List<Airline> findAllAirlines();

	@Query("select a from Airline a where a.id = :airlineId")
	Airline findAirlineById(int airlineId);

	@Query("select l from Leg l where l.aircraft.id = :aircraftId and "//
		+ "(l.draftMode = false and l.scheduledDeparture > :date and l.scheduledArrival > :date or l.draftMode = false)")
	List<Leg> findLegsByAircraft(int aircraftId, Date date);
}
