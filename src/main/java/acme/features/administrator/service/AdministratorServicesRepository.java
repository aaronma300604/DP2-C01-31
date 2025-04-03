
package acme.features.administrator.service;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.service.Service;

@Repository
public interface AdministratorServicesRepository extends AbstractRepository {

	@Query("select s from Service s")
	List<Service> findAllServices();

	@Query("select s from Service s where s.id = :serviceId")
	Service findServiceById(int serviceId);

	@Query("select sc.currency from SystemConfig sc")
	List<String> finAllCurrencies();

	@Query("select s from Service s where s.promotionCode = :promotionCode")
	Service findServiceByPromotionCode(String promotionCode);
}
