package vega.uplink.commanding.itl;

import herschel.share.interpreter.InterpreterUtil;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.TreeMap;
import java.util.TreeSet;

import vega.uplink.DateUtil;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationSequence;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;

public class ItlUtil {
	public static String porToITL(Por POR, int sed){
		return porToITL(POR,sed,POR.getStartDate().toDate(),POR.getEndDate().toDate());
	}
	public static String porToITL(Por POR, int sed,Date itlStartTime,Date itlEndTime){
		Mib mib;
		try {
			mib=Mib.getMib();
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(itlStartTime)+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(itlStartTime)+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(itlEndTime)+"\n\n\n";
		
		String l05="";
			AbstractSequence[] tempSeq = POR.getOrderedSequences();
			Parameter[] tempParam;
			SequenceProfile[] tempPro;
			ObsList obsList=new ObsList(sed);
			for (int j=0;j<tempSeq.length;j++){
				Observation obs=((ObservationSequence)tempSeq[j]).getObs();
				obsList.put(obs);
			}
			for (int j=0;j<tempSeq.length;j++){
				if (InterpreterUtil.isInstance(ObservationSequence.class, tempSeq[j])){
					String itlEvent="";
					Observation obs=((ObservationSequence)tempSeq[j]).getObs();
					if (((ObservationSequence)tempSeq[j]).getExecutionTimeEvent().getName().equals("START_OBS")){
						itlEvent=obsList.getStartEvent(obs);
					}
					if (((ObservationSequence)tempSeq[j]).getExecutionTimeEvent().getName().equals("END_OBS")){
						itlEvent=obsList.getEndEvent(obs);
					}
	
					l05=l05+itlEvent+" "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[j]).getExecutionTimeDelta())+" "+ tempSeq[j].getInstrumentName()+"\t*\t"+tempSeq[j].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[j].getName())+"\n";
				}else{
					l05=l05+DateUtil.dateToLiteral(tempSeq[j].getExecutionDate())+" "+ tempSeq[j].getInstrument()+"\t*\t"+tempSeq[j].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[j].getName())+"\n";
					//LOG.info("WARNING:"+DateUtil.dateToLiteral(tempSeq[j].getExecutionDate())+" "+ tempSeq[j].getInstrument()+" "+tempSeq[j].getName()+" is a literal sequence");

				}
				tempParam = tempSeq[j].getParameters();
				tempParam = tempSeq[j].getParameters();
				for (int z=0;z<tempParam.length;z++){
					l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+" \n";
				}
				tempPro=tempSeq[j].getProfiles();
				String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
				String powerProfile="\tPOWER_PROFILE = \t\t\t";
				boolean dataRatePresent=false;
				boolean powerProfilePresent=false;
				for (int k=0;k<tempPro.length;k++){
					if (tempPro[k].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
						dataRateProfile=dataRateProfile+" "+tempPro[k].getOffSetString()+"\t"+new Double(tempPro[k].getValue()).toString()+"\t[bits/sec]";
						dataRatePresent=true;
					}
					if (tempPro[k].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
						powerProfile=powerProfile+" "+tempPro[k].getOffSetString()+"\t"+new Double(tempPro[k].getValue()).toString()+"\t[Watts]";
						powerProfilePresent=true;
					}
					
				}
				if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
				if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

				l05=l05+"\t\t\t\t)\n";

			}
			

		//}
		result=l01+l02+l03+l04+l05;
		return result;

	}
	
	public static String porToEVF(Por POR,int sed){
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		String result="";
		AbstractSequence[] seqs = POR.getOrderedSequences();
		String l05="";
		ObsList obsList=new ObsList(sed);
		for (int i=0;i<seqs.length;i++){
			if (InterpreterUtil.isInstance(ObservationSequence.class,seqs[i] )){
				Observation obs = ((ObservationSequence)seqs[i]).getObs();
				obsList.put(obs);
			}
		}
		Iterator<String> it = obsList.getEventsIterator();
		while (it.hasNext()){

			String eventName=it.next();
			
			l05=l05+eventName+"\n";

		}
		result=l05;
		return result;
	}

}
