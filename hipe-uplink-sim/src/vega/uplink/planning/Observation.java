package vega.uplink.planning;

import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.EventType;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.ProductListener;
import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SequenceInterface;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.commanding.SequenceTimelineInterface;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlockInterface;
import vega.uplink.pointing.PointingBlockSetInterface;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.attitudes.DerivedPhaseAngle;
import vega.uplink.pointing.attitudes.Track;

/**
 * An Observation defines an activity of the spacecraft both in pointing and commanding.
 * All commands and pointing blocks times are relative to events, the start observation event and the end observation event.
 * This allows to move the observation in the timeline and all the pointing blocks and command sequences will move with it.
 * @author jarenas
 *
 */
public class Observation extends MapContext implements PointingBlockSetInterface,SequenceTimelineInterface,java.lang.Comparable<Observation>{
	private java.util.HashSet<ObservationListener> listeners;
	//private final Logger LOG = Logger.getLogger(Observation.class.getName());
	public static boolean LISTEN=true;
	public java.util.SortedMap<String,Date> eventMap;

	private Observation(){
		super();
		listeners=new java.util.HashSet<ObservationListener>();
		eventMap=new java.util.TreeMap<String,Date>();
	}
	
	public void setEventMap(SortedMap<String,Date> map){
		eventMap=map;
	}
	/**
	 * Creates an Observation with a start time and end time
	 * @param startDate
	 * @param endDate
	 */
	public Observation (Date startDate,Date endDate){
		//super();
		//listeners=new java.util.HashSet<ObservationListener>();
		this();
		this.setStartDate(new FineTime(startDate));
		this.setEndDate(new FineTime(endDate));
		this.setProduct("sequences", new ObservationPor(this));
		this.setProduct("pointing", new ObservationPointingSlice(this));
		this.setName(""+new Date().getTime());
		setFileName("OBS_"+getName()+".ROS");
		setPath(Properties.getProperty("user.home"));
		this.getCommanding().addProductListener(new ObservationMemberListener(this));
		this.getPointing().addProductListener(new ObservationMemberListener(this));
		
		new ObservationMetadataListener(this);

	}
	
	/**
	 * Set the file name that will be used when this Observation will be saved to a file
	 * @param fileName
	 */
	public void setFileName(String fileName){
		this.getMeta().set("fileName", new StringParameter(fileName));
		this.metaChange(null);
	}
	
	/**
	 * Static method that allows to create an Observation with start time equals to th ecurrent time and the end time one hour after.
	 * The POinting will be defines as power optimized.
	 * @return Default Observation
	 */
	public static Observation getDefaultObservation(){
		Date date = new Date();
		Observation result = new Observation(date,new Date(date.getTime()+3600000));
		PointingBlock defaultPointing = new PointingBlock("OBS", new Date(), new Date());
		defaultPointing.setAttitude(new DerivedPhaseAngle(new Track()));
		ObservationPointingBlock obsBlock1 = new ObservationPointingBlock(result,ObservationEvent.START_OBS,0,ObservationEvent.END_OBS,0,defaultPointing);
		result.addObservationBlock(obsBlock1);
		return result;
	}
	
	/**
	 * Add a listener to the changes of this Observation
	 * @param newListener
	 */
	public void addObservationListener(ObservationListener newListener){
		listeners.add(newListener);
	}
	/**
	 * Remove a listener to the changes in this Observation
	 * @param listener
	 */
	public void removeObservationListener(ObservationListener listener){
		listeners.remove(listener);
	}
	
	/**
	 * Add an ebent to the list of events that can be used in this observation. Only the events
	 * START_OBS, END OBS and the ones added via this method can be used in the observation
	 * @param event The event to be added
	 * @param date The date co-related to this event
	 */
	public void addEvent(ObservationEvent event,Date date){
		eventMap.put(event.getName(), date);
	}
	
	/**
	 * Remove an event from the list of events that can be used in this observation
	 * @param event
	 */
	public void removeEvent(ObservationEvent event){
		eventMap.remove(event.getName());
	}


	protected void fireChange(DatasetEvent<Product> source){
		VegaLog.info("Firing Observation change");
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		while (it.hasNext()){
			it.next().observationChanged(ev);
		}
	}
	
	public void metaChange(DatasetEvent<MetaData> source){
		if (!LISTEN) return;
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		while (it.hasNext()){
			it.next().metadataChanged(ev);;
		}
	}
	protected void commandingChange(DatasetEvent<Product> source){
		if (!LISTEN) return;
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		while (it.hasNext()){
			it.next().commandingChanged(ev);;
		}
	}
	protected void pointingChange(DatasetEvent<Product> source){
		if (!LISTEN) return;
		//LOG.info("Firing Observation Pointing change");
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		while (it.hasNext()){
			it.next().pointingChanged(ev);;
		}
	}
	
