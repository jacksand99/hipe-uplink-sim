package vega.uplink.pointing;

import java.util.Date;
import java.util.NoSuchElementException;

import org.apache.commons.lang3.ArrayUtils;

import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;

public class PtrChecker {
	public static String checkSlice(PointingBlocksSlice slice){
		/*Ptr tempPtr = new Ptr();
		PtrSegment tempSegment=new PtrSegment("temp");
		tempSegment.setBlocks(slice.getBlocks());
		tempPtr.addSegment(tempSegment);
		return checkPtr(tempPtr);*/
		return checkBlocks(slice.getBlocks())+checkBlockDuration(slice.getBlocks())+checkOffsetDuration(slice.getBlocks())+checkInternalSlewsDuration(slice.getBlocks());
	}
	public static String checkPtrHTML(Ptr ptr){
		String result="";
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Blocks</th><th>Error</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;
		result=result+checkBlocksHTML(ptr)+checkBlockDurationHTML(ptr)+checkGapsHTML(ptr)+checkOffsetDurationHTML(ptr)+checkInternalSlewsDurationHTML(ptr);
		result=result+"</table>";
		return result;
	}

	public static String checkPtr(Ptr ptr){
		return checkBlocks(ptr)+checkBlockDuration(ptr)+checkGaps(ptr)+checkOffsetDuration(ptr)+checkInternalSlewsDuration(ptr);

	}
	
