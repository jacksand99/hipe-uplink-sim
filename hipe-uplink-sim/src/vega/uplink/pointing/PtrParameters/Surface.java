package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Units;

public class Surface extends PointingElement {
	/**
	 * surface
	 */
	public static String SURFACE_TAG="surface";
	public Surface(PointingElement org){
		
		super(org);
	}
	
	public Surface copy(){
		Surface result = new Surface(super.copy());
		return result;
	}
	/**
	 * Creates a surface referenced to the CG surface
	 */
	public Surface(){
		super(SURFACE_TAG,"");
		this.setRef("CG");
	}
	/*public Surface(String surfaceName){
		super(SURFACE_TAG,"");
		setSurfaceName(surfaceName);
	}*/
	/**
	 * Creates a ellipsoid surface.
	 * This representation allows to define a reference ellipsoid, defined by the semi axes a, b and c and
	 * the corresponding axes. The axes are defined
	 * relative to a frame (for example CG). The specified axes shall be mutually orthogonal.
	 * @param frame Frame that this surface is relative to
	 * @param origin Origin of the surface
	 * @param unitsA Units where the semi axe A is expressed
	 * @param a semi axe A
	 * @param unitsB Units where the semi axe B is expressed
	 * @param b semi axe B
	 * @param unitsC Units where the semi axe C is expressed
	 * @param c semi axe C
	 * @param axisAFrame Frame of the axe A
	 * @param axisAX X Component of the axe A
	 * @param axisAY Y Component of the axe A
	 * @param axisAZ Z Component of the axe A
	 * @param axisBFrame Frame of the axe B
	 * @param axisBX X Component of the axe B
	 * @param axisBY Y Component of the axe B
	 * @param axisBZ Z Component of the axe B
	 * @param axisCFrame Frame of the axe C
	 * @param axisCX X Component of the axe C
	 * @param axisCY Y Component of the axe C
	 * @param axisCZ Z Component of the axe C
	 */
	public Surface(String frame,String origin,String unitsA,float a,String unitsB,float b,String unitsC,float c,String axisAFrame,float axisAX,float axisAY,float axisAZ,String axisBFrame,float axisBX,float axisBY,float axisBZ,String axisCFrame,float axisCX,float axisCY,float axisCZ){
		super(SURFACE_TAG,"");
		//setSurfaceName(name);
		this.setFrame(frame);
		this.setOrigin(origin);
		this.setA(unitsA, a);
		this.setB(unitsB, b);
		this.setC(unitsC, c);
		this.setAAxis(axisAFrame, axisAX, axisAY, axisAZ);
		this.setBAxis(axisBFrame, axisBX, axisBY, axisBZ);
		this.setCAxis(axisCFrame, axisCX, axisCY, axisCZ);
		
	}
	/**
	 * Creates a ellipsoid surface.
	 * This representation allows to define a reference ellipsoid, defined by the semi axes a, b and c and
	 * the corresponding axes. The axes are defined
	 * relative to a frame (for example CG). The specified axes shall be mutually orthogonal.
	 * @param frame Frame that this surface is relative to (And the axis A, B and C)
	 * @param origin Origin of the surface
	 * @param units Units where the semi axes A,B and C is expressed
	 * @param a semi axe A
	 * @param b semi axe B
	 * @param c semi axe C
	 * @param axisAX X Component of the axe A
	 * @param axisAY Y Component of the axe A
	 * @param axisAZ Z Component of the axe A
	 * @param axisBX X Component of the axe B
	 * @param axisBY Y Component of the axe B
	 * @param axisBZ Z Component of the axe B
	 * @param axisCX X Component of the axe C
	 * @param axisCY Y Component of the axe C
	 * @param axisCZ Z Component of the axe C
	 */
	public Surface(String frame,String origin,String units,float a,float b,float c,float axisAX,float axisAY,float axisAZ,float axisBX,float axisBY,float axisBZ,float axisCX,float axisCY,float axisCZ){
		this(frame,origin,units,a,units,b,units,c,frame,axisAX,axisAY,axisAZ,frame,axisBX,axisBY,axisBZ,frame,axisCX,axisCY,axisCZ);
	}
	
	
	/**
	 * Set the frame that this surface is relative to.
	 * @param frame
	 */
	public void setFrame(String frame){
		addAttribute(new PointingElement("frame",frame));
	}
	
