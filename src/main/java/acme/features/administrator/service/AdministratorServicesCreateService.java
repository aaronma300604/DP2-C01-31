
package acme.features.administrator.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Administrator;
import acme.client.helpers.MomentHelper;
import acme.client.helpers.StringHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.service.Service;

@GuiService
public class AdministratorServicesCreateService extends AbstractGuiService<Administrator, Service> {

	@Autowired
	private AdministratorServicesRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Service service;

		service = new Service();

		super.getBuffer().addData(service);
	}

	@Override
	public void bind(final Service service) {
		super.bindObject(service, "name", "picture", "avgDwellTime", "promotionCode", "discountApplied");
	}

	@Override
	public void validate(final Service service) {
		boolean availableCurrency;
		List<String> currencies;
		currencies = this.repository.finAllCurrencies();
		String currency;
		currency = super.getRequest().getData("discountApplied", String.class);
		if (!StringHelper.isBlank(currency)) {
			currency = currency.length() >= 3 ? currency.substring(0, 3).toUpperCase() : currency;
			availableCurrency = currencies.contains(currency);

			super.state(availableCurrency, "discountApplied", "acme.validation.invalid-currency.message");
		}

		boolean uniquecode;
		Service existingService;

		existingService = this.repository.findServiceByPromotionCode(service.getPromotionCode());
		uniquecode = existingService == null || existingService.equals(service);
		super.state(uniquecode, "promotionCode", "acme.validation.service.duplicated-code.message");

		boolean goodYear;

		if (service.getPromotionCode() != null && !service.getPromotionCode().isBlank()) {
			Date currentMoment = MomentHelper.getCurrentMoment();
			int year = currentMoment.getYear();
			String lastTwoDigits = String.valueOf(year);
			lastTwoDigits = lastTwoDigits.substring(lastTwoDigits.length() - 2);
			goodYear = service.getPromotionCode().endsWith(lastTwoDigits);
			super.state(goodYear, "promotionCode", "acme.validation.service.bad-year-promotion-code.message");
		}
	}

	@Override
	public void perform(final Service service) {
		if (StringHelper.isBlank(service.getPromotionCode()))
			service.setPromotionCode(null);
		this.repository.save(service);
	}

	@Override
	public void unbind(final Service service) {
		Dataset dataset;

		dataset = super.unbindObject(service, "name", "picture", "avgDwellTime", "promotionCode", "discountApplied");

		super.getResponse().addData(dataset);
	}
}
