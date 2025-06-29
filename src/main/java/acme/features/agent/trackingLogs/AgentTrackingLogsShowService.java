
package acme.features.agent.trackingLogs;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.AcceptanceStatus;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentTrackingLogsShowService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AgentTrackingLogsRepository	repository;

	@Autowired
	private ClaimRepository				claimRepository;


	@Override
	public void authorise() {
		boolean status;
		int logId;
		Claim claim;
		AssistanceAgent agent;

		logId = super.getRequest().getData("id", int.class);
		claim = this.repository.findClaimByTrackingLogId(logId);
		agent = claim == null ? null : claim.getAssistanceAgent();
		status = super.getRequest().getPrincipal().hasRealm(agent);

		super.getResponse().setAuthorised(status);
	}
	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLog(id);

		super.getBuffer().addData(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;
		SelectChoices choices = SelectChoices.from(AcceptanceStatus.class, log.getAccepted());

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "draftMode");

		dataset.put("accepted", log.getAccepted().toString());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);

		boolean creatable = true;

		Claim cl = log.getClaim();

		if (cl != null) {

			List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(cl.getId());

			if (!trackingLogs.isEmpty()) {

				TrackingLog highestTrackingLog = trackingLogs.get(0);
				double highestPercentage = highestTrackingLog.getResolutionPercentage();
				int highestIteration = highestTrackingLog.getIteration();

				boolean isLast100 = highestPercentage == 100.0;
				boolean isLastFirstIteration = highestIteration == 1;

				if (isLast100)
					if (!isLastFirstIteration)
						creatable = false;
			}
		}

		super.getResponse().addGlobal("creatable", creatable);
	}

}
