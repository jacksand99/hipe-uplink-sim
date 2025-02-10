package vega.uplink.planning;

import java.util.Date;
import java.util.HashMap;

public class EventFile {
	private HashMap<ObservationEvent,Date> events;
	
	public EventFile(){
		events=new  HashMap<ObservationEvent,Date>();
	}
	
	public void addEvent(ObservationEvent event,Date date){
		events.put(event, date);
	}
	
	public void addEvent(String eventName,Date date){
		events.put(new ObservationEvent(eventName), date);
	}
	
	public Date getDate(ObservationEvent event){
		return events.get(event);
	}
	public Date getDate(String eventName){
		return events.get(new ObservationEvent(eventName));
	}
	
	public void removeEvent(ObservationEvent event){
		events.remove(event);
	}
	public void removeEvent(String eventName){
		events.remove(new ObservationEvent(eventName));
	}
	
	public ObservationEvent[] getAllEvents(){
		ObservationEvent[] result=new ObservationEvent[events.size()];
		result=events.entrySet().toArray(result);
		return result;
	}
	
	
}
