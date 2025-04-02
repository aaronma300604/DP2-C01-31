
package acme.features.administrator.systemConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfig.SystemConfig;

@GuiService
public class AdministratorSystemConfigurationCreateService extends AbstractGuiService<Administrator, SystemConfig> {

	@Autowired
	AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SystemConfig systemConfig;

		systemConfig = new SystemConfig();

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
		;
	}

	@Override
	public void perform(final SystemConfig systemConfig) {
		assert systemConfig != null;
		if (systemConfig.isSystemCurrency()) {
			List<SystemConfig> scs = this.repository.findAllSystemCurrencies();
			scs.stream().forEach(sc -> sc.setSystemCurrency(false));
		}

		this.repository.save(systemConfig);
	}

	@Override
	public void unbind(final SystemConfig systemConfig) {
		assert systemConfig != null;

		Dataset dataset;

		dataset = super.unbindObject(systemConfig, "currency", "systemCurrency");

		super.getResponse().addData(dataset);
	}
}
