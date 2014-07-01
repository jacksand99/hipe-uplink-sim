package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;

public class Velocity extends PointingAttitude {
	public Velocity(PointingMetadata org){
		super(org);
	}

	public Velocity(){
		super("velocity");
	}
	
	public Velocity(Boresight boresight,PhaseAngle phaseAngle){
		this();
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
	}
}
