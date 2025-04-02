
package acme.features.agent.trackingLogs;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AgentTrackingLogsRepository extends AbstractRepository {

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	List<TrackingLog> findTrackingLogsFromClaimId(@Param("claimId") final int claimId);

	@Query("SELECT t FROM TrackingLog t WHERE t.id = :logId")
	TrackingLog findTrackingLog(@Param("logId") int logId);

	@Query("SELECT t.claim FROM TrackingLog t WHERE t.id = :logId")
	Claim findClaimByTrackingLogId(@Param("logId") int logId);

}
