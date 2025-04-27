
package acme.features.customer.dashboard;

import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface CustomerDashboardRepository extends AbstractRepository {
	//
	//	@Query("select b from Booking b where b.customer.id = :id ")
	//	List<Booking> findBookingsByCustomerId(int id);
	//
	//	@Query("select b.flight.destination from Booking where b.customer.id = :customerId and b.flight is not null order by b.purchaseMoment desc")
	//	List<String> findLastFiveDestinationsByCustomerId(int id, PageRequest pageRequest);

}
