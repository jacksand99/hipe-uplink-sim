package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

import java.util.Date;

//import rosetta.uplink.EvtmEvent;

public class EvtmEventCon extends EvtmEvent {
	private int angle;
	public EvtmEventCon(String eventId,Date eventTime,long eventDuration,int eventAngle){
		super(EvtmEvent.EVENT_TYPE_CON,eventId,eventTime,eventDuration);
		angle=eventAngle;
	}
	
	public int getAngle(){
		return angle;
	}
	
	public void setAngle(int eventAngle){
		angle=eventAngle;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" angle=\""+this.getAngle()+"\"/>\n";
	}

	
}