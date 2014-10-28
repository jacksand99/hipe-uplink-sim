package vega.uplink.planning;

import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.MetaDataListener;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.ProductListener;
import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;














import herschel.share.interpreter.InterpreterUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
//import java.io.IOException;
//import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.util.Log;
import org.w3c.dom.Document;

import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceInterface;
import vega.uplink.commanding.SequenceTimelineInterface;
import vega.uplink.planning.gui.ScheduleModel;
//import vega.uplink.planning.Schedule.ObservationScheduleListener;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlockInterface;
import vega.uplink.pointing.PointingBlockSetInterface;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.attitudes.DerivedPhaseAngle;
import vega.uplink.pointing.attitudes.Track;

public class Observation extends MapContext implements PointingBlockSetInterface,SequenceTimelineInterface{
	//private Date startDate;
	//private Date endDate;
	//String name;
	//private Vector<ObservationListener> listeners;
	private java.util.HashSet<ObservationListener> listeners;
	private final Logger LOG = Logger.getLogger(Observation.class.getName());
	public static boolean LISTEN=true;
	private Observation(){
		super();
		listeners=new java.util.HashSet<ObservationListener>();
	}
	public Observation (Date startDate,Date endDate){
		super();
		listeners=new java.util.HashSet<ObservationListener>();
		//listeners=new Vector<ObservationListener>();
		this.setStartDate(new FineTime(startDate));
		this.setEndDate(new FineTime(endDate));
		/*this.startDate=startDate;
		this.endDate=endDate;*/
		//Por por=new Por();
		//por.addProductListener(new ObservationMemberListener(this));
		this.setProduct("sequences", new Por());
		this.setProduct("pointing", new PointingBlocksSlice());
		this.setName(""+new Date().getTime());
		setFileName("OBS_"+getName()+".ROS");
		setPath(Properties.getProperty("user.home"));
		this.getCommanding().addProductListener(new ObservationMemberListener(this));
		this.getPointing().addProductListener(new ObservationMemberListener(this));
		
		//this.getMeta().addMetaDataListener(new ObservationMetadataListener(this));
		new ObservationMetadataListener(this);
		//this.addProductListener(new ObservationMetadataListener(this));
		//LOG.info("Added metadata listener");

	}
	
	public void setFileName(String fileName){
		this.getMeta().set("fileName", new StringParameter(fileName));
		this.metaChange(null);
	}
	
	public static Observation getDefaultObservation(){
		Date date = new Date();
		Observation result = new Observation(date,new Date(date.getTime()+3600000));
		PointingBlock defaultPointing = new PointingBlock("OBS", new Date(), new Date());
		defaultPointing.setAttitude(new DerivedPhaseAngle(new Track()));
		ObservationPointingBlock obsBlock1 = new ObservationPointingBlock(result,ObservationEvent.START_OBS,0,ObservationEvent.END_OBS,0,defaultPointing);
		result.addObservationBlock(obsBlock1);
		return result;
	}
	
	public void addObservationListener(ObservationListener newListener){
		//Thread.dumpStack();
		//LOG.info("Added Observation Listener");
		listeners.add(newListener);
	}
	public void removeObservationListener(ObservationListener listener){
		//LOG.info("removed Observation Listener");
		listeners.remove(listener);
	}
	
