
package acme.features.agent.legs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.leg.Leg;
import acme.realms.employee.AssistanceAgent;

@GuiController
public class AgentLegsController extends AbstractGuiController<AssistanceAgent, Leg> {

	@Autowired
	private AgentLegsShowService showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
