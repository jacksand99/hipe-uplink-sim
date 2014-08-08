package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class TargetInert extends DirectionVector {
	public TargetInert(){
		//super("target");
		this("EME2000",0,0,1);
	}
	public TargetInert(String frame,float longitude,float latitude){
		super("target",frame,longitude,latitude);
	}
	public TargetInert(PointingMetadata pm){
		super("target",pm);
	}
	public TargetInert(String reference){
		super("target",reference);
	}
	public TargetInert(String frame,float x,float y, float z){
		super("target",frame,x,y,z);
	}
	public TargetInert(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		super("target",frame,unitLongitude,longitude,unitLatitude,latitude);
	}
	public TargetInert(String origin,String target){
		super("target",origin,target);
	}
	public TargetInert(String axis,String rotationAxis,float rotationAngle){
		super("target",axis,rotationAxis,rotationAngle);
	}
	public TargetInert(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		super("target",axis,rotationAxis,rotationAngleUnit,rotationAngle);
		
	}
}
