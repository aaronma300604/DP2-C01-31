
package acme.forms.administrator;

import acme.entities.airline.AirlineType;

public interface AirlinesByType {

	AirlineType getAirlineType();
	void setAirlineType(AirlineType airlineType);

	Integer getCountAirline();
	void setCountAirline(Integer countAirline);
}
