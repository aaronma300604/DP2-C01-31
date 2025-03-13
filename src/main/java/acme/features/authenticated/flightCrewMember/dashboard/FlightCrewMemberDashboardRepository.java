
package acme.features.authenticated.flightCrewMember.dashboard;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface FlightCrewMemberDashboardRepository extends AbstractRepository {

	/*
	 * @Query("""
	 * SELECT DISTINCT a.name
	 * FROM FlightAssignment fa
	 * JOIN fa.leg l
	 * JOIN l.destination a
	 * ORDER BY fa.date DESC
	 * LIMIT 5
	 * """)
	 * List<String> findLastFiveDestinations();
	 */
}
