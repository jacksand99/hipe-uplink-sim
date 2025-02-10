package vega.uplink.commanding;

import herschel.ia.dataset.StringParameter;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.time.FineTime;


import herschel.share.interpreter.InterpreterUtil;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.TreeMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vega.hipe.products.AbstractXmlMapContext;
import vega.uplink.DateUtil;
import vega.uplink.Properties;


	
/**
 * Class to store a Payload Operation Request (POR), is a set of sequences to be executed at the spacecraft
 * @author jarenas
 *
 */
public class Por extends AbstractXmlMapContext implements SequenceTimelineInterface{
	/**
	 * Rosetta Testing Team
	 */
	static private String AUTHOR="SOL-SOC";
	TreeMap<String,AbstractSequence> sequenceMap;
	boolean calculateValidity;
	String1d initModes;
	String1d initMS;
	String1d initMemory;
	String1d initDataStore;
	public Por copy(){
		Por result = new Por();
		AbstractSequence[] seqs = this.getSequences();
		AbstractSequence[] newSeq=new AbstractSequence[seqs.length] ;
		for (int i=0;i<seqs.length;i++){
			newSeq[i]=(AbstractSequence) seqs[i].copy();
		}
		result.setSequences(newSeq);
		result.setInitModes(getInitModes().copy());
		result.setInitMS(getInitMS().copy());
		result.setInitMemory(getInitMemory().copy());
		result.setInitDataStore(getInitDataStore().copy());
		return result;
		
	}
	public Por (){
		super();
		setCreator(AUTHOR);
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));
		setVersion("1");
		this.setType("POR");
		this.setCreationDate(new FineTime(new java.util.Date()));
		sequenceMap=new TreeMap<String,AbstractSequence>();
		calculateValidity=true;
		initModes=new String1d();
		initMS=new String1d();
		initMemory=new String1d();
		initDataStore=new String1d();

	}
	/**
	 * In the case that the POR has been loaded form an ITL, get the itl lines to init the modes
	 * @return
	 */
	public String1d getInitModes(){
		return initModes;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, get the itl lines to init the MS
	 * @return
	 */	
	public String1d getInitMS(){
		return initMS;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, get the itl lines to init the memory
	 * @return
	 */
	public String1d getInitMemory(){
		return initMemory;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, get the itl lines to init the data store
	 * @return
	 */
	public String1d getInitDataStore(){
		return initDataStore;
	}

	
	/**
	 * In the case that the POR has been loaded form an ITL, set the itl lines to init the modes
	 * @param im
	 */
	public void setInitModes(String1d im){
		initModes=im;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, set the itl lines to init the MS
	 * @param ims
	 */	
	public void setInitMS(String1d ims){
		initMS=ims;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, set the itl lines to init the memory
	 * @param ims
	 */
	public void setInitMemory(String1d ims){
		initMemory=ims;
	}
	/**
	 * In the case that the POR has been loaded form an ITL, set the itl lines to init the data store
	 * @param ims
	 */
	public void setInitDataStore(String1d ims){
		initDataStore=ims;
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
	
	/**
	 * Set the version of the POR
	 * @param version
	 */
	public void setVersion(String version){
		getMeta().set("version", new StringParameter(version));
	}
	
	/**
	 * Get the version of the POR
	 * @return
	 */
	public String getVersion(){
		return (String) getMeta().get("version").getValue();
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
	/**
	 * Remove a sequence from the POR. The sequence that will removed will be the one with the same UniqueID as the one passed
	 * @param sequence
	 */
	public void removeSequence(SequenceInterface sequence){
		this.getRefs().remove(sequence.getUniqueID());
		sequenceMap.remove(sequence.getUniqueID());
	}
	/**
	 * Remove all sequences form the POR
	 */
	public void removeAllSequences(){
		AbstractSequence[] seqs = this.getSequences();
		for (int i=0;i<seqs.length;i++){
			removeSequence(seqs[i]);
		}
	}
	/**
	 * Regenerate a POR form another oen. It will remove all sequences from this POR and add the sequences from the given one, keeping the rest of the metadata as it is in this one.
	 * @param por
	 */
	public void regenerate(Por por){
		this.removeAllSequences();
		this.setSequences(por.getSequences());
	}

	/**
	 * Set it to True if the validity of the POR should be recalculated for any new sequence added or false otherwise
	 * @param recalcualte
	 */
	public void  setCalculateValidity(boolean recalcualte){
		calculateValidity=recalcualte;
		if (recalcualte) calculateValidity();
	}
	
	protected void calculateValidity(){
		if (!calculateValidity) return;
		Mib mib;
		try {
			mib = Mib.getMib();
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not get Mib "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		AbstractSequence[] seqs=getSequences();
		int size=seqs.length;
		java.util.Date lower=null;
		java.util.Date higher=null;
		
		for (int i=0;i<size;i++){
			java.util.Date exDate=seqs[i].getExecutionDate();
			java.util.Date endDate=new Date(seqs[i].getExecutionDate().getTime()+(1000*mib.getTotalSequenceDuration(seqs[i].getName())));
			if (lower==null) lower=exDate;
			if (higher==null) higher=endDate;
			if (exDate.before(lower)) lower=exDate;
			if (endDate.after(higher)) higher=endDate;
		}
		if (lower!=null)  this.setStartDate(new FineTime(lower));

		if (higher!=null){
			//higher
			this.setEndDate(new FineTime(higher));
		}

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
	/**
	 * Get a subpor with all sequences from a date to a date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Por getSubPor(Date startDate,Date endDate){
		Por result=new Por();
		AbstractSequence[] seqs = this.findOverlapingSequences(startDate, endDate);
		result.setSequences(seqs);
		return result;
	}
	/**
	 * Get a subpor with all sequences from a given instrument
	 * @param instrument
	 * @return
	 */
	public Por getSubPor(String instrument){
		Vector<AbstractSequence> vector= new Vector<AbstractSequence>();
		
		AbstractSequence[] seqs=getSequences();
		for (int i=0;i<seqs.length;i++){
			if (seqs[i].getInstrumentName().equals(instrument)) vector.add(seqs[i]);
		}
		Sequence[] array=new Sequence[vector.size()];
		vector.toArray(array);
		Por result = new Por();
		result.setSequences(array);
		return result;
	}
	/**
	 * get all sequences with execution date in range of dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public AbstractSequence[] findOverlapingSequences(Date startDate,Date endDate){
		java.util.Vector<AbstractSequence> affected=new java.util.Vector<AbstractSequence>();
		AbstractSequence[] seqs=getSequences();
		for (int i=0;i<seqs.length;i++){
			AbstractSequence cs = seqs[i];
			boolean con1=false;
			boolean con2=false;
			if (cs.getExecutionDate().after(endDate) ) con1=true;
			if (cs.getExecutionDate().before(startDate)) con2=true;
			if (!con1 && !con2) affected.add(cs);
		}

		AbstractSequence[] result= new AbstractSequence[affected.size()];
		result=affected.toArray(result);
		return result;
	}
	
	/**
	 * Set the generation date of a POR
	 * @param date
	 */
	public void setGenerationDate(java.util.Date date){
		this.setCreationDate(new FineTime(date));
	}
	
	/**
	 * Set the generation date of the POR as String in DOY format (yyyy-D'T'HH:mm:ss.SSS'Z')
	 * @param time
	 * @throws ParseException
	 */
	public void setGenerationTime(String time) throws ParseException{
		setGenerationDate(DateUtil.DOYToDate(time));
	}
	/**
	 * Get the genration date of the POR
	 * @return
	 */
	public java.util.Date getGenerationDate(){
		return getCreationDate().toDate();
	}
	
	/**
	 * Get teh generation date of the POR as string in DOY format (yyyy-D'T'HH:mm:ss.SSS'Z')
	 * @return
	 */
	public String getGenerationTime(){
		return DateUtil.dateToDOYNoMilli(getCreationDate().toDate());
	}
	
	/**
	 * Get the validity dates for this POR
	 * @return an array of size 2 where the element 0 is the start date and element 1 is the end date
	 */
	public java.util.Date[] getValidityDates(){
		Date[] result = new java.util.Date[2];
		result[0]=getStartDate().toDate();
		result[1]=getEndDate().toDate();
		return result;
	}
	
	/**
	 * Set the validity dates for this POR.
	 * @param dates n array of size 2 where the element 0 is the start date and element 1 is the end date
	 */
	public void setValidityDates(java.util.Date[] dates){
		setStartDate(new FineTime(dates[0]));
		setEndDate(new FineTime(dates[1]));

	}
	
	/**
	 * Set the validity dates for this POR as strings in DOY format (yyyy-D'T'HH:mm:ss.SSS'Z')
	 * @param times an array of size 2 where the element 0 is the start date and element 1 is the end date
	 * @throws ParseException
	 */
	public void setValidityTimes(String[] times) throws ParseException{
		setStartDate(new FineTime(DateUtil.DOYToDate(times[0])));
		setEndDate(new FineTime(DateUtil.DOYToDate(times[1])));
	}
	
	/**
	 * Get the validity dates for this POR as strings in DOY format (yyyy-D'T'HH:mm:ss.SSS'Z')
	 * @return an array of size 2 where the element 0 is the start date and element 1 is the end date
	 */
	public String[] getValidityTimes(){
		String[] result=new String[2];
		result[0]=DateUtil.dateToDOYNoMilli(getStartDate().toDate());
		result[1]=DateUtil.dateToDOYNoMilli(getEndDate().toDate());
		return result;
	}
	
	public void setSequences(SequenceInterface[] porSequences){
		setCalculateValidity(false);
		for (int i=0;i<porSequences.length;i++){
			addSequence(porSequences[i]);
		}
		setCalculateValidity(true);
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
		//rootElement.setAttribute("xmlns:xs", "http://www.w3.org/2001/XMLSchema");
		rootElement.setAttribute("xsi:noNamespaceSchemaLocation", "solPlanningData.xsd");
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
	
	private void addDefaultParameters(){
		Mib mib=null;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get Mib:"+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		AbstractSequence[] seq = this.getSequences();
		for (int i=0;i<seq.length;i++){
			Parameter[] dp = mib.getDefaultParameters(seq[i].getName());
			for (int j=0;j<dp.length;j++){
				try{
					Parameter rp = seq[i].getParameter(dp[j].getName());
					if (rp==null) seq[i].addParameter(dp[j]);
				}catch (Exception e){
					seq[i].addParameter(dp[j]);
				}
			}
		}
	}

	
	public String toXml(){
		Mib mib;
		try{
			mib = Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		addDefaultParameters();
		AbstractSequence[] seqs=getOrderedSequences();
		String l01="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		String l02="<planningData xsi:noNamespaceSchemaLocation=\"solPlanningData.xsd\"\n              xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n";
		String l03="\t<commandRequests>\n";
		String l04="\t\t<header type=\"POR\" formatVersion=\"1\">\n";
		String l05="\t\t\t<genTime>"+getGenerationTime()+"</genTime>\n";
		String l06="\t\t\t<validityRange type=\"absoluteTime\">\n";
		String l07="\t\t\t\t<startTime>"+getValidityTimes()[0]+"</startTime>\n";
		String l08="\t\t\t\t<stopTime>"+getValidityTimes()[1]+"</stopTime>\n";
		String l09="\t\t\t</validityRange>\n";
		String l10="\t\t</header>\n";
		String l11="\t\t<occurrenceList count=\""+seqs.length+"\" creationTime=\""+getGenerationTime()+"\" author=\""+getCreator()+"\">\n";
		//String l12="";
		StringBuilder l12=new StringBuilder();
		l12.append("");
		int size=seqs.length;
		for (int i=0;i<size;i++){
			int c = i+1;
			l12.append("\t\t\t"+"<!-- Sequence "+seqs[i].getName()+" ("+c+") -->\n");
			l12.append("\t\t\t"+"<!-- Description: "+mib.getSequenceDescription(seqs[i].getName())+" -->\n");
			
			l12.append(seqs[i].toXml(3)+"\n");
		}
		String l13="\t\t</occurrenceList>\n";
		String l14="\t</commandRequests>\n";
		String l15="</planningData>\n";
		StringBuilder result=new StringBuilder();
		result.append(l01);
		result.append(l02);
		result.append(l03);
		result.append(l04);
		result.append(l05);
		result.append(l06);
		result.append(l07);
		result.append(l08);
		result.append(l09);
		result.append(l10);
		result.append(l11);
		result.append(l12);
		result.append(l13);
		result.append(l14);
		result.append(l15);
		return result.toString();
		
	}
	
	public AbstractSequence[] getOrderedSequences(){
		OrderedSequences ordered = new OrderedSequences();
		AbstractSequence[] arraySeq=getSequences();
		for (int i=0;i<arraySeq.length;i++){
			ordered.put(arraySeq[i].getExecutionDate(), arraySeq[i]);
		}
		return ordered.toSequenceArray();
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
	
	/**
	 * Get a POR with the sequences for a given instrument
	 * @param instrument
	 * @return
	 */
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
	@Override
	public String getXmlData() {
		return this.toXml();
	}
	@Override
	public void setFileName(String newFileName) {
		setName(newFileName);
		
	}
	@Override
	public String getFileName() {
		return getName();
	}
	@Override
	public void setXmlData(String data) {
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
			Document doc;
	
			doc = dBuilder.parse(stream);
			Por tempPor = PorUtils.readPorfromDocument(doc);
			this.removeAllSequences();
			this.setSequences(tempPor.getSequences());
			
		}catch (Exception e){
			IllegalArgumentException iae=new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		
	}
	@Override
	public void saveAs(String file) throws FileNotFoundException,
			UnsupportedEncodingException {
		PorUtils.writePORtofile(file, this);
		
	}
	@Override
	public void save() throws FileNotFoundException,
			UnsupportedEncodingException {
		PorUtils.savePor(this);
		
	}
	
	
	class OrderedSequences extends TreeMap<Date,AbstractSequence>{
		public AbstractSequence put(Date key,AbstractSequence value){
			AbstractSequence former = this.get(key);
			if (former==null) return super.put(key, value);
			return put(new Date(key.getTime()+1),value);
		}
		public AbstractSequence[] toSequenceArray(){
			AbstractSequence[] result=new AbstractSequence[this.size()];
			result=this.values().toArray(result);
			return result;
		}
	}
	
}
