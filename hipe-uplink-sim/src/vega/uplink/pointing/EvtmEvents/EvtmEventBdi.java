package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.DoubleParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmEventBdi extends EvtmEvent{
	//private float sundistance;
	public String getType(){
		return EvtmEvent.EVENT_TYPE_BDI;
	}
	
	public EvtmEventBdi(String eventId,java.util.Date eventTime,long eventDuration,float eventSundistance){
		super(EvtmEvent.EVENT_TYPE_BDI,eventId,eventTime,eventDuration);
		setSunDistance(eventSundistance);
		//sundistance=eventSundistance;
	}
	
	public float getSunDistance(){
		return ((Double) getMeta().get("sundistance").getValue()).floatValue();
		//return sundistance;
	}
	
	public void setSunDistance(float eventSunDistance){
		getMeta().set("sundistance", new DoubleParameter(eventSunDistance));
		//sundistance=eventSunDistance;
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" sundistance=\""+this.getSunDistance()+"\"/>\n";
	}

}
