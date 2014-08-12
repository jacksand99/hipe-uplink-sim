package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PdfmSurface;
import vega.uplink.pointing.PointingMetadata;

public class Surface extends PdfmSurface {
	public Surface(PointingMetadata org){
		super(org);
	}

	/**
	 * Creates a surface referenced to another surface
	 * @param ref Name of the other surface
	 */
	public Surface(String ref){
		super();
		super.addAttribute(new PointingMetadata("ref",ref));
	}
	/**
	 * Creates a surface referenced to the CG surface
	 */
	public Surface(){
		this("CG");
	}
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
		super(null,frame,origin,unitsA,a,unitsB,b,unitsC,c,axisAFrame,axisAX,axisAY,axisAZ,axisBFrame,axisBX,axisBY,axisBZ,axisCFrame,axisCX,axisCY,axisCZ);
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
	 * Set the name of the other surface this surface refers to
	 * @param ref
	 */
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));
	}
	/**
	 * Get the name of the other surface this surface refers to
	 * @param ref
	 */	
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}

}
