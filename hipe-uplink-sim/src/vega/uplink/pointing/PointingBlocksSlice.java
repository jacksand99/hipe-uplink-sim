package vega.uplink.pointing;

import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class PointingBlocksSlice extends Product implements PointingBlockSetInterface{
	/*private String name;
	private String[] includes;
	//private TreeMap<Date,PointingBlock> blMap;
	public static String SEGMENT_TAG="segment";
	public static String METADATA_TAG="metadata";
	public static String NAME_TAG="name";
	public static String INCLUDE_TAG="include";
	public static String HREF_TAG="href";
	public static String DATA_TAG="data";
	public static String TIMELINE_TAG="timeline";
	public static String FRAME_TAG="frame";*/
	public void regenerate(PointingBlockSetInterface slice){
		Set<String> ks = keySet();
		String[] obs=new String[ks.size()] ;
		obs=ks.toArray(obs);
		for (int i=0;i<obs.length;i++){
			remove(obs[i]);
		}
		this.setBlocks(slice.getBlocks());
	}
	
	public PointingBlocksSlice copy(){
		PointingBlocksSlice result = new PointingBlocksSlice();
		/*Set<String> ks = keySet();
		String[] obs=new String[ks.size()] ;
		obs=ks.toArray(obs);
		for (int i=0;i<obs.length;i++){
			remove(obs[i]);
		}*/
		PointingBlock[] blocks = this.getBlocks();
		for (int i=0;i<blocks.length;i++){
			blocks[i]=blocks[i].copy();
		}
		result.setBlocks(blocks);
		return result;
		/*try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String tempText="<slice>\n"+this.toXml(0)+"</slice>\n";
			InputStream stream = new ByteArrayInputStream(tempText.getBytes(StandardCharsets.UTF_8));
			Document doc;
	
			doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			//Node node = (Node) doc;
			//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
			PointingBlocksSlice tempSlice = PtrUtils.readBlocksfromDoc(doc);
			return tempSlice;
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not replicate slice:"+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}*/

	}
	
	private String getBlockName(PointingBlock block){
		String result=block.getType()+" "+PointingBlock.dateToZulu(block.getStartTime());
		return result;
				
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

	private TreeMap<Date,PointingBlock> getBlMap(){
		TreeMap<Date, PointingBlock> result = new TreeMap<Date,PointingBlock>();
		Iterator<Dataset> it = this.getSets().values().iterator();
		while (it.hasNext()){
			PointingBlock block = (PointingBlock) it.next();
			result.put(block.getStartTime(), block);
		}
		return result;
		
	}
	
	
	/**
	 * Create a PTR segment with the given name
	 * @param segmentName Name of the segment
	 */
	public PointingBlocksSlice(){
		super();
		setName(""+new Date().getTime());
		//blMap=new TreeMap<Date,PointingBlock>();
	}
	
	public Product asProduct(){
		return this;

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
	}
	
	/**
	 * Remove a block from the segment. It adjust as well the slews if they are affected by the removal.
	 * @param block Block to be removed
	 */
	public void removeBlock(PointingBlockInterface block){
		if (!InterpreterUtil.isInstance(PointingBlock.class, block)){
			block=PointingBlock.toPointingBlock(block);
		}
		
		Date oldDate=block.getStartTime();
		PointingBlockInterface before = blockBefore(block);
		PointingBlockInterface after = blockAfter(block);
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
		hardRemoveBlock((PointingBlock)block);
		
	}
	
	/**
	 * Get the block before the one given (before the start time the block), or null if there is no block before this one
	 * @param block
	 * @return
	 */
	public PointingBlockInterface blockBefore(PointingBlockInterface block){
		if (block==null) return null;

		long time=block.getStartTime().getTime()-1;
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> result = blMap.floorEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
	}
	public PointingBlockInterface blockAfter(PointingBlockInterface block){
		if (block==null) return null;



		long time=block.getStartTime().getTime()+1;
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> result = blMap.ceilingEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
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
				PointingBlockInterface before = blockBefore(block);
				if (before!=null){
					if (before.getType().equals(PointingBlock.TYPE_SLEW)){
						PointingBlockInterface beforeSlew=blockBefore(before);
						this.removeBlock(before);
						beforeSlew.setEndTime(block.getStartTime());
					}else{
						before.setEndTime(block.getStartTime());
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
			PointingBlockInterface before = blockBefore(block);
			if (before.isSlew() && block.getType().equals(PointingBlock.TYPE_SLEW)){
				//System.out.println("Duplicatr Slew detected");
				hardRemoveBlock(block);
			}
		}

		PointingBlock[] blocks2 = getBlocks();
		for (int i=0;i<blocks2.length-1;i++){
			PointingBlock block = blocks2[i];
			PointingBlockInterface after = blockAfter(block);
			if (after.isSlew() && block.getType().equals(PointingBlock.TYPE_SLEW)){
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
			PointingBlockInterface before = blockBefore(block);
			PointingBlockInterface after = blockAfter(block);
			if (before!=null && !block.isSlew() && !before.isSlew() && !before.getEndTime().equals(block.getStartTime())){
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
	public void addBlock(PointingBlockInterface newBlock){
		if (!InterpreterUtil.isInstance(PointingBlock.class, newBlock)){
			newBlock=PointingBlock.toPointingBlock(newBlock);
		}
		boolean new_is_slew = newBlock.getType().equals(PointingBlock.TYPE_SLEW);
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> lastEntry = blMap.lastEntry();
		PointingBlockInterface before;
		if (new_is_slew &&((PointingBlockSlew) newBlock).getBlockBefore()==null){
			if (lastEntry==null){
				throw (new IllegalArgumentException("Can not introduce a SLEW as first block of a segment"));
				//before=null;
			}
			else before=lastEntry.getValue();
		}
		else before= blockBefore(newBlock);
		PointingBlockInterface after;
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
				hardInsertBlock((PointingBlock)newBlock);
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
					hardInsertBlock((PointingBlock)newBlock);
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
							throw new IllegalArgumentException("Two consecutives slews are not accepted at "+PointingBlock.dateToZulu(before.getEndTime()));
							//return; //can not insert a slew after a slew
						}
						((PointingBlockSlew) before).setBlockAfter(newBlock);
						hardInsertBlock((PointingBlock)newBlock);
						return; //job done
					}else{
						//The last object is not a slew
						if (new_is_slew){
							((PointingBlockSlew) newBlock).setBlockBefore(before);
						}
						hardInsertBlock((PointingBlock)newBlock);
						return; //job done
					}
				}else{
					//It is neither at the beggining or the end of the list
					if (before_is_slew){
						if (new_is_slew) return;//can not insert a slew after a slew
						((PointingBlockSlew) before).setBlockAfter(newBlock);
						//Also insert a slew just after
						hardInsertBlock((PointingBlock)newBlock);
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
								hardRemoveBlock((PointingBlock)before);
								slewBefore.setBlockBefore(((PointingBlockSlew)before).getBlockBefore());
							}
							hardInsertBlock((PointingBlock)newBlock);
							hardInsertBlock(slewBefore);
							return; //job done
						}else{
							//Neither the block before or after are slew
							if (new_is_slew){
								((PointingBlockSlew) newBlock).setBlockBefore(before);
								((PointingBlockSlew) newBlock).setBlockAfter(after);
								hardInsertBlock((PointingBlock)newBlock);
								return; //job done
							}
							else{
								//neither the block before, after or the new one are slews
								hardInsertBlock((PointingBlock)newBlock);
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
	public void setBlocks(PointingBlockInterface[] newBlocks){
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
		
		/*result=result+iString+"<"+SEGMENT_TAG+" "+NAME_TAG+"='"+getName()+"'>\n";
		result=result+iString+"\t"+"<"+METADATA_TAG+">\n";
		for (int i=0;i<includes.length;i++){
			result=result+iString+"\t\t"+"<"+INCLUDE_TAG+" "+HREF_TAG+"='"+includes[i]+"'/>\n";
		}
		result=result+iString+"\t"+"</"+METADATA_TAG+">\n";
		result=result+iString+"\t"+"<"+DATA_TAG+">\n";
		result=result+iString+"\t\t"+"<"+TIMELINE_TAG+" "+FRAME_TAG+"='"+getTimeLineFrame()+"'>\n";*/
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			/*if (blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !inObsSegment){
				obsBlockConter++;
				result=result+iString+"\t\t\t<!--OBS SLICE #"+String.format("%04d", obsBlockConter)+"-->\n";
				inObsSegment=true;
			}
			if (!blocks[i].getType().equals(PointingBlock.TYPE_OBS) && !blocks[i].getType().equals(PointingBlock.TYPE_SLEW)) inObsSegment=false;
			int blocknumber=i+1;
			result=result+iString+"\t\t\t<!-- BLOCK #"+blocknumber+"-->\n";*/
			result=result+blocks[i].toXml(indent+3);
		}
		/*result=result+iString+"\t\t"+"</"+TIMELINE_TAG+">\n";

		result=result+iString+"\t"+"</"+DATA_TAG+">\n";
		result=result+iString+"</"+SEGMENT_TAG+">\n";*/

		
		
		return result;
	}
	
	
	/**
	 * Get the block with a start time equal to the given one or the greates value before the given date
	 * @param time The desired time
	 * @return The block with a start time less or equal to the given time or null if there is no such block
	 */
	public PointingBlock getBlockAt(java.util.Date time){
		TreeMap<Date, PointingBlock> blMap = this.getBlMap();
		Entry<Date, PointingBlock> floorEntry = blMap.floorEntry(time);
		if (floorEntry==null) return null;
		PointingBlock result = floorEntry.getValue();
		if (result.getEndTime().before(time)) return null;
		return result;
	}
	
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
	/*public PointingBlocksSlice getBlocksAt(java.util.Date startTime,java.util.Date endTime){
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
		PointingBlocksSlice pbs = new PointingBlocksSlice();
		pbs.setBlocks(result);
		return pbs;
	//return getBlockAt(startTime);
	}*/
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
		return pbs;
	}
	
	public PointingBlocksSlice getAllMaintenanceBlocks(){
		
		
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(PointingBlock.TYPE_MNAV) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MNAV) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MOCM) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MSLW) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MWNV) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MWOL) ||
					blocks[i].getType().equals(PointingBlock.TYPE_MWAC)) result.add(blocks[i]);
		}
		
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		PointingBlocksSlice pbs = new PointingBlocksSlice();
		pbs.setBlocks(resultArray);
		return pbs;
	}
	
	public void setSlice(PointingBlockSetInterface slice){
		PointingBlockInterface[] obs = slice.getBlocks();
		for (int i=0;i<obs.length;i++){
			PointingBlock[] blocksToRemove = this.getBlocksAt(obs[i].getStartTime(), obs[i].getEndTime()).getBlocks();
			this.removeBlocks(blocksToRemove);
			this.addBlock(obs[i]);
			//PtrUtils.repairGaps(this);

		}
	}


	


}
