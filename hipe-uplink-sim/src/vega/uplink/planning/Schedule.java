package vega.uplink.planning;


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

/**
 * An Schedule is a timeline of all spacecraft activities.
 * It contains an Observation Schedule but also a PTSL with the maintenance activities and a PDFM to be able to decode the PTR
 * @author jarenas
 *
 */
public class Schedule extends MapContext implements ObservationListener{
	private final Logger LOG = Logger.getLogger(Schedule.class.getName());
	private boolean ptrDirty;
	private boolean porDirty;
	private Por cachedPor;
	private Ptr cachedPtr;
	public Schedule(PtrSegment ptslSegment,ObservationsSchedule obs){

		super();
		
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

		
	}
	
	public Schedule(PtrSegment ptslSegment){
		this (ptslSegment,null);
	}
	
	
	public void setPtslSegment(PtrSegment ptslSegment){
		this.setProduct("ptslSegment", ptslSegment);
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

		} 
	}
	
	public void setObservationsSchedule(ObservationsSchedule obs){
		ptrDirty=true;
		porDirty=true;
		this.setProduct("observationsSchedule", obs);
		obs.addObservationListener(this);
		
	}
	public void setPdfm(Pdfm pdfm){
		this.setProduct("pdfm", pdfm);
		
	}
	public Pdfm getPdfm(){
		try {
			return (Pdfm) this.getProduct("pdfm");
		} catch (Exception e) {
			return null;
		} 
	}
	public ObservationsSchedule getObservationsSchedule(){
		try {
			return (ObservationsSchedule) this.getProduct("observationsSchedule");
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
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

	}
	
	public Por getPor(){
		if (!porDirty){
			return cachedPor;
		}
		Ptr ptr= new Ptr();
		PtrSegment segment = this.getPtslSegment().copy();
		Observation[] orgObservations = getObservations();
		Observation[] observations=new Observation[orgObservations.length];
		for (int i=0;i<orgObservations.length;i++){
			observations[i]=orgObservations[i].copy();
		}
		for (int i=0;i<observations.length;i++){
			segment.setSlice(observations[i]);
		}
		ptr.addSegment(segment);
		Ptr ptsl=new Ptr();
		ptsl.addSegment(this.getPtslSegment().copy());
		Ptr tempPtr =PtrUtils.rebasePtrPtsl(ptr, ptsl);
		cachedPtr=tempPtr;
		ptrDirty=false;
		SuperPor result=new SuperPor();
		for (int i=0;i<observations.length;i++){
			result.addPor(observations[i].getCommanding());
		}
		cachedPor=result;
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
		
	}

	@Override
	public void scheduleChanged() {
		LOG.info("Both POR and PTR dirty");
		ptrDirty=true;
		porDirty=true;
	}

	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		scheduleChanged();
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		LOG.info("PTR dirty");
		ptrDirty=true;
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		LOG.info("POR dirty");
		porDirty=true;
		
	}

	
	

	
}
