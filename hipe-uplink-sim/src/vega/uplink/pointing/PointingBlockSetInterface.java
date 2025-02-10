package vega.uplink.pointing;



public interface PointingBlockSetInterface {
	public void regenerate(PointingBlockSetInterface slice);
	
	//public PointingBlockSetInterface copy();
	

	/**
	 * Get the name of this segment
	 * @return name of the segment
	 */
	public String getName();
	
	/**
	 * Set or replace the name of this segment
	 * @param newName
	 */
	public void setName(String newName);


	
	

	


	

	
	/**
	 * Remove a block from the segment. It adjust as well the slews if they are affected by the removal.
	 * @param block Block to be removed
	 */
	public void removeBlock(PointingBlockInterface block);
	
	/**
	 * Get the block before the one given (before the start time the block), or null if there is no block before this one
	 * @param block
	 * @return
	 */
	public PointingBlockInterface blockBefore(PointingBlockInterface block);
	public PointingBlockInterface blockAfter(PointingBlockInterface block);
	

	
	/**
	 * Add a new block to the given segment
	 * @param newBlock Block to be added
	 */
	public void addBlock(PointingBlockInterface newBlock);
	
	/**
	 * Get all the blocks in this segment
	 * @return Array with all the blocks in this segment
	 */
	public PointingBlockInterface[] getBlocks();
	
	
	/**
	 * Remove all currents blocks and substitute by the given ones
	 * @param newBlocks New blocks to replace the current ones
	 */
	public void setBlocks(PointingBlockInterface[] newBlocks);
	

	

	

	

	

	

	
	/**
	 * Get the XML representation of this segment as per FD ICD
	 * @param indent the Number of tabs to be added before each line
	 * @return The XML representation of this segment
	 */
	public String toXml(int indent);
	
	
	/**
	 * Get the block with a start time equal to the given one or the greates value before the given date
	 * @param time The desired time
	 * @return The block with a start time less or equal to the given time or null if there is no such block
	 */
	public PointingBlock getBlockAt(java.util.Date time);
	

	public PointingBlockSetInterface getBlocksAt(java.util.Date startTime,java.util.Date endTime);


	
	public void removeBlocks(PointingBlock[] blocks);
	
	/**
	 * Get all blocks of a given type
	 * @param blockType type of blocks to search for
	 * @return an array of blocks of the given type
	 */

	public PointingBlockSetInterface getAllBlocksOfType(String blockType);
	
	public void setSlice(PointingBlockSetInterface slice);

}
