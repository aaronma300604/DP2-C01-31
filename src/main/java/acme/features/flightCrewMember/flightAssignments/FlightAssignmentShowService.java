
package acme.features.flightCrewMember.flightAssignments;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int flightAssignmentId;
		FlightAssignment fa;
		FlightCrewMember member;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		fa = this.repository.findFa(flightAssignmentId);
		member = fa == null ? null : fa.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {

		FlightAssignment fa;
		int id;

		id = super.getRequest().getData("id", int.class);
		fa = this.repository.findFa(id);

		super.getBuffer().addData(fa);

	}

	@Override
	public void unbind(final FlightAssignment fa) {
		SelectChoices statusChoices;
		SelectChoices dutyChoices;
		Dataset dataset;

		statusChoices = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());
		dutyChoices = SelectChoices.from(Duty.class, fa.getDuty());
		dataset = super.unbindObject(fa, "moment", "duty", "currentStatus", "remarks");
		dataset.put("statuses", statusChoices);
		dataset.put("duties", dutyChoices);

		super.getResponse().addData(dataset);

	}

}
