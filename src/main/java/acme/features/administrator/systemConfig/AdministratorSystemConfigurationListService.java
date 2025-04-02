
package acme.features.administrator.systemConfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.systemConfig.SystemConfig;

@GuiService
public class AdministratorSystemConfigurationListService extends AbstractGuiService<Administrator, SystemConfig> {

	@Autowired
	private AdministratorSystemConfigurationRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);

	}

	@Override
	public void load() {
		List<SystemConfig> sysConfs;
		sysConfs = this.repository.findAllCurrencies();
		super.getBuffer().addData(sysConfs);
	}

	@Override
	public void unbind(final SystemConfig sysConf) {
		assert sysConf != null;
		Dataset dataset;

		dataset = super.unbindObject(sysConf, "currency", "systemCurrency");

		super.getResponse().addData(dataset);
	}
}
