package vega.uplink.pointing;

import java.util.Date;
import java.util.NoSuchElementException;

import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;

public class PtrChecker {
	public static String checkPtr(Ptr ptr){
		//return checkSlewDuration(ptr)+checkGaps(ptr)+checkOffsetDuration(ptr)+checkInternalSlewsDuration(ptr);
		return checkBlockDuration(ptr)+checkGaps(ptr)+checkOffsetDuration(ptr)+checkInternalSlewsDuration(ptr);

		//return checkSlewDuration(ptr)+checkGaps(ptr);
	}
	
	
	public static String comparePtrs(Ptr fptr,Ptr sptr){
		int nBlocksf = fptr.getSegments()[0].getBlocks().length;
		int nBlockss = sptr.getSegments()[0].getBlocks().length;
		Ptr ptr1=null;
		Ptr ptr2=null;
		if (nBlocksf>nBlockss){
			ptr2=fptr;
			ptr1=sptr;
		}else{
			ptr2=sptr;
			ptr1=fptr;
		}
		String result="";
		/*if (ptr1!=null){
			String checkPtr1 = checkPtr(ptr1);
			if (!checkPtr1.equals("")){
				result=result+"**************************\nPTR 1 failed sanity check\n**************************\n"+checkPtr1;
			}
		}
		if (ptr2!=null){
			String checkPtr2 = checkPtr(ptr2);
			if (!checkPtr2.equals("")){
				result=result+"**************************\nPTR 2 failed sanity check\n**************************\n"+checkPtr2;
			}
		}*/

		if (ptr2!=null){
			
			PtrSegment ptr1Segment = ptr1.getSegments()[0];
			PtrSegment ptr2Segment = ptr2.getSegment(ptr1Segment.getName());
			if (!ptr1Segment.getStartDate().equals(ptr2Segment.getStartDate())){
				result=result+"Start date of the "+ptr1.getName()+" and the "+ptr2.getName()+" segments are not the same:\n";
				result=result+"PTR segment starts at:"+	ptr1Segment.getStartDate().toDate()+"\n";
				result=result+"PTSL segment starts at:"+	ptr2Segment.getStartDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			if (!ptr1Segment.getEndDate().equals(ptr2Segment.getEndDate())){
				result=result+"End date of the "+ptr1.getName()+" and the "+ptr2.getName()+" segments are not the same:\n";
				result=result+"PTR segment ends at:"+	ptr1Segment.getEndDate().toDate()+"\n";
				result=result+"PTSL segment ends at:"+	ptr2Segment.getEndDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			PointingBlock[] ptr2Blocks = ptr2Segment.getBlocks();
			for (int i=0;i<ptr2Blocks.length;i++){
				PointingBlock ptr2Block = ptr2Blocks[i];
				if (!ptr2Block.getType().equals(PointingBlock.TYPE_SLEW)){
					PointingBlock ptrBlock = ptr1Segment.getBlockAt(ptr2Block.getStartTime());
					if (ptrBlock==null){
						result=result+"Block not found in "+ptr1.getName()+":\n";
						result=result+ptr2Block.toXml(1)+"\n";
					}else{
						if (!ptr2Block.equals(ptrBlock)){
							result=result+"Block in "+ptr1.getName()+" different to same block in "+ptr2.getName()+":\n";
							result=result+"Block in "+ptr2.getName()+":\n";
							result=result+ptr2Block.toXml(0)+"\n";
							result=result+"Blocks in "+ptr1.getName()+":\n";
							PointingBlock[] blocksToReport = ptr1Segment.getBlocksAt(ptr2Block.getStartTime(),ptr2Block.getEndTime());
							for (int j=0;j<blocksToReport.length;j++){
								result=result+blocksToReport[j].toXml(0)+"\n";
								
							}
							//result=result+ptrBlock.toXml(0)+"\n";
							result=result+"--------------------------------------\n";
						}
					}
				}
			}
			return result;
		}else return "PTR2 is null";
		//return result+checkPtr(ptr);
	}

	public static String checkPtr(Ptr ptr,Ptr ptsl){
		if (ptsl!=null){
			String result="";
			PtrSegment ptrSegment = ptr.getSegments()[0];
			PtrSegment ptslSegment = ptsl.getSegment(ptrSegment.getName());
			if (ptslSegment==null) throw(new IllegalArgumentException("There is no "+ptrSegment.getName()+" segment in the ptsl"));

			if (!ptrSegment.getStartDate().equals(ptslSegment.getStartDate())){
				result=result+"Start date of the PTR and the PTSL segments are not the same:\n";
				result=result+"PTR segment starts at:"+	ptrSegment.getStartDate().toDate()+"\n";
				result=result+"PTSL segment starts at:"+	ptslSegment.getStartDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			if (!ptrSegment.getEndDate().equals(ptslSegment.getEndDate())){
				result=result+"End date of the PTR and the PTSL segments are not the same:\n";
				result=result+"PTR segment ends at:"+	ptrSegment.getEndDate().toDate()+"\n";
				result=result+"PTSL segment ends at:"+	ptslSegment.getEndDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			PointingBlock[] ptslBlocks = ptslSegment.getBlocks();
			for (int i=0;i<ptslBlocks.length;i++){
				PointingBlock ptslBlock = ptslBlocks[i];
				if (!ptslBlock.getType().equals(PointingBlock.TYPE_OBS) && !ptslBlock.getType().equals(PointingBlock.TYPE_SLEW)){
					PointingBlock ptrBlock = ptrSegment.getBlockAt(ptslBlock.getStartTime());
					if (ptrBlock==null){
						result=result+"Block not found in PTR:\n";
						result=result+ptslBlock.toXml(1)+"\n";
					}else{
						if (!ptslBlock.equals(ptrBlock)){
							result=result+"Block in PTR different to same block in PTSL:\n";
							result=result+"Block in PTSL:\n";
							result=result+ptslBlock.toXml(0)+"\n";
							result=result+"Block in PTR:\n";
							result=result+ptrBlock.toXml(0)+"\n";
							result=result+"--------------------------------------\n";
						}else{
							PointingBlock ptslBlockBefore = ptslSegment.blockBefore(ptslBlock);
							PointingBlock ptslBlockAfter = ptslSegment.blockAfter(ptslBlock);
							PointingBlock ptrBlockBefore=ptrSegment.blockBefore(ptrBlock);
							PointingBlock ptrBlockAfter=ptrSegment.blockAfter(ptrBlock);
							if (ptslBlockBefore!=null && ptrBlockBefore!=null){
								if (!ptslBlockBefore.getType().equals(ptrBlockBefore.getType())){
									result=result+"Block type before maintenance block in PTR different to same block in PTSL:\n";
									result=result+"Block in PTSL:\n";
									result=result+ptslBlockBefore.toXml(0)+"\n";
									result=result+ptslBlock.toXml(0)+"\n";
									result=result+"Block in PTR:\n";
									result=result+ptrBlockBefore.toXml(0)+"\n";
									result=result+ptrBlock.toXml(0)+"\n";
									result=result+"--------------------------------------\n";
									
								}
							}
							if (ptslBlockAfter!=null && ptrBlockAfter!=null){
								if (!ptslBlockAfter.getType().equals(ptrBlockAfter.getType())){
									result=result+"Block type after maintenance block in PTR different to same block in PTSL:\n";
									result=result+"Block in PTSL:\n";
									result=result+ptslBlock.toXml(0)+"\n";
									result=result+ptslBlockAfter.toXml(0)+"\n";
									result=result+"Block in PTR:\n";
									result=result+ptrBlock.toXml(0)+"\n";									
									result=result+ptrBlockAfter.toXml(0)+"\n";
									result=result+"--------------------------------------\n";
									
								}
								
							}

						}
					}
				}
			}
			if (result.equals("")) return result+checkPtr(ptr);
			else{
				
				return "**************************\nPTR/PTSL check\n**************************\n"+result+checkPtr(ptr);
			}
		}else{
			return checkPtr(ptr);
		}
		//return result+checkPtr(ptr);
	}
	
	public static String checkBlockDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			for (int j=0;j<blocks.length;j++){
				if (blocks[j].getDuration()<300000){
					messages=messages+"PTR:Pointing Block too short: "+PointingBlock.dateToZulu(blocks[j].getStartTime())+" - "+PointingBlock.dateToZulu(blocks[j].getEndTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nBLOCK DURATION\n**************************\n"+messages;
		}
	}
	public static String checkSlewDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] slews = segment.getAllBlocksOfType(PointingBlock.TYPE_SLEW);
			for (int j=0;j<slews.length;j++){
				if (slews[j].getDuration()<300000){
					messages=messages+"PTR:Slew too short: "+PointingBlock.dateToZulu(slews[j].getStartTime())+" - "+PointingBlock.dateToZulu(slews[j].getEndTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nSLEW DURATION\n**************************\n"+messages;
		}
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
					messages=messages+"--------------------------------------\n";
				}
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
				if (!blockBefore.isSlew() && !blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive blocks without slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}

			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nGAP CHECKS\n**************************\n"+messages;
		}

		//return messages;
	}
	/*protected static boolean isSlew(PointingBlock block){
		boolean result=false;
		if (block.getType().equals("SLEW") || block.getType().equals("MOCM") || block.getType().equals("MWOL") || block.getType().equals("MSLW")){
			result=true;
		}
		return result;
	}*/
	public static String checkConsecutiveSlews(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				//PointingBlock blockAfter = blocks[j+1];
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive slews detected at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nCONSECUTIVE SLEWS\n**************************\n"+messages;
		}

		//return messages;
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
							messages=messages+"Block:\n";
							messages=messages+blocks[j].toXml(0)+"\n";
							messages=messages+"Offset ends at:"+PointingBlock.dateToZulu(offsetendtime)+"\n";
							messages=messages+"--------------------------------------\n";
							
						}
						if (blocks[j].getStartTime().after(new Date(offsetstarttime.getTime()-90000))){
							messages=messages+"PTR:The offset period starts less than 90 seconds after the block starts for block at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
							messages=messages+"--------------------------------------\n";
						}
						
					}


				}

			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nOFFSET DURATION\n**************************\n"+messages;
		}

		//return messages;
	}
	
	public static String checkInternalSlewsDuration(Ptr ptr){
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
						
						//Date offsetstarttime
						if (offset.isScan()){
							float lineSlew = ((OffsetScan) offset).getLineSlewTime(Units.SECONDS);
							//offsetstarttime =((OffsetScan) offset).getStartDate();
							if (lineSlew<30){
								messages=messages+"PTR:The line slew is less than 30 seconds at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
								messages=messages+"Block:\n";
								messages=messages+blocks[j].toXml(0)+"\n";
								messages=messages+"--------------------------------------\n";
							}

						}
						if (offset.isRaster()){
							float lineSlew = ((OffsetRaster) offset).getLineSlewTime(Units.SECONDS);
							float pointSlew = ((OffsetRaster) offset).getPointSlewTime(Units.SECONDS);
							if (lineSlew<30){
								messages=messages+"PTR:The line slew is less than 30 seconds at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
								messages=messages+"Block:\n";
								messages=messages+blocks[j].toXml(0)+"\n";
								messages=messages+"--------------------------------------\n";
							}
							if (pointSlew<30){
								messages=messages+"PTR:The point slew is less than 30 seconds at "+PointingBlock.dateToZulu(blocks[j].getStartTime())+"\n";
								messages=messages+"Block:\n";
								messages=messages+blocks[j].toXml(0)+"\n";
								messages=messages+"--------------------------------------\n";
							}


						}
						
					}


				}

			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nINTERNAL SLEWS DURATION\n**************************\n"+messages;
		}
		
	}


}
