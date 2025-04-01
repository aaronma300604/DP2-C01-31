
package acme.features.flightCrewMember.dashboard;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.client.services.AbstractGuiService;
import acme.forms.administrator.Dashboard;
import acme.realms.employee.FlightCrewMember;

@GuiController
public class FlightCrewMemberDashboardController extends AbstractGuiController<FlightCrewMember, Dashboard> {

	//TODO: Change the service type due to an unknown error with the extensions in the actual service
	@Autowired
	private AbstractGuiService<FlightCrewMember, Dashboard> showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("show", this.showService);
	}
}
