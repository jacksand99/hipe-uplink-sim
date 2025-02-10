package vega.uplink.planning;

import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SequenceInterface;

public class ObservationPor extends Por{
	Observation parent;
	int sed;
	public static int counter=0;
	public void regenerate(ObservationPor por){
		super.regenerate(por);
		parent.commandingChange(null);
	}
	public ObservationPor(Observation obs){
		super();
		parent=obs;
		sed=0;
	}
	public Observation getObservation(){
		return parent;
		
	}
	public void addSequence(SequenceInterface seq){
		seq.setUniqueID(ObservationPor.getUniqueID());
		super.addSequence(seq);
	}
	public void setObservation(Observation obs){
		parent=obs;
		AbstractSequence[] seq = this.getSequences();
		for (int i=0;i<seq.length;i++){
			((ObservationSequence)seq[i]).setObservation(obs);
		}
	}
	
	public String toObsXml(int indent){
		String result="";
		AbstractSequence[] seq = getOrderedSequences();
		for (int i=0;i<seq.length;i++){
			result=result+((ObservationSequence) seq[i]).toObsXml(0);
		}
		return result;
	}
	
	public static String getUniqueID(){
		counter++;
		return "P"+String.format("%09d", counter);
	}
}
