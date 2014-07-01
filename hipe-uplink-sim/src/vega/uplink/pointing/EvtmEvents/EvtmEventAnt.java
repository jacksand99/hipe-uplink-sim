package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

public class EvtmEventAnt extends EvtmEvent {
	private String ems_station;
	public EvtmEventAnt(String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station){
		super(EvtmEvent.EVENT_TYPE_ANT,eventId,eventTime,eventDuration);
		ems_station=eventEms_station;
	}
	
	public String getEms_station(){
		return ems_station;
	}
	
	public void setEms_station(String eventEms_station){
		ems_station=eventEms_station;
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		if (ems_station==null){
			return super.toString(count);
		}
		else{
			return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" ems:station=\""+this.getEms_station()+"\"/>\n";
		}
	}

}
