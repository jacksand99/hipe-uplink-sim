package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;

public class Terminator extends PointingAttitude {
	public Terminator(PointingMetadata org){
		super(org);
	}

	public Terminator(Boresight boresight,PhaseAngle phaseAngle,Surface surface){
		super("terminator");
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		setSurface(surface);
	}
	
	public Terminator(Surface surface){
		super("terminator");
		setSurface(surface);
	}
	
	public Terminator(){
		this(new Surface("CG"));
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}

}
