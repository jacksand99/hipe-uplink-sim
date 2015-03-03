package vega.uplink.planning;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.Sequence;
//import vega.uplink.commanding.SequenceInterface;
import vega.uplink.commanding.SequenceProfile;


/**
 * Commanding Sequence that is driven by an Observation event instead of times for execution time of the sequence
 * @author jarenas
 *
 */
public class ObservationSequence extends Sequence{
	private ObservationEvent executionTimeEvent;
	private long executionTimeDelta;
	private Observation parent;

	public ObservationSequence (Observation parent,ObservationEvent executionEvent,long executionDelta,String sequenceName,String sequenceID,String sequenceFlag,char sequenceSource,char sequenceDestination,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles){
		super(sequenceName,sequenceID,sequenceFlag,sequenceSource,sequenceDestination,new Date(parent.getDateForEvent(executionEvent).getTime()+executionDelta),sequenceParamaters,sequenceProfiles);
		executionTimeEvent=executionEvent;
		executionTimeDelta=executionDelta;
		this.parent=parent;
		
		
		
	}
	/*public String getUniqueID(){
		//return thi
	}*/
	
	
	public ObservationSequence (Observation parent,ObservationEvent executionEvent,long executionDelta,String sequenceName,String sequenceID,String sequenceFlag,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles) throws ParseException{
		this(parent,executionEvent,executionDelta,sequenceName,sequenceID,sequenceFlag,'P','S',sequenceParamaters,sequenceProfiles);
	}
	
	public ObservationSequence (Observation parent,ObservationEvent executionEvent,long executionDelta,String sequenceName,String sequenceID,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles) throws ParseException{
		this(parent,executionEvent,executionDelta,sequenceName,sequenceID,INSERT_FLAG,'P','S',sequenceParamaters,sequenceProfiles);
	}
	
	public ObservationSequence (Observation parent,ObservationEvent executionEvent,long executionDelta,String sequenceName,String sequenceID) throws ParseException{

		this(parent,executionEvent,executionDelta,sequenceName,sequenceID,INSERT_FLAG,'P','S',null,null);
	
	}
	/*public ObservationSequence copy(){
		ObservationSequence result=new ObservationSequence(parent,executionEvent
	}*/
	public ObservationSequence (Observation parent,ObservationEvent executionEvent,long executionDelta,Sequence seq){
		this(parent,executionEvent,executionDelta,seq.getName(),seq.getUniqueID(),seq.getFlag(),seq.getSource(),seq.getDestination(),seq.getParameters(),seq.getProfiles());
	}
	public ObservationEvent getExecutionTimeEvent(){
		return executionTimeEvent;
	}
	public long getExecutionTimeDelta(){
		return executionTimeDelta;
	}
	public void setExecutionTimeEvent(ObservationEvent event){
		executionTimeEvent=event;
	}
	
	protected void setObservation(Observation obs){
		parent=obs;
	}
	
	public void setExecutionTimeDelta(long delta){
		executionTimeDelta=delta;
	}
	
	public Observation getObs(){
		return parent;
	}
	
	public String getObservationName(){
		return parent.getName();
	}
	public String getInstrumentName(){
		String result=super.getInstrumentName();
		if (result.equals("UNKNOWN") && this.getObs()!=null) return this.getObs().getInstrument();
		return result;
	}

	@Override
	public Date getExecutionDate() {
		return new Date(parent.getDateForEvent(executionTimeEvent).getTime()+executionTimeDelta);
		// TODO Auto-generated method stub
		//return null;
	}



	@Override
	public void setExecutionTime(String time) throws ParseException {
		throw new IllegalArgumentException("Can not set the time to an observation Sequence");
		
	}
	
	public String toObsXml(int indent){
		String indentString="";
		for (int i=0;i<indent;i++){
			indentString=indentString+"\t";
		}
		String l1=indentString+"<sequence name=\""+getName()+"\">\n";
		String l6=indentString+"\t<executionTime>\n";
		String l7=indentString+"\t\t<actionTime>"+getExecutionTimeEvent().getName()+ObservationUtil.getOffset(this.getExecutionTimeDelta())+"</actionTime>\n";

		String l8=indentString+"\t</executionTime>\n";
		Parameter[] parameters = getParameters();
		String l9="";
		String l10="";
		String l11="";
		if (parameters!=null && parameters.length>0){
			l9=indentString+"\t<parameterList count=\""+parameters.length+"\">\n";
			for (int i=0;i<parameters.length;i++){
				if (parameters[i]==null) System.out.println("parameter is nulll:"+new Integer(i).toString()+getUniqueID());
				l10=l10+parameters[i].toXML(i+1, indent+2)+"\n";
			}
			l11=indentString+"\t</parameterList>\n";
		}
		String l12="";
		SequenceProfile[] profiles=getProfiles();
		if (profiles.length>0) l12=indentString+"\t<profileList count=\""+profiles.length+"\">\n";
		String l13="";
		String l14="";
		if (profiles.length>0){
			for (int i=0;i<profiles.length;i++){
				l13=l13+profiles[i].toXml(indent+2)+"\n";
			}
			l14=indentString+"\t</profileList>\n";

		}
		String l15=indentString+"</sequence>\n";
		return l1+l6+l7+l8+l9+l10+l11+l12+l13+l14+l15;
	}
	
	public String toObsXml(){
		return toObsXml(0);
	}



}
