package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.Height;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;
//import vega.uplink.pointing.PtrParameters.Target;
//import vega.uplink.pointing.PtrParameters.TargetDir;
import vega.uplink.pointing.PtrParameters.TargetDir;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

public class Limb extends PointingAttitude {
	public Limb(PointingMetadata org){
		super(org);
	}
	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface,OffsetAngles oAngles){
		this(boresight,phaseAngle,targetDir,height,surface);
		addChild(oAngles);
	}
	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,targetDir,height,surface,oAngles);
		addChild(oRefAxis);
	}

	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface){
		super("limb");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(targetDir);
		addChild(height);
		addChild(surface);
		
		
	}
	
	public Limb(TargetDir targetDir,Height height,Surface surface){
		super("limb");
		setTargetDir(targetDir);
		setHeight(height);
		setSurface(surface);
	}
	
	public Limb(){
		super("limb");
		//this(new Boresight(),new PhaseAngle(),new TargetDir("CG2Sun"),new Height("km","0."),new Surface("CG"));
	}
	
	
	public TargetDir getTargetDir(){
		return (TargetDir) this.getChild("targetDir");
	}
	
	public void setTargetDir(TargetDir target){
		addChild(target);
	}
	
	public void setHeight(Height height){
		addChild(height);
	}
	
	public Height getHeight(){
		return (Height) this.getChild("height");
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}

}
