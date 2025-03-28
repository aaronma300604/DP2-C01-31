
package acme.features.flightCrewMember.flightAssignments;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignment.FlightAssignment;
import acme.realms.employee.FlightCrewMember;

@GuiController
public class FlightAssignmentsController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightAssignmentListService					listService;

	@Autowired
	private FlightAssignmentListServiceUncompletedLegs	listServiceUL;

	@Autowired
	private FlightAssignmentShowService					showService;

	@Autowired
	private FlightAssignmentCreateService				createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addCustomCommand("listUL", "list", this.listServiceUL);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
	}
}
