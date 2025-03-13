
package acme.features.customer.dashboard;

public interface BookingStatistics {

	Double getAverage();
	void setAverage(Double average);

	Double getMaximum();
	void setMaximum(Double maximum);

	Double getMinimum();
	void setMinimum(Double minimum);

	Double getStdDev();
	void setStdDev(Double stdDev);

}