	protected void fireChange(DatasetEvent<Product> source){
		LOG.info("Firing Observation change");
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		//Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			it.next().observationChanged(ev);
		}
	}
	
	public void metaChange(DatasetEvent<MetaData> source){
		if (!LISTEN) return;
		//LOG.info("Firing Observation Metadata change");
		ObservationChangeEvent ev = new ObservationChangeEvent(this);
		
		Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
		//Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			it.next().metadataChanged(ev);;
		}
	}
	protected void commandingChange(DatasetEvent<Product> source){
		if (!LISTEN) return;
		//LOG.info("Firing Observation Commanding change");
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
	
	public String getFileName(){
		return (String) this.getMeta().get("fileName").getValue();
	}
	
	
	public Observation copy(){
		Observation result = new Observation();
		result.setMeta(getMeta().copy());
		result.setProduct("sequences", getCommanding().copy());
		PointingBlocksSlice newPointing = new PointingBlocksSlice();
		PointingBlock[] blocks = getPointing().getBlocks();
		for (int i=0;i<blocks.length;i++){
			blocks[i]=blocks[i].copy();
			((ObservationPointingBlock) blocks[i]).setParent(result);
		}
		newPointing.setBlocks(blocks);
		result.setProduct("pointing", newPointing);
		return result;
		/*result.setMeta(getMeta().copy());
		
    	String text = this.toXml();
    	

		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try{
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
			Document doc;
	
			doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			//Node node = (Node) doc;
			//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
			Observation tempobs = ObservationUtil.readObservationFromDoc(doc);
			Iterator<ObservationListener> it = new Vector<ObservationListener>(listeners).iterator();
			while (it.hasNext()){
				tempobs.addObservationListener(it.next());
			}
			return tempobs;
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw iae;
			
		}*/
	}
	
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
		//this.fireChange();
		
	}
	public void setName(String newName){
		//LOG.info("Observation changed name");
		boolean oldListen = Observation.LISTEN;
		Observation.LISTEN=false;
		this.getMeta().set("name", new StringParameter(newName));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
		
		//fireChange(null);
		
	}
	
	public String getName(){
		return (String) this.getMeta().get("name").getValue();
	}
	public Date getObsStartDate(){
		return getStartDate().toDate();
	}
	
	public Date getObsEndDate(){
		return getEndDate().toDate();
	}
	
	public void setObsStartDate(Date date){
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.setStartDate(new FineTime(date));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
	}
	
	public void setObsEndDate(Date date){
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.setEndDate(new FineTime(date));
		Observation.LISTEN=oldListen;
		this.metaChange(null);
	}
	
	
	public Por getCommanding(){
		try {
			return (Por) this.getProduct("sequences");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not get the commanding part of the observation");
			iae.initCause(e);
			throw(iae);
		}

	}
	
	public PointingBlocksSlice getPointing(){
		try {
			return (PointingBlocksSlice) this.getProduct("pointing");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			IllegalArgumentException iae = new IllegalArgumentException("Can not get the pointing part of the observation");
			iae.initCause(e);
			throw(iae);
		}
		
	}
	@Override
	public void addSequence(SequenceInterface sequence) {
		throw new IllegalArgumentException("Can not add a sequence to an observation");
		//getCommanding().addSequence(sequence);
		// TODO Auto-generated method stub
		
	}
	public void addObservationSequence(ObservationSequence sequence) {
		//throw new IllegalArgumentException("Can not add a sequence to an observation");
		getCommanding().addSequence(sequence);
		//fireChange();

		// TODO Auto-generated method stub
		
	}

	@Override
	public SequenceInterface[] getSequencesForDate(Date date) {
		// TODO Auto-generated method stub
		return getCommanding().getSequencesForDate(date);
	}

	@Override
	public void setSequences(SequenceInterface[] porSequences) {
		throw new IllegalArgumentException("Can not add sequences to an Observation");
		//getCommanding().setSequences(porSequences);
		
	}
	public void setObservationSequences(ObservationSequence[] porSequences) {
		getCommanding().setSequences(porSequences);
		//fireChange();

	}

	@Override
	public SequenceInterface[] getSequences() {
		// TODO Auto-generated method stub
		return getCommanding().getSequences();
	}

	@Override
	public String toXml() {
		return toXml(0);
		// TODO Auto-generated method stub
		//return null;
	}

	@Override
	public SequenceInterface[] getOrderedSequences() {
		// TODO Auto-generated method stub
		return getCommanding().getOrderedSequences();
	}

	@Override
	public SequenceInterface getSequenceBefore(Date date) {
		// TODO Auto-generated method stub
		return getCommanding().getSequenceBefore(date);
	}

	@Override
	public void regenerate(PointingBlockSetInterface slice) {
		//getPointing().regenerate(slice);
		
	}

	/*@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}*/

	/*@Override
	public void setName(String newName) {
		this.name=newName;
		
	}*/

	@Override
	public void removeBlock(PointingBlockInterface block) {
		// TODO Auto-generated method stub
		getPointing().removeBlock(block);
		//fireChange();

		
	}

	@Override
	public PointingBlockInterface blockBefore(PointingBlockInterface block) {
		// TODO Auto-generated method stub
		return getPointing().blockBefore(block);
	}

	@Override
	public PointingBlockInterface blockAfter(PointingBlockInterface block) {
		// TODO Auto-generated method stub
		return getPointing().blockAfter(block);
	}

	@Override
	public void addBlock(PointingBlockInterface newBlock) {
		throw new IllegalArgumentException("Can not add a block to an Observation");
		// TODO Auto-generated method stub
		//getPointing().addBlock(newBlock);
		
	}
	public void addObservationBlock(ObservationPointingBlock newBlock) {
		//throw new IllegalArgumentException("Can not add a block to an Observation");
		// TODO Auto-generated method stub
		getPointing().addBlock(newBlock);
		//fireChange();
		
	}

	@Override
	public PointingBlockInterface[] getBlocks() {
		// TODO Auto-generated method stub
		return getPointing().getBlocks();
	}

	@Override
	public void setBlocks(PointingBlockInterface[] newBlocks) {
		throw new IllegalArgumentException("Can not set Blocks to observation");
		// TODO Auto-generated method stub
		//getPointing().setBlocks(newBlocks);
		
	}
	public void setObservationBlocks(ObservationPointingBlock[] newBlocks) {
		//throw new IllegalArgumentException("Can not set Blocks to observation");
		// TODO Auto-generated method stub
		getPointing().setBlocks(newBlocks);
		//fireChange();

		
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
		result=result+iString+"\t<startDate>"+PointingBlock.dateToZulu(getObsStartDate())+"</startDate>\n";
		result=result+iString+"\t<endDate>"+PointingBlock.dateToZulu(getObsEndDate())+"</endDate>\n";
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
		
		
		// TODO Auto-generated method stub
		return result;
	}

	@Override
	public PointingBlock getBlockAt(Date time) {
		// TODO Auto-generated method stub
		return getPointing().getBlockAt(time);
	}

	@Override
	public PointingBlockSetInterface getBlocksAt(Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		return getPointing().getBlocksAt(startTime, endTime);
	}

	@Override
	public void removeBlocks(PointingBlock[] blocks) {
		// TODO Auto-generated method stub
		getPointing().removeBlocks(blocks);
		//fireChange();

		
	}

	@Override
	public PointingBlockSetInterface getAllBlocksOfType(String blockType) {
		// TODO Auto-generated method stub
		return getPointing().getAllBlocksOfType(blockType);
	}

	@Override
	public void setSlice(PointingBlockSetInterface slice) {
		throw new IllegalArgumentException("Can not set a slice into an observation");
		// TODO Auto-generated method stub
		//getPointing().setSlice(slice);
		
	}
	public void setObservationSlice(Observation slice) {
		//throw new IllegalArgumentException("Can not set a slice into an observation");
		// TODO Auto-generated method stub
		getPointing().setSlice(slice);
		//fireChange();

		
	}
	
	public Date getDateForEvent(ObservationEvent event){
		if (event.equals(ObservationEvent.START_OBS)) return getObsStartDate();
		if (event.equals(ObservationEvent.END_OBS)) return getObsEndDate();
		
		return null;
	}
	
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	
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
		private final Logger LOG = Logger.getLogger(ObservationMemberListener.class.getName());
		private Observation parent;
		public ObservationMemberListener(Observation parent){
			
			this.parent=parent;
		}
		@Override
		public void targetChanged(DatasetEvent<Product> arg0) {
			//System.out.println(arg0);
			Product source = arg0.getTarget();
			if (InterpreterUtil.isInstance(Por.class, source)){
				parent.commandingChange(arg0);
				return;
			}
			if (InterpreterUtil.isInstance(PointingBlocksSlice.class, source)){
				parent.pointingChange(arg0);
				return;
			}

			//System.out.println(source);
			// TODO Auto-generated method stub
			
			//LOG.info("Observation Member Changed");
			//LOG.info(""+arg0);
			parent.fireChange(arg0);
			//parent.refsChanged();
		}
		
	}
	
	class ObservationMetadataListener implements herschel.ia.dataset.MetaDataListener  {
		private final Logger LOG = Logger.getLogger(ObservationMetadataListener.class.getName());
		private Observation parent;
		public ObservationMetadataListener(Observation parent){
			//LOG.info("Cerated metadata listener");
			this.parent=parent;
			parent.getMeta().addMetaDataListener(this);
		}
		@Override
		public void targetChanged(DatasetEvent<MetaData> arg0) {
			//System.out.println("metadatachanged");
			//LOG.info("metadata changed");
			//System.out.println("metadata changed");
			parent.metaChange(arg0);// TODO Auto-generated method stub
			//System.out.println("metadatachanged");
			
		}
		/*@Override
		public void targetChanged(DatasetEvent<Product> arg0) {
			// TODO Auto-generated method stub
			LOG.info("metadata changed");
		}*/
		
	}
	public String toString(){
		return this.getName();
	}
	
	/*public void addListener(ObservationListener listener){
		listeners.add(listener);
		this.addProductListener(arg0);
		//this.getMeta().addMetaDataListener(listener);
	}
	
	private void fireChange(){
		Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			it.next().ObservationChanged(this);
		}
	}*/

	

}