	/**
	 * Get the filename desired for the file holding this Observation
	 * @return
	 */
	public String getFileName(){
		return (String) this.getMeta().get("fileName").getValue();
	}
	
	
	public Observation copy(){
		Observation result = new Observation();
		result.setMeta(getMeta().copy());
		AbstractSequence[] seq = this.getCommanding().getSequences();
		ObservationSequence[] newSeq=new ObservationSequence[seq.length];
		for (int i=0;i<seq.length;i++){
			newSeq[i]=new ObservationSequence(result,((ObservationSequence)seq[i]).getExecutionTimeEvent(),((ObservationSequence)seq[i]).getExecutionTimeDelta(),(ObservationSequence)seq[i]);
		}
		ObservationPor por = new ObservationPor(result);
		por.setSequences(newSeq);
		//result.getCommanding().setSequences(newSeq);
		result.setProduct("sequences", por);
		ObservationPointingSlice newPointing = new ObservationPointingSlice(result);
		PointingBlock[] blocks = getPointing().getBlocks();
		for (int i=0;i<blocks.length;i++){
			blocks[i]=blocks[i].copy();
			((ObservationPointingBlock) blocks[i]).setParent(result);
		}
		newPointing.setBlocks(blocks);
		result.setProduct("pointing", newPointing);
		return result;
	}
	
	/**
	 * Get the duration of this observation in milliseconds
	 * @return
	 */
	public long getDurationMilliSecs(){
		return this.getEndDate().toDate().getTime()-this.getStartDate().toDate().getTime();
	}
	
	public void regenerate(Observation obs){
		
		this.setMeta(obs.getMeta());
		this.remove("sequences");
		this.remove("pointing");
		this.setProduct("sequences", obs.getCommanding());
		this.setProduct("pointing", obs.getPointing());
		this.metaChange(null);
		this.pointingChange(null);
		this.commandingChange(null);
		
	}
	public void setName(String newName){
		//LOG.info("Observation changed name");
		boolean oldListen = Observation.LISTEN;
		Observation.LISTEN=false;
		this.getMeta().set("name", new StringParameter(newName));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
		
	}
	
	public String getName(){
		return (String) this.getMeta().get("name").getValue();
	}
	/**
	 * Get the start time of this observation
	 * @return
	 */
	public Date getObsStartDate(){
		return getStartDate().toDate();
	}
	
	/**
	 * Get the end time of this Observation
	 * @return
	 */
	public Date getObsEndDate(){
		return getEndDate().toDate();
	}
	
	/**
	 * Set the start time of this Observation
	 * @param date
	 */
	public void setObsStartDate(Date date){
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.setStartDate(new FineTime(date));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
	}
	
	/**
	 * Set the end time of this Observation
	 * @param date
	 */
	public void setObsEndDate(Date date){
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.setEndDate(new FineTime(date));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
	}
	
	
	/**
	 * Get the commanding part of this Observation
	 * @return
	 */
	public Por getCommanding(){
		try {
			return (Por) this.getProduct("sequences");
		} catch (Exception e) {
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not get the commanding part of the observation");
			iae.initCause(e);
			throw(iae);
		}

	}
	

	/*public Por getCommanding(){
		ObservationPor result=(ObservationPor) getCommanding();
		//result.sed(sed);
		return result;
	}*/
	
	/**
	 * Set the pointing part of an Observation
	 * @param slice The blocks defining the pointing for the observation
	 */
	public void setPointing(ObservationPointingSlice slice){
		slice.setObservation(this);
		this.setProduct("pointing", slice);
		DatasetEvent<Product> event = new DatasetEvent<Product>(this,EventType.DATA_CHANGED,null,null);
		this.pointingChange(null);
	}
	
	/**
	 * Set the commanding part of the observation
	 * @param por The sequences defining the commanding part of the observation
	 */
	public void setCommanding(ObservationPor por){
		por.setObservation(this);
		this.setProduct("commanding", por);
		this.commandingChange(null);
	}
	
	
	
	/**
	 * Get the pointing part of this Observation
	 * @return
	 */
	public PointingBlocksSlice getPointing(){
		try {
			return (PointingBlocksSlice) this.getProduct("pointing");
		} catch (Exception e) {
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not get the pointing part of the observation");
			iae.initCause(e);
			throw(iae);
		}
		
	}
	@Override
	public void addSequence(SequenceInterface sequence) {
		throw new IllegalArgumentException("Can not add a sequence to an observation");
		
	}
	/**
	 * Add a Sequernce to this Observation
	 * @param sequence
	 */
	public void addObservationSequence(ObservationSequence sequence) {
		sequence.setObservation(this);
		getCommanding().addSequence(sequence);
		
	}

