
package acme.features.any.services;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.service.Service;

@Repository
public interface AnyServicesRepository extends AbstractRepository {

	@Query("select s from Service s")
	List<Service> findAllServices();

	@Query("select s from Service s where s.id = :serviceId")
	Service findServiceById(int serviceId);
}
