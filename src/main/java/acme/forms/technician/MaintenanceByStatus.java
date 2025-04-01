
package acme.forms.technician;

import acme.entities.maintenanceRecord.MaintenanceStatus;

public interface MaintenanceByStatus {

	MaintenanceStatus getMaintenanceStatus();
	void setOperationalScope(MaintenanceStatus ms);

	Integer getCountMaintenance();
	void setCountMaintenance(Integer cm);
}
