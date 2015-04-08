package vega.uplink.commanding.itl;

import vega.uplink.planning.ObservationEvent;

public class ItlEvent extends ObservationEvent{
	//private String name;
	//private int count;
	
	public ItlEvent(String event){
		//if (event==null) throw(new IllegalArgumentException("Null is not allowed as Itl Event name"));
		super(event);
		if (event==null) throw(new IllegalArgumentException("Null is not allowed as Itl Event name"));

	}
	public ItlEvent(String eventName,Integer eventCount){
		//if (eventCount==null) eventCount=-1;
		super(eventName+"_COUNT_"+eventCount);
		//name=eventName;
		//count=eventCount;
	}
	
	public String getItlEventName(){
		String tempName = this.getName();
		int index = tempName.indexOf("_COUNT_");
		return tempName.substring(0,index);
	}
	
	public int getItlEventCount(){
		String tempName = this.getName();
		int index = tempName.indexOf("_COUNT_");
		return Integer.parseInt(tempName.substring(index+7));
	}
	
	public String toString(){
		if (getItlEventCount()>-1){
			return getItlEventName()+" (COUNT="+getItlEventCount()+")";
		}else{
			return getItlEventName();
		}
	}
	
	public boolean equals(ItlEvent other){
		if (toString().equals(other.toString())) return true;
		else return false;
	}
	

	
	
}
