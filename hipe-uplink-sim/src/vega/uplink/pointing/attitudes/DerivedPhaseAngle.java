package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.PhaseAngle;

public class DerivedPhaseAngle extends PointingAttitude {

	public DerivedPhaseAngle(PointingMetadata org) {
		super(org);
		// TODO Auto-generated constructor stub
	}
	public DerivedPhaseAngle(PointingAttitude att){
		super(att.getAttitudeType()+"_pwropt");
	}
	public DerivedPhaseAngle(PointingAttitude att,PhaseAngle phaseAngle){
		super("phaseAngle",att.getBoresight(),phaseAngle);
		//att.setBoresight(boresight);
		setAttitude(att);
	}
	
	public void setAttitude(PointingAttitude newAttitude){
		this.addChild(newAttitude);
	}
	
	public PointingAttitude getAttitude(){
		return (PointingAttitude) this.getChild("attitude");
	}


}
