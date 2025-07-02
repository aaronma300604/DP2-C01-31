
package acme.features.agent.claims;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.features.agent.legs.AgentLegsRepository;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentClaimsUpdateService extends AbstractGuiService<AssistanceAgent, Claim> {

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
			int claimId = super.getRequest().getData("id", int.class);
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			Claim claim = this.repository.findClaim(claimId);

			if (agentId == 0 || !super.getRequest().getPrincipal().hasRealm(claim.getAssistanceAgent()) || claim.isDraftMode() == false)
				status = false;

			if (legId != 0) {
				Leg leg = this.repository.findLegById(legId);
				Date now = MomentHelper.getCurrentMoment();
				List<Leg> accessibleLegs = this.agentLegsRepository.findAllPublishedAndOccurredLegs(now);

				// Check that the agent is assigned a valid leg
				if (agentId == 0 || leg == null || !accessibleLegs.contains(leg))
					status = false;
			}

		} else
			status = false;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setDraftMode(true);
		claim.setAssistanceAgent(agent);
		Date date = MomentHelper.getCurrentMoment();
		claim.setDate(date);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "email", "description", "leg", "type");
	}

	@Override
	public void validate(final Claim claim) {
		boolean legNotNull = claim.getLeg() != null;
		super.state(legNotNull, "leg", "acme.validation.claim.legNull.message");
	}

	@Override
	public void perform(final Claim claim) {
		Date date = MomentHelper.getCurrentMoment();
		claim.setDate(date);
		this.repository.save(claim);
	}

	@Override
	public void unbind(final Claim claim) {
		Dataset dataset;
		SelectChoices choices;

		choices = SelectChoices.from(ClaimType.class, claim.getType());

		dataset = super.unbindObject(claim, "email", "description", "type", "draftMode");
		dataset.put("type", choices.getSelected().getKey());
		dataset.put("types", choices);

		dataset.put("claimId", claim.getId());

		dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);
		dataset.put("legId", claim.getLeg() != null ? claim.getLeg().getId() : null);

		Date now = MomentHelper.getCurrentMoment();
		List<Leg> allLegs = this.agentLegsRepository.findAllPublishedAndOccurredLegs(now);
		SelectChoices legChoices = SelectChoices.from(allLegs, "flightNumber", claim.getLeg());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
