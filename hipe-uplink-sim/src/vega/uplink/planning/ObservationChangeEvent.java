package vega.uplink.planning;


/**
 * Event generated when an Observation changed
 * @author jarenas
 *
 */
public class ObservationChangeEvent {
	Observation source;
	public ObservationChangeEvent(Observation source){
		this.source=source;
	}
	

	
	/**
	 * Get the Observation that originates the event
	 * @return
	 */
	public Observation getSource(){
		return source;
	}
}
