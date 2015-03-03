package vega.uplink.commanding.itl;
import java.util.Date;
import java.util.TreeMap;

public class EventList extends TreeMap<Date,String>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public EventList(){
		super();
	}
	public String put(Date date,String event){
		String prev = super.get(date);
		if (prev==null) return super.put(date, event);
		if (prev.equals(event)) return event;
		return put(new Date(date.getTime()+1),event);
	}
}
