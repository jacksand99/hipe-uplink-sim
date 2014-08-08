package vega.uplink.pointing.EvtmEvents;

import herschel.ia.dataset.LongParameter;
import herschel.ia.dataset.StringParameter;

//import rosetta.uplink.pointing.EvtmEvent;


public class EvtmEventSignal extends EvtmEventAnt {
	//private String criteria;
	//private int elevation;
	//private long ems_rtlt;
	
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
		return (String) getMeta().get("criteria").getValue();
		//return criteria;
	}
	
	public void setCriteria(String eventCriteria){
		getMeta().set("criteria", new StringParameter(eventCriteria));
		//criteria=eventCriteria;
	}
	
	public int getElevation(){
		return ((Long) getMeta().get("elevation").getValue()).intValue();

		//return elevation;
	}
	
	public void setElevation(int eventElevation){
		getMeta().set("elevation", new LongParameter(eventElevation));
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
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" ems:station=\""+this.getEms_station()+"\" criteria=\""+this.getCriteria()+"\" elevation=\""+this.getElevation()+"\" ems:rtlt=\""+this.getEms_rtlt()+"\"/>\n";
	}
}
