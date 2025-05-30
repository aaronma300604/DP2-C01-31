
package acme.features.customer.passenger;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.booking.PassengerBooking;
import acme.entities.passenger.Passenger;
import acme.features.customer.passengerBooking.CustomerPassengerBookingDeleteService;
import acme.realms.client.Customer;

@GuiService
public class CustomerPassengerDeleteService extends AbstractGuiService<Customer, Passenger> {

	//Internal state---------------------------------------------------------

	@Autowired
	private CustomerPassengerRepository				repository;
	@Autowired
	private CustomerPassengerBookingDeleteService	passengerBookingService;


	@Override
	public void authorise() {
		boolean authorised = false;

		if (super.getRequest().hasData("id")) {
			int passengerId = super.getRequest().getData("id", int.class);
			Passenger passenger = this.repository.findPassengerById(passengerId);

			if (passenger != null) {
				Customer customer = passenger.getCustomer();
				boolean isOwned = super.getRequest().getPrincipal().hasRealm(customer);
				boolean isDraft = passenger.isDraftMode();

				authorised = isOwned && isDraft;
			}
		}

		super.getResponse().setAuthorised(authorised);
	}

	@Override
	public void load() {
		Passenger passenger;
		int id;

		id = super.getRequest().getData("id", int.class);
		passenger = this.repository.findPassengerById(id);

		this.getBuffer().addData(passenger);

	}
	@Override
	public void validate(final Passenger passenger) {
		;
	}

	@Override
	public void bind(final Passenger passenger) {
		super.bindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds");

	}

	@Override
	public void perform(final Passenger passenger) {
		List<PassengerBooking> relation = this.repository.findPassengerBookingByPassengerId(passenger.getId());
		relation.stream().forEach(pb -> this.passengerBookingService.perform(pb));
		this.repository.delete(passenger);
	}

	@Override
	public void unbind(final Passenger passenger) {
		Dataset dataset;
		dataset = super.unbindObject(passenger, "name", "email", "passportNumber", "dateOfBirth", "specialNeeds", "draftMode");
		super.getResponse().addData(dataset);

	}

}
