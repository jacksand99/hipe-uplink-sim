package vega.uplink.pointing;

import java.util.Date;

import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;

public class PtrChecker {
	public static String checkPtr(Ptr ptr){
		return checkSlewDuration(ptr)+checkGaps(ptr)+checkOffsetDuration(ptr);
	}
	
	public static String checkSlewDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] slews = segment.getAllBlocksOfType("SLEW");
			for (int j=0;j<slews.length;j++){
				if (slews[j].getDuration()<300000){
					messages=messages+"PTR:Slew too short: "+PointingBlock.dateToZulu(slews[j].getStartTime())+" - "+PointingBlock.dateToZulu(slews[j].getEndTime())+"\n";
				}
			}
		}
		
		return messages;
	}
	
	public static String checkGaps(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				//PointingBlock blockAfter = blocks[j+1];
				if (!blockBefore.getEndTime().equals(blocks[j].getStartTime())){
					messages=messages+"PTR:Gap detected between "+PointingBlock.dateToZulu(blockBefore.getEndTime())+" and "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
				}
				if (isSlew(blockBefore) && isSlew(blocks[j])){
					messages=messages+"PTR:Two consecutive slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
				}
				if (!isSlew(blockBefore) && !isSlew(blocks[j])){
					messages=messages+"PTR:Two consecutive blocks without slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
				}

			}
		}
		
		return messages;
	}
	private static boolean isSlew(PointingBlock block){
		boolean result=false;
		if (block.getType().equals("SLEW") || block.getType().equals("MOCM") || block.getType().equals("MWOL") || block.getType().equals("MSLW")){
			result=true;
		}
		return result;
	}
	public static String checkConsecutiveSlews(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				//PointingBlock blockAfter = blocks[j+1];
				if (isSlew(blockBefore) && isSlew(blocks[j])){
					messages=messages+"PTR:Two consecutive slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
				}
			}
		}
		
		return messages;
	}

	public static String checkOffsetDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getAllBlocksOfType("OBS");
			
			for (int j=0;j<blocks.length;j++){
				PointingAttitude att = blocks[j].getAttitude();
				if (att!=null){
					OffsetAngles offset = att.getOffsetAngles();
					if (offset!=null){
						Date offsetendtime=new Date(blocks[j].getEndTime().getTime()-90000);
						Date offsetstarttime=new Date(blocks[j].getStartTime().getTime()+90000);
						
						//Date offsetstarttime
						if (offset.isScan()){
							offsetendtime = ((OffsetScan) offset).getEndDate();
							offsetstarttime =((OffsetScan) offset).getStartDate();

						}
						if (offset.isRaster()){
							offsetendtime = ((OffsetRaster) offset).getEndDate();
							offsetstarttime =((OffsetRaster) offset).getStartDate();
						}
						if (offset.isCustom()){
							offsetendtime = ((OffsetCustom) offset).getEndDate();
							offsetstarttime =((OffsetCustom) offset).getStartDate();

						}
						if (blocks[j].getEndTime().before(new Date(offsetendtime.getTime()+90000))){
							messages=messages+"PTR:The offset period ends less than 90 seconds before the block ends for block at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
						}
						if (blocks[j].getStartTime().after(new Date(offsetstarttime.getTime()-90000))){
							messages=messages+"PTR:The offset period starts less than 90 seconds after the block starts for block at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
						}
						
					}


				}

			}
		}
		
		return messages;
	}


}
