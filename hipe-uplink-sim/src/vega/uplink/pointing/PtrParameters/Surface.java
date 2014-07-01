package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class Surface extends PointingMetadata {
	public Surface(PointingMetadata org){
		super(org);
	}

	public Surface(String ref){
		super("surface","");
		super.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}

}
