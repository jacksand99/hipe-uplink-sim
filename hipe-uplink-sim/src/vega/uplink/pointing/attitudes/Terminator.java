package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

/**
 * points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
 * @author jarenas
 *
 */
public class Terminator extends PointingAttitude {
	public Terminator(PointingElement org){
		super(org);
	}
	/**
	 * Generate an attitude that points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param surface Elliptical surface
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
	public Terminator(Boresight boresight,PhaseAngle phaseAngle,Surface surface,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,surface,oAngles);
		addChild(oRefAxis);
	}
	/**
	 * Generate an attitude that points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param surface Elliptical surface
	 * @param oAngles OffsetAngles
	 */
	public Terminator(Boresight boresight,PhaseAngle phaseAngle,Surface surface,OffsetAngles oAngles){
		this(boresight,phaseAngle,surface);
		addChild(oAngles);
	}
	/**
	 * Generate an attitude that points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param surface Elliptical surface
	 */
	public Terminator(Boresight boresight,PhaseAngle phaseAngle,Surface surface){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TERMINATOR);
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		setSurface(surface);
	}
	/**
	 * Generate an attitude that points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
	 * @param surface Elliptical surface
	 */	
	public Terminator(Surface surface){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TERMINATOR);
		setSurface(surface);
	}
	
	public Terminator(){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TERMINATOR);
		//this(new Surface("CG"));
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}

}
