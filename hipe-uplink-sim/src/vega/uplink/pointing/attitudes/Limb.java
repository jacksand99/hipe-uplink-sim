package vega.uplink.pointing.attitudes;

import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.Height;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Surface;
import vega.uplink.pointing.PtrParameters.TargetDir;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;

/**
 * Points the boresight to an user-selected point relative to the limb of CG.
 * @author jarenas
 *
 */
public class Limb extends PointingAttitude {
	public Limb(PointingMetadata org){
		super(org);
	}
	/**
	 * Creates an Attitude that points the boresight to an user-selected point relative to the limb of CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param targetDir The selected point on the limb lies in the half-plane defined by the CG-to-ROS direction and positive direction towards targetDir (targetDir must not be aligned with the CGto- ROS direction).
	 * @param height The boresight is pointed towards the point that lies the specified height along the local normal of the selected point on the limb.
	 * @param surface Surface for which the limb is calculated.
	 * @param oAngles OffsetAngles
	 */	
	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface,OffsetAngles oAngles){
		this(boresight,phaseAngle,targetDir,height,surface);
		addChild(oAngles);
	}
	/**
	 * Creates an Attitude that points the boresight to an user-selected point relative to the limb of CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param targetDir The selected point on the limb lies in the half-plane defined by the CG-to-ROS direction and positive direction towards targetDir (targetDir must not be aligned with the CGto- ROS direction).
	 * @param height The boresight is pointed towards the point that lies the specified height along the local normal of the selected point on the limb.
	 * @param surface Surface for which the limb is calculated.
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
	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface,OffsetAngles oAngles,OffsetRefAxis oRefAxis){
		this(boresight,phaseAngle,targetDir,height,surface,oAngles);
		addChild(oRefAxis);
	}
	/**
	 * Creates an Attitude that points the boresight to an user-selected point relative to the limb of CG.
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 * @param targetDir The selected point on the limb lies in the half-plane defined by the CG-to-ROS direction and positive direction towards targetDir (targetDir must not be aligned with the CGto- ROS direction).
	 * @param height The boresight is pointed towards the point that lies the specified height along the local normal of the selected point on the limb.
	 * @param surface Surface for which the limb is calculated.
	 */
	public Limb(Boresight boresight,PhaseAngle phaseAngle,TargetDir targetDir,Height height,Surface surface){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_LIMB);
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
		addChild(targetDir);
		addChild(height);
		addChild(surface);
		
		
	}
	/**
	 * Creates an Attitude that points the boresight to an user-selected point relative to the limb of CG.
	 * @param targetDir The selected point on the limb lies in the half-plane defined by the CG-to-ROS direction and positive direction towards targetDir (targetDir must not be aligned with the CGto- ROS direction).
	 * @param height The boresight is pointed towards the point that lies the specified height along the local normal of the selected point on the limb.
	 * @param surface Surface for which the limb is calculated.
	 */	
	public Limb(TargetDir targetDir,Height height,Surface surface){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_LIMB);
		setTargetDir(targetDir);
		setHeight(height);
		setSurface(surface);
	}
	
	/**
	 * Creates an Attitude that points the boresight to an user-selected point relative to the limb of CG.
	 * The targetDir is CG2Sun, the height is 0 km and the surface CG
	 */
	public Limb(){
		super(PointingAttitude.POINTING_ATTITUDE_TYPE_LIMB);
		//this(new Boresight(),new PhaseAngle(),new TargetDir("CG2Sun"),new Height("km","0."),new Surface("CG"));
	}
	
	
	public TargetDir getTargetDir(){
		return (TargetDir) this.getChild("targetDir");
	}
	
	public void setTargetDir(TargetDir target){
		addChild(target);
	}
	
	public void setHeight(Height height){
		addChild(height);
	}
	
	public Height getHeight(){
		return (Height) this.getChild("height");
	}
	
	public void setSurface(Surface surface){
		this.addChild(surface);
	}
	
	public Surface getSurface(){
		return (Surface) this.getChild("surface");
	}

}
