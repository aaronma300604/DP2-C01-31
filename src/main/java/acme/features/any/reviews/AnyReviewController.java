
package acme.features.any.reviews;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Any;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.review.Review;

@GuiController
public class AnyReviewController extends AbstractGuiController<Any, Review> {

	@Autowired
	AnyReviewsListService	listService;

	@Autowired
	AnyReviewsShowService	showService;

	@Autowired
	AnyReviewsCreateService	createService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);

	}
}
