
package acme.features.agent.claims;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.claim.Claim;
import acme.realms.employee.AssistanceAgent;

@GuiController
public class AgentClaimsController extends AbstractGuiController<AssistanceAgent, Claim> {

	@Autowired
	private AgentClaimsListService		listService;

	@Autowired
	private AgentClaimsCreateService	createService;

	@Autowired
	private AgentClaimsShowService		showService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);

	}

}
