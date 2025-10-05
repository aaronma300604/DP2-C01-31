
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

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "draftMode");

		SelectChoices choices;

		boolean onlyOneChoice = true;

		if (log != null && !log.isDraftMode())
			choices = SelectChoices.from(AcceptanceStatus.class, log.getAccepted());
		else {
			Claim cl = log.getClaim();

			List<TrackingLog> trackingLogs = null;
			if (cl != null) {
				trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(cl.getId());
				trackingLogs.removeIf(existingLog -> existingLog.getId() == log.getId());
			}

			List<AcceptanceStatus> allowed = TrackingLogValidatorUtil.allowedStatuses(log, trackingLogs);

			choices = new SelectChoices();
			choices.add("0", "----", log.getAccepted() == null);

			int nonNullCount = 0;

			AcceptanceStatus accepted = log.getAccepted();

			if (allowed.isEmpty()) {
				if (accepted != null) {
					String key = accepted.toString();
					choices.add(key, key, true);
					nonNullCount++;
				}
			} else {
				boolean found = false;
				for (AcceptanceStatus s : allowed) {
					String key = s.toString();
					boolean selected = s.equals(accepted);
					choices.add(key, key, selected);
					nonNullCount++;
					if (selected)
						found = true;
				}
				if (accepted != null && !found) {
					String key = accepted.toString();
					choices.add(key, key, true);
					nonNullCount++;
				}
			}

			onlyOneChoice = nonNullCount == 1;
		}

		dataset.put("accepted", choices.getSelected().getKey());
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
		super.getResponse().addGlobal("readOnlyStatus", onlyOneChoice);
	}

}
