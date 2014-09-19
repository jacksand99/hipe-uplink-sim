package vega.uplink.pointing.PtrParameters.Offset;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

public class OffsetCustom extends OffsetAngles {
	/**
	 * xRates
	 */
	public static String XRATES_FIELD="xRates";
	/**
	 * yRates
	 */	
	public static String YRATES_FIELD="yRates";
	/**
	 * xAngles
	 */
	public static String XANGLES_FIELD="xAngles";
	/**
	 * yAngles
	 */
	public static String YANGLES_FIELD="yAngles";
	/**
	 * deltaTimes
	 */
	public static String DELTATIMES_FIELD="deltaTimes";
	/**
	 * startTime
	 */
	public static String STARTTIME_FIELD="startTime";
	/**
	 * min
	 */
	public static String DELTATIMES_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * deg
	 */
	public static String XANGLES_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * deg
	 */
	public static String YANGLES_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * deg/sec
	 */
	public static String XRATES_DEFAULT_UNIT=Units.DEGREES_PER_SECOND;
	/**
	 * deg/sec
	 */
	public static String YRATES_DEFAULT_UNIT=Units.DEGREES_PER_SECOND;

	public OffsetCustom(PointingElement org){
		super(org);
		normalize();
	}
	public void regenerate(PointingElement org){
		super.regenerate(org);
		normalize();
	}
	
	private OffsetCustom(){
		super(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM);
	}

	/**
	 * If offsetAngles contains the attribute ref=custom a customised path of offset
	 * angles can be specified by providing rotation angles and rates at certain times. Table 7 lists
	 * the corresponding parameters.
	 * For customised offset angles the rotation angles and rates are specified for the offset-x & yaxes
	 * for a certain list of times. The times are specified by a list of delta times relative to a
	 * start-time. The first time is defined by the start-time plus the first delta-time in the list. All
	 * following delta times are defined as delta relative to the previous time in the list. The
	 * element deltaTimes must contain at least two elements. The elements xAngle, yAngle,
	 * xRates and yRates must have the same number of elements as delta-time. The elements
	 * define the offset angles and rates at the respective time.
	 * The rotation angles between two times are defined as 3rd-order polynomial that matches
	 * the angles and rates at the interval borders. The rotation angles before and after the last
	 * point in time result from extrapolation of the polynomial in the first and last interval,
	 * respectively.
	 * @param startTime Start time of the off set rotation
	 * @param deltaTimesUnits The time unit that is used to express the deltatimes (for example min).
	 * @param deltaTimes List of delta time. All entries must be positive (because the delta times are cumulative).
	 * @param xAnglesUnits The angle unit used to express the xAngles (for example deg) 
	 * @param xAngles Rotation angles towards the offset-x-axis. The list must be of the same length as deltaTimes
	 * @param xRatesUnits The angle rotation speed unit used to express the xRates (for example deg/sec)
	 * @param xRates Rotation rate towards the offset-x-axis. The list must be of the same length as deltaTimes
	 * @param yAnglesUnits  The angle unit used to express the yAngles (for example deg) 
	 * @param yAngles Rotation angles towards the offset-y-axis. The list must be of the same length as deltaTimes
	 * @param yRatesUnits The angle rotation speed unit used to express the yRates (for example deg/sec)
	 * @param yRates Rotation rate towards the offset-y-axis. The list must be of the same length as deltaTimes
	 * @throws ParseException
	 */
	public OffsetCustom (Date startTime,String deltaTimesUnits,float[] deltaTimes,String xAnglesUnits,float[] xAngles,String xRatesUnits,float[] xRates,String yAnglesUnits,float[] yAngles,String yRatesUnits,float[] yRates) throws ParseException{
		super(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM);
		setStartDate(startTime);
		setDeltaTimesUnit(deltaTimesUnits);
		setDeltaTimes(deltaTimes);
		setXAngles(xAngles);
		setXAnglesUnit(xAnglesUnits);
		setXRates(xRates);
		setXRatesUnit(xRatesUnits);
		setYAngles(yAngles);
		setYAnglesUnit(yAnglesUnits);
		setYRates(yRates);
		setYRatesUnit(yRatesUnits);
		
	}
	/**
	 * If offsetAngles contains the attribute ref=custom a customised path of offset
	 * angles can be specified by providing rotation angles and rates at certain times. Table 7 lists
	 * the corresponding parameters.
	 * For customised offset angles the rotation angles and rates are specified for the offset-x & yaxes
	 * for a certain list of times. The times are specified by a list of delta times relative to a
	 * start-time. The first time is defined by the start-time plus the first delta-time in the list. All
	 * following delta times are defined as delta relative to the previous time in the list. The
	 * element deltaTimes must contain at least two elements. The elements xAngle, yAngle,
	 * xRates and yRates must have the same number of elements as delta-time. The elements
	 * define the offset angles and rates at the respective time.
	 * The rotation angles between two times are defined as 3rd-order polynomial that matches
	 * the angles and rates at the interval borders. The rotation angles before and after the last
	 * point in time result from extrapolation of the polynomial in the first and last interval,
	 * respectively.
	 * @param startTime Start time of the off set rotation
	 * @param deltaTimes List of delta time. All entries must be positive (because the delta times are cumulative). The time unit must be min.
	 * @param xAngles Rotation angles towards the offset-x-axis. The list must be of the same length as deltaTimes. The angles must be expressed in deg.
	 * @param xRates Rotation rate towards the offset-x-axis. The list must be of the same length as deltaTimes. The speed must be expressed in deg/sec.
	 * @param yAngles Rotation angles towards the offset-y-axis. The list must be of the same length as deltaTimes. The angles must be expressed in deg.
	 * @param yRates Rotation rate towards the offset-y-axis. The list must be of the same length as deltaTimes. The speed must be expressed in deg/sec.
	 * @throws ParseException
	 */

