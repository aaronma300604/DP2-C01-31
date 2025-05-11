
package acme.features.customer.dashboard;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.booking.Booking;
import acme.entities.booking.TravelClassType;
import acme.entities.flight.Flight;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {

	@Query("select b from Booking b where b.customer.id = :id ")
	List<Booking> findBookingsByCustomerId(int id);

	@Query("select b.flight from Booking b where b.customer.id = :customerId and b.flight is not null order by b.purchaseMoment desc")
	List<Flight> findLastFiveFlightsByCustomerId(int customerId, PageRequest pageRequest);

	@Query("select b from Booking b where b.customer.id = :customerId and b.purchaseMoment >= :lastYear")
	List<Booking> findBookingsByCustomerIdSince(int customerId, Date lastYear);

	@Query("select count(b) from Booking b where b.customer.id = :customerId and b.travelClass = :travelClass")
	Integer countBookingByTravelClass(int customerId, TravelClassType travelClass);

	@Query("select count(pb) from PassengerBooking pb where pb.booking.id = :bookingId")
	Long getPassengersCountFromBooking(int bookingId);

}
