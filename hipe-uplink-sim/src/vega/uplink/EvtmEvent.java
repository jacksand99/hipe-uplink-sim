package vega.uplink;
import java.text.ParseException;
import java.util.Date;

public class EvtmEvent implements Comparable<EvtmEvent>{
	private Date time;
	public static String EVENT_TYPE_AOS="aos";
	public static String EVENT_TYPE_LOS="los";
	public static String EVENT_TYPE_VIS="vis";
	public static String EVENT_TYPE_ANT="ant";
	public static String EVENT_TYPE_ORB="orb";
	public static String EVENT_TYPE_CON="con";
	public static String EVENT_TYPE_BDI="bdi";
	private String type;
	private String id;
	//private int count;
	private long duration;
	/*private String ems_station;
	private String criteria;
	private int elevation;
	private int ems_rtlt;
	private float distance;
	private int angle;
	private float sundistance;*/
	
	public EvtmEvent(String eventType,String eventId,Date eventTime,long eventDuration){
		type=eventType;
		id=eventId;
		time=eventTime;
		duration=eventDuration;
	}
	
	public String getType(){
		return type;
		
	}
	
	public void setType(String eventType){
		type=eventType;
	}
	public String getId(){
		return id;
	}
	public Date getTime(){
		return time;
	}
	public long getDuration(){
		return duration;
	}
	
	public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
		return DateUtil.DOYToDate(zuluTime);
	}

	public static String dateToZulu(java.util.Date date){
		return DateUtil.dateToDOY(date);
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
		
	}
	public boolean equals(EvtmEvent event) {
	    boolean result = true;
	    if (this.type!=event.getType()) result=false;
	    if (this.id!=event.getId()) result=false;
	    if (this.duration!=event.getDuration()) result=false;
	    if (!this.time.equals(event.getTime())) result=false;
	    
	    return result;
	    
	}

	
	


}
