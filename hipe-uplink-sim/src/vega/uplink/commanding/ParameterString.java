package vega.uplink.commanding;

import herschel.ia.numeric.String1d;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ParameterString extends Parameter{
	//private String value;
	
	public ParameterString(String parameterName,String parameterRepresentation,String parameterValue){
		super(parameterName,parameterRepresentation,"");
		getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(parameterValue));

		//value=parameterValue;
	}
	
	public String getValue(){
		return ((String1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);

		//return value;
	}
	
	public void setValue(String parameterValue){
		//value=parameterValue;
		getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(parameterValue));
	}

	public String getStringValue(){
		return getValue();
	}
	
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
				//eleValue.setAttribute("radix", this.radix);
				eleValue.setTextContent(this.getStringValue());
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
		String l2= "<value representation=\""+getRepresentation()+"\">"+getStringValue()+"</value>";
		String l3="</parameter>";
		return indentString+l1+"\n\t"+indentString+l2+"\n"+indentString+l3;
	}
	

}
