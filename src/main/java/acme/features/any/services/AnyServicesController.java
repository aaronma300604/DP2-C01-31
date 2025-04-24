
package acme.features.any.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.service.Service;

@GuiController
public class AnyServicesController extends AbstractGuiController<Any, Service> {

	@Autowired
	private AnyServicesListService	listService;

	@Autowired
	private AnyServicesShowService	showService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
	}
}
