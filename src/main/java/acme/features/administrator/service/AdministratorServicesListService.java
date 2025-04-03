
package acme.features.administrator.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AdministratorServicesListService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	private AdministratorServicesRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		List<Service> services;

		services = this.repository.findAllServices();

		super.getBuffer().addData(services);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "avgDwellTime");

		super.getResponse().addData(dataset);
	}
}
