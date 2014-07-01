package vega.uplink.pointing;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PdfmDirVector extends PointingMetadata {
	public PdfmDirVector(String name){
		super(name,"");
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
	}


}
