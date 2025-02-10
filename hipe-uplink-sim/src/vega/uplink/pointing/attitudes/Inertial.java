package vega.uplink.pointing.attitudes;

//import java.lang.annotation.Target;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
//import vega.uplink.pointing.PtrParameters.Target;
import vega.uplink.pointing.PtrParameters.TargetInert;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

/**
 * The boresight is aligned with a fixed vector given relative to inertialframe.
 * @author jarenas
 *
 */
public class Inertial extends PointingAttitude {
	public Inertial(PointingElement org){
		super(org);
	}
	
	public Inertial copy(){
		Inertial result = new Inertial(super.copy());
		return result;
	}
	/**
	 * Creates an Attitude where the boresight is aligned with a fixed vector given relative to inertialframe.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target Fixed vector defined relative to inertial frame
	 * @param oAngles OffsetAngles
	 */
	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target,OffsetAngles oAngles){
		this(boresight,phaseAngle,target);
		this.addChild(oAngles);
	}
	/**
	 * Creates an Attitude where the boresight is aligned with a fixed vector given relative to inertialframe.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target Fixed vector defined relative to inertial frame
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
	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,target,oAngles);
		this.addChild(oRefAxis);
	
	}
	
	/**
	 * Creates an Attitude where the boresight is aligned with a fixed vector given relative to inertialframe.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target Fixed vector defined relative to inertial frame
	 */
	public Inertial(Boresight boresight,PhaseAngle phaseAngle,TargetInert target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_INERTIAL);
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	/**
	 * Creates an Attitude where the boresight is aligned with a fixed vector given relative to inertialframe.
	 * @param target Fixed vector defined relative to inertial frame
	 */	
	public Inertial(TargetInert target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_INERTIAL);
		setTarget(target);
		
	}
	
	public Inertial(){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_INERTIAL);
		//this(new Boresight(),new PhaseAngle(),new Target("EME2000","0.","1.","1."));
	}
	
	public TargetInert getTarget(){
		return (TargetInert) this.getChild("target");
	}
	
	public void setTarget(TargetInert target){
		addChild(target);
	}
}
