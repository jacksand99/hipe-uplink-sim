package vega.uplink.pointing;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.LongParameter;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;

import java.text.ParseException;
import java.util.Date;

public class EvtmEvent  extends CompositeDataset implements Comparable<EvtmEvent>{
	//private Date time;
	public static String EVENT_TYPE_AOS="aos";
	public static String EVENT_TYPE_LOS="los";
	public static String EVENT_TYPE_VIS="vis";
	public static String EVENT_TYPE_ANT="ant";
	public static String EVENT_TYPE_ORB="orb";
	public static String EVENT_TYPE_CON="con";
	public static String EVENT_TYPE_BDI="bdi";
	//private String type;
	//private String id;
	//private int count;
	//private long duration;
	/*private String ems_station;
	private String criteria;
	private int elevation;
	private int ems_rtlt;
	private float distance;
	private int angle;
	private float sundistance;*/
	
	public EvtmEvent(String eventType,String eventId,Date eventTime,long eventDuration){
		getMeta().set("type", new StringParameter(eventType));
		//type=eventType;
		getMeta().set("id", new StringParameter(eventId));
		getMeta().set("time", new DateParameter(new FineTime(eventTime)));
		getMeta().set("duration", new LongParameter(eventDuration));

		//id=eventId;
		//time=eventTime;
		//duration=eventDuration;
	}
	public EvtmEvent copy(){
		return new EvtmEvent(getType(),getId(),getTime(),getDuration());
	}
	
	public String getType(){
		return (String) getMeta().get("type").getValue();
		
	}
	
	public void setType(String eventType){
		getMeta().set("type", new StringParameter(eventType));

		//type=eventType;
	}
	public String getId(){
		return (String) getMeta().get("id").getValue();

		//return id;
	}
	public Date getTime(){
		return ((FineTime) getMeta().get("time").getValue()).toDate();

		//return time;
	}
	public long getDuration(){
		return ((Long) getMeta().get("duration").getValue());
		//return duration;
	}
	
	public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime);
	}

	public static String dateToZulu(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss.SSS'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\"/>\n";
	}

	@Override
	public int compareTo(EvtmEvent o) {
		return this.getTime().compareTo(o.getTime());
		//return 0;
	}

	
	


}
