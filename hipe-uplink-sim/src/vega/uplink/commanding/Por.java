package vega.uplink.commanding;

//import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;
//import herschel.share.interpreter.InterpreterUtil;
//import herschel.share.predicate.Predicate;


import herschel.share.interpreter.InterpreterUtil;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vega.uplink.Properties;


	
public class Por extends MapContext implements SequenceTimelineInterface{
	static private String AUTHOR="Rosetta Testing Team";
	TreeMap<String,AbstractSequence> sequenceMap;
	
	public Por (){
		super();
		setCreator(AUTHOR);
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));
		this.setType("POR");
		this.setCreationDate(new FineTime(new java.util.Date()));
		sequenceMap=new TreeMap<String,AbstractSequence>();
	}
	
	public void setName(String name){
		getMeta().set("name", new StringParameter(name));
	}
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getName(){
		return (String) getMeta().get("name").getValue();
	}
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	public void addSequence(SequenceInterface sequence){
		if (InterpreterUtil.isInstance(AbstractSequence.class, sequence)){
			setProduct(sequence.getUniqueID(),(AbstractSequence)sequence);
			sequenceMap.put(sequence.getUniqueID(), (AbstractSequence)sequence);
			calculateValidity();
		}else{
			addSequence(new Sequence(sequence));
		}
	}

	/*public void addSequence(AbstractSequence sequence){
		setProduct(sequence.getUniqueID(),sequence);
		sequenceMap.put(sequence.getUniqueID(), sequence);
		calculateValidity();
	}*/
	
	protected void calculateValidity(){
		AbstractSequence[] seqs=getSequences();
		int size=seqs.length;
		java.util.Date lower=null;
		java.util.Date higher=null;
		
		for (int i=0;i<size;i++){
			java.util.Date exDate=seqs[i].getExecutionDate();
			if (lower==null) lower=exDate;
			if (higher==null) higher=exDate;
			if (exDate.before(lower)) lower=exDate;
			if (exDate.after(higher)) higher=exDate;
		}
		if (lower!=null)  this.setStartDate(new FineTime(lower));

		if (higher!=null) this.setEndDate(new FineTime(higher));

	}
	
	public AbstractSequence[] getSequencesForDate(java.util.Date date){
		Vector<AbstractSequence> vector= new Vector<AbstractSequence>();
		
		AbstractSequence[] seqs=getSequences();
		for (int i=0;i<seqs.length;i++){
			if (seqs[i].getExecutionDate().equals(date)) vector.add(seqs[i]);
		}
		Sequence[] result=new Sequence[vector.size()];
		vector.toArray(result);
		return result;
	}
	
	public void setGenerationDate(java.util.Date date){
		this.setCreationDate(new FineTime(date));
	}
	
	public void setGenerationTime(String time) throws ParseException{
		setGenerationDate(Sequence.zuluToDate(time));
	}
	public java.util.Date getGenerationDate(){
		return getCreationDate().toDate();
	}
	
	public String getGenerationTime(){
		return Sequence.dateToZulu(getCreationDate().toDate());
	}
	
	public java.util.Date[] getValidityDates(){
		Date[] result = new java.util.Date[2];
		result[0]=getStartDate().toDate();
		result[1]=getEndDate().toDate();
		return result;
	}
	
	public void setValidityDates(java.util.Date[] dates){
		setStartDate(new FineTime(dates[0]));
		setEndDate(new FineTime(dates[1]));

	}
	
	public void setValidityTimes(String[] times) throws ParseException{
		setStartDate(new FineTime(Sequence.zuluToDate(times[0])));
		setEndDate(new FineTime(Sequence.zuluToDate(times[1])));
	}
	
	public String[] getValidityTimes(){
		String[] result=new String[2];
		result[0]=Sequence.dateToZulu(getStartDate().toDate());
		result[1]=Sequence.dateToZulu(getEndDate().toDate());
		return result;
	}
	
	public void setSequences(SequenceInterface[] porSequences){
		for (int i=0;i<porSequences.length;i++){
			addSequence(porSequences[i]);
		}
		calculateValidity();
	}
	
	public AbstractSequence[] getSequences(){
		int size=sequenceMap.size();
		Sequence[] result=new  Sequence[size];
		sequenceMap.values().toArray(result);
		return result;
	}
	protected Document getXMLDocument() throws ParserConfigurationException{
		AbstractSequence[] seqs=getOrderedSequences();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("planningData");
		rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		rootElement.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
		rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "rosPlanningData.xsd");
		Element eleCommandRequest=doc.createElement("commandRequests");
		Element eleHeader=doc.createElement("header");
		eleHeader.setAttribute("type", "POR");
		eleHeader.setAttribute("formatVersion", "1");
		Element eleGenTime=doc.createElement("genTime");
		eleGenTime.setTextContent(getGenerationTime());
		Element eleValidityRange=doc.createElement("validityRange");
		eleValidityRange.setAttribute("type", "absoluteTime");
		Element eleStartTime=doc.createElement("startTime");
		eleStartTime.setTextContent(getValidityTimes()[0]);
		Element eleStopTime=doc.createElement("stopTime");
		eleStopTime.setTextContent(getValidityTimes()[1]);
		eleValidityRange.appendChild(eleStartTime);
		eleValidityRange.appendChild(eleStopTime);
		eleHeader.appendChild(eleGenTime);
		eleHeader.appendChild(eleValidityRange);
		eleCommandRequest.appendChild(eleHeader);
		Element eleOcurrenceList=doc.createElement("occurrenceList");
		eleOcurrenceList.setAttribute("count", ""+seqs.length);
		eleOcurrenceList.setAttribute("creationTime", getGenerationTime());
		eleOcurrenceList.setAttribute("author", getCreator());
		int size=seqs.length;
		for (int i=0;i<size;i++){
			eleOcurrenceList.appendChild(((Sequence) seqs[i]).getXMLElement(doc));
		}
		eleCommandRequest.appendChild(eleOcurrenceList);
		rootElement.appendChild(eleCommandRequest);
		doc.appendChild(rootElement);
		return doc;
		
		
	}

	
	public String toXml(){
		AbstractSequence[] seqs=getOrderedSequences();
		String l01="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String l02="<planningData xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n              xmlns:xs=\"http://www.w3.org/2001/XMLSchema\"\n              xsi:noNamespaceSchemaLocation=\"rosPlanningData.xsd\">\n";
		String l03="\t<commandRequests>\n";
		String l04="\t\t<header type=\"POR\" formatVersion=\"1\">\n";
		String l05="\t\t\t<genTime>"+getGenerationTime()+"</genTime>\n";
		String l06="\t\t\t<validityRange type=\"absoluteTime\">\n";
		String l07="\t\t\t\t<startTime>"+getValidityTimes()[0]+"</startTime>\n";
		String l08="\t\t\t\t<stopTime>"+getValidityTimes()[1]+"</stopTime>\n";
		String l09="\t\t\t</validityRange>\n";
		String l10="\t\t</header>\n";
		String l11="\t\t<occurrenceList count=\""+seqs.length+"\" creationTime=\""+getGenerationTime()+"\" author=\""+getCreator()+"\">\n";
		String l12="";
		int size=seqs.length;
		for (int i=0;i<size;i++){
			l12=l12+seqs[i].toXml(3)+"\n";
		}
		String l13="\t\t</occurrenceList>\n";
		String l14="\t</commandRequests>\n";
		String l15="</planningData>\n";
		return l01+l02+l03+l04+l05+l06+l07+l08+l09+l10+l11+l12+l13+l14+l15;
		
	}
	
	public AbstractSequence[] getOrderedSequences(){
		
		AbstractSequence[] arraySeq=getSequences();
		if (arraySeq==null) return new Sequence[0];
		java.util.Vector<AbstractSequence> result =new java.util.Vector<AbstractSequence>();
		long[] times;
		java.util.Set<Long> set = new HashSet<Long>();
		Long newTime;
		for (int i=0;i<arraySeq.length;i++){
			newTime=new Long(arraySeq[i].getExecutionDate().getTime());
			set.add(newTime);
		}
		times=new long[set.size()];
		Iterator<Long> it = set.iterator();
		int counter=0;
		while(it.hasNext()) {
		  times[counter]=(it.next()).longValue();
		  counter++;
		}
		Arrays.sort(times);
		for (int i=0;i<times.length;i++){
			AbstractSequence[] seqDate = this.getSequencesForDate(new java.util.Date(times[i]));
			for (int j=0;j<seqDate.length;j++) result.add(seqDate[j]);
		}
		Sequence[] ret=new Sequence[result.size()];
		result.toArray(ret);
		return ret;
	}
	
	protected AbstractSequence[] insertInOrder(AbstractSequence[] arr,AbstractSequence newSeq){
		AbstractSequence[] result=new AbstractSequence[0];
		if (arr.length==0) return insertSequenceAt(arr,newSeq,0);
		if (newSeq.getExecutionDate().after(arr[arr.length-1].getExecutionDate())){
			result=this.insertSequenceAt(arr, newSeq, arr.length);
		} 
		
		for (int i=0;i<arr.length;i++){
			if (newSeq.getExecutionDate().equals(arr[i].getExecutionDate())){
				result=this.insertSequenceAt(arr, newSeq, i);
			}
			
			if (newSeq.getExecutionDate().before(arr[i].getExecutionDate())){
				result=this.insertSequenceAt(arr, newSeq, i);
			}
			
		}
		return result;
	}
	
	protected AbstractSequence[] insertSequenceAt(AbstractSequence[] arr,AbstractSequence newSeq, int index){
		int size=arr.length;
		AbstractSequence[] result = new AbstractSequence[size+1];
		for (int i=0;i<index;i++){
			result[i]=arr[i];
		}
		result[index]=newSeq;
		for (int i=index;i<size;i++){
			result[i+1]=arr[i];
		}
		return result;
	}
	
	public AbstractSequence getSequenceBefore(java.util.Date date){
		AbstractSequence[] tempSeqs=this.getOrderedSequences();
		AbstractSequence result=null;
		for (int i=0;i<tempSeqs.length;i++){
			if (tempSeqs[i].getExecutionDate().before(date) || tempSeqs[i].equals(date)){
				result=tempSeqs[i];
			}
		}
		return result;
		
	}
	
	public Por getPORforInstrument(String instrument){
		Por result= new Por();
		result.setValidityDates(this.getValidityDates());
		AbstractSequence[] seq=this.getOrderedSequences();
		for (int i=0;i<seq.length;i++){
			if (seq[i].getInstrument().equals(instrument)){
				result.addSequence(seq[i]);
			}
		}
		
		return result;
	}
	
	/*private class SequenceRule implements herschel.share.predicate.Predicate<Product> {

		@Override
		public Predicate<Product> and(Predicate<Product> arg0) {
			return this;
			//return (evaluate(arg0) && arg0.evaluate(arg0)); 
			// TODO Auto-generated method stub
			//return null;
		}

		@Override
		public boolean evaluate(Product arg0) {
			return InterpreterUtil.isInstance(Sequence.class, arg0);
			// TODO Auto-generated method stub
			//return false;
		}

		@Override
		public Predicate<Product> or(Predicate<Product> arg0) {
			return this;
			// TODO Auto-generated method stub
			//return null;
		}
		
	}*/
	

	
}
