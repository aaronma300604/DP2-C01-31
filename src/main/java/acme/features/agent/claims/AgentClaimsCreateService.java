
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
public class AgentClaimsCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository	repository;

	@Autowired
	private AgentLegsRepository		agentLegsRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim claim;
		AssistanceAgent agent;

		agent = (AssistanceAgent) super.getRequest().getPrincipal().getActiveRealm();

		claim = new Claim();
		claim.setDraftMode(true);
		claim.setAssistanceAgent(agent);

		super.getBuffer().addData(claim);
	}

	@Override
	public void bind(final Claim claim) {
		int legId;
		Leg leg;

		legId = super.getRequest().getData("leg", int.class);
		leg = this.repository.findLegById(legId);

		super.bindObject(claim, "email", "description", "date", "leg", "type");
		claim.setLeg(leg);/*
							 * String typeValue = super.getRequest().getData("type", String.class);
							 * claim.setType(typeValue != null ? ClaimType.valueOf(typeValue) : null);
							 */
	}

	@Override
	public void validate(final Claim claim) {
	}

	@Override
	public void perform(final Claim claim) {
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

		List<Leg> allLegs = this.agentLegsRepository.findAllLegs();
		SelectChoices legChoices = SelectChoices.from(allLegs, "flightNumber", claim.getLeg());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}
}
