package vega.uplink.commanding.itl;
import vega.uplink.commanding.Sequence;

public class CommandLine {
	private Event event;
	private long delta;
	private String instrument;
	private Sequence sequence;
	
	public CommandLine(Event evt,long del,String ins,Sequence seq){
		event=evt;
		delta=del;
		instrument=ins;
		sequence=seq;
	}
	
	public String toString(){
		String result="";
		
		return result;
	}
}
