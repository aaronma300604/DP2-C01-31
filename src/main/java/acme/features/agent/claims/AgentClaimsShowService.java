
package acme.features.agent.claims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.claim.ClaimType;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentClaimsShowService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository	repository;

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
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(claim, "date", "email", "description", "type");

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);
		dataset.put("legId", claim.getLeg() != null ? claim.getLeg().getId() : null);

		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		if (trackingLogs != null && !trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			dataset.put("status", highestTrackingLog.getAccepted().toString());
		}

		super.getResponse().addData(dataset);
	}
}
