
package acme.features.authenticated.customer.booking;

import acme.client.services.AbstractGuiService;
import acme.entities.booking.Booking;
import acme.realms.client.Customer;

public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------
	/*
	 * @Autowired
	 * private CustomerBookingRepository repository;
	 * 
	 * // AbstractGuiService interface -------------------------------------------
	 * 
	 * 
	 * @Override
	 * public void authorise() {
	 * boolean status;
	 * 
	 * status = !super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
	 * 
	 * super.getResponse().setAuthorised(status);
	 * }
	 * 
	 * @Override
	 * public void load() {
	 * List<Booking> bookings;
	 * int customerId;
	 * 
	 * customerId = super.getRequest().getPrincipal().getActiveRealm().getId();
	 * bookings = this.repository.findBookingsByCustomerId(customerId);
	 * 
	 * super.getBuffer().addData(bookings);
	 * }
	 * 
	 * @Override
	 * public void bind(final Customer object) {
	 * assert object != null;
	 * 
	 * super.bindObject(object, "company", "sector");
	 * }
	 * 
	 * @Override
	 * public void validate(final Customer object) {
	 * assert object != null;
	 * }
	 * 
	 * @Override
	 * public void perform(final Customer object) {
	 * assert object != null;
	 * 
	 * this.repository.save(object);
	 * }
	 * 
	 * @Override
	 * public void unbind(final Consumer object) {
	 * Dataset dataset;
	 * 
	 * dataset = super.unbindObject(object, "company", "sector");
	 * 
	 * super.getResponse().addData(dataset);
	 * }
	 * 
	 * @Override
	 * public void onSuccess() {
	 * if (super.getRequest().getMethod().equals("POST"))
	 * PrincipalHelper.handleUpdate();
	 * }
	 */

}
