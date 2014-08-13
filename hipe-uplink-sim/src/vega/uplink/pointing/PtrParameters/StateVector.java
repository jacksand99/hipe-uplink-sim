package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

/**
 * 
 * @author jarenas
 *
 */
public class StateVector extends PointingMetadata {
	public static String REF_TAG="ref";
	public static String POSITION_FIELD="position";
	public static String FRAME_FIELD="frame";
	public static String ORIGIN_FIELD="origin";
	public static String X_FIELD="x";
	public static String Y_FIELD="y";
	public static String Z_FIELD="z";
	public static String DEFAULT_UNIT="km";
	public static String LANDMARK_FIELD="landmark";
	public static String UNITS_TAG="units";
	public static String NAME_TAG="name";
	public StateVector(PointingMetadata org){
		super(org);
	}

	/**
	 * Creates a new StateVector refereed to a solar system object or landmarks defined in the PDFM
	 * @param type Type of the stateVector
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public StateVector(String type,String ref){
		super(type,"");
		setRef(ref);

	}
	
	/**
	 * Set the referenced solar system object or landmark
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata(REF_TAG,ref));		
	}
	
	/**
	 * Get the referenced solar system object or landmark
	 * @return solar system object or landmark defined in the PDFM
	 */
	public String getRef(){
		return this.getAttribute(REF_TAG).getValue();
	}
	
