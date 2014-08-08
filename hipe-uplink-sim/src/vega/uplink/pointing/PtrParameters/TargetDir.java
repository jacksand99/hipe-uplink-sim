package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class TargetDir extends DirectionVector {
	public TargetDir(){
		//super("targetDir");
		this("CG2Sun");
	}
	public TargetDir(String frame,float longitude,float latitude){
		super("targetDir",frame,longitude,latitude);
	}
	public TargetDir(PointingMetadata pm){
		super("targetDir",pm);
	}
	public TargetDir(String reference){
		super("targetDir",reference);
	}
	public TargetDir(String frame,float x,float y, float z){
		super("targetDir",frame,x,y,z);
	}
	public TargetDir(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		super("targetDir",frame,unitLongitude,longitude,unitLatitude,latitude);
	}
	public TargetDir(String origin,String target){
		super("targetDir",origin,target);
	}
	public TargetDir(String axis,String rotationAxis,float rotationAngle){
		super("targetDir",axis,rotationAxis,rotationAngle);
	}
	public TargetDir(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		super("targetDir",axis,rotationAxis,rotationAngleUnit,rotationAngle);
		
	}

}
