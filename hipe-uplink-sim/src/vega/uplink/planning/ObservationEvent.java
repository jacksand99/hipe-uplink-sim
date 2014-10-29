package vega.uplink.planning;

/**
 * An event that drives the times in the sequences and pointing of an Observation.
 * By default, every observation has 2 events, start time and end time of the observation
 * @author jarenas
 *
 */
public class ObservationEvent {
	private String name;
	/**
	 * START_OBS
	 */
	public static ObservationEvent START_OBS=new ObservationEvent("START_OBS");
	/**
	 * END_OBS
	 */
	public static ObservationEvent END_OBS=new ObservationEvent("END_OBS");
	
	/**
	 * Generates and Observation Event with the given name
	 * @param name
	 */
	public ObservationEvent(String name){
		this.setName(name);
		
	}
	/**
	 * Get the name of this event
	 * @return
	 */
	public String getName(){
		return name;
	}
	/**
	 * Set the name of this event
	 * @param name
	 */
	public void setName(String name){
		this.name=name;
	}
	public boolean equals(ObservationEvent ev){
		return name.equals(ev.getName());
	}

}
