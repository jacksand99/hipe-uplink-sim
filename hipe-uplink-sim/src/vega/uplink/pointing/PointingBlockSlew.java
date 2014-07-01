package vega.uplink.pointing;

public class PointingBlockSlew extends PointingBlock {
	private PointingBlock blockBefore;
	private PointingBlock blockAfter;
	
	public PointingBlockSlew(){
		super("SLEW",new java.util.Date(),new java.util.Date());
	}
	
	public void setBlockBefore(PointingBlock before){
		blockBefore=before;
	}

	public void setBlockAfter(PointingBlock after){
		blockAfter=after;
	}
	
	public PointingBlock getBlockBefore(){
		return blockBefore;
	}
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
