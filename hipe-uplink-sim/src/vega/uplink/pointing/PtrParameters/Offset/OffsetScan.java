package vega.uplink.pointing.PtrParameters.Offset;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public class OffsetScan extends OffsetAngles {
	public static String STARTTIME_FIELD="startTime";
	public static String NUMBEROFLINES_FIELD="numberOfLines";
	public static String NUMBEROFSCANSPERLINE_FIELD="numberOfScansPerLine";
	public static String XSTART_FIELD="xStart";
	public static String XSTART_DEFAULT_UNIT="deg";
	public static String YSTART_FIELD="yStart";
	public static String YSTART_DEFAULT_UNIT="deg";
	public static String SCANDELTA_FIELD="scanDelta";
	public static String SCANDELTA_DEFAULT_UNIT="deg";
	public static String LINEDELTA_FIELD="lineDelta";
	public static String LINEDELTA_DEFAULT_UNIT="deg";
	public static String SCANTIME_FIELD="scanTime";
	public static String SCANTIME_DEFAULT_UNIT="min";
	public static String SCANSPEED_FIELD="scanSpeed";
	public static String SCANSPEED_DEFAULT_UNIT="deg/min";
	public static String SCANSLEWTIME_FIELD="scanSlewTime";
	public static String SCANSLEWTIME_DEFAULT_UNIT="min";
	public static String LINESLEWTIME_FIELD="lineSlewTime";
	public static String LINESLEWTIME_DEFAULT_UNIT="min";
	public static String BORDERSLEWTIME_FIELD="borderSlewTime";
	public static String BORDERSLEWTIME_DEFAULT_UNIT="min";
	public static String LINEAXIS_FIELD="lineAxis";
	public static String KEEPLINEDIR_FIELD="keepLineDir";
	public static String KEEPSCANDIR_FIELD="keepScanDir";
	
	public OffsetScan(PointingMetadata org){
		super(org);
	}

	public OffsetScan(Date startTime,int numberOfLines,int numberOfScansPerLine,float xStart,float yStart,float scanDelta,float lineDelta,Float scanTime,Float scanSpeed,float scanSlewTime,float lineSlewTime,float borderSlewTime,String lineAxis,boolean keepLineDir,boolean keepScanDir){
		super("scan");
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
		setFloatField(BORDERSLEWTIME_FIELD,lineSlewTime);
		setUnit(BORDERSLEWTIME_FIELD,BORDERSLEWTIME_DEFAULT_UNIT);
		setStringField(LINEAXIS_FIELD,lineAxis);
		setBooleanField(KEEPLINEDIR_FIELD,keepLineDir);
		setBooleanField(KEEPSCANDIR_FIELD,keepScanDir);		
	}
	public OffsetScan(String startTime,String numberOfLines,String numberOfScansPerLine,String xStart,String yStart,String scanDelta,String lineDelta,String scanTime,String scanSpeed,String scanSlewTime,String lineSlewTime,String borderSlewTime,String lineAxis,String keepLineDir,String keepScanDir) throws ParseException{
		super("scan");
		setStartDate(PointingBlock.zuluToDate(startTime));
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
		setFloatField(BORDERSLEWTIME_FIELD,Float.parseFloat(lineSlewTime));
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

	public float getBorderSlewTime(){
		return Float.parseFloat(getChild(BORDERSLEWTIME_FIELD).getValue());
	}

	public float getScanSpeed(){
		return Float.parseFloat(getChild(SCANSPEED_FIELD).getValue());
	}

	public float getScanTime(){
		return Float.parseFloat(getChild(SCANTIME_FIELD).getValue());
	}
	
	public float getScanSlewTime(){
		if (getChild(SCANSLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(SCANSLEWTIME_FIELD).getValue());
		}else{
			return 1.0f;
		}
	}
	public float getLineSlewTime(){
		if (getChild(LINESLEWTIME_FIELD)!=null){
			return Float.parseFloat(getChild(LINESLEWTIME_FIELD).getValue());
		}else{
			return 1.0f;
		}
	}

	public long getDurationMilliSecs(){
		float scanTime=0.0f;
		if (getChild(SCANSPEED_FIELD)!=null){
			scanTime = getScanDelta()/getScanSpeed();
		}
		else{
			scanTime = getScanTime();
		}
		float lineTime=(scanTime*getNumberOfScansPerLine())+(getScanSlewTime()*(getNumberOfScansPerLine()-1));
		float result = ((lineTime*getNumberOfLines())+((getNumberOfLines()-1)*getLineSlewTime()))*60*1000;
		if (result>=0) return new Float(result).longValue();
		else return new Float(result*-1).longValue();

	}
	

	public OffsetScan(String startTime) throws ParseException{
		this(startTime,"1","1","0.","0.","0.","0.","1.",null,"1.","1.","1.","y","false","false");
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
	
	public Date getEndDate(){
		long result=getStartDate().getTime()+getDurationMilliSecs();
		return new Date(result);
	}

	public boolean isScan(){
		return true;
	}

	
	/*public int getScanDuration(){
		scanSpeed=
	}*/

}
