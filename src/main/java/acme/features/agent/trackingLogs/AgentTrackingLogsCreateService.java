
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
public class AgentTrackingLogsCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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

		if (method.equals("GET")) {
			claimId = super.getRequest().getData("claimid", int.class);
			cl = this.claimRepository.findById(claimId);

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
							status = false;
				}
			}
		}

		if (method.equals("POST"))

		{
			claimId = super.getRequest().getData("claimid", int.class);
			cl = this.claimRepository.findById(claimId);

			int agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

			if (agentId == 0 || cl == null || !super.getRequest().getPrincipal().hasRealm(cl.getAssistanceAgent()))
				status = false;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog log;
		int claimId;

		claimId = super.getRequest().getData("claimid", int.class);
		Claim claim = this.claimRepository.findById(claimId);

		log = new TrackingLog();
		log.setDraftMode(true);
		log.setClaim(claim);
		Date date = MomentHelper.getCurrentMoment();
		log.setLastUpdate(date);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "stepUndergoing", "resolutionPercentage", "resolution", "accepted");
		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(log.getClaim().getId());

		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			if (highestTrackingLog.getResolutionPercentage() == 100.0)
				log.setIteration(highestTrackingLog.getIteration() + 1);
			else
				log.setIteration(highestTrackingLog.getIteration());
		} else
			log.setIteration(1);

		int claimId = super.getRequest().getData("claimid", int.class);
		Claim claim = this.claimRepository.findById(claimId);
		log.setClaim(claim);

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

		if (percentage < 100.0 && status != AcceptanceStatus.PENDING) {
			super.state(false, "accepted", "acme.validation.trackinglog.status-must-be-pending");
			return;
		}

		if (percentage == 100.0 && status == AcceptanceStatus.PENDING) {
			super.state(false, "accepted", "acme.validation.trackinglog.status-cannot-be-pending");
			return;
		}
	}

	@Override
	public void perform(final TrackingLog log) {
		Date date = MomentHelper.getCurrentMoment();
		log.setLastUpdate(date);

		List<TrackingLog> trackingLogs = this.claimRepository.getTrackingLogsByResolutionOrder(log.getClaim().getId());

		if (!trackingLogs.isEmpty()) {
			TrackingLog highestTrackingLog = trackingLogs.get(0);

			if (highestTrackingLog.getResolutionPercentage() == 100.0)
				log.setIteration(highestTrackingLog.getIteration() + 1);
			else
				log.setIteration(highestTrackingLog.getIteration());
		} else
			log.setIteration(1);

		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;
		SelectChoices choices = SelectChoices.from(AcceptanceStatus.class, log.getAccepted());

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "stepUndergoing", "resolutionPercentage", "resolution", "iteration", "claim");

		dataset.put("accepted", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
