package vega.uplink.pointing.PtrParameters.Offset;

import vega.uplink.pointing.PointingMetadata;

public class OffsetFixed extends OffsetAngles{
	
	public OffsetFixed(PointingMetadata org){
		super(org);
	}

	
	public OffsetFixed(String xAngle,String yAngle){
		super("fixed");
		setXAngle(Float.parseFloat(xAngle));
		setXAngleUnit("deg");
		setYAngle(Float.parseFloat(yAngle));
		setYAngleUnit("deg");

	}
	public OffsetFixed(String xAngleUnit,float xAngle,String yAngleUnit,float yAngle){
		super("fixed");
		setXAngle(xAngle);
		setXAngleUnit(xAngleUnit);
		setYAngle(yAngle);
		setYAngleUnit(yAngleUnit);
		
	}
	
	public OffsetFixed(float xAngle,float yAngle){
		super("fixed");
		setXAngle(xAngle);
		setXAngleUnit("deg");
		setYAngle(yAngle);
		setYAngleUnit("deg");
	}
	
	public void setXAngle(float xAngle){
		setFloatField("xAngle",xAngle);
	}
	
	public float getXAngle(){
		return Float.parseFloat(getChild("xAngle").getValue());
	}
	
	public void setXAngleUnit(String unit){
		setUnit("xAngle",unit);
	}
	
	public void setYAngle(float yAngle){
		setFloatField("yAngle",yAngle);
	}
	
	public void setYAngleUnit(String unit){
		setUnit("yAngle",unit);
	}

	
	public float getYAngle(){
		return Float.parseFloat(getChild("yAngle").getValue());
	}


	
	public OffsetFixed(){
		this("0.0","0.0");
	}
	
	public boolean isFixed(){
		return true;
	}


	@Override
	public long getDurationMilliSecs() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public OffsetFixed copy() {
		OffsetFixed result = new OffsetFixed();
		
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
