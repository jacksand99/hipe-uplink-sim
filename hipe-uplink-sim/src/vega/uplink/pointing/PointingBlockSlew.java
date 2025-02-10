package vega.uplink.pointing;

import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.Properties;

/**
 * An attitude slew is implemented by inserting a slew block in the PTR. A slew block must be
 * placed in between two observation blocks. The duration of slew blocks is defined implicitly
 * by the end time of the previous observation block and the start time of the following
 * observation block.
 * @author jarenas
 *
 */
public class PointingBlockSlew extends PointingBlock {
	private PointingBlockInterface blockBefore;
	private PointingBlockInterface blockAfter;
	
	public PointingBlockSlew(){
		super("SLEW",new java.util.Date(),new java.util.Date());
	}
	public PointingBlockSlew(String type,Date startTime,Date endTime) {
	    super(type,startTime,endTime);
	}
	public PointingBlockSlew(PointingElement pb) {
	    super(pb);
	}
	
	/**
	 * Set the pointing block before the slew
	 * @param before
	 */
	public void setBlockBefore(PointingBlockInterface before){
		if (before!=null) if (before.getType().equals("SLEW")) throw new IllegalArgumentException("Trying to put two consecutive slews "+DateUtil.dateToZulu(before.getStartTime()));
		blockBefore=before;
	}
	/**
	 * Set the pointing block after the slew
	 * @param before
	 */
	public void setBlockAfter(PointingBlockInterface after){
		if (after!=null) if (after.getType().equals("SLEW")) throw new IllegalArgumentException("Trying to put to consecutive slews "+DateUtil.dateToZulu(after.getEndTime()));
		blockAfter=after;
	}
	
	/**
	 * @return The pointing block before the slew or null if it has not been set yet
	 */
	public PointingBlockInterface getBlockBefore(){
		return blockBefore;
	}
	/**
	 * @return The pointing block after the slew or null if it has not been set yet
	 */
	public PointingBlockInterface getBlockAfter(){
		return blockAfter;
	}
	
	public java.util.Date getStartTime(){
	    if (blockBefore!=null) {
	        return blockBefore.getEndTime();
	    }
	    else {

	            if (super.getStartTime()!=null) {
	                return super.getStartTime();
	            }else {
	                throw new IllegalArgumentException("Trying to get the start time of slew with no block before");

	            }
	    }

		
	}
	
	public java.util.Date getEndTime(){
	       if (blockAfter!=null) {
	            return blockAfter.getEndTime();
	        }
	        else {

	                if (super.getEndTime()!=null) {
	                    return super.getEndTime();
	                }else {
	                    throw new IllegalArgumentException("Trying to get the start time of slew with no block before");

	                }
	        }

	    
	}
	
	/*public void setStartTime(java.util.Date time){
		//Do nothing
	}
	
	public void setEndTime(java.util.Date time){
		//Do nothing
	}*/

	public String toXml(int indent){
	    String format = herschel.share.util.Configuration.getProperty(Properties.POINTING_DATE_FORMAT);
	    String formatTag="";
	    if (format.equals("DOY")) {
	        formatTag=" format=\"DOY\"";
	    }
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
	    if (blockBefore==null) {

	        return iString+"<block ref='SLEW'>\n"+iString+"\t<startTime"+formatTag+">"+DateUtil.dateToDOYNoMilli(getStartTime())+"</startTime>\n"+iString+"</block>";
	    }
	       if (blockAfter==null) {
	           PointingElement time = this.getChild("endTime");
	            return iString+"<block ref='SLEW'>\n"+iString+"\t<endTime\"+formatTag+\">"+DateUtil.dateToDOYNoMilli(getEndTime())+"</endTime>\n"+iString+"</block>";
	        }
		return iString+"<block ref='SLEW'/>\n";
	}
	
	public String toString(){
		return "SlewBlock startTime:"+DateUtil.dateToZulu(getStartTime())+" endTime:"+DateUtil.dateToZulu(getEndTime());
	}

}
