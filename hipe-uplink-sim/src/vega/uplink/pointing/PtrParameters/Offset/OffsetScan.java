package vega.uplink.pointing.PtrParameters.Offset;


import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * @author jarenas
 *
 */
public class OffsetScan extends OffsetAngles {
	/**
	 * startTime
	 */
	public static String STARTTIME_FIELD="startTime";
	/**
	 * numberOfLines
	 */
	public static String NUMBEROFLINES_FIELD="numberOfLines";
	/**
	 * numberOfScansPerLine
	 */
	public static String NUMBEROFSCANSPERLINE_FIELD="numberOfScansPerLine";
	/**
	 * xStart
	 */
	public static String XSTART_FIELD="xStart";
	/**
	 * deg
	 */
	public static String XSTART_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * yStart
	 */
	public static String YSTART_FIELD="yStart";
	/**
	 * deg
	 */
	public static String YSTART_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * scanDelta
	 */
	public static String SCANDELTA_FIELD="scanDelta";
	/**
	 * deg
	 */
	public static String SCANDELTA_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * lineDelta
	 */
	public static String LINEDELTA_FIELD="lineDelta";
	/**
	 * deg
	 */
	public static String LINEDELTA_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * scanTime
	 */
	public static String SCANTIME_FIELD="scanTime";
	/**
	 * min
	 */
	public static String SCANTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * scanSpeed
	 */
	public static String SCANSPEED_FIELD="scanSpeed";
	/**
	 * deg/min
	 */
	public static String SCANSPEED_DEFAULT_UNIT=Units.DEGREES_PER_MINUTE;
	/**
	 * scanSlewTime
	 */
	public static String SCANSLEWTIME_FIELD="scanSlewTime";
	/**
	 * min
	 */
	public static String SCANSLEWTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * lineSlewTime
	 */
	public static String LINESLEWTIME_FIELD="lineSlewTime";
	/**
	 * min
	 */
	public static String LINESLEWTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * borderSlewTime
	 */
	public static String BORDERSLEWTIME_FIELD="borderSlewTime";
	/**
	 * min
	 */
	public static String BORDERSLEWTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * lineAxis
	 */
	public static String LINEAXIS_FIELD="lineAxis";
	/**
	 * keepLineDir
	 */
	public static String KEEPLINEDIR_FIELD="keepLineDir";
	/**
	 * keepScanDir
	 */
	public static String KEEPSCANDIR_FIELD="keepScanDir";
	
