
package acme.features.flightCrewMember.flightAssignments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
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
		SelectChoices legChoices;
		Dataset dataset;

		List<Leg> posibleLegs;
		statusChoices = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());
		dutyChoices = SelectChoices.from(Duty.class, fa.getDuty());
		posibleLegs = this.getPosibleLegs();
		legChoices = SelectChoices.from(posibleLegs, "flightNumber", null);
		if (fa.getLeg() != null && posibleLegs.contains(fa.getLeg()))
			legChoices = SelectChoices.from(posibleLegs, "flightNumber", fa.getLeg());
		else if (!posibleLegs.contains(fa.getLeg()) && !fa.isDraftMode()) {
			posibleLegs.add(fa.getLeg());
			legChoices = SelectChoices.from(posibleLegs, "flightNumber", fa.getLeg());
		}
		dataset = super.unbindObject(fa, "moment", "duty", "currentStatus", "remarks", "draftMode");
		dataset.put("statuses", statusChoices);
		dataset.put("duties", dutyChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("flightAssignmentId", fa.getId());

		boolean showActivityLogs = false;
		if (fa != null && fa.getLeg() != null)
			showActivityLogs = fa.getLeg().getScheduledArrival().before(MomentHelper.getCurrentMoment());
		dataset.put("showActivityLogs", showActivityLogs);

		super.getResponse().addData(dataset);

	}

	public List<Leg> getPosibleLegs() {
		Date currentDate = MomentHelper.getCurrentMoment();
		List<Leg> posibleLegs = this.repository.findUpcomingLegs(currentDate);
		if (posibleLegs == null)
			posibleLegs = new ArrayList<>();
		return posibleLegs;
	}

}
