
package acme.features.administrator.systemConfig;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.systemConfig.SystemConfig;

@Repository
public interface AdministratorSystemConfigurationRepository extends AbstractRepository {

	@Query("select sc from SystemConfig sc")
	List<SystemConfig> findAllCurrencies();

	@Query("select sc from SystemConfig sc where sc.id =:id")
	SystemConfig findSystemCurrencyById(int id);

	@Query("select sc from SystemConfig sc where sc.systemCurrency = true")
	List<SystemConfig> findAllSystemCurrencies();

}
