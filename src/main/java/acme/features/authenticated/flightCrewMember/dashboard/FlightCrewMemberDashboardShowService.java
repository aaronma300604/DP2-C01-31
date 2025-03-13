
package acme.features.authenticated.flightCrewMember.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.flightCrewMember.Dashboard;
import acme.realms.employee.FlightCrewMember;

@GuiService
public class FlightCrewMemberDashboardShowService extends AbstractGuiService<FlightCrewMember, Dashboard> {

	@Autowired
	private FlightCrewMemberDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Dashboard dashboard;
		List<String> lastFiveDestinations;

		lastFiveDestinations = new ArrayList<>();

		dashboard = new Dashboard();
		dashboard.setLastFiveDestinations(lastFiveDestinations);

		super.getBuffer().addData(dashboard);

	}

	@Override
	public void unbind(final Dashboard dashboard) {

		Dataset dataset;

		dataset = super.unbindObject(dashboard, "lastFiveDestinations");

		super.getResponse().addData(dataset);
	}

}
