package vega.uplink;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Date;

public class Evtm {
	//private HashMap<Date,EvtmEvent> eventsMap;
    private HashMap<EvtmEvent,Date> eventsMap;
	private Date generationTime;
	private Date validityStart;
	private Date validityEnd;
	private String spacecraft;
	private String icdVersion;
	
	public Evtm(Date evtmGenerationTime,Date evtmValidityStart,Date evtmValidityEnd,String evtmSpacecraft,String evtmIcdVersion){
		//eventsMap=new HashMap<Date,EvtmEvent>();
	    eventsMap=new HashMap<EvtmEvent,Date>();
		generationTime=evtmGenerationTime;
		validityStart=evtmValidityStart;
		validityEnd=evtmValidityEnd;
		spacecraft=evtmSpacecraft;
		icdVersion=evtmIcdVersion;
	}
	
	public Evtm(Date evtmGenerationTime,Date evtmValidityStart,Date evtmValidityEnd,String evtmIcdVersion){
		this(evtmGenerationTime,evtmValidityStart,evtmValidityEnd,"SOLO",evtmIcdVersion);
	}
	
	public Evtm(Date evtmValidityStart,Date evtmValidityEnd,String evtmIcdVersion){
		this(new java.util.Date(),evtmValidityStart,evtmValidityEnd,"SOLO",evtmIcdVersion);
	}
	
	public Evtm(Date evtmValidityStart,Date evtmValidityEnd){
		this(new java.util.Date(),evtmValidityStart,evtmValidityEnd,"SOLO","SGS-FD ICD 2.7");
	}
	
	public Evtm(){
		this(new java.util.Date(),new java.util.Date(),new java.util.Date(),"SOLO","SGS-FD ICD 2.7");
	}
	
	public Date getGenerationTime(){
		return generationTime;
	}
	
	public void setGenerationTime(Date evtmGenerationTime){
		generationTime=evtmGenerationTime;
	}
	
	public Date getValidityStart(){
		return validityStart;
	}
	
	public void setValidityStart(Date evtmValidityStart){
		validityStart=evtmValidityStart;
	}
	
	public void setValidityEnd(Date evtmValidityEnd){
		validityEnd=evtmValidityEnd;
	}
	
	public void setSpacecraft(String evtmSpacecraft){
		spacecraft=evtmSpacecraft;
	}
	
	public void setIcdVersion(String evtmIcdVersion){
		icdVersion=evtmIcdVersion;
	}
	
	public Date getValidityEnd(){
		return validityEnd;
	}
	
	public String getSpacecraft(){
		return spacecraft;
	}
	
	public String getIcdVersion(){
		return icdVersion;
	}
	
	public void addEvent(EvtmEvent event){

		eventsMap.put(event, event.getTime());
	}
	
	public EvtmEvent[] getAllEventsAt(Date time) {
	    java.util.Vector<EvtmEvent> allEventsResult=new java.util.Vector<EvtmEvent>();
	    Set<EvtmEvent> allEvents = eventsMap.keySet();
	    Iterator<EvtmEvent> it = allEvents.iterator();
	    while (it.hasNext()) {
	            EvtmEvent e = it.next();
	            if (e.getTime().equals(time)) allEventsResult.add(e);
	    }
	    return (EvtmEvent[]) allEventsResult.toArray();
	        
	    
	}
	public EvtmEvent getEvent(Date time){
	    EvtmEvent result = null;
	    Set<EvtmEvent> allEvents = eventsMap.keySet();
	    Iterator<EvtmEvent> it = allEvents.iterator();
	    while (it.hasNext()) {
	        EvtmEvent e = it.next();
	        if (e.getTime().equals(time)) result=e;
	    }
		return result;
	}
	
	public EvtmEvent[] getAllEvents(){
	    Set<EvtmEvent> allEvents = eventsMap.keySet();
	    return (EvtmEvent[]) allEvents.toArray();
	    /*
		EvtmEvent[] result=new EvtmEvent[eventsMap.size()];
		eventsMap.values().toArray(result);
		java.util.Arrays.sort(result);
		return result;*/
	    
	}
	public boolean contains(EvtmEvent e) {
	    return eventsMap.containsKey(e);
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
