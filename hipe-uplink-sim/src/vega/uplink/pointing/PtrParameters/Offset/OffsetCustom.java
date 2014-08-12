package vega.uplink.pointing.PtrParameters.Offset;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public class OffsetCustom extends OffsetAngles {
	
	public OffsetCustom(PointingMetadata org){
		super(org);
	}
	
	private OffsetCustom(){
		super("custom");
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
		super("custom");
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
		super("custom");
		setStartDate(startTime);
		setDeltaTimesUnit("min");
		setDeltaTimes(deltaTimes);
		setXAngles(xAngles);
		setXAnglesUnit("deg");
		setXRates(xRates);
		setXRatesUnit("deg/sec");
		setYAngles(yAngles);
		setYAnglesUnit("deg");
		setYRates(yRates);
		setYRatesUnit("deg/sec");
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
		super("custom");
		setStartDate(PointingBlock.zuluToDate(startTime));
		setDeltaTimesUnit("min");
		setDeltaTimes(stringToFloatArray(deltaTimes));
		setXAnglesUnit("deg");
		setXAngles(stringToFloatArray(xAngles));
		setXRates(stringToFloatArray(xRates));
		setXRatesUnit("deg/sec");
		setYAngles(stringToFloatArray(yAngles));
		setYAnglesUnit("deg");
		setYRates(stringToFloatArray(yRates));
		setYRatesUnit("deg/sec");
	}
	
	public float[] getXRates(){
		return stringToFloatArray(getChild("xRates").getValue());
	}

	public float[] getYRates(){
		return stringToFloatArray(getChild("yRates").getValue());
	}
	
	public void setXRates(float[] xRates){
		setFloatArrayField("xRates",xRates);

	}
	public void setYRates(float[] xAngles){
		setFloatArrayField("yRates",xAngles);

	}

	public String getXRatesUnit(){
		return getUnit("xRates");
		
	}
	
	public void setXRatesUnit(String unit){
		setUnit("xRates",unit);
		
	}

	public void setYRatesUnit(String unit){
		setUnit("yRates",unit);
		
	}

	public String getYRatesUnit(){
		return getUnit("yRates");
		
	}

	public void setXAngles(float[] xAngles){
		setFloatArrayField("xAngles",xAngles);

	}

	public void setYAngles(float[] yAngles){
		setFloatArrayField("yAngles",yAngles);

	}

	public float[] getXAngles(){
		return stringToFloatArray(getChild("xAngles").getValue());
	}

	public float[] getYAngles(){
		return stringToFloatArray(getChild("yAngles").getValue());
	}

	public String getXAnglesUnit(){
		return getUnit("xAngles");
		
	}

	public String getYAnglesUnit(){
		return getUnit("yAngles");
		
	}
	
	public void setXAnglesUnit(String unit){
		setUnit("xAngles",unit);
		
	}
	public void setYAnglesUnit(String unit){
		setUnit("yAngles",unit);
		
	}

	public void setDeltaTimes(float[] deltaTimes){
		setFloatArrayField("deltaTimes",deltaTimes);
	}
	public float[] getDeltaTimes(){
		float[] def={0.0f,1.0f};
		PointingMetadata child = getChild("deltaTimes");
		if (child==null){
			return def;
		}
		else return stringToFloatArray(child.getValue());
	}

	
	
	
	public void setDeltaTimesUnit(String unit){
		setUnit("deltaTimes",unit);

		
	}
	
	public String getDeltaTimesUnit(){
		return getUnit("deltaTimes");
		
	}
	
	
	public void setStartDate(Date date){
		PointingMetadata startTimeCh = new PointingMetadata("startTime",PointingBlock.dateToZulu(date));
		this.addChild(startTimeCh);

	}
	
	public Date getStartDate(){
		try {
			return PointingBlock.zuluToDate(getChild("startTime").getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
		float[] deltaTimes=getDeltaTimes();
		float result=0;
		for (int i=0;i<deltaTimes.length;i++){
			result=result+deltaTimes[i];
		}
		return new Float(result*1000*60).longValue();
	}
	
	public Date getEndDate(){
		long result=getStartDate().getTime()+getDurationMilliSecs();
		return new Date(result);
	}
	
	public boolean isCustom(){
		return true;
	}
	
	public OffsetCustom copy() {
		OffsetCustom result = new OffsetCustom();
		
		//result.setValue(getValue());
		PointingMetadata[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingMetadata[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		//System.out.println(result.toXml(0));

		return result;
	}



	
	
}
