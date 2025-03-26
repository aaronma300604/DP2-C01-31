
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

	@Query("SELECT mr FROM Involves i JOIN i.maintenanceRecord mr"//
		+ " JOIN i.task t JOIN t.technician tech"//
		+ " WHERE tech.id = :technicianId ORDER BY mr.nextInspection ASC")
	List<MaintenanceRecord> findNextInspectionByTechnician(Long technicianId);

	@Query("SELECT mr.aircraft FROM Involves i JOIN i.maintenanceRecord mr"//
		+ " JOIN i.task t JOIN t.technician tech WHERE tech.id = :technicianId GROUP BY mr.aircraft ORDER BY COUNT(t) DESC")
	List<Aircraft> findTopAircraftsByTaskCount(Long technicianId);

	@Query("SELECT COUNT(mr) AS countRecords, AVG(mr.estimatedCost.amount) AS average, "//
		+ "MIN(mr.estimatedCost.amount) AS minimum, MAX(mr.estimatedCost.amount) AS maximum, STDDEV(mr.estimatedCost.amount) "//
		+ "AS standardDeviation FROM Involves i JOIN i.maintenanceRecord mr JOIN i.task t JOIN t.technician tech "//
		+ "WHERE tech.id = :technicianId AND mr.date >= :lastYear")
	MaintenanceRecordCostStatistics findCostStatistics(Date lastYear, Long technicianId);

	@Query("SELECT COUNT(t) AS countTasks, AVG(t.estimatedDuration) AS average, "//
		+ "MIN(t.estimatedDuration) AS minimum, MAX(t.estimatedDuration) AS maximum, STDDEV(t.estimatedDuration) "//
		+ "AS standardDeviation FROM Involves i JOIN i.task t JOIN t.technician tech "//
		+ "WHERE tech.id = :technicianId")
	MaintenanceRecordDurationStatistics findDurationStatistics(Long technicianId);

}
