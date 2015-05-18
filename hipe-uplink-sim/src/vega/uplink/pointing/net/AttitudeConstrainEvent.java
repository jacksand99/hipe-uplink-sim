package vega.uplink.pointing.net;

import java.util.Date;

import vega.uplink.pointing.EvtmEvent;

public class AttitudeConstrainEvent implements Comparable<AttitudeConstrainEvent> {
	public String type;
	public int counter;
	public Date start;
	public Date end;
	public float min;
	public float max;
	public String name;
	
	public AttitudeConstrainEvent(String type,int counter,Date start,Date end,float min,float max,String name){
		this.type=type;
		this.counter=counter;
		this.start=start;
		this.end=end;
		this.min=min;
		this.max=max;
		this.name=name;
	}
	
	public String toString(){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return type+" "+counter+" "+dateFormat.format(start)+" "+dateFormat.format(end)+" "+min+" "+max+" "+name;
	}
	public int compareTo(AttitudeConstrainEvent o) {
		return this.start.compareTo(o.start);
		//return 0;
	}

}
