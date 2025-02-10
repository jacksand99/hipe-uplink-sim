package vega.uplink.commanding;

//import herschel.share.util.Configuration;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import vega.uplink.DateUtil;
import vega.uplink.Properties;



/**
 * A special sequence used to set the priority downlink for the instruments
 * @author jarenas
 *
 */
public class PrioritySequence extends Sequence {
	
	public PrioritySequence() throws ParseException{
		super("ASYF033A","P0001",DateUtil.dateToDOY(new Date()));
		this.setUniqueID((""+new Date().getTime()).substring(5));
		try{
			setName(Properties.getProperty(Properties.ANTENNA_PRIORITY_COMMAND));
		}catch(Exception e){
			e.printStackTrace();
			setName("ASYF033A");
		}		
		List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = insList.iterator();
		while (it.hasNext()){
			String ins=it.next();
			
			try{
				String paramName=Properties.getProperty(Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+ins);
				if (!paramName.equals("VOID")) addParameter(new ParameterFloat(paramName,Parameter.REPRESENTATION_RAW,Parameter.RADIX_DECIMAL,18));
			}catch (Exception e){
				e.printStackTrace();
			}
		}

	}

}
