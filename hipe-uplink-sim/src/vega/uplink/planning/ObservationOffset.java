package vega.uplink.planning;

/**
 * Pointing offset for Observations
 * @author jarenas
 *
 */
public interface ObservationOffset {
	/**
	 * Set the observation parent of this offset
	 * @param obs
	 */
	public void setParent(Observation obs);
}
