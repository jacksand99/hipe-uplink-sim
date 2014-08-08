package vega.uplink.pointing;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;

import java.util.HashMap;
import java.util.Date;

public class Evtm extends Product{
	private HashMap<Date,EvtmEvent> eventsMap;
	//private Date generationTime;
	//private Date validityStart;
	//private Date validityEnd;
	//private String spacecraft;
	//private String icdVersion;
	
	public Evtm(Date evtmGenerationTime,Date evtmValidityStart,Date evtmValidityEnd,String evtmSpacecraft,String evtmIcdVersion){
		super();
		//eventsMap=new HashMap<Date,EvtmEvent>();
		this.setCreationDate(new FineTime(evtmGenerationTime));
		//generationTime=evtmGenerationTime;
		this.setStartDate(new FineTime(evtmValidityStart));
		//validityStart=evtmValidityStart;
		this.setEndDate(new FineTime(evtmValidityEnd));
		getMeta().set("spacecraft", new StringParameter(evtmSpacecraft));
		getMeta().set("icdVersion", new StringParameter(evtmIcdVersion));

		//validityEnd=evtmValidityEnd;
		//spacecraft=evtmSpacecraft;
		//icdVersion=evtmIcdVersion;
	}
	
	public Evtm(Date evtmGenerationTime,Date evtmValidityStart,Date evtmValidityEnd,String evtmIcdVersion){
		this(evtmGenerationTime,evtmValidityStart,evtmValidityEnd,"ROS",evtmIcdVersion);
	}
	
	public Evtm(Date evtmValidityStart,Date evtmValidityEnd,String evtmIcdVersion){
		this(new java.util.Date(),evtmValidityStart,evtmValidityEnd,"ROS",evtmIcdVersion);
	}
	
	public Evtm(Date evtmValidityStart,Date evtmValidityEnd){
		this(new java.util.Date(),evtmValidityStart,evtmValidityEnd,"ROS","PLID-0.0");
	}
	
	public Evtm(){
		this(new java.util.Date(),new java.util.Date(),new java.util.Date(),"ROS","PLID-0.0");
	}
	
	public Date getGenerationTime(){
		return this.getCreationDate().toDate();
		//return generationTime;
	}
	
	public void setGenerationTime(Date evtmGenerationTime){
		this.setCreationDate(new FineTime(evtmGenerationTime));

		//generationTime=evtmGenerationTime;
	}
	
	public Date getValidityStart(){
		return this.getStartDate().toDate();
		//return validityStart;
	}
	
	public void setValidityStart(Date evtmValidityStart){
		this.setStartDate(new FineTime(evtmValidityStart));

		//validityStart=evtmValidityStart;
	}
	
	public void setValidityEnd(Date evtmValidityEnd){
		//return this.getEndDate().toDate();
		this.setEndDate(new FineTime(evtmValidityEnd));

		//validityEnd=evtmValidityEnd;
	}
	
	public void setSpacecraft(String evtmSpacecraft){
		getMeta().set("spacecraft", new StringParameter(evtmSpacecraft));

		//spacecraft=evtmSpacecraft;
	}
	
	public void setIcdVersion(String evtmIcdVersion){
		getMeta().set("icdVersion", new StringParameter(evtmIcdVersion));

		//icdVersion=evtmIcdVersion;
	}
	
	public Date getValidityEnd(){
		return this.getEndDate().toDate();
		//return validityEnd;
	}
	
	public String getSpacecraft(){
		return (String) getMeta().get("spacecraft").getValue();
		//return spacecraft;
	}
	
	public String getIcdVersion(){
		return (String) getMeta().get("icdVersion").getValue();

		//return icdVersion;
	}
	
	public void addEvent(EvtmEvent event){
		this.set(EvtmEvent.dateToZulu(event.getTime()), event);
		//eventsMap.put(event.getTime(), event);
	}
	
	public EvtmEvent getEvent(Date time){
		return (EvtmEvent)get(EvtmEvent.dateToZulu(time));
		//return eventsMap.get(time);
	}
	
	public EvtmEvent[] getAllEvents(){
		EvtmEvent[] result=new EvtmEvent[this.getSets().size()];
		this.getSets().values().toArray(result);
		//EvtmEvent[] result=new EvtmEvent[eventsMap.size()];
		//eventsMap.values().toArray(result);
		java.util.Arrays.sort(result);
		return result;
	}
	
	public EvtmEvent[] getEventsByType(String type){
		java.util.Vector<EvtmEvent> resultVector=new java.util.Vector<EvtmEvent>();
		EvtmEvent[] allEvents=getAllEvents();
		for (int i=0;i<allEvents.length;i++){
			if (allEvents[i].getType().equals(type)) resultVector.add(allEvents[i]);
		}
		EvtmEvent[] result=new EvtmEvent[resultVector.size()];
		resultVector.toArray(result);
		return result;
	}
	
	public String toString(){
		java.util.HashMap<String,Integer> counters=new java.util.HashMap<String,Integer>();
		String result="";
		result=result+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		result=result+"<eventfile xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://esa.esoc.events rosetta_event_definitions.xsd\" xmlns=\"http://esa.esoc.events\" xmlns:ems=\"http://esa.esoc.ems\">\n";
		result=result+"\t<header format_version=\"1\" spacecraft=\""+this.getSpacecraft()+"\" icd_version=\""+this.getIcdVersion()+"\" gen_time=\""+EvtmEvent.dateToZulu(this.getGenerationTime())+"\" validity_start=\""+EvtmEvent.dateToZulu(this.getValidityStart())+"\" validity_end=\""+EvtmEvent.dateToZulu(this.getValidityEnd())+"\"/>\n";
		result=result+"\t<events>\n";
		EvtmEvent[] allEvents=this.getAllEvents();
		for (int i=0;i<allEvents.length;i++){
			String key=allEvents[i].getId();
			Integer temp=counters.get(key);
			if (temp==null) temp=1;
			
			result=result+"\t\t"+allEvents[i].toString(temp);
			counters.put(key, temp+1);
		}
		result=result+"\t</events>\n";
		result=result+"</eventfile>";
		return result;
		
	}
}
