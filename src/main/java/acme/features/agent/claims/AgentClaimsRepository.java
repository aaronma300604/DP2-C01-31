
package acme.features.agent.claims;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.claim.Claim;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;

@Repository
public interface AgentClaimsRepository extends AbstractRepository {

	@Query("SELECT c FROM Claim c WHERE c.id = :claimId")
	Claim findClaim(@Param("claimId") final int claimId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId")
	List<Claim> findMyClaims(@Param("agentId") final int agentId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId " + "AND EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c " + "AND t.resolutionPercentage = (SELECT MAX(t2.resolutionPercentage) FROM TrackingLog t2 WHERE t2.claim = c) "
		+ "AND t.accepted IN ('ACCEPTED', 'REJECTED'))")
	List<Claim> findMyCompletedClaims(@Param("agentId") int agentId);

	@Query("SELECT c FROM Claim c WHERE c.assistanceAgent.id = :agentId " + "AND EXISTS (SELECT t FROM TrackingLog t WHERE t.claim = c " + "AND t.resolutionPercentage = (SELECT MAX(t2.resolutionPercentage) FROM TrackingLog t2 WHERE t2.claim = c) "
		+ "AND t.accepted = 'PENDING')")
	List<Claim> findMyUndergoingClaims(@Param("agentId") int agentId);

	@Query("SELECT l FROM Leg l WHERE l.id = :legId")
	Leg findLegById(@Param("legId") final int legId);

	@Query("SELECT l FROM Leg l WHERE l.flightNumber = :flightNumber")
	Leg findLegByFlightNumber(@Param("flightNumber") final String flightNumber);

	@Query("SELECT t FROM TrackingLog t WHERE t.claim.id = :claimId")
	List<TrackingLog> findTrackingLogsByClaimId(@Param("claimId") int claimId);
}
