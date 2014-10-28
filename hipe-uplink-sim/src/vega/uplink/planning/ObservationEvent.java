package vega.uplink.planning;

public class ObservationEvent {
	//public static 
	private String name;
	public static ObservationEvent START_OBS=new ObservationEvent("START_OBS");
	public static ObservationEvent END_OBS=new ObservationEvent("END_OBS");
	
	public ObservationEvent(String name){
		this.setName(name);
		
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public boolean equals(ObservationEvent ev){
		return name.equals(ev.getName());
	}

}
