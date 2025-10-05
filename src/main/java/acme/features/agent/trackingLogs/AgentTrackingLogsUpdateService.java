
package acme.features.agent.trackingLogs;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.AcceptanceStatus;
import acme.entities.claim.Claim;
import acme.entities.claim.ClaimRepository;
import acme.entities.trackingLog.TrackingLog;
import acme.realms.employee.AssistanceAgent;

@GuiService
public class AgentTrackingLogsUpdateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

	@Autowired
	private AgentTrackingLogsRepository	repository;

	@Autowired
	private ClaimRepository				claimRepository;


	@Override
	public void authorise() {
		boolean status = true;
		int claimId;
		Claim cl;
		String method = super.getRequest().getMethod();

		if (method.equals("POST") || method.equals("GET")) {
			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int logId = super.getRequest().getData("id", int.class);
			TrackingLog tl = this.repository.findTrackingLog(logId);

			if (agentId == 0 || !super.getRequest().getPrincipal().hasRealm(tl.getClaim().getAssistanceAgent()) || tl.isDraftMode() == false)
				status = false;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLog(id);
		log.setDraftMode(true);
		Date date = MomentHelper.getCurrentMoment();
		log.setLastUpdate(date);

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(id));

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "stepUndergoing", "resolutionPercentage", "resolution", "accepted");

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(log.getId()));

		super.getBuffer().addData(log);

	}

	@Override
	public void validate(final TrackingLog trackingLog) {

		Double percentage = trackingLog.getResolutionPercentage();
		AcceptanceStatus status = trackingLog.getAccepted();

		if (percentage == null) {
			super.state(false, "resolutionPercentage", "acme.validation.trackinglog.missing-percentage");
			return;
		}

		if (status == null) {
			super.state(false, "accepted", "acme.validation.trackinglog.missing-status");
			return;
		}

		Claim claim = trackingLog.getClaim();
		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(claim.getId());

		trackingLogs.removeIf(existingLog -> existingLog.getId() == trackingLog.getId());

		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);
			double highestPercentage = highestTrackingLog.getResolutionPercentage();
			int highestIteration = highestTrackingLog.getIteration();

			boolean isLast100 = highestPercentage == 100.0;
			boolean isLastFirstIteration = highestIteration == 1;

			if (isLast100) {
				if (isLastFirstIteration) {
					if (percentage != 100.0) {
						super.state(false, "resolutionPercentage", "acme.validation.trackinglog.must-be-100-after-completion");
						return;
					}
				} else {
					super.state(false, "resolutionPercentage", "acme.validation.trackinglog.no-more-after-100");
					return;
				}

				if (status != highestTrackingLog.getAccepted()) {
					super.state(false, "accepted", "acme.validation.trackinglog.status-must-match-after-100");
					return;
				}

			} else if (percentage <= highestPercentage) {
				super.state(false, "resolutionPercentage", "acme.validation.trackinglog.percentage-too-low", String.format("%.2f", highestPercentage), claim.getId());
				return;
			}
		}

		String errorCode = TrackingLogValidatorUtil.validateStatusConsistency(trackingLog);
		if (errorCode != null) {
			super.state(false, "accepted", errorCode);
			return;
		}
	}

	@Override
	public void perform(final TrackingLog log) {

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(log.getId()));

		Date date = MomentHelper.getCurrentMoment();
		log.setLastUpdate(date);

		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "stepUndergoing", "resolutionPercentage", "resolution", "claim", "draftMode");

		Claim cl = log.getClaim();

		List<TrackingLog> trackingLogs = null;
		if (cl != null) {
			trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(cl.getId());
			trackingLogs.removeIf(existingLog -> existingLog.getId() == log.getId());
		}

		List<AcceptanceStatus> allowed = TrackingLogValidatorUtil.allowedStatuses(log, trackingLogs);

		SelectChoices choices = new SelectChoices();
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

		boolean onlyOneChoice = nonNullCount == 1;

		boolean creatable = true;

		if (cl != null)
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

		dataset.put("accepted", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addGlobal("creatable", creatable);
		super.getResponse().addGlobal("readOnlyStatus", onlyOneChoice);
		super.getResponse().addData(dataset);
	}

}
