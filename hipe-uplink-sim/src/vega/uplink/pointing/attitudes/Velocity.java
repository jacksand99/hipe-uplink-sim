package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

/**
 * Points the boresight along the velocity vector of the SC relative to CG.
 * @author jarenas
 *
 */
public class Velocity extends PointingAttitude {
	public Velocity(PointingElement org){
		super(org);
	}

	public Velocity(){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_VELOCITY);
	}
	
	public Velocity copy(){
		Velocity result = new Velocity(super.copy());
		return result;
	}
	/**
	 * Creates an attitude pointing the boresight along the velocity vector of the SC relative to CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param oAngles OffsetAngles
	 * @param oRefAxis defines together with the boresight two axes in SC frame around which the rotations are performed.
	 * offsetRefAxis is of type direction vector. In the following these axes are called offset-x-axis and offset-y-axis. The offset-y-axis is the unit vector
	 * along the cross product of boresight and offsetRefAxis. The offset-x-axis is defined
	 * such that offset-x-axis, offset-y-axis and boresight form a right handed orthogonal frame. If
	 * for offsetRefAxis a direction vector is specified that is defined relative to inertial frame
	 * then it is converted to SC frame by using the basic pointing without offset rotations. The
	 * resulting SC attitude with offset rotations is given by the basic pointing rotated right
	 * handed first around the offset-x-axis by minus the y-angle and then a right handed rotation
	 * around the offset-y-axis by the x-angle. This convention is chosen because a positive x and
	 * y-angle rotate the boresight towards the offset-x-axis and y-axis, respectively
	 */
	public Velocity(Boresight boresight,PhaseAngle phaseAngle,OffsetAngles oAngle,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,oAngle);
		addChild(oRefAxis);
	}
	/**
	 * Creates an attitude pointing the boresight along the velocity vector of the SC relative to CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param oAngles OffsetAngles
	 */
	public Velocity(Boresight boresight,PhaseAngle phaseAngle,OffsetAngles oAngle){
		this(boresight,phaseAngle);
		addChild(oAngle);
	}
	/**
	 * Creates an attitude pointing the boresight along the velocity vector of the SC relative to CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 */	
	public Velocity(Boresight boresight,PhaseAngle phaseAngle){
		this();
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
	}
}
