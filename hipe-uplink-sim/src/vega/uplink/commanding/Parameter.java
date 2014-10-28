package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.ArrayData;
import herschel.ia.numeric.String1d;
import herschel.share.interpreter.InterpreterUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.util.ArrayList;
import java.util.List;
 





import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Parameter extends TableDataset{
	//private String name;
	//private String representation;
	//private String radix;
	//private String stringValue;
	public static String RADIX_DECIMAL="Decimal";
	public static String RADIX_HEX="Hexadecimal";
	public static String RADIX_OCTAL="Octal";
	public static String REPRESENTATION_RAW="Raw";
	public static String REPRESENTATION_ENGINEERING="Engineering";
	public static String COLUMN_NAME_NAME="Name";
	public static String COLUMN_NAME_REPRESENTATION="Representation";
	public static String COLUMN_NAME_RADIX="Radix";
	public static String COLUMN_NAME_VALUE="Value";
	
	
	public Parameter(String parameterName,String parameterRepresentation,String parameterRadix){
		super();		
		/*name=parameterName;
		representation=parameterRepresentation;
		radix=parameterRadix;
		stringValue="";*/
		Column cName=new Column(new String1d().append(parameterName));
		Column cRepresentation=new Column(new String1d().append(parameterRepresentation));
		Column cRadix=new Column(new String1d().append(parameterRadix));
		Column cValue=new Column(new String1d().append(""));
		addColumn(cName);
		addColumn(cRepresentation);
		addColumn(cRadix);
		addColumn(cValue);
		setColumnName(0, COLUMN_NAME_NAME);
		setColumnName(1, COLUMN_NAME_REPRESENTATION);
		setColumnName(2, COLUMN_NAME_RADIX);
		setColumnName(3, COLUMN_NAME_VALUE);

	}

	/*public Parameter(String parameterName,String parameterRepresentation,String parameterRadix,float parameterValue){
		name=parameterName;
		representation=parameterRepresentation;
		radix=parameterRadix;
		value=parameterValue;
	}*/
	
	public String getName(){
		return ((String1d) getColumn(COLUMN_NAME_NAME).getData()).get(0);
		//return name;
		
	}
	
	public String getRepresentation(){
		return ((String1d) getColumn(COLUMN_NAME_REPRESENTATION).getData()).get(0);

		//return representation;
	}
	
	public String getRadix(){
		return ((String1d) getColumn(COLUMN_NAME_RADIX).getData()).get(0);

		//return radix;
	}
	
	public String getStringValue(){
		return ((String1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);

		//return stringValue;
	}
	
	public void setName(String parameterName){
		//name=parameterName;
		getColumn(COLUMN_NAME_NAME).setData(new String1d().append(parameterName));
	}
	
	public void setRepresentation(String parameterRepresentation){
		//representation=parameterRepresentation;
		getColumn(COLUMN_NAME_REPRESENTATION).setData(new String1d().append(parameterRepresentation));

	}
	
	public void setRadix(String parameterRadix){
		//radix=parameterRadix;
		getColumn(COLUMN_NAME_RADIX).setData(new String1d().append(parameterRadix));

	}
	
	/*public void setStringValue(String parameterStringValue){
		stringValue=parameterStringValue;
	}*/
	
	
	public String toXML(int position){
		return toXML(position,0);
	}
	
	protected Element getXMLElement(int position,Document doc){
		Element eleParameter=null;
		  try {
			  
				eleParameter = doc.createElement("parameter");
				eleParameter.setAttribute("name", this.getName());
				eleParameter.setAttribute("position", new Integer(position).toString());
				Element eleValue=doc.createElement("value");
				eleValue.setAttribute("representation", getRepresentation());
				eleValue.setAttribute("radix", getRadix());
				String val = getStringValue();
				if (getRadix().equals(RADIX_HEX)){
					val.replace("0x", "");
				}
				eleValue.setTextContent(val);
				eleParameter.appendChild(eleValue);
				
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  
		  return eleParameter;
	}
	public String toXML(int position,int indent){
		String indentString="";
		for (int i=0;i<=indent;i++){
			indentString=indentString+"\t";
		}
		String pos=new Integer(position).toString();				
		String l1= "<parameter name=\""+this.getName()+"\" position=\""+pos+"\">";
		String val = getStringValue();
		if (getRadix().equals(RADIX_HEX)){
			val=val.replace("0x", "");
		}

		String l2= "<value representation=\""+getRepresentation()+"\" radix=\""+getRadix()+"\">"+val+"</value>";
		String l3="</parameter>";
		//return l1+"\n\t"+l2+"\n"+l3;
		return indentString+l1+"\n\t"+indentString+l2+"\n"+indentString+l3;
	}
/*	
	public void 	setColumn(int index, Column newColumn){
		ArrayData data = newColumn.getData();
		if (InterpreterUtil.isInstance(String1d.class, data)){
			String1d dataTemp=(String1d) data;
			String actualData=dataTemp.getAt(0);
			if (index==0){
				this.setName(actualData);
			}
			if (index==1){
				this.setRepresentation(actualData);
			}
			if (index==2){
				this.setRadix(actualData);
			}
			if (index==3){
				this.setValue(actualData);
			}

		}
	}
	
	public void 	setColumn(String name, Column newColumn) {
		if (name.equals(COLUMN_NAME_NAME)) setColumn(0,newColumn);
		if (name.equals(COLUMN_NAME_REPRESENTATION)) setColumn(0,newColumn);
		if (name.equals(COLUMN_NAME_RADIX)) setColumn(0,newColumn);
		if (name.equals(COLUMN_NAME_VALUE)) setColumn(0,newColumn);
		
	}
	
	public void 	setColumnName(int index, String newName) {
		//Do nothing
	}
    //Replaces a column at specified column index in this table.
	public void 	setValueAt(Object newValue, int rowIndex, int columnIndex) {
		System.out.println("setvalue:"+Object.class);
		if (InterpreterUtil.isInstance(String.class, newValue)){
			if (rowIndex==0 && columnIndex==0){
				setName((String) newValue);
			}
			if (rowIndex==0 && columnIndex==1){
				setRepresentation((String) newValue);
			}
			if (rowIndex==0 && columnIndex==2){
				setRadix((String) newValue);
			}
			if (rowIndex==0 && columnIndex==3){
				setValue((String) newValue);
			}

		}
	}
	
	public void setValue(String newValue){
		stringValue=newValue;
		getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(stringValue));
	}
	
	public void 	setColumn(int index, String newName, Column newColumn){
		setColumn(index,newColumn);
	}
	public int 	indexOf(String name) {
		if (name.equals(COLUMN_NAME_NAME)) return 0;
		if (name.equals(COLUMN_NAME_REPRESENTATION)) return 1;
		if (name.equals(COLUMN_NAME_RADIX)) return 2;
		if (name.equals(COLUMN_NAME_VALUE)) return 3;
		return -1;
	}
	
	
	public Object 	getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex==0 && columnIndex==0){
			return getName();
		}
		if (rowIndex==0 && columnIndex==1){
			return getRepresentation();
		}
		if (rowIndex==0 && columnIndex==2){
			return getRadix();
		}
		if (rowIndex==0 && columnIndex==3){
			return getStringValue();
		}
		return "";

	}
*/	
	public int 	getRowCount() {
		return 1;
	}
	
	public String 	getColumnName(int index) {
		if (index==0) return COLUMN_NAME_NAME;
		if (index==1) return COLUMN_NAME_REPRESENTATION;
		if (index==2) return COLUMN_NAME_RADIX;
		if (index==3) return COLUMN_NAME_VALUE;
		return null;
		
	}
	
	public int 	getColumnCount() {
		return 4;
	}
	

}
