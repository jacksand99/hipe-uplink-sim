package vega.uplink.pointing.attitudes;

//import java.lang.annotation.Target;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
//import vega.uplink.pointing.PtrParameters.Target;
import vega.uplink.pointing.PtrParameters.TargetInert;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

public class Inertial extends PointingAttitude {
	public Inertial(PointingMetadata org){
		super(org);
	}
	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target,OffsetAngles oAngles){
		this(boresight,phaseAngle,target);
		this.addChild(oAngles);
	}
	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,target,oAngles);
		this.addChild(oRefAxis);
	
	}
	

	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target){
		super("inertial");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	
	public Inertial(TargetInert target){
		super("inertial");
		setTarget(target);
		
	}
	
	public Inertial(){
		super("inertial");
		//this(new Boresight(),new PhaseAngle(),new Target("EME2000","0.","1.","1."));
	}
	
	public TargetInert getTarget(){
		return (TargetInert) this.getChild("target");
	}
	
	public void setTarget(TargetInert target){
		addChild(target);
	}
}
