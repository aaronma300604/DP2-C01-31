
package acme.features.agent.trackingLogs;

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
public class AgentTrackingLogsDeleteService extends AbstractGuiService<AssistanceAgent, TrackingLog> {

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

			if (agentId == 0 || !super.getRequest().getPrincipal().hasRealm(tl.getClaim().getAssistanceAgent()))
				status = false;
		}

		super.getResponse().setAuthorised(status);

	}

	@Override
	public void load() {
		TrackingLog log;

		log = new TrackingLog();
		log.setDraftMode(true);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final TrackingLog log) {
		super.bindObject(log, "stepUndergoing", "resolutionPercentage", "resolution", "accepted");

	}

	@Override
	public void validate(final TrackingLog log) {

	}

	@Override
	public void perform(final TrackingLog log) {
		this.repository.delete(log);
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
