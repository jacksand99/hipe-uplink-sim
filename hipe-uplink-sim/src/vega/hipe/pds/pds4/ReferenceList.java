package vega.hipe.pds.pds4;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The Reference_List class provides lists general references and cross-references for the product. References cited elsewhere in the label need not be repeated here.
 * @author jarenas
 *
 */
public class ReferenceList {
	HashSet<InternalReference> references;
	public static String REFERENCE_LIST="Reference_List";
	
	public ReferenceList(){
		references=new HashSet<InternalReference>();
	}
	
	public static ReferenceList getFromNode(Node node){
		ReferenceList result= new ReferenceList();
		Element poElement = (Element) node;
		NodeList referenceList = poElement.getElementsByTagName(InternalReference.INTERNAL_REFERENCE);
		for (int i=0;i<referenceList.getLength();i++){
			result.addReference(InternalReference.getFromNode(referenceList.item(i)));
		}
		return result;
	}
	
	public void addReference(InternalReference newReference){
		references.add(newReference);
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
		result=result+id+"<Reference_List>\n";

		Iterator<InternalReference> it = references.iterator();
		while (it.hasNext()){
			result=result+it.next().toXml(indent+1);
		}
		result=result+id+"</Reference_List>\n";
		
		return result;
	}
}
