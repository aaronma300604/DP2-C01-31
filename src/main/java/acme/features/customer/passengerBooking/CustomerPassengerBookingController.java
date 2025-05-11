
package acme.features.customer.passengerBooking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.booking.PassengerBooking;
import acme.realms.client.Customer;

@GuiController
public class CustomerPassengerBookingController extends AbstractGuiController<Customer, PassengerBooking> {

	@Autowired
	private CustomerPassengerBookingListService		listService;
	@Autowired
	private CustomerPassengerBookingCreateService	createService;
	@Autowired
	private CustomerPassengerBookingShowService		showService;

	@Autowired
	private CustomerPassengerBookingUpdateService	updateService;
	@Autowired
	private CustomerPassengerBookingDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
