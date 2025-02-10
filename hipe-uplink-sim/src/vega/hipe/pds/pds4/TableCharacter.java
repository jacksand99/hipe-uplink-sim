package vega.hipe.pds.pds4;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Table Character class is an extension of table base and defines a simple character table.
 * @author jarenas
 *
 */
public class TableCharacter implements TableBase{
	private String local_identifier;
	private String offset;
	private String offset_unit;
	private String records;
	private String record_delimiter;
	private RecordCharacter recordCharacter;
	public static String LOCAL_IDENTIFIER="local_identifier";
	public static String OFFSET="offset";
	public static String UNIT="unit";
	public static String RECORDS="records";
	public static String RECORD_DELIMITER="record_delimiter";
	public static String TABLE_CHARACTER="Table_Character";
	
	public TableCharacter(){
		local_identifier="";
		offset="";
		offset_unit="";
		records="";
		record_delimiter="";
		recordCharacter=new RecordCharacter();	
	}
	
	public static TableCharacter getFromNode(Node node){
		TableCharacter result=new TableCharacter();
		Element poElement = (Element) node;
		try{
			result.setLocalIdentifier(poElement.getElementsByTagName(LOCAL_IDENTIFIER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LOCAL_IDENTIFIER);
			e.printStackTrace();
		}
		try{
			result.setOffset(((Element) node).getElementsByTagName(OFFSET).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+OFFSET);
		}
		try{
			result.setOffsetUnit(((Element) node).getElementsByTagName(OFFSET).item(0).getAttributes().getNamedItem(UNIT).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+OFFSET+" "+UNIT);
		}

		try{
			result.setRecords(((Element) node).getElementsByTagName(RECORDS).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RECORDS);
		}
		
		try{
			result.setRecordDelimiter(((Element) node).getElementsByTagName(RECORD_DELIMITER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RECORD_DELIMITER);
		}
		
		try{
			result.setRecordCharacter(RecordCharacter.getFromNode(((Element) node).getElementsByTagName(RecordCharacter.RECORD_CHARACTER).item(0)));
			//result.setRecordDelimiter(((Element) node).getElementsByTagName(RecordCharacter.RECORD_CHARACTER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RecordCharacter.RECORD_CHARACTER);
		}		
		/*try{
			result.setTableCharacter(((Element) node).getElementsByTagName(TABLE_CHARACTER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+TABLE_CHARACTER);
		}*/
		
		
		return result;
	}
	
	public String getLocalIdentifier(){
		return local_identifier;
	}
	
	public void setLocalIdentifier(String newLocalIdentifier){
		local_identifier=newLocalIdentifier;
	}
	
	
	public String getOffset(){
		return offset;
	}
	
	public void setOffset(String newOffset){ 
		offset=newOffset;
	}
	
	public String getOffsetUnit(){
		return offset_unit;
	}
	
	public void setOffsetUnit(String newOffsetUnit){
		offset_unit=newOffsetUnit;
	}
	
	public String getRecords(){
		return records;
	}
	
	public void setRecords(String newRecords){
		records=newRecords;
	}
	
	public String getRecordDelimiter(){
		return record_delimiter;
	}
	
	public void setRecordDelimiter(String newRecordDelimiter){
		record_delimiter=newRecordDelimiter;
	}
	
	public RecordCharacter getRecordCharacter(){
		return recordCharacter;
	}
	
	public void setRecordCharacter(RecordCharacter newRecordCharacter){
		recordCharacter=newRecordCharacter;
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
		result=result+id+"<Table_Character>\n";
		result=result+id+"\t<local_identifier>"+local_identifier+"</local_identifier>\n";
		result=result+id+"\t<records>"+records+"</records>\n";
		result=result+id+"\t<record_delimiter>"+record_delimiter+"</record_delimiter>\n";
		result=result+id+"\t<offset unit=\""+offset_unit+"\">"+offset+"</offset>\n";
		result=result+recordCharacter.toXml(indent+1);
		
		result=result+id+"</Table_Character>\n";
		
		return result;
	}
}
