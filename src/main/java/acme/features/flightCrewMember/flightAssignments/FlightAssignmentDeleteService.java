
package acme.features.flightCrewMember.flightAssignments;

import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
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
import acme.realms.employee.AvaliabilityStatus;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

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
		status = fa != null && fa.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

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
	public void bind(final FlightAssignment fa) {

		int legId;
		List<Leg> selectedLegs = this.getPosibleLegs();

		FlightCrewMember actualMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		String rawLeg = super.getRequest().getData("leg", String.class);
		legId = super.getRequest().getData("leg", int.class);
		Leg legAssigned = this.repository.findLegById(legId);

		//Comprobacion de inyección de datos en legs
		if (!"0".equals(rawLeg))
			if (legAssigned == null)
				throw new RuntimeException("Unexpected leg data");
			else if (!selectedLegs.contains(legAssigned))
				throw new RuntimeException("Unexpected leg received");

		//Comprobacion de inyección de datos en currentStatus

		String rawStatus = super.getRequest().getData("currentStatus", String.class);

		CurrentStatus status = null;
		if (!"0".equals(rawStatus)) {               // el usuario sí seleccionó algo
			try {
				status = CurrentStatus.valueOf(rawStatus); // puede lanzar IllegalArgumentException
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException("Unexpected currentStatus received");
			}
			if (!EnumSet.allOf(CurrentStatus.class).contains(status))
				throw new RuntimeException("Unexpected currentStatus received");
		}
		//Comprobacion de inyección de datos en duty
		String rawDuty = super.getRequest().getData("duty", String.class);
		Duty duty = null;
		if (!"0".equals(rawDuty)) {               // el usuario sí seleccionó algo
			try {
				duty = Duty.valueOf(rawDuty); // puede lanzar IllegalArgumentException
			} catch (IllegalArgumentException ex) {
				throw new RuntimeException("Unexpected duty received");
			}
			if (!EnumSet.allOf(Duty.class).contains(duty))
				throw new RuntimeException("Unexpected duty received");
		}

		super.bindObject(fa, "moment", "duty", "currentStatus", "remarks");
		fa.setFlightCrewMember(actualMember);
		fa.setLeg(legAssigned);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		;
	}

	@Override
	public void perform(final FlightAssignment fa) {
		this.repository.delete(fa);
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
		legChoices = SelectChoices.from(posibleLegs, "flightNumber", fa.getLeg());

		dataset = super.unbindObject(fa, "moment", "duty", "currentStatus", "remarks", "draftMode");
		dataset.put("statuses", statusChoices);
		dataset.put("duties", dutyChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);

		super.getResponse().addData(dataset);
	}

	public List<FlightCrewMember> getAvailableMembers() {
		List<FlightCrewMember> avaliableMembers = this.repository.findMembersByStatus(AvaliabilityStatus.AVALIABLE);
		if (avaliableMembers == null)
			avaliableMembers = new ArrayList<>();
		return avaliableMembers;
	}

	public List<Leg> getPosibleLegs() {
		Date currentDate = MomentHelper.getCurrentMoment();
		List<Leg> posibleLegs = this.repository.findUpcomingLegs(currentDate);
		if (posibleLegs == null)
			posibleLegs = new ArrayList<>();
		return posibleLegs;
	}

}
