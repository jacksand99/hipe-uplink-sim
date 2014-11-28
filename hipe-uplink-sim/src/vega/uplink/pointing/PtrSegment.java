package vega.uplink.pointing;

import herschel.ia.dataset.Dataset;
//import herschel.ia.dataset.DateParameter;
//import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
//import herschel.ia.dataset.TableDataset;
import herschel.share.fltdyn.time.FineTime;



import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
//import java.text.ParseException;
//import java.util.Collection;
import java.util.Date;
//import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
//import java.util.SortedMap;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/*import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;*/

//import vega.uplink.commanding.GsPass;


/**
 * Contains the pointing request for a certain MTP period
 * @author jarenas
 *
 */
public class PtrSegment extends PointingBlocksSlice{
	//private String name;
	private String[] includes;
	//private TreeMap<Date,PointingBlock> blMap;
	public static String SEGMENT_TAG="segment";
	public static String METADATA_TAG="metadata";
	public static String NAME_TAG="name";
	public static String INCLUDE_TAG="include";
	public static String HREF_TAG="href";
	public static String DATA_TAG="data";
	public static String TIMELINE_TAG="timeline";
	public static String FRAME_TAG="frame";
	
	
	
	private String getBlockName(PointingBlock block){
		String result=block.getType()+" "+PointingBlock.dateToZulu(block.getStartTime());
		return result;
				
	}
	private TreeMap<Date,PointingBlock> getBlMap(){
		TreeMap<Date, PointingBlock> result = new TreeMap<Date,PointingBlock>();
		Iterator<Dataset> it = this.getSets().values().iterator();
		while (it.hasNext()){
			PointingBlock block = (PointingBlock) it.next();
			result.put(block.getStartTime(), block);
		}
		return result;
		
	}
	private TreeMap<Integer,Date> getVstpMap(){
		TreeMap<Integer, Date> result = new TreeMap<Integer,Date>();
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Iterator<PointingBlock> it = blMap.values().iterator();
		while (it.hasNext()){
			PointingBlock block = it.next();
			int vstp = block.getVstpNumberMeta();
			if (vstp>0) result.put(vstp,block.getStartTime());
		}
		return result;
		
	}
	
	
	/**
	 * Create a PTR segment with the given name
	 * @param segmentName Name of the segment
	 */
	public PtrSegment(String segmentName){
		super();
		//blMap=new TreeMap<Date,PointingBlock>();
		setName(segmentName);
		//name=segmentName;
		includes=new String[0];
		this.setTimeLineFrame("SC");
	}
	
