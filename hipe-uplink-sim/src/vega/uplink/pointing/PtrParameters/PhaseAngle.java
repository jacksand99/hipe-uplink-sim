package vega.uplink.pointing.PtrParameters;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * Rule that fixes the degree of freedom around the boresight.
 * @author jarenas
 *
 */
public class PhaseAngle extends PointingElement {
	/**
	 * yDir
	 */
	static String YDIR="yDir";
	/**
	 * xDir
	 */
	static String XDIR="xDir";
	/**
	 * zDir
	 */
	static String ZDIR="zDir";
	/**
	 * phaseAngle
	 */
	public static String PHASEANGLE_TAG="phaseAngle";
	/**
	 * ref
	 */
	public static String REF_TAG="ref";
	/**
	 * flip
	 */
	public static String FLIP_TAG="flip";
	/**
	 * flipStartTime
	 */
	public static String FLIP_START_TIME_TAG="flipStartTime";
	/**
	 * flipType
	 */
	public static String FLIP_TYPE_TAG="flipType";
	/**
	 * alignYCGSCSunNormal
	 */
	public static String ALIGN_YCGSC_SUN_NORMAL_TAG="alignYCGSCSunNormal";
	/**
	 * refEpoch
	 */
	public static String ALIGN_YCGSC_SUN_NORMAL_START_TIME_TAG="refEpoch";
	/**
	 * deltaTime
	 */
	public static String ALIGN_YCGSC_SUN_NORMAL_DELTA_TIME_TAG="deltaTime";
	/**
	 * powerOptimised
	 */
	public static String POWER_OPTIMIZED_TAG="powerOptimised";
	
	/**
	 * yDir
	 */
	public static String YDIR_TAG="yDir";
	
	/**
	 * align
	 */
	public static String ALIGN_TAG="align";
	
	/**
	 * angle
	 */
	public static String ANGLE_TAG="angle";
	
	/**
	 * units
	 */
	public static String UNITS_TAG="units";
	/**
	 * SCAxis
	 */
	public static String SC_AXIS_TAG="SCAxis";
	/**
	 * x
	 */
	public static String X_TAG="x";
	/**
	 * y
	 */
	public static String Y_TAG="y";
	/**
	 * z
	 */
	public static String Z_TAG="z";
	/**
	 * frame
	 */
	public static String FRAME_TAG="frame";
	/**
	 * inertialAxis
	 */
	public static String INERTIAL_AXIS_TAG="inertialAxis";
	

