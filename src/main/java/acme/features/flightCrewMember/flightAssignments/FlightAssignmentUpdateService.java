/*
 * FlightAssignmentUpdateService.java
 *
 * Copyright (C) 2012-2025 Rafael Corchuelo.
 *
 * In keeping with the traditional purpose of furthering education and research, it is
 * the policy of the copyright owner to permit non-commercial use and redistribution of
 * this software. It has been tested carefully, but it is not guaranteed for any particular
 * purposes. The copyright owner does not offer any warranties or representations, nor do
 * they accept any liabilities with respect to them.
 */

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
import acme.realms.employee.AvaliabilityStatus;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentUpdateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

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
		status = fa != null && fa.isDraftMode() && super.getRequest().getPrincipal().hasRealm(member) && fa.getDuty() == Duty.LEAD_ATTENDANT;

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

		int crewMemberId;
		int legId;
		crewMemberId = super.getRequest().getData("crewMember", int.class);
		FlightCrewMember member = this.repository.findMemberById(crewMemberId);
		legId = super.getRequest().getData("leg", int.class);
		Leg legAssigned = this.repository.findLegById(legId);

		super.bindObject(fa, "moment", "duty", "currentStatus", "remarks");
		fa.setFlightCrewMember(member);
		fa.setLeg(legAssigned);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean existSimultaneousLeg = false;
		boolean unproperDuties = false;

		Leg legAnalized = flightAssignment.getLeg();
		Date currentDate = MomentHelper.getCurrentMoment();
		Date legDeparture = flightAssignment.getLeg().getScheduledDeparture();
		List<Leg> simultaneousLegs = this.repository.findSimultaneousLegs(legDeparture, currentDate);
		if (simultaneousLegs.size() == 1 && simultaneousLegs.get(0) == legAnalized)
			existSimultaneousLeg = true;
		List<FlightAssignment> legCopilotAssignments = this.repository.findFlightAssignmentsByLegAndDuty(legAnalized, Duty.COPILOT);
		List<FlightAssignment> legPilotAssignments = this.repository.findFlightAssignmentsByLegAndDuty(legAnalized, Duty.PILOT);
		if (legCopilotAssignments.size() < 2 && legPilotAssignments.size() < 2)
			unproperDuties = true;

		super.state(existSimultaneousLeg, "leg", "{acme.validation.flight-assignment.leg.message}");
		super.state(unproperDuties, "duty", "{acme.validation.flight-assignment.duty.message}");

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
		this.repository.save(flightAssignment);
	}

	@Override
	public void unbind(final FlightAssignment fa) {
		SelectChoices statusChoices;
		SelectChoices dutyChoices;
		SelectChoices memberChoices;
		SelectChoices legChoices;
		Dataset dataset;
		List<FlightCrewMember> avaliableMembers;
		List<Leg> posibleLegs;

		statusChoices = SelectChoices.from(CurrentStatus.class, fa.getCurrentStatus());
		dutyChoices = SelectChoices.from(Duty.class, fa.getDuty());
		avaliableMembers = this.getAvailableMembers();
		posibleLegs = this.getPosibleLegs();
		legChoices = SelectChoices.from(posibleLegs, "flightNumber", fa.getLeg());
		memberChoices = SelectChoices.from(avaliableMembers, "userAccount.username", fa.getFlightCrewMember());
		dataset = super.unbindObject(fa, "moment", "duty", "currentStatus", "remarks", "draftMode");
		dataset.put("statuses", statusChoices);
		dataset.put("duties", dutyChoices);
		dataset.put("leg", legChoices.getSelected().getKey());
		dataset.put("legs", legChoices);
		dataset.put("crewMember", memberChoices.getSelected().getKey());
		dataset.put("crewMembers", memberChoices);

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
