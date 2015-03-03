package vega.uplink.commanding;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;

import vega.uplink.Properties;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import herschel.ia.dataset.ArrayDataset;
import herschel.ia.dataset.StringParameter;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

public class SimulationContext extends MapContext{
	private static SimulationContext context;
	
	public SimulationContext(Date startDate,Date endDate){
		this();
		setStartDate(new FineTime(startDate));
		setEndDate(new FineTime(endDate));
	}
	public SimulationContext() {
		super();
		init();
	}
	
	private void init(){
		getMeta().set("initScript", new StringParameter(Properties.getProperty(Properties.DEFAULT_INIT_SCRIPT)));
		getMeta().set("postScript", new StringParameter(Properties.getProperty(Properties.DEFAULT_POST_SCRIPT)));
		getMeta().set("planningPeriod", new StringParameter("unknownPlanningPeriod"));
		
		set("historyModes",new HistoryModes());
		set("powerInstrument",new PowerInstrument());
		set("executionDates",new ArrayDataset(new Long1d()));
		set("zRecordDates",new ArrayDataset(new Long1d()));
		set("historyPower",new ArrayDataset(new Float1d()));
		set("mocPowerHistory",new ArrayDataset(new Float1d()));
		set("historyPowerZ",new ArrayDataset(new Float1d()));
		setProduct("por",new SuperPor());
		setProduct("dl_por",new Por());		
		try{
			set("orcd",Orcd.readORCDfile(Properties.getProperty(Properties.ORCD_FILE)));
		}catch(Exception e){
			set("orcd",Orcd.readORCDfromJar());

		}
		try{
			set("mocPower",MocPower.ReadFromFile(Properties.getProperty(Properties.PWPL_FILE)));
		}catch (Exception e){
			set("mocPower",MocPower.ReadFromJar());

		}
		//set("modelState",ModelState.getModelState());
		set("modelState",new ModelState());
		set("fecs",new Fecs());
		setProduct("ptr",new Ptr());
		setProduct("pdfm",new Pdfm());
		set("log",new ArrayDataset(new String1d()));
		

	}
	
	
	public HistoryModes getHistoryModes(){
		return (HistoryModes) get("historyModes");
	}
	
	public PowerInstrument getPowerInstrument(){
		return (PowerInstrument) get("powerInstrument");
	}
	
	public Long1d getExecutionDates(){
		 return (Long1d) ((ArrayDataset) get("executionDates")).getData();
	}
	
	public Long1d getZRecordDates(){
		 return (Long1d) ((ArrayDataset) get("zRecordDates")).getData();
		
	}
	
	public Float1d getHistoryPower(){
		 return (Float1d) ((ArrayDataset) get("historyPower")).getData();
		
	}
	public Float1d getMocPowerHistory(){
		 return (Float1d) ((ArrayDataset) get("mocPowerHistory")).getData();
		
	}
	
	public Float1d getHistoryPowerZ(){
		 return (Float1d) ((ArrayDataset) get("historyPowerZ")).getData();
		
	}
	
	public SuperPor getPor(){
		try {
			return (SuperPor) getProduct("por");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public Por getDlPor(){
		try {
			return (Por) getProduct("dl_por");
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	public Orcd getOrcd(){
		return (Orcd) get("orcd");
	}
	
	public MocPower getMocPower(){
		return (MocPower) get("mocPower");
	}
	
	public ModelState getModelState(){
		return (ModelState) get("modelState");
		
	}
	
	public Fecs getFecs(){
		return (Fecs) get("fecs");
	}
	
	public void setFecs(Fecs fecs){
		set("fecs",fecs);
	}
	
	public Ptr getPtr(){
		try {
			return (Ptr) getProduct("ptr");
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	public Pdfm getPdfm(){
		try {
			return (Pdfm) getProduct("pdfm");
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPtr(Ptr ptr){
		setProduct("ptr",ptr);
	}
	public void setPdfm(Pdfm pdfm){
		setProduct("pdfm",pdfm);
	}

	public void setPlanningPeriod(String period){
		getMeta().set("planningPeriod", new StringParameter(period));
	}
	
	public String getPlanningPeriod(){
		return (String) getMeta().get("planningPeriod").getValue();
	}

	
	public void setInitScript(String script){
		getMeta().set("initScript", new StringParameter(script));
	}
	public void setPostScript(String script){
		getMeta().set("postScript", new StringParameter(script));
	}
	
	public String getInitScript(){
		return (String) getMeta().get("initScript").getValue();
	}
	public String getPostScript(){
		return (String) getMeta().get("postScript").getValue();
	}
	
	public void log(String message){
		((String1d) ((ArrayDataset) get("log")).getData()).append(message);
	}
	
	public String getLog(){
		String result="";
		String1d log=((String1d) ((ArrayDataset) get("log")).getData());
		int size = log.getSize();
		//String[] lines=log.toArray();
		for (int i=0;i<size;i++){
			result=result+log.get(i)+"\n";
		}
		return result;
	}
	
	
	public void reset(){
		init();
		getModelState().reset();
	}
	
	
	public SimulationContext copy(){
		return (SimulationContext) super.copy();
	}
}
