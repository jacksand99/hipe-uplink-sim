package vega.uplink.pointing.PtrParameters;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PointingMetadata;

/**
 * Vector defined in SC frame that shall be pointed to the target
 * @author jarenas
 *
 */
public class Boresight extends DirectionVector{
	/**
	 * Creates a fixed boresight direction vector expressed in the 0,0,1 cartesian coordinates
	 * in the frame SC
	 */
	public Boresight(){
		//super("boresight");
		this("SC",0,0,1);
	}
	/**
	 * Creates a boresight fixed direction vector expressed in spherical coordinates
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
	public Boresight(String frame,float longitude,float latitude){
		super("boresight",frame,longitude,latitude);
	}
	public Boresight(PointingMetadata pm){
		super("boresight",pm);
	}
	/**
	 * Creates a boresight Direction vector referenced to another direction vector
	 * Direction vectors can be defined by referencing to
	 * direction vectors that were previously defined and
	 * got a name assigned.
	 * @param reference Name of the referenced direction vector
	 */
	public Boresight(String reference){
		super("boresight",reference);
	}
	/**
	 * Creates a fixed boresight direction vector expressed in cartesian coordinates
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
	public Boresight(String frame,float x,float y, float z){
		super("boresight",frame,x,y,z);
	}
	/**
	 * Creates a fixed boresight direction vector expressed in spherical coordinates
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
	public Boresight(String frame,String unitLongitude,float longitude,String unitLatitude,float latitude){
		super("boresight",frame,unitLongitude,longitude,unitLatitude,latitude);
	}
	/**
	 * Creates a boresight direction vector based in Origin and Target
	 * This representation is used to define a direction
	 * vector from two state vectors.
	 * Because state vectors are always defined relative
	 * to inertial frame, the resulting direction vector is
	 * defined relative to inertial frame.
	 * @param origin Origin State vector
	 * @param target Target State vector
	 */
	public Boresight(String origin,String target){
		super("boresight",origin,target);
	}
	/**
	 * Creates a boresight rotated Direction Vector
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
	public Boresight(String axis,String rotationAxis,float rotationAngle){
		super("boresight",axis,rotationAxis,rotationAngle);
	}
	/**
	 * Creates a boresight rotated Direction Vector
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
	public Boresight(String axis,String rotationAxis,String rotationAngleUnit,float rotationAngle){
		super("boresight",axis,rotationAxis,rotationAngleUnit,rotationAngle);
		
	}

}
/*public class Boresight extends PointingMetadata {
	public Boresight(PointingMetadata org){
		super(org);
	}

	public Boresight(String frame,String x,String y,String z){
		super("boresight","");
		addAttribute(new PointingMetadata("frame",frame));
		setX(Float.parseFloat(x));
		setY(Float.parseFloat(y));
		setZ(Float.parseFloat(z));
		
		
	}

	public Boresight(String frame,float x,float y,float z){
		super("boresight","");
		addAttribute(new PointingMetadata("frame",frame));
		setX(x);
		setY(y);
		setZ(z);
	}

	
	public float getX(){
		return Float.parseFloat(getChild("x").getValue());
	}
	
	public void setX(float x){
		addChild(new PointingMetadata("x",""+x));
	}

	public float getY(){
		return Float.parseFloat(getChild("y").getValue());
	}

	public void setY(float y){
		addChild(new PointingMetadata("y",""+y));
	}

	public float getZ(){
		return Float.parseFloat(getChild("z").getValue());
	}

	public void setZ(float z){
		addChild(new PointingMetadata("z",""+z));
	}


	public Boresight(String ref){
		super("boresight","");
		addAttribute(new PointingMetadata("ref",ref));
		
	}
	public Boresight(){
		this("SC","0.","0.","1.");
		//this("Nadir_Nav_Boresight");
	}
	
	public String getFrame(){
		return getAttribute("frame").getValue();
	}
	
	
	public Boresight copy() {
		Boresight result = new Boresight();
		result.setValue(getValue());
		PointingMetadata[] ch = getChildren();
		for (int i=0;i<ch.length;i++){
			result.addChild(ch[i]);
		}
		PointingMetadata[] att = getAttributes();
		for (int i=0;i<att.length;i++){
			result.addAttribute(att[i]);
		}
		return result;
	}
	
	public String toDirVectorXml(String dirVectorName){
		int indent=1;
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				result=result+iString+"<"+"dirVector name=\""+dirVectorName+"\"";
				for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			result=result+iString+"<"+"dirVector name=\""+dirVectorName+"\"";
			for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</"+"dirVector"+">\n";
			
		}
		return result;

	}


}*/
