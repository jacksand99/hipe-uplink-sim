package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Target;

public class Track extends PointingAttitude {
	public Track(PointingMetadata org){
		super(org);
	}

	public Track(Boresight boresight,PhaseAngle phaseAngle,Target target){
		super("track");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	
	public Track(Target target){
		super("track");
		setTarget(target);
	}
	
	public Track(){
		this(new Boresight(),new PhaseAngle(),new Target("CG"));
	}
	
	public Target getTarget(){
		return (Target) this.getChild("target");
	}
	
	public void setTarget(Target target){
		addChild(target);
	}

}
