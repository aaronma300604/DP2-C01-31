
package acme.features.technician.maintenanceRecord;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;

@Repository
public interface TechnicianMaintenanceRecordsRepository extends AbstractRepository {

	@Query("select mr from MaintenanceRecord mr where mr.technician.id = :technicianId")
	List<MaintenanceRecord> findMyRecords(final int technicianId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :recordId")
	MaintenanceRecord findRecord(final int recordId);

	@Query("select a from Aircraft a where a.id = :aircraftId")
	Aircraft findAircraftById(final int aircraftId);

	@Query("select a from Aircraft a ")
	List<Aircraft> findAllAircrafts();
}
