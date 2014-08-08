package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.DoubleParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmEventOrb extends EvtmEvent {
	//private float distance;
	public String getType(){
		return EvtmEvent.EVENT_TYPE_ORB;
	}
	
	public EvtmEventOrb(String eventId,java.util.Date eventTime,long eventDuration,float eventDistance){
		super(EvtmEvent.EVENT_TYPE_ORB,eventId,eventTime,eventDuration);
		setDistance(eventDistance);
		//distance=eventDistance;
	}
	
	public float getDistance(){
		return ((Double)getMeta().get("distance").getValue()).floatValue();
		//return distance;
	}
	
	public void setDistance(float eventSunDistance){
		getMeta().set("distance", new DoubleParameter(eventSunDistance));
		//distance=eventSunDistance;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" distance=\""+this.getDistance()+"\"/>\n";
	}


}