	public PhaseAngle(PointingElement org){
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
		super(PHASEANGLE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,FLIP_TAG));
		this.addChild(new PointingElement(FLIP_START_TIME_TAG,tRef));
		PointingElement flipType = new PointingElement(FLIP_TYPE_TAG,yRot);
		this.addChild(flipType);
		
	}
	/**
	 * Align the SC Y axis with the normal of CGSC-Sun plane:
	 * The projections of SC +Y-axis and the cross product of the SC to CG and SC to Sun direction
	 * into the plane perpendicular to the boresights are 
	 * aligned. The cross product is calculated for a fixed instant in time that is given by the sum
	 * refEpoch and deltaTime. For that epoch the SC to CG and SC to Sun direction must not be aligned.
	 * @param rEpoch
	 * @param deltaTime
	 * @param deltaTimeUnits
	 */
	public PhaseAngle(java.lang.String rEpoch, String deltaTimeUnits,String deltaTime){
		super(PHASEANGLE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,PhaseAngle.ALIGN_YCGSC_SUN_NORMAL_TAG));
		//this.addAttribute(new PointingElement(PhaseAngle.ALIGN_YCGSC_SUN_NORMAL_TAG,""));
		setReferenceEpoch(rEpoch);
		setDeltaTime(deltaTime,deltaTimeUnits);
	}
	/**
	 * Align the SC Y axis with the normal of CGSC-Sun plane:
	 * The projections of SC +Y-axis and the cross product of the SC to CG and SC to Sun direction
	 * into the plane perpendicular to the boresights are 
	 * aligned. The cross product is calculated for a fixed instant in time that is given by the sum
	 * refEpoch and deltaTime. For that epoch the SC to CG and SC to Sun direction must not be aligned.
	 * @param rEpoch
	 * @param deltaTime
	 * @param deltaTimeUnits
	 */
	public PhaseAngle(Date rEpoch, String deltaTimeUnits,float deltaTime){
		this(PointingBlock.dateToZulu(rEpoch),""+deltaTime,deltaTimeUnits);
	}
	/**
	 * Align the SC Y axis with the normal of CGSC-Sun plane:
	 * The projections of SC +Y-axis and the cross product of the SC to CG and SC to Sun direction
	 * into the plane perpendicular to the boresights are 
	 * aligned. The cross product is calculated for a fixed instant in time that is given by the sum
	 * refEpoch and deltaTime. For that epoch the SC to CG and SC to Sun direction must not be aligned.
	 * The delta time units is assumed to be minutes.
	 * @param rEpoch
	 * @param deltaTime
	 * 
	 */
	public PhaseAngle(Date rEpoch, float deltaTime){
		this(PointingBlock.dateToZulu(rEpoch),""+deltaTime,Units.MINUTES);
	}
	/**
	 * Get the reference epoch for Align the SC Y axis with the normal of CGSC-Sun plane
	 * @return
	 */
	public Date getReferenceEpoch(){
		try{
			return PointingBlock.zuluToDate(this.getChild(ALIGN_YCGSC_SUN_NORMAL_START_TIME_TAG).getValue());
		}catch (Exception e){
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not read date for Reference Epoch: "+this.getChild(FLIP_START_TIME_TAG).getValue()+" "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
	}
	
	/**
	 * For Align the SC Y axis with the normal of CGSC-Sun plane, get the delta time
	 * @return Delta Time
	 */
	public float getDeltaTime(){
		return Float.parseFloat(getChild(ALIGN_YCGSC_SUN_NORMAL_DELTA_TIME_TAG).getValue());
	}
	/**
	 * For Align the SC Y axis with the normal of CGSC-Sun plane, get the delta time in the given unit
	 * @param unit the desired unit
	 * @return Delta Time
	 */
	public float getDeltaTime(String unit){
		return Units.convertUnit(Float.parseFloat(getChild(ALIGN_YCGSC_SUN_NORMAL_DELTA_TIME_TAG).getValue()),getDeltaTimeUnit(),unit);
	}
	
	public String getDeltaTimeUnit(){
		return this.getUnit(ALIGN_YCGSC_SUN_NORMAL_DELTA_TIME_TAG);
	}
	
	public void setDeltaTime(String value,String units){
		PointingElement deltaElement = new PointingElement(PhaseAngle.ALIGN_YCGSC_SUN_NORMAL_DELTA_TIME_TAG,value);
		deltaElement.addAttribute(new PointingElement(UNITS_TAG,units));
		this.addChild(deltaElement);
	}
	public void setDeltaTime(long value,String units){
		setDeltaTime(""+value,units);
	}
	public void setDeltaTime(long value){
		setDeltaTime(""+value,"min");
	}
	
	public void setReferenceEpoch(String epoch){
		this.addChild(new PointingElement(PhaseAngle.ALIGN_YCGSC_SUN_NORMAL_START_TIME_TAG,epoch));
	}
	
	public void setReferenceEpoch(Date epoch){
		setReferenceEpoch(PointingBlock.dateToZulu(epoch));
	}

	public String getUnit(String field){
		PointingElement deltaTimesCh = getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute(UNITS_TAG)==null) return null;
		return deltaTimesCh.getAttribute(UNITS_TAG).getValue();
		
	}
	/**
	 * Get the flip type
	 * @return
	 */
	public String getFlipType(){
		return this.getChild(FLIP_TYPE_TAG).getValue();
	}
	
	public Date getFlipeStartTime(){
		try {
			return PointingBlock.zuluToDate(this.getChild(FLIP_START_TIME_TAG).getValue());
		} catch (Exception e) {
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not read date for Flip Start Time: "+this.getChild(FLIP_START_TIME_TAG).getValue()+" "+e.getMessage());
			iae.initCause(e);
			throw(iae);

			//return null;
			// TODO Auto-generated catch block
			
		}
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
		super(PHASEANGLE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,POWER_OPTIMIZED_TAG));
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
		super(PHASEANGLE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,POWER_OPTIMIZED_TAG));
		setYDir(yDir);
		setAngle(Units.DEGREE,angle);
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
			this.addChild(new PointingElement(YDIR_TAG,"true"));
		}
		else{
			this.addChild(new PointingElement(YDIR_TAG,"false"));
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
		super(PHASEANGLE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,ALIGN_TAG));
		PointingElement child = new PointingElement(SC_AXIS_TAG,"");
		child.addAttribute(new PointingElement(FRAME_TAG,sCAxysFrame));
		child.addChild(new PointingElement(X_TAG,""+sCAxysx));
		child.addChild(new PointingElement(Y_TAG,""+sCAxysy));
		child.addChild(new PointingElement(Z_TAG,""+sCAxysz));
		this.addChild(child);
		
		PointingElement child2 = new PointingElement(INERTIAL_AXIS_TAG,"");
		child2.addAttribute(new PointingElement(FRAME_TAG,inertialAxysFrame));
		child2.addChild(new PointingElement(X_TAG,""+inertialAxysx));
		child2.addChild(new PointingElement(Y_TAG,""+inertialAxysy));
		child2.addChild(new PointingElement(Z_TAG,""+inertialAxysz));
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
		PointingElement an = (new PointingElement(ANGLE_TAG,angle));
		an.addAttribute(new PointingElement(UNITS_TAG,units));
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
		PointingElement an = (new PointingElement(ANGLE_TAG,angle+""));
		an.addAttribute(new PointingElement(UNITS_TAG,units));
		this.addChild(an);
	}
	public float getAngle(){
		return Float.parseFloat(getChild(ANGLE_TAG).getValue());
	}
	public String getAngleUnit(){
		return getChild(ANGLE_TAG).getChild(UNITS_TAG).getValue();
	}
	public float getAngle(String unit){
		return Units.convertUnit(getAngle(), getAngleUnit(), unit);
	}
	
	/**
	 * Set the flip start time for the flip type PhaseAngle. It is specified in the
	 * <flipStartTime> element. For all type of flips the
	 * flip duration is 60min.

	 * @param time
	 */
	public void setFlipStartTime(Date time){
		this.addChild(new PointingElement(FLIP_START_TIME_TAG,PointingBlock.dateToZulu(time)));
	}
	

}
