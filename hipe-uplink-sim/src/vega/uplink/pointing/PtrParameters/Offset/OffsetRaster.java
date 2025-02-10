package vega.uplink.pointing.PtrParameters.Offset;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * A raster is defined if the element offsetAngles contains the attribute ref=raster.
 * The rotation angles before the raster start-time and after the dwell-time of the last rasterpoint correspond to the angles of the first and last raster point respectively.
 * @author jarenas
 *
 */
public class OffsetRaster extends OffsetAngles {
	/**
	 * startTime
	 */
	public static String STARTTIME_FIELD="startTime";
	/**
	 * xPoints
	 */
	public static String XPOINTS_FIELD="xPoints";
	/**
	 * yPoints
	 */
	public static String YPOINTS_FIELD="yPoints";
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
	 * xDelta
	 */
	public static String XDELTA_FIELD="xDelta";
	/**
	 * deg
	 */
	public static String XDELTA_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * yDelta
	 */
	public static String YDELTA_FIELD="yDelta";
	/**
	 * deg
	 */
	public static String YDELTA_DEFAULT_UNIT=Units.DEGREE;
	/**
	 * pointSlewTime
	 */
	public static String POINTSLEWTIME_FIELD="pointSlewTime";
	/**
	 * min
	 */
	public static String POINTSLEWTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * lineSlewTime
	 */
	public static String LINESLEWTIME_FIELD="lineSlewTime";
	/**
	 * min
	 */
	public static String LINESLEWTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * dwelTime
	 */
	public static String DWELLTIME_FIELD="dwellTime";
	/**
	 * min
	 */
	public static String DWELLTIME_DEFAULT_UNIT=Units.MINUTES;
	/**
	 * lineAxis
	 */
	public static String LINEAXIS_FIELD="lineAxis";
	/**
	 * keepLineDir
	 */
	public static String KEEPLINEDIR_FIELD="keepLineDir";
	
	public OffsetRaster(PointingElement org){
		super(org);
	}
	
