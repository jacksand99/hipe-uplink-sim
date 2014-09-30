package vega.uplink.pointing;


import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import herschel.ia.numeric.String1d;
import herschel.ia.dataset.ArrayDataset;

/**
 * Class to hold the pointing Metadata
 * @author jarenas
 *
 */
public class PointingMetadata extends PointingElement{
	public static String METADATA_TAG="metadata";
	//public static String COMMENT_TAG="comment";
	public PointingMetadata(){
		super(METADATA_TAG,"");
		set("comments",new ArrayDataset(new String1d()));
	}
	public void addComment(String comment){
		getComments().append(comment);
	}
	
	public String1d getComments(){
		return (String1d)((ArrayDataset)this.get("comments")).getData();
	}
	public int getVstpNumber(){
		PointingElement child = this.getChild("vstpNumber");
		if (child==null) return -1;
		return Integer.parseInt(child.getValue());
	}
	
	/**
	 * Write a XML representation of this element
	 * @param indent The initial indent to be applied to the XML
	 * @return
	 */
	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		PointingElement[] childs=this.getChildren();
		PointingElement[] attr=this.getAttributes();
		String1d comments=this.getComments();
		if (childs.length==0 && comments.length()==0){
			if (attr.length>0 || getValue()!=""){
				result=result+iString+"<"+getName();
				for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
				if(!getValue().equals("")) result=result+">"+getValue()+"</"+getName()+">\n";
				else{
					result=result+"/>\n";
				}
			}
		}else{
			result=result+iString+"<"+getName();
			for (int z=0;z<attr.length;z++) result=result+" "+attr[z].getName()+"='"+attr[z].getValue()+"'";
			result=result+">\n";


			for (int j=0;j<childs.length;j++){
				result=result+childs[j].toXml(indent+1);
			}
			for (int c=0;c<comments.length();c++){
				result=result+iString+"\t<comment>"+comments.get(c)+"</comment>\n";
			}
			result=result+iString+"</"+getName()+">\n";
			
		}
		return result;
	}
	
	public static PointingMetadata readFrom(Node node){
		String name=node.getNodeName().trim();
		if (name.equals("#text")) return null;
		if (name.equals("#comment")) return null;
		//String nodeValue = node.getNodeValue();
		String value=null;
		//if (!node.hasChildNodes()) value=node.getTextContent();
		value=getFirstLevelTextContent(node);
		//if (nodeValue!=null) value=nodeValue.trim();
		if (value==null) value="";
		else value=value.trim();
		PointingMetadata result = new PointingMetadata();

		if (node.hasAttributes()){
			NamedNodeMap att = node.getAttributes();
			int size=att.getLength();
			for (int i=0;i<size;i++){
				String attname=att.item(i).getNodeName().trim();
				String attvalue=att.item(i).getNodeValue().trim();
				if (attname!="" && attvalue!="") result.addAttribute(new PointingElement(attname,attvalue));
			}
		}
		if (node.hasChildNodes()){
			NodeList children = node.getChildNodes();
			int size=children.getLength();
			for (int i=0;i<size;i++){
				if (children.item(i).getNodeName().equals("comment")) result.addComment(getFirstLevelTextContent(children.item(i)));
				else{
					PointingElement child = PointingElement.readFrom(children.item(i));
					if (child!=null) result.addChild(child);
				}
			}			
		}
		return result;
	}



}
