package vega.hipe.pds.pds4;

import org.w3c.dom.Node;

/**
 * The Discipline area allows the insertion of discipline specific metadata.
 * @author jarenas
 *
 */
public class DisciplineArea {
	public static String DISCIPLINE_AREA="Discipline_Area";
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<"+DISCIPLINE_AREA+">\n";


		result=result+id+"</"+DISCIPLINE_AREA+">\n";
		
		return result;
	}
	
	public static DisciplineArea getFromNode(Node node){
		return new DisciplineArea();
	}
	
}
