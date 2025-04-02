
package acme.features.any.reviews;

import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.principals.Any;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.review.Review;

@GuiService
public class AnyReviewsListService extends AbstractGuiService<Any, Review> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AnyReviewsRepository repository;

	// AbstractRestService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {

		List<Review> output = this.repository.findAllReviews(MomentHelper.deltaFromCurrentMoment(-1, ChronoUnit.YEARS));

		super.getBuffer().addData(output);
	}

	@Override
	public void unbind(final Review review) {
		Dataset dataset;

		dataset = super.unbindObject(review, "text");
		super.getResponse().addData(dataset);
	}
}
