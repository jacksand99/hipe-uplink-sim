package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

/**
 * @author jarenas
 *
 */
public class InitTargetPoint extends PointingElement{
    /**
     * units
     */
    public static String UNITS_TAG="units";
    /**
     * x
     */
    //public static String X_TAG="x";
    public static String INITTARGETPOINT_TAG="initTargetPoint";
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
    public static String COORD_TAG="coord";
    /**
     * ra
     */
    public static String RA_TAG="ra";
    /**
     * dec
     */
    public static String DEC_TAG="dec";
    /**
     * r
     */
    public static String R_TAG="r";
    /**
     * origin
     */
    public static String ORIGIN_TAG="origin";
    /**
     * ref
     */
    public static String REF_TAG="ref";
    /**
     * target
     */
    public static String TARGET_TAG="target";
    /**
     * rotate
     */
    public static String ROTATE_TAG="rotate";
    /**
     * axis
     */
    public static String AXIS_TAG="axis";
    /**
     * rotationAxis
     */
    public static String ROTATION_AXIS_TAG="rotationAxis";
    /**
     * rotationAngle
     */
    public static String ROTATION_ANGLE_TAG="rotationAngle";
    public InitTargetPoint(String type,PointingElement pm){
        this(type);
        this.copyFrom(pm);
        //super(pm);
    }
    
    public InitTargetPoint copy(){
        InitTargetPoint result = new InitTargetPoint(super.copy());
        return result;
    }
    public InitTargetPoint(PointingElement org){
        super(org);
    }
    public InitTargetPoint(String type){
        super(type,"");
    }
    
    public String getUnit(String field){
        PointingElement deltaTimesCh = getChild(field);
        if (deltaTimesCh==null) return null;
        if (deltaTimesCh.getAttribute(UNITS_TAG)==null) return null;
        return deltaTimesCh.getAttribute(UNITS_TAG).getValue();
        
    }
    
    public void setUnit(String field,String unit){
        PointingElement deltaTimesCh = getChild(field);
        //System.out.println(deltaTimesCh);
        if (deltaTimesCh==null) deltaTimesCh=new PointingElement(field,"");
        deltaTimesCh.addAttribute(new PointingElement(UNITS_TAG,unit));
        addChild(deltaTimesCh);
        
    }
    
    public void setFloatArrayField(String field,float[] xAngles){
        String sDT="";
        for (int i=0;i<xAngles.length;i++){
            sDT=sDT+xAngles[i]+" ";
        }
        PointingElement deltaTimesCh;
        deltaTimesCh=getChild(field);
        if (deltaTimesCh==null){
            deltaTimesCh = new PointingElement(field,sDT);
        }else{
            deltaTimesCh.setValue(sDT);
        }
        //PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
        addChild(deltaTimesCh);

    }
    
    public void setFloatField(String field,float value){
        String sDT=""+value;
        PointingElement deltaTimesCh;
        deltaTimesCh=getChild(field);
        if (deltaTimesCh==null){
            deltaTimesCh = new PointingElement(field,sDT);
        }else{
            deltaTimesCh.setValue(sDT);
        }
        //PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
        addChild(deltaTimesCh);
        
    }
    
    public void setIntegerField(String field,int value){
        String sDT=""+value;
        PointingElement deltaTimesCh;
        deltaTimesCh=getChild(field);
        if (deltaTimesCh==null){
            deltaTimesCh = new PointingElement(field,sDT);
        }else{
            deltaTimesCh.setValue(sDT);
        }
        //PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
        addChild(deltaTimesCh);
        
    }
    
    public void setStringField(String field,String value){
        String sDT=""+value;
        PointingElement deltaTimesCh;
        deltaTimesCh=getChild(field);
        if (deltaTimesCh==null){
            deltaTimesCh = new PointingElement(field,sDT);
        }else{
            deltaTimesCh.setValue(sDT);
        }
        //PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
        addChild(deltaTimesCh);
        
    }
    
    public void setBooleanField(String field,boolean value){
        String sDT=""+value;
        PointingElement deltaTimesCh;
        deltaTimesCh=getChild(field);
        if (deltaTimesCh==null){
            deltaTimesCh = new PointingElement(field,sDT);
        }else{
            deltaTimesCh.setValue(sDT);
        }
        //PointingMetadata deltaTimesCh = new PointingMetadata("xRates",sDT);
        addChild(deltaTimesCh);

    }
    


    /**
     * Return the frame to which the vector is relative
     * @return Frame to which the vector is relative
     */
    public String getFrame(){
        return getAttribute(FRAME_TAG).getValue();
    }
    public String getCoord(){
        return getAttribute(COORD_TAG).getValue();
    }
    
