package vega.uplink.pointing;

import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.share.fltdyn.time.FineTime;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;


/**
 * Contains the pointing request for a certain MTP period
 * @author jarenas
 *
 */
public class PtrSegment extends Product{
	private String name;
	private String[] includes;
	private TreeMap<Date,PointingBlock> blMap;
	
	private String getBlockName(PointingBlock block){
		String result=block.getType()+" "+PointingBlock.dateToZulu(block.getStartTime());
		return result;
				
	}
	
	
	/**
	 * Create a PTR segment with the given name
	 * @param segmentName Name of the segment
	 */
	public PtrSegment(String segmentName){
		super();
		blMap=new TreeMap<Date,PointingBlock>();
		name=segmentName;
		includes=new String[0];
		this.setTimeLineFrame("SC");
	}
	
	public Product asProduct(){
		return this;

	}
	/**
	 * Get all blocks of this segment as an array
	 * @return
	 */
	private PointingBlock[] getBlocksasArry(){
		PointingBlock[] result=new PointingBlock[blMap.size()];
		Iterator<Entry<Date, PointingBlock>> it = blMap.entrySet().iterator();
		int i=0;
		while (it.hasNext()){
			result[i]=it.next().getValue();
			i++;
		}
		return result;
		
	}
	private void hardRemoveBlock(PointingBlock block){
		remove(getBlockName(block));
		blMap.remove(block.getStartTime());
	}
	