	@Override
	public SequenceInterface[] getSequencesForDate(Date date) {
		return getCommanding().getSequencesForDate(date);
	}

	@Override
	public void setSequences(SequenceInterface[] porSequences) {
		throw new IllegalArgumentException("Can not add sequences to an Observation");
		
	}
	/**
	 * Set the sequences of this Observation
	 * @param porSequences
	 */
	public void setObservationSequences(ObservationSequence[] porSequences) {
		getCommanding().setSequences(porSequences);

	}

	@Override
	public SequenceInterface[] getSequences() {
		return getCommanding().getSequences();
	}

	@Override
	public String toXml() {
		return toXml(0);
	}

	@Override
	public SequenceInterface[] getOrderedSequences() {
		return getCommanding().getOrderedSequences();
	}

	@Override
	public SequenceInterface getSequenceBefore(Date date) {
		return getCommanding().getSequenceBefore(date);
	}

	@Override
	public void regenerate(PointingBlockSetInterface slice) {
		
	}


	@Override
	public void removeBlock(PointingBlockInterface block) {
		getPointing().removeBlock(block);

		
	}

	@Override
	public PointingBlockInterface blockBefore(PointingBlockInterface block) {
		return getPointing().blockBefore(block);
	}

	@Override
	public PointingBlockInterface blockAfter(PointingBlockInterface block) {
		return getPointing().blockAfter(block);
	}

	@Override
	public void addBlock(PointingBlockInterface newBlock) {
		throw new IllegalArgumentException("Can not add a block to an Observation");
		
	}
	
	/**
	 * Add a pointiong block tho this Observation
	 * @param newBlock
	 */
	public void addObservationBlock(ObservationPointingBlock newBlock) {
		newBlock.setParent(this);
		getPointing().addBlock(newBlock);
		
	}

	@Override
	public PointingBlockInterface[] getBlocks() {
		return getPointing().getBlocks();
	}

	@Override
	public void setBlocks(PointingBlockInterface[] newBlocks) {
		throw new IllegalArgumentException("Can not set Blocks to observation");
		
	}
	/**
	 * Set the pointing blocks of this Observation
	 * @param newBlocks
	 */
	public void setObservationBlocks(ObservationPointingBlock[] newBlocks) {
		getPointing().setBlocks(newBlocks);

		
	}

	@Override
	public String toXml(int indent) {
		
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		result=result+iString+"<observation>\n";
		result=result+iString+"\t<name>"+getName()+"</name>\n";
		result=result+iString+"\t<type>"+getType()+"</type>\n";
		result=result+iString+"\t<creator>"+getCreator()+"</creator>\n";
		result=result+iString+"\t<description>"+getDescription()+"</description>\n";
		result=result+iString+"\t<instrument>"+getInstrument()+"</instrument>\n";
		result=result+iString+"\t<formatVersion>"+getFormatVersion()+"</formatVersion>\n";		
		result=result+iString+"\t<startDate>"+DateUtil.dateToZulu(getObsStartDate())+"</startDate>\n";
		result=result+iString+"\t<endDate>"+DateUtil.dateToZulu(getObsEndDate())+"</endDate>\n";
		result=result+iString+"\t<sequences>\n";
		AbstractSequence[] seq = getCommanding().getOrderedSequences();
		for (int i=0;i<seq.length;i++){
			result=result+((ObservationSequence) seq[i]).toObsXml(2+indent);
		}
		result=result+iString+"\t</sequences>\n";
		result=result+iString+"\t<pointingBlocks>\n";
		PointingBlock[] blocks = getPointing().getBlocks();
		for (int i=0;i<blocks.length;i++){
			result=result+((ObservationPointingBlock) blocks[i]).toObsXml(2+indent);
		}
		
		result=result+iString+"\t</pointingBlocks>\n";
		result=result+iString+"</observation>";
		
		return result;
	}
	
