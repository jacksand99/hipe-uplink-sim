package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Primary_Result_Summary class provides a high-level description of the types of products included in the collection or bundle
 * @author jarenas
 *
 */
public class PrimaryResultSummary {
	private String type;
	private String purpose;
	private String data_regime;
	private String processing_level;
	private String description;
	
	public static String TYPE="type";
	public static String PURPOSE="purpose";
	public static String DATA_REGIME="data_regime";
	public static String PROCESSING_LEVEL="processing_level";
	public static String DESCRIPTION="description";
	public static String PRIMARY_RESULT_SUMMARY="Primary_Result_Summary";
	
	public PrimaryResultSummary(){
		type="";
		purpose="";
		data_regime="";
		processing_level="";
		description="";
	}
	
	public static PrimaryResultSummary getFromNode(Node node){
		PrimaryResultSummary result=new PrimaryResultSummary();
		//Element poElement = (Element) node;
		try{
			result.setType(((Element) node).getElementsByTagName(TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+TYPE);
		}
		try{
			result.setPurpose(((Element) node).getElementsByTagName(PURPOSE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+PURPOSE);
		}
		try{
			result.setDataRegime(((Element) node).getElementsByTagName(DATA_REGIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DATA_REGIME);
		}
		try{
			result.setProcessingLevel(((Element) node).getElementsByTagName(PROCESSING_LEVEL).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+PROCESSING_LEVEL);
		}
		try{
			result.setDescription(((Element) node).getElementsByTagName(DESCRIPTION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DESCRIPTION);
		}
			return result;
	}

	
	public String getType(){
		return type;
	}
	
	public String getPurpose(){
		return purpose;
	}
	
	public String getDataRegime(){
		return data_regime;
	}
	
	public String getProcessingLevel(){
		return processing_level;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDescription){
		description=newDescription;
	}
	
	public void setType(String newType){
		type=newType;
	}
	
	public void setPurpose(String newPurpose){
		purpose=newPurpose;
	}
	
	public void setDataRegime(String newDataRegime){
		data_regime=newDataRegime;
	}
	
	public void setProcessingLevel(String newProcessingLevel){
		processing_level=newProcessingLevel;
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
		result=result+id+"<Primary_Result_Summary>\n";
		result=result+id+"\t<type>"+type+"</type>\n";
		result=result+id+"\t<purpose>"+purpose+"</purpose>\n";
		result=result+id+"\t<data_regime>"+data_regime+"</data_regime>\n";
		result=result+id+"\t<processing_level>"+processing_level+"</processing_level>\n";
		result=result+id+"\t<description>"+description+"</description>\n";
		result=result+id+"</Primary_Result_Summary>\n";
		
		return result;
	}
}
	
