package vega.hipe.pds.pds4;

import org.w3c.dom.Node;

/**
 * The mission area allows the insertion of mission specific metadata.
 * @author jarenas
 *
 */
public class MissionArea {
	public static String MISSION_AREA="Mission_Area";
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<Mission_Area>\n";


		result=result+id+"</Mission_Area>\n";
		
		return result;
	}
	
	public static MissionArea getFromNode(Node node){
		return new MissionArea();
	}
}
