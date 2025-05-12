
package acme.features.flightCrewMember.flightAssignments;

import java.util.EnumSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLog.ActivityLog;
import acme.entities.flightAssignment.CurrentStatus;
import acme.entities.flightAssignment.Duty;
import acme.entities.flightAssignment.FlightAssignment;
import acme.entities.leg.Leg;
import acme.features.flightCrewMember.activityLog.ActivityLogDeleteService;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentDeleteService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository	repository;

	@Autowired
	private ActivityLogDeleteService	activityLogDeleteService;


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

			} catch (Exception e) {
				authorised = false;
			}
			//Comprobacion de inyección de datos en currentStatus

			String rawStatus = super.getRequest().getData("currentStatus", String.class);

			CurrentStatus Cstatus = null;
			if (!"0".equals(rawStatus)) {               // el usuario sí seleccionó algo
				try {
					Cstatus = CurrentStatus.valueOf(rawStatus); // puede lanzar IllegalArgumentException
				} catch (IllegalArgumentException ex) {
					authorised = false;
				}
				if (!EnumSet.allOf(CurrentStatus.class).contains(Cstatus))
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
		boolean allowed = status && authorised;
		super.getResponse().setAuthorised(allowed);
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

		FlightCrewMember actualMember = (FlightCrewMember) super.getRequest().getPrincipal().getActiveRealm();

		legId = super.getRequest().getData("leg", int.class);
		Leg legAssigned = this.repository.findLegById(legId);

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
		List<ActivityLog> logs = this.repository.findActivityLogsByFa(fa.getId());
		this.repository.deleteAll(logs);
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

	public List<Leg> getPosibleLegs() {
		List<Leg> posibleLegs = this.repository.findAllLegs();
		return posibleLegs;
	}

}
