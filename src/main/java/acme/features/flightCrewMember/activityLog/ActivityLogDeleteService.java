
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
public class ActivityLogDeleteService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean authorised = true;

		try {
			String method = super.getRequest().getMethod();
			if (method.equals("POST")) {
				Date currentMoment = MomentHelper.getCurrentMoment();
				int flightCrewMemberID = super.getRequest().getPrincipal().getActiveRealm().getId();
				int flightAssignmentId = super.getRequest().getData("assignment", int.class);
				FlightAssignment fa = this.repository.findAssignmentById(flightAssignmentId);
				List<FlightAssignment> possibleAssignments = this.repository.findAssignmentsByMemberIdCompletedLegs(currentMoment, flightCrewMemberID);
				String rawAssignment = super.getRequest().getData("assignment", String.class);

				if (!"0".equals(rawAssignment))
					if (fa == null || !possibleAssignments.contains(fa))
						authorised = false;
			}
		} catch (Exception e) {
			authorised = false;
		}
		boolean status;
		int flightAssignmentId;
		ActivityLog al;
		FlightCrewMember member;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		al = this.repository.findLogById(flightAssignmentId);
		member = al == null ? null : al.getFlightAssignment().getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member);
		boolean allowed = authorised && status;
		super.getResponse().setAuthorised(allowed);
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
		;
	}

	@Override
	public void perform(final ActivityLog activityLog) {
		this.repository.delete(activityLog);
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
