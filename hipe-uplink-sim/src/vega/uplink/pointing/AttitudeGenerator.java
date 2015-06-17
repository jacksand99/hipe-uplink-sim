package vega.uplink.pointing;

import java.util.Date;



import vega.uplink.track.Fecs;
import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.math.Attitude;

public interface AttitudeGenerator {
	
	public AttitudeMap getQuaternions(PointingBlock block);
	
	public AttitudeMap getQuaternions(PtrSegment segment);

	public AttitudeMap getQuaternions(Ptr ptr);
	
	public Attitude getAttitude(java.util.Date date);

	public java.util.Map<Long, Attitude> getAttitudes(PointingBlock block);
	
	public java.util.Map<Long, Attitude> getAttitudes(PtrSegment segment);

	public java.util.Map<Long, Attitude> getAttitudes(Ptr ptr);
	
	public Quaternion getQuaternion(java.util.Date date);

	
	public SolarAspectAngle getSaa(Date time);
	
	
	public SolarAspectAngle[] getSaa(PointingBlock block);
	
	public SolarAspectAngle[] getSaa(PtrSegment segment);
	
	public SolarAspectAngle[] getSaa(Ptr ptr);
	
	public String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm);
	
	public String checkPtr(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs);

	public String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm);
	
	public String checkPtrHTML(Ptr ptr,Ptr ptsl,Pdfm pdfm,Fecs fecs);

	 
	
	
	
}
