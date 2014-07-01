package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class TargetDir extends PointingMetadata {
	public TargetDir(PointingMetadata org){
		super(org);
	}

	public TargetDir(String ref){
		super("targetDir","");
		//this.addAttribute(new PointingMetadata("ref",ref));
		setRef(ref);
	}
	
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}
}
