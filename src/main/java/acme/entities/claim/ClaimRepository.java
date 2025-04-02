
package acme.entities.claim;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface ClaimRepository extends AbstractRepository {

	@Query("SELECT tl FROM TrackingLog tl " +
	       "WHERE tl.claim.id = :claimId " +
	       "AND tl.iteration = (SELECT MAX(tl2.iteration) FROM TrackingLog tl2 " +
	       "                     WHERE tl2.claim.id = tl.claim.id " +
	       "                     AND tl2.draftMode = false) " +
	       "AND tl.draftMode = false " +
	       "ORDER BY tl.resolutionPercentage DESC")
	List<TrackingLog> getTrackingLogsByResolutionOrder(int claimId);

	@Query("select c from Claim c where c.id = :claimId")
	Claim findById(int claimId);
}
