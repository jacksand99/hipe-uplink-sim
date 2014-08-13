package vega.uplink.pointing.EvtmEvents;

import vega.uplink.pointing.EvtmEvent;
import herschel.ia.dataset.LongParameter;

import java.util.Date;

//import rosetta.uplink.EvtmEvent;

public class EvtmEventCon extends EvtmEvent {
	//private int angle;
	public static String ANGLE_TAG="angle";
	public EvtmEventCon(String eventId,Date eventTime,long eventDuration,int eventAngle){
		super(EvtmEvent.EVENT_TYPE_CON,eventId,eventTime,eventDuration);
		setAngle(eventAngle);
		//angle=eventAngle;
	}
	
	public int getAngle(){
		return ((Long)getMeta().get(ANGLE_TAG).getValue()).intValue();
		//return angle;
	}
	
	public void setAngle(int eventAngle){
		getMeta().set(ANGLE_TAG, new LongParameter(eventAngle));
		//angle=eventAngle;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+ANGLE_TAG+"=\""+this.getAngle()+"\"/>\n";
	}

	
}
