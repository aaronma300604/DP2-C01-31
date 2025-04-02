
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface TechnicianMaintenanceRecordsRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId or mr.draftMode = false")
	List<MaintenanceRecord> findAvailableRecords(final int technicianId);

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	List<MaintenanceRecord> findMyRecords(final int technicianId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :recordId")
	MaintenanceRecord findRecord(final int recordId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(final int aircraftId);

	@Query("select a from Aircraft a ")
	List<Aircraft> findAllAircrafts();

	@Query("select count(t) from Task t where t.draftMode = false or t.technician.id = :technicianId")
	Integer countAvailableTasks(int technicianId);

	@Query("select sc.currency from SystemConfig sc")
	List<String> finAllCurrencies();

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :recordId")
	List<Task> findTasksByRecord(int recordId);
}
