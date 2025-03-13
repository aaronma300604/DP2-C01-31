
package acme.forms.administrator;

import acme.entities.airport.OperationalScope;

public interface AirportsByScope {

	OperationalScope getOperationalScope();
	void setOperationalScope(OperationalScope operationalScope);

	Integer getCountAirports();
	void setCountAirports(Integer countAirports);
}
