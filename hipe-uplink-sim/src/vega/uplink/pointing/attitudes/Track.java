package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.DirectionVector;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.TargetTrack;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

/**
 * The target is given by a solar system object or landmark.
 * @author jarenas
 *
 */
public class Track extends PointingAttitude {
	public Track(PointingElement org){
		super(org);
	}
	
	public Track copy(){
		Track result = new Track(super.copy());
		return result;
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target solar system object or landmarks defined in the PDFM
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
	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,target,oAngles);
		addChild(oRefAxis);
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target solar system object or landmarks defined in the PDFM
	 * @param oAngles OffsetAngles
	 */
	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target,OffsetAngles oAngles){
		this(boresight,phaseAngle,target);
		addChild(oAngles);
		
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target solar system object or landmarks defined in the PDFM
	 */
	public Track(Boresight boresight,PhaseAngle phaseAngle,TargetTrack target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param target solar system object or landmarks defined in the PDFM
	 */
	public Track(Boresight boresight,PhaseAngle phaseAngle,DirectionVector target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(target);
		
		
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param target solar system object or landmarks defined in the PDFM
	 */	
	public Track(TargetTrack target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
		setTarget(target);
	}
	/**
	 * Creates an Attitude where the target is given by a solar system object or landmark.
	 * @param target solar system object or landmarks defined in the PDFM
	 */	
	public Track(DirectionVector target){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
		setTarget(target);
	}
	
	public Track(){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
		//this(new Boresight(),new PhaseAngle(),new Target("CG"));
	}
	
	public PointingElement getTarget(){
		return (PointingElement) this.getChild(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK);
	}
	
	public void setTarget(TargetTrack target){
		addChild(target);
	}
	
	public void setTarget(DirectionVector target){
		addChild(target);
	}


}
