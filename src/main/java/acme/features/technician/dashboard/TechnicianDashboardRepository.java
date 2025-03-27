
package acme.features.technician.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.forms.technician.MaintenanceByStatus;
import acme.forms.technician.MaintenanceRecordCostStatistics;
import acme.forms.technician.MaintenanceRecordDurationStatistics;

@Repository
public interface TechnicianDashboardRepository extends AbstractRepository {

	@Query("SELECT mr.maintenanceStatus AS maintenanceStatus, COUNT(mr) AS countMaintenance FROM MaintenanceRecord mr GROUP BY mr.maintenanceStatus")
	List<MaintenanceByStatus> findNumberOfMaintenanceByStatus();

	@Query("SELECT i.maintenanceRecord AS mr FROM Involves i "//
		+ " WHERE i.task.technician.id = :technicianId ORDER BY i.maintenanceRecord.nextInspection ASC")
	List<MaintenanceRecord> findNextInspectionByTechnician(int technicianId);

	@Query("SELECT i.maintenanceRecord.aircraft FROM Involves i"//
		+ " WHERE i.task.technician.id = :technicianId GROUP BY i.maintenanceRecord.aircraft ORDER BY COUNT(i.task) DESC")
	List<Aircraft> findTopAircraftsByTaskCount(int technicianId);

	@Query("SELECT COUNT(i.maintenanceRecord) AS countRecords, AVG(i.maintenanceRecord.estimatedCost.amount) AS average, "//
		+ "MIN(i.maintenanceRecord.estimatedCost.amount) AS minimum, MAX(i.maintenanceRecord.estimatedCost.amount) AS maximum," //
		+ " STDDEV(i.maintenanceRecord.estimatedCost.amount) AS standardDeviation " //
		+ "FROM Involves i WHERE i.task.technician.id = :technicianId AND i.maintenanceRecord.date >= :lastYear")
	MaintenanceRecordCostStatistics findCostStatistics(Date lastYear, int technicianId);

	@Query("SELECT COUNT(i.task) AS countTasks, AVG(i.task.estimatedDuration) AS average, "//
		+ "MIN(i.task.estimatedDuration) AS minimum, MAX(i.task.estimatedDuration) AS maximum, STDDEV(i.task.estimatedDuration) "//
		+ "AS standardDeviation FROM Involves i WHERE i.task.technician.id = :technicianId")
	MaintenanceRecordDurationStatistics findDurationStatistics(int technicianId);

}
