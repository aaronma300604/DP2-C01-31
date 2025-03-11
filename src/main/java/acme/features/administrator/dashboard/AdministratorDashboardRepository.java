
package acme.features.administrator.dashboard;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.forms.administrator.AirportsByScope;

@Repository
public interface AdministratorDashboardRepository extends AbstractRepository {

	@Query("select a.operationalScope as operationalScope,count(a) as countAirports from Airport a group by a.operationalScope")
	List<AirportsByScope> airportsByScope();
}
