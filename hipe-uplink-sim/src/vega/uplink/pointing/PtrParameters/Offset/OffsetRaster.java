package vega.uplink.pointing.PtrParameters.Offset;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public class OffsetRaster extends OffsetAngles {
	public static String STARTTIME_FIELD="startTime";
	public static String XPOINTS_FIELD="xPoints";
	public static String YPOINTS_FIELD="yPoints";
	public static String XSTART_FIELD="xStart";
	public static String XSTART_DEFAULT_UNIT="deg";
	public static String YSTART_FIELD="yStart";
	public static String YSTART_DEFAULT_UNIT="deg";
	public static String XDELTA_FIELD="xDelta";
	public static String XDELTA_DEFAULT_UNIT="deg";
	public static String YDELTA_FIELD="yDelta";
	public static String YDELTA_DEFAULT_UNIT="deg";
	public static String POINTSLEWTIME_FIELD="pointSlewTime";
	public static String POINTSLEWTIME_DEFAULT_UNIT="min";
	public static String LINESLEWTIME_FIELD="lineSlewTime";
	public static String LINESLEWTIME_DEFAULT_UNIT="min";
	public static String DWELLTIME_FIELD="dwellTime";
	public static String DWELLTIME_DEFAULT_UNIT="min";
	public static String LINEAXIS_FIELD="lineAxis";
	public static String KEEPLINEDIR_FIELD="keepLineDir";
	
	public OffsetRaster(PointingMetadata org){
		super(org);
	}

	public OffsetRaster(Date startTime,int xPoints,int yPoints,float xStart,float yStart,float xDelta,float yDelta,float pointSlewTime,float lineSlewTime,float dwellTime,String lineAxis,boolean keepLineDir) {
		super("raster");
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
	
	public OffsetRaster(String startTime,String xPoints,String yPoints,String xStart,String yStart,String xDelta,String yDelta,String pointSlewTime,String lineSlewTime,String dwellTime,String lineAxis,String keepLineDir) throws ParseException{
		super("raster");
		
		setStartDate(PointingBlock.zuluToDate(startTime));
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
	

	
	public float getPointSlewTime(){
		if (getChild(LINESLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(LINESLEWTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	
	public float getDwellTime(){
		if (getChild(DWELLTIME_FIELD)!=null){
			return Float.parseFloat(getChild(DWELLTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	
	public float getLineSlewTime(){
		if (getChild(POINTSLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(POINTSLEWTIME_FIELD).getValue().trim());
		}else{
			return 1.0f;
		}

	}
	
	public long getDurationMilliSecs(){
		float linetime=((getXPoints()-1)*getPointSlewTime())+(getXPoints()*getDwellTime());
		float result=(linetime*getYPoints())+((getYPoints()-1)*getLineSlewTime());
		return new Float(result*1000*60).longValue();
	}

	public OffsetRaster(String startTime) throws ParseException{
		this(startTime,"1","1","0.","0.","0.","0.","1.","1.","1.","y","false");
	}
	
	public void setStartDate(Date date){
		PointingMetadata startTimeCh = new PointingMetadata("startTime",PointingBlock.dateToZulu(date));
		this.addChild(startTimeCh);

	}
	
	public Date getStartDate(){
		try {
			return PointingBlock.zuluToDate(getChild("startTime").getValue().trim());
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



}