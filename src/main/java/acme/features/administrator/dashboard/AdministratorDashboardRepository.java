
package acme.features.administrator.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.forms.administrator.AirlinesByType;
import acme.forms.administrator.AirportsByScope;
import acme.forms.administrator.ReviewsStatistics;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select a.operationalScope as operationalScope, count(a) as countAirports from Airport a group by a.operationalScope")
	List<AirportsByScope> numberOfAirportsByScope();

	@Query("select a.type as airlineType, count(a) as countAirline from Airline a group by a.type")
	List<AirlinesByType> numberOfAirlinesByType();

	@Query("select 1.0 * count(a) / (select count(b) from Airline b) from Airline a where a.email is not null and a.phone is not null")
	Double ratioAirlinesWithEmailOrPhone();

	@Query("select 1.0 * count(a) / (select count(b) from Aircraft b) from Aircraft a where a.active = true")
	Double ratioActiveAircrafts();

	@Query("select 1.0 * count(a) / (select count(b) from Review b) from Review a where a.score > 5.0")
	Double ratioRewiewsScoreAbove5();

	@Query("select count(r) as countReviews, avg(r.score) as average, "//
		+ "min(r.score) as minimum, max(r.score) as maximum, stddev(r.score) "//
		+ "as standardDeviation from Review r where r.moment >= :date10WeeksAgo")
	ReviewsStatistics reviewsStatistics(Date date10WeeksAgo);

}
