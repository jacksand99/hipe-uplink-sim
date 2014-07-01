package vega.uplink.pointing;

import herschel.share.fltdyn.math.Quaternion;

public interface AttitudeGenerator {
	
	public java.util.HashMap<Long, Quaternion> getAttitude(PointingBlock block);
	
	public java.util.HashMap<Long, Quaternion> getAttitude(PtrSegment segment);

	public java.util.HashMap<Long, Quaternion> getAttitude(Ptr ptr);
	
	public Quaternion getAttitude(java.util.Date date);
}
