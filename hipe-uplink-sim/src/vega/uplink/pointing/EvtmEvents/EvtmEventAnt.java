package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.StringParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmEventAnt extends EvtmEvent {
	//private String ems_station;
	public static String EMS_STATION_TAG="ems:station";
	public EvtmEventAnt(String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station){
		super(EvtmEvent.EVENT_TYPE_ANT,eventId,eventTime,eventDuration);
		if (eventEms_station.equals(null)) eventEms_station="None";

		getMeta().set("ems_station", new StringParameter(eventEms_station));
		//ems_station=eventEms_station;
	}
	
	public String getEms_station(){
		return (String) getMeta().get("ems_station").getValue();
		//return ems_station;
	}
	
	public void setEms_station(String eventEms_station){
		getMeta().set("ems_station", new StringParameter(eventEms_station));

		//ems_station=eventEms_station;
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		if (this.getEms_station().equals("None")){
			return super.toString(count);
		}
		else{
			return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+EMS_STATION_TAG+"=\""+this.getEms_station()+"\"/>\n";
		}
	}

}
