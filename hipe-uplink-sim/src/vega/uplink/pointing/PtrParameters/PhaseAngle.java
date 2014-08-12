package vega.uplink.pointing.PtrParameters;

import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingMetadata;

/**
 * Rule that fixes the degree of freedom around the boresight.
 * @author jarenas
 *
 */
public class PhaseAngle extends PointingMetadata {
	static String YDIR="yDir";
	static String XDIR="xDir";
	static String ZDIR="xDir";

	public PhaseAngle(PointingMetadata org){
		super(org);
	}

	/**
	 * Flip: This phase angle is used to support comet fly-bys
	 * that contain the sun direction in the fly-by plane.
	 * Before the flip either the plus or minus Y-axis are
	 * aligned with orbital pole of the SC wrt. the comet
	 * projected in the plane perpendicular to the
	 * boresight. During the flip the SC rotates 180deg
	 * around the boresight such that the opposite axis
	 * than before the flip is aligned with the orbital
	 * pole.
	 * When this phase angle is used, the selected
	 * <phaseAngle ref=”flip” >
	 * <flipStartTime> 2014-12-01T00:00:00
	 * </flipStartTime>
	 * <flipType> pyPosRot </flipType>
	 * </phaseAngle>
	 * boresight must be in the SC-x-z-plane.
	 * The type of flip (element <flipType>) indicates
	 * whether the SC plus Y axis (“py”)or the minus Y
	 * axis (“my”) is aligned with the orbital pole before
	 * the flip and whether the rotation during the flip is
	 * a positive (“PosRot”) or negative (“NegRot”) rotation around
	 * the boresight. Thus there are four different values
	 * for the ref-attribute of the <flipType> element:
	 * pyPosRot
	 * pyNegRot
	 * myPosRot
	 * myNegRot
	 * The flip start time is specified in the
	 * <flipStartTime> element. For all type of flips the
	 * flip duration is 60min.
	 * @param tRef Flip Start Time
	 * @param yRot Flip type (pyPosRot,pyNegRot,myPosRot or myNegRot)
	 */
	public PhaseAngle(java.lang.String tRef, java.lang.String yRot){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","flip"));
		this.addChild(new PointingMetadata("flipStartTime",tRef));
		PointingMetadata flipType = new PointingMetadata("flipType",yRot);
		//flipType.addAttribute(new PointingMetadata("ref",yRot));
		this.addChild(flipType);
		
	}
	
