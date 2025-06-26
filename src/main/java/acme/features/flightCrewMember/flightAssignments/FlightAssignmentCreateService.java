/*
 * FlightAssignmentCreateService.java
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
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentCreateService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private FlightAssignmentRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {

		boolean authorised = true;
		String method = super.getRequest().getMethod();
		if (method.equals("POST")) {
			List<Leg> selectedLegs = this.getPosibleLegs();
			String rawLeg = super.getRequest().getData("leg", String.class);
			try {
				int legId = super.getRequest().getData("leg", int.class);
				Leg legAssigned = this.repository.findLegById(legId);

				if (!"0".equals(rawLeg))
					if (legAssigned == null)
						authorised = false;
					else if (!selectedLegs.contains(legAssigned))
						authorised = false;
			} catch (Exception e) {
				authorised = false;
			}

		}
		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		FlightAssignment fa;
		FlightCrewMember member;

		member = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		fa = new FlightAssignment();
		fa.setDraftMode(true);
		fa.setFlightCrewMember(member);
		fa.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(fa);
	}

	@Override
	public void bind(final FlightAssignment fa) {

		int legId;

		FlightCrewMember actualMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legId = super.getRequest().getData("leg", int.class);
		Leg legAssigned = this.repository.findLegById(legId);
		Date current = MomentHelper.getCurrentMoment();

		super.bindObject(fa, "duty", "currentStatus", "remarks");
		fa.setFlightCrewMember(actualMember);
		fa.setLeg(legAssigned);
		fa.setMoment(current);

	}

	@Override
	public void validate(final FlightAssignment flightAssignment) {
		boolean flightAssignmentNotNull;
		flightAssignmentNotNull = flightAssignment.getFlightCrewMember() == null ? false : true;
		super.state(flightAssignmentNotNull, "crewMember", "acme.validation.flight-assignment.faNull.message");

	}

	@Override
	public void perform(final FlightAssignment flightAssignment) {
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

	public List<Leg> getPosibleLegs() {
		Date currentDate = MomentHelper.getCurrentMoment();
		List<Leg> posibleLegs = this.repository.findUpcomingLegs(currentDate);
		if (posibleLegs == null)
			posibleLegs = new ArrayList<>();
		return posibleLegs;
	}

}
