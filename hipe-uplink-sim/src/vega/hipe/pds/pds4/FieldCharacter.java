package vega.hipe.pds.pds4;

import herschel.ia.gui.plot.renderer.tick.DateInterval.Unit;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import sun.management.counter.Units;
import vega.hipe.logging.VegaLog;

/**
 * The Field_Character class defines a field of a character record or a field of a character group.
 * @author jarenas
 *
 */
public class FieldCharacter {
	private String name;
	private String field_number;
	private String field_location;
	private String field_location_unit;
	private String data_type;
	private String field_length;
	private String field_length_unit;
	private String field_format;
	private String description;
	private String scaling_factor;
	private String unit;
	/**
	 * The name attribute provides a word or combination of words by which the object is known.
	 * 
	 * Type: UTF8_Short_String_Collapsed
	 * Class Name: Field_Character
	 * Minimum Characters: 1
	 * Maximum Characters: 255
	 * Nillable: false
	 * Attribute Concept: Name
	 * Conceptual Domain: Short_String
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String NAME="name";
	
	/**
	 * The scaling_factor attribute is the scaling factor to be applied to each stored value in order to recover an original value. The observed value (Ov) is calculated from the stored value (Sv) thus: Ov = (Sv * scaling_factor) + value_offset. The default value is 1.
	 * 
	 * Type: ASCII_Real
	 * Class Name: Field_Character
	 * Nillable: false
	 * Attribute Concept: Factor
	 * Conceptual Domain: Real
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String SCALING_FACTOR="scaling_factor";
	public static String FIELD_NUMBER="field_number";
	/**
	 * The field_location attribute provides the starting byte for a field within a record or group, counting from '1'.
	 * 
	 * Type: ASCII_Integer
	 * 
	 * Unit of Measure Type: Units_of_Storage
	 * Valid Units: byte
	 * Specified Unit Id: byte
	 * Class Name: Field_Character
	 * Minimum Value: 1
	 * Nillable: false
	 * Attribute Concept: Location
	 * Conceptual Domain: Integer
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String FIELD_LOCATION="field_location";
	public static String  UNIT="unit";
	
	/**
	 * The data_type attribute provides the hardware representation used to store a value in Field_Character (see PDS Standards Reference section "Character Data Types").
	 * 
	 * Type: ASCII_Short_String_Collapsed
	 * Class Name: Field_Character
	 * Minimum Characters: 1
	 * Maximum Characters: 255
	 * Nillable: false
	 * Attribute Concept: Type
	 * Conceptual Domain: Short_String
	 * Steward: pds
	 * Namespace Id: pds
	 * 
	 * Values:
	 * ASCII_AnyURI	 
	 * ASCII_Boolean	 
	 * ASCII_DOI	 
	 * ASCII_Date_DOY	 
	 * ASCII_Date_Time_DOY	 
	 * ASCII_Date_Time_DOY_UTC	 
	 * ASCII_Date_Time_YMD	 
	 * ASCII_Date_Time_YMD_UTC	 
	 * ASCII_Date_YMD	 
	 * ASCII_Directory_Path_Name	 
	 * ASCII_File_Name	 
	 * ASCII_File_Specification_Name	 
	 * ASCII_Integer	 
	 * ASCII_LID	 
	 * ASCII_LIDVID	 
	 * ASCII_LIDVID_LID	 
	 * ASCII_MD5_Checksum	 
	 * ASCII_NonNegative_Integer	 
	 * ASCII_Numeric_Base16	 
	 * ASCII_Numeric_Base2	 
	 * ASCII_Numeric_Base8	 
	 * ASCII_Real	 
	 * ASCII_String	 
	 * ASCII_Time	 
	 * ASCII_VID	 
	 * UTF8_String
	 */
	public static String DATA_TYPE="data_type";
	public static String DATA_TYPE_ASCII_ANYURI="ASCII_AnyURI";
	public static String DATA_TYPE_ASCII_BOOLEAN="ASCII_Boolean";
	public static String DATA_TYPE_ASCII_DOI="ASCII_DOI";
	public static String DATA_TYPE_ASCII_DATE_DOY="ASCII_Date_DOY";
	public static String DATA_TYPE_ASCII_DATE_TIME_DOY="ASCII_Date_Time_DOY";
	public static String DATA_TYPE_ASCII_DATE_TIME_DOY_UTC="ASCII_Date_Time_DOY_UTC";
	public static String DATA_TYPE_ASCII_DATE_TIME_YMD="ASCII_Date_Time_YMD";
	public static String DATA_TYPE_ASCII_DATE_TIME_YMD_UTC="ASCII_Date_Time_YMD_UTC";
	public static String DATA_TYPE_ASCII_DATE_YMD="ASCII_Date_YMD";
	public static String DATA_TYPE_ASCII_DIRECTORY_PATH_NAME="ASCII_Directory_Path_Name";
	public static String DATA_TYPE_ASCII_FILE_NAME="ASCII_File_Name";
	public static String DATA_TYPE_ASCII_FILE_SPECIFICATION_NAME="ASCII_File_Specification_Name";
	public static String DATA_TYPE_ASCII_INTEGER="ASCII_Integer";
	public static String DATA_TYPE_ASCII_LID="ASCII_LID";
	public static String DATA_TYPE_ASCII_LIDVID="ASCII_LIDVID";
	public static String DATA_TYPE_ASCII_LIDVID_LID="ASCII_LIDVID_LID";
	public static String DATA_TYPE_ASCII_MD5_CHECKSUM="ASCII_MD5_Checksum";
	public static String DATA_TYPE_ASCII_NONNEGATIVE_INTEGER="ASCII_NonNegative_Integer";
	public static String DATA_TYPE_ASCII_NUMERIC_BASE16="ASCII_Numeric_Base16";
	public static String DATA_TYPE_ASCII_NUMERIC_BASE2="ASCII_Numeric_Base2";
	public static String DATA_TYPE_ASCII_NUMERIC_BASE8="ASCII_Numeric_Base8";
	public static String DATA_TYPE_ASCII_REAL="ASCII_Real";
	public static String DATA_TYPE_ASCII_STRING="ASCII_String";
	public static String DATA_TYPE_ASCII_TIME="ASCII_Time";
	public static String DATA_TYPE_ASCII_VID="ASCII_VID";
	public static String DATA_TYPE_UTF8_STRING="UTF8_String";
	/**
	 * The field_length attribute provides the number of bytes in the field.
	 * 
	 * Type: ASCII_Integer
	 * 
	 * Unit of Measure Type: Units_of_Storage
	 * Valid Units: byte
	 * Specified Unit Id: byte
	 * Class Name: Field_Character
	 * Minimum Value: 1
	 * Nillable: false
	 * Attribute Concept: Length
	 * Conceptual Domain: Integer
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String FIELD_LENGTH="field_length";
	/**
	 * The field_format attribute gives the magnitude and precision of the data value. The standard POSIX string formats are used.
	 * 
	 * Type: ASCII_Short_String_Collapsed
	 * Class Name: Field_Character
	 * Minimum Characters: 1
	 * Maximum Characters: 255
	 * Nillable: false
	 * Attribute Concept: Format
	 * Conceptual Domain: Short_String
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String FIELD_FORMAT="field_format";
	/**
	 * The description attribute provides a statement, picture in words, or account that describes or is otherwise relevant to the object.
	 * 
	 * Type: UTF8_Text_Preserved
	 * Class Name: Field_Character
	 * Minimum Characters: 1
	 * Nillable: false
	 * Attribute Concept: Description
	 * Conceptual Domain: Text
	 * Steward: pds
	 * Namespace Id: pds
	 */
	public static String DESCRIPTION="description";
	public static String FIELD_CHARACTER="Field_Character";
	public FieldCharacter(){
		name="";
		field_number="";
		field_location="";
		field_location_unit="";
		data_type="";
		field_length="";
		field_length_unit="";	
		field_format="";
		description="";
		scaling_factor="";
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
		result=result+id+"<"+FIELD_CHARACTER+">\n";
		result=result+id+"\t<"+NAME+">"+name+"</"+NAME+">\n";
		result=result+id+"\t<"+FIELD_NUMBER+">"+field_number+"</"+FIELD_NUMBER+">\n";
		result=result+id+"\t<"+FIELD_LOCATION+" unit=\""+field_location_unit+"\">"+field_location+"</"+FIELD_LOCATION+">\n";
		result=result+id+"\t<"+DATA_TYPE+">"+data_type+"</"+DATA_TYPE+">\n";
		result=result+id+"\t<"+FIELD_LENGTH+" unit=\""+field_length_unit+"\">"+field_length+"</"+FIELD_LENGTH+">\n";
		result=result+id+"\t<"+FIELD_FORMAT+">"+field_format+"</"+FIELD_FORMAT+">\n";
		result=result+id+"\t<"+DESCRIPTION+">"+description+"</"+DESCRIPTION+">\n";	
		if (!scaling_factor.equals("")) result=result+id+"\t<"+SCALING_FACTOR+">"+scaling_factor+"</"+SCALING_FACTOR+">\n";

		result=result+id+"</"+FIELD_CHARACTER+">\n";
		
		return result;
	}
	public static FieldCharacter getFromNode(Node node){
		FieldCharacter result=new FieldCharacter();
		Element poElement = (Element) node;
		try{
			result.setName(((Element) node).getElementsByTagName(NAME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+NAME);
		}
		try{
			result.setFieldNumber(((Element) node).getElementsByTagName(FIELD_NUMBER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_NUMBER);
		}
		try{
			result.setFieldLocation(((Element) node).getElementsByTagName(FIELD_LOCATION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_LOCATION);
		}
		
		try{
			result.setFieldLocationUnit(((Element) node).getElementsByTagName(FIELD_LOCATION).item(0).getAttributes().getNamedItem(UNIT).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_LOCATION+" "+UNIT);
		}
		
		try{
			result.setDataType(((Element) node).getElementsByTagName(DATA_TYPE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DATA_TYPE);
		}
		
		try{
			result.setFieldLength(((Element) node).getElementsByTagName(FIELD_LENGTH).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_LENGTH);
		}
		
		try{
			result.setScalingFactor(((Element) node).getElementsByTagName(SCALING_FACTOR).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+SCALING_FACTOR);
		}
		
		try{
			result.setFieldLengthUnit(((Element) node).getElementsByTagName(FIELD_LENGTH).item(0).getAttributes().getNamedItem(UNIT).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_LENGTH+" "+UNIT);
		}
		
		try{
			result.setFieldFormat(((Element) node).getElementsByTagName(FIELD_FORMAT).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FIELD_FORMAT);
		}
		
		try{
			result.setDescription(((Element) node).getElementsByTagName(DESCRIPTION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+DESCRIPTION);
		}
			return result;
	}

	
	public String getName(){
		return name;
	}
	
	public String getScalingFactor(){
		return scaling_factor;
	}
	
	public void setScalingFactor(String newScalingFactor){
		scaling_factor=newScalingFactor;
	}
	
	
	public void setName(String newName){
		name=newName;
	}
	
	public int getFieldNumber(){
	
		return Integer.parseInt(field_number);
	}
	
	public void setFieldNumber(String newFieldNumber){
		field_number=newFieldNumber;
	}
	
	public void setFieldNumber(int newFieldNumber){
		field_number=""+newFieldNumber;
	}
	
	public String getFieldLocation(){
		return field_location;
	}
	
	
	public void setFieldLocation(String newFieldLocation){
		field_location=newFieldLocation;
	}
	
	public String getDataType(){
		return data_type;
	}
	
	public void setDataType(String newDataType){
		data_type=newDataType;
	}
	
	public String getFieldLength(){
		return field_length;
	}
	
	public void setFieldLength(String newFieldLength){
		field_length=newFieldLength;
	}
	
	public String getFieldFormat(){
		return field_format;
	}
	
	public void setFieldFormat(String newFieldFormat){
		field_format=newFieldFormat;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String newDescription){
		description=newDescription;
	}
	public String getFieldLocationUnit(){
		return field_location_unit;
	}
	public void setFieldLocationUnit(String newFieldLocationUnit){
		field_location_unit=newFieldLocationUnit;
	}
	public String getFieldLengthUnit(){
		return field_length_unit;
	}
	public void setFieldLengthUnit(String newFieldLengthUnit){
		field_length_unit=newFieldLengthUnit;
	}
}