	/**
	 * Definition of a state vector that represents a landmark
	 * @param type The type of state vector. Subclasses will harcode this type
	 * @param landmark The name of the landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public StateVector(String type,String landmark,String origin,String frame,String x,String y,String z){
		this(type,origin,frame,x,y,z);
		this.setLandMark(landmark);

	}
	/**
	 * Definition of a state vector that represents a landmark
	 * @param type The type of state vector. Subclasses will harcode this type
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public StateVector(String type,String origin,String frame,String x,String y,String z){
		super(type,"");
		PointingMetadata fraChild = new PointingMetadata(POSITION_FIELD,"");
		fraChild.addAttribute(new PointingMetadata(FRAME_FIELD,frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX(DEFAULT_UNIT,Float.parseFloat(x));
		setY(DEFAULT_UNIT,Float.parseFloat(y));
		setZ(DEFAULT_UNIT,Float.parseFloat(z));

	}
	/**
	 * Definition of a state vector that represents a landmark
	 * @param type The type of state vector. Subclasses will harcode this type
	 * @param landmark The name of the landmark
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public StateVector(String type,String landmark,String origin,String frame,float x,float y,float z){
		this(type,origin,frame,x,y,z);
		setLandMark(landmark);

	}
	/**
	 * Definition of a state vector that represents a landmark
	 * @param type The type of state vector. Subclasses will harcode this type
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param xUnit The unit that the X component of the landmark is expressed
	 * @param x The X component of the position of the landmark.
	 * @param yUnit The unit that the Y component of the landmark is expressed
	 * @param y The Y component of the position of the landmark. 
	 * @param zUnit The unit that the Z component of the landmark is expressed
	 * @param z The Z component of the position of the landmark. 
	 */
	public StateVector(String type,String origin,String frame,String xUnit,float x,String yUnit,float y,String zUnit,float z){
		super(type,"");
		PointingMetadata fraChild = new PointingMetadata(POSITION_FIELD,"");
		fraChild.addAttribute(new PointingMetadata(FRAME_FIELD,frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX(xUnit,x);
		setY(yUnit,y);
		setZ(zUnit,z);
		
	}
	/**
	 * Definition of a state vector that represents a landmark
	 * @param type The type of state vector. Subclasses will harcode this type
	 * @param origin The origin
	 * @param frame The frame where the landmark is defined
	 * @param x The X component of the position of the landmark. It must be expressed in km.
	 * @param y The Y component of the position of the landmark. It must be expressed in km.
	 * @param z The Z component of the position of the landmark. It must be expressed in km.
	 */
	public StateVector(String type,String origin,String frame,float x,float y,float z){
		super(type,"");
		//setLandMark(landmark);
		PointingMetadata fraChild = new PointingMetadata(POSITION_FIELD,"");
		fraChild.addAttribute(new PointingMetadata(FRAME_FIELD,frame));
		this.addChild(fraChild);
		this.setOrigin(origin);
		setX(DEFAULT_UNIT,x);
		setY(DEFAULT_UNIT,y);
		setZ(DEFAULT_UNIT,z);

	}

	/**
	 * Set the origin component of the landmark
	 * @param origin
	 */
	public void setOrigin(String origin){
		PointingMetadata org = new PointingMetadata(ORIGIN_FIELD,"");
		org.addAttribute(new PointingMetadata(REF_TAG,origin));
		this.addChild(org);
	}
	
	/**
	 * Get the origin component of the landmark
	 * @return
	 */
	public String getOrigin(){
		return this.getChild(ORIGIN_FIELD).getAttribute(REF_TAG).getValue();
	}
	private PointingMetadata getPosition(){
		return this.getChild(POSITION_FIELD);
	}
	
	
	/**
	 * Set the X component of the position of the landmark
	 * @param unit The unit that the X component of the position of the landmark is expressed
	 * @param x the X component of the position of the landmark
	 */
	public void setX(String unit,float x){
		getPosition().addChild(new PointingMetadata(X_FIELD,""+x));
		setUnit(X_FIELD,unit);
	}
	/**
	 * Set the Y component of the position of the landmark
	 * @param unit The unit that the Y component of the position of the landmark is expressed
	 * @param y the Y component of the position of the landmark
	 */
	public void setY(String unit,float y){
		getPosition().addChild(new PointingMetadata(Y_FIELD,""+y));
		setUnit(Y_FIELD,unit);
	}
	/**
	 * Set the Z component of the position of the landmark
	 * @param unit The unit that the Z component of the position of the landmark is expressed
	 * @param z the Z component of the position of the landmark
	 */
	public void setZ(String unit,float z){
		getPosition().addChild(new PointingMetadata(Z_FIELD,""+z));
		setUnit(Z_FIELD,unit);
	}
	
	/**
	 * Get the X component of the position of the landmark
	 * @return X component of the position of the landmark
	 */
	public float getX(){
		return Float.parseFloat(getPosition().getChild(X_FIELD).getValue());
	}
	/**
	 * Get the Y component of the position of the landmark
	 * @return Y component of the position of the landmark
	 */
	public float getY(){
		return Float.parseFloat(getPosition().getChild(Y_FIELD).getValue());
	}
	/**
	 * Get the Y component of the position of the landmark
	 * @return Y component of the position of the landmark
	 */
	public float getZ(){
		return Float.parseFloat(getPosition().getChild(Z_FIELD).getValue());
	}
	/**
	 * Get the unit where X component of the position of the landmark is expressed
	 * @return the unit where X component of the position of the landmark is expressed
	 */
	public String getUnitX(){
		return getUnit(X_FIELD);
	}
	/**
	 * Get the unit where Y component of the position of the landmark is expressed
	 * @return the unit where Y component of the position of the landmark is expressed
	 */
	public String getUnitY(){
		return getUnit(Y_FIELD);
	}
	/**
	 * Get the unit where Z component of the position of the landmark is expressed
	 * @return the unit where Z component of the position of the landmark is expressed
	 */
	public String getUnitZ(){
		return getUnit(Z_FIELD);
	}
	/**
	 * Get the name of the landmark
	 * @return the name of the landmark
	 */
	public String getLandMark(){
		return getAttribute(LANDMARK_FIELD).getValue();
	}
	
	public void setLandMark(String name){
		addAttribute(new PointingMetadata(LANDMARK_FIELD,name));
	}
	
	public String getUnit(String field){
		PointingMetadata deltaTimesCh = getPosition().getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute(UNITS_TAG)==null) return null;
		return deltaTimesCh.getAttribute(UNITS_TAG).getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingMetadata deltaTimesCh = getPosition().getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingMetadata(field,"");
		deltaTimesCh.addAttribute(new PointingMetadata(UNITS_TAG,unit));
		getPosition().addChild(deltaTimesCh);
		
	}
	
	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
					result=result+iString+"<"+getName();
					for (int z=0;z<attr.length;z++){
						if (!attr[z].getName().equals(LANDMARK_FIELD)){

							result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
						}else{
							result=result+" "+NAME_TAG+"='"+attr[z].getValue()+"'";
						}
					}
					if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
					else{
						result=result+"/>\n";
					}
			}
		}else{
			result=result+iString+"<"+getName();
			for (int z=0;z<attr.length;z++){
				if (!attr[z].getName().equals(LANDMARK_FIELD)){
					result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				}else{
					result=result+" "+NAME_TAG+"='"+attr[z].getValue()+"'";
				}
			}
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</"+getName()+">\n";
			
		}
		return result;
	}
	




}
