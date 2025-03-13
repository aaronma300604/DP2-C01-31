
package acme.forms.manager;

import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.airport.Airport;
import acme.entities.leg.LegStatus;
import acme.features.manager.dashboard.FlightStatistics;
import acme.realms.employee.AirlineManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	Map<Integer, AirlineManager>	rankingManagerByExperience;

	Integer							yearsToRetire;

	Double							onTimeAndDelayedLegs;

	Airport							mostPopular;

	Airport							lessPopular;

	Map<LegStatus, Integer>			numberLegsByStatus;

	FlightStatistics				statistcsAboutFlights;
}
