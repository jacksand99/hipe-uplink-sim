package vega.uplink.commanding;
import static java.lang.Math.abs;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Double1d;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.String1d;

public class SequenceProfile extends TableDataset{
	
	//String type;
	public static String PROFILE_TYPE_DR="DR";
	public static String PROFILE_TYPE_PW="PW";
	/*private double value;
	private int offSetHours;
	private int offSetMinutes;
	private int offSetSeconds;*/
	public static String COLUMN_NAME_TYPE="Type";
	public static String COLUMN_NAME_OFFSET="Offset";
	public static String COLUMN_NAME_VALUE="Value";
	
	
	public SequenceProfile(String profileType,String profileOffset,double profileValue){
		Column cType=new Column(new String1d().append(profileType));
		Column cOffset=new Column(new String1d().append(profileOffset));
		Column cValue=new Column(new Double1d().append(profileValue));
		//Column cValue=new Column(new String1d().append(""));
		addColumn(cType);
		addColumn(cOffset);
		addColumn(cValue);
		//addColumn(cValue);
		setColumnName(0, COLUMN_NAME_TYPE);
		setColumnName(1, COLUMN_NAME_OFFSET);
		setColumnName(2, COLUMN_NAME_VALUE);
		//setColumnName(3, COLUMN_NAME_VALUE);

		/*type=profileType;
		value=profileValue;
		offSetHours=getHoursFromString(profileOffset);
		offSetMinutes=getMinutesFromString(profileOffset);
		offSetSeconds=getSecondsFromString(profileOffset);*/
		
	}
	
	public String getType(){
		return ((String1d) getColumn(COLUMN_NAME_TYPE).getData()).get(0);
		//return type;
	}
	
	public double getValue(){
		return ((Double1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);
		//return value;
	}
	
	public String getOffSetString(){
		return ((String1d) getColumn(COLUMN_NAME_OFFSET).getData()).get(0);

		/*String hours;
		String minutes;
		String seconds;
		if (offSetHours <10){
			hours = "0"+new Integer(offSetHours).toString();
		} else {
			hours =new Integer(offSetHours).toString();
		}

		if (offSetMinutes <10){
			minutes = "0"+new Integer(offSetMinutes).toString();
		} else {
			minutes =new Integer(offSetMinutes).toString();
		}
		
		if (offSetSeconds <10){
			seconds = "0"+new Integer(offSetSeconds).toString();
		} else {
			seconds =new Integer(offSetSeconds).toString();
		}
		
		return hours+":"+minutes+":"+seconds;*/
	}
	
	public int getOffSetSeconds(){
		String offset=getOffSetString();
		int offSetHours=getHoursFromString(offset);
		int offSetMinutes=getMinutesFromString(offset);
		int offSetSeconds=getSecondsFromString(offset);

		return offSetSeconds+(offSetMinutes*60)+(offSetHours*60*60);
	}
	
	public void setType(String profileType){
		getColumn(COLUMN_NAME_TYPE).setData(new String1d().append(profileType));

		//type=profileType;
	}
	
	public void setValue(float profileValue){
		getColumn(COLUMN_NAME_VALUE).setData(new Float1d().append(profileValue));

		//value=profileValue;
	}
	
	public void setOffSetString(String profileOffSet){
		getColumn(COLUMN_NAME_OFFSET).setData(new String1d().append(profileOffSet));

		/*offSetHours=getHoursFromString(profileOffSet);
		offSetMinutes=getMinutesFromString(profileOffSet);
		offSetSeconds=getSecondsFromString(profileOffSet);*/
	}
	
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
		/*offSetHours=hours;
		offSetMinutes=minutes;
		offSetSeconds=seconds;*/
		
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

	
	public String toXml(int indent){
		//String indentString="";
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
		//return indentString+l1+"\n\t"+indentString+l2+"\n\t"+indentString+l3+"\n"+indentString+l4;
		return result.toString();
	}
	
	public boolean equal(SequenceProfile sq){
		if (sq.getType().equals(getType()) && sq.getOffSetString().equals(getOffSetString()) && sq.getValue()==getValue()) return true;
		else return false;
	}
	


}
