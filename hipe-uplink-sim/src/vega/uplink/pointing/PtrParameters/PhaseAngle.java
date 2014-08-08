package vega.uplink.pointing.PtrParameters;

import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

public class PhaseAngle extends PointingMetadata {
	static String YDIR="yDir";
	static String XDIR="xDir";
	static String ZDIR="xDir";

	public PhaseAngle(PointingMetadata org){
		super(org);
	}

	/*public PhaseAngle(String ref,String dir){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref",ref));
		this.addChild(new PointingMetadata(dir,"true"));
		
	}*/
	public PhaseAngle(java.lang.String tRef, java.lang.String yRot){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","flip"));
		this.addChild(new PointingMetadata("flipStartTime",tRef));
		PointingMetadata flipType = new PointingMetadata("flipType",yRot);
		//flipType.addAttribute(new PointingMetadata("ref",yRot));
		this.addChild(flipType);
		
	}
	
	public PhaseAngle(java.util.Date tRef, java.lang.String yRot){
		this(PointingBlock.dateToZulu(tRef),yRot);
	}
	public PhaseAngle(java.lang.Boolean yDir, String units,float angle){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","powerOptimised"));
		setYDir(yDir);
		setAngle(units,angle);
		
	}
	public PhaseAngle(java.lang.Boolean yDir, float angle){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","powerOptimised"));
		setYDir(yDir);
		setAngle("deg",angle);
		//this.addChild(new PointingMetadata(yDir,yDir));
		
	}
	public void setYDir(boolean yDir){
		if (yDir){
			this.addChild(new PointingMetadata("yDir","true"));
		}
		else{
			this.addChild(new PointingMetadata("yDir","false"));
		}
	}
	public PhaseAngle(java.lang.Boolean yDir){
		this(yDir,90);
	}
	public PhaseAngle(){
		this(true);
		//this("powerOptimised","yDir");
		//setAngle("deg","90");
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
	public void setAngle(String units,float angle){
		PointingMetadata an = (new PointingMetadata("angle",angle+""));
		an.addAttribute(new PointingMetadata("units",units));
		this.addChild(an);
	}
	
	public void setFlipStartTime(Date time){
		this.addChild(new PointingMetadata("flipStartTime",PointingBlock.dateToZulu(time)));
	}

}
