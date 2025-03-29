
package acme.features.technician.involves;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.maintenanceRecord.Involves;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.task.Task;

@Repository
public interface TechnicianInvolvesRepository extends AbstractRepository {

	@Query("select i from Involves i where i.maintenanceRecord.id = :recordId")
	List<Involves> findInvolvesByRecord(final int recordId);

	@Query("select mr from MaintenanceRecord mr where mr.id = :recordId")
	MaintenanceRecord findRecordById(final int recordId);

	@Query("select t from Task t where t.id = :taskId")
	Task findTaskById(final int taskId);

	@Query("select t from Task t where (t.technician.id = :technicianId OR t.draftMode = false)")
	List<Task> findAllSelectableTasks(final int technicianId);

}
