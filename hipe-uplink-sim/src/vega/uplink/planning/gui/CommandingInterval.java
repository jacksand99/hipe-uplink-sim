package vega.uplink.planning.gui;

import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

class CommandingInterval extends IntervalImpl{
	String name;
	Date startDate;
	Date endDate;
	String instrument;
	CommandingInterval (String name,String instrument,Date startDate,Date endDate){
		super();
		this.name=name;
		this.startDate=startDate;
		this.endDate=endDate;
		this.instrument=instrument;
		this.setBegin(new JaretDate(startDate));
		this.setEnd(new JaretDate(endDate));
		
	}
	
	public String toString(){
		return name+" ["+DateUtil.dateToZulu(startDate)+"-"+DateUtil.dateToZulu(endDate)+"]";
	}
	
	public String getInstrument(){
		return instrument;
	}
}