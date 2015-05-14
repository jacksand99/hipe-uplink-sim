package vega.uplink.planning;


import java.util.Date;
import java.util.logging.Logger;

import herschel.ia.dataset.ArrayDataset;

//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;

//import org.w3c.dom.Document;





import vega.hipe.logging.VegaLog;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SuperPor;
import vega.uplink.planning.period.Plan;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.exclusion.AbstractExclusion;
import vega.uplink.track.Fecs;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.StringParameter;
import herschel.ia.numeric.String1d;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

/**
 * An Schedule is a timeline of all spacecraft activities.
 * It contains an Observation Schedule but also a PTSL with the maintenance activities and a PDFM to be able to decode the PTR
 * @author jarenas
 *
 */
public class Schedule extends MapContext implements ObservationListener{
	//private final Logger LOG = Logger.getLogger(Schedule.class.getName());
	private boolean ptrDirty;
	private boolean porDirty;
	private Por cachedPor;
	private Ptr cachedPtr;
	int counter;
	public Schedule(PtrSegment ptslSegment,ObservationsSchedule obs){

		super();
		counter=0;

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
			setObservationsSchedule(obs);
			getPor();
		}

		
	}
	public int getCounter(){
		return counter++;
	}
	public Schedule(PtrSegment ptslSegment){
		this (ptslSegment,null);
	}
	
	private String1d getItlIncludesString1d(){
		String1d itlIncludes;
		try{
			itlIncludes=(String1d)((ArrayDataset) this.get("itlIncludes")).getData();
		}catch (Exception e){
			itlIncludes=new String1d();
			this.set("itlIncludes", new ArrayDataset(itlIncludes));
		}
		return itlIncludes;
	}
	
	public void addItlInclude(String include){
		getItlIncludesString1d().append(include);
	}
	
	public String[] getItlIncludes(){
		return getItlIncludesString1d().toArray();
	}
	
	private String1d getEvfIncludesString1d(){
		String1d evfIncludes;
		try{
			evfIncludes=(String1d)((ArrayDataset) this.get("evfIncludes")).getData();
		}catch (Exception e){
			evfIncludes=new String1d();
			this.set("evfIncludes", new ArrayDataset(evfIncludes));
		}
		return evfIncludes;
	}
	
	public void addEvfInclude(String include){
		getEvfIncludesString1d().append(include);
	}
	
	public String[] getEvfIncludes(){
		return getEvfIncludesString1d().toArray();
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
	public void setPlan(Plan plan){
		this.setProduct("plan", plan);
	}
	
	public void setExclusion(AbstractExclusion ex){
		this.set("exclusion", ex);
	}
	public AbstractExclusion getExclusion(){
		try {
			return (AbstractExclusion) get("exclusion");
		} catch (Exception e) {
			return null;
		} 
		
	}
	public Plan getPlan(){
		try {
			return (Plan) this.getProduct("plan");
		} catch (Exception e) {
			return null;
		} 
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
	
	public void setFecs(Fecs fecs){
		this.set("fecs", fecs);
		
	}
	public Fecs getFecs(){
		try {
			return (Fecs) this.get("fecs");
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
	
	public Observation[] getObservations(){
		return getObservationsSchedule().getObservations();
	}
	
	public Ptr getPtr(){
		if (!ptrDirty) return cachedPtr;
		boolean oldListen = Observation.LISTEN;
		Observation.LISTEN=false;
		Ptr ptr= new Ptr();
		PtrSegment segment = this.getPtslSegment().copy();
		PointingBlocksSlice sl=new PointingBlocksSlice();
		Observation[] observations = getObservations();
		for (int i=0;i<observations.length;i++){
			Observation c = observations[i].copy();
			sl.setSlice(c);
		}
		segment.setSlice(sl);
		ptr.addSegment(segment);
		Ptr ptsl=new Ptr();
		ptsl.addSegment(this.getPtslSegment().copy());
		PtrUtils.rebasePtrPtsl(ptr, ptsl);
		try{
		Ptr tempPtr = ptr;
		cachedPtr=tempPtr;
		ptrDirty=false;
		Observation.LISTEN=oldListen;
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
		Plan plan = getPlan();
		if (plan!=null) result=result+plan.toXml(1)+"\n";
		AbstractExclusion exclusion = this.getExclusion();
		if (exclusion!=null) result=result+exclusion.toXml(1);
		result=result+"</schedule>\n";
		return result;
		
	}
	
	public void setFileName(String fileName){
		this.getMeta().set("fileName", new StringParameter(fileName));
	}
	
	public String getFileName(){
		try{
			return (String) this.getMeta().get("fileName").getValue();
		}catch (Exception e){
			return null;
		}
	}
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}
	public void targetChanged(DatasetEvent<Dataset> arg0) {
		VegaLog.info("ObservationSchedule Changed");
		super.targetChanged(arg0);
		
	}

	@Override
	public void observationChanged(ObservationChangeEvent event) {
		
	}

	@Override
	public void scheduleChanged() {
		VegaLog.info("Both POR and PTR dirty");
		ptrDirty=true;
		porDirty=true;
	}

	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		scheduleChanged();
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		VegaLog.info("Listenerd change in pointing");
		VegaLog.info("PTR dirty");
		ptrDirty=true;
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		VegaLog.info("POR dirty");
		porDirty=true;
		
	}
	public Schedule getPeriodSchedule(Date startDate,Date endDate){
		Schedule result= new Schedule(this.getPtslSegment());
		result.setPdfm(this.getPdfm());
		result.setFileName(this.getFileName());
		Observation[] allObs = this.getObservations();
		for (int i=0;i<allObs.length;i++){
			if (allObs[i].getStartDate().atOrAfter(new FineTime(startDate)) && allObs[i].getEndDate().atOrBefore(new FineTime(endDate))){
				result.addObservation(allObs[i].copy());
			}
		}
		return result;
		
	}
	public Schedule getInstrumentSchedule(String instrument){
		Schedule result= new Schedule(this.getPtslSegment());
		result.setPdfm(this.getPdfm());
		result.setFileName(this.getFileName());
		Observation[] allObs = this.getObservations();
		for (int i=0;i<allObs.length;i++){
			if (allObs[i].getInstrument().equals(instrument)){
				result.addObservation(allObs[i].copy());
			}
		}
		return result;
	}

	
	

	
}