	public OffsetCustom (Date startTime,float[] deltaTimes,float[] xAngles,float[] xRates,float[] yAngles,float[] yRates) throws ParseException{
		super(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM);
		setStartDate(startTime);
		setDeltaTimesUnit(DELTATIMES_DEFAULT_UNIT);
		setDeltaTimes(deltaTimes);
		setXAngles(xAngles);
		setXAnglesUnit(XANGLES_DEFAULT_UNIT);
		setXRates(xRates);
		setXRatesUnit(XRATES_DEFAULT_UNIT);
		setYAngles(yAngles);
		setYAnglesUnit(YANGLES_DEFAULT_UNIT);
		setYRates(yRates);
		setYRatesUnit(YRATES_DEFAULT_UNIT);
	}
	/**
	 * If offsetAngles contains the attribute ref=custom a customised path of offset
	 * angles can be specified by providing rotation angles and rates at certain times. Table 7 lists
	 * the corresponding parameters.
	 * For customised offset angles the rotation angles and rates are specified for the offset-x & yaxes
	 * for a certain list of times. The times are specified by a list of delta times relative to a
	 * start-time. The first time is defined by the start-time plus the first delta-time in the list. All
	 * following delta times are defined as delta relative to the previous time in the list. The
	 * element deltaTimes must contain at least two elements. The elements xAngle, yAngle,
	 * xRates and yRates must have the same number of elements as delta-time. The elements
	 * define the offset angles and rates at the respective time.
	 * The rotation angles between two times are defined as 3rd-order polynomial that matches
	 * the angles and rates at the interval borders. The rotation angles before and after the last
	 * point in time result from extrapolation of the polynomial in the first and last interval,
	 * respectively.
	 * @param startTime Start time of the off set rotation
	 * @param deltaTimes List of delta time. All entries must be positive (because the delta times are cumulative). The time unit must be min.
	 * @param xAngles Rotation angles towards the offset-x-axis. The list must be of the same length as deltaTimes. The angles must be expressed in deg.
	 * @param xRates Rotation rate towards the offset-x-axis. The list must be of the same length as deltaTimes. The speed must be expressed in deg/sec.
	 * @param yAngles Rotation angles towards the offset-y-axis. The list must be of the same length as deltaTimes. The angles must be expressed in deg.
	 * @param yRates Rotation rate towards the offset-y-axis. The list must be of the same length as deltaTimes. The speed must be expressed in deg/sec.
	 * @throws ParseException
	 */
	public OffsetCustom (String startTime,String deltaTimes,String xAngles,String xRates,String yAngles,String yRates) throws ParseException{
		super(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM);
		setStartDate(PointingBlock.zuluToDate(startTime));
		setDeltaTimesUnit(DELTATIMES_DEFAULT_UNIT);
		setDeltaTimes(stringToFloatArray(deltaTimes));
		setXAnglesUnit(XANGLES_DEFAULT_UNIT);
		setXAngles(stringToFloatArray(xAngles));
		setXRates(stringToFloatArray(xRates));
		setXRatesUnit(XRATES_DEFAULT_UNIT);
		setYAngles(stringToFloatArray(yAngles));
		setYAnglesUnit(YANGLES_DEFAULT_UNIT);
		setYRates(stringToFloatArray(yRates));
		setYRatesUnit(YRATES_DEFAULT_UNIT);
	}
	
	public float[] getXRates(){
		return stringToFloatArray(getChild(XRATES_FIELD).getValue());
	}
	public float[] getXRates(String unit){
		return Units.convertUnit(getXRates(),getXRatesUnit(),unit);
	}


	public float[] getYRates(){
		return stringToFloatArray(getChild(YRATES_FIELD).getValue());
	}
	public float[] getYRates(String unit){
		return Units.convertUnit(getYRates(),getYRatesUnit(),unit);
	}

	
	public void setXRates(float[] xRates){
		setFloatArrayField(XRATES_FIELD,xRates);

	}
	public void setYRates(float[] xAngles){
		setFloatArrayField(YRATES_FIELD,xAngles);

	}

	public String getXRatesUnit(){
		return getUnit(XRATES_FIELD);
		
	}
	
	public void setXRatesUnit(String unit){
		setUnit(XRATES_FIELD,unit);
		
	}

	public void setYRatesUnit(String unit){
		setUnit(YRATES_FIELD,unit);
		
	}

	public String getYRatesUnit(){
		return getUnit(YRATES_FIELD);
		
	}

