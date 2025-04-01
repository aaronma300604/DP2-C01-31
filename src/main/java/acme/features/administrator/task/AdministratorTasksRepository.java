
package acme.features.administrator.task;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.task.Task;

@Repository
public interface AdministratorTasksRepository extends AbstractRepository {

	@Query("select t from Task t where t.id = :taskId")
	Task findTask(final int taskId);

	@Query("select t from Task t where  t.draftMode = false")
	List<Task> findAvailableTasks();

	@Query("select i.task from Involves i where i.maintenanceRecord.id = :recordId")
	List<Task> findTasksByRecord(final int recordId);

}
