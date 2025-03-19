
package acme.features.manager.legs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.leg.Leg;

@Repository
public interface ManagerLegsRepository extends AbstractRepository {

	@Query("select l from Leg l where l.manager.id = :managerId order by l.scheduledDeparture")
	List<Leg> findMyLegs(int managerId);
}
