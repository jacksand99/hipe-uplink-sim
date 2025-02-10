package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Modification_Detail class provides the details of one round of modification for the product. The first, required, instance of this class documents the date the product was first registered.
 * @author jarenas
 *
 */
public class ModificationDetail {
	private String modification_date;
	private String version_id;
	private String description;
	public static String MODIFICATION_DETAIL="Modification_Detail";
	
	public static String MODIFICATION_DATE="modification_date";
	public static String VERSION_ID="version_id";
	public static String DESCRIPTION="description";
	public ModificationDetail(){
		modification_date="";
		version_id="";
		description="";
	}
	
	public String getModificationDate(){
		return modification_date;
	}
	public String getVersionId(){
		return version_id;
		
	}
	public String getDescription(){
		return description;
	}
	
	public void setModicationDate(String newDate){
		modification_date=newDate;
	}
	public void setVersionId(String newVersionId){
		version_id=newVersionId;
	}
	public void setDescription(String newDescription){
		description=newDescription;
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
		result=result+id+"<Modification_Detail>\n";
		result=result+id+"\t<modification_date>"+modification_date+"</modification_date>\n";
		result=result+id+"\t<version_id>"+version_id+"</version_id>\n";
		result=result+id+"\t<description>"+description+"</description>\n";

		result=result+id+"</Modification_Detail>\n";
		
		return result;
	}
	
	public static ModificationDetail getFromNode(Node node){
		ModificationDetail result=new ModificationDetail();
		Element poElement = (Element) node;
		try{
			result.setModicationDate(((Element) node).getElementsByTagName(MODIFICATION_DATE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+MODIFICATION_DATE);
		}
		try{
			result.setVersionId(((Element) node).getElementsByTagName(VERSION_ID).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+VERSION_ID);
		}
		try{
			result.setDescription(((Element) node).getElementsByTagName(DESCRIPTION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DESCRIPTION);
		}
			return result;
	}
}
