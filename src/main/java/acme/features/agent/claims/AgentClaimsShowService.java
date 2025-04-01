
package acme.features.agent.claims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.AcceptanceStatus;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.entities.trackingLog.TrackingLog;
import acme.features.agent.legs.AgentLegsRepository;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentClaimsShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository	repository;

	@Autowired
	private AgentLegsRepository		agentLegsRepository;

	@Autowired
	private ClaimRepository			claimRepository;


	@Override
	public void authorise() {
		boolean status;
		int claimId;
		Claim claim;
		AssistanceAgent agent;

		claimId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaim(claimId);
		agent = claim == null ? null : claim.getAssistanceAgent();
		status = super.getRequest().getPrincipal().hasRealm(agent) || claim != null && !claim.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		int id;

		id = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaim(id);

		super.getBuffer().addData(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(ClaimType.class, claim.getType());
		int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(claim, "date", "email", "description", "type");

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		dataset.put("claimId", claim.getId());

		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		String claimStatus = null;
		if (trackingLogs != null && !trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			claimStatus = highestTrackingLog.getAccepted() != null ? highestTrackingLog.getAccepted().toString() : null;

			dataset.put("status", claimStatus);

			if (highestTrackingLog.getAccepted() != null && (highestTrackingLog.getAccepted() == AcceptanceStatus.ACCEPTED || highestTrackingLog.getAccepted() == AcceptanceStatus.REJECTED))
				dataset.put("leg", "-");
			else {
				dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);
				List<Leg> allLegs = this.agentLegsRepository.findAllLegs();
				SelectChoices legChoices = SelectChoices.from(allLegs, "flightNumber", claim.getLeg());
				dataset.put("legs", legChoices);
			}
		}

		super.getResponse().addData(dataset);
	}
}
