package vega.uplink.planning;

//import java.io.IOException;
//import java.security.GeneralSecurityException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import vega.uplink.commanding.Por;
import vega.uplink.commanding.SuperPor;
import vega.uplink.planning.gui.ScheduleViewer;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlockInterface;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.Context;
import herschel.ia.pal.MapContext;
import herschel.ia.dataset.ProductListener;

public class Schedule extends MapContext implements ObservationListener{
	//private ObservationScheduleListener listener;
	private final Logger LOG = Logger.getLogger(Schedule.class.getName());
	private boolean ptrDirty;
	private boolean porDirty;
	private Por cachedPor;
	private Ptr cachedPtr;
	//protected CompositeDataset fds;
	public Schedule(PtrSegment ptslSegment,ObservationsSchedule obs){

		super();
		//LOG.info("Setting both ptr and por dirty");
		//ptrDirty=true;
		//porDirty=true;
		
		//getPtr();
		//fds=new CompositeDataset();
		//this.set("fds", fds);
		//listener=new ObservationScheduleListener(this);
		
		if (ptslSegment==null){
			throw new IllegalArgumentException("An schedulle must have a PTSL segment");
		}
		if (obs==null){
			cachedPtr=new Ptr();
			cachedPtr.addSegment(ptslSegment.copy());
			obs=new ObservationsSchedule();
			obs.addObservationListener(this);
			this.setProduct("observationsSchedule", obs);
			setPtslSegment(ptslSegment);
			cachedPor=new Por();
			ptrDirty=false;
			porDirty=false;
		}else{
			setPtslSegment(ptslSegment);
			getPor();
		}
		//if (obs==null) obs=new ObservationsSchedule();

		
	}
	
	public Schedule(PtrSegment ptslSegment){
		this (ptslSegment,null);
	}
	
	/*public void addScheduleListener(ObservationListener newListener){
		this.getObservationsSchedule().addObservationListener(newListener);
	}*/
	
	public void setPtslSegment(PtrSegment ptslSegment){
		this.setProduct("ptslSegment", ptslSegment);
		//this.addProductListener(listener);
		//ptslSegment.aaddProductListener(this);
	}
	
	public void addObservationListener(ObservationListener newListener){
		this.getObservationsSchedule().addObservationListener(newListener);
	}
	
