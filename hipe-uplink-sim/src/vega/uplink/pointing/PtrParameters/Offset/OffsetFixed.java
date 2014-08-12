package vega.uplink.pointing.PtrParameters.Offset;

import vega.uplink.pointing.PointingMetadata;

public class OffsetFixed extends OffsetAngles{
	
	public OffsetFixed(PointingMetadata org){
		super(org);
	}

	/**
	 * If offsetAngles contains the attribute ref=fixed two fixed rotation angles can be specified.
	 * @param xAngle Rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis) expressed in deg.
	 * @param yAngle Rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis) expressed in deg.
	 */	
	public OffsetFixed(String xAngle,String yAngle){
		super("fixed");
		setXAngle(Float.parseFloat(xAngle));
		setXAngleUnit("deg");
		setYAngle(Float.parseFloat(yAngle));
		setYAngleUnit("deg");

	}
	/**
	 * If offsetAngles contains the attribute ref=fixed two fixed rotation angles can be specified.
	 * @param xAngleUnit Units used to express the xAngle, for example deg.
	 * @param xAngle Rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis)
	 * @param yAngleUnit Units used to express the yAngle, for example deg.
	 * @param yAngle Rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis)
	 */
	public OffsetFixed(String xAngleUnit,float xAngle,String yAngleUnit,float yAngle){
		super("fixed");
		setXAngle(xAngle);
		setXAngleUnit(xAngleUnit);
		setYAngle(yAngle);
		setYAngleUnit(yAngleUnit);
		
	}
	/**
	 * If offsetAngles contains the attribute ref=fixed two fixed rotation angles can be specified.
	 * @param xAngle Rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis) expressed in deg.
	 * @param yAngle Rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis) expressed in deg.
	 */		
	public OffsetFixed(float xAngle,float yAngle){
		super("fixed");
		setXAngle(xAngle);
		setXAngleUnit("deg");
		setYAngle(yAngle);
		setYAngleUnit("deg");
	}
	
	/**
	 * Set the Rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis)
	 * @param xAngle
	 */
	public void setXAngle(float xAngle){
		setFloatField("xAngle",xAngle);
	}
	/**
	 * Get the Rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis)
	 */	
	public float getXAngle(){
		return Float.parseFloat(getChild("xAngle").getValue());
	}
	
	/**
	 * Set the unit used to express the rotation angle of the boresight towards the offset-x-axis (rotation around plus offset-yaxis)
	 * @param unit
	 */
	public void setXAngleUnit(String unit){
		setUnit("xAngle",unit);
	}
	
	/**
	 * Set the rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis)
	 * @param yAngle
	 */
	public void setYAngle(float yAngle){
		setFloatField("yAngle",yAngle);
	}
	/**
	 * Set the unit used to express the rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis)
	 * @param unit
	 */	
	public void setYAngleUnit(String unit){
		setUnit("yAngle",unit);
	}

	/**
	 * Get the rotation angle of the boresight towards the offset-y-axis (rotation around minus offset-xaxis)
	 */
	public float getYAngle(){
		return Float.parseFloat(getChild("yAngle").getValue());
	}


	
	/**
	 * If offsetAngles contains the attribute ref=fixed two fixed rotation angles can be specified.
	 * The rotation are 0.0 and 0.0
	 */
	public OffsetFixed(){
		this("0.0","0.0");
	}
	
	public boolean isFixed(){
		return true;
	}


	@Override
	public long getDurationMilliSecs() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public OffsetFixed copy() {
		OffsetFixed result = new OffsetFixed();
		
		//result.setValue(getValue());
		PointingMetadata[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingMetadata[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		//System.out.println(result.toXml(0));

		return result;
	}


}
