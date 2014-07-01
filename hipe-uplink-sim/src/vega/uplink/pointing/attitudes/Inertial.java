package vega.uplink.pointing.attitudes;

//import java.lang.annotation.Target;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Target;

public class Inertial extends PointingAttitude {
	public Inertial(PointingMetadata org){
		super(org);
	}

	public Inertial(Boresight boresight,PhaseAngle phaseAngle,Target target){
		super("inertial");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	
	public Inertial(Target target){
		super("inertial");
		setTarget(target);
		
	}
	
	public Inertial(){
		this(new Boresight(),new PhaseAngle(),new Target("EME2000","0.","1.","1."));
	}
	
	public Target getTarget(){
		return (Target) this.getChild("target");
	}
	
	public void setTarget(Target target){
		addChild(target);
	}
}
