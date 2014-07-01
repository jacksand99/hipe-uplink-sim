package vega.uplink.pointing.EvtmEvents;

//import rosetta.uplink.pointing.EvtmEvent;


public class EvtmEventSignal extends EvtmEventAnt {
	private String criteria;
	private int elevation;
	private long ems_rtlt;
	
	public EvtmEventSignal(String eventType,String eventId,java.util.Date eventTime,long eventDuration,String eventEms_station,String eventCriteria,int eventElevation,long eventEms_rtlt){
		super(eventId,eventTime,eventDuration,eventEms_station);
		this.setType(eventType);
		criteria=eventCriteria;
		elevation=eventElevation;
		ems_rtlt=eventEms_rtlt;
	}
	
	public String getCriteria(){
		return criteria;
	}
	
	public void setCriteria(String eventCriteria){
		criteria=eventCriteria;
	}
	
	public int getElevation(){
		return elevation;
	}
	
	public void setElevation(int eventElevation){
		elevation=eventElevation;
	}
	
	public long getEms_rtlt(){
		return ems_rtlt;
	}
	
	public void setEms_rtlt(long eventEms_rtlt){
		ems_rtlt=eventEms_rtlt;
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		return "<"+this.getType()+" id=\""+this.getId()+"\" time=\""+dateToZulu(this.getTime())+"\" count=\""+count+"\" duration=\""+this.getDuration()+"\" ems:station=\""+this.getEms_station()+"\" criteria=\""+this.getCriteria()+"\" elevation=\""+this.getElevation()+"\" ems:rtlt=\""+this.getEms_rtlt()+"\"/>\n";
	}
}
