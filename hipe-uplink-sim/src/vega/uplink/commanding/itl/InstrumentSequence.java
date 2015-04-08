package vega.uplink.commanding.itl;

import java.text.ParseException;
//import java.util.Date;




import vega.uplink.commanding.Parameter;
//import vega.uplink.DateUtil;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;

public class InstrumentSequence extends Sequence{
	private ItlEvent event;
	private String delta;
	//private String instrument;
	InstrumentSequence(String commandName,String uid,String date,String instrument) throws ParseException{
		super(commandName,uid,date);
		//this.instrument=instrument;
		setInstrument(instrument);
		event=null;
		delta=null;
	}
	InstrumentSequence(String commandName,String uid,String flag,char source,char destination,String exTime,Parameter[] param,SequenceProfile[] profiles,String instrument) throws ParseException{
		super(commandName,uid,flag,source,destination,exTime,param,profiles);
		//this.instrument=instrument;
		setInstrument(instrument);
		event=null;
		delta=null;

	}
	
	public ItlEvent getEvent(){
		return event;
	}
	
	public String getDelta(){
		return delta;
	}
	
	public void setEvent(ItlEvent event){
		this.event=event;
	}
	
	public void setDelta(String delta){
		this.delta=delta;
	}
	
	/*public String getInstrumentName(){
		return instrument;
	}*/
}
