
package acme.features.agent.trackingLogs;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.claim.AcceptanceStatus;
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
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		TrackingLog log;
		int id;

		id = super.getRequest().getData("id", int.class);
		log = this.repository.findTrackingLog(id);

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(id));

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "accepted");

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(log.getId()));

		super.getBuffer().addData(log);

	}

	@Override
	public void validate(final TrackingLog log) {

	}

	@Override
	public void perform(final TrackingLog log) {

		if (log.getClaim() == null)
			log.setClaim(this.repository.findClaimByTrackingLogId(log.getId()));
		this.repository.save(log);
	}

	@Override
	public void unbind(final TrackingLog log) {
		Dataset dataset;
		int agentId;
		SelectChoices choices = SelectChoices.from(AcceptanceStatus.class, log.getAccepted());

		agentId = super.getRequest().getPrincipal().getActiveRealm().getId();

		dataset = super.unbindObject(log, "lastUpdate", "stepUndergoing", "resolutionPercentage", "resolution", "claim");

		dataset.put("accepted", choices.getSelected().getKey());
		dataset.put("types", choices);

		super.getResponse().addData(dataset);
	}

}
