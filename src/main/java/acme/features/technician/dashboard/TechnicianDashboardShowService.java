
package acme.features.technician.dashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.aircraft.Aircraft;
import acme.entities.maintenanceRecord.MaintenanceRecord;
import acme.entities.maintenanceRecord.MaintenanceStatus;
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
		Map<MaintenanceStatus, Integer> numberOfMaintenanceByStatus;
		MaintenanceRecord nearestNextInspection;
		List<Aircraft> mostTasksAircrafts;
		List<MaintenanceRecordCostStatistics> costStatistics;
		List<MaintenanceRecordDurationStatistics> durationStatistics;

		numberOfMaintenanceByStatus = new HashMap<>();
		nearestNextInspection = null;
		mostTasksAircrafts = new ArrayList<>();
		costStatistics = new ArrayList<>();
		durationStatistics = new ArrayList<>();

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