	public void setXAngles(float[] xAngles){
		setFloatArrayField(XANGLES_FIELD,xAngles);

	}

	public void setYAngles(float[] yAngles){
		setFloatArrayField(YANGLES_FIELD,yAngles);

	}

	public float[] getXAngles(){
		return stringToFloatArray(getChild(XANGLES_FIELD).getValue());
	}
	
	public float[] getXAngles(String unit){
		return Units.convertUnit(getXAngles(),getXAnglesUnit(),unit);
	}


	public float[] getYAngles(){
		return stringToFloatArray(getChild(YANGLES_FIELD).getValue());
	}
	public float[] getYAngles(String unit){
		return Units.convertUnit(getYAngles(),getYAnglesUnit(),unit);
	}

	public String getXAnglesUnit(){
		return getUnit(XANGLES_FIELD);
		
	}

	public String getYAnglesUnit(){
		return getUnit(YANGLES_FIELD);
		
	}
	
	public void setXAnglesUnit(String unit){
		setUnit(XANGLES_FIELD,unit);
		
	}
	public void setYAnglesUnit(String unit){
		setUnit(YANGLES_FIELD,unit);
		
	}

	public void setDeltaTimes(float[] deltaTimes){
		setFloatArrayField(DELTATIMES_FIELD,deltaTimes);
	}
	public float[] getDeltaTimes(){
		float[] def={0.0f,1.0f};
		PointingElement child = getChild(DELTATIMES_FIELD);
		if (child==null){
			return def;
		}
		else return stringToFloatArray(child.getValue());
	}
	public float[] getDeltaTimes(String unit){
		return Units.convertUnit(getDeltaTimes(),getDeltaTimesUnit(),unit);
	}

	
	
	
	public void setDeltaTimesUnit(String unit){
		setUnit(DELTATIMES_FIELD,unit);

		
	}
	
	public String getDeltaTimesUnit(){
		return getUnit(DELTATIMES_FIELD);
		
	}
	
	
	public void setStartDate(Date date){
		PointingElement startTimeCh = new PointingElement(STARTTIME_FIELD,PointingBlock.dateToZulu(date));
		this.addChild(startTimeCh);

	}
	
	public Date getStartDate(){
		try {
			return PointingBlock.zuluToDate(getChild(STARTTIME_FIELD).getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);

			//e.printStackTrace();
			//return null;
		}
	}
	/**
	 * If offsetAngles contains the attribute ref=custom a customised path of offset
	 * angles can be specified by providing rotation angles and rates at certain times. Table 7 lists
	 * the corresponding parameters.
	 * For customised offset angles the rotation angles and rates are specified for the offset-x & yaxes
	 * for a certain list of times. The times are specified by a list of delta times relative to a
	 * start-time. The first time is defined by the start-time plus the first delta-time in the list. All
	 * following delta times are defined as delta relative to the previous time in the list. The
	 * element deltaTimes must contain at least two elements. The elements xAngle, yAngle,
	 * xRates and yRates must have the same number of elements as delta-time. The elements
	 * define the offset angles and rates at the respective time.
	 * The rotation angles between two times are defined as 3rd-order polynomial that matches
	 * the angles and rates at the interval borders. The rotation angles before and after the last
	 * point in time result from extrapolation of the polynomial in the first and last interval,
	 * respectively.
	 * The deltaTimes are set to 0.0 and 0.1
	 * The xAngles to 0.0 and 0.0
	 * The xRates to 0.0 and 0.0
	 * The yAngles to 0.0 and 0.0
	 * The yRates to 0.0 and 0.0
	 * @param startTime Start time of the off set rotation
	 * @throws ParseException
	 */	
	public  OffsetCustom (String startTime) throws ParseException{
		this(startTime,"0. 1.","0. 0.","0. 0.","0. 0.","0. 0.");
	}
	
	public long getDurationMilliSecs(){
		float[] deltaTimes=getDeltaTimes(DELTATIMES_DEFAULT_UNIT);
		float result=0;
		for (int i=0;i<deltaTimes.length;i++){
			result=result+deltaTimes[i];
		}
		return new Float(result*1000*60).longValue();
	}
	
	public Date getEndDate(){
		float[] deltaTimes=getDeltaTimes(DELTATIMES_DEFAULT_UNIT);
		float result=0;
		for (int i=1;i<deltaTimes.length;i++){
			result=result+deltaTimes[i];
		}
		long duration = new Float(result*1000*60).longValue();

		long result2=getStartDate().getTime()+duration;
		return new Date(result2);
	}
	
	public boolean isCustom(){
		return true;
	}
	
	public OffsetCustom copy() {
		OffsetCustom result = new OffsetCustom();
		
		//result.setValue(getValue());
		PointingElement[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingElement[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		//System.out.println(result.toXml(0));

		return result;
	}
	
	private void normalize(){
		try{
			setDeltaTimes(getDeltaTimes());
			setXAngles(getXAngles());
			setXRates(getXRates());
			setYAngles(getYAngles());
			setYRates(getYRates());
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Can not reasd number array from field: "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		
	}



	
	
}
