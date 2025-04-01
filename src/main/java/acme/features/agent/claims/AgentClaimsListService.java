
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
public class AgentClaimsListService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository	repository;

	@Autowired
	private ClaimRepository			claimRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Claim> claims;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		claims = this.repository.findMyClaims(agentId);

		super.getBuffer().addData(claims);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choices = SelectChoices.from(ClaimType.class, claim.getType());
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(claim, "date", "email", "description", "type");

		String description = claim.getDescription();
		if (description != null && description.length() > 30)
			description = description.substring(0, 30) + "...";
		dataset.put("description", description);

		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);
		dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);

		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		if (trackingLogs != null && !trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			dataset.put("status", highestTrackingLog.getAccepted().toString());
		}

		super.getResponse().addData(dataset);
	}

}
