
package acme.features.agent.trackingLogs;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiController
public class AgentTrackingLogsController extends AbstractGuiController<AssistanceAgent, TrackingLog> {

	@Autowired
	private AgentTrackingLogsListService	listService;

	@Autowired
	private AgentTrackingLogsShowService	showService;

	@Autowired
	private AgentTrackingLogsCreateService	createService;

	@Autowired
	private AgentTrackingLogsDeleteService	deleteService;

	@Autowired
	private AgentTrackingLogsUpdateService	updateService;

	@Autowired
	private AgentTrackingLogsPublishService	publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}

}
