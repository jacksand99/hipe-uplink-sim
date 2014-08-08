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
