
package acme.features.administrator.dashboard;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.administrator.AirlinesByType;
import acme.forms.administrator.AirportsByScope;
import acme.forms.administrator.Dashboard;
import acme.forms.administrator.ReviewsStatistics;

@GuiService
public class AdministratorDashboardShowService extends AbstractGuiService<Administrator, Dashboard> {

	@Autowired
	private AdministratorDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Dashboard dashboard;
		List<AirportsByScope> airportsByScope;
		List<AirlinesByType> airlinesByType;
		Double ratioAirlinesWithEmailOrPhone;
		Double ratioActiveAircrafts;
		Double ratioRewiewsScoreAbove5;
		ReviewsStatistics reviewsStatistics;

		airportsByScope = new ArrayList<>();//TODO: call repository queries
		airlinesByType = new ArrayList<>();//TODO: call repository queries
		ratioAirlinesWithEmailOrPhone = 0.0;//TODO: call repository queries
		ratioActiveAircrafts = 0.0;//TODO: call repository queries
		ratioRewiewsScoreAbove5 = 0.0;//TODO: call repository queries
		reviewsStatistics = null;//TODO: call repository queries

		dashboard = new Dashboard();
		dashboard.setAirportsByScope(airportsByScope);
		dashboard.setAirlinesByType(airlinesByType);
		dashboard.setRatioAirlinesWithEmailOrPhone(ratioAirlinesWithEmailOrPhone);
		dashboard.setRatioActiveAircrafts(ratioActiveAircrafts);
		dashboard.setRatioRewiewsScoreAbove5(ratioRewiewsScoreAbove5);
		dashboard.setReviewsStatistics(reviewsStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final Dashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"airportsByScope", "airlinesByType", // 
			"ratioAirlinesWithEmailOrPhone", "ratioActiveAircrafts", //
			"ratioRewiewsScoreAbove5", "reviewsStatistics");

		super.getResponse().addData(dataset);
	}
}
