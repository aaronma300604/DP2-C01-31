
package acme.features.flightCrewMember.activityLog;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class ActivityLogPublishService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		int activityLogId;
		ActivityLog activityLog;

		activityLogId = super.getRequest().getData("id", int.class);
		activityLog = this.repository.findLogById(activityLogId);

		super.getBuffer().addData(activityLog);

	}

	@Override
	public void bind(final ActivityLog activityLog) {

		int flightAssignmentId;
		flightAssignmentId = super.getRequest().getData("assignment", int.class);
		FlightAssignment fa = this.repository.findAssignmentById(flightAssignmentId);
		super.bindObject(activityLog, "moment", "incident", "description", "severityLevel");
		activityLog.setFlightAssignment(fa);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean isFlightAssingmentDraftMode;
		isFlightAssingmentDraftMode = activityLog.getFlightAssignment().isDraftMode();

		super.state(!isFlightAssingmentDraftMode, "assignment", "acme.validation.activity-log.assignmentInDraftMode.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		activityLog.setDraftMode(false);
		this.repository.save(activityLog);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		int flightCrewMemberID;
		Date currentMoment;

		currentMoment = MomentHelper.getCurrentMoment();
		flightCrewMemberID = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<FlightAssignment> assignments;
		assignments = this.repository.findAssignmentsByMemberIdCompletedLegs(currentMoment, flightCrewMemberID);

		SelectChoices assignmentChoices;
		assignmentChoices = SelectChoices.from(assignments, "leg.flightNumber", activityLog.getFlightAssignment());

		dataset = super.unbindObject(activityLog, "moment", "incident", "description", "severityLevel", "draftMode");
		dataset.put("assignment", assignmentChoices.getSelected().getKey());
		dataset.put("assignmentChoices", assignmentChoices);

		super.getResponse().addData(dataset);
	}

}