    /**
     * Set the frame to which the vector is relative
     * @param frame Frame to which the vector is relative
     */
    public void setFrame(String frame){
        addAttribute(new PointingElement(FRAME_TAG,frame));
    }
    public void setCoord(String coord){
        addAttribute(new PointingElement(COORD_TAG,coord));
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
     * @param unitRa Unit in which the ra is expressed
     * @param ra ra
     * @param unitDec Unit in which the latitude is expressed
     * @param dec dec
     */
    public InitTargetPoint(String type,String frame,String coord,String unitRa,float ra,String unitDec,float dec,String unitR,float r){
        this(type);
        this.setFrame(frame);
        this.setCoord(coord);
        this.setRa(unitRa, ra);
        this.setDec(unitDec, dec);
        this.setR(unitR, r);
    }
    public InitTargetPoint(String frame,String coord,String unitRa,float ra,String unitDec,float dec,String unitR,float r){
        this(InitTargetPoint.INITTARGETPOINT_TAG);
        this.setFrame(frame);
        this.setCoord(coord);
        this.setRa(unitRa, ra);
        this.setDec(unitDec, dec);
        this.setR(unitR, r);
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
     * @param ra Ra expressed in deg.
     * @param dec Dec expressed in deg.
     */
    public InitTargetPoint(String type,String frame,String coord,float ra,float dec,float r){
        this(type,frame,coord,Units.DEGREE,ra,Units.DEGREE,dec,Units.KILOMETER,r);
    }
    public InitTargetPoint(String frame,String coord,float ra,float dec,float r){
        this(InitTargetPoint.INITTARGETPOINT_TAG,frame,coord,Units.DEGREE,ra,Units.DEGREE,dec,Units.KILOMETER,r);
    }
    
    /**
     * For a direction vector expressed in spherical coordinates, set the longitude
     * @param units Unit in which the longitude is expressed
     * @param ra Ra
     */
    public void setRa(String units,float ra){
        setFloatField(RA_TAG,ra);
        //this.addChild(new PointingMetadata("lon",""+longitude));
        this.setUnit(RA_TAG, units);
    }
    /**
     * For a direction vector expressed in spherical coordinates, set the longitude in deg.
     * @param ra Ra in deg.
     */
    public void setRa(float ra){
        setRa(Units.DEGREE,ra);
    }
    /**
     * For a direction vector expressed in spherical coordinates, set the latitude
     * @param units Unit in which the latitude is expressed
     * @param latitude Latitude
     */
    public void setDec(String units,float dec){
        //this.addChild(new PointingMetadata("lat",""+latitude));
        this.setFloatField(DEC_TAG, dec);
        this.setUnit(DEC_TAG, units);
    }
    public void setR(String units,float r){
        //this.addChild(new PointingMetadata("lat",""+latitude));
        this.setFloatField(R_TAG, r);
        this.setUnit(R_TAG, units);
    }
    /**
     * For a direction vector expressed in spherical coordinates, set the latitude in deg.
     * @param latitude Latitude in deg.
     */
    public void setDec(float dec){
        setDec(Units.DEGREE,dec);
    }
    public void setR(float r){
        setDec(Units.KILOMETER,r);
    }
    
    /**
     * For a direction vector expressed in spherical coordinates, get the latitude
     * @return Latitude
     */
    public float getRa(){
        return Float.parseFloat(getChild(RA_TAG).getValue());
    }
    /**
     * For a direction vector expressed in spherical coordinates, get the latitude in the given unit
     * @param unit the desired unit
     * @return Latitude
     */
    public float getRa(String unit){
        return Units.convertUnit(Float.parseFloat(getChild(RA_TAG).getValue()),getRaUnit(),unit);
    }

    /**
     * For a direction vector expressed in spherical coordinates, get the longitude
     * @return Longitude
     */
    public float getDec(){
        return Float.parseFloat(getChild(DEC_TAG).getValue());
    }
    /**
     * For a direction vector expressed in spherical coordinates, get the longitude in the given unit
     * @parm unit The desired unit
     * @return Longitude
     */
    public float getDec(String unit){
        return Units.convertUnit(Float.parseFloat(getChild(DEC_TAG).getValue()),getDecUnit(),unit);
    }
    public float getR(String unit){
        return Units.convertUnit(Float.parseFloat(getChild(R_TAG).getValue()),getRUnit(),unit);
    }
    /**
     * For a direction vector expressed in spherical coordinates, get the unit where the longitude is expressed.
     * @return unit where the longitude is expressed
     */
    public String getRaUnit(){
        return this.getUnit(RA_TAG);
    }
    /**
     * For a direction vector expressed in spherical coordinates, get the unit where the latitude is expressed.
     * @return unit where the latitude is expressed
     */
    public String getDecUnit(){
        return this.getUnit(DEC_TAG);
    }
    public String getRUnit(){
        return this.getUnit(R_TAG);
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
    public InitTargetPoint(String type,String origin,String target){
        this(type);
        setOrigin(origin);
        setTarget(target);
    }
    
    /**
     * For state vector based in Origin and Target, set the Origin
     * @param origin Origin State vector
     */
    public void setOrigin(String origin){
        PointingElement originChild = new PointingElement(ORIGIN_TAG,"");
        originChild.addAttribute(new PointingElement(REF_TAG,origin));
        this.addChild(originChild);
    }
    /**
     * For state vector based in Origin and Target, set the Target
     * @param target Target State vector
     */ 
    public void setTarget(String target){
        PointingElement targetChild = new PointingElement(TARGET_TAG,"");
        targetChild.addAttribute(new PointingElement(REF_TAG,target));
        this.addChild(targetChild);
        
    }
    
    /**
     * For state vector based in Origin and Target, get the Target
     * @return Target State vector
     */
    public String getTarget(){
        return this.getChild(TARGET_TAG).getAttribute(REF_TAG).getValue();
    }
    /**
     * For state vector based in Origin and Target, get the Origin
     * @return Origin State vector
     */
    public String getOrigin(){
        return this.getChild(ORIGIN_TAG).getAttribute(REF_TAG).getValue();
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
    public InitTargetPoint(String type,String reference){
        this(type);
        setReference(reference);
    }
    
    /**
     * For referenced direction vectors set the name of the referenced vector
     * @param reference Name of the referenced vector
     */
    public void setReference(String reference){
        this.addAttribute(new PointingElement(REF_TAG,reference));
    }
    
    /**
     * For referenced direction vectors set the name of the referenced vector
     * @return Name of the referenced vector
     */
    public String getReference(){
        return this.getAttribute(REF_TAG).getValue();
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
    public InitTargetPoint(String type,String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
        this(type);
        this.addAttribute(new PointingElement(REF_TAG,ROTATE_TAG));
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
    public InitTargetPoint(String type,String axis,String rotationAxis,float rotationAngle){
        this(type,axis,rotationAxis,Units.DEGREE,rotationAngle);
    }
    
    /**
     * For rotated direction vector, set the axis this vector reference 
     * @param axis
     */
    public void setAxis(String axis){
        PointingElement axisChild = new PointingElement(AXIS_TAG,"");
        axisChild.addAttribute(new PointingElement(REF_TAG,axis));
        this.addChild(axisChild);
    }
    
    /**
     * For rotated direction vector, get the axis this vector reference
     * @return Axis
     */
    public String getAxis(){
        return this.getChild(AXIS_TAG).getAttribute(REF_TAG).getValue();
    }
    
    /**
     * For rotated direction vector, set the axis this vector rotation axis
     * @param rotationAxis Rotation Axis
     */
    public void setRotationAxis(String rotationAxis){
        PointingElement rotationAxisChild = new PointingElement(ROTATION_AXIS_TAG,"");
        rotationAxisChild.addAttribute(new PointingElement(REF_TAG,rotationAxis));
        this.addChild(rotationAxisChild);
        
    }
    /**
     * For rotated direction vector, get the axis this vector rotation axis
     * @return Rotation Axis
     */
    public String getRotationAxis(){
        return this.getChild(ROTATION_AXIS_TAG).getAttribute(REF_TAG).getValue();
    }
    /**
     * For rotated direction vector, set the rotation angle
     * @param angleUnit Unit in which the angle of rotation is expressed
     * @param angle Rotation angle
     */
    public void setRotationAngle(String angleUnit,float angle){
        this.setFloatField(ROTATION_ANGLE_TAG, angle);
        this.setUnit(ROTATION_ANGLE_TAG, angleUnit);
    }
    /**
     * For rotated direction vector, set the rotation angle
     * @param angle Rotation angle. The rotation angle unit is set to deg.
     */
    public void setRotationAngle(float angle){
        setRotationAngle(Units.DEGREE,angle);
    }
    /**
     * For rotated direction vector, get the rotation angle
     * @return Rotation angle
     */
    public float getRotationAngle(){
        return Float.parseFloat(this.getChild(ROTATION_ANGLE_TAG).getValue());
    }
    /**
     * For rotated direction vector, get the rotation angle in the given unit
     * @param unit The desired unit
     * @return Rotation angle
     */
    public float getRotationAngle(String unit){
        return Units.convertUnit(Float.parseFloat(this.getChild(ROTATION_ANGLE_TAG).getValue()),getRotationAngleUnit(),unit);
    }

    
    /**
     * For rotated direction vector, get the units where the rotation angle is expressed.
     * @return
     */
    public String getRotationAngleUnit(){
        return getUnit(ROTATION_ANGLE_TAG);
    }
    

}
