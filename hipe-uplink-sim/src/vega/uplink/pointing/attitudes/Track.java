package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.DirectionVector;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.TargetTrack;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

public class Track extends PointingAttitude {
	public Track(PointingMetadata org){
		super(org);
	}
	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,target,oAngles);
		addChild(oRefAxis);
	}

	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target,OffsetAngles oAngles){
		this(boresight,phaseAngle,target);
		addChild(oAngles);
		
	}
	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target){
		super("track");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	public Track(Boresight boresight,PhaseAngle phaseAngle,DirectionVector target){
		super("track");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	
	public Track(TargetTrack target){
		super("track");
		setTarget(target);
	}
	public Track(DirectionVector target){
		super("track");
		setTarget(target);
	}
	
	public Track(){
		super("track");
		//this(new Boresight(),new PhaseAngle(),new Target("CG"));
	}
	
	public PointingMetadata getTarget(){
		return (PointingMetadata) this.getChild("target");
	}
	
	public void setTarget(TargetTrack target){
		addChild(target);
	}
	
	public void setTarget(DirectionVector target){
		addChild(target);
	}


}
