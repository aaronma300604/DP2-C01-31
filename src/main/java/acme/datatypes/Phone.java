
package acme.datatypes;

import javax.persistence.Embeddable;

import acme.client.components.basis.AbstractDatatype;
import acme.client.components.validation.Mandatory;
import acme.constraints.phone.ValidPhone;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@ValidPhone
public class Phone extends AbstractDatatype {

	private static final long	serialVersionUID	= 1L;

	@Mandatory
	private String				number;


	@Override
	public String toString() {
		return this.number;
	}
}
