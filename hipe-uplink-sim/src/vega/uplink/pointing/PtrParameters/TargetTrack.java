package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class TargetTrack extends StateVector {
	public TargetTrack(PointingMetadata org){
		super(org);
	}
	public TargetTrack(String ref){
		super("target",ref);
	}
	public TargetTrack(){
		this("CG");
	}
	public TargetTrack(String landmark,String origin,String frame,float x,float y,float z){
		super("target",landmark,origin,frame,x,y,z);
	}
	public TargetTrack(String origin,String frame,String xUnit,float x,String yUnit,float y,String zUnit,float z){
		super("target",origin,frame,xUnit,x,yUnit,y,zUnit,z);
	}

	public TargetTrack(String origin,String frame,float x,float y,float z){
		super("target",origin,frame,x,y,z);
	}

	public TargetTrack(String landmark,String origin,String frame,String x,String y,String z){
		super("target",landmark,origin,frame,x,y,z);
	}
	public TargetTrack(String origin,String frame,String x,String y,String z){
		super("target",origin,frame,x,y,z);
	}

}