	public static String comparePtrsHTML(Ptr fptr,Ptr sptr){

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
		String PTR_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>"+ptr2.getName()+" Block</th><th>"+ptr1.getName()+" Blocks</th>\n"
				+ "</tr>\n";

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
				result=result+ptr1.getName()+" segment starts at:"+	ptr1Segment.getStartDate().toDate()+"\n";
				result=result+ptr2.getName()+" segment starts at:"+	ptr2Segment.getStartDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			if (!ptr1Segment.getEndDate().equals(ptr2Segment.getEndDate())){
				result=result+"End date of the "+ptr1.getName()+" and the "+ptr2.getName()+" segments are not the same:\n";
				result=result+ptr1.getName()+" segment ends at:"+	ptr1Segment.getEndDate().toDate()+"\n";
				result=result+ptr2.getName()+" segment ends at:"+	ptr2Segment.getEndDate().toDate()+"\n";
				result=result+"--------------------------------------\n";
				
			}
			result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;
			PointingBlock[] ptr2Blocks = ptr2Segment.getBlocks();
			for (int i=0;i<ptr2Blocks.length;i++){
				PointingBlock ptr2Block = ptr2Blocks[i];
				if (!(ptr2Block.getType().equals(PointingBlock.TYPE_SLEW) || ptr2Block.getType().equals(PointingBlock.TYPE_SYNC))){
					PointingBlock ptrBlock = ptr1Segment.getBlockAt(ptr2Block.getStartTime());
					if (ptrBlock==null){
						result=result+"<tr>";
						//result=result+"Block not found in "+ptr1.getName()+":\n";
						result=result+"<td><pre>"+HtmlEditorKit.escapeHTML(ptr2Block.toXml(0))+"</pre></td>";
						result=result+"<td>"+"Block not found in "+ptr1.getName()+"</td>";

						result=result+"</tr>";
						//result=result+ptr2Block.toXml(1)+"\n";
					}else{
						if (!ptr2Block.equals(ptrBlock)){
							result=result+"<tr>";

							//result=result+"Block in "+ptr1.getName()+" different to same block in "+ptr2.getName()+":\n";
							result=result+"<td><pre>"+HtmlEditorKit.escapeHTML(ptr2Block.toXml(0))+"</pre></td>";

							//result=result+"Block in "+ptr2.getName()+":\n";
							//result=result+ptr2Block.toXml(0)+"\n";
							//result=result+"Blocks in "+ptr1.getName()+":\n";
							//result=result+"<td>"+HtmlEditorKit.escapeHTML(ptr1Block.toXml(0))+"</td>";
							result=result+"<td><pre>";
							PointingBlock[] blocksToReport = ptr1Segment.getBlocksAt(ptr2Block.getStartTime(),ptr2Block.getEndTime()).getBlocks();
							for (int j=0;j<blocksToReport.length;j++){
								result=result+HtmlEditorKit.escapeHTML(blocksToReport[j].toXml(0))+"\n";
								
							}
							result=result+"</pre></td></tr>";
							
							//result=result+ptrBlock.toXml(0)+"\n";
							//result=result+"--------------------------------------\n";
						}
					}
				}
			}
			result=result+"</table>";
			return result;
		}else return "PTR2 is null";
		//return result+checkPtr(ptr);
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
				if (!(ptr2Block.getType().equals(PointingBlock.TYPE_SLEW) || ptr2Block.getType().equals(PointingBlock.TYPE_SYNC))){
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
							PointingBlock[] blocksToReport = ptr1Segment.getBlocksAt(ptr2Block.getStartTime(),ptr2Block.getEndTime()).getBlocks();
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
	public static String checkPtrHTML(Ptr ptr,Ptr ptsl){
		if (ptsl!=null){
			String PTR_TABLE_HEADER=""
					+ "<tr>\n"
					+ "	<th>"+ptsl.getName()+" Block</th><th>"+ptr.getName()+" Blocks</th>\n"
					+ "</tr>\n";
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
			result=result+"<table class=\"gridtable\">"+PTR_TABLE_HEADER;
			PointingBlock[] ptslBlocks = ptslSegment.getBlocks();
			for (int i=0;i<ptslBlocks.length;i++){
				PointingBlock ptslBlock = ptslBlocks[i];
				if (!ptslBlock.getType().equals(PointingBlock.TYPE_OBS) && !(ptslBlock.getType().equals(PointingBlock.TYPE_SLEW) || ptslBlock.getType().equals(PointingBlock.TYPE_SYNC))){
					PointingBlock ptrBlock = ptrSegment.getBlockAt(ptslBlock.getStartTime());
					if (ptrBlock==null){
						result=result+"<tr>";
						//result=result+"Block not found in "+ptr1.getName()+":\n";
						result=result+"<td><pre>"+HtmlEditorKit.escapeHTML(ptslBlock.toXml(0))+"</pre></td>";
						result=result+"<td>"+"Block not found in "+ptr.getName()+"</td>";

						result=result+"</tr>";

						//result=result+"Block not found in PTR:\n";
						//result=result+ptslBlock.toXml(1)+"\n";
					}else{
						if (!ptslBlock.equals(ptrBlock)){
							result=result+"<tr>";

							//result=result+"Block in "+ptr1.getName()+" different to same block in "+ptr2.getName()+":\n";
							result=result+"<td><pre>"+HtmlEditorKit.escapeHTML(ptslBlock.toXml(0))+"</pre></td>";

							//result=result+"Block in "+ptr2.getName()+":\n";
							//result=result+ptr2Block.toXml(0)+"\n";
							//result=result+"Blocks in "+ptr1.getName()+":\n";
							//result=result+"<td>"+HtmlEditorKit.escapeHTML(ptr1Block.toXml(0))+"</td>";
							result=result+"<td><pre>";
							PointingBlock[] blocksToReport = ptrSegment.getBlocksAt(ptslBlock.getStartTime(),ptslBlock.getEndTime()).getBlocks();
							for (int j=0;j<blocksToReport.length;j++){
								result=result+HtmlEditorKit.escapeHTML(blocksToReport[j].toXml(0))+"\n";
								
							}
							result=result+"</pre></td></tr>";

							//result=result+"Block in PTR different to same block in PTSL:\n";
							//result=result+"Block in PTSL:\n";
							//result=result+ptslBlock.toXml(0)+"\n";
							//result=result+"Block in PTR:\n";
							//result=result+ptrBlock.toXml(0)+"\n";
							//result=result+"--------------------------------------\n";
						}else{
							PointingBlock ptslBlockBefore = ptslSegment.blockBefore(ptslBlock);
							PointingBlock ptslBlockAfter = ptslSegment.blockAfter(ptslBlock);
							PointingBlock ptrBlockBefore=ptrSegment.blockBefore(ptrBlock);
							PointingBlock ptrBlockAfter=ptrSegment.blockAfter(ptrBlock);
							if (ptslBlockBefore!=null && ptrBlockBefore!=null){
								if (!ptslBlockBefore.getType().equals(ptrBlockBefore.getType())){
									result=result+"<tr>";
									result=result+"<td>Block type before maintenance block in PTR different to same block in PTSL<br><pre>"+HtmlEditorKit.escapeHTML(ptslBlockBefore.toXml(0)+ptslBlock.toXml(0))+"</pre></td>";
									result=result+"<td>Block type before maintenance block in PTR different to same block in PTSL<br><pre>"+HtmlEditorKit.escapeHTML(ptrBlockBefore.toXml(0)+ptrBlock.toXml(0))+"</pre></td>";
									result=result+"</tr>";

									
								}
							}
							if (ptslBlockAfter!=null && ptrBlockAfter!=null){
								if (!ptslBlockAfter.getType().equals(ptrBlockAfter.getType())){
									result=result+"<tr>";
									result=result+"<td>Block type after maintenance block in PTR different to same block in PTSL<br><pre>"+HtmlEditorKit.escapeHTML(ptslBlock.toXml(0)+ptslBlockAfter.toXml(0))+"</pre></td>";
									result=result+"<td>Block type after maintenance block in PTR different to same block in PTSL<br><pre>"+HtmlEditorKit.escapeHTML(ptrBlock.toXml(0)+ptrBlockAfter.toXml(0))+"</pre></td>";
									result=result+"</tr>";

									
								}
								
							}

						}
					}
				}
			}
			result=result+"</table>";
			return result+checkPtrHTML(ptr);
		}else{
			return checkPtrHTML(ptr);
		}
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
				if (!ptslBlock.getType().equals(PointingBlock.TYPE_OBS) && !(ptslBlock.getType().equals(PointingBlock.TYPE_SLEW) || ptslBlock.getType().equals(PointingBlock.TYPE_SYNC))){
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
	public static String checkBlockDurationHTMLRow(PointingBlock[] blocks){
		String messages="";
		for (int j=0;j<blocks.length;j++){
			if (blocks[j].getDuration()<300000){
				if (!blocks[j].isSlew()){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>Pointing Block too short</td></tr>";
				}else{
					if (j+1<blocks.length)
						messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0)+blocks[j+1].toXml(0))+"</pre></td><td>Slew too short</td></tr>";
					else 
						messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0))+"</pre></td><td>Slew too short</td></tr>";
					
				}
			}
		}
		return messages;

	}

	public static String checkBlockDuration(PointingBlock[] blocks){
		String messages="";
		for (int j=0;j<blocks.length;j++){
			if (blocks[j].getDuration()<300000){
				messages=messages+"PTR:Pointing Block too short: "+dateToString(blocks[j].getStartTime())+" - "+dateToString(blocks[j].getEndTime())+"\n";
				messages=messages+"--------------------------------------\n";
			}
		}
		return messages;

	}
	public static String checkBlockDurationHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			messages=messages+checkBlockDurationHTMLRow(blocks);
		}
		return messages;
	}

	public static String checkBlockDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			messages=messages+checkBlockDuration(blocks);
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nBLOCK DURATION\n**************************\n"+messages;
		}
	}
	public static String dateToString(Date date) {
	    if (herschel.share.util.Configuration.getProperty(Properties.POINTING_DATE_FORMAT).equals("DOY")) {
	        return DateUtil.dateToDOYNoMilli(date);
	    }
	    else return DateUtil.dateToZulu(date);
	}

	public static String checkSlewDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] slews =ArrayUtils.addAll(segment.getAllBlocksOfType(PointingBlock.TYPE_SLEW).getBlocks(),segment.getAllBlocksOfType(PointingBlock.TYPE_SYNC).getBlocks());;
			
			for (int j=0;j<slews.length;j++){
				if (slews[j].getDuration()<300000){
					messages=messages+"PTR:Slew too short: "+dateToString(slews[j].getStartTime())+" - "+dateToString(slews[j].getEndTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
			}
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nSLEW DURATION\n**************************\n"+messages;
		}
	}
	public static String checkGapsHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				//PointingBlock blockAfter = blocks[j+1];
				if (!blockBefore.getEndTime().equals(blocks[j].getStartTime())){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0))+"</pre></td><td>PTR:Gap detected between "+dateToString(blockBefore.getEndTime())+" and "+dateToString(blocks[j].getStartTime())+"</td></tr>";
				}
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0))+"</pre></td><td>Two consecutive slews detected at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

				}
				if (!blockBefore.isSlew() && !blocks[j].isSlew()){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0))+"</pre></td><td>Two consecutive blocks without slews detected at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

				}
				if (blockBefore.getEndTime().after(blocks[j].getStartTime())){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1].toXml(0)+blocks[j].toXml(0))+"</pre></td><td>Two overlaping blocks detected at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

					
				}
			}
		}
		return messages;

		//return messages;
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
					messages=messages+"PTR:Gap detected between "+dateToString(blockBefore.getEndTime())+" and "+dateToString(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive slews detected at "+dateToString(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
				if (!blockBefore.isSlew() && !blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive blocks without slews detected at "+dateToString(blocks[j].getStartTime())+"\n";
					messages=messages+"--------------------------------------\n";
				}
				if (blockBefore.getEndTime().after(blocks[j].getStartTime())){
					messages=messages+"PTR:Two overlaping blocks detected at "+dateToString(blocks[j].getStartTime())+"\n";
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
	public static String checkBlocksHTMLRow(PointingBlock[] blocks){
		String messages="";
		for (int j=1;j<blocks.length;j++){
			try{
				blocks[j].validate();
			}catch (Exception e){
				messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>Incorrect Block detected:\n"+e.getMessage()+"</td></tr>";

				
			}
		}
		return messages;
		
	}

	public static String checkBlocks(PointingBlock[] blocks){
		String messages="";
		for (int j=1;j<blocks.length;j++){
			try{
				blocks[j].validate();
			}catch (Exception e){
				messages=messages+"PTR:Incorrect Block detected at "+dateToString(blocks[j].getStartTime())+"\n";
				messages=messages+e.getMessage();
				messages=messages+"--------------------------------------\n";
				
			}
		}
		return messages;
		
	}
	public static String checkBlocksHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			messages=messages+checkBlocksHTMLRow(blocks);
		}
		return messages;

		//return messages;
	}

	public static String checkBlocks(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			messages=messages+checkBlocks(blocks);
			/*for (int j=1;j<blocks.length;j++){
				try{
					blocks[j].validate();
				}catch (Exception e){
					messages=messages+"PTR:Incorrect Block detected at "+dateToString(blocks[j].getStartTime())+"\n";
					messages=messages+e.getMessage();
					messages=messages+"--------------------------------------\n";
					
				}
			}*/
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nPOINTING BLOCK CHECKS\n**************************\n"+messages;
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
	public static String checkConsecutiveSlewsHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				//PointingBlock blockAfter = blocks[j+1];
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j-1]+blocks[j].toXml(0))+"</pre></td><td>Two consecutive slews detected at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

				}
			}
		}
		return messages;


		//return messages;
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
				if (blockBefore.isSlew() && blocks[j].isSlew()){
					messages=messages+"PTR:Two consecutive slews detected at "+dateToString(blocks[j].getStartTime())+"\n";
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
	public static String checkOffsetDurationHTMLRow(PointingBlock[] blocks){
		String messages="";
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
						long borderSlewTime = new Float(((OffsetScan) offset).getBorderSlewTime(Units.SECONDS)).longValue()*1000;
						offsetstarttime =new Date(((OffsetScan) offset).getStartDate().getTime()-borderSlewTime);

						//offsetstarttime =((OffsetScan) offset).getStartDate();

					}
					if (offset.isRaster()){
						offsetendtime = ((OffsetRaster) offset).getEndDate();
						offsetstarttime =((OffsetRaster) offset).getStartDate();
					}
					if (offset.isCustom()){
						offsetendtime = ((OffsetCustom) offset).getEndDate();
						//float deltaTime = ((OffsetCustom) offset).getDeltaTimes(Units.SECONDS)[0]))*1000;
						//offsetstarttime =new Date(new Float(((OffsetCustom) offset).getStartDate().getTime()-deltaTime).longValue());
						//offsetstarttime =((OffsetCustom) offset).getStartDate();
						long deltaTime = new Float(((OffsetCustom) offset).getDeltaTimes(Units.SECONDS)[0]).longValue()*1000;
						offsetstarttime =new Date(((OffsetCustom) offset).getStartDate().getTime()+deltaTime);

					}
					if (blocks[j].getEndTime().before(new Date(offsetendtime.getTime()+90000))){
						messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>PTR:The offset period ends less than 90 seconds before the block ends for block at "+dateToString(blocks[j].getStartTime())+"<br>"+"Offset ends at:"+dateToString(offsetendtime)+"</td></tr>";

						
					}
					if (blocks[j].getStartTime().after(new Date(offsetstarttime.getTime()-90000))){
						messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>PTR:The offset period starts less than 90 seconds after the block starts for block at "+dateToString(blocks[j].getStartTime())+"<br>"+"Offset starts at:"+dateToString(offsetstarttime)+"</td></tr>";

					}
					
				}


			}
		}

		return messages;
	}

	public static String checkOffsetDuration(PointingBlock[] blocks){
		String messages="";
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
						long borderSlewTime = new Float(((OffsetScan) offset).getBorderSlewTime(Units.SECONDS)).longValue()*1000;
						offsetstarttime =new Date(((OffsetScan) offset).getStartDate().getTime()-borderSlewTime);

						//offsetstarttime =((OffsetScan) offset).getStartDate();

					}
					if (offset.isRaster()){
						offsetendtime = ((OffsetRaster) offset).getEndDate();
						offsetstarttime =((OffsetRaster) offset).getStartDate();
					}
					if (offset.isCustom()){
						offsetendtime = ((OffsetCustom) offset).getEndDate();
						//offsetstarttime =((OffsetCustom) offset).getStartDate();
						long deltaTime = new Float(((OffsetCustom) offset).getDeltaTimes(Units.SECONDS)[0]).longValue()*1000;
						offsetstarttime =new Date(((OffsetCustom) offset).getStartDate().getTime()+deltaTime);

					}
					if (blocks[j].getEndTime().before(new Date(offsetendtime.getTime()+90000))){
						messages=messages+"PTR:The offset period ends less than 90 seconds before the block ends for block at "+dateToString(blocks[j].getStartTime())+"\n";
						messages=messages+"Block:\n";
						messages=messages+blocks[j].toXml(0)+"\n";
						messages=messages+"Offset ends at:"+dateToString(offsetendtime)+"\n";
						messages=messages+"--------------------------------------\n";
						
					}
					if (blocks[j].getStartTime().after(new Date(offsetstarttime.getTime()-90000))){
						messages=messages+"PTR:The offset period starts less than 90 seconds after the block starts for block at "+dateToString(blocks[j].getStartTime())+"\n";
						messages=messages+"--------------------------------------\n";
					}
					
				}


			}
		}

		return messages;
	}

	public static String checkOffsetDurationHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getAllBlocksOfType("OBS").getBlocks();
			messages=messages+checkOffsetDurationHTMLRow(blocks);
		}
		return messages;

		//return messages;
	}
		
	public static String checkOffsetDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getAllBlocksOfType("OBS").getBlocks();
			messages=messages+checkOffsetDuration(blocks);
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nOFFSET DURATION\n**************************\n"+messages;
		}

		//return messages;
	}
	public static String checkInternalSlewsDurationHTMLRow(PointingBlock[] blocks){
		String messages="";
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
							messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>PTR:The line slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

						}

					}
					if (offset.isRaster()){
						float lineSlew = ((OffsetRaster) offset).getLineSlewTime(Units.SECONDS);
						float pointSlew = ((OffsetRaster) offset).getPointSlewTime(Units.SECONDS);
						if (lineSlew<30){
							messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>PTR:The line slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

						}
						if (pointSlew<30){
							messages=messages+"<tr><td><pre>"+HtmlEditorKit.escapeHTML(blocks[j].toXml(0))+"</pre></td><td>PTR:The point slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"</td></tr>";

						}


					}
					
				}


			}

		}

		return messages;
	}

	public static String checkInternalSlewsDuration(PointingBlock[] blocks){
		String messages="";
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
							messages=messages+"PTR:The line slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"\n";
							messages=messages+"Block:\n";
							messages=messages+blocks[j].toXml(0)+"\n";
							messages=messages+"--------------------------------------\n";
						}

					}
					if (offset.isRaster()){
						float lineSlew = ((OffsetRaster) offset).getLineSlewTime(Units.SECONDS);
						float pointSlew = ((OffsetRaster) offset).getPointSlewTime(Units.SECONDS);
						if (lineSlew<30){
							messages=messages+"PTR:The line slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"\n";
							messages=messages+"Block:\n";
							messages=messages+blocks[j].toXml(0)+"\n";
							messages=messages+"--------------------------------------\n";
						}
						if (pointSlew<30){
							messages=messages+"PTR:The point slew is less than 30 seconds at "+dateToString(blocks[j].getStartTime())+"\n";
							messages=messages+"Block:\n";
							messages=messages+blocks[j].toXml(0)+"\n";
							messages=messages+"--------------------------------------\n";
						}


					}
					
				}


			}

		}

		return messages;
	}
	public static String checkInternalSlewsDurationHTML(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getAllBlocksOfType("OBS").getBlocks();
			messages=messages+checkInternalSlewsDurationHTMLRow(blocks);
		}
		return messages;
		
	}

	public static String checkInternalSlewsDuration(Ptr ptr){
		String messages="";
		PtrSegment[] segs = ptr.getSegments();
		for (int i=0;i<segs.length;i++){
			PtrSegment segment = segs[i];
			PointingBlock[] blocks = segment.getAllBlocksOfType("OBS").getBlocks();
			messages=messages+checkInternalSlewsDuration(blocks);
		}
		if (messages.equals("")) return messages;
		else{
			
			return "**************************\nINTERNAL SLEWS DURATION\n**************************\n"+messages;
		}
		
	}


}
