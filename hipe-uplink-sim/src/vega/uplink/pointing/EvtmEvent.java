package vega.uplink.pointing;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.LongParameter;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;

public class EvtmEvent  extends CompositeDataset implements Comparable<EvtmEvent>{
	//private Date time;
	public static String EVENT_TYPE_AOS="aos";
	public static String EVENT_TYPE_LOS="los";
	public static String EVENT_TYPE_VIS="vis";
	public static String EVENT_TYPE_ANT="ant";
	public static String EVENT_TYPE_ORB="orb";
	public static String EVENT_TYPE_CON="con";
	public static String EVENT_TYPE_BDI="bdi";
    public static String EVENT_TYPE_POWER="POWER";
	
	public static String ID_TAG="id";
	public static String TIME_TAG="time";
	public static String DURATION_TAG="duration";
	public static String COUNT_TAG="count";
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
		getMeta().set(ID_TAG, new StringParameter(eventId));
		getMeta().set(TIME_TAG, new DateParameter(new FineTime(eventTime)));
		getMeta().set(DURATION_TAG, new LongParameter(eventDuration));

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
		return (String) getMeta().get(ID_TAG).getValue();

		//return id;
	}
	public Date getTime(){
		return ((FineTime) getMeta().get(TIME_TAG).getValue()).toDate();

		//return time;
	}
	public long getDuration(){
		return ((Long) getMeta().get(DURATION_TAG).getValue());
		//return duration;
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
		return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\"/>\n";
	}

	@Override
	public int compareTo(EvtmEvent o) {
		return this.getTime().compareTo(o.getTime());
		//return 0;
	}
	public boolean equals(EvtmEvent e) {
	    boolean result=true;
	    if (this.getId()!=e.getId()) result=false;
	    if (this.getDuration()!=this.getDuration()) result=false;
	    if (!this.getTime().equals(e.getTime())) result=false;
	    return result;
	    
	}

	
	


}
