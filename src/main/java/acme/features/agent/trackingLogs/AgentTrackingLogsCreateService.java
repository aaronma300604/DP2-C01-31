
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
public class AgentTrackingLogsCreateService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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
		TrackingLog log;
		int claimId;

		claimId = super.getRequest().getData("claimid", int.class);
		Claim claim = this.claimRepository.findById(claimId);

		log = new TrackingLog();
		log.setDraftMode(true);
		log.setClaim(claim);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "accepted");
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
	public void validate(final TrackingLog log) {

	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;
		SelectChoices choices = SelectChoices.from(AcceptanceStatus.class, log.getAccepted());

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "iteration", "claim");

		dataset.put("accepted", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
