package vega.uplink.pointing;

import java.util.Date;

import vega.uplink.pointing.PtrParameters.PointedAxis;

public class PointingBlockGSEP extends PointingBlock {
	public PointingBlockGSEP(Date startTime,Date endTime,PointedAxis coordinates,boolean sunPointing,boolean ecliptic,boolean north){
		super("GSEP",startTime,endTime);
		this.addChild(coordinates);
		this.setBooleanField("sunPointing",sunPointing);
		this.setBooleanField("ecliptic",ecliptic);
		this.setBooleanField("north",north);
	}
	public void setBooleanField(String field,boolean value){
		if (value) this.addChild(new PointingMetadata(field,"true"));
		else this.addChild(new PointingMetadata(field,"false"));
	}
	
	public boolean isSunPointing(){
		return Boolean.parseBoolean(this.getChild("sunPointing").getValue());
	}
	public boolean isEcliptic(){
		return Boolean.parseBoolean(this.getChild("ecliptic").getValue());
	}
	
	public boolean isNorth(){
		return Boolean.parseBoolean(this.getChild("north").getValue());
	}

}
