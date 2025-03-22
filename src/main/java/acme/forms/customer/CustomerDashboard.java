
package acme.forms.customer;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.features.customer.dashboard.BookingStatistics;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long		serialVersionUID	= 1L;

	private List<String>			lastFiveDestinations;
	private Double					moneySpent;
	private Map<String, Integer>	bookingsByEconomy;
	private Map<String, Integer>	bookingsByBusiness;
	private BookingStatistics		costBooking;
	private BookingStatistics		passengerBooking;

}
