package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.Units;

/**
 * @author jarenas
 *
 */
/**
 * @author jarenas
 *
 */
public class DirectionVector extends PointingMetadata{
	public DirectionVector(String type,PointingMetadata pm){
		this(type);
		this.copyFrom(pm);
		//super(pm);
	}
	public DirectionVector(String type){
		super(type,"");
	}
	
	public String getUnit(String field){
		PointingMetadata deltaTimesCh = getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute("units")==null) return null;
		return deltaTimesCh.getAttribute("units").getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingMetadata deltaTimesCh = getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingMetadata(field,"");
		deltaTimesCh.addAttribute(new PointingMetadata("units",unit));
		addChild(deltaTimesCh);
		
	}
	
	public void setFloatArrayField(String field,float[] xAngles){
		String sDT="";
		for (int i=0;i<xAngles.length;i++){
			sDT=sDT+xAngles[i]+" ";
		}
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);

	}
	
	public void setFloatField(String field,float value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setIntegerField(String field,int value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setStringField(String field,String value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);
		
	}
	
	public void setBooleanField(String field,boolean value){
		String sDT=""+value;
		PointingMetadata deltaTimesCh;
		deltaTimesCh=getChild(field);
		if (deltaTimesCh==null){
			deltaTimesCh = new PointingMetadata(field,sDT);
		}else{
			deltaTimesCh.setValue(sDT);
		}
		//PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
		addChild(deltaTimesCh);

	}
	
	//Cartesian
	/*public DirectionVector(String type,PointingMetadata pm){
		super(type,pm);
	}*/
	/**
	 * Creates a fixed direction vector expressed in cartesian coordinates
	 * For cartesian coordinates the element must
	 * contain the child elements <x> <y> and <z> that
	 * contain fixed numbers specifying the
	 * components (that must not all be zero). The
	 * direction vector corresponds to the normalization
	 * of the vector.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param x The x component of this vector
	 * @param y The y component of this vector
	 * @param z The z component of this vector
	 */
	public DirectionVector(String type,String frame,float x,float y, float z){
		this(type);
		setFrame(frame);
		setX(x);
		setY(y);
		setZ(z);
	}
	/**
	 * Set the x component of the fixed vector
	 * @param x The x component
	 */
	public void setX(float x){
		this.addChild(new PointingMetadata("x",""+x));
	}

	/**
	 * Set the y component of the fixed vector
	 * @param y The y component
	 */
	public void setY(float y){
		this.addChild(new PointingMetadata("y",""+y));
	}

	/**
	 * Set the z component of the fixed vector
	 * @param z The z component
	 */
	public void setZ(float z){
		this.addChild(new PointingMetadata("z",""+z));
	}
	
	/**
	 * Get the x component of the fixed vector
	 * @return The x component
	 */
	public float getX(){
		return Float.parseFloat(getChild("x").getValue());
	}

	/**
	 * Get the y component of the fixed vector
	 * @return The y component
	 */
	public float getY(){
		return Float.parseFloat(getChild("y").getValue());
	}

	/**
	 * Get the z component of the fixed vector
	 * @return The z component
	 */
	public float getZ(){
		return Float.parseFloat(getChild("z").getValue());
	}

	/**
	 * Return the frame to which the vector is relative
	 * @return Frame to which the vector is relative
	 */
	public String getFrame(){
		return getAttribute("frame").getValue();
	}
	
	/**
	 * Set the frame to which the vector is relative
	 * @param frame Frame to which the vector is relative
	 */
	public void setFrame(String frame){
		addAttribute(new PointingMetadata("frame",frame));
	}
	//Cordinates spherical
	/*public CoordinatesSpherical(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	/**
	 * Creates a fixed direction vector expressed in spherical coordinates
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
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param unitLongitude Unit in which the longitude is expressed
	 * @param longitude Longitude
	 * @param unitLatitude Unit in which the latitude is expressed
	 * @param latitude Latitude
	 */
	public DirectionVector(String type,String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		this(type);
		this.setFrame(frame);
		this.setLongitude(unitLongitude, longitude);
		this.setLatitude(unitLatitude, latitude);
	}
	/**
	 * Creates a fixed direction vector expressed in spherical coordinates
	 * For spherical coordinates the element must
	 * contain the child elements <lon> and <lat>. The
	 * cartesian components of the vector are
	 * (cos(lat)*cos(lon), cos(lat)*sin(lon), sin(lat)). 
	 * The unit in which longitude and latitude are written must be deg.
	 * The frame attribute specifies relative to which
	 * frame the direction vector is defined. Possible
	 * values are SC, EME2000 and CG.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param frame The frame where this vector is expressed (SC,EME2000,CG)
	 * @param longitude Longitude expressed in deg.
	 * @param latitude Latitude expressed in deg.
	 */
	public DirectionVector(String type,String frame,float longitude,float latitude){
		this(type,frame,"deg",longitude,"deg",latitude);
	}
	
	/**
	 * For a direction vector expressed in spherical coordinates, set the longitude
	 * @param units Unit in which the longitude is expressed
	 * @param longitude Longitude
	 */
	public void setLongitude(String units,float longitude){
		setFloatField("lon",longitude);
		//this.addChild(new PointingMetadata("lon",""+longitude));
		this.setUnit("lon", units);
	}
	/**
	 * For a direction vector expressed in spherical coordinates, set the longitude in deg.
	 * @param longitude Longitude in deg.
	 */
	public void setLongitude(float longitude){
		setLongitude("deg",longitude);
	}
	/**
	 * For a direction vector expressed in spherical coordinates, set the latitude
	 * @param units Unit in which the latitude is expressed
	 * @param latitude Latitude
	 */
	public void setLatitude(String units,float latitude){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("lat", latitude);
		this.setUnit("lat", units);
	}
	/**
	 * For a direction vector expressed in spherical coordinates, set the latitude in deg.
	 * @param latitude Latitude in deg.
	 */
	public void setLatitude(float latitude){
		setLatitude("deg",latitude);
	}
	
	/**
	 * For a direction vector expressed in spherical coordinates, get the latitude
	 * @return Latitude
	 */
	public float getLatitude(){
		return Float.parseFloat(getChild("lat").getValue());
	}
	/**
	 * For a direction vector expressed in spherical coordinates, get the latitude in the given unit
	 * @param unit the desired unit
	 * @return Latitude
	 */
	public float getLatitude(String unit){
		return Units.convertUnit(Float.parseFloat(getChild("lat").getValue()),getLatitudeUnit(),unit);
	}

	/**
	 * For a direction vector expressed in spherical coordinates, get the longitude
	 * @return Longitude
	 */
	public float getLongitude(){
		return Float.parseFloat(getChild("lon").getValue());
	}
	/**
	 * For a direction vector expressed in spherical coordinates, get the longitude in the given unit
	 * @parm unit The desired unit
	 * @return Longitude
	 */
	public float getLongitude(String unit){
		return Units.convertUnit(Float.parseFloat(getChild("lon").getValue()),getLongitudeUnit(),unit);
	}

	/**
	 * For a direction vector expressed in spherical coordinates, get the unit where the longitude is expressed.
	 * @return unit where the longitude is expressed
	 */
	public String getLongitudeUnit(){
		return this.getUnit("lon");
	}
	/**
	 * For a direction vector expressed in spherical coordinates, get the unit where the latitude is expressed.
	 * @return unit where the latitude is expressed
	 */
	public String getLatitudeUnit(){
		return this.getUnit("lat");
	}
	//Origin Target
	/*public OriginTarget(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	/**
	 * Creates a state vector based in Origin and Target
	 * This representation is used to define a direction
	 * vector from two state vectors.
	 * Because state vectors are always defined relative
	 * to inertial frame, the resulting direction vector is
	 * defined relative to inertial frame.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param origin Origin State vector
	 * @param target Target State vector
	 */
	public DirectionVector(String type,String origin,String target){
		this(type);
		setOrigin(origin);
		setTarget(target);
	}
	
	/**
	 * For state vector based in Origin and Target, set the Origin
	 * @param origin Origin State vector
	 */
	public void setOrigin(String origin){
		PointingMetadata originChild = new PointingMetadata("origin","");
		originChild.addAttribute(new PointingMetadata("ref",origin));
		this.addChild(originChild);
	}
	/**
	 * For state vector based in Origin and Target, set the Target
	 * @param target Target State vector
	 */	
	public void setTarget(String target){
		PointingMetadata targetChild = new PointingMetadata("target","");
		targetChild.addAttribute(new PointingMetadata("ref",target));
		this.addChild(targetChild);
		
	}
	
	/**
	 * For state vector based in Origin and Target, get the Target
	 * @return Target State vector
	 */
	public String getTarget(){
		return this.getChild("target").getAttribute("ref").getValue();
	}
	/**
	 * For state vector based in Origin and Target, get the Origin
	 * @return Origin State vector
	 */
	public String getOrigin(){
		return this.getChild("origin").getAttribute("ref").getValue();
	}
	//Reference
	/*public Reference(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	/**
	 * Creates a Direction vector referenced to another direction vector
	 * Direction vectors can be defined by referencing to
	 * direction vectors that were previously defined and
	 * got a name assigned.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param reference Name of the referenced direction vector
	 */
	public DirectionVector(String type,String reference){
		this(type);
		setReference(reference);
	}
	
	/**
	 * For referenced direction vectors set the name of the referenced vector
	 * @param reference Name of the referenced vector
	 */
	public void setReference(String reference){
		this.addAttribute(new PointingMetadata("ref",reference));
	}
	
	/**
	 * For referenced direction vectors set the name of the referenced vector
	 * @return Name of the referenced vector
	 */
	public String getReference(){
		return this.getAttribute("ref").getValue();
	}
	
	//Rotate
	/*public Rotate(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	/**
	 * Creates a rotated Direction Vector
	 * This representation allows to define a direction
	 * vector by right-handed rotation of the direction
	 * vector axis around rotationAxis by the angle
	 * rotationAngle.
	 * The axes axis and rotationAxis must both
	 * be defined either relative to SC or inertial frame.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param axis The axis that this vector reference
	 * @param rotationAxis Rotation axis of the vector
	 * @param rotationAngleUnit Unit in which the angle of rotation is expressed
	 * @param rotationAngle Rotation angle
	 */
	public DirectionVector(String type,String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		this(type);
		this.addAttribute(new PointingMetadata("ref","rotate"));
		setAxis(axis);
		setRotationAxis(rotationAxis);
		setRotationAngle(rotationAngleUnit,rotationAngle);
	}
	/**
	 * Creates a rotated Direction Vector
	 * This representation allows to define a direction
	 * vector by right-handed rotation of the direction
	 * vector axis around rotationAxis by the angle
	 * rotationAngle.
	 * The axes axis and rotationAxis must both
	 * be defined either relative to SC or inertial frame.
	 * @param type The type of direction vector (target, targetDir or dirVector)
	 * @param axis The axis that this vector reference
	 * @param rotationAxis Rotation axis of the vector
	 * @param rotationAngle Rotation angle. It must be expressed in deg.
	 */
	public DirectionVector(String type,String axis,String rotationAxis,float rotationAngle){
		this(type,axis,rotationAxis,"deg",rotationAngle);
	}
	
	/**
	 * For rotated direction vector, set the axis this vector reference 
	 * @param axis
	 */
	public void setAxis(String axis){
		PointingMetadata axisChild = new PointingMetadata("axis","");
		axisChild.addAttribute(new PointingMetadata("ref",axis));
		this.addChild(axisChild);
	}
	
	/**
	 * For rotated direction vector, get the axis this vector reference
	 * @return Axis
	 */
	public String getAxis(){
		return this.getChild("axis").getAttribute("ref").getValue();
	}
	
	/**
	 * For rotated direction vector, set the axis this vector rotation axis
	 * @param rotationAxis Rotation Axis
	 */
	public void setRotationAxis(String rotationAxis){
		PointingMetadata rotationAxisChild = new PointingMetadata("rotationAxis","");
		rotationAxisChild.addAttribute(new PointingMetadata("ref",rotationAxis));
		this.addChild(rotationAxisChild);
		
	}
	/**
	 * For rotated direction vector, get the axis this vector rotation axis
	 * @return Rotation Axis
	 */
	public String getRotationAxis(){
		return this.getChild("rotationAxis").getAttribute("ref").getValue();
	}
	/**
	 * For rotated direction vector, set the rotation angle
	 * @param angleUnit Unit in which the angle of rotation is expressed
	 * @param angle Rotation angle
	 */
	public void setRotationAngle(String angleUnit,float angle){
		this.setFloatField("rotationAngle", angle);
		this.setUnit("rotationAngle", angleUnit);
	}
	/**
	 * For rotated direction vector, set the rotation angle
	 * @param angle Rotation angle. The rotation angle unit is set to deg.
	 */
	public void setRotationAngle(float angle){
		setRotationAngle("deg",angle);
	}
	/**
	 * For rotated direction vector, get the rotation angle
	 * @return Rotation angle
	 */
	public float getRotationAngle(){
		return Float.parseFloat(this.getChild("rotationAngle").getValue());
	}
	/**
	 * For rotated direction vector, get the rotation angle in the given unit
	 * @param unit The desired unit
	 * @return Rotation angle
	 */
	public float getRotationAngle(String unit){
		return Units.convertUnit(Float.parseFloat(this.getChild("rotationAngle").getValue()),getRotationAngleUnit(),unit);
	}

	
	/**
	 * For rotated direction vector, get the units where the rotation angle is expressed.
	 * @return
	 */
	public String getRotationAngleUnit(){
		return getUnit("rotationAngle");
	}

}