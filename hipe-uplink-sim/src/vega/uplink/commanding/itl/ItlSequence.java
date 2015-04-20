package vega.uplink.commanding.itl;

import java.io.IOException;
import java.text.ParseException;

import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.planning.ObservationPor;
import vega.uplink.planning.ObservationSequence;
import vega.uplink.planning.ObservationUtil;

public class ItlSequence extends ObservationSequence{
	Mib mib;
	public ItlSequence(Itl parent, ItlEvent executionEvent,
			long executionDelta, String sequenceName, String sequenceID,
			String sequenceFlag, Parameter[] sequenceParamaters,
			SequenceProfile[] sequenceProfiles) throws ParseException {
		super(parent, executionEvent, executionDelta, sequenceName, sequenceID,
				sequenceFlag, sequenceParamaters, sequenceProfiles);
		try {
			mib=Mib.getMib();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException("Could not get mib:"+e.getMessage());
			iae.initCause(e);
			throw iae;
		}
		// TODO Auto-generated constructor stub
	}
	
	public ItlSequence(Itl parent, ItlEvent executionEvent,
			long executionDelta, String sequenceName, String sequenceID, Parameter[] sequenceParamaters,
			SequenceProfile[] sequenceProfiles) throws ParseException {
		super(parent, executionEvent, executionDelta, sequenceName, sequenceID,
				 sequenceParamaters, sequenceProfiles);
		try {
			mib=Mib.getMib();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException("Could not get mib:"+e.getMessage());
			iae.initCause(e);
			throw iae;
		}

		// TODO Auto-generated constructor stub
	}
	
	public ItlSequence(Itl parent, ItlEvent executionEvent,
			long executionDelta, String sequenceName, Parameter[] sequenceParamaters,
			SequenceProfile[] sequenceProfiles) throws ParseException {
		this(parent, executionEvent, executionDelta, sequenceName, ObservationPor.getUniqueID(),
				 sequenceParamaters, sequenceProfiles);
		// TODO Auto-generated constructor stub
	}
	public ItlSequence(Itl parent, ItlEvent executionEvent,
			long executionDelta, String sequenceName) throws ParseException {
		super(parent, executionEvent, executionDelta, sequenceName, ObservationPor.getUniqueID());
		try {
			mib=Mib.getMib();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException("Could not get mib:"+e.getMessage());
			iae.initCause(e);
			throw iae;
		}

		// TODO Auto-generated constructor stub
	}
	
	public String toString(){
		String result="";
		result=result+((ItlEvent) this.getExecutionTimeEvent()).toString()+" "+ObservationUtil.getOffset(getExecutionTimeDelta())+" "+ getInstrumentName()+"\t*\t"+getName()+" (\\ #"+mib.getSequenceDescription(getName())+"\n";
		Parameter[] tempParam = getParameters();
		for (int z=0;z<tempParam.length;z++){
			result=result+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+" \n";
		}
		SequenceProfile[] tempPro = getProfiles();
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
		if (dataRatePresent) result =result+dataRateProfile+"\\\n";
		if (powerProfilePresent) result =result+powerProfile+"\\\n";

		result=result+"\t\t\t\t)\n";
		
		return result;

	}
	

}
