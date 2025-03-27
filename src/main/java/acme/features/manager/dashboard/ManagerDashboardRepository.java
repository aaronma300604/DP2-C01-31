
package acme.features.manager.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.airport.Airport;
import acme.entities.leg.LegStatus;
import acme.forms.manager.FlightStatistics;
import acme.forms.manager.LegsByStatus;

@Repository
public interface ManagerDashboardRepository extends AbstractRepository {

	@Query("select count(m) + 1 from AirlineManager m where m.experience > (select am.experience from AirlineManager am where am.id = :managerId)")
	Integer findRankingManagerByExperience(int managerId);

	@Query("select 65 - (year(:currentDate) - year(m.birth)) from AirlineManager m where m.id = :managerId")
	Integer findYearsUntilRetirement(int managerId, Date currentDate);

	@Query("select 1.0 * count(l) / nullif((select count(lg) from Leg lg where lg.manager.id = :managerId),0) from Leg l " + //
		"where l.manager.id = :managerId and l.status = :status")
	Double findRatioStatusLegs(int managerId, LegStatus status);

	@Query("select a from Airport a where a in (select o from Leg l join l.origin o where l.manager.id = :managerId) or a in " + //
		"(select d from Leg lg join lg.destination d where lg.manager.id = :managerId) group by a order by " + //
		"((select count(l) from Leg l where l.manager.id = :managerId and l.origin.id = a.id) " + //
		"+ (select count(lg) from Leg lg where lg.manager.id = :managerId and lg.destination.id = a.id)) desc")
	List<Airport> findMostPopularAirport(int managerId);

	@Query("select a from Airport a where a in (select o from Leg l join l.origin o where l.manager.id = :managerId) or a in " + //
		"(select d from Leg lg join lg.destination d where lg.manager.id = :managerId) group by a order by " + //
		"((select count(l) from Leg l where l.manager.id = :managerId and l.origin.id = a.id) " + //
		"+ (select count(lg) from Leg lg where lg.manager.id = :managerId and lg.destination.id = a.id)) asc")
	List<Airport> findLessPopularAirport(int managerId);

	@Query("select l.status as status, count(l) as legsNumber from Leg l where l.flight.manager.id = :managerId group by l.status")
	List<LegsByStatus> findNumberOfLegsByStatus(int managerId);

	@Query("select avg(f.cost.amount) as costAverage, min(f.cost.amount) as minimum, max(f.cost.amount) as maximum, stddev(f.cost.amount) " + //
		"as standardDeviation from Flight f where f.manager.id = :managerId")
	FlightStatistics findStatisticsFromMyFlights(int managerId);
}
