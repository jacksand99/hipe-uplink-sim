package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.DoubleParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmEventOrb extends EvtmEvent {
	public static String DISTANCE_TAG="distance";
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
		return ((Double)getMeta().get(DISTANCE_TAG).getValue()).floatValue();
		//return distance;
	}
	
	public void setDistance(float eventSunDistance){
		getMeta().set(DISTANCE_TAG, new DoubleParameter(eventSunDistance));
		//distance=eventSunDistance;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+DISTANCE_TAG+"=\""+this.getDistance()+"\"/>\n";
	}


}
