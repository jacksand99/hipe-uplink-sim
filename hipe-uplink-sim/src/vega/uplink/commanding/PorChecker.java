package vega.uplink.commanding;

import java.io.IOException;
import java.util.Date;

import vega.uplink.pointing.PointingBlock;

public class PorChecker {
	public static String checkPor(Por por) throws IOException{
		Mib mib=Mib.getMib();
		String messages="";
		Date startDate = por.getValidityDates()[0];
		Date endDate = por.getValidityDates()[1];
		Sequence[] sequences = por.getSequences();
		for (int i=0;i<sequences.length;i++){
			if (sequences[i].getExecutionDate().before(startDate)){
				messages=messages+"POR:Sequence execution date before validity of POR: "+sequences[i].getName()+" - "+sequences[i].getExecutionTime()+"\n";
				messages=messages+"--------------------------------------\n";
			}
			if (sequences[i].getExecutionDate().after(endDate)){
				messages=messages+"POR:Sequence execution date after validity of POR: "+sequences[i].getName()+" - "+sequences[i].getExecutionTime()+"\n";
				messages=messages+"--------------------------------------\n";
			}
			try{
				mib.checkSequence(sequences[i]);
			}catch (Exception e){
				messages=messages+"POR:Error: "+e.getMessage()+"\n";
				e.printStackTrace();
				messages=messages+"--------------------------------------\n";
			
			}
				

				
		}
			if (messages.equals("")) return messages;
			else{

			return "**************************\nPOR CHECK\n**************************\n"+messages;
			}

	}
	

}
