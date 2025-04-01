
package acme.entities.claim;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("select tl from TrackingLog tl where tl.claim.id = :claimId order by tl.resolutionPercentage desc")
	List<TrackingLog> getTrackingLogsByResolutionOrder(int claimId);
}
