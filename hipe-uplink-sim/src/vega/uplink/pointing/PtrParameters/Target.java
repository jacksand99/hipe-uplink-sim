package vega.uplink.pointing.PtrParameters;

import vega.uplink.pointing.PointingMetadata;

/**
 * Fixed vector defined relative to inertial frame, solar system object or landmarks defined in the PDFM
 * @author jarenas
 *
 */
public class Target extends PointingMetadata {
	public Target(PointingMetadata org){
		super(org);
	}

	/**
	 * Creates a new Target refereed to a solar system object or landmarks defined in the PDFM
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public Target(String ref){
		super("target","");
		setRef(ref);

	}
	
	/**
	 * Set the referenced solar system object or landmark
	 * @param ref solar system object or landmarks defined in the PDFM
	 */
	public void setRef(String ref){
		this.addAttribute(new PointingMetadata("ref",ref));		
	}
	
	/**
	 * Get the referenced solar system object or landmark
	 * @return solar system object or landmark defined in the PDFM
	 */
	public String getRef(){
		return this.getAttribute("ref").getValue();
	}
	/**
	 * Target with fixed vector defined relative to the frame
	 * @param frame Frame to which the vector is relative
	 * @param x X Component of the vector
	 * @param y Y Component of the vector
	 * @param z Z Component of the vector
	 */
	public Target(String frame,String x,String y,String z){
		super("target","");
		setFrame(frame);
		setX(Float.parseFloat(x));
		setY(Float.parseFloat(y));
		setZ(Float.parseFloat(z));

	}
	
	/**
	 * Target with fixed vector defined relative to the frame
	 * @param frame Frame to which the vector is relative
	 * @param x X Component of the vector
	 * @param y Y Component of the vector
	 * @param z Z Component of the vector
	 */
	public Target(String frame,float x,float y,float z){
		super("target","");
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
}
