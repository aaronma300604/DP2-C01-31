
package acme.features.manager.dashboard;

public interface FlightStatistics {

	Double getAverage();
	void setAverage();

	Double getMinimum();
	void setMinimum();

	Double getMaximum();
	void setMaximum();

	Double getStandardDeviation();
	void setStandardDeviation();
}
