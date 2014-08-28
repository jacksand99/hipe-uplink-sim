package vega.uplink.pointing.PtrParameters.Offset;

import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public abstract class OffsetAngles extends PointingMetadata {
	/**
	 * offsetAngles
	 */
	public static String OFFSETANGLES_TAG="offsetAngles";
	/**
	 * custom
	 */
	public static String OFFSETANGLES_TYPE_CUSTOM="custom";
	/**
	 * fixed
	 */
	public static String OFFSETANGLES_TYPE_FIXED="fixed";
	/**
	 * raster
	 */
	public static String OFFSETANGLES_TYPE_RASTER="raster";
	/**
	 * scan
	 */
	public static String OFFSETANGLES_TYPE_SCAN="scan";
	/**
	 * ref
	 */
	public static String REF_TAG="ref";
	public OffsetAngles(PointingMetadata org){
		super(org);
	}

	/**
	 * Creates an offset rotation referenced to another offset rotation
	 * @param ref
	 */
	public OffsetAngles(String ref){
		super(OFFSETANGLES_TAG,"");
		this.addAttribute(new PointingMetadata(REF_TAG,ref));

	}
	private OffsetAngles(){
		super(OFFSETANGLES_TAG,"");
	}
	
	/**
	 * Get the start time of the offset rotation
	 * @return
	 */
	public String getStartTime(){
		//System.out.println( this.getChild("startTime").getValue());
		return this.getChild("startTime").getValue();
	}
	/**
	 * Set the start time of the offset rotation
	 * @return
	 */	
	public void setStartTime(String startTime){
		//System.out.println(startTime);
		PointingMetadata startTimeCh = new PointingMetadata("startTime",startTime);
		addChild(startTimeCh);
		//System.out.println( this.getChild("startTime").getValue());

	}
	/**
	 * Set the start time of the offset rotation
	 * @return
	 */	
	public void setStartTime(Date startTime){
		this.setStartTime(PointingBlock.dateToZulu(startTime));
	}
	
	public abstract OffsetAngles copy();
	/*{
		OffsetAngles result = new OffsetAngles();
		
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
	}*/
	
	protected float[] stringToFloatArray(String data){
		//System.out.println("%"+data+"%");
		data=data.replaceAll("\\s+", " ");
		if (data.startsWith(" ")) data=data.substring(1);
		//System.out.println("%"+data+"%");

		String[] dTArray = data.split(" ");
		float[] dTF=new float[dTArray.length];
		for (int i=0;i<dTArray.length;i++){
			//System.out.println("*"+dTArray[i]+"*");
			dTF[i]=Float.parseFloat(dTArray[i]);
		}
		return dTF;
	}
	
	public String getUnit(String field){
		PointingMetadata deltaTimesCh = getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute("units")==null) return null;
		return deltaTimesCh.getAttribute("units").getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingMetadata deltaTimesCh = getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingMetadata(field,"");
		deltaTimesCh.addAttribute(new PointingMetadata("units",unit));
		addChild(deltaTimesCh);
		
	}
	
	public void setFloatArrayField(String field,float[] xAngles){
		String sDT="";
		for (int i=0;i<xAngles.length;i++){
			sDT=sDT+xAngles[i]+" ";
		}
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);

	}
	
	public void setFloatField(String field,float value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setIntegerField(String field,int value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setStringField(String field,String value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setBooleanField(String field,boolean value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);

	}
	
	
	
	/*public long getDurationMilliSecs(){
		return 0L;
	}*/

	/**
	 * True if this offset rotation is of the type custom. False otherwise
	 * @return
	 */
	public boolean isCustom(){
		return false;
	}
	/**
	 * True if this offset rotation is of the type raster. False otherwise
	 * @return
	 */
	public boolean isRaster(){
		return false;
	}
	/**
	 * True if this offset rotation is of the type scan. False otherwise
	 * @return
	 */
	public boolean isScan(){
		return false;
	}
	/**
	 * True if this offset rotation is of the type fixed. False otherwise
	 * @return
	 */
	public boolean isFixed(){
		return false;
	}
	/**
	 * Get the duration in milliseconds of this offset rotation
	 * @return
	 */
	public abstract long getDurationMilliSecs();

	


}
