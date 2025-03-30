
package acme.entities.booking;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import acme.client.helpers.SpringHelper;
import acme.constraints.booking.ValidBooking;
import acme.entities.flight.Flight;
import acme.realms.client.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
public class Booking extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	@ValidString(pattern = "^[A-Z0-9]{6,8}$")
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@Temporal(TemporalType.TIMESTAMP)
	@ValidMoment(past = true)
	private Date				purchaseMoment;

	@Mandatory
	@Automapped
	@Valid
	private TravelClassType		travelClass;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				price;


	@Transient
	public Money price() {
		BookingRepository repository = SpringHelper.getBean(BookingRepository.class);
		Long numberPassangers = repository.numberOfPassengerByBooking(this.getId());
		Money price = new Money();
		price.setCurrency(this.flight.getCost().getCurrency());
		price.setAmount(this.flight.getCost().getAmount() * numberPassangers);
		return price;
	}


	@Optional
	@Automapped
	@ValidString(max = 4)
	private String		lastCreditCardNibble;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer	customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight		flight;

	@Mandatory
	@Automapped
	private boolean		draftMode;

}
