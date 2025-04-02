
package acme.features.any.reviews;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.review.Review;

@Repository
public interface AnyReviewsRepository extends AbstractRepository {

	@Query("select r from Review r where r.moment > :date")
	List<Review> findAllReviews(Date date);

	@Query("select r from Review r where r.id = :id")
	Review findReview(int id);

}
