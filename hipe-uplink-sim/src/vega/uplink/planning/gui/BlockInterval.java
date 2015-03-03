package vega.uplink.planning.gui;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

class BlockInterval extends IntervalImpl{
	PointingBlock block;
	BlockInterval (PointingBlock block){
		super();
		this.block=block;
		//JaretDate.setJaretDateFormatter(new DateFormatter());
		//JaretDate jaretBegin = new JaretDate(block.getStartTime());
		//jaretBegin.setJaretDateFormatter(new DateFormatter());
		this.setBegin(new JaretDate(block.getStartTime()));
		this.setEnd(new JaretDate(block.getEndTime()));
		
	}
	
	public String toString(){
		return block.getType()+" ["+DateUtil.dateToZulu(block.getStartTime())+"-"+DateUtil.dateToZulu(block.getEndTime())+"]";
	}
	
	public PointingBlock getBlock(){
		return block;
	}
	
	public String getInstrument(){
		return block.getInstrument();
	}
}
