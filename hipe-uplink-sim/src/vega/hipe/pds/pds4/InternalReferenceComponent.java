package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

public class InternalReferenceComponent extends InternalReference{
	public static String LIDVID_REFERENCE="lid_reference";
	public static InternalReferenceComponent getFromNode(Node node){
		InternalReferenceComponent result=new InternalReferenceComponent();
		Element poElement = (Element) node;
		try{
			result.setLidvidReference(((Element) node).getElementsByTagName(LIDVID_REFERENCE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LIDVID_REFERENCE);
		}
		try{
			result.setReferenceType(((Element) node).getElementsByTagName(REFERENCE_TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+REFERENCE_TYPE);
		}
		

			return result;
	}
	
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<"+INTERNAL_REFERENCE+">\n";
		result=result+id+"\t<"+LIDVID_REFERENCE+">"+this.getLidvidReference()+"</"+LIDVID_REFERENCE+">\n";
		result=result+id+"\t<"+REFERENCE_TYPE+">"+this.getReferenceType()+"</"+REFERENCE_TYPE+">\n";

		result=result+id+"</"+INTERNAL_REFERENCE+">\n";
		
		return result;
	}
}
