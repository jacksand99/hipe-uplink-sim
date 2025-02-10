package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Investigation_Area class provides information about an investigation (mission, observing campaign or other coordinated, large-scale data collection effort).
 * @author jarenas
 *
 */
public class InvestigationArea {
	private String name;
	private String type;
	private InternalReference internalReference;
	public static String NAME="name";
	public static String TYPE="type";
	public static String INVESTIGATION_AREA="Investigation_Area";
	public InvestigationArea(){
		name="";
		type="";
		internalReference=new InternalReference();
	}
	
	public static InvestigationArea getFromNode(Node node){
		InvestigationArea result=new InvestigationArea();
		Element poElement = (Element) node;
		try{
			result.setName(((Element) node).getElementsByTagName(NAME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+NAME+" in "+INVESTIGATION_AREA);
		}
		try{
			result.setType(((Element) node).getElementsByTagName(TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+TYPE+" in "+INVESTIGATION_AREA);
		}
		
		try{
			result.setInternalReference(InternalReference.getFromNode(((Element) node).getElementsByTagName(InternalReference.INTERNAL_REFERENCE).item(0)));
			//result.setType(((Element) node).getElementsByTagName(TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+InternalReference.INTERNAL_REFERENCE+" in "+INVESTIGATION_AREA);
		}
		

			return result;
	}
	
	public String getName(){
		return name;
	}
	
	public String getType(){
		return type;
	}
	
	public InternalReference getInternalReference(){
		return internalReference;
	}
	
	public void setName(String newName){
		name=newName;
	}
	
	public void setType(String newType){
		type=newType;
	}
	
	public void setInternalReference(InternalReference newInternalReferfence){
		internalReference=newInternalReferfence;
	}
	
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<Investigation_Area>\n";
		result=result+id+"\t<name>"+name+"</name>\n";
		result=result+id+"\t<type>"+type+"</type>\n";
		result=result+internalReference.toXml(indent+1);

		result=result+id+"</Investigation_Area>\n";
		
		return result;
	}
}
