package vega.uplink.planning;

public interface ObservationListener {
	/**
	 * Method called when an Observation changed but it is not known in which part (pointing, commanding or metadata)
	 * @param event
	 */
	public void observationChanged(ObservationChangeEvent event);
	
	/**
	 * Method called when the schedule has changed globally but not a existing observation (for example if a Observation has been removed or added)
	 */
	public void scheduleChanged();
	
	/**
	 * Method called when the metadata of an observation has changed
	 * @param event
	 */
	public void metadataChanged(ObservationChangeEvent event);
	
	/**
	 * Method called when the pointing of an Observation has changed
	 * @param event
	 */
	public void pointingChanged(ObservationChangeEvent event);
	
	/**
	 * Method called when the commanding part of an Observation has changed
	 * @param event
	 */
	public void commandingChanged(ObservationChangeEvent event);
	
}
