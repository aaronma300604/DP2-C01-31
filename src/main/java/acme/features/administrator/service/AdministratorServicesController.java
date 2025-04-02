
package acme.features.administrator.service;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.service.Service;

@GuiController
public class AdministratorServicesController extends AbstractGuiController<Administrator, Service> {

	@Autowired
	private AdministratorServicesListService	listService;

	@Autowired
	private AdministratorServicesShowService	showService;

	@Autowired
	private AdministratorServicesCreateService	createService;

	@Autowired
	private AdministratorServicesUpdateService	updateService;

	@Autowired
	private AdministratorServicesDeleteService	deleteService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
	}
}
