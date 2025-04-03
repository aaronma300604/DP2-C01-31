
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
	private AgentClaimsDeleteService	deleteService;

	@Autowired
	private AgentClaimsUpdateService	updateService;

	@Autowired
	private AgentClaimsPublishService	publishService;

	@Autowired
	private AgentClaimsShowService		showService;


	@PostConstruct
	protected void initialise() {

		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);
		super.addCustomCommand("publish", "update", this.publishService);

	}

}
