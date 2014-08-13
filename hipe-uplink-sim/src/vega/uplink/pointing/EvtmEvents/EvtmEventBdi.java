package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.DoubleParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmEventBdi extends EvtmEvent{
	public static String SUNDISTANCE_TAG="sundistance";
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
		return ((Double) getMeta().get(SUNDISTANCE_TAG).getValue()).floatValue();
		//return sundistance;
	}
	
	public void setSunDistance(float eventSunDistance){
		getMeta().set(SUNDISTANCE_TAG, new DoubleParameter(eventSunDistance));
		//sundistance=eventSunDistance;
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+SUNDISTANCE_TAG+"=\""+this.getSunDistance()+"\"/>\n";
	}

}