	public PtrSegment getPtslSegment(){
		try {
			return (PtrSegment) this.getProduct("ptslSegment");
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}
	
	public void setObservationsSchedule(ObservationsSchedule obs){
		ptrDirty=true;
		porDirty=true;
		this.setProduct("observationsSchedule", obs);
		obs.addObservationListener(this);
		
		//this.addProductListener(listener);
		//obs.addProductListener(this);
	}
	public void setPdfm(Pdfm pdfm){
		this.setProduct("pdfm", pdfm);
		
		//this.addProductListener(listener);
		//obs.addProductListener(this);
	}
	public Pdfm getPdfm(){
		try {
			return (Pdfm) this.getProduct("pdfm");
		} catch (Exception e) {
			return null;
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}
	public ObservationsSchedule getObservationsSchedule(){
		try {
			return (ObservationsSchedule) this.getProduct("observationsSchedule");
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} 
	}
	
	public void addObservation(Observation obs){
		getObservationsSchedule().addObservation(obs);
	}
	
	public void removeObservation(Observation obs){
		getObservationsSchedule().removeObs(obs);
	}
	
	public void removeObservation(int index){
		getObservationsSchedule().removeObs(index);
	}
	
	public Observation[] getObservations(){
		return getObservationsSchedule().getObservations();
	}
	
	public Ptr getPtr(){
		if (!ptrDirty) return cachedPtr;
		boolean oldListen = Observation.LISTEN;
		Observation.LISTEN=false;
		Ptr ptr= new Ptr();
		PtrSegment segment = this.getPtslSegment().copy();
		Observation[] observations = getObservations();
		for (int i=0;i<observations.length;i++){
			Observation c = observations[i].copy();
			PointingBlockInterface[] b = c.getBlocks();
			for (int j=0;j<b.length;j++){
				//b[j].addMetadata(new PointingMetadata());
				PointingMetadata meta = b[j].getMetadataElement();
				if (meta==null) meta=new PointingMetadata();
				meta.addComment(c.getInstrument());
				meta.addComment(c.getName());
				meta.addComment(c.getDescription());
				meta.addComment(c.getCreator());
				b[j].setMetadata(meta);
				
			}
			segment.setSlice(c);
		}
		ptr.addSegment(segment);
		Ptr ptsl=new Ptr();
		ptsl.addSegment(this.getPtslSegment().copy());
		PtrUtils.rebasePtrPtsl(ptr, ptsl);
		try{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		InputStream stream = new ByteArrayInputStream(ptr.toXml().getBytes(StandardCharsets.UTF_8));
		Document doc;

		doc = dBuilder.parse(stream);
		doc.getDocumentElement().normalize();
		//Node node = (Node) doc;
		//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
		Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
		Observation.LISTEN=oldListen;
		cachedPtr=tempPtr;
		ptrDirty=false;
		
		return tempPtr;
		
		}catch (Exception e){
			Observation.LISTEN=true;
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		//Observation.LISTEN=true;
		//segment.setSlice(slice);
	}
	
	public Por getPor(){
		if (!porDirty){
			//LOG.info("POR is not dirty, returnning cahced version");
			return cachedPor;
		}
		Ptr ptr= new Ptr();
		//LOG.info("Copying PTSL");
		PtrSegment segment = this.getPtslSegment().copy();
		//LOG.info("Finsih getting PTSL");
		//LOG.info("Get Observations");
		Observation[] orgObservations = getObservations();
		//LOG.info("Finish getting observations");
		//LOG.info("Get copy of observations");
		Observation[] observations=new Observation[orgObservations.length];
		for (int i=0;i<orgObservations.length;i++){
			observations[i]=orgObservations[i].copy();
		}
		//LOG.info("finish Get copy of observations");
		//LOG.info("set observations as slices");
		for (int i=0;i<observations.length;i++){
			segment.setSlice(observations[i]);
		}
		//LOG.info("finish set observations as slices");
		//LOG.info("add segment to ptr");
		ptr.addSegment(segment);
		//LOG.info("finsih add segment to ptr");
		Ptr ptsl=new Ptr();
		ptsl.addSegment(this.getPtslSegment().copy());
		//LOG.info("rebase ptr");
		Ptr tempPtr =PtrUtils.rebasePtrPtsl(ptr, ptsl);
		//LOG.info("finsih rebase ptr");
		cachedPtr=tempPtr;
		ptrDirty=false;
		SuperPor result=new SuperPor();
		//Observation[] observations = getObservations();
		for (int i=0;i<observations.length;i++){
			result.addPor(observations[i].getCommanding());
		}
		cachedPor=result;
		//LOG.info("POR cached, dirty=false");
		porDirty=false;
		return result;
	}
	
	public String toXml(){
		String result="";
		result=result+"<schedule>\n";
		result=result+getObservationsSchedule().toXml(1);
		result=result+getPtslSegment().toXml(1);
		Pdfm pdfm = this.getPdfm();
		String pdfmXml="";
		if (pdfm!=null){
			pdfmXml = pdfm.toXml();
			pdfmXml=pdfmXml.replace("<?xml version=\"1.0\"?>\n", "");
		}
		result=result+pdfmXml+"\n";
		result=result+"</schedule>\n";
		return result;
		
	}
	
	public void setFileName(String fileName){
		this.getMeta().set("fileName", new StringParameter(fileName));
	}
	
	public String getFileName(){
		return (String) this.getMeta().get("fileName").getValue();
	}
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	public void targetChanged(DatasetEvent<Dataset> arg0) {
		LOG.info("ObservationSchedule Changed");
		super.targetChanged(arg0);
		
	}

	@Override
	public void observationChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scheduleChanged() {
		LOG.info("Both POR and PTR dirty");
		// TODO Auto-generated method stub
		ptrDirty=true;
		porDirty=true;
	}

	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		scheduleChanged();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		LOG.info("PTR dirty");
		ptrDirty=true;
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		LOG.info("POR dirty");
		porDirty=true;
		// TODO Auto-generated method stub
		
	}

	/*@Override
	public int compareTo(ObservationListener o) {
		Schedule other=null;
		try{
			other=(Schedule) o;
		}catch (Exception e){
			
		}
		// TODO Auto-generated method stub
		return 0;
	}*/
	
	
	/*class ObservationScheduleListener implements ProductListener{
		private final Logger LOG = Logger.getLogger(ObservationScheduleListener.class.getName());
		private Schedule parent;
		public ObservationScheduleListener(Schedule parent){
			this.parent=parent;
		}
		@Override
		public void targetChanged(DatasetEvent<Product> arg0) {
			// TODO Auto-generated method stub
			
			LOG.info("ObservationSchedule Changed");
			LOG.info(""+arg0);
			
			parent.refsChanged();
		}
		
	}*/

	
}
