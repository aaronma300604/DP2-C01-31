
package acme.forms.technician;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;

public class TechnicianDashboard extends AbstractForm {

	private static final long					serialVersionUID	= 1L;

	Map<MaintenanceStatus, Integer>				numberOfMaintenanceByStatus;
	MaintenanceRecord							nearestNextInspection;
	List<Aircraft>								mostTasksAircrafts;
	List<MaintenanceRecordCostStatistics>		costStatistics;
	List<MaintenanceRecordDurationStatistics>	durationStatistics;
}
