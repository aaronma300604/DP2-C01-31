
package acme.forms.administrator;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Dashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	List<AirportsByScope>		airportsByScope;
	List<AirlinesByType>		airlinesByType;
	Double						ratioAirlinesWithEmailOrPhone;
	Double						ratioActiveAircrafts;
	Double						ratioNotActiveAircrafts;
	Double						ratioRewiewsScoreAbove5;
	ReviewsStatistics			reviewsStatistics;
}
