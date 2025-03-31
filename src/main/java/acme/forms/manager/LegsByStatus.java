
package acme.forms.manager;

import acme.entities.leg.LegStatus;

public interface LegsByStatus {

	LegStatus getStatus();
	void setStatus(LegStatus status);

	Integer getLegsNumber();
	void setLegsNumber(Integer legsNumber);
}
