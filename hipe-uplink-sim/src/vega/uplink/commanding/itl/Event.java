package vega.uplink.commanding.itl;

public class Event {
	private String name;
	private int count;
	private java.util.Date value;
	
	public Event(String eventName,int eventCount,java.util.Date eventValue){
		name=eventName;
		count=eventCount;
		value=eventValue;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCount(){
		
		return count;
	}
	
	public java.util.Date getValue(){
		return value;
	}
}
