package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Target_Identification class provides detailed target identification information.
 * @author jarenas
 *
 */
public class TargetIdentification {
	private String name;
	private String type;
	public String description;
	private InternalReference internalReference;
	public static String NAME="name";
	public static String TYPE="type";
	public static String DESCRIPTION="description";
	public static String TARGET_IDENTIFICATION="Target_Identification";
	
	public TargetIdentification(){
		name="";
		type="";
		internalReference=new InternalReference();
		description="";
	}
	
	public static TargetIdentification getFromNode(Node node){
		TargetIdentification result=new TargetIdentification();
		Element poElement = (Element) node;
		try{
			result.setName(((Element) node).getElementsByTagName(NAME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+NAME);
		}
		try{
			result.setType(((Element) node).getElementsByTagName(TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+TYPE);
		}
		try{
			result.setDescription(((Element) node).getElementsByTagName(DESCRIPTION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DESCRIPTION);
		}
		
		try{
			result.setInternalReference(InternalReference.getFromNode(((Element) node).getElementsByTagName(InternalReference.INTERNAL_REFERENCE).item(0)));
		}catch (Exception e){
			VegaLog.info("Could not get "+InternalReference.INTERNAL_REFERENCE);
		}
		

			return result;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDescription){
		description=newDescription;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String newName){
		name=newName;
	}
	
	public String getType(){
		return type;
	}
	
	public InternalReference getInternalReference(){
		return internalReference;
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
		result=result+id+"<Target_Identification>\n";
		result=result+id+"\t<name>"+name+"</name>\n";
		result=result+id+"\t<type>"+type+"</type>\n";
		result=result+id+"\t<description>"+description+"</description>\n";
		result=result+internalReference.toXml(indent+1);

		result=result+id+"</Target_Identification>\n";
		
		return result;
	}
}
