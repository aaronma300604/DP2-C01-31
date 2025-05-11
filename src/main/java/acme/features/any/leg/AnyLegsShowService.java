
package acme.features.any.leg;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.leg.Leg;
import acme.entities.leg.LegStatus;

@GuiService
public class AnyLegsShowService extends AbstractGuiService<Any, Leg> {

	@Autowired
	private AnyLegsRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int id;
		Leg leg;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);
		status = leg != null && !leg.isDraftMode();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Leg leg;
		int id;

		id = super.getRequest().getData("id", int.class);
		leg = this.repository.findLegById(id);

		super.getBuffer().addData(leg);

	}

	@Override
	public void unbind(final Leg leg) {
		Dataset dataset;
		SelectChoices statusChoices;

		statusChoices = SelectChoices.from(LegStatus.class, leg.getStatus());

		dataset = super.unbindObject(leg, "flightNumber", "scheduledDeparture", "scheduledArrival", "duration", "status");
		dataset.put("statuses", statusChoices);
		dataset.put("aircraft", leg.getAircraft().getModel());
		dataset.put("origin", leg.getOrigin().getName());
		dataset.put("destination", leg.getDestination().getName());
		dataset.put("manager", leg.getManager().getIdentity().getName());

		super.getResponse().addData(dataset);
	}
}
