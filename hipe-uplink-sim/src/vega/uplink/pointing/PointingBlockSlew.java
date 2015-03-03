package vega.uplink.pointing;

import vega.uplink.DateUtil;

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
		if (blockBefore==null) throw new IllegalArgumentException("Trying to get the start time of slew with no block before");

		return blockBefore.getEndTime();
	}
	
	public java.util.Date getEndTime(){
		if (blockAfter==null) throw new IllegalArgumentException("Trying to get the end time of slew with no block after");
		return blockAfter.getStartTime();
	}
	
	/*public void setStartTime(java.util.Date time){
		//Do nothing
	}
	
	public void setEndTime(java.util.Date time){
		//Do nothing
	}*/

	public String toXml(int indent){
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		return iString+"<block ref='SLEW'/>\n";
	}
	
	public String toString(){
		return "SlewBlock startTime:"+DateUtil.dateToZulu(getStartTime())+" endTime:"+DateUtil.dateToZulu(getEndTime());
	}

}
