
package acme.features.manager.dashboard;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airport.Airport;
import acme.entities.leg.LegStatus;
import acme.forms.manager.Dashboard;
import acme.realms.employee.AirlineManager;

@GuiService
public class ManagerDashboardShowService extends AbstractGuiService<AirlineManager, Dashboard> {

	@Autowired
	private ManagerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Dashboard dashboard;
		Map<Integer, AirlineManager> rankingManagerByExperience;
		Integer yearsToRetire;
		Double onTimeAndDelayedLegs;
		Airport mostPopular;
		Airport lessPopular;
		Map<LegStatus, Integer> numberLegsByStatus;
		FlightStatistics statistcsAboutFlights;

		rankingManagerByExperience = new HashMap<>();//TODO: call repository queries
		yearsToRetire = null;//TODO: call repository queries
		onTimeAndDelayedLegs = null;//TODO: call repository queries
		mostPopular = null;//TODO: call repository queries
		lessPopular = null;//TODO: call repository queries
		numberLegsByStatus = new HashMap<>();//TODO: call repository queries
		statistcsAboutFlights = null;//TODO: call repository queries

		dashboard = new Dashboard();
		dashboard.setRankingManagerByExperience(rankingManagerByExperience);
		dashboard.setYearsToRetire(yearsToRetire);
		dashboard.setOnTimeAndDelayedLegs(onTimeAndDelayedLegs);
		dashboard.setMostPopular(mostPopular);
		dashboard.setLessPopular(lessPopular);
		dashboard.setNumberLegsByStatus(numberLegsByStatus);
		dashboard.setStatistcsAboutFlights(statistcsAboutFlights);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final Dashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"rankingManagerByExperience", "yearsToRetire", // 
			"onTimeAndDelayedLegs", "mostPopular", //
			"lessPopular", "numberLegsByStatus", "statistcsAboutFlights");

		super.getResponse().addData(dataset);
	}
}
