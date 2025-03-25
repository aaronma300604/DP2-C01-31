
package acme.forms.administrator;

public interface ReviewsStatistics {

	Integer getCountReviews();
	void setCount(Integer count);

	Double getAverage();
	void setAverage(Double average);

	Integer getMinimum();
	void setMinimum(Integer minimum);

	Integer getMaximum();
	void setMaximum(Integer maximum);

	Double getStandardDeviation();
	void setStandardDeviation(Double standardDeviation);
}
