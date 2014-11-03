package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;

public class OffsetRefAxis extends DirectionVector {
	
	/**
	 * offsetRefAxis
	 */
	public static String OFFSETREFAXIS_TAG="offsetRefAxis";
	/**
	 * SC_Xaxis
	 */	
	public static String SC_X_AXIS_TAG="SC_Xaxis";
	
	/**
	 * Creates a offsetRefAxis Direction vector referenced to SC_Xaxis direction vector
	 * Direction vectors can be defined by referencing to
	 * direction vectors that were previously defined and
	 * got a name assigned.
	 * @param reference Name of the referenced direction vector
	 */
	public OffsetRefAxis(){
		//super("offsetRefAxis");
		this(SC_X_AXIS_TAG);
	}
	/**
	 * Creates a offsetRefAxis fixed direction vector expressed in spherical coordinates
	 * For spherical coordinates the element must
	 * contain the child elements <lon> and <lat>. The
	 * cartesian components of the vector are
	 * (cos(lat)*cos(lon), cos(lat)*sin(lon), sin(lat)). 
	 * The unit in which longitude and latitude are written must be deg.
	 * The frame attribute specifies relative to which
	 * frame the direction vector is defined. Possible
	 * values are SC, EME2000 and CG.
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param longitude Longitude expressed in deg.
	 * @param latitude Latitude expressed in deg.
	 */
	public OffsetRefAxis(String frame,float longitude,float latitude){
		super(OFFSETREFAXIS_TAG,frame,longitude,latitude);
	}
	public OffsetRefAxis(PointingElement pm){
		super(OFFSETREFAXIS_TAG,pm);
	}
	
	public OffsetRefAxis copy(){
		OffsetRefAxis result = new OffsetRefAxis(super.copy());
		return result;
	}
	/**
	 * Creates a offsetRefAxis Direction vector referenced to another direction vector
	 * Direction vectors can be defined by referencing to
	 * direction vectors that were previously defined and
	 * got a name assigned.
	 * @param reference Name of the referenced direction vector
	 */
	public OffsetRefAxis(String reference){
		super(OFFSETREFAXIS_TAG,reference);
	}
	/**
	 * Creates a fixed offsetRefAxis direction vector expressed in cartesian coordinates
	 * For cartesian coordinates the element must
	 * contain the child elements <x> <y> and <z> that
	 * contain fixed numbers specifying the
	 * components (that must not all be zero). The
	 * direction vector corresponds to the normalization
	 * of the vector.
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param x The x component of this vector
	 * @param y The y component of this vector
	 * @param z The z component of this vector
	 */
	public OffsetRefAxis(String frame,float x,float y, float z){
		super(OFFSETREFAXIS_TAG,frame,x,y,z);
	}
	/**
	 * Creates a fixed offsetRefAxis direction vector expressed in spherical coordinates
	 * For spherical coordinates the element must
	 * contain the child elements <lon> and <lat>. The
	 * cartesian components of the vector are
	 * (cos(lat)*cos(lon), cos(lat)*sin(lon), sin(lat)). For
	 * spherical coordinates the attribute units must
	 * be provided and must contain a dimension of an
	 * angle.
	 * The frame attribute specifies relative to which
	 * frame the direction vector is defined. Possible
	 * values are SC, EME2000 and CG.
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param unitLongitude Unit in which the longitude is expressed
	 * @param longitude Longitude
	 * @param unitLatitude Unit in which the latitude is expressed
	 * @param latitude Latitude
	 */
	public OffsetRefAxis(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		super(OFFSETREFAXIS_TAG,frame,unitLongitude,longitude,unitLatitude,latitude);
	}
	/**
	 * Creates a offsetRefAxis direction vector based in Origin and Target
	 * This representation is used to define a direction
	 * vector from two state vectors.
	 * Because state vectors are always defined relative
	 * to inertial frame, the resulting direction vector is
	 * defined relative to inertial frame.
	 * @param origin Origin State vector
	 * @param target Target State vector
	 */
	public OffsetRefAxis(String origin,String target){
		super(OFFSETREFAXIS_TAG,origin,target);
	}
	/**
	 * Creates a offsetRefAxis rotated Direction Vector
	 * This representation allows to define a direction
	 * vector by right-handed rotation of the direction
	 * vector axis around rotationAxis by the angle
	 * rotationAngle.
	 * The axes axis and rotationAxis must both
	 * be defined either relative to SC or inertial frame.
	 * @param axis The axis that this vector reference
	 * @param rotationAxis Rotation axis of the vector
	 * @param rotationAngle Rotation angle. It must be expressed in deg.
	 */
	public OffsetRefAxis(String axis,String rotationAxis,float rotationAngle){
		super(OFFSETREFAXIS_TAG,axis,rotationAxis,rotationAngle);
	}
	/**
	 * Creates a offsetRefAxis rotated Direction Vector
	 * This representation allows to define a direction
	 * vector by right-handed rotation of the direction
	 * vector axis around rotationAxis by the angle
	 * rotationAngle.
	 * The axes axis and rotationAxis must both
	 * be defined either relative to SC or inertial frame.
	 * @param axis The axis that this vector reference
	 * @param rotationAxis Rotation axis of the vector
	 * @param rotationAngleUnit Unit in which the angle of rotation is expressed
	 * @param rotationAngle Rotation angle
	 */
	public OffsetRefAxis(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		super(OFFSETREFAXIS_TAG,axis,rotationAxis,rotationAngleUnit,rotationAngle);
		
	}


}
