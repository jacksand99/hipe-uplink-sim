package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Observing System Component class references one or more subsystems used to collect data. A subsystem can be an instrument_host, instrument, or any other similar product. Each subsystem is categorized as either a sensor or a source. If the observing system includes both a sensor and a source, Observing System Component occurs twice (once for each type) otherwise it only occurs once.
 * @author jarenas
 *
 */
public class ObservingSystemComponent {
	private String name;
	private String type;
	public String description;
	private InternalReferenceComponent internalReference;
	public static String NAME="name";
	public static String TYPE="type";
	public static String DESCRIPTION="description";
	public static String OBSERVING_SYSTEM_COMMPONENT="Observing_System_Component";
	
	
	public ObservingSystemComponent(){
		name="";
		type="";
		internalReference=new InternalReferenceComponent();
		description="";
	}
	
	public static ObservingSystemComponent getFromNode(Node node){
		ObservingSystemComponent result=new ObservingSystemComponent();
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
			result.setInternalReference(InternalReferenceComponent.getFromNode(((Element) node).getElementsByTagName(InternalReference.INTERNAL_REFERENCE).item(0)));
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
	
	public void setInternalReference(InternalReferenceComponent newInternalReferfence){
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
		result=result+id+"<Observing_System_Component>\n";
		result=result+id+"\t<name>"+name+"</name>\n";
		result=result+id+"\t<type>"+type+"</type>\n";
		result=result+id+"\t<description>"+description+"</description>\n";
		result=result+internalReference.toXml(indent+1);

		result=result+id+"</Observing_System_Component>\n";
		
		return result;
	}
}