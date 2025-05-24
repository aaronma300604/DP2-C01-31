
package acme.features.agent.claims;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.features.agent.legs.AgentLegsRepository;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentClaimsPublishService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository	repository;

	@Autowired
	private AgentLegsRepository		agentLegsRepository;


	@Override
	public void authorise() {
		boolean status = true;
		String method = super.getRequest().getMethod();

		if (method.equals("POST")) {
			int legId = super.getRequest().getData("leg", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int claimId;
			Claim claim;
			AssistanceAgent agent;

			claimId = super.getRequest().getData("id", int.class);
			claim = this.repository.findClaim(claimId);
			agent = claim == null ? null : claim.getAssistanceAgent();
			status = claim != null && claim.isDraftMode() && super.getRequest().getPrincipal().hasRealm(agent);

			super.getResponse().setAuthorised(status);

			if (legId != 0) {
				Leg leg = this.repository.findLegById(legId);
				List<Leg> accessibleLegs = this.agentLegsRepository.findAllPublishedLegs();

				// Check that the agent is assigned a valid leg
				if (agentId == 0 || leg == null || !accessibleLegs.contains(leg))
					status = false;
			}

		}

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
	public void bind(final Claim claim) {
		super.bindObject(claim, "date", "email", "description", "type", "leg");

	}

	@Override
	public void validate(final Claim claim) {
		boolean canBePublish = claim.getLeg() != null && !claim.getLeg().isDraftMode();
		super.state(canBePublish, "*", "acme.validation.claim.cant-be-publish.message");
	}

	@Override
	public void perform(final Claim claim) {
		claim.setDraftMode(false);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "date", "email", "description", "type", "draftMode");
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		dataset.put("claimId", claim.getId());
		dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);
		dataset.put("legId", claim.getLeg() != null ? claim.getLeg().getId() : null);

		List<Leg> allLegs = this.agentLegsRepository.findAllPublishedLegs();
		SelectChoices legChoices = SelectChoices.from(allLegs, "flightNumber", claim.getLeg());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
