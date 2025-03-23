
package acme.features.flightCrewMember.flightAssignments;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightAssignmentListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<FlightAssignment> fs;
		int flightCrewMemberid;

		flightCrewMemberid = super.getRequest().getPrincipal().getActiveRealm().getId();
		fs = this.repository.findAssignmentsByCrewMemberId(flightCrewMemberid);

		super.getBuffer().addData(fs);
	}

	@Override
	public void unbind(final FlightAssignment flightAssignment) {
		Dataset dataset;

		dataset = super.unbindObject(flightAssignment, "duty", "moment");
		super.addPayload(dataset, flightAssignment, "status", "leg.scheduledDeparture", "remarks");

		super.getResponse().addData(dataset);
	}
}
