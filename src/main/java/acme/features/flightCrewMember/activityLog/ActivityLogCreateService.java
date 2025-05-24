
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
public class ActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

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

		super.getResponse().setAuthorised(authorised);

	}

	@Override
	public void load() {

		ActivityLog activityLog;

		activityLog = new ActivityLog();
		activityLog.setMoment(MomentHelper.getCurrentMoment());
		activityLog.setDraftMode(true);

		super.getBuffer().addData(activityLog);

	}
	@Override
	public void bind(final ActivityLog activityLog) {

		int flightAssignmentId;

		flightAssignmentId = super.getRequest().getData("assignment", int.class);
		FlightAssignment fa = this.repository.findAssignmentById(flightAssignmentId);

		super.bindObject(activityLog, "incident", "description", "severityLevel");
		activityLog.setFlightAssignment(fa);
	}

	@Override
	public void validate(final ActivityLog activityLog) {
		boolean notNullAssignment;
		notNullAssignment = activityLog.getFlightAssignment() == null ? false : true;
		super.state(notNullAssignment, "assignment", "acme.validation.flight-assignment.faNull.message");
	}

	@Override
	public void perform(final ActivityLog activityLog) {
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
