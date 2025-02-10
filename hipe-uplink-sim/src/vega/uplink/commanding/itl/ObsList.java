package vega.uplink.commanding.itl;

import java.util.Date;
import java.util.Iterator;
import java.util.TreeMap;

import vega.uplink.DateUtil;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;

public class ObsList {
	TreeMap<Date,Observation> list;
	int sed;
	public ObsList(int sed){
		list=new TreeMap<Date,Observation>();
		this.sed=sed;
	}
	public boolean contains(Observation obs){
		return list.values().contains(obs);
	}
	public void put(Observation obs){
		if (contains(obs)) return;
		listPut(obs.getObsStartDate(),obs);
	}
	
	private void listPut(Date date,Observation obs){
		Observation prev = list.get(date);
		if (prev==null) {
			list.put(date,obs);
			return;
		}
		listPut(new Date(date.getTime()+1),obs);
		
	}
	public Integer getIndexOf(Observation obs){
		if (!contains(obs)) return -1;
		Iterator<Observation> it = list.values().iterator();
		int index=-1;
		while (it.hasNext()){
			index++;
			Observation o = it.next();
			if (o.equals(obs)) return index;
		}
		return -1;
	}
	
	public int getCounter(Observation obs){
		int index=getIndexOf(obs);
		if (index==-1) return -1;
		else return sed+index;
	}
	
	public int getCounterAndAdd(Observation obs){
		put(obs);
		return getCounter(obs);
	}
	
	public Iterator<Observation> iterator(){
		return list.values().iterator();
	}
	
	public EventList getEventList(){
		EventList result=new EventList();
		Iterator<Observation> it = iterator();	
		while(it.hasNext()){
			Observation obs=it.next();
			result.put(obs.getObsStartDate(),DateUtil.dateToLiteral(obs.getObsStartDate())+" "+getStartEvent(obs));
			result.put(obs.getObsEndDate(),DateUtil.dateToLiteral(obs.getObsEndDate())+" "+getEndEvent(obs));
			
		}
		return result;
		
	}
	
	public Iterator<String> getEventsIterator(){
		return getEventList().values().iterator();
	}
	
	public String getStartEvent(Observation obs){
		String eventName=ObservationUtil.getEventString(obs);
		String itlEventStart=eventName+"_SO";
		String eventStart=itlEventStart+" (COUNT = "+String.format("%06d", getCounterAndAdd(obs))+" )";
		return eventStart;
	}
	public String getEndEvent(Observation obs){
		String eventName=ObservationUtil.getEventString(obs);
		String itlEventEnd=eventName+"_EO";
		String eventEnd=itlEventEnd+" (COUNT = "+String.format("%06d", getCounterAndAdd(obs))+" )";
		return eventEnd;
	}
	
}
