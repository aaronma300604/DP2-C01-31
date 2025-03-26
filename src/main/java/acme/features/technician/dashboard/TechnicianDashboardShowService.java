
package acme.features.technician.dashboard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.forms.technician.MaintenanceByStatus;
import acme.forms.technician.MaintenanceRecordCostStatistics;
import acme.forms.technician.MaintenanceRecordDurationStatistics;
import acme.forms.technician.TechnicianDashboard;
import acme.realms.employee.Technician;

@GuiService
public class TechnicianDashboardShowService extends AbstractGuiService<Technician, TechnicianDashboard> {

	@Autowired
	private TechnicianDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		TechnicianDashboard dashboard;
		List<MaintenanceByStatus> numberOfMaintenanceByStatus;
		MaintenanceRecord nearestNextInspection;
		List<Aircraft> mostTasksAircrafts;
		MaintenanceRecordCostStatistics costStatistics;
		MaintenanceRecordDurationStatistics durationStatistics;

		numberOfMaintenanceByStatus = this.repository.findNumberOfMaintenanceByStatus();
		nearestNextInspection = this.repository.findNextInspectionByTechnician(1l).get(0);
		mostTasksAircrafts = this.repository.findTopAircraftsByTaskCount(1l).stream().limit(5).toList();
		costStatistics = null;
		durationStatistics = this.repository.findDurationStatistics(1l);

		dashboard = new TechnicianDashboard();
		dashboard.setNumberOfMaintenanceByStatus(numberOfMaintenanceByStatus);
		dashboard.setNearestNextInspection(nearestNextInspection);
		dashboard.setMostTasksAircrafts(mostTasksAircrafts);
		dashboard.setCostStatistics(costStatistics);
		dashboard.setDurationStatistics(durationStatistics);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final TechnicianDashboard dashboard) {
		Dataset dataset;

		dataset = super.unbindObject(dashboard, //
			"numberOfMaintenanceByStatus", "nearestNextInspection", // 
			"mostTasksAircrafts", "costStatistics", //
			"durationStatistics");

		super.getResponse().addData(dataset);
	}

}