	public PhaseAngle(java.util.Date tRef, java.lang.String yRot){
		this(PointingBlock.dateToZulu(tRef),yRot);
	}
	/**
	 * The power optimised roll places the SC-y-axis at a
	 * certain angle relative to the SC to Sun direction.
	 * The SC-y-axis is along (i.e. has positive projection
	 * on) the cross product of boresight and SC to Sun
	 * direction, if the parameter yDir is set to true.
	 * For yDir set to false it is towards minus the
	 * cross product. The default value for yDir is set to
	 * true.
	 * It is recommended that this phaseAngle
	 * representation is only selected, when the sun
	 * direction is between 30deg and 150deg from the
	 * inertial pointing direction (otherwise the pointing
	 * can have high rates).
	 * @param yDir YDir
	 * @param units Angle units
	 * @param angle Angle
	 */
	public PhaseAngle(java.lang.Boolean yDir, String units,float angle){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","powerOptimised"));
		setYDir(yDir);
		setAngle(units,angle);
		
	}
	/**
	 * The power optimised roll places the SC-y-axis at a
	 * certain angle relative to the SC to Sun direction.
	 * The SC-y-axis is along (i.e. has positive projection
	 * on) the cross product of boresight and SC to Sun
	 * direction, if the parameter yDir is set to true.
	 * For yDir set to false it is towards minus the
	 * cross product. The default value for yDir is set to
	 * true.
	 * It is recommended that this phaseAngle
	 * representation is only selected, when the sun
	 * direction is between 30deg and 150deg from the
	 * inertial pointing direction (otherwise the pointing
	 * can have high rates).
	 * The angle unit is assumed to be deg
	 * @param yDir YDir
	 * @param angle Angle
	 */
	public PhaseAngle(java.lang.Boolean yDir, float angle){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","powerOptimised"));
		setYDir(yDir);
		setAngle("deg",angle);
		//this.addChild(new PointingMetadata(yDir,yDir));
		
	}
	/**
	 * For Power Optimised Phase Angle, set the yDir parameter.
	 * if the parameter yDir is set to true The SC-y-axis is along (i.e. has positive projection
	 * on) the cross product of boresight and SC to Sun direction.
	 * For yDir set to false it is towards minus the
	 * cross product. 
	 * @param yDir
	 */
	public void setYDir(boolean yDir){
		if (yDir){
			this.addChild(new PointingMetadata("yDir","true"));
		}
		else{
			this.addChild(new PointingMetadata("yDir","false"));
		}
	}
	/**
	 * The power optimised roll places the SC-y-axis at a
	 * 90 deg angle relative to the SC to Sun direction.
	 * The SC-y-axis is along (i.e. has positive projection
	 * on) the cross product of boresight and SC to Sun
	 * direction, if the parameter yDir is set to true.
	 * For yDir set to false it is towards minus the
	 * cross product. The default value for yDir is set to
	 * true.
	 * It is recommended that this phaseAngle
	 * representation is only selected, when the sun
	 * direction is between 30deg and 150deg from the
	 * inertial pointing direction (otherwise the pointing
	 * can have high rates).
	 * @param yDir
	 */
	public PhaseAngle(java.lang.Boolean yDir){
		this(yDir,90);
	}
	/**
	 * The power optimised roll places the SC-y-axis at a
	 * 90 deg angle relative to the SC to Sun direction.
	 *yDir is set to true, so the SC-y-axis is along (i.e. has positive projection
	 * on) the cross product of boresight and SC to Sun
	 * direction.
	 * It is recommended that this phaseAngle
	 * representation is only selected, when the sun
	 * direction is between 30deg and 150deg from the
	 * inertial pointing direction (otherwise the pointing
	 * can have high rates).
	 */
	public PhaseAngle(){
		this(true);
		//this("powerOptimised","yDir");
		//setAngle("deg","90");
	}
	/**
	 * Align SC axis with certain inertial direction. The
	 * rotation angle around the boresight is chosen
	 * such that the projections of the vectors SCAxis
	 * and inertialAxis projected in the plane
	 * perpendicular to the boresight are aligned.
	 * The SCAxis and inertialAxis must not be parallel
	 * to the boresight.
	 * For the SC and inertial axis references to
	 * predefined axes are allowed.
	 * For the duration of the pointing, it is
	 * recommended that the selected inertial axis is
	 * between 30 and 150deg from the inertial pointing
	 * direction (otherwise the pointing can have high
	 * rates).
	 * @param sCAxys Frame Spacecraft axys reference frame
	 * @param sCAxysx X Spacecraft Axis
	 * @param sCAxysy Y Spacecraft Axis
	 * @param sCAxysz Z Spacecraft Axis
	 * @param inertialAxysFrame Inertial axis reference frame
	 * @param inertialAxysx X Inertial Axis
	 * @param inertialAxysy Y Inertial axis
	 * @param inertialAxysz Z Inertial axis
	 */
	public PhaseAngle(String sCAxysFrame,float sCAxysx,float sCAxysy,float sCAxysz,String inertialAxysFrame,float inertialAxysx,float inertialAxysy,float inertialAxysz){
		super("phaseAngle","");
		this.addAttribute(new PointingMetadata("ref","align"));
		PointingMetadata child = new PointingMetadata("SCAxis","");
		child.addAttribute(new PointingMetadata("frame",sCAxysFrame));
		child.addChild(new PointingMetadata("x",""+sCAxysx));
		child.addChild(new PointingMetadata("y",""+sCAxysy));
		child.addChild(new PointingMetadata("z",""+sCAxysz));
		this.addChild(child);
		
		PointingMetadata child2 = new PointingMetadata("inertialAxis","");
		child2.addAttribute(new PointingMetadata("frame",inertialAxysFrame));
		child2.addChild(new PointingMetadata("x",""+inertialAxysx));
		child2.addChild(new PointingMetadata("y",""+inertialAxysy));
		child2.addChild(new PointingMetadata("z",""+inertialAxysz));
		this.addChild(child2);


	}
	/**
	 * For Power Optimised Phase Angle, set the angle parameter.
	 * The power optimised roll places the SC-y-axis at a
	 * certain angle relative to the SC to Sun direction.
	 * @param units The units for the angle, for example deg
	 * @param angle The angle parameter, for example 90
	 */
	public void setAngle(String units,String angle){
		PointingMetadata an = (new PointingMetadata("angle",angle));
		an.addAttribute(new PointingMetadata("units",units));
		this.addChild(an);
	}
	/**
	 * For Power Optimised Phase Angle, set the angle parameter.
	 * The power optimised roll places the SC-y-axis at a
	 * certain angle relative to the SC to Sun direction.
	 * @param units The units for the angle, for example deg
	 * @param angle The angle parameter, for example 90
	 */	
	public void setAngle(String units,float angle){
		PointingMetadata an = (new PointingMetadata("angle",angle+""));
		an.addAttribute(new PointingMetadata("units",units));
		this.addChild(an);
	}
	
	/**
	 * Set the flip start time for the flip type PhaseAngle. It is specified in the
	 * <flipStartTime> element. For all type of flips the
	 * flip duration is 60min.

	 * @param time
	 */
	public void setFlipStartTime(Date time){
		this.addChild(new PointingMetadata("flipStartTime",PointingBlock.dateToZulu(time)));
	}

}
