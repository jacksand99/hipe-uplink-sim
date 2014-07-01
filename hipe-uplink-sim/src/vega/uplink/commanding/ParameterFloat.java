package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.numeric.Float1d;
//import herschel.ia.numeric.String1d;

public class ParameterFloat extends Parameter{
	//private float value;
	
	public ParameterFloat(String parameterName,String parameterRepresentation,String parameterRadix,float parameterValue){
		super(parameterName,parameterRepresentation,parameterRadix);
		Column cValue=new Column(new Float1d().append(parameterValue));
		setColumn(COLUMN_NAME_VALUE,cValue);
		//value=parameterValue;
		//getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(getStringValue()));

	}
	
	public float getValue(){
		return ((Float1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);
		//return value;
	}
	
	public void setValue(float parameterValue){
		getColumn(COLUMN_NAME_VALUE).setData(new Float1d().append(parameterValue));
		/*value=parameterValue;
		if (getRadix().equals(RADIX_DECIMAL)){
			getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(new Float(parameterValue).toString()));

		}
		if (getRadix().equals(RADIX_HEX)){
			getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(Float.toHexString(parameterValue)));
		}*/

		//getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(new Float(parameterValue).toString()));

	}
	
	/*public void setValue(String parameterValue){
		value=Float.parseFloat(parameterValue);
		getColumn(COLUMN_NAME_VALUE).setData(new String1d().append(parameterValue));
	}*/
	
	public String getStringValue(){
		if (getRadix().equals(RADIX_DECIMAL)){
			return new Float(getValue()).toString();
		}
		if (getRadix().equals(RADIX_HEX)){
			return Integer.toHexString(new Float(getValue()).intValue());
		}
		return "";
		
	}

}
