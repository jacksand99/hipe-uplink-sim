package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;

public class EvtmEventBdi extends EvtmEvent{
	private float sundistance;
	public String getType(){
		return EvtmEvent.EVENT_TYPE_BDI;
	}
	
	public EvtmEventBdi(String eventId,java.util.Date eventTime,long eventDuration,float eventSundistance){
		super(EvtmEvent.EVENT_TYPE_BDI,eventId,eventTime,eventDuration);
		sundistance=eventSundistance;
	}
	
	public float getSunDistance(){
		return sundistance;
	}
	
	public void setSunDistance(long eventSunDistance){
		sundistance=eventSunDistance;
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" sundistance=\""+this.getSunDistance()+"\"/>\n";
	}

}
