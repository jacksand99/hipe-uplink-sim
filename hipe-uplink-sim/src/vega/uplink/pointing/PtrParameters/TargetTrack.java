package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;

public class TargetTrack extends StateVector {
	public static String TARGET_TAG="target";
	public TargetTrack(PointingElement org){
		super(org);
	}
	/**
	 * Creates a new target refereed to a solar system object or landmarks defined in the PDFM
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public TargetTrack(String ref){
		super(TARGET_TAG,ref);
	}
	/**
	 * Creates a new target refereed to CG
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public TargetTrack(){
		this("CG");
	}
	/**
	 * Definition of a target that represents a landmark
	 * @param landmark The name of the landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public TargetTrack(String landmark,String origin,String frame,float x,float y,float z){
		super(TARGET_TAG,landmark,origin,frame,x,y,z);
	}
	/**
	 * Definition of a target that represents a landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param xUnit The unit that the X component of the landmark is expressed
	 * @param x The X component of the position of the landmark.
	 * @param yUnit The unit that the Y component of the landmark is expressed
	 * @param y The Y component of the position of the landmark. 
	 * @param zUnit The unit that the Z component of the landmark is expressed
	 * @param z The Z component of the position of the landmark. 
	 */
	public TargetTrack(String origin,String frame,String xUnit,float x,String yUnit,float y,String zUnit,float z){
		super(TARGET_TAG,origin,frame,xUnit,x,yUnit,y,zUnit,z);
	}
	/**
	 * Definition of a target that represents a landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public TargetTrack(String origin,String frame,float x,float y,float z){
		super(TARGET_TAG,origin,frame,x,y,z);
	}
	/**
	 * Definition of a target that represents a landmark
	 * @param landmark The name of the landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public TargetTrack(String landmark,String origin,String frame,String x,String y,String z){
		super(TARGET_TAG,landmark,origin,frame,x,y,z);
	}
	/**
	 * Definition of a target that represents a landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public TargetTrack(String origin,String frame,String x,String y,String z){
		super(TARGET_TAG,origin,frame,x,y,z);
	}

}
