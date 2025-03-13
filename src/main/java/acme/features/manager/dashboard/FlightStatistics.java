
package acme.features.manager.dashboard;

public interface FlightStatistics {

	Double getAverage();
	void setAverage(Double average);

	Double getMinimum();
	void setMinimum(Double minimum);

	Double getMaximum();
	void setMaximum(Double maximum);

	Double getStandardDeviation();
	void setStandardDeviation(Double standardDeviation);
}