	public OffsetScan(PointingElement org){
		super(org);
	}
	private OffsetScan(){
		super(OffsetAngles.OFFSETANGLES_TYPE_SCAN);
	}
	/**
	 * A scan is defined if the element offsetAngles contains the attribute ref=scan.
	 * Before the scan start-time and after the last scan a slew is performed with duration
	 * borderSlewTime. Before the initial slew and after the final slew the offset angles are fixed
	 * to the angles at start of the first and the end of the last scan respectively.
	 * @param startTime Scan start time 
	 * @param numberOfLines Number of lines along which a scan is performed
	 * @param numberOfScansPerLine Number of scans that are performed per line
	 * @param xStartUnit Unit used to express xStart. For example deg.
	 * @param xStart rotation angle of start point of first line towards offset-x-axis
	 * @param yStartUnit Unit used to express yStart. For example deg.
	 * @param yStart rotation angle of start point of first line towards offset-y-axis
	 * @param scanDeltaUnit Unit used to express scanDelta. For example deg.
	 * @param scanDelta Delta angle of one scan
	 * @param lineDeltaUnit Unit used to express lineDelta. For example deg.
	 * @param lineDelta Angular offset between two lines of the scan.
	 * @param scanTimeUnit Unit used to express scanTime. For example min.
	 * @param scanTime Duration of one scan. If this parameter is not null the parameter scanSpeed must be null.
	 * @param scanSpeedUnit Unit used to express scanSpeed. For example deg/min.
	 * @param scanSpeed Angular Speed of a scan.If this parameter is not null the parameter scanTime must be null.
	 * @param scanSlewTimeUnit Unit used to express scanSlewTime. For example min.
	 * @param scanSlewTime Slew time between two scans in the same line.
	 * @param lineSlewTimeUnit Unit used to express lineSlewTime. For example min.
	 * @param lineSlewTime Slew time between two scans in different lines.
	 * @param borderSlewTimeUnit Unit used to express borderSlewTime. For example min.
	 * @param borderSlewTime Slew time before first and after last scan to reach start angles of fisrts scan and final angles of last scan
	 * @param lineAxis Name of offset-axis along which the scans are performed
	 * @param keepLineDir Flag indicating whether the direction of the first scan line is kept for other lines (=true) or alternated (=false)
	 * @param keepScanDir Flag indicating whether the direction of the scan performed in one line is kept (=true) or alternated (=false)
	 */
	public OffsetScan(Date startTime,int numberOfLines,int numberOfScansPerLine,String xStartUnit,float xStart,String yStartUnit,float yStart,String scanDeltaUnit,float scanDelta,String lineDeltaUnit,float lineDelta,String scanTimeUnit,Float scanTime,String scanSpeedUnit,Float scanSpeed,String scanSlewTimeUnit,float scanSlewTime,String lineSlewTimeUnit,float lineSlewTime,String borderSlewTimeUnit,float borderSlewTime,String lineAxis,boolean keepLineDir,boolean keepScanDir){
		super(OffsetAngles.OFFSETANGLES_TYPE_SCAN);
		setStartDate(startTime);
		setIntegerField(NUMBEROFLINES_FIELD,numberOfLines);
		setIntegerField(NUMBEROFSCANSPERLINE_FIELD,numberOfScansPerLine);
		setFloatField(XSTART_FIELD,xStart);
		setUnit(XSTART_FIELD,xStartUnit);
		setFloatField(YSTART_FIELD,yStart);
		setUnit(YSTART_FIELD,yStartUnit);
		setFloatField(SCANDELTA_FIELD,scanDelta);
		setUnit(SCANDELTA_FIELD,scanDeltaUnit);
		setFloatField(LINEDELTA_FIELD,lineDelta);
		setUnit(LINEDELTA_FIELD,lineDeltaUnit);
		if (scanTime!=null){
			setFloatField(SCANTIME_FIELD,scanTime);
			setUnit(SCANTIME_FIELD,scanTimeUnit);
		}else{
			setFloatField(SCANSPEED_FIELD,scanSpeed);
			setUnit(SCANSPEED_FIELD,scanSpeedUnit);

		}
		setFloatField(SCANSLEWTIME_FIELD,scanSlewTime);
		setUnit(SCANSLEWTIME_FIELD,scanSlewTimeUnit);
		setFloatField(LINESLEWTIME_FIELD,lineSlewTime);
		setUnit(LINESLEWTIME_FIELD,lineSlewTimeUnit);
		setFloatField(BORDERSLEWTIME_FIELD,lineSlewTime);
		setUnit(BORDERSLEWTIME_FIELD,borderSlewTimeUnit);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,keepLineDir);
		setBooleanField(KEEPSCANDIR_FIELD,keepScanDir);		
	}
	/**
	 * A scan is defined if the element offsetAngles contains the attribute ref=scan.
	 * Before the scan start-time and after the last scan a slew is performed with duration
	 * borderSlewTime. Before the initial slew and after the final slew the offset angles are fixed
	 * to the angles at start of the first and the end of the last scan respectively.
	 * Times are expressed in min, angles in deg and angular speed in deg/min.
	 * @param startTime Scan start time 
	 * @param numberOfLines Number of lines along which a scan is performed
	 * @param numberOfScansPerLine Number of scans that are performed per line
	 * @param xStart rotation angle of start point of first line towards offset-x-axis
	 * @param yStart rotation angle of start point of first line towards offset-y-axis
	 * @param scanDelta Delta angle of one scan
	 * @param lineDelta Angular offset between two lines of the scan.
	 * @param scanTime Duration of one scan. If this parameter is not null the parameter scanSpeed must be null.
	 * @param scanSpeed Angular Speed of a scan.If this parameter is not null the parameter scanTime must be null.
	 * @param scanSlewTime Slew time between two scans in the same line.
	 * @param lineSlewTime Slew time between two scans in different lines.
	 * @param borderSlewTime Slew time before first and after last scan to reach start angles of fisrts scan and final angles of last scan
	 * @param lineAxis Name of offset-axis along which the scans are performed
	 * @param keepLineDir Flag indicating whether the direction of the first scan line is kept for other lines (=true) or alternated (=false)
	 * @param keepScanDir Flag indicating whether the direction of the scan performed in one line is kept (=true) or alternated (=false)
	 */
	public OffsetScan(Date startTime,int numberOfLines,int numberOfScansPerLine,float xStart,float yStart,float scanDelta,float lineDelta,Float scanTime,Float scanSpeed,float scanSlewTime,float lineSlewTime,float borderSlewTime,String lineAxis,boolean keepLineDir,boolean keepScanDir){
		super(OffsetAngles.OFFSETANGLES_TYPE_SCAN);
		setStartDate(startTime);
		setIntegerField(NUMBEROFLINES_FIELD,numberOfLines);
		setIntegerField(NUMBEROFSCANSPERLINE_FIELD,numberOfScansPerLine);
		setFloatField(XSTART_FIELD,xStart);
		setUnit(XSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(YSTART_FIELD,yStart);
		setUnit(YSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(SCANDELTA_FIELD,scanDelta);
		setUnit(SCANDELTA_FIELD,SCANDELTA_DEFAULT_UNIT);
		setFloatField(LINEDELTA_FIELD,lineDelta);
		setUnit(LINEDELTA_FIELD,LINEDELTA_DEFAULT_UNIT);
		if (scanTime!=null){
			setFloatField(SCANTIME_FIELD,scanTime);
			setUnit(SCANTIME_FIELD,SCANTIME_DEFAULT_UNIT);
		}else{
			setFloatField(SCANSPEED_FIELD,scanSpeed);
			setUnit(SCANSPEED_FIELD,SCANSPEED_DEFAULT_UNIT);

		}
		setFloatField(SCANSLEWTIME_FIELD,scanSlewTime);
		setUnit(SCANSLEWTIME_FIELD,SCANSLEWTIME_DEFAULT_UNIT);
		setFloatField(LINESLEWTIME_FIELD,lineSlewTime);
		setUnit(LINESLEWTIME_FIELD,LINESLEWTIME_DEFAULT_UNIT);
		setFloatField(BORDERSLEWTIME_FIELD,borderSlewTime);
		setUnit(BORDERSLEWTIME_FIELD,BORDERSLEWTIME_DEFAULT_UNIT);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,keepLineDir);
		setBooleanField(KEEPSCANDIR_FIELD,keepScanDir);		
	}
	/**
	 * A scan is defined if the element offsetAngles contains the attribute ref=scan.
	 * Before the scan start-time and after the last scan a slew is performed with duration
	 * borderSlewTime. Before the initial slew and after the final slew the offset angles are fixed
	 * to the angles at start of the first and the end of the last scan respectively.
	 * Times are expressed in min, angles in deg and angular speed in deg/min.
	 * @param startTime Scan start time 
	 * @param numberOfLines Number of lines along which a scan is performed
	 * @param numberOfScansPerLine Number of scans that are performed per line
	 * @param xStart rotation angle of start point of first line towards offset-x-axis
	 * @param yStart rotation angle of start point of first line towards offset-y-axis
	 * @param scanDelta Delta angle of one scan
	 * @param lineDelta Angular offset between two lines of the scan.
	 * @param scanTime Duration of one scan. If this parameter is not null the parameter scanSpeed must be null.
	 * @param scanSpeed Angular Speed of a scan.If this parameter is not null the parameter scanTime must be null.
	 * @param scanSlewTime Slew time between two scans in the same line.
	 * @param lineSlewTime Slew time between two scans in different lines.
	 * @param borderSlewTime Slew time before first and after last scan to reach start angles of fisrts scan and final angles of last scan
	 * @param lineAxis Name of offset-axis along which the scans are performed
	 * @param keepLineDir Flag indicating whether the direction of the first scan line is kept for other lines (=true) or alternated (=false)
	 * @param keepScanDir Flag indicating whether the direction of the scan performed in one line is kept (=true) or alternated (=false)
	 */
	public OffsetScan(String startTime,String numberOfLines,String numberOfScansPerLine,String xStart,String yStart,String scanDelta,String lineDelta,String scanTime,String scanSpeed,String scanSlewTime,String lineSlewTime,String borderSlewTime,String lineAxis,String keepLineDir,String keepScanDir) throws ParseException{
		super(OffsetAngles.OFFSETANGLES_TYPE_SCAN);
		setStartDate(DateUtil.zuluToDate(startTime));
		setIntegerField(NUMBEROFLINES_FIELD,Integer.parseInt(numberOfLines));
		setIntegerField(NUMBEROFSCANSPERLINE_FIELD,Integer.parseInt(numberOfScansPerLine));
		setFloatField(XSTART_FIELD,Float.parseFloat(xStart));
		setUnit(XSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(YSTART_FIELD,Float.parseFloat(yStart));
		setUnit(YSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(SCANDELTA_FIELD,Float.parseFloat(scanDelta));
		setUnit(SCANDELTA_FIELD,SCANDELTA_DEFAULT_UNIT);
		setFloatField(LINEDELTA_FIELD,Float.parseFloat(lineDelta));
		setUnit(LINEDELTA_FIELD,LINEDELTA_DEFAULT_UNIT);
		if (scanTime!=null){
			setFloatField(SCANTIME_FIELD,Float.parseFloat(scanTime));
			setUnit(SCANTIME_FIELD,SCANTIME_DEFAULT_UNIT);
		}else{
			setFloatField(SCANSPEED_FIELD,Float.parseFloat(scanSpeed));
			setUnit(SCANSPEED_FIELD,SCANSPEED_DEFAULT_UNIT);

		}
		setFloatField(SCANSLEWTIME_FIELD,Float.parseFloat(scanSlewTime));
		setUnit(SCANSLEWTIME_FIELD,SCANSLEWTIME_DEFAULT_UNIT);
		setFloatField(LINESLEWTIME_FIELD,Float.parseFloat(lineSlewTime));
		setUnit(LINESLEWTIME_FIELD,LINESLEWTIME_DEFAULT_UNIT);
		setFloatField(BORDERSLEWTIME_FIELD,Float.parseFloat(borderSlewTime));
		setUnit(BORDERSLEWTIME_FIELD,BORDERSLEWTIME_DEFAULT_UNIT);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,Boolean.parseBoolean(keepLineDir));
		setBooleanField(KEEPSCANDIR_FIELD,Boolean.parseBoolean(keepScanDir));		

		
	}
	
	public int getNumberOfLines(){
		return Integer.parseInt(getChild(NUMBEROFLINES_FIELD).getValue().replaceAll(" ", ""));
	}
	
	public int getNumberOfScansPerLine(){
		return Integer.parseInt(getChild(NUMBEROFSCANSPERLINE_FIELD).getValue().replaceAll(" ", ""));
	}
	
	public float getScanDelta(){
		return Float.parseFloat(getChild(SCANDELTA_FIELD).getValue());
	}
	public String getScanDeltaUnit(){
		String result = getUnit(SCANDELTA_FIELD);
		if (result==null) return SCANDELTA_DEFAULT_UNIT;
		else return result;
	}
	public float getScanDelta(String unit){
		return Units.convertUnit(getScanDelta(),getScanDeltaUnit(),unit);
	}

	public float getBorderSlewTime(){
		return Float.parseFloat(getChild(BORDERSLEWTIME_FIELD).getValue());
	}
	public String getBorderSlewTimeUnit(){
		String result = getUnit(BORDERSLEWTIME_FIELD);
		if (result==null) return BORDERSLEWTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(BORDERSLEWTIME_FIELD);
	}
	public float getBorderSlewTime(String unit){
		return Units.convertUnit(getBorderSlewTime(),getBorderSlewTimeUnit(),unit);
	}

	public float getScanSpeed(){
		return Float.parseFloat(getChild(SCANSPEED_FIELD).getValue());
	}
	public String getScanSpeedUnit(){
		String result = getUnit(SCANSPEED_FIELD);
		if (result==null) return SCANSPEED_DEFAULT_UNIT;
		else return result;

		//return getUnit(SCANSPEED_FIELD);
	}
	public float getScanSpeed(String unit){
		return Units.convertUnit(getScanSpeed(),getScanSpeedUnit(),unit);
	}

	public float getScanTime(){
		return Float.parseFloat(getChild(SCANTIME_FIELD).getValue());
	}
	public String getScanTimeUnit(){
		String result = getUnit(SCANTIME_FIELD);
		if (result==null) return SCANTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(SCANTIME_FIELD);
	}
	public float getScanTime(String unit){
		return Units.convertUnit(getScanTime(),getScanTimeUnit(),unit);
	}
	
	public float getScanSlewTime(){
		if (getChild(SCANSLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(SCANSLEWTIME_FIELD).getValue());
		}else{
			return 1.0f;
		}
	}
	public String getScanSlewTimeUnit(){
		String result = getUnit(SCANSLEWTIME_FIELD);
		if (result==null) return SCANSLEWTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(SCANSLEWTIME_FIELD);
	}
	public float getScanSlewTime(String unit){
		return Units.convertUnit(getScanSlewTime(),getScanSlewTimeUnit(),unit);
	}
	public float getLineSlewTime(){
		if (getChild(LINESLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(LINESLEWTIME_FIELD).getValue());
		}else{
			return 1.0f;
		}
	}
	public String getLineSlewTimeUnit(){
		String result = getUnit(LINESLEWTIME_FIELD);
		if (result==null) return LINESLEWTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(LINESLEWTIME_FIELD);
	}
	public float getLineSlewTime(String unit){
		return Units.convertUnit(getLineSlewTime(),getLineSlewTimeUnit(),unit);
	}

	public long getDurationMilliSecs(){
		
		float scanTime=0.0f;
		if (getChild(SCANSPEED_FIELD)!=null){
			scanTime = Math.abs(getScanDelta(SCANDELTA_DEFAULT_UNIT)/getScanSpeed(SCANSPEED_DEFAULT_UNIT));
		}
		else{
			scanTime = getScanTime(SCANTIME_DEFAULT_UNIT);
		}
		float lineTime = (getNumberOfScansPerLine() * scanTime) + ((getNumberOfScansPerLine() - 1) * getScanSlewTime(SCANSLEWTIME_DEFAULT_UNIT));

		return new Float((2 * getBorderSlewTime(BORDERSLEWTIME_DEFAULT_UNIT)) + (getNumberOfLines() *lineTime)+ (getNumberOfLines() - 1) * getLineSlewTime(LINESLEWTIME_DEFAULT_UNIT)).longValue()*60*1000;

	}
	

	/**
	 * Scan crated with
	 * numberOfLines set to
	 * numberOfScansPerLine set to 1
	 * xStart set to 0.0 deg
	 * yStart set to 0.0 deg
	 * scanDelta set to 0.0 deg
	 * lineDelta set to 0.0 deg
	 * scanTime set to 1 min.
	 * scanSpeed set to null
	 * scanSlewTime set to 1 min.
	 * lineSlewTime set to 1.0 min.
	 * borderSlewTime set to 1.0 min.
	 * lineAxis set to y
	 * keepLIneDir set to false
	 * keepScanDir set to false
	 * @param startTime
	 * @throws ParseException
	 */
	public OffsetScan(String startTime) throws ParseException{
		this(startTime,"1","1","0.","0.","0.","0.","1.",null,"1.","1.","1.","y","false","false");
	}
	
	public void setStartDate(Date date){
		PointingElement startTimeCh = new PointingElement(STARTTIME_FIELD,DateUtil.dateToZulu(date));
		this.addChild(startTimeCh);

	}
	
	public Date getStartDate(){
		try {
			return DateUtil.zuluToDate(getChild(STARTTIME_FIELD).getValue());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Date getEndDate(){
		
		float scanTime=0.0f;
		if (getChild(SCANSPEED_FIELD)!=null){
			scanTime = Math.abs(getScanDelta(SCANDELTA_DEFAULT_UNIT)/getScanSpeed(SCANSPEED_DEFAULT_UNIT));
		}
		else{
			scanTime = getScanTime(SCANTIME_DEFAULT_UNIT);
		}
		float lineTime = (getNumberOfScansPerLine() * scanTime) + ((getNumberOfScansPerLine() - 1) * getScanSlewTime(SCANSLEWTIME_DEFAULT_UNIT));

		long et = new Float((1 * getBorderSlewTime(BORDERSLEWTIME_DEFAULT_UNIT)) + (getNumberOfLines() *lineTime)+ (getNumberOfLines() - 1) * getLineSlewTime(LINESLEWTIME_DEFAULT_UNIT)).longValue()*60*1000;

		long result=getStartDate().getTime()+et;
		return new Date(result);
	}

	public boolean isScan(){
		return true;
	}
	
	public OffsetScan copy() {
		OffsetScan result = new OffsetScan();
		
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


	
	/*public int getScanDuration(){
		scanSpeed=
	}*/

}
