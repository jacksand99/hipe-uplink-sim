package vega.uplink.planning;

public interface ObservationListener {
	public void observationChanged(ObservationChangeEvent event);
	
	public void scheduleChanged();
	
	public void metadataChanged(ObservationChangeEvent event);
	
	public void pointingChanged(ObservationChangeEvent event);
	
	public void commandingChanged(ObservationChangeEvent event);
	
}
