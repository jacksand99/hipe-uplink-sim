package vega.uplink.pointing;

import java.util.Date;


import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.math.Attitude;

public interface AttitudeGenerator {
	
	public java.util.Map<Long, Quaternion> getQuaternions(PointingBlock block);
	
	public java.util.Map<Long, Quaternion> getQuaternions(PtrSegment segment);

	public java.util.Map<Long, Quaternion> getQuaternions(Ptr ptr);
	
	public Attitude getAttitude(java.util.Date date);

	public java.util.Map<Long, Attitude> getAttitudes(PointingBlock block);
	
	public java.util.Map<Long, Attitude> getAttitudes(PtrSegment segment);

	public java.util.Map<Long, Attitude> getAttitudes(Ptr ptr);
	
	public Quaternion getQuaternion(java.util.Date date);

	
	public SolarAspectAngle getSaa(Date time);
	
	
	public SolarAspectAngle[] getSaa(PointingBlock block);
	
	public SolarAspectAngle[] getSaa(PtrSegment segment);
	
	public SolarAspectAngle[] getSaa(Ptr ptr);
	
	
	
}
