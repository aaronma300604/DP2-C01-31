
package acme.features.any.reviews;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewsShowService extends AbstractGuiService<Any, Review> {

	@Autowired
	private AnyReviewsRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Review review;
		int id;

		id = super.getRequest().getData("id", int.class);
		review = this.repository.findReview(id);

		super.getBuffer().addData(review);
	}

	@Override
	public void unbind(final Review review) {
		assert review != null;

		Dataset dataset;

		dataset = super.unbindObject(review, "reviewerName", "subject", "text", "score", "recommended");
		dataset.put("confirmation", false);

		super.getResponse().addData(dataset);
	}
}
