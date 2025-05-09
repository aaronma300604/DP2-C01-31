
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
public class FlightAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository	repository;

	private FlightCrewMember			currentCrewMemberAssigned;

	private Leg							currentLegAssigned;

	private Duty						currentDutyAssigned;


	@Override
	public void authorise() {
		boolean status;
		boolean authorised = true;
		int flightAssignmentId;
		FlightAssignment fa;
		FlightCrewMember member;

		flightAssignmentId = super.getRequest().getData("id", int.class);
		fa = this.repository.findFa(flightAssignmentId);
		member = fa == null ? null : fa.getFlightCrewMember();
		status = fa != null && fa.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member);

		String method = super.getRequest().getMethod();
		if (method.equals("POST")) {
			List<Leg> selectedLegs = this.getPosibleLegs();
			String rawLeg = super.getRequest().getData("leg", String.class);
			int legId = super.getRequest().getData("leg", int.class);
			Leg legAssigned = this.repository.findLegById(legId);

			if (!"0".equals(rawLeg))
				if (legAssigned == null)
					authorised = false;
				else if (!selectedLegs.contains(legAssigned))
					authorised = false;

			//Comprobacion de inyección de datos en currentStatus

			String rawStatus = super.getRequest().getData("currentStatus", String.class);

			CurrentStatus fastatus = null;
			if (!"0".equals(rawStatus)) {               // el usuario sí seleccionó algo
				try {
					fastatus = CurrentStatus.valueOf(rawStatus); // puede lanzar IllegalArgumentException
				} catch (IllegalArgumentException ex) {
					authorised = false;
				}
				if (!EnumSet.allOf(CurrentStatus.class).contains(fastatus))
					authorised = false;
			}
			//Comprobacion de inyección de datos en duty
			String rawDuty = super.getRequest().getData("duty", String.class);
			Duty duty = null;
			if (!"0".equals(rawDuty)) {               // el usuario sí seleccionó algo
				try {
					duty = Duty.valueOf(rawDuty); // puede lanzar IllegalArgumentException
				} catch (IllegalArgumentException ex) {
					authorised = false;
				}
				if (!EnumSet.allOf(Duty.class).contains(duty))
					authorised = false;
			}
		}
		boolean b = status && authorised;
		super.getResponse().setAuthorised(b);
	}

	@Override
	public void load() {
		FlightAssignment fa;
		int id;
		id = super.getRequest().getData("id", int.class);
		fa = this.repository.findFa(id);
		this.currentCrewMemberAssigned = fa.getFlightCrewMember();
		this.currentLegAssigned = fa.getLeg();
		this.currentDutyAssigned = fa.getDuty();

		super.getBuffer().addData(fa);

	}

	@Override
	public void bind(final FlightAssignment fa) {

		int legId;
		FlightCrewMember actualMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legId = super.getRequest().getData("leg", int.class);
		Leg legAssigned = this.repository.findLegById(legId);

		super.bindObject(fa, "moment", "duty", "currentStatus", "remarks");
		fa.setFlightCrewMember(actualMember);
		fa.setLeg(legAssigned);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean existSimultaneousLeg = false;
		boolean unproperPilotDuty = true;
		boolean unproperCopilotDuty = true;
		boolean alreadyAssignedToTheLeg = true;
		boolean legIsPublished = false;
		boolean flightAssignmentNotNull;

		//Comprobación de simultaneidad entre legs
		Leg legAnalized = flightAssignment.getLeg() == null ? this.currentLegAssigned : flightAssignment.getLeg();
		Date legDeparture = legAnalized.getScheduledDeparture();
		Date legArrival = legAnalized.getScheduledArrival();
		FlightCrewMember fcmAnalized = flightAssignment.getFlightCrewMember() == null ? this.currentCrewMemberAssigned : flightAssignment.getFlightCrewMember();
		List<Leg> simultaneousLegs = this.repository.findSimultaneousLegsByMember(legDeparture, legArrival, legAnalized.getId(), fcmAnalized.getId());
		if (flightAssignment.getLeg() != null && flightAssignment.getFlightCrewMember() != null) {
			if (simultaneousLegs.isEmpty())
				existSimultaneousLeg = true;
		} else
			existSimultaneousLeg = true;

		//Comprobación de número de pilotos y copilotos
		List<FlightAssignment> legCopilotAssignments = this.repository.findFlightAssignmentsByLegAndDuty(legAnalized, Duty.COPILOT);
		List<FlightAssignment> legPilotAssignments = this.repository.findFlightAssignmentsByLegAndDuty(legAnalized, Duty.PILOT);
		Duty dutyAnalized = flightAssignment.getDuty() == null ? this.currentDutyAssigned : flightAssignment.getDuty();

		if (flightAssignment.getDuty() != null && flightAssignment.getLeg() != null) {
			if (dutyAnalized == Duty.COPILOT)
				if (legCopilotAssignments.size() + 1 >= 2)
					unproperCopilotDuty = false;
			if (dutyAnalized == Duty.PILOT)
				if (legPilotAssignments.size() + 1 >= 2)
					unproperPilotDuty = false;
		} else {
			unproperPilotDuty = true;
			unproperCopilotDuty = true;
		}

		//Comprobación de leg publicada
		if (!legAnalized.isDraftMode())
			legIsPublished = true;

		//Comprobación de doble asignación a una leg
		List<Leg> legsWithFaPublished = this.repository.findLegsAndAssignmentsByMemberIdPublished(fcmAnalized.getId());
		if (flightAssignment.getFlightCrewMember() != null && flightAssignment.getLeg() != null)
			if (legsWithFaPublished != null && !legsWithFaPublished.isEmpty() && legsWithFaPublished.contains(legAnalized))
				alreadyAssignedToTheLeg = false;

		flightAssignmentNotNull = flightAssignment.getFlightCrewMember() == null ? false : true;

		super.state(flightAssignmentNotNull, "crewMember", "acme.validation.flight-assignment.faNull.message");
		super.state(alreadyAssignedToTheLeg, "*", "acme.validation.flight-assignment.memberAlreadyAssigned.message");
		super.state(existSimultaneousLeg, "leg", "acme.validation.flight-assignment.legCurrency.message");
		super.state(unproperCopilotDuty, "duty", "acme.validation.flight-assignment.dutyCopilot.message");
		super.state(unproperPilotDuty, "duty", "acme.validation.flight-assignment.dutyPilot.message");
		super.state(legIsPublished, "*", "acme.validation.flight-assignment.cant-be-published.message");

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		flightAssignment.setDraftMode(false);
		this.repository.save(flightAssignment);
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
