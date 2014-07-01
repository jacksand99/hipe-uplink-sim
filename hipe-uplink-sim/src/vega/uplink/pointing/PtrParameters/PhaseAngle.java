package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

public class PhaseAngle extends PointingMetadata {
	static String YDIR="yDir";
	static String XDIR="xDir";
	static String ZDIR="xDir";

	public PhaseAngle(PointingMetadata org){
		super(org);
	}

	public PhaseAngle(String ref,String dir){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref",ref));
		this.addChild(new PointingMetadata(dir,"true"));
		
	}
	
	public PhaseAngle(){
		this("powerOptimised","yDir");
		setAngle("deg","90");
	}
	public PhaseAngle(String sCAxysFrame,float sCAxysx,float sCAxysy,float sCAxysz,String inertialAxysFrame,float inertialAxysx,float inertialAxysy,float inertialAxysz){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","align"));
		PointingMetadata child = new PointingMetadata("SCAxis","");
		child.addAttribute(new PointingMetadata("frame",sCAxysFrame));
		child.addChild(new PointingMetadata("x",""+sCAxysx));
		child.addChild(new PointingMetadata("y",""+sCAxysy));
		child.addChild(new PointingMetadata("z",""+sCAxysz));
		this.addChild(child);
		
		PointingMetadata child2 = new PointingMetadata("inertialAxis","");
		child2.addAttribute(new PointingMetadata("frame",inertialAxysFrame));
		child2.addChild(new PointingMetadata("x",""+inertialAxysx));
		child2.addChild(new PointingMetadata("y",""+inertialAxysy));
		child2.addChild(new PointingMetadata("z",""+inertialAxysz));
		this.addChild(child2);


	}
	
	public void setAngle(String units,String angle){
		PointingMetadata an = (new PointingMetadata("angle",angle));
		an.addAttribute(new PointingMetadata("units",units));
		this.addChild(an);
	}
}
