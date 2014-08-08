package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

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

	public DirectionVector(String type,String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		this(type);
		this.setFrame(frame);
		this.setLongitude(unitLongitude, longitude);
		this.setLatitude(unitLatitude, latitude);
	}
	public DirectionVector(String type,String frame,float longitude,float latitude){
		this(type,frame,"deg",longitude,"deg",latitude);
	}
	public void setLongitude(String units,float longitude){
		setFloatField("lon",longitude);
		//this.addChild(new PointingMetadata("lon",""+longitude));
		this.setUnit("lon", units);
	}
	public void setLongitude(float longitude){
		setLongitude("deg",longitude);
	}
	public void setLatitude(String units,float latitude){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("lat", latitude);
		this.setUnit("lat", units);
	}
	public void setLatitude(float latitude){
		setLatitude("deg",latitude);
	}
	public float getLatitude(){
		return Float.parseFloat(getChild("lat").getValue());
	}
	public float getLongitude(){
		return Float.parseFloat(getChild("lon").getValue());
	}
	public String getLongitudeUnit(){
		return this.getUnit("lon");
	}
	public String getLatitudeUnit(){
		return this.getUnit("lat");
	}
	//Origin Target
	/*public OriginTarget(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	public DirectionVector(String type,String origin,String target){
		this(type);
		setOrigin(origin);
		setTarget(target);
	}
	public void setOrigin(String origin){
		PointingMetadata originChild = new PointingMetadata("origin","");
		originChild.addAttribute(new PointingMetadata("ref",origin));
		this.addChild(originChild);
	}
	public void setTarget(String target){
		PointingMetadata targetChild = new PointingMetadata("target","");
		targetChild.addAttribute(new PointingMetadata("ref",target));
		this.addChild(targetChild);
		
	}
	
	public String getTarget(){
		return this.getChild("target").getAttribute("ref").getValue();
	}
	public String getOrigin(){
		return this.getChild("origin").getAttribute("ref").getValue();
	}
	//Reference
	/*public Reference(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	public DirectionVector(String type,String reference){
		this(type);
		setReference(reference);
	}
	public void setReference(String reference){
		this.addAttribute(new PointingMetadata("ref",reference));
	}
	
	public String getReference(){
		return this.getAttribute("ref").getValue();
	}
	
	//Rotate
	/*public Rotate(String type,PointingMetadata pm){
		super(type,pm);
	}*/

	public DirectionVector(String type,String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		this(type);
		this.addAttribute(new PointingMetadata("ref","rotate"));
		setAxis(axis);
		setRotationAxis(rotationAxis);
		setRotationAngle(rotationAngleUnit,rotationAngle);
	}
	public DirectionVector(String type,String axis,String rotationAxis,float rotationAngle){
		this(type,axis,rotationAxis,"deg",rotationAngle);
	}
	
	public void setAxis(String axis){
		PointingMetadata axisChild = new PointingMetadata("axis","");
		axisChild.addAttribute(new PointingMetadata("ref",axis));
		this.addChild(axisChild);
	}
	
	public String getAxis(){
		return this.getChild("axis").getAttribute("ref").getValue();
	}
	public void setRotationAxis(String rotationAxis){
		PointingMetadata rotationAxisChild = new PointingMetadata("rotationAxis","");
		rotationAxisChild.addAttribute(new PointingMetadata("ref",rotationAxis));
		this.addChild(rotationAxisChild);
		
	}
	public String getRotationAxis(){
		return this.getChild("rotationAxis").getAttribute("ref").getValue();
	}
	public void setRotationAngle(String angleUnit,float angle){
		this.setFloatField("rotationAngle", angle);
		this.setUnit("rotationAngle", angleUnit);
	}
	public void setRotationAngle(float angle){
		setRotationAngle("deg",angle);
	}
	public float getRotationAngle(){
		return Float.parseFloat(this.getChild("rotationAngle").getValue());
	}
	public String getRotationAngleUnit(){
		return getUnit("rotationAngle");
	}

}
