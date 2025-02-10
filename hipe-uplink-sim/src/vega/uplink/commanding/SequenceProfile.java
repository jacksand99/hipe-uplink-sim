package vega.uplink.commanding;
import static java.lang.Math.abs;


import org.w3c.dom.Document;
import org.w3c.dom.Element;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Double1d;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.String1d;

/**
 * Class to store a sequence profile (a consumption of resources, power or datarate)
 * @author jarenas
 *
 */
public class SequenceProfile extends TableDataset{
	/**
	 * DR
	 */
	public static String PROFILE_TYPE_DR="DR";
	/**
	 * PW
	 */
	public static String PROFILE_TYPE_PW="PW";
	/**
	 * Type
	 */
	public static String COLUMN_NAME_TYPE="Type";
	/**
	 * Offset
	 */
	public static String COLUMN_NAME_OFFSET="Offset";
	/**
	 * Value
	 */
	public static String COLUMN_NAME_VALUE="Value";
	
	
	/**
	 * Creates a new sequence profile
	 * @param profileType profile type, PW or DR
	 * @param profileOffset offset (time after execution) that this profile will start to be valid
	 * @param profileValue value of this profile
	 */
	public SequenceProfile(String profileType,String profileOffset,double profileValue){
		Column cType=new Column(new String1d().append(profileType));
		Column cOffset=new Column(new String1d().append(profileOffset));
		Column cValue=new Column(new Double1d().append(profileValue));
		addColumn(cType);
		addColumn(cOffset);
		addColumn(cValue);
		setColumnName(0, COLUMN_NAME_TYPE);
		setColumnName(1, COLUMN_NAME_OFFSET);
		setColumnName(2, COLUMN_NAME_VALUE);
		
	}
	
	/**
	 * Get the profile type (PW or DR)
	 * @return
	 */
	public String getType(){
		return ((String1d) getColumn(COLUMN_NAME_TYPE).getData()).get(0);
	}
	
	/**
	 * Get the value of this profile
	 * @return
	 */
	public double getValue(){
		return ((Double1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);
	}
	
	/**
	 * Get the offset as string
	 * @return
	 */
	public String getOffSetString(){
		return ((String1d) getColumn(COLUMN_NAME_OFFSET).getData()).get(0);
	}
	
	/**
	 * Get the offset as number of seconds
	 * @return
	 */
	public int getOffSetSeconds(){
		String offset=getOffSetString();
		int offSetHours=getHoursFromString(offset);
		int offSetMinutes=getMinutesFromString(offset);
		int offSetSeconds=getSecondsFromString(offset);

		return offSetSeconds+(offSetMinutes*60)+(offSetHours*60*60);
	}
	
	/**
	 * Set the profile type (PW or DR)
	 * @param profileType
	 */
	public void setType(String profileType){
		getColumn(COLUMN_NAME_TYPE).setData(new String1d().append(profileType));
	}
	
	/**
	 * Set the profile value
	 * @param profileValue
	 */
	public void setValue(float profileValue){
		getColumn(COLUMN_NAME_VALUE).setData(new Float1d().append(profileValue));
	}
	
	/**
	 * Set the profile offset as string
	 * @param profileOffSet
	 */
	public void setOffSetString(String profileOffSet){
		getColumn(COLUMN_NAME_OFFSET).setData(new String1d().append(profileOffSet));
	}
	
	/**
	 * Set the profile offset as number of seconds
	 * @param profileOffSetSeconds
	 */
	public void setOffSetSeconds(int profileOffSetSeconds){
		int hours = abs(profileOffSetSeconds/3600);
		int minutes =abs((profileOffSetSeconds-(hours*3600))/60);
		int seconds =(profileOffSetSeconds-(hours*3600)-(minutes*60));
		
		String sHours;
		String sMinutes;
		String sSeconds;
		if (hours <10){
			sHours = "0"+new Integer(hours).toString();
		} else {
			sHours =new Integer(hours).toString();
		}

		if (minutes <10){
			sMinutes = "0"+new Integer(minutes).toString();
		} else {
			sMinutes =new Integer(minutes).toString();
		}
		
		if (seconds <10){
			sSeconds = "0"+new Integer(seconds).toString();
		} else {
			sSeconds =new Integer(seconds).toString();
		}
		
		this.setOffSetString(hours+":"+minutes+":"+seconds);
		
	}
	
	private static int getHoursFromString(String time){
		char[] arr =  time.toCharArray();
		char[] arr2 = {arr[0],arr[1]};
		return new Integer(new String(arr2)).intValue();
	}

	private static int getMinutesFromString(String time){
		char[] arr =  time.toCharArray();
		char[] arr2 = {arr[3],arr[4]};
		return new Integer(new String(arr2)).intValue();
	}
	
	private static int getSecondsFromString(String time){
		char[] arr =  time.toCharArray();
		char[] arr2 = {arr[6],arr[7]};
		return new Integer(new String(arr2)).intValue();
	}
	
	/**
	 * Generate an xml representation of this profile
	 * @return
	 */
	public String toXml(){
		return toXml(0);
	}
	
	protected Element getXMLElement(Document doc){
		Element eleProfile=null;
		  try {
				eleProfile = doc.createElement("profile");
				eleProfile.setAttribute("type", getType());
				Element eleTimeOffset=doc.createElement("timeOffset");
				eleTimeOffset.setTextContent(getOffSetString());
				Element eleValue=doc.createElement("value");
				eleValue.setTextContent(new Double(getValue()).toString());
				eleProfile.appendChild(eleTimeOffset);
				eleProfile.appendChild(eleValue);
				
				
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  return eleProfile;
	}

	
	/**
	 * get a xml representation of this profile with a given indentation
	 * @param indent
	 * @return
	 */
	public String toXml(int indent){
		StringBuilder indentString=new StringBuilder();
		for (int i=0;i<=indent;i++){
			indentString.append("\t");
		}
		String l1="<profile type=\""+getType()+"\">";
		String l2="<timeOffset>"+getOffSetString()+"</timeOffset>";
		String l3="<value>"+new Double(getValue()).toString()+"</value>";
		String l4="</profile>";
		StringBuilder result=new StringBuilder();
		result.append(indentString);
		result.append(l1);
		result.append("\n\t");
		result.append(indentString);
		result.append(l2);
		result.append("\n\t");
		result.append(indentString);
		result.append(l3);
		result.append("\n");
		result.append(indentString);
		result.append(l4);
		return result.toString();
	}
	
	public boolean equal(SequenceProfile sq){
		if (sq.getType().equals(getType()) && sq.getOffSetString().equals(getOffSetString()) && sq.getValue()==getValue()) return true;
		else return false;
	}
	


}
