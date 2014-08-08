package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

//public class OffsetRefAxis extends PointingMetadata {
public class OffsetRefAxis extends DirectionVector {	
	public OffsetRefAxis(){
		//super("offsetRefAxis");
		this("SC_Xaxis");
	}
	public OffsetRefAxis(String frame,float longitude,float latitude){
		super("offsetRefAxis",frame,longitude,latitude);
	}
	public OffsetRefAxis(PointingMetadata pm){
		super("offsetRefAxis",pm);
	}
	public OffsetRefAxis(String reference){
		super("offsetRefAxis",reference);
	}
	public OffsetRefAxis(String frame,float x,float y, float z){
		super("offsetRefAxis",frame,x,y,z);
	}
	public OffsetRefAxis(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		super("offsetRefAxis",frame,unitLongitude,longitude,unitLatitude,latitude);
	}
	public OffsetRefAxis(String origin,String target){
		super("offsetRefAxis",origin,target);
	}
	public OffsetRefAxis(String axis,String rotationAxis,float rotationAngle){
		super("offsetRefAxis",axis,rotationAxis,rotationAngle);
	}
	public OffsetRefAxis(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		super("offsetRefAxis",axis,rotationAxis,rotationAngleUnit,rotationAngle);
		
	}

/*	public OffsetRefAxis(PointingMetadata org){
		super(org);
	}

	public OffsetRefAxis(String ref){
		super("offsetRefAxis","");
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public OffsetRefAxis(){
		this("SC_Xaxis");
	}*/

}
