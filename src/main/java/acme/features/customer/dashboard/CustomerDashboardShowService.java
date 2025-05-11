
package acme.features.customer.dashboard;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;
import acme.forms.customer.CustomerDashboard;
import acme.realms.client.Customer;

@GuiService
public class CustomerDashboardShowService extends AbstractGuiService<Customer, CustomerDashboard> {

	@Autowired
	private CustomerDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		CustomerDashboard dashboard = new CustomerDashboard();
		List<String> lastFiveDestinations;
		List<Flight> lastFiveFlights = null;
		Double moneySpent;
		String bookingsByEconomy;
		String bookingsByBusiness;
		String costBooking;
		String passengerBooking;

		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
		List<Booking> bookingsByCustomer = this.repository.findBookingsByCustomerId(customerId);
		if (!bookingsByCustomer.isEmpty())
			lastFiveFlights = this.repository.findLastFiveFlightsByCustomerId(customerId, PageRequest.of(0, 5));
		lastFiveDestinations = lastFiveFlights.stream().map(x -> x.getDestination().getCity()).toList();

		List<Booking> bookingLastYear = this.repository.findBookingsByCustomerIdSince(customerId, MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS));
		moneySpent = bookingLastYear.stream().map(x -> x.price().getAmount()).filter(x -> x != null).mapToDouble(p -> p).sum();

		Integer economyBookings = this.repository.countBookingByTravelClass(customerId, TravelClassType.ECONOMY);
		Integer businessBookings = this.repository.countBookingByTravelClass(customerId, TravelClassType.BUSINESS);
		bookingsByEconomy = String.valueOf(economyBookings);
		bookingsByBusiness = String.valueOf(businessBookings);

		List<Booking> bookingsFiveYears = this.repository.findBookingsByCustomerIdSince(customerId, MomentHelper.deltaFromCurrentMoment(-5, ChronoUnit.YEARS));
		List<Double> prices = bookingsFiveYears.stream().map(b -> b.price().getAmount()).filter(x -> x != null).collect(Collectors.toList());
		costBooking = this.formatBookingStatistics(prices);

		List<Long> passengerCounts = bookingsByCustomer.stream().map(x -> this.repository.getPassengersCountFromBooking(x.getId())).toList();
		passengerBooking = this.formatPassengerStatistics(passengerCounts);

		dashboard.setBookingsByEconomy(bookingsByEconomy);
		dashboard.setBookingsByBusiness(bookingsByBusiness);
		dashboard.setCostBooking(costBooking);
		dashboard.setLastFiveDestinations(lastFiveDestinations);
		dashboard.setMoneySpent(moneySpent);
		dashboard.setPassengerBooking(passengerBooking);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final CustomerDashboard customerDashboard) {
		Dataset dataset;

		dataset = super.unbindObject(customerDashboard, "lastFiveDestinations", "moneySpent", "bookingsByEconomy", "bookingsByBusiness", "costBooking", "passengerBooking");

		super.getResponse().addData(dataset);
	}

	private String formatBookingStatistics(final List<Double> prices) {
		int count = prices.size();
		double average = prices.stream().mapToDouble(p -> p).average().orElse(0.0);
		double min = prices.stream().mapToDouble(p -> p).min().orElse(0.0);
		double max = prices.stream().mapToDouble(p -> p).max().orElse(0.0);
		double stdDev = 0.0;

		if (count > 1) {
			double mean = average;
			double variance = prices.stream().mapToDouble(p -> Math.pow(p - mean, 2)).sum() / (count - 1);
			stdDev = Math.sqrt(variance);
		}

		return String.format("Count: %d\nAverage: %.2f\nMinimum: %.2f\nMaximum: %.2f\nStandard Deviation: %.2f", count, average, min, max, stdDev);
	}
	private String formatPassengerStatistics(final List<Long> passengerCounts) {
		LongSummaryStatistics stats = passengerCounts.stream().mapToLong(x -> x.longValue()).summaryStatistics();

		double avg = stats.getAverage();
		double variance = passengerCounts.stream().mapToDouble(n -> Math.pow(n - avg, 2)).average().orElse(0.0);
		double stdev = Math.sqrt(variance);

		return String.format("Total bookings: %d\nAverage passengers: %.2f\nMin: %d\nMax: %d\nStandard Deviation: %.2f", stats.getCount(), stats.getAverage(), stats.getMin(), stats.getMax(), stdev);
	}

}
