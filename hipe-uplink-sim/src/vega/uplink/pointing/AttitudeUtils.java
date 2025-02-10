package vega.uplink.pointing;

import java.util.Date;

import herschel.ia.numeric.Long1d;
import herschel.share.fltdyn.math.Matrix3;
import herschel.share.fltdyn.math.Quaternion;

public class AttitudeUtils {
	
	/**
	 * Get the angular difference between two quaternions in degrees
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static double getAngularDistance(Quaternion q1,Quaternion q2){
		double angle = (q1.multiply(q2.conjugate())).normalizeSign().angle();
		double result = Units.convertUnit(angle, "rad", "deg");
		return result;
	}
	
	/**
	 * Get the interpolated valued for a given date, having the attitude in 2 dates before and after the desired one.
	 * @param date1
	 * @param q1
	 * @param date2
	 * @param q2
	 * @param date
	 * @return
	 */
	public static Quaternion interpolate(Date date1,Quaternion q1,Date date2, Quaternion q2,Date date){
		double delta = (1.0 * date.getTime() - date1.getTime()) / (date2.getTime() - date1.getTime());
		Quaternion result = q1.slerp(q2, delta);
		return result;

	}
	
	/**
	 * Get the angular difference in the X axis between 2 quaternions in degrees
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static double getAngularDistanceXAxis(Quaternion q1,Quaternion q2){
		Quaternion q = q1.conjugate().multiply(q2);
		Matrix3 m = q.toMatrix3();
		return  Units.convertUnit(Math.acos(m.get(0,0)), "rad", "deg");
	}
	/**
	 * Get the angular difference in the Y axis between 2 quaternions in degrees
	 * @param q1
	 * @param q2
	 * @return
	 */
	public static double getAngularDistanceYAxis(Quaternion q1,Quaternion q2){
		Quaternion q = q1.conjugate().multiply(q2);
		Matrix3 m = q.toMatrix3();
		return  Units.convertUnit(Math.acos(m.get(1,1)), "rad", "deg");
	}
	/**
	 * Get the angular difference in the Z axis between 2 quaternions in degrees
	 * @param q1
	 * @param q2
	 * @return
	 */
		public static double getAngularDistanceZAxis(Quaternion q1,Quaternion q2){
		Quaternion q = q1.conjugate().multiply(q2);
		Matrix3 m = q.toMatrix3();
		return  Units.convertUnit(Math.acos(m.get(2,2)), "rad", "deg");
	}
		
	public static int[] getInterval(Long1d array,int loc1,int loc2,long value){
		int[] result = new int[2];
		if (value >= array.get(loc1)){
			if (value <= array.get(loc2)){
				if (loc2 == loc1+1){
					
					result[0]=loc1;
					result[1]=loc2;
					return result;
				}
				result = getInterval(array, loc1, loc1+((loc2-loc1)/2), value);
				if (result==null){
					return getInterval(array, loc1+((loc2-loc1)/2), loc2, value);
				}else{
				
					return result;
				}
			}
		}
		return null;
	}
	
	public static int[] getInterval(Long[] array,int loc1,int loc2,long value){
		int[] result = new int[2];
		if (value >= array[loc1]){
			if (value <= array[loc2]){
				if (loc2 == loc1+1){
					
					result[0]=loc1;
					result[1]=loc2;
					return result;
				}
				result = getInterval(array, loc1, loc1+((loc2-loc1)/2), value);
				if (result==null){
					return getInterval(array, loc1+((loc2-loc1)/2), loc2, value);
				}else{
				
					return result;
				}
			}
		}
		return null;
	}

}
