
package acme.features.administrator.systemConfig;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfig.SystemConfig;

@GuiService
public class AdministratorSystemConfigurationDeleteService extends AbstractGuiService<Administrator, SystemConfig> {

	@Autowired
	AdministratorSystemConfigurationRepository repository;


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
	public void bind(final SystemConfig systemConfig) {
		assert systemConfig != null;
		super.bindObject(systemConfig, "currency", "systemCurrency");
	}

	@Override
	public void validate(final SystemConfig systemConfig) {
		assert systemConfig != null;
		super.state(!systemConfig.isSystemCurrency(), "*", "acme.validation.system-currency.current");
	}

	@Override
	public void perform(final SystemConfig systemConfig) {
		assert systemConfig != null;

		this.repository.delete(systemConfig);
	}

	@Override
	public void unbind(final SystemConfig systemConfig) {
		assert systemConfig != null;
		Dataset dataset;

		dataset = super.unbindObject(systemConfig, "currency", "systemCurrency");

		super.getResponse().addData(dataset);
	}
}
