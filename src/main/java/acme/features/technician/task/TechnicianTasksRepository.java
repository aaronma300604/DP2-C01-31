
package acme.features.technician.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;
import acme.realms.employee.Technician;

@Repository
public interface TechnicianTasksRepository extends AbstractRepository {

	@Query("select t from Task t where t.technician.id = :technicianId")
	List<Task> findMyTasks(final int technicianId);

	@Query("select t from Task t where t.id = :taskId")
	Task findTask(final int taskId);

	@Query("select t from Task t where (t.technician.id = :technicianId or t.draftMode = false)")
	List<Task> findAvailableTasks(final int technicianId);

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :recordId")
	List<Task> findTasksByRecord(final int recordId);

	@Query("Select i from Involves i where i.task.id = :taskId")
	List<Involves> findInvolvesByTask(final int taskId);

	@Query("select mr.technician from MaintenanceRecord mr where mr.id = :recordId")
	Technician findTechnicianByRecord(int recordId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :recordId")
	MaintenanceRecord findRecord(int recordId);
}
