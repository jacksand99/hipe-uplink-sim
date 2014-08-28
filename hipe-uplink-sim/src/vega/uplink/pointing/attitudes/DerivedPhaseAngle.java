package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.PhaseAngle;

/**
 * The derived pointing phaseAngle allows to modify the phaseAngle of an attitude. This is
 * typically needed if an offset rotation is required for the boresight, but the attitude shall be
 * power optimised.
 * @author jarenas
 *
 */
public class DerivedPhaseAngle extends PointingAttitude {

	public DerivedPhaseAngle(PointingMetadata org) {
		super(org);
		// TODO Auto-generated constructor stub
	}
	/**
	 * The derived pointing phaseAngle allows to modify the phaseAngle of an attitude. This is
	 * typically needed if an offset rotation is required for the boresight, but the attitude shall be
	 * @param att Contains the definition of the attitude profile i.e. a basic pointing and optionally offset angles
	 */
	public DerivedPhaseAngle(PointingAttitude att){
		super(att.getAttitudeType()+PointingAttitude.POWEROPTIMIZED_TAG);
	}
	/**
	 * The derived pointing phaseAngle allows to modify the phaseAngle of an attitude. This is
	 * typically needed if an offset rotation is required for the boresight, but the attitude shall be
	 * @param att Contains the definition of the attitude profile i.e. a basic pointing and optionally offset angles
	 * @param phaseAngle PhaseAngle to be applied
	 */
	public DerivedPhaseAngle(PointingAttitude att,PhaseAngle phaseAngle){
		super(PhaseAngle.PHASEANGLE_TAG,att.getBoresight(),phaseAngle);
		//att.setBoresight(boresight);
		setAttitude(att);
	}
	
	/**
	 * Set the definition of the attitude profile i.e. a basic pointing and optionally offset angles
	 * @param newAttitude
	 */
	public void setAttitude(PointingAttitude newAttitude){
		this.addChild(newAttitude);
	}
	/**
	 * Get the definition of the attitude profile i.e. a basic pointing and optionally offset angles
	 */	
	public PointingAttitude getAttitude(){
		return (PointingAttitude) this.getChild(PointingAttitude.ATTITUDE_TAG);
	}


}
