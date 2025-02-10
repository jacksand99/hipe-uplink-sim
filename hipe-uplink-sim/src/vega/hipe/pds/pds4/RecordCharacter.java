package vega.hipe.pds.pds4;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;

/**
 * The Record_Character class is a component of the table class and defines a record of the table.
 * @author jarenas
 *
 */
public class RecordCharacter {
	private String fields;
	private String groups;
	private String record_length;
	private String record_length_unit;
	private HashSet<FieldCharacter> fieldCharacters;
	public static String FIELDS="fields";
	public static String GROUPS="groups";
	public static String RECORD_LENGTH="record_length";
	public static String UNIT="unit";
	public static String RECORD_CHARACTER="Record_Character";
	
	public RecordCharacter(){
		fields="";
		groups="";
		record_length="";
		record_length_unit="";
		fieldCharacters=new HashSet<FieldCharacter>();
	}
	
	public static RecordCharacter getFromNode(Node node){
		RecordCharacter result=new RecordCharacter();
		//Element poElement = (Element) node;
		try{
			result.setFields(((Element) node).getElementsByTagName(FIELDS).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELDS);
		}
		try{
			result.setGroups(((Element) node).getElementsByTagName(GROUPS).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+GROUPS);
		}
		try{
			result.setRecordLenght(((Element) node).getElementsByTagName(RECORD_LENGTH).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RECORD_LENGTH);
		}
		try{
			result.setRecordLenghtUnit(((Element) node).getElementsByTagName(RECORD_LENGTH).item(0).getAttributes().getNamedItem(UNIT).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RECORD_LENGTH+" "+UNIT);
		}
		try{
			NodeList nList=((Element) node).getElementsByTagName(FieldCharacter.FIELD_CHARACTER);
			for (int i=0;i<nList.getLength();i++){
				result.addFieldCharacter(FieldCharacter.getFromNode(nList.item(i)));
			}
		}catch (Exception e){
			VegaLog.info("Could not get "+FieldCharacter.FIELD_CHARACTER);
		}

		return result;
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
		result=result+id+"<Record_Character>\n";
		result=result+id+"\t<fields>"+fields+"</fields>\n";
		result=result+id+"\t<groups>"+groups+"</groups>\n";
		result=result+id+"\t<record_length unit=\""+record_length_unit+"\">"+record_length+"</record_length>\n";
		Iterator<FieldCharacter> it = fieldCharacters.iterator();
		while (it.hasNext()){
			result=result+it.next().toXml(indent+1);
		}
		
		result=result+id+"</Record_Character>\n";
		
		return result;
	}
	
	public void addFieldCharacter(FieldCharacter newFieldCharacter){
		fieldCharacters.add(newFieldCharacter);
	}
	
	public String getFields(){
		return fields;
	}
	
	public void setFields(String newFields){
		fields=newFields;
	}
	
	public String getGroups(){
		return groups;
	}
	
	public void setGroups(String newGroups){
		groups=newGroups;
	}
	
	public String getRecordLenght(){
		return record_length;
	}
	
	public void setRecordLenght(String newRecordLenght){
		record_length=newRecordLenght;
	}
	
	public String getRecordLenghtUnit(){
		return record_length_unit;
	}
	
	public void setRecordLenghtUnit(String newRecordLenghtUnit){
		record_length_unit=newRecordLenghtUnit;
	}
}
