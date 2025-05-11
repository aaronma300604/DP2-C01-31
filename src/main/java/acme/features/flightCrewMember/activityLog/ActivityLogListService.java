
package acme.features.flightCrewMember.activityLog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class ActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private ActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		FlightAssignment fa;
		FlightCrewMember member;

		flightAssignmentId = super.getRequest().getData("flightAssignmentID", int.class);
		fa = this.repository.findAssignmentById(flightAssignmentId);
		member = fa == null ? null : fa.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) && fa != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		List<ActivityLog> logs;
		int flightAssingmentId = super.getRequest().getData("flightAssignmentID", int.class);
		logs = this.repository.findLogsByFlightAssignment(flightAssingmentId);

		super.getBuffer().addData(logs);
	}
	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "moment", "incident", "description", "severityLevel");

	}
	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		dataset = super.unbindObject(log, "moment", "incident", "description", "severityLevel");
		super.getResponse().addData(dataset);
	}

}
