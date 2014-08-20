package vega.uplink.pointing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PtrParameters.DirectionVector;
import vega.uplink.pointing.PtrParameters.Surface;

/**
 * Within the PDFM it is possible to define the surfaces. 
 * One surface with name CG is predefined and represents a
 * spherical comet with 2km radius. This is the default surface used in Limb pointings.
 * @author jarenas
 *
 */
public class PdfmSurface extends Surface {
	//public static String SURFACE_TAG="surface";
	public PdfmSurface(PointingMetadata org){
		
		super(org);
	}
	public PdfmSurface(){
		super();
		removeAttribute("ref");
		//super(SURFACE_TAG,"");
	}
	public PdfmSurface(String surfaceName){
		super();
		removeAttribute("ref");
		//super(SURFACE_TAG,"");
		setSurfaceName(surfaceName);
	}
	/**
	 * Creates a ellipsoid surface.
	 * This representation allows to define a reference ellipsoid, defined by the semi axes a, b and c and
	 * the corresponding axes. The axes are defined
	 * relative to a frame (for example CG). The specified axes shall be mutually orthogonal.
	 * @param name Name of the surface, that will be used to refer to it.
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
	public PdfmSurface(String name,String frame,String origin,String unitsA,float a,String unitsB,float b,String unitsC,float c,String axisAFrame,float axisAX,float axisAY,float axisAZ,String axisBFrame,float axisBX,float axisBY,float axisBZ,String axisCFrame,float axisCX,float axisCY,float axisCZ){
		super(frame,origin,unitsA,a,unitsB,b,unitsC,c,axisAFrame,axisAX,axisAY,axisAZ,axisBFrame,axisBX,axisBY,axisBZ,axisCFrame,axisCX,axisCY,axisCZ);
		setSurfaceName(name);
		
	}
	/**
	 * Creates a ellipsoid surface.
	 * This representation allows to define a reference ellipsoid, defined by the semi axes a, b and c and
	 * the corresponding axes. The axes are defined
	 * relative to a frame (for example CG). The specified axes shall be mutually orthogonal.
	 * @param name Name of the surface, that will be used to refer to it.
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
	public PdfmSurface(String name,String frame,String origin,String units,float a,float b,float c,float axisAX,float axisAY,float axisAZ,float axisBX,float axisBY,float axisBZ,float axisCX,float axisCY,float axisCZ){
		this(name,frame,origin,units,a,units,b,units,c,frame,axisAX,axisAY,axisAZ,frame,axisBX,axisBY,axisBZ,frame,axisCX,axisCY,axisCZ);
	}
	
	/**
	 * Set the name of the surface, that will be used to refer to it.
	 * @param name
	 */
	public void setSurfaceName(String name){
		if (name!=null) this.addChild(new PointingMetadata("surfacename",name));
	}
	
	

	/**
	 * Get the name of this surface, that will be used to refer to it.
	 * @return
	 */
	public String getSurfaceName(){
		//System.out.println(this.getChild(childName))
		if (this.getChild("surfacename")==null) return null;
		return this.getChild("surfacename").getValue();
	}
	
	/**
	 * 
	 * @return True if this surface has name that will be used to refer to it or false if it is an inline definition
	 */
	public boolean hasSurfaceName(){
		if (this.getChild("surfacename")==null){
			System.out.println("false");
			return false;
		}
		else return true;
	}

	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		String surfaceName=getSurfaceName();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				if (hasSurfaceName()){
					result=result+iString+"<surface name=\""+surfaceName+"\"";
				}
				else result=result+iString+"<surface ";
				for (int z=0;z<attr.length;z++){
					if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				}
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			if (hasSurfaceName()){
				result=result+iString+"<surface name=\""+surfaceName+"\"";
			}
			else result=result+iString+"<surface ";
			for (int z=0;z<attr.length;z++){
				if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			}
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				if (!childs[j].getName().equals("surfacename")) result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</surface>\n";
			
		}
		return result;
	}
	
	public static PdfmSurface readFrom(Node node){
		String name=node.getAttributes().getNamedItem("name").getTextContent();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;


		String value=node.getTextContent();
		if (value==null) value="";
		PdfmSurface result = new PdfmSurface(name);

		if (node.hasAttributes()){
			NamedNodeMap att = node.getAttributes();
			int size=att.getLength();
			for (int i=0;i<size;i++){
				String attname=att.item(i).getNodeName();
				String attvalue=att.item(i).getNodeValue();
				if (!attname.equals("name") && !attname.equals("") && !attvalue.equals("")) result.addAttribute(new PointingMetadata(attname,attvalue));
			}
		}
		if (node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			int size=children.getLength();
			for (int i=0;i<size;i++){
				PointingMetadata child = PointingMetadata.readFrom(children.item(i));
				if (child!=null) result.addChild(child);
			}			
		}
		return result;
	}

}
