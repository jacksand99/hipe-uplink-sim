package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.LongParameter;
import herschel.ia.dataset.StringParameter;

//import rosetta.uplink.pointing.EvtmEvent;


public class EvtmEventSignal extends EvtmEventAnt {
	//private String criteria;
	//private int elevation;
	//private long ems_rtlt;
	public static String CRITERIA_TAG="criteria";
	public static String ELEVATION_TAG="elevation";
	public static String EMS_RTLT_TAG="ems:rtlt";
	
	public EvtmEventSignal(String eventType,String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station,String eventCriteria,int eventElevation,long eventEms_rtlt){
		super(eventId,eventTime,eventDuration,eventEms_station);
		this.setType(eventType);
		setCriteria(eventCriteria);
		//criteria=eventCriteria;
		setElevation(eventElevation);
		//elevation=eventElevation;
		setEms_rtlt(eventEms_rtlt);
		//ems_rtlt=eventEms_rtlt;
	}
	
	public String getCriteria(){
		return (String) getMeta().get(CRITERIA_TAG).getValue();
		//return criteria;
	}
	
	public void setCriteria(String eventCriteria){
		getMeta().set(CRITERIA_TAG, new StringParameter(eventCriteria));
		//criteria=eventCriteria;
	}
	
	public int getElevation(){
		return ((Long) getMeta().get(ELEVATION_TAG).getValue()).intValue();

		//return elevation;
	}
	
	public void setElevation(int eventElevation){
		getMeta().set(ELEVATION_TAG, new LongParameter(eventElevation));
		//elevation=eventElevation;
	}
	
	public long getEms_rtlt(){
		return ((Long) getMeta().get("ems_rtlt").getValue());
		//return ems_rtlt;
	}
	
	public void setEms_rtlt(long eventEms_rtlt){
		getMeta().set("ems_rtlt", new LongParameter(eventEms_rtlt));

		//ems_rtlt=eventEms_rtlt;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" "+ID_TAG+"=\""+this.getId()+"\" "+TIME_TAG+"=\""+dateToZulu(this.getTime())+"\" "+COUNT_TAG+"=\""+count+"\" "+DURATION_TAG+"=\""+this.getDuration()+"\" "+EMS_STATION_TAG+"=\""+this.getEms_station()+"\" "+CRITERIA_TAG+"=\""+this.getCriteria()+"\" "+ELEVATION_TAG+"=\""+this.getElevation()+"\" "+EMS_RTLT_TAG+"=\""+this.getEms_rtlt()+"\"/>\n";
	}
}
