
package acme.features.agent.trackingLogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.ClaimRepository;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentTrackingLogsListService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AgentTrackingLogsRepository	repository;

	@Autowired
	private ClaimRepository				claimRepository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<TrackingLog> logs;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
		logs = this.repository.findTrackingLogsFromClaimId(super.getRequest().getData("claimid", int.class));

		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution");

		dataset.put("accepted", log.getAccepted().toString());

		super.getResponse().addData(dataset);
	}

}
