
package acme.features.any.reviews;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewsCreateService extends AbstractGuiService<Any, Review> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReviewsRepository repository;

	// AbstractService<Authenticated, Consumer> ---------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Review object;

		object = new Review();
		object.setMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Review object) {
		assert object != null;

		super.bindObject(object, "reviewerName", "subject", "text", "score", "recommended");
	}

	@Override
	public void validate(final Review object) {
		assert object != null;
		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "acme.validation.confirmation.message");
	}

	@Override
	public void perform(final Review object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Review object) {
		Dataset dataset;

		dataset = super.unbindObject(object, "reviewerName", "subject", "text", "score", "recommended");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}

}
