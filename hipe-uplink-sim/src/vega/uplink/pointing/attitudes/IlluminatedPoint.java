package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;

public class IlluminatedPoint extends PointingAttitude {
	public IlluminatedPoint(PointingMetadata org){
		super(org);
	}

	public IlluminatedPoint(Boresight boresight,PhaseAngle phaseAngle,Surface surface){
		super("illuminatedPoint");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		setSurface(surface);
	}
	
	public IlluminatedPoint(Surface surface){
		super("illuminatedPoint");
		setSurface(surface);
	}
	
	public IlluminatedPoint(){
		this(new Surface("CG"));
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}

}