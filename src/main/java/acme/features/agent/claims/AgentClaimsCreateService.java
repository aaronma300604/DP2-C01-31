
package acme.features.agent.claims;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimType;
import acme.entities.leg.Leg;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentClaimsCreateService extends AbstractGuiService<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsRepository repository;


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

		super.bindObject(claim, "email", "description", "date", "leg");
		claim.setLeg(leg);
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

		dataset.put("leg", claim.getLeg() != null ? claim.getLeg().getFlightNumber() : null);
		dataset.put("legId", claim.getLeg() != null ? claim.getLeg().getId() : null);

		super.getResponse().addData(dataset);
	}
}
