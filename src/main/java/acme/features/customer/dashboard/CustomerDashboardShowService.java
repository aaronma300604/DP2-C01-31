
package acme.features.customer.dashboard;

import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.forms.manager.Dashboard;
import acme.realms.client.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, Dashboard> {
	//
	//	@Autowired
	//	private CustomerDashboardRepository repository;
	//
	//
	//	@Override
	//	public void authorise() {
	//		super.getResponse().setAuthorised(true);
	//	}
	//
	//	@Override
	//	public void load() {
	//		Dashboard dashboard = null;
	//		List<String> lastFiveDestinations;
	//		Double moneySpent;
	//		Map<String, Integer> bookingsByEconomy;
	//		Map<String, Integer> bookingsByBusiness;
	//		BookingStatistics costBooking;
	//		BookingStatistics passengerBooking;
	//		BookingStatistics statisticsAboutBookings;
	//
	//		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
	//		List<Booking> bookingsByCustomer = this.repository.findBookingsByCustomerId(customerId);
	//		if (!bookingsByCustomer.isEmpty())
	//			lastFiveDestinations = this.repository.findLastFiveDestinationsByCustomerId(customerId, PageRequest.of(0, 5));
	//		Date currentDate = MomentHelper.getCurrentMoment();
	//
	//		super.getBuffer().addData(dashboard);
	//	}

}
