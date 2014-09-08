package vega.uplink.pointing;


import vega.uplink.pointing.PtrParameters.Boresight;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.PhaseAngle;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.StringParameter;
import herschel.share.interpreter.InterpreterUtil;

/**
 * Basic pointing. For each basic pointing a boresight is aligned with a target defined relative to inertial frame.
 * @author jarenas
 *
 */
public class PointingAttitude extends PointingElement{
	/**
	 * fixed attitude type that was implemented in the same PTR at an earlier time.
	 */
	public static String POINTING_ATTITUDE_TYPE_CAPTURE="capture";
	//public static String POINTING_ATTITUDE_TYPE_DERIVEDPHASEANGLE="capture";
	/**
	 * attitude type pointing the boresight to an illuminated point of the comet
	 */
	public static String POINTING_ATTITUDE_TYPE_ILLUMINATEDPOINT="illuminatedPoint";
	/**
	 * Attitude type where the boresight is aligned with a fixed vector given relative to inertial frame.
	 */
	public static String POINTING_ATTITUDE_TYPE_INERTIAL="inertial";
	/**
	 * Attitude type that points the boresight to an user-selected point relative to the limb of CG.
	 */
	public static String POINTING_ATTITUDE_TYPE_LIMB="limb";
	/**
	 * attitude type that points the boresight to the specular point wrt. Earth on an elliptical surface defined relative to the centre of CG.
	 */
	public static String POINTING_ATTITUDE_TYPE_SPECULAR="specular";
	/**
	 * attitude type that points the boresight to the point on the terminator that is in the comet-sun-SC plane and visible from the SC.
	 */
	public static String POINTING_ATTITUDE_TYPE_TERMINATOR="terminator";
	/**
	 * Attitude type where the target is given by a solar system object or landmark.
	 */
	public static String POINTING_ATTITUDE_TYPE_TRACK="track";
	/**
	 * Attitude type pointing the boresight along the velocity vector of the SC relative to CG
	 */
	public static String POINTING_ATTITUDE_TYPE_VELOCITY="velocity";
	
	public static String ATTITUDE_TAG="attitude";
	public static String REF_TAG="ref";
	public static String POWEROPTIMIZED_TAG="_pwropt";

	public PointingAttitude(PointingElement org){
		super(org);
	}

	/**
	 * Creates a pointing attitude of the given type
	 * @param type type of this attitude
	 * @param boresight Vector defined in SC frame that shall be pointed to the target
	 * @param phaseAngle Rule that fixes the degree of freedom around the boresight.
	 */
	public PointingAttitude(String type,Boresight boresight,PhaseAngle phaseAngle){
		super(ATTITUDE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,type));
		setBoresight(boresight);
		setPhaseAngle(phaseAngle);
	}
	/**
	 * Creates a pointing attitude of the given type
	 * @param type type of this attitude
	 */
	public PointingAttitude(String type){
		super(ATTITUDE_TAG,"");
		this.addAttribute(new PointingElement(REF_TAG,type));
		//setBoresight(new Boresight());
		//setPhaseAngle(new PhaseAngle());
	}
	
	/**
	 * Get the type of this attitude
	 * @return
	 */
	public String getAttitudeType(){
		return this.getAttribute(REF_TAG).getValue();
	}
	
	/**
	 * Set the Vector defined in SC frame that shall be pointed to the target
	 * @param boresight
	 */
	public void setBoresight(Boresight boresight){
		this.addChild(boresight);
	}
	
	/**
	 * Get the Vector defined in SC frame that shall be pointed to the target
	 * @return
	 */
	public Boresight getBoresight(){
		PointingElement result = this.getChild(Boresight.BORESIGHT_TAG);
		if (result!=null){
			return (Boresight) result;
		}else{
			return new Boresight();
		}
	}
	
	/**
	 * Get the rule that fixes the degree of freedom around the boresight.
	 * if the attitude type ends with _pwropt (power optimized) it return the default phase angle
	 * @return
	 */
	public PhaseAngle getPhaseAngle(){
		if (getAttitudeType().endsWith(POWEROPTIMIZED_TAG)){
			PhaseAngle result = new PhaseAngle();
			return result;
			//result.addAttribute(new PointingMetadata("ref","powerOptimised"));
			
		}
		PointingElement result = this.getChild(PhaseAngle.PHASEANGLE_TAG);
		if (result!=null){
			return (PhaseAngle) this.getChild(PhaseAngle.PHASEANGLE_TAG);
		}else{
			return new PhaseAngle();
		}
	}
	/**
	 * Set the rule that fixes the degree of freedom around the boresight.
	 * if the attitude type ends with _pwropt (power optimized) it will not set the phase angle
	 * @param phaseAngle
	 */	
	public void setPhaseAngle(PhaseAngle phaseAngle){
		if (getAttitudeType().endsWith(POWEROPTIMIZED_TAG)) return; //If it is power optimised, then do not add attitude
		
		this.addChild(phaseAngle);
	}
	
	/**
	 * Set the offset reference axis and the offset angles
	 * @param refAxis
	 * @param angles
	 */
	public void SetOffset(OffsetRefAxis refAxis,OffsetAngles angles ){
		this.addChild(refAxis);
		this.addChild(angles);
	}
	
	/**
	 * Get the off set reference axis
	 * @return
	 */
	public OffsetRefAxis getOffsetRefAxis(){
		PointingElement child = this.getChild(OffsetRefAxis.OFFSETREFAXIS_TAG);
		if (child==null) return null;
		if (InterpreterUtil.isInstance(OffsetRefAxis.class, child)){
			return (OffsetRefAxis) child;
		}else return null;
	}
	
	/**
	 * Get the offset angles
	 * @return
	 */
	public OffsetAngles getOffsetAngles(){
		PointingElement child = this.getChild(OffsetAngles.OFFSETANGLES_TAG);
		if (child==null) return null;
		if (InterpreterUtil.isInstance(OffsetAngles.class, child)){
			return (OffsetAngles) child;
		}else return null;
	}

	
	public PointingAttitude copy() {
		PointingAttitude result = new PointingAttitude(getAttitudeType());
		PointingElement[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		
		return result;
	}


}