	private void hardInsertBlock(PointingBlock block){
		set(getBlockName(block),block);
		blMap.put(block.getStartTime(), block);
		//System.out.println("inserted "+block.toXml(0));
		if (!blMap.lastEntry().getValue().getType().equals(PointingBlock.TYPE_SLEW)){
			this.setValidityDates(blMap.firstKey(), blMap.lastEntry().getValue().getEndTime());
		}
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
			if (before.getValue().equals("SLEW") && after.getType().equals("SLEW")){
				PointingBlockSlew slewBefore=(PointingBlockSlew) before;
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				hardRemoveBlock(slewBefore);
				hardRemoveBlock(slewAfter);
				PointingBlockSlew newSlew = new PointingBlockSlew();
				newSlew.setBlockBefore(slewBefore.getBlockBefore());
				newSlew.setBlockAfter(slewAfter.getBlockAfter());
				hardInsertBlock(newSlew);				
			}
			if (before.getValue().equals("SLEW") && !after.getType().equals("SLEW")){
				PointingBlockSlew slewBefore=(PointingBlockSlew) before;
				slewBefore.setBlockAfter(after);				
			}
			if (!before.getValue().equals("SLEW") && after.getType().equals("SLEW")){
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				slewAfter.setBlockBefore(before);
			}
		}
		if (before==null && after!=null){
			if (after.getType().equals("SLEW")){
				PointingBlockSlew slewAfter=(PointingBlockSlew) after;				
				hardRemoveBlock(slewAfter);
			}
		}
		if (before!=null && after==null){
			if (before.getType().equals("SLEW")){
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
	private PointingBlock blockBefore(PointingBlock block){
		if (block==null) return null;

		long time=block.getStartTime().getTime()-1;
		Entry<Date, PointingBlock> result = blMap.floorEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
	}
	private PointingBlock blockAfter(PointingBlock block){
		if (block==null) return null;



		long time=block.getStartTime().getTime()+1;
		Entry<Date, PointingBlock> result = blMap.ceilingEntry(new Date(time));
		if (result==null) return null;
		else return result.getValue();
	}
	
	/**
	 * Check if there is any gap before a MOCM, MWOL or MSLW block (which have) internal slews and extend the observation before to end at the beginning of the maintenance block
	 */
	public void repairMocm(){
		Iterator<Entry<Date, PointingBlock>> it = ((TreeMap<Date,PointingBlock>)blMap.clone()).entrySet().iterator();
		while (it.hasNext()){
			PointingBlock block = it.next().getValue();
			if (block.getType().equals(PointingBlock.TYPE_MOCM) || block.getType().equals(PointingBlock.TYPE_MWOL) || block.getType().equals(PointingBlock.TYPE_MSLW)){
				PointingBlock before = blockBefore(block);
				if (before!=null){
					if (before.getType().equals(PointingBlock.TYPE_SLEW)){
						PointingBlock beforeSlew=blockBefore(before);
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
	
	/**
	 * Check if there is any gap and fill it with slews. It is recommended to run repairMocm() before to avoid that a slew 
	 */
	public void repairSlews(){
		PointingBlock[] blocks = getBlocks();

		for (int i=0;i<blocks.length;i++){
			PointingBlock block = blocks[i];
			PointingBlock before = blockBefore(block);
			PointingBlock after = blockAfter(block);
			if (before!=null && !before.getType().equals("SLEW") && !before.getEndTime().equals(block.getStartTime())){
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
		boolean new_is_slew = newBlock.getType().equals("SLEW");
		Entry<Date, PointingBlock> lastEntry = blMap.lastEntry();
		PointingBlock before;
		if (new_is_slew &&((PointingBlockSlew) newBlock).getBlockBefore()==null){
			before=lastEntry.getValue();
		}
		else before= blockBefore(newBlock);
		PointingBlock after;
		if (new_is_slew &&((PointingBlockSlew) newBlock).getBlockAfter()==null){
			after=null;
		}
		else after= blockAfter(newBlock);
		boolean before_is_slew=(before!=null && before.getType().equals("SLEW"));
		boolean after_is_slew=(after!=null && after.getType().equals("SLEW"));
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
							return; //can not insert a slew after a slew
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
		return name;
	}
	
	/**
	 * Set or replace the name of this segment
	 * @param newName
	 */
	public void setName(String newName){
		name=newName;
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
		String result=new String();
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		
		result=result+iString+"<segment name='"+getName()+"'>\n";
		result=result+iString+"\t"+"<metadata>\n";
		for (int i=0;i<includes.length;i++){
			result=result+iString+"\t\t"+"<include href='"+includes[i]+"'/>\n";
		}
		result=result+iString+"\t"+"</metadata>\n";
		result=result+iString+"\t"+"<data>\n";
		result=result+iString+"\t\t"+"<timeline frame='"+getTimeLineFrame()+"'>\n";
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			int blocknumber=i+1;
			result=result+"<!-- BLOCK #"+blocknumber+"-->\n";
			result=result+blocks[i].toXml(indent+3);
		}
		result=result+iString+"\t\t"+"</timeline>\n";

		result=result+iString+"\t"+"</data>\n";
		result=result+iString+"</segment>\n";

		
		
		return result;
	}
	
	
	/**
	 * Get the block with a start time equal to the given one or the greates value before the given date
	 * @param time The desired time
	 * @return The block with a start time less or equal to the given time
	 */
	public PointingBlock getBlockAt(java.util.Date time){
		return blMap.floorEntry(time).getValue();
	}
	
	/*public PointingBlock getBlockAt(java.util.Date startTime,java.util.Date endTime){
		return getBlockAt(startTime);
	}*/
	
	/**
	 * Get all blocks of a given type
	 * @param blockType type of blocks to search for
	 * @return an array of blocks of the given type
	 */
	public PointingBlock[] getAllBlocksOfType(String blockType){
		java.util.Vector<PointingBlock> result=new java.util.Vector<PointingBlock>();
		PointingBlock[] blocks = getBlocks();
		for (int i=0;i<blocks.length;i++){
			if (blocks[i].getType().equals(blockType)) result.add(blocks[i]);
		}
		
		PointingBlock[] resultArray=new PointingBlock[result.size()];
		result.toArray(resultArray);
		return resultArray;
	}
	
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
	
	
	
}
