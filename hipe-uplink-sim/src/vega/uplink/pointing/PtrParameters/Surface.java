package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PdfmSurface;
import vega.uplink.pointing.PointingMetadata;

public class Surface extends PdfmSurface {
	public Surface(PointingMetadata org){
		super(org);
	}

	public Surface(String ref){
		super();
		super.addAttribute(new PointingMetadata("ref",ref));
	}
	public Surface(){
		this("CG");
	}
	public Surface(String frame,String origin,String unitsA,float a,String unitsB,float b,String unitsC,float c,String axisAFrame,float axisAX,float axisAY,float axisAZ,String axisBFrame,float axisBX,float axisBY,float axisBZ,String axisCFrame,float axisCX,float axisCY,float axisCZ){
		super(null,frame,origin,unitsA,a,unitsB,b,unitsC,c,axisAFrame,axisAX,axisAY,axisAZ,axisBFrame,axisBX,axisBY,axisBZ,axisCFrame,axisCX,axisCY,axisCZ);
	}
	public Surface(String frame,String origin,String units,float a,float b,float c,float axisAX,float axisAY,float axisAZ,float axisBX,float axisBY,float axisBZ,float axisCX,float axisCY,float axisCZ){
		this(frame,origin,units,a,units,b,units,c,frame,axisAX,axisAY,axisAZ,frame,axisBX,axisBY,axisBZ,frame,axisCX,axisCY,axisCZ);
	}

	
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}

}
