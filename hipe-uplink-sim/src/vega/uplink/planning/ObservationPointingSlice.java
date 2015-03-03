package vega.uplink.planning;

import vega.uplink.DateUtil;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;

public class ObservationPointingSlice extends PointingBlocksSlice {
	Observation parent;
	public ObservationPointingSlice(Observation obs){
		super();
		parent=obs;
	}
	
	public Observation getObservation(){
		return parent;
	}
	public void setObservation(Observation obs){
		parent=obs;
		PointingBlock[] blocks = this.getBlocks();
		for (int i=0;i<blocks.length;i++){
			((ObservationPointingBlock)blocks[i]).setParent(obs);
		}
	}
	public void regenerate(PointingBlocksSlice slice){
		super.regenerate(slice);
		parent.pointingChange(null);
	}
	public String toObsXml(int indent){
		
			
			String result="";
			String iString="";
			for (int i=0;i<indent;i++){
				iString=iString+"\t";
			}
			result=result+iString+"\t<pointingBlocks>\n";
			PointingBlock[] blocks = getBlocks();
			for (int i=0;i<blocks.length;i++){
				result=result+((ObservationPointingBlock) blocks[i]).toObsXml(2+indent);
			}
			
			result=result+iString+"\t</pointingBlocks>\n";
			
			return result;
	}
	
}
