
package acme.features.technician.dashboard;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
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

		int technicianId = super.getRequest().getPrincipal().getActiveRealm().getId();

		numberOfMaintenanceByStatus = this.repository.findNumberOfMaintenanceByStatus();
		nearestNextInspection = this.repository.findNextInspectionByTechnician(technicianId).get(0);
		mostTasksAircrafts = this.repository.findTopAircraftsByTaskCount(technicianId).stream().limit(5).toList();

		Date currentDate = MomentHelper.getCurrentMoment();
		Calendar cal = Calendar.getInstance();
		cal.setTime(currentDate);
		cal.add(Calendar.YEAR, -1);
		costStatistics = this.repository.findCostStatistics(cal.getTime(), technicianId);
		durationStatistics = this.repository.findDurationStatistics(technicianId);

		//TODO: Show dashboard in frontend.

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
