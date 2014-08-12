package vega.uplink.pointing;

/**
 * An attitude slew is implemented by inserting a slew block in the PTR. A slew block must be
 * placed in between two observation blocks. The duration of slew blocks is defined implicitly
 * by the end time of the previous observation block and the start time of the following
 * observation block.
 * @author jarenas
 *
 */
public class PointingBlockSlew extends PointingBlock {
	private PointingBlock blockBefore;
	private PointingBlock blockAfter;
	
	public PointingBlockSlew(){
		super("SLEW",new java.util.Date(),new java.util.Date());
	}
	
	/**
	 * Set the pointing block before the slew
	 * @param before
	 */
	public void setBlockBefore(PointingBlock before){
		blockBefore=before;
	}
	/**
	 * Set the pointing block after the slew
	 * @param before
	 */
	public void setBlockAfter(PointingBlock after){
		blockAfter=after;
	}
	
	/**
	 * @return The pointing block before the slew or null if it has not been set yet
	 */
	public PointingBlock getBlockBefore(){
		return blockBefore;
	}
	/**
	 * @return The pointing block after the slew or null if it has not been set yet
	 */
	public PointingBlock getBlockAfter(){
		return blockAfter;
	}
	
	public java.util.Date getStartTime(){
		return blockBefore.getEndTime();
	}
	
	public java.util.Date getEndTime(){
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
		return "SlewBlock startTime:"+PointingBlock.dateToZulu(getStartTime())+" endTime:"+PointingBlock.dateToZulu(getEndTime());
	}

}
