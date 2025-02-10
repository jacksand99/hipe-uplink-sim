package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.DoubleParameter;
import vega.uplink.pointing.EvtmEvent;

public class EvtmPower extends EvtmEvent {
    public static String MAX_TAG="max";
    //private float distance;
    public String getType(){
        return EvtmEvent.EVENT_TYPE_POWER;
    }
    
    public EvtmPower(String eventId,java.util.Date eventTime,long eventDuration,float eventMax){
        super(EvtmEvent.EVENT_TYPE_ORB,eventId,eventTime,eventDuration);
        setMax(eventMax);
        //distance=eventDistance;
    }
    
    public float getMax(){
        return ((Double)getMeta().get(MAX_TAG).getValue()).floatValue();
        //return distance;
    }
    
    public void setMax(float eventSunDistance){
        getMeta().set(MAX_TAG, new DoubleParameter(eventSunDistance));
        //distance=eventSunDistance;
    }
    
    public String toString(){
        return toString(0);
    }
    public String toString(int count){
        return "<"+this.getType()+" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+MAX_TAG+"=\""+this.getMax()+"\"/>\n";
    }


}
