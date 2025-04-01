
package acme.features.administrator.maintenanceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface AdministratorMaintenanceRecordsRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.draftMode = false")
	List<MaintenanceRecord> findAvailableRecords();

	@Query("select mr from MaintenanceRecord mr where mr.id = :recordId")
	MaintenanceRecord findRecord(final int recordId);

}
