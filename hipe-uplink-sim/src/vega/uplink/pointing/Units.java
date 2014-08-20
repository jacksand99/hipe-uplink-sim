package vega.uplink.pointing;

import herschel.share.unit.Unit;
import herschel.share.unit.impl.UnitFactory;

public class Units {
	//Angle
	public static String DEGREE="deg";
	public static String RADIANS="rad";
	public static String ARCMINUTE="arcMin";
	public static String ARCSECOND="arcSec";
	//Angular velocity
	public static String DEGREES_PER_SECOND="deg/sec";
	public static String RADIANS_PER_SECONS="rad/sec";
	public static String ARCMINUTES_PER_SECOND="arcSec/sec";
	public static String ARCSECONDS_PER_SECOND="arcSec/sec";

	public static String DEGREES_PER_MINUTE="deg/min";
	public static String RADIANS_PER_MINUTE="rad/min";
	public static String ARCMINUTES_PER_MINUTE="arcSec/min";
	public static String ARCSECONDS_PER_MINUTE="arcSec/min";

	public static String DEGREES_PER_HOUR="deg/hour";
	public static String RADIANS_PER_HOUR="rad/hour";
	public static String ARCMINUTES_PER_HOUR="arcSec/hour";
	public static String ARCSECONDS_PER_HOUR="arcSec/hour";
	//Distance
	public static String KILOMETER="km";
	public static String METER="m";
	//Time
	public static String SECONDS="sec";
	public static String MINUTES="min";
	public static String HOUR="hour";
	public static String DAY="day";
	private static String[] allowedAngleSet={DEGREE,RADIANS,ARCMINUTE,ARCSECOND}; 
	private static String[] allowedTimeSet={SECONDS,MINUTES,HOUR,DAY};
	private static String[] allowedDistance={KILOMETER,METER};
	private static String[] allowedAngularVelocity={DEGREES_PER_SECOND,RADIANS_PER_SECONS,ARCMINUTES_PER_SECOND,ARCSECONDS_PER_SECOND,DEGREES_PER_MINUTE,RADIANS_PER_MINUTE,ARCMINUTES_PER_MINUTE,ARCSECONDS_PER_MINUTE,DEGREES_PER_HOUR,RADIANS_PER_HOUR,ARCMINUTES_PER_HOUR,ARCSECONDS_PER_HOUR};
	
	public static long convertUnit(long orgValue,String orgUnit,String targetUnit){
		Unit<?> unit1 = UnitFactory.getUnit(translateUnit(orgUnit));
		Unit<?> unit2 = UnitFactory.getUnit(translateUnit(targetUnit));
		double value = unit1.getValue(orgValue, unit2);
		return new Double(value).longValue();
	}

	public static int convertUnit(int orgValue,String orgUnit,String targetUnit){
		Unit<?> unit1 = UnitFactory.getUnit(translateUnit(orgUnit));
		Unit<?> unit2 = UnitFactory.getUnit(translateUnit(targetUnit));
		double value = unit1.getValue(orgValue, unit2);
		return new Double(value).intValue();
	}
	public static double convertUnit(double orgValue,String orgUnit,String targetUnit){
		Unit<?> unit1 = UnitFactory.getUnit(translateUnit(orgUnit));
		Unit<?> unit2 = UnitFactory.getUnit(translateUnit(targetUnit));
		double value = unit1.getValue(orgValue, unit2);
		return new Double(value);
	}
	public static float convertUnit(float orgValue,String orgUnit,String targetUnit){
		Unit<?> unit1 = UnitFactory.getUnit(translateUnit(orgUnit));
		Unit<?> unit2 = UnitFactory.getUnit(translateUnit(targetUnit));
		double value = unit1.getValue(orgValue, unit2);
		return new Double(value).floatValue();
	}

	public static float[] convertUnit(float[] orgValue,String orgUnit,String targetUnit){
		int lenght = orgValue.length;
		float[] result=new float[lenght];
		for (int i=0;i<lenght;i++){
			result[i]=convertUnit(orgValue[i],orgUnit,targetUnit);
		}
		return result;
	}
	public static int[] convertUnit(int[] orgValue,String orgUnit,String targetUnit){
		int lenght = orgValue.length;
		int[] result=new int[lenght];
		for (int i=0;i<lenght;i++){
			result[i]=convertUnit(orgValue[i],orgUnit,targetUnit);
		}
		return result;
	}
	public static long[] convertUnit(long[] orgValue,String orgUnit,String targetUnit){
		int lenght = orgValue.length;
		long[] result=new long[lenght];
		for (int i=0;i<lenght;i++){
			result[i]=convertUnit(orgValue[i],orgUnit,targetUnit);
		}
		return result;
	}
	public static double[] convertUnit(double[] orgValue,String orgUnit,String targetUnit){
		int lenght = orgValue.length;
		double[] result=new double[lenght];
		for (int i=0;i<lenght;i++){
			result[i]=convertUnit(orgValue[i],orgUnit,targetUnit);
		}
		return result;
	}
	
	private static String translateUnit(String unit){
		String result=unit;
		if (unit.equals(SECONDS)) result="s";
		if (unit.equals(HOUR)) result="h";
		if (unit.equals(DAY)) result="d";
		if (unit.equals(DEGREES_PER_SECOND)) result="deg s-1";
		if (unit.equals(RADIANS_PER_SECONS)) result="rad s-1";
		if (unit.equals(ARCMINUTES_PER_SECOND)) result="arcmin s-1";
		if (unit.equals(ARCSECONDS_PER_SECOND)) result="arcsec s-1";
		if (unit.equals(DEGREES_PER_MINUTE)) result="deg min-1";
		if (unit.equals(RADIANS_PER_MINUTE)) result="rad min-1";
		if (unit.equals(ARCMINUTES_PER_MINUTE)) result="arcmin min-1";
		if (unit.equals(ARCSECONDS_PER_MINUTE)) result="arcsec min-1";
		if (unit.equals(DEGREES_PER_HOUR)) result="deg h-1";
		if (unit.equals(RADIANS_PER_HOUR)) result="rad h-1";
		if (unit.equals(ARCMINUTES_PER_HOUR)) result="arcmin h-1";
		if (unit.equals(ARCSECONDS_PER_HOUR)) result="arcsec h-1";
		if (unit.equals(ARCMINUTE)) result="arcmin";
		if (unit.equals(ARCSECOND)) result="arcsec";		
		return result;
	}
	
	public static boolean isValidAngleUnit(String unit){
		return java.util.Arrays.asList(allowedAngleSet).contains(unit);
	}
	public static boolean isValidAngularVelocityUnit(String unit){
		return java.util.Arrays.asList(allowedAngularVelocity).contains(unit);
	}
	public static boolean isValidDistanceUnit(String unit){
		return java.util.Arrays.asList(allowedDistance).contains(unit);
	}
	public static boolean isValidTimeUnit(String unit){
		return java.util.Arrays.asList(allowedTimeSet).contains(unit);
	}
	public static boolean isValidUnit(String unit){
		boolean result=false;
		if (isValidAngleUnit(unit)) result=true;
		if (isValidAngularVelocityUnit(unit)) result=true;
		if (isValidDistanceUnit(unit)) result=true;
		if (isValidTimeUnit(unit)) result=true;		
		return result;
	}

}
