package vega.uplink.pointing;

import herschel.share.fltdyn.math.Quaternion;

import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class AttitudeMap extends TreeMap<Long,Quaternion>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Quaternion put(Long time,Quaternion attitude){
		return super.put(time, attitude);
	}
	
	public Quaternion put(Date time,Quaternion attitude){
		return put(time.getTime(),attitude);
	}
	
	public Quaternion get(Long time){
		if (time<super.firstKey()) return super.firstEntry().getValue();
		if (time>super.lastKey()) return super.lastEntry().getValue();

		Quaternion result = super.get(time);
		if (result!=null) return result;
		Quaternion q1 = super.ceilingEntry(time).getValue();
		Quaternion q2 = super.floorEntry(time).getValue();
		Long time1 = super.ceilingKey(time);
		Long time2 = super.floorKey(time);
		return AttitudeUtils.interpolate(new java.util.Date(time1),q1,new java.util.Date(time2),q2,new Date(time));
	}
	
	public Quaternion get(Date time){
		return get(time.getTime());
	}
	
	public AttitudeMap subMap(Long fromTime,Long toTime){
		SortedMap<Long, Quaternion> r = super.subMap(fromTime, toTime);
		AttitudeMap result = new AttitudeMap();
		result.putAll(r);
		return result;
		
	}
	public AttitudeMap subMap(Date fromTime,Date toTime){
		return subMap(fromTime.getTime(),toTime.getTime());
	}
	
	public double getAngularDistance(PointingBlock b1,PointingBlock b2){
		return AttitudeUtils.getAngularDistance(get(b1.getEndTime()),get(b2.getStartTime()));
	}
	
	public double getOffsetXAxis(PointingBlock b1,PointingBlock b2){
		return AttitudeUtils.getAngularDistanceXAxis(get(b1.getEndTime()),get(b2.getStartTime()));
	}
	
	public double getOffsetYAxis(PointingBlock b1,PointingBlock b2){
		return AttitudeUtils.getAngularDistanceYAxis(get(b1.getEndTime()),get(b2.getStartTime()));
	}
	
	public double getOffsetZAxis(PointingBlock b1,PointingBlock b2){
		return AttitudeUtils.getAngularDistanceZAxis(get(b1.getEndTime()),get(b2.getStartTime()));
	}
}
