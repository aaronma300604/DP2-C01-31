
package acme.features.administrator.systemConfig;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfig.SystemConfig;

@GuiService
public class AdministratorSystemConfigurationShowService extends AbstractGuiService<Administrator, SystemConfig> {

	@Autowired
	private AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfig systemConfig;
		int id;

		id = super.getRequest().getData("id", int.class);
		systemConfig = this.repository.findSystemCurrencyById(id);

		super.getBuffer().addData(systemConfig);
	}

	@Override
	public void unbind(final SystemConfig systemConfig) {
		assert systemConfig != null;
		Dataset dataset;

		dataset = super.unbindObject(systemConfig, "currency", "systemCurrency");

		super.getResponse().addData(dataset);
	}
}