	/**
	 * Get a string with the commanding part of the observation in ITL format
	 * @return
	 */
	public String toItl(){
		Mib mib;
			try {
				mib=Mib.getMib();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				IllegalArgumentException iae = new IllegalArgumentException("Could not init mib "+e.getMessage());
				iae.initCause(e);
				throw(iae);
			}
			TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
			Por POR = this.getCommanding();
			String result="";

			String l05="";
			AbstractSequence[] tempSeq=POR.getSequences();
			Parameter[] tempParam;
			SequenceProfile[] tempPro;
			for (int i=0;i<tempSeq.length;i++){
				String eventName=((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName();
				Integer count = counter.get(eventName);
				if (count==null) count=0;
				else count=count+1;
				counter.put(eventName, count);
				String itlEvent=((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName();
				itlEvent=itlEvent.replace(" ", "_");
				l05=l05+itlEvent+" "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[i]).getExecutionTimeDelta())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n";
				tempParam = tempSeq[i].getParameters();
				for (int z=0;z<tempParam.length;z++){
					l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+"\n";
				}
				tempPro=tempSeq[i].getProfiles();
				String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
				String powerProfile="\tPOWER_PROFILE = \t\t\t";
				boolean dataRatePresent=false;
				boolean powerProfilePresent=false;
				for (int j=0;j<tempPro.length;j++){
					if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
						dataRateProfile=dataRateProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]";
						dataRatePresent=true;
					}
					if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
						powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
						powerProfilePresent=true;
					}
					
				}
				if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
				if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

				l05=l05+"\t\t\t\t)\n";

			}
			result=l05;
			return result;
		
	}

	@Override
	public PointingBlock getBlockAt(Date time) {
		return getPointing().getBlockAt(time);
	}

	@Override
	public PointingBlockSetInterface getBlocksAt(Date startTime, Date endTime) {
		return getPointing().getBlocksAt(startTime, endTime);
	}

	@Override
	public void removeBlocks(PointingBlock[] blocks) {
		getPointing().removeBlocks(blocks);

		
	}

	@Override
	public PointingBlockSetInterface getAllBlocksOfType(String blockType) {
		return getPointing().getAllBlocksOfType(blockType);
	}

	@Override
	public void setSlice(PointingBlockSetInterface slice) {
		throw new IllegalArgumentException("Can not set a slice into an observation");
		
	}
	
	/**
	 * Merge to Observations (on the pointing)
	 * @param slice
	 */
	public void setObservationSlice(Observation slice) {
		getPointing().setSlice(slice);

		
	}
	
	/**
	 * Get teh actual date for an event
	 * @param event
	 * @return
	 */
	public Date getDateForEvent(ObservationEvent event){
		if (event.equals(ObservationEvent.START_OBS)) return getObsStartDate();
		if (event.equals(ObservationEvent.END_OBS)) return getObsEndDate();
		return eventMap.get(event.getName());
		
		//return null;
	}
	
	/**
	 * Get all the events defined for this observation
	 * @return
	 */
	public ObservationEvent[] getDefinedEvents(){
		ObservationEvent[] result=new ObservationEvent[eventMap.size()];
		Iterator<String> it = eventMap.keySet().iterator();
		int counter=0;
		while (it.hasNext()){
			result[counter]=new ObservationEvent(it.next());
			counter++;
		}
		//result=eventMap.keySet().toArray(result);
		return result;
	}
	
	/**
	 * Set the desired path when saving this Observation
	 * @param path
	 */
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	/**
	 * Set the desired path when saving this Observation
	 * @return
	 */
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	
	/**
	 * Shift an event defined for this observation a number of milliseconds
	 * @param event The event to be shifted
	 * @param delta The number of milliseconds to shift
	 */
	public void shiftEvent(ObservationEvent event,long delta){
		if (event.equals(ObservationEvent.START_OBS)){
			Date oldStartDate = this.getStartDate().toDate();
			Date newStartDate=new Date(oldStartDate.getTime()+delta);
			this.setStartDate(new FineTime(newStartDate));
		}
		if (event.equals(ObservationEvent.END_OBS)){
			Date oldendDate = this.getEndDate().toDate();
			Date newEndDate=new Date(oldendDate.getTime()+delta);
			this.setEndDate(new FineTime(newEndDate));
		}
		
	}
	
	class ObservationMemberListener implements ProductListener{
		private Observation parent;
		public ObservationMemberListener(Observation parent){
			
			this.parent=parent;
		}
		@Override
		public void targetChanged(DatasetEvent<Product> arg0) {
			Product source = arg0.getTarget();
			if (InterpreterUtil.isInstance(Por.class, source)){
				parent.commandingChange(arg0);
				return;
			}
			if (InterpreterUtil.isInstance(PointingBlocksSlice.class, source)){
				parent.pointingChange(arg0);
				return;
			}

			parent.fireChange(arg0);
		}
		
	}
	
	class ObservationMetadataListener implements herschel.ia.dataset.MetaDataListener  {
		private Observation parent;
		public ObservationMetadataListener(Observation parent){
			this.parent=parent;
			parent.getMeta().addMetaDataListener(this);
		}
		@Override
		public void targetChanged(DatasetEvent<MetaData> arg0) {
			parent.metaChange(arg0);// TODO Auto-generated method stub
			
		}
		
	}
	public String toString(){
		return this.getName();
	}
	@Override
	public int compareTo(Observation o) {
		if (this.getObsStartDate().before(o.getObsStartDate())) return -1;
		if (this.getObsStartDate().after(o.getObsStartDate())) return 1;
		return 1;
	}
	

	

}
