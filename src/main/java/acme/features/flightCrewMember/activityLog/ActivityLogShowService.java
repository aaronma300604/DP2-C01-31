
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
public class ActivityLogShowService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		ActivityLog al;
		FlightCrewMember member;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		al = this.repository.findLogById(flightAssignmentId);
		member = al == null ? null : al.getFlightAssignment().getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member);

		super.getResponse().setAuthorised(status);

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
	public void validate(final ActivityLog activityLog) {
		;
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
		dataset.put("faId", activityLog.getFlightAssignment().getId());
		dataset.put("fadraftMode", activityLog.getFlightAssignment().isDraftMode());

		super.getResponse().addData(dataset);
	}

}
