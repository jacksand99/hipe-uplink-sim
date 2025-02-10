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

/**
 * Class to store a parameter from a sequence
 * @author jarenas
 *
 */
public class Parameter extends TableDataset{
	public static String RADIX_DECIMAL="Decimal";
	public static String RADIX_HEX="Hexadecimal";
	public static String RADIX_OCTAL="Octal";
	public static String REPRESENTATION_RAW="Raw";
	public static String REPRESENTATION_ENGINEERING="Engineering";
	public static String COLUMN_NAME_NAME="Name";
	public static String COLUMN_NAME_REPRESENTATION="Representation";
	public static String COLUMN_NAME_RADIX="Radix";
	public static String COLUMN_NAME_VALUE="Value";
	public static String COLUMN_NAME_DESCRIPTION="Description";
	
	
	/**
	 * Creates a new Parameter
	 * @param parameterName Name of the parameter
	 * @param parameterRepresentation Either Raw or Engineering
	 * @param parameterRadix One of Decimal, Hexadecimal or Octal
	 */
	public Parameter(String parameterName,String parameterRepresentation,String parameterRadix){
		super();		
		Column cName=new Column(new String1d().append(parameterName));
		Column cRepresentation=new Column(new String1d().append(parameterRepresentation));
		Column cRadix=new Column(new String1d().append(parameterRadix));
		Column cValue=new Column(new String1d().append(""));
		Column cDescription=new Column(new String1d().append(""));
		addColumn(COLUMN_NAME_NAME,cName);
		addColumn(COLUMN_NAME_REPRESENTATION,cRepresentation);
		addColumn(COLUMN_NAME_RADIX,cRadix);
		addColumn(COLUMN_NAME_VALUE,cValue);
		addColumn(COLUMN_NAME_DESCRIPTION,cDescription);
		
		/*System.out.println(getColumnCount());
		setColumnName(0, COLUMN_NAME_NAME);
		setColumnName(1, COLUMN_NAME_REPRESENTATION);
		setColumnName(2, COLUMN_NAME_RADIX);
		setColumnName(3, COLUMN_NAME_VALUE);*/

	}

	
	/**
	 * Get the name of the parameter
	 * @return
	 */
	public String getName(){
		return ((String1d) getColumn(COLUMN_NAME_NAME).getData()).get(0);
		
	}
	   public String getDescription(){
	       String result=null;
	        result=((String1d) getColumn(COLUMN_NAME_DESCRIPTION).getData()).get(0);
	        if (result=="") result =null;
	        return result;
	        
	    }
	
	/**
	 * Get the representation of the parameter. Either Raw or Engineering
	 * @return
	 */
	public String getRepresentation(){
		return ((String1d) getColumn(COLUMN_NAME_REPRESENTATION).getData()).get(0);
	}
	
	/**
	 * Get the radix of the parameter. One of Decimal, Hexadecimal or Octal 
	 * @return
	 */
	public String getRadix(){
		return ((String1d) getColumn(COLUMN_NAME_RADIX).getData()).get(0);
	}
	
	/**
	 * Get the value of the parameter as String
	 * @return
	 */
	public String getStringValue(){
		return ((String1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);
	}
	
	/**
	 * Set the name of the parameter
	 * @param parameterName
	 */
	public void setName(String parameterName){
		getColumn(COLUMN_NAME_NAME).setData(new String1d().append(parameterName));
	}
	   public void setDescription(String parameterDescription){
	        getColumn(COLUMN_NAME_DESCRIPTION).setData(new String1d().append(parameterDescription));
	    }
	
	/**
	 * Set the representation of the parameter. Either Raw or Engineering.
	 * @param parameterRepresentation
	 */
	public void setRepresentation(String parameterRepresentation){
		getColumn(COLUMN_NAME_REPRESENTATION).setData(new String1d().append(parameterRepresentation));

	}
	
	/**
	 * Set the radix of the parameter. One of Decimal, Hexadecimal or Octal
	 * @param parameterRadix
	 */
	public void setRadix(String parameterRadix){
		getColumn(COLUMN_NAME_RADIX).setData(new String1d().append(parameterRadix));

	}
	
	
	
	/**
	 * Get the XML representation of this parameter
	 * @param position The value of the attribute position
	 * @return
	 */
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
                Element eleDescription=doc.createElement("description");
                eleDescription.setTextContent(""+getDescription());
                if (getDescription()!=null) eleParameter.appendChild(eleDescription);
				
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  
		  return eleParameter;
	}
	/**
	 * Get the XML representation of this parameter with a given indentation
	 * @param position The value of the attribute position
	 * @param indent The desired indentation for the text
	 * @return
	 */
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
        String l4="";
        if (this.getDescription()!=null || this.getDescription()!="") l4=indentString+"\t"+"<description>"+this.getDescription()+"</description>\n";
        String l3="</parameter>";
        return indentString+l1+"\n\t"+indentString+l2+"\n"+l4+indentString+l3;
	}

	public int 	getRowCount() {
		return 1;
	}
	
	public String 	getColumnName(int index) {
		if (index==0) return COLUMN_NAME_NAME;
		if (index==1) return COLUMN_NAME_REPRESENTATION;
		if (index==2) return COLUMN_NAME_RADIX;
		if (index==3) return COLUMN_NAME_VALUE;
		if (index==4) return COLUMN_NAME_DESCRIPTION;
		return null;
		
	}
	
	public int 	getColumnCount() {
		return 5;
	}
	

}
