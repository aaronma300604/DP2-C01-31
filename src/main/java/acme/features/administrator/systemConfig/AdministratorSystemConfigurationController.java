
package acme.features.administrator.systemConfig;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.systemConfig.SystemConfig;

@GuiController
public class AdministratorSystemConfigurationController extends AbstractGuiController<Administrator, SystemConfig> {

	@Autowired
	private AdministratorSystemConfigurationListService		listService;

	@Autowired
	private AdministratorSystemConfigurationShowService		showService;

	@Autowired
	private AdministratorSystemConfigurationCreateService	createService;

	@Autowired
	private AdministratorSystemConfigurationUpdateService	updateService;

	@Autowired
	private AdministratorSystemConfigurationDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
