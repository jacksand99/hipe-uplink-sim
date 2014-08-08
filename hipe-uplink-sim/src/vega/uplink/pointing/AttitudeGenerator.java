package vega.uplink.pointing;

import java.util.Date;


import herschel.share.fltdyn.math.Quaternion;

public interface AttitudeGenerator {
	
	public java.util.Map<Long, Quaternion> getAttitude(PointingBlock block);
	
	public java.util.Map<Long, Quaternion> getAttitude(PtrSegment segment);

	public java.util.Map<Long, Quaternion> getAttitude(Ptr ptr);
	
	public Quaternion getAttitude(java.util.Date date);
	
	public SolarAspectAngle getSaa(Date time);
	
	
	public SolarAspectAngle[] getSaa(PointingBlock block);
	
	public SolarAspectAngle[] getSaa(PtrSegment segment);
	
	public SolarAspectAngle[] getSaa(Ptr ptr);
	
	
	
}