	private OffsetRaster(){
		super(OffsetAngles.OFFSETANGLES_TYPE_RASTER);
	}
	/**
	 * A raster is defined if the element offsetAngles contains the attribute ref=raster.
	 * The rotation angles before the raster start-time and after the dwell-time of the last raster point correspond to the angles of the first and last raster point respectively.
	 * @param startTime Raster start time
	 * @param xPoints Number of Points in offset-x-direction
	 * @param yPoints Number of Points in offset-y-direction
	 * @param xStartUnit Unit for xStart. For example deg.
	 * @param xStart rotation angle of first raster-point towards the offset x-axis
	 * @param yStartUnit Unit for yStart. For example deg.
	 * @param yStart rotation angle of first raster-point towards the offset-y-axis
	 * @param xDeltaUnit Unit for xDelta. For example deg.
	 * @param xDelta Delta angle towards offset x axis between two raster points
	 * @param yDeltaUnit Unit for yDelta. For example deg.
	 * @param yDelta Delta angle towards offset-y-axis between two raster points
	 * @param pointSlewTimeUnit Units used for pointSlewTime. For example min.
	 * @param pointSlewTime Slew time between two raster-points in the same line.
	 * @param lineSlewTimeUnit Units used for lineSlewTime. For example min.
	 * @param lineSlewTime Slew time between two raster-points in different lines.
	 * @param dwellTimeUnit Units used for dwellTime. For example min.
	 * @param dwellTime Time spent at one raster point
	 * @param lineAxis Name of offset-axis along which the raster-points are connected in a line.
	 * @param keepLineDir Flag indicating whether the direction of the first raster-row is kept (=true) or alternated (=false)
	 */
	public OffsetRaster(Date startTime,int xPoints,int yPoints,String xStartUnit,float xStart,String yStartUnit,float yStart,String xDeltaUnit,float xDelta,String yDeltaUnit,float yDelta,String pointSlewTimeUnit,float pointSlewTime,String lineSlewTimeUnit,float lineSlewTime,String dwellTimeUnit,float dwellTime,String lineAxis,boolean keepLineDir) {
		super(OffsetAngles.OFFSETANGLES_TYPE_RASTER);
		setStartDate(startTime);
		setIntegerField(XPOINTS_FIELD,xPoints);
		setIntegerField(YPOINTS_FIELD,yPoints);
		setFloatField(XSTART_FIELD,xStart);
		setUnit(XSTART_FIELD,xStartUnit);
		setFloatField(YSTART_FIELD,yStart);
		setUnit(YSTART_FIELD,yStartUnit);
		setFloatField(XDELTA_FIELD,xDelta);
		setUnit(XDELTA_FIELD,xDeltaUnit);
		setFloatField(YDELTA_FIELD,yDelta);
		setUnit(YDELTA_FIELD,yDeltaUnit);
		setFloatField(POINTSLEWTIME_FIELD,pointSlewTime);
		setUnit(POINTSLEWTIME_FIELD,pointSlewTimeUnit);
		setFloatField(LINESLEWTIME_FIELD,lineSlewTime);
		setUnit(LINESLEWTIME_FIELD,lineSlewTimeUnit);
		setFloatField(DWELLTIME_FIELD,dwellTime);
		setUnit(DWELLTIME_FIELD,dwellTimeUnit);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,keepLineDir);
		
	}
	/**
	 * A raster is defined if the element offsetAngles contains the attribute ref=raster.
	 * Times are expressed in min and angles in deg.
	 * The rotation angles before the raster start-time and after the dwell-time of the last raster point correspond to the angles of the first and last raster point respectively.
	 * @param startTime Raster start time
	 * @param xPoints Number of Points in offset-x-direction
	 * @param yPoints Number of Points in offset-y-direction
	 * @param xStart rotation angle of first raster-point towards the offset x-axis
	 * @param yStart rotation angle of first raster-point towards the offset-y-axis
	 * @param xDelta Delta angle towards offset x axis between two raster points
	 * @param yDelta Delta angle towards offset-y-axis between two raster points
	 * @param pointSlewTime Slew time between two raster-points in the same line.
	 * @param lineSlewTime Slew time between two raster-points in different lines.
	 * @param dwellTime Time spent at one raster point
	 * @param lineAxis Name of offset-axis along which the raster-points are connected in a line.
	 * @param keepLineDir Flag indicating whether the direction of the first raster-row is kept (=true) or alternated (=false)
	 */
	public OffsetRaster(Date startTime,int xPoints,int yPoints,float xStart,float yStart,float xDelta,float yDelta,float pointSlewTime,float lineSlewTime,float dwellTime,String lineAxis,boolean keepLineDir) {
		super(OffsetAngles.OFFSETANGLES_TYPE_RASTER);
		setStartDate(startTime);
		setIntegerField(XPOINTS_FIELD,xPoints);
		setIntegerField(YPOINTS_FIELD,yPoints);
		setFloatField(XSTART_FIELD,xStart);
		setUnit(XSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(YSTART_FIELD,yStart);
		setUnit(YSTART_FIELD,YSTART_DEFAULT_UNIT);
		setFloatField(XDELTA_FIELD,xDelta);
		setUnit(XDELTA_FIELD,XDELTA_DEFAULT_UNIT);
		setFloatField(YDELTA_FIELD,yDelta);
		setUnit(YDELTA_FIELD,YDELTA_DEFAULT_UNIT);
		setFloatField(POINTSLEWTIME_FIELD,pointSlewTime);
		setUnit(POINTSLEWTIME_FIELD,POINTSLEWTIME_DEFAULT_UNIT);
		setFloatField(LINESLEWTIME_FIELD,lineSlewTime);
		setUnit(LINESLEWTIME_FIELD,LINESLEWTIME_DEFAULT_UNIT);
		setFloatField(DWELLTIME_FIELD,dwellTime);
		setUnit(DWELLTIME_FIELD,DWELLTIME_DEFAULT_UNIT);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,keepLineDir);

	}
	/**
	 * A raster is defined if the element offsetAngles contains the attribute ref=raster.
	 * Times are expressed in min and angles in deg.
	 * The rotation angles before the raster start-time and after the dwell-time of the last raster point correspond to the angles of the first and last raster point respectively.
	 * @param startTime Raster start time
	 * @param xPoints Number of Points in offset-x-direction
	 * @param yPoints Number of Points in offset-y-direction
	 * @param xStart rotation angle of first raster-point towards the offset x-axis
	 * @param yStart rotation angle of first raster-point towards the offset-y-axis
	 * @param xDelta Delta angle towards offset x axis between two raster points
	 * @param yDelta Delta angle towards offset-y-axis between two raster points
	 * @param pointSlewTime Slew time between two raster-points in the same line.
	 * @param lineSlewTime Slew time between two raster-points in different lines.
	 * @param dwellTime Time spent at one raster point
	 * @param lineAxis Name of offset-axis along which the raster-points are connected in a line.
	 * @param keepLineDir Flag indicating whether the direction of the first raster-row is kept (=true) or alternated (=false)
	 */
	public OffsetRaster(String startTime,String xPoints,String yPoints,String xStart,String yStart,String xDelta,String yDelta,String pointSlewTime,String lineSlewTime,String dwellTime,String lineAxis,String keepLineDir) throws ParseException{
		super(OffsetAngles.OFFSETANGLES_TYPE_RASTER);
		
		setStartDate(DateUtil.zuluToDate(startTime));
		setIntegerField(XPOINTS_FIELD,Integer.parseInt(xPoints));
		setIntegerField(YPOINTS_FIELD,Integer.parseInt(yPoints));
		setFloatField(XSTART_FIELD,Float.parseFloat(xStart));
		setUnit(XSTART_FIELD,XSTART_DEFAULT_UNIT);
		setFloatField(YSTART_FIELD,Float.parseFloat(yStart));
		setUnit(YSTART_FIELD,YSTART_DEFAULT_UNIT);
		setFloatField(XDELTA_FIELD,Float.parseFloat(xDelta));
		setUnit(XDELTA_FIELD,XDELTA_DEFAULT_UNIT);
		setFloatField(YDELTA_FIELD,Float.parseFloat(yDelta));
		setUnit(YDELTA_FIELD,YDELTA_DEFAULT_UNIT);
		setFloatField(POINTSLEWTIME_FIELD,Float.parseFloat(pointSlewTime));
		setUnit(POINTSLEWTIME_FIELD,POINTSLEWTIME_DEFAULT_UNIT);
		setFloatField(LINESLEWTIME_FIELD,Float.parseFloat(lineSlewTime));
		setUnit(LINESLEWTIME_FIELD,LINESLEWTIME_DEFAULT_UNIT);
		setFloatField(DWELLTIME_FIELD,Float.parseFloat(dwellTime));
		setUnit(DWELLTIME_FIELD,DWELLTIME_DEFAULT_UNIT);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,Boolean.parseBoolean(keepLineDir));

	}
	

	
	/**
	 * Get slew time between two raster-points in the same line.
	 * @return
	 */
	public float getPointSlewTime(){
		if (getChild(LINESLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(LINESLEWTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	public String getPointSlewTimeUnit(){
		String result = getUnit(LINESLEWTIME_FIELD);
		if (result==null) return LINESLEWTIME_DEFAULT_UNIT;
		else return result;
		//return getUnit(LINESLEWTIME_FIELD);
	}
	public float getPointSlewTime(String unit){
		return Units.convertUnit(getPointSlewTime(),getPointSlewTimeUnit(),unit);
	}
	
	/**
	 * Get time spent at one raster point
	 * @return
	 */
	public float getDwellTime(){
		if (getChild(DWELLTIME_FIELD)!=null){
			return Float.parseFloat(getChild(DWELLTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	public void setDwellTime(String unit,float value){
		setFloatField(DWELLTIME_FIELD,value);
		setUnit(DWELLTIME_FIELD,unit);

	}
	public String getDwellTimeUnit(){
		String result = getUnit(DWELLTIME_FIELD);
		if (result==null) return DWELLTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(DWELLTIME_FIELD);
	}
	public float getDwellTime(String unit){
		return Units.convertUnit(getDwellTime(),getDwellTimeUnit(),unit);
	}
	/**
	 * Get Name of offset-axis along which the raster-points are connected in a line.
	 * @return
	 */
	public String getLineAxis(){
		try{
			return getChild(LINEAXIS_FIELD).getValue();
		}catch (java.lang.NullPointerException npe){
			return "y";
		}
	}
	/**
	 * Set Name of offset-axis along which the raster-points are connected in a line.
	 * @param lineAxis The new lineAxis
	 */
	public void setLineAxis(String lineAxis){
		setStringField(LINEAXIS_FIELD,lineAxis);
	
	}
	/**
	 * Get the rotation angle of first raster-point towards the offset x-axis
	 * @return
	 */
	public float getXStart(){
			try{
				return Float.parseFloat(getChild(XSTART_FIELD).getValue().trim());
			}catch (NullPointerException npe){
				return 0f;
			}
		
	}
	
	/**
	 * Get the unit in which the rotation angle of first raster-point towards the offset x-axis is expressed
	 * @return
	 */
	public String getXStartUnit(){
		String result = getUnit(XSTART_FIELD);
		if (result==null) return XSTART_DEFAULT_UNIT;
		else return result;

		//return getUnit(XSTART_FIELD);
	}
	
	/**
	 * Get the rotation angle of first raster-point towards the offset x-axis expressed in a specific unit
	 * @param unit the unit in which the value will be expressed
	 * @return
	 */
	public float getXStart(String unit){
		return Units.convertUnit(getXStart(),getXStartUnit(),unit);
		
	}
	/**
	 * Set the rotation angle of first raster-point towards the offset x-axis expressed in a specific unit
	 * @param unit the unit in which the value is expressed
	 * @param value
	 */
	public void setXStart(String unit,float value){
		setFloatField(XSTART_FIELD,value);
		setUnit(XSTART_FIELD,unit);
	
	}
	/**
	 * Get the rotation angle of first raster-point towards the offset y-axis
	 * @return
	 */
	public float getYStart(){
		try{
			return Float.parseFloat(getChild(YSTART_FIELD).getValue().trim());
		}catch (NullPointerException npe){
			return 0f;
		}

			//return Float.parseFloat(getChild(YSTART_FIELD).getValue().trim());
		
	}
	
	/**
	 * Get the unit in which the rotation angle of first raster-point towards the offset y-axis is expressed
	 * @return
	 */
	public String getYStartUnit(){
		String result = getUnit(YSTART_FIELD);
		if (result==null) return YSTART_DEFAULT_UNIT;
		else return result;

		//return getUnit(YSTART_FIELD);
	}
	
	/**
	 * Get the rotation angle of first raster-point towards the offset y-axis expressed in a specific unit
	 * @param unit the unit in which the value will be expressed
	 * @return
	 */
	public float getYStart(String unit){
		return Units.convertUnit(getYStart(),getYStartUnit(),unit);
		
	}
	/**
	 * Set the rotation angle of first raster-point towards the offset y-axis expressed in a specific unit
	 * @param unit the unit in which the value is expressed
	 * @param value
	 */
	public void setYStart(String unit,float value){
		setFloatField(YSTART_FIELD,value);
		setUnit(YSTART_FIELD,unit);
	
	}
	
	/**
	 * Get slew time between two raster-points in different lines.
	 * @return
	 */
	public float getLineSlewTime(){
		if (getChild(POINTSLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(POINTSLEWTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	public String getLineSlewTimeUnit(){
		String result = getUnit(POINTSLEWTIME_FIELD);
		if (result==null) return POINTSLEWTIME_DEFAULT_UNIT;
		else return result;

		//return getUnit(POINTSLEWTIME_FIELD);
	}
	public float getLineSlewTime(String unit){
		return Units.convertUnit(getLineSlewTime(),getLineSlewTimeUnit(),unit);
	}
	/**
	 * Set slew time between two raster-points in different lines.
	 * @param unit The unit in which the new value is expressed
	 * @param value The new value for the slew time
	 */
	public void setLineSlewTime(String unit,float value){
		setFloatField(LINESLEWTIME_FIELD,value);
		setUnit(LINESLEWTIME_FIELD,unit);
	
	}
	
	
	public long getDurationMilliSecs(){
		if (getLineAxis().equals("x") || getLineAxis().equals("X")){
			
			return new Float(getXPoints() * (getYPoints() * getDwellTime(DWELLTIME_DEFAULT_UNIT) + (getYPoints() - 1) * getPointSlewTime(POINTSLEWTIME_DEFAULT_UNIT)) + (getXPoints() - 1) * getLineSlewTime(LINESLEWTIME_DEFAULT_UNIT)).longValue()*60*1000;
		}
		if (getLineAxis().equals("y") || getLineAxis().equals("Y")){
			
			return new Float(getYPoints() * (getXPoints() * getDwellTime(DWELLTIME_DEFAULT_UNIT) + (getXPoints() - 1) * getPointSlewTime(POINTSLEWTIME_DEFAULT_UNIT)) + (getYPoints() - 1) * getLineSlewTime(LINESLEWTIME_DEFAULT_UNIT)).longValue()*60*1000;
		}
		

		return 0;
	}

	/**
	 * Creates a raster with 
	 * xPoints set to 1
	 * yPoints set to 1
	 * xStart set to 0.0 deg.
	 * yStart set to 0.0 deg.
	 * xDelta set to 0.0 deg.
	 * yDelta set to 0.0 deg.
	 * pointSlewTime set to 1 min. 
	 * lineSlewTime set to 1 min.
	 * dwellTime set to 1 min.
	 * lineAxis set to y
	 * keepLineDir is set to false
	 * @param startTime start Time of Raster
	 * @throws ParseException
	 */
	public OffsetRaster(String startTime) throws ParseException{
		this(startTime,"1","1","0.","0.","0.","0.","1.","1.","1.","y","false");
	}
	
	public void setStartDate(Date date){
		PointingElement startTimeCh = new PointingElement("startTime",DateUtil.dateToZulu(date));
		this.addChild(startTimeCh);

	}
	
	public Date getStartDate(){
		try {
			return DateUtil.zuluToDate(getChild("startTime").getValue().trim());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setXPoints(int xPoints){
		setIntegerField(XPOINTS_FIELD,xPoints);
	}
	
	public int getXPoints(){
		if (getChild(XPOINTS_FIELD)!=null){
			return Integer.parseInt(getChild(XPOINTS_FIELD).getValue().trim());
		}else return 1;
	}

	public void setYPoints(int yPoints){
		setIntegerField(YPOINTS_FIELD,yPoints);
	}

	public int getYPoints(){
		if (getChild(YPOINTS_FIELD)!=null){
			return Integer.parseInt(getChild(YPOINTS_FIELD).getValue().trim());
		}else return 1;			
	}
	
	public boolean isRaster(){
		return true;
	}
	
	public Date getEndDate(){
		long result=getStartDate().getTime()+getDurationMilliSecs();
		return new Date(result);
	}
	
	public OffsetRaster copy() {
		OffsetRaster result = new OffsetRaster();
		
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




}
