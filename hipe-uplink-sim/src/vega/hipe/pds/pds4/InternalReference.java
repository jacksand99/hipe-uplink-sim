package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Internal_Reference class is used to cross-reference other products in the PDS registry system.
 * @author jarenas
 *
 */
public class InternalReference {
	private String lidvid_reference;
	private String reference_type;
	public static String LIDVID_REFERENCE="lidvid_reference";
	public static String REFERENCE_TYPE="reference_type";
	public static String INTERNAL_REFERENCE="Internal_Reference";
	
	
	public InternalReference(){
		lidvid_reference="";
		reference_type="";
	}
	
	public static InternalReference getFromNode(Node node){
		InternalReference result=new InternalReference();
		Element poElement = (Element) node;
		try{
			result.setLidvidReference(((Element) node).getElementsByTagName(LIDVID_REFERENCE).item(0).getTextContent().trim());
			try{
				result.setReferenceType(((Element) node).getElementsByTagName(REFERENCE_TYPE).item(0).getTextContent().trim());
			}catch (Exception e){
				VegaLog.info("Could not get "+REFERENCE_TYPE);
			}

		}catch (Exception e){
			VegaLog.info("Could not get "+LIDVID_REFERENCE);
			return InternalReferenceComponent.getFromNode(node);
		}
		

			return result;
	}


	public InternalReference(String lidvidReference,String referenceType){
		lidvid_reference=lidvidReference;
		reference_type=referenceType;
		
	}
	
	public String getLidvidReference(){
		return lidvid_reference;
	}
	
	public String getReferenceType(){
		return reference_type;
	}
	
	public void setLidvidReference(String newLidvidReference){
		lidvid_reference=newLidvidReference;
	}
	
	public void setReferenceType(String newReferenceType){
		reference_type=newReferenceType;
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
		result=result+id+"<"+INTERNAL_REFERENCE+">\n";
		result=result+id+"\t<"+LIDVID_REFERENCE+">"+lidvid_reference+"</"+LIDVID_REFERENCE+">\n";
		result=result+id+"\t<"+REFERENCE_TYPE+">"+reference_type+"</"+REFERENCE_TYPE+">\n";

		result=result+id+"</"+INTERNAL_REFERENCE+">\n";
		
		return result;
	}
}
