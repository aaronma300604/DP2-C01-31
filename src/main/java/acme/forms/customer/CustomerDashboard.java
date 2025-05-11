
package acme.forms.customer;

import java.util.List;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	private List<String>		lastFiveDestinations;
	private Double				moneySpent;
	private String				bookingsByEconomy;
	private String				bookingsByBusiness;
	private String				costBooking;
	private String				passengerBooking;

}
