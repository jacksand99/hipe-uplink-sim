package vega.uplink.commanding;

import java.math.BigDecimal;

import herschel.ia.dataset.Column;
import herschel.ia.numeric.Float1d;

/**
 * Class to store a sequence parameter with numeric value (raw)
 * @author jarenas
 *
 */
public class ParameterFloat extends Parameter{
	
	public ParameterFloat(String parameterName,String parameterRepresentation,String parameterRadix,float parameterValue){
		super(parameterName,parameterRepresentation,parameterRadix);
		Column cValue=new Column(new Float1d().append(parameterValue));
		setColumn(COLUMN_NAME_VALUE,cValue);

	}
	
	public float getValue(){
		return ((Float1d) getColumn(COLUMN_NAME_VALUE).getData()).get(0);
	}
	
	public void setValue(float parameterValue){
		getColumn(COLUMN_NAME_VALUE).setData(new Float1d().append(parameterValue));

	}
	
	
	public String getStringValue(){
		float value = getValue();
		if (value==0) return "0";
		String result="";
		if (getRadix().equals(RADIX_DECIMAL)){
			result=new BigDecimal(value).toPlainString();
		}
		if (getRadix().equals(RADIX_HEX)){
			result= "0x"+Integer.toHexString(new Float(value).intValue());
		}
		if (result.endsWith(".0")) result=result.substring(0, result.length()-3);
		return result;
		
	}

}
