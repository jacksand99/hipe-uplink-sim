package vega.uplink.pointing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.pointing.PtrParameters.DirectionVector;

public class PdfmDirVector extends DirectionVector {
	public PdfmDirVector(String name){
		super("dirVector");
		setDirVectorName(name);
	}
	
	/**
	 * Set the name of the surface, that will be used to refer to it.
	 * @param name
	 */
	public void setDirVectorName(String name){
		if (name!=null) this.addChild(new PointingMetadata("dirvectorname",name));
	}
	
	

	/**
	 * Get the name of this surface, that will be used to refer to it.
	 * @return
	 */
	public String getDirVectorName(){
		//System.out.println(this.getChild(childName))
		if (this.getChild("dirvectorname")==null) return null;
		return this.getChild("dirvectorname").getValue();
	}
	
	/**
	 * 
	 * @return True if this surface has name that will be used to refer to it or false if it is an inline definition
	 */
	public boolean hasDirVectorName(){
		if (this.getChild("dirvectorname")==null){
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
		String surfaceName=getDirVectorName();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				if (hasDirVectorName()){
					result=result+iString+"<dirVector name=\""+surfaceName+"\"";
				}
				else result=result+iString+"<dirVector ";
				for (int z=0;z<attr.length;z++){
					if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				}
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			if (hasDirVectorName()){
				result=result+iString+"<dirVector name=\""+surfaceName+"\"";
			}
			else result=result+iString+"<dirVector ";
			for (int z=0;z<attr.length;z++){
				if (!attr[z].getName().equals("name")) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			}
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				if (!childs[j].getName().equals("dirvectorname")) result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</dirVector>\n";
			
		}
		return result;
	}
	
	public static PdfmDirVector readFrom(Node node){
		String name=node.getAttributes().getNamedItem("name").getTextContent();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;


		String value=node.getTextContent();
		if (value==null) value="";
		PdfmDirVector result = new PdfmDirVector(name);

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



	/*public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingMetadata[] childs=this.getChildren();
		PointingMetadata[] attr=this.getAttributes();
		if (childs.length==0){
			if (attr.length>0 || getValue()!=""){
				result=result+iString+"<dirVector name=\""+getName()+"\"";
				for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			result=result+iString+"<dirVector name=\""+getName()+"\"";
			for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			result=result+iString+"</dirVector>\n";
			
		}
		return result;
	}
	
	public static PdfmDirVector readFrom(Node node){
		String name=node.getAttributes().getNamedItem("name").getTextContent();
		//String name=node.getNodeName();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;

		//System.out.println(name);
		//String value="";
		String value=node.getTextContent();
		if (value==null) value="";
		PdfmDirVector result = new PdfmDirVector(name);

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
		//else{
			//value=node.getNodeValue();
			//if (value!="" && value!=null)result.setValue(value);
		//}
		return result;
	}*/


}
