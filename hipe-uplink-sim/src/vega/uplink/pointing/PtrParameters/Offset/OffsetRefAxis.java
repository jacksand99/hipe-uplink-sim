package vega.uplink.pointing.PtrParameters.Offset;

import vega.uplink.pointing.PointingMetadata;

public class OffsetRefAxis extends PointingMetadata {
	
	public OffsetRefAxis(PointingMetadata org){
		super(org);
	}

	public OffsetRefAxis(String ref){
		super("offsetRefAxis","");
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	
	public OffsetRefAxis(){
		this("SC_Xaxis");
	}

}
