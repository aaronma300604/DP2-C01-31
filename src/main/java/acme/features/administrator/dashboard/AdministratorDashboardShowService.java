
package acme.features.administrator.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.airline.AirlineType;
import acme.entities.airport.OperationalScope;
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
		Double ratioNotActiveAircrafts;
		Double ratioRewiewsScoreAbove5;
		ReviewsStatistics reviewsStatistics;

		airportsByScope = this.repository.numberOfAirportsByScope();
		airlinesByType = this.repository.numberOfAirlinesByType();
		ratioAirlinesWithEmailOrPhone = this.repository.ratioAirlinesWithEmailOrPhone();
		ratioActiveAircrafts = this.repository.ratioActiveAircrafts();
		ratioNotActiveAircrafts = ratioActiveAircrafts == null ? null : 1.0 - ratioActiveAircrafts;
		ratioRewiewsScoreAbove5 = this.repository.ratioRewiewsScoreAbove5();
		Date currentDate = MomentHelper.getCurrentMoment();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.DAY_OF_MONTH, -70);
		reviewsStatistics = this.repository.reviewsStatistics(cal.getTime());

		dashboard = new Dashboard();
		dashboard.setAirportsByScope(airportsByScope);
		dashboard.setAirlinesByType(airlinesByType);
		dashboard.setRatioAirlinesWithEmailOrPhone(ratioAirlinesWithEmailOrPhone);
		dashboard.setRatioActiveAircrafts(ratioActiveAircrafts);
		dashboard.setRatioNotActiveAircrafts(ratioNotActiveAircrafts);
		dashboard.setRatioRewiewsScoreAbove5(ratioRewiewsScoreAbove5);
		dashboard.setReviewsStatistics(reviewsStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final Dashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, "ratioAirlinesWithEmailOrPhone", "ratioActiveAircrafts", //
			"ratioNotActiveAircrafts", "ratioRewiewsScoreAbove5");

		Map<OperationalScope, Integer> scopes = AdministratorDashboardShowService.airportsByScope(dashboard.getAirportsByScope());
		dataset.put("domesticAirports", scopes.get(OperationalScope.DOMESTIC));
		dataset.put("internationalAirports", scopes.get(OperationalScope.INTERNATIONAL));
		dataset.put("regionalAirports", scopes.get(OperationalScope.REGIONAL));

		Map<AirlineType, Integer> types = AdministratorDashboardShowService.airlinesByScope(dashboard.getAirlinesByType());
		dataset.put("luxuryAirlines", types.get(AirlineType.LUXURY));
		dataset.put("standardAirlines", types.get(AirlineType.STANDARD));
		dataset.put("lowCostAirlines", types.get(AirlineType.LOW_COST));

		dataset.put("countReviews", dashboard.getReviewsStatistics().getCountReviews());
		dataset.put("averageReviews", dashboard.getReviewsStatistics().getAverage());
		dataset.put("minimumReviews", dashboard.getReviewsStatistics().getMinimum());
		dataset.put("maximumReviews", dashboard.getReviewsStatistics().getMaximum());
		dataset.put("deviationReviews", dashboard.getReviewsStatistics().getStandardDeviation());

		super.getResponse().addData(dataset);
	}

	private static Map<OperationalScope, Integer> airportsByScope(final List<AirportsByScope> ls) {
		Map<OperationalScope, Integer> res = new HashMap<>();
		OperationalScope[] scopes = OperationalScope.values();
		for (OperationalScope scope : scopes)
			res.put(scope, 0);
		for (AirportsByScope scope : ls)
			res.put(scope.getOperationalScope(), scope.getCountAirports());
		return res;
	}

	private static Map<AirlineType, Integer> airlinesByScope(final List<AirlinesByType> ls) {
		Map<AirlineType, Integer> res = new HashMap<>();
		AirlineType[] types = AirlineType.values();
		for (AirlineType type : types)
			res.put(type, 0);
		for (AirlinesByType type : ls)
			res.put(type.getAirlineType(), type.getCountAirline());
		return res;
	}
}