	/**
	 * Set the origin component of this surface
	 * @param origin
	 */
	public void setOrigin(String origin){
		PointingElement originChild = new PointingElement("origin","");
		originChild.addAttribute(new PointingElement("ref",origin));
		this.addChild(originChild);
	}
	/**
	 * Get the origin component of this surface
	 * @param origin
	 */
	public String getOrigin(){
		return this.getChild("origin").getAttribute("ref").getValue();
	}
	/**
	 * Set the A semi axe component of this surface with units
	 * @param origin
	 */
	public void setA(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("a", value);
		this.setUnit("a", units);
	}
	public float getA(){
		return getFloatField("a");
	}
	public float getA(String unit){
		return Units.convertUnit(getA(), getUnitA(), unit);
	}
	public String getUnitA(){
		return getUnit("a");
	}
	/**
	 * Set the B semi axe component of this surface with units
	 * @param origin
	 */
	public void setB(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("b", value);
		this.setUnit("b", units);
	}
	public float getB(){
		return getFloatField("b");
	}
	public float getB(String unit){
		return Units.convertUnit(getB(), getUnitB(), unit);
	}
	public String getUnitB(){
		return getUnit("b");
	}
	/**
	 * Set the C semi axe component of this surface with units
	 * @param origin
	 */
	public void setC(String units,float value){
		//this.addChild(new PointingMetadata("lat",""+latitude));
		this.setFloatField("c", value);
		this.setUnit("c", units);
	}
	public float getC(){
		return getFloatField("c");
	}
	public float getC(String unit){
		return Units.convertUnit(getC(), getUnitC(), unit);
	}
	public String getUnitC(){
		return getUnit("c");
	}
	/**
	 * Set the a axis of this surface, with the frame it refers to
	 * @param name
	 * @param frame
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setAxis(String name,String frame,float x,float y, float z){
		this.addChild(new DirectionVector(name,frame,x,y,z));
	}
	/**
	 * Set the A axis of this surface, with the frame it refers to
	 * @param frame
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setAAxis(String frame,float x,float y, float z){
		setAxis("axisA",frame,x,y,z);
	}
	/**
	 * Set the B axis of this surface, with the frame it refers to
	 * @param frame
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setBAxis(String frame,float x,float y, float z){
		setAxis("axisB",frame,x,y,z);
	}
	/**
	 * Set the C axis of this surface, with the frame it refers to
	 * @param frame
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setCAxis(String frame,float x,float y, float z){
		setAxis("axisC",frame,x,y,z);
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
	
	public float getFloatField(String field){
		return Float.parseFloat(getChild(field).getValue());
	}
	public String getUnit(String field){
		PointingElement deltaTimesCh = getChild(field);
		if (deltaTimesCh==null) return null;
		if (deltaTimesCh.getAttribute("units")==null) return null;
		return deltaTimesCh.getAttribute("units").getValue();
		
	}
	
	public void setUnit(String field,String unit){
		PointingElement deltaTimesCh = getChild(field);
		//System.out.println(deltaTimesCh);
		if (deltaTimesCh==null) deltaTimesCh=new PointingElement(field,"");
		deltaTimesCh.addAttribute(new PointingElement("units",unit));
		addChild(deltaTimesCh);
		
	}
	/*public String getUnit(String field){
		return getChild(field).getAttribute("units").getValue();
	}*/

	/**
	 * Return the frame to which the Surface is relative
	 * @return Frame to which the vector is relative
	 */
	public String getFrame(){
		return getAttribute("frame").getValue();
	}

	

	/**
	 * Creates a surface referenced to another surface
	 * @param ref Name of the other surface
	 */
	public Surface(String ref){
		this();
		addAttribute(new PointingElement("ref",ref));
	}
	/**
	 * Creates a surface referenced to the CG surface
	 */
	/*public Surface(){
		this("CG");
	}*/

	
	/**
	 * Set the name of the other surface this surface refers to
	 * @param ref
	 */
	public void setRef(String ref){
		this.addAttribute(new PointingElement("ref",ref));
	}
	/**
	 * Get the name of the other surface this surface refers to
	 * @param ref
	 */	
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}

}