	public PtrSegment copy(){
    	String text = this.toXml(0);

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
			Document doc;
	
			doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			//Node node = (Node) doc;
			//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
			PtrSegment tempseg = PtrUtils.getPtrSegmentFromDoc(doc);
			return tempseg;
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw iae;
			
		}
	}
	
	public Product asProduct(){
		return this;

	}
	
	public Date getSegmentStartDate(){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		return blMap.firstKey();
	}
	
	public Date getSegmentEndDate(){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		return blMap.lastEntry().getValue().getEndTime();
	}
	/**
	 * Get all blocks of this segment as an array
	 * @return
	 */
	private PointingBlock[] getBlocksasArry(){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		PointingBlock[] result=new PointingBlock[blMap.size()];
		Iterator<Entry<Date, PointingBlock>> it = blMap.entrySet().iterator();
		int i=0;
		while (it.hasNext()){
			result[i]=it.next().getValue();
			i++;
		}
		return result;
		
	}
	protected void hardRemoveBlock(PointingBlock block){
		remove(getBlockName(block));
		//blMap.remove(block.getStartTime());
	}
	
	protected void hardInsertBlock(PointingBlock block){
		set(getBlockName(block),block);
		//blMap.put(block.getStartTime(), block);
		//System.out.println("inserted "+block.toXml(0));
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		if (!blMap.lastEntry().getValue().getType().equals(PointingBlock.TYPE_SLEW)){
			this.setValidityDates(blMap.firstKey(), blMap.lastEntry().getValue().getEndTime());
		}
	}
	protected void hardInsertBlocks(PointingBlock[] blocks){
		//TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		//PointingBlock block;
		PointingBlock lastBlock=null;
		for (int i=0;i<blocks.length;i++){
			PointingBlock block=blocks[i];
			if (block.getType().equals("SLEW")) ((PointingBlockSlew) block).setBlockBefore(lastBlock);
			if (lastBlock!=null && lastBlock.getType().equals("SLEW")) ((PointingBlockSlew) lastBlock).setBlockAfter(block);
			set(getBlockName(block),block);
			lastBlock=block;

		}
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		if (!blMap.lastEntry().getValue().getType().equals(PointingBlock.TYPE_SLEW)){
			this.setValidityDates(blMap.firstKey(), blMap.lastEntry().getValue().getEndTime());
		}

		//set(getBlockName(block),block);
		//blMap.put(block.getStartTime(), block);
		//System.out.println("inserted "+block.toXml(0));
		/*TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		if (!blMap.lastEntry().getValue().getType().equals(PointingBlock.TYPE_SLEW)){
			this.setValidityDates(blMap.firstKey(), blMap.lastEntry().getValue().getEndTime());
		}*/
	}
	
	/**
	 * Remove a block from the segment. It adjust as well the slews if they are affected by the removal.
	 * @param block Block to be removed
	 */
	public void removeBlock(PointingBlock block){
		
		Date oldDate=block.getStartTime();
		PointingBlock before = blockBefore(block);
		PointingBlock after = blockAfter(block);
		if (before!=null && after!=null){
			if (before.getType().equals(PointingBlock.TYPE_SLEW) && after.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlockSlew slewBefore=(PointingBlockSlew) before;
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				hardRemoveBlock(slewBefore);
				hardRemoveBlock(slewAfter);
				PointingBlockSlew newSlew = new PointingBlockSlew();
				newSlew.setBlockBefore(slewBefore.getBlockBefore());
				newSlew.setBlockAfter(slewAfter.getBlockAfter());
				hardInsertBlock(newSlew);				
			}
			if (before.getType().equals(PointingBlock.TYPE_SLEW) && !after.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlockSlew slewBefore=(PointingBlockSlew) before;
				slewBefore.setBlockAfter(after);				
			}
			if (!before.getType().equals(PointingBlock.TYPE_SLEW) && after.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				slewAfter.setBlockBefore(before);
			}
		}
		if (before==null && after!=null){
			if (after.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				hardRemoveBlock(slewAfter);
			}
		}
		if (before!=null && after==null){
			if (before.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlockSlew slewBefore=(PointingBlockSlew) before;
				slewBefore.setBlockAfter(null);
			}
		}
		hardRemoveBlock(block);
		
	}
	
	/**
	 * Get the block before the one given (before the start time the block), or null if there is no block before this one
	 * @param block
	 * @return
	 */
	public PointingBlock blockBefore(PointingBlock block){
		if (block==null) return null;

		long time=block.getStartTime().getTime()-1;
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> result = blMap.floorEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
	}
	public PointingBlock blockAfter(PointingBlock block){
		if (block==null) return null;



		long time=block.getStartTime().getTime()+1;
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> result = blMap.ceilingEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
	}
	public void repairConsecutiveBlocks(){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		//Iterator<Entry<Date, PointingBlock>> it = ((TreeMap<Date,PointingBlock>)blMap.clone()).entrySet().iterator();
		Iterator<Entry<Date, PointingBlock>> it = ((TreeMap<Date,PointingBlock>)blMap).entrySet().iterator();
		while (it.hasNext()){
			PointingBlock block = it.next().getValue();
			PointingBlock blockAfter = blockAfter(block);
			if (blockAfter!=null && block.getType().equals(PointingBlock.TYPE_OBS) && !blockAfter.isSlew() && block.getEndTime().equals(blockAfter.getStartTime())){
				//if (block.getEndTime().equals(blockAfter.getStartTime())){
					block.setEndTime(new Date(blockAfter.getStartTime().getTime()-300000));
					PointingMetadata meta = block.getMetadataElement();
					if (meta==null){
						meta=new PointingMetadata();
						
					}
					meta.addComment("End time of the block modified");
					if (block.getMetadataElement()==null){
						block.setMetadata(new PointingMetadata());
					}
					
					block.getMetadataElement().addComment("End time of the block modified");
					PointingBlockSlew newSlew = new PointingBlockSlew();
					newSlew.setBlockBefore(block);
					newSlew.setBlockAfter(blockAfter);
					this.addBlock(newSlew);
				//}
			}
		}
		
		Iterator<Entry<Date, PointingBlock>> it2 = ((TreeMap<Date,PointingBlock>)blMap).entrySet().iterator();
		while (it2.hasNext()){
			PointingBlock block = it2.next().getValue();
			PointingBlock blockBefore = blockBefore(block);
			if (blockBefore!=null && block.getType().equals(PointingBlock.TYPE_OBS) && !blockBefore.isSlew() && block.getStartTime().equals(blockBefore.getEndTime())){
				//if (block.getEndTime().equals(blockAfter.getStartTime())){
					block.setStartTime(new Date(blockBefore.getEndTime().getTime()+300000));
					PointingMetadata meta = block.getMetadataElement();
					if (meta==null){
						meta=new PointingMetadata();
						
					}
					meta.addComment("Start time of the block modified");

					PointingBlockSlew newSlew = new PointingBlockSlew();
					newSlew.setBlockBefore(blockBefore);
					newSlew.setBlockAfter(block);
					this.addBlock(newSlew);
				//}
			}
		}

	}
	
	/**
	 * Check if there is any gap before a MOCM, MWOL or MSLW block (which have) internal slews and extend the observation before to end at the beginning of the maintenance block
	 */
	public void repairMocm(){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		//Iterator<Entry<Date, PointingBlock>> it = ((TreeMap<Date,PointingBlock>)blMap.clone()).entrySet().iterator();
		Iterator<Entry<Date, PointingBlock>> it = ((TreeMap<Date,PointingBlock>)blMap).entrySet().iterator();
		while (it.hasNext()){
			PointingBlock block = it.next().getValue();
			if (block.getType().equals(PointingBlock.TYPE_MOCM) || block.getType().equals(PointingBlock.TYPE_MWOL) || block.getType().equals(PointingBlock.TYPE_MSLW)){
				PointingBlock before = blockBefore(block);
				if (before!=null){
					if (before.getType().equals(PointingBlock.TYPE_SLEW)){
						PointingBlock beforeSlew=blockBefore(before);
						this.removeBlock(before);
						//this.hardRemoveBlock(before);
						if (beforeSlew.getType().equals(PointingBlock.TYPE_OBS)) {
							beforeSlew.setEndTime(block.getStartTime());
						}
						
					}else{
						if (before.getType().equals(PointingBlock.TYPE_OBS)){
							before.setEndTime(block.getStartTime());
						}
					}
					//before.setEndTime(block.getStartTime());
				}

			}

		}
	}
	private void removeDuplicateSlews(){
		PointingBlock[] blocks = getBlocks();
		for (int i=1;i<blocks.length;i++){
			PointingBlock block = blocks[i];
			PointingBlock before = blockBefore(block);
			if (before.getType().equals(PointingBlock.TYPE_SLEW) && block.getType().equals(PointingBlock.TYPE_SLEW)){
				//System.out.println("Duplicatr Slew detected");
				hardRemoveBlock(block);
			}
		}
			
			//PointingBlock after = blockAfter(block);
	}
	
	/**
	 * Check if there is any gap and fill it with slews. It is recommended to run repairMocm() before to avoid that a slew 
	 */
	public void repairSlews(){
		removeDuplicateSlews();
		PointingBlock[] blocks = getBlocks();

		for (int i=0;i<blocks.length;i++){
			PointingBlock block = blocks[i];
			PointingBlock before = blockBefore(block);
			PointingBlock after = blockAfter(block);
			if (before!=null && !before.getType().equals(PointingBlock.TYPE_SLEW) && !before.getEndTime().equals(block.getStartTime())){
				PointingBlockSlew newSlew=new PointingBlockSlew();
				newSlew.setBlockBefore(before);
				newSlew.setBlockAfter(block);
				addBlock(newSlew);
			}
		}

	}
	
	/**
	 * Add a new block to the given segment
	 * @param newBlock Block to be added
	 */
	public void addBlock(PointingBlock newBlock){
		boolean new_is_slew = newBlock.getType().equals(PointingBlock.TYPE_SLEW);
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> lastEntry = blMap.lastEntry();
		PointingBlock before;
		if (new_is_slew &&((PointingBlockSlew) newBlock).getBlockBefore()==null){
			if (lastEntry==null){
				throw (new IllegalArgumentException("Can not introduce a SLEW as first block of a segment"));
				//before=null;
			}
			else before=lastEntry.getValue();
		}
		else before= blockBefore(newBlock);
		PointingBlock after;
		if (new_is_slew &&((PointingBlockSlew) newBlock).getBlockAfter()==null){
			after=null;
		}
		else after= blockAfter(newBlock);
		boolean before_is_slew=(before!=null && before.getType().equals(PointingBlock.TYPE_SLEW));
		boolean after_is_slew=(after!=null && after.getType().equals(PointingBlock.TYPE_SLEW));
		if (lastEntry==null){
			//The block list empty
			if (new_is_slew) return; //Can not insert slew in an empty list;
			else{
				hardInsertBlock(newBlock);
				return; //job done
			}
		}else{
			//The list is not empty
			if (before==null){
				//It is going to be the first in the list
				if (new_is_slew) return; //Can not insert slew at the beggining list;
				else{
					//It is a normal block
					if (after_is_slew){
						//In case a orphan slew was left at the beginning of the list
						((PointingBlockSlew) after).setBlockBefore(newBlock);
					}
					hardInsertBlock(newBlock);
					return; //job done
				}
			}
			else{
				// It is not going to be the first in the list
				if (after==null){
					//It is going to be the last in the list
					if (before_is_slew){
						//The last object is a slew
						if (new_is_slew){
							try{
								throw new IllegalArgumentException("Two consecutives slews are not accepted at "+PointingBlock.dateToZulu(before.getEndTime()));
							}catch (IllegalArgumentException e){
								throw new IllegalArgumentException("Two consecutives slews are not accepted at "+PointingBlock.dateToZulu(before.getStartTime()));							
							}
							//return; //can not insert a slew after a slew
						}
						((PointingBlockSlew) before).setBlockAfter(newBlock);
						hardInsertBlock(newBlock);
						return; //job done
					}else{
						//The last object is not a slew
						if (new_is_slew){
							((PointingBlockSlew) newBlock).setBlockBefore(before);
						}
						hardInsertBlock(newBlock);
						return; //job done
					}
				}else{
					//It is neither at the beggining or the end of the list
					if (before_is_slew){
						if (new_is_slew) return;//can not insert a slew after a slew
						((PointingBlockSlew) before).setBlockAfter(newBlock);
						//Also insert a slew just after
						hardInsertBlock(newBlock);
						return; //job done
					}else{
						//before is not a slew
						if (after_is_slew){
							if (new_is_slew) return; //can not insert a slew before a slew
							//It is a normal block
							((PointingBlockSlew) after).setBlockBefore(newBlock);
							//Also ingest a slew before
							PointingBlockSlew slewBefore=new PointingBlockSlew();
							slewBefore.setBlockAfter(newBlock);
							if (!before_is_slew){
								slewBefore.setBlockBefore(before);
							}else{
								//remove orphan slew and reajust
								hardRemoveBlock(before);
								slewBefore.setBlockBefore(((PointingBlockSlew)before).getBlockBefore());
							}
							hardInsertBlock(newBlock);
							hardInsertBlock(slewBefore);
							return; //job done
						}else{
							//Neither the block before or after are slew
							if (new_is_slew){
								((PointingBlockSlew) newBlock).setBlockBefore(before);
								((PointingBlockSlew) newBlock).setBlockAfter(after);
								hardInsertBlock(newBlock);
								return; //job done
							}
							else{
								//neither the block before, after or the new one are slews
								hardInsertBlock(newBlock);
								return; //job done								
							}
						}
					}
				}
			}
		}
		//Dead code, it should never reach hear.
	}
	
	/**
	 * Get all the blocks in this segment
	 * @return Array with all the blocks in this segment
	 */
	public PointingBlock[] getBlocks(){
		
		return getBlocksasArry();
	}
	
	
	/**
	 * Remove all currents blocks and substitute by the given ones
	 * @param newBlocks New blocks to replace the current ones
	 */
	public void setBlocks(PointingBlock[] newBlocks){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Iterator<Entry<Date, PointingBlock>> it = blMap.entrySet().iterator();
		while (it.hasNext()){
			remove(getBlockName(it.next().getValue()));
		}
		for (int i=0;i<newBlocks.length;i++){
			addBlock(newBlocks[i]);
		}
	}
	
	/**
	 * Set the time frame that will be specified in the xml
	 * @param newFrame The new time frame
	 */
	public void setTimeLineFrame(String newFrame){
		getMeta().set("timeLineFrame", new StringParameter(newFrame));
	}
	
	/**
	 * Get the time frame of this segment
	 * @return THe string representing the time frame of this segment
	 */
	public String getTimeLineFrame(){
		return (String) getMeta().get("timeLineFrame").getValue();
	}
	
	/**
	 * Get the name of this segment
	 * @return name of the segment
	 */
	public String getName(){
		return (String) this.getMeta().get("name").getValue();
		//return name;
	}
	
	/**
	 * Set or replace the name of this segment
	 * @param newName
	 */
	public void setName(String newName){
		this.getMeta().set("name", new StringParameter(newName));
		//name=newName;
	}
	
	/**
	 * Set or replace the includes of this segment (will appear in the XML)
	 * @param newIncludes
	 */
	public void setIncludes(String[] newIncludes){
		includes=newIncludes;
	}
	
	/**
	 * Add a single include to this segment
	 * @param newInclude new include to be added
	 */
	public void addInclude(String newInclude){
		String[] newArray=new String[includes.length+1];
		for (int i=0;i<includes.length;i++){
			newArray[i]=includes[i];
		}
		newArray[includes.length]=newInclude;
		setIncludes(newArray);
	}
	
	/**
	 * Get the XML representation of this segment as per FD ICD
	 * @param indent the Number of tabs to be added before each line
	 * @return The XML representation of this segment
	 */
	public String toXml(int indent){
		int obsBlockConter=0;
		boolean inObsSegment=false;
		String result=new String();
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		
		result=result+iString+"<"+SEGMENT_TAG+" "+NAME_TAG+"='"+getName()+"'>\n";
		result=result+iString+"\t"+"<"+METADATA_TAG+">\n";
		for (int i=0;i<includes.length;i++){
			result=result+iString+"\t\t"+"<"+INCLUDE_TAG+" "+HREF_TAG+"='"+includes[i]+"'/>\n";
		}
		result=result+iString+"\t"+"</"+METADATA_TAG+">\n";
		result=result+iString+"\t"+"<"+DATA_TAG+">\n";
		result=result+iString+"\t\t"+"<"+TIMELINE_TAG+" "+FRAME_TAG+"='"+getTimeLineFrame()+"'>\n";
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !inObsSegment){
				obsBlockConter++;
				result=result+iString+"\t\t\t<!--OBS SLICE #"+String.format("%04d", obsBlockConter)+"-->\n";
				inObsSegment=true;
			}
			if (!blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !blocks[i].getType().equals(PointingBlock.TYPE_SLEW)) inObsSegment=false;
			int blocknumber=i+1;
			result=result+iString+"\t\t\t<!-- BLOCK #"+blocknumber+"-->\n";
			result=result+blocks[i].toXml(indent+3);
		}
		result=result+iString+"\t\t"+"</"+TIMELINE_TAG+">\n";

		result=result+iString+"\t"+"</"+DATA_TAG+">\n";
		result=result+iString+"</"+SEGMENT_TAG+">\n";

		
		
		return result;
	}
	
	
	/**
	 * Get the block with a start time equal to the given one or the greates value before the given date
	 * @param time The desired time
	 * @return The block with a start time less or equal to the given time or null if there is no such block
	 */
	/*public PointingBlock getBlockAt(java.util.Date time){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> floorEntry = blMap.floorEntry(time);
		if (floorEntry==null) return null;
		return floorEntry.getValue();
	}*/
	public PointingBlock getBlockAt(java.util.Date time){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> floorEntry = blMap.floorEntry(time);
		if (floorEntry==null) return null;
		PointingBlock result = floorEntry.getValue();
		if (result.getEndTime().before(time)){
			PointingBlock after = blockAfter(result);
			if (after!=null && after.isSlew()) return after;
			else return null;
		}
		return result;
	}

	public PointingBlocksSlice getBlocksAt(java.util.Date startTime,java.util.Date endTime){
		PointingBlocksSlice result=new PointingBlocksSlice();
		startTime=new Date(startTime.getTime()+1);
		endTime=new Date(endTime.getTime()-1);
		//TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Iterator<PointingBlock> it = this.getBlMap().values().iterator();
		while(it.hasNext()){
			PointingBlock block=it.next();
			boolean con1=false;
			boolean con2=false;
			if (block.getStartTime().after(endTime)) con1=true;
			if (block.getEndTime().before(startTime)) con2=true;
			if (!con1 && !con2) result.addBlock(block);
		}
		return result;
	}
		/*if (blMap.size()==0) return new PointingBlocksSlice();
		SortedMap<Date, PointingBlock> sm = new TreeMap<Date, PointingBlock>(blMap.subMap(startTime, endTime));
		PointingBlock startObs = getBlockAt(startTime);
		PointingBlock endObs = getBlockAt(endTime);
		if (startObs!=null) sm.put(startObs.getStartTime(), startObs);
		if (endObs!=null) sm.put(endObs.getStartTime(), endObs);
		Collection<PointingBlock> val = sm.values();
		PointingBlock[] result=new PointingBlock[val.size()];
		result=val.toArray(result);
		PointingBlocksSlice pbs = new PointingBlocksSlice();
		pbs.setBlocks(result);
		return pbs;
	//return getBlockAt(startTime);
	}*/
	

	
	/*public PointingBlock[] getBlocksAt(java.util.Date startTime,java.util.Date endTime){
		endTime=new Date(endTime.getTime()-1);
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		SortedMap<Date, PointingBlock> sm = new TreeMap<Date, PointingBlock>(blMap.subMap(startTime, endTime));
		PointingBlock startObs = getBlockAt(startTime);
		PointingBlock endObs = getBlockAt(endTime);
		sm.put(startObs.getStartTime(), startObs);
		sm.put(endObs.getStartTime(), endObs);
		Collection<PointingBlock> val = sm.values();
		PointingBlock[] result=new PointingBlock[val.size()];
		result=val.toArray(result);
		return result;
		//return getBlockAt(startTime);
	}*/
	
	public void removeBlocks(PointingBlock[] blocks){
		for (int i=0;i<blocks.length;i++){
			removeBlock(blocks[i]);
		}
	}
	
	/**
	 * Get all blocks of a given type
	 * @param blockType type of blocks to search for
	 * @return an array of blocks of the given type
	 */
	public PointingBlocksSlice getAllBlocksOfType(String blockType){
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(blockType)) result.add(blocks[i]);
		}
		
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		PointingBlocksSlice pbs = new PointingBlocksSlice();
		pbs.setBlocks(resultArray);
		pbs.setName(blockType);
		return pbs;
	}
	public PointingBlock[] getAllSlews(){
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals("SLEW")) result.add(blocks[i]);
		}
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		return resultArray;
	}
	public PointingBlocksSlice getAllBlocksOfVstp(int vstp){
		TreeMap<Integer, Date> vstpMap = getVstpMap();
		Date startDate = vstpMap.get(vstp);
		if (startDate==null) return null;
		Date endDate = vstpMap.get(vstp+1);
		if (endDate==null){
			endDate=this.getSegmentEndDate();
		}
		PointingBlocksSlice rs = this.getBlocksAt(startDate, endDate);
		rs.setName("VSTP_"+String.format("%04d", vstp));
		return rs;
	}
	public int[] getVstpNumbers(){
		TreeMap<Integer, Date> vstpMap = getVstpMap();
		Set<Integer> numbers = vstpMap.keySet();
		Integer[] result=new Integer[numbers.size()];
		result=numbers.toArray(result);
		int[] finalResult=new int[result.length];
		for (int i=0;i<result.length;i++){
			finalResult[i]=result[i];
		}
		return finalResult;
	}
	/*public PointingBlock[] getAllBlocksOfType(String blockType){
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(blockType)) result.add(blocks[i]);
		}
		
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		return resultArray;
	}*/
	
	/*public PointingBlock getBlockAt(String startTime,String endTime) throws ParseException{
		return getBlockAt(PointingBlock.zuluToDate(startTime));
	}*/
	
	/**
	 * Set the validity boundaries of this segment 
	 * @param startDate
	 * @param endDate
	 */
	private void setValidityDates(Date startDate,Date endDate){
		setStartDate(new FineTime(startDate));
		setEndDate(new FineTime(endDate));
	}
	
	public PointingBlocksSlice getSlice(int number){
		PointingBlocksSlice result=new PointingBlocksSlice();
		int obsBlockConter=0;
		boolean inObsSegment=false;
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !inObsSegment){
				obsBlockConter++;
				
				//result=result+iString+"\t\t\t<!--OBS SLICE #"+String.format("%04d", obsBlockConter)+"-->\n";
				inObsSegment=true;
			}
			if (!blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !blocks[i].getType().equals(PointingBlock.TYPE_SLEW)) inObsSegment=false;
			if (inObsSegment && obsBlockConter==number && blocks[i].getType().equals(PointingBlock.TYPE_OBS)) result.addBlock(blocks[i]);
		}
		blocks=result.getBlocks();
		PointingBlock lastBlock = blocks[result.size()-1];
		if (lastBlock.getType().equals(PointingBlock.TYPE_SLEW)) result.removeBlock(lastBlock);
		result.setName("OBS_SLICE_"+String.format("%04d", number));
		return result;
	}
	
	public void setSlice(PointingBlocksSlice slice){
		PointingBlock[] obs = slice.getBlocks();
		for (int i=0;i<obs.length;i++){
			PointingBlock[] blocksToRemove = this.getBlocksAt(obs[i].getStartTime(), obs[i].getEndTime()).getBlocks();
			this.removeBlocks(blocksToRemove);
			this.addBlock(obs[i]);
			PtrUtils.repairGaps(this);

		}
	}
	
	
	public PointingBlocksSlice getAllBlocksOfInstrument(String instrument){
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (instrument.equals(blocks[i].getInstrument())) result.add(blocks[i]);
		}
		
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		PointingBlocksSlice resultSlice = new PointingBlocksSlice();
		resultSlice.setBlocks(resultArray);
		resultSlice.setName(instrument);
		return resultSlice;
		
	}
	
	 
	
	
	
}
