package vega.hipe.pds.pds4;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;

/**
 * The Observing System class describes the entire suite used to collect the data.
 * @author jarenas
 *
 */
public class ObservingSystem {
	private String name;
	public static String NAME="name";
	private HashSet<ObservingSystemComponent> components;
	public static String OBSERVING_SYSTEM="Observing_System";
	public static ObservingSystem getFromNode(Node node){
		ObservingSystem result = new ObservingSystem();
		Element poElement = (Element) node;
		try{
			result.setName(((Element) node).getElementsByTagName(NAME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+NAME);
		}
		try{
			NodeList nListObsComponent = poElement.getElementsByTagName(ObservingSystemComponent.OBSERVING_SYSTEM_COMMPONENT);
			for (int i=0;i<nListObsComponent.getLength();i++){
				result.addObservingSystemComponent(ObservingSystemComponent.getFromNode(nListObsComponent.item(i)));
			}
		}catch (Exception e){
			
		}
		return result;
	}
	public ObservingSystem(){
		name="";
		components=new HashSet<ObservingSystemComponent>();
	}
	public String getName(){
		return name;
	}
	public void setName(String newName){
		name=newName;
	}
	
	public void addObservingSystemComponent(ObservingSystemComponent newComponent){
		components.add(newComponent);
	}
	
	public ObservingSystemComponent[] getObservingSystemComponent(){
		ObservingSystemComponent[] result=new ObservingSystemComponent[components.size()];
		return components.toArray(result);
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
		result=result+id+"<Observing_System>\n";
		result=result+id+"\t<name>"+name+"</name>\n";
		Iterator<ObservingSystemComponent> it = components.iterator();
		while (it.hasNext()){
			result=result+it.next().toXml(indent+1);
		}
		result=result+id+"<Observing_System>\n";		
		
		return result;
	}
}
