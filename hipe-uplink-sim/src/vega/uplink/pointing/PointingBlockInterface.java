package vega.uplink.pointing;



public interface PointingBlockInterface {
	
	public String getType();
	
	/**
	 * @return start date of the time interval during which the pointing block is valid
	 */
	public java.util.Date getStartTime();
	/**
	 * @return end date of the time interval during which the pointing block is valid
	 */		
	public java.util.Date getEndTime();
	
	/**
	 * Set start date of the time interval during which the pointing block is valid
	 * @param time
	 */
	public void setStartTime(java.util.Date time);
	/**
	 * Set end date of the time interval during which the pointing block is valid
	 * @param time
	 */		
	public void setEndTime(java.util.Date time);
	
	/**
	 * @return Duration of this block in milliseconds
	 */
	public long getDuration();
	/**
	 * Set the attitude of this block
	 * @param newAttitude
	 */
	public void setAttitude(PointingAttitude newAttitude);
	/**
	 * Get the attitude of this block
	 */
	public PointingAttitude getAttitude();
	
	/**
	 * Get a specific metadata of the given name (vstpNumber,positionError,hgaRequest,comment or planning)
	 * @param name
	 * @return
	 */
	public PointingElement getMetadata(String name);
	public PointingMetadata getMetadataElement();
	/**
	 * Get all the metadata that this block have or null if it has no metadata
	 * @return
	 */
	public PointingElement[] getMetadata();
	
	/**
	 * The child element <metadata> can have the child elements:
	 * <vstpNumber> is present in the first block of a VSTP and contains the VSTPnumber
	 * (three digits with leading zero).
	 * <positionError> contains the maximum position error for this and the
	 * following blocks that do not contain an updated position error in metadata.
	 * <positionError> contains the three simple child elements <alongtrack
	 * units=km>, <crosstrack units=km> and <radial units=km>. Each
	 * element contains a list of maximum position offset along the respective direction. If
	 * the length of these lists is longer than 1 then the additional element <epoch> must
	 * be provided that contains a list of times. Each time must have the format YYYYMM-
	 * DDTHH:MM:SS[.SSS]Z. The length of the lists in all elements must be the
	 * same.
	 * <hgaRequest> can be used to specify the required HGA articulation.
	 * <comment> optional element for inserting comments.
	 * <planning> the SGS may use this element to store any information
	 * If <metadata> has no child elements the element may be omitted.
	 * @param newMetadata
	 */
	public void addMetadata(PointingMetadata newMetadata);
	
	public void setMetadata(PointingMetadata meta);
	
	
	

	
	

	

	
	public  boolean isSlew();
	public boolean isMaintenance();
	
	public boolean validate();
	
	public String getInstrument();
	
	public int getVstpNumberMeta();
	
}
