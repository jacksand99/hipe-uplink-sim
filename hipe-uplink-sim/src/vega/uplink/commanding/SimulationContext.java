package vega.uplink.commanding;

import java.io.IOException;
import java.security.GeneralSecurityException;

import vega.uplink.Properties;
import vega.uplink.pointing.Ptr;
import herschel.ia.dataset.ArrayDataset;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;
import herschel.ia.pal.MapContext;
//import herschel.share.util.Configuration;

public class SimulationContext extends MapContext{
	//public HistoryModes historyModes;
	//public PowerInstrument powerInstrument;
	//java.text.SimpleDateFormat dateFormat2;
	//public Long1d executionDates;
	//public Long1d zRecordDates;
	//public Float1d historyPower;
	//public Float1d mocPowerHistory;
	//public Float1d historyPowerZ;
	//public Fecs fecs;
	//public SuperPor sp;
	//public Orcd orcd;
	//public MocPower mocPower;
	//public ModelState ms;
	//public SsmmSimulator ssmm;
	//public Ptr ptr;
	//public Por dl_por;
	private static SimulationContext context;
	//public SsmmHistory ssmmHistory;
	public SimulationContext() {
		super();
		init();
	}
	
	private void init(){
		set("historyModes",new HistoryModes());
		//historyModes=new HistoryModes(); //Composite dataset
		set("powerInstrument",new PowerInstrument());
		//powerInstrument=new PowerInstrument(); //Composite Dataset
		//dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		set("executionDates",new ArrayDataset(new Long1d()));
		//executionDates = new Long1d(); 
		set("zRecordDates",new ArrayDataset(new Long1d()));
		//zRecordDates=new Long1d();
		set("historyPower",new ArrayDataset(new Float1d()));
		//historyPower =new Float1d(); 
		set("mocPowerHistory",new ArrayDataset(new Float1d()));
		//mocPowerHistory= new Float1d(); 
		set("historyPowerZ",new ArrayDataset(new Float1d()));
		//historyPowerZ=new Float1d();
		setProduct("por",new SuperPor());
		//sp=new SuperPor(); //MapContext
		setProduct("dl_por",new Por());
		
		//dl_por=new Por(); //MapContext
		//ssmmHistory=new SsmmHistory();
		//ssmm=new SsmmSimulator();
		//ssmm=new RosettaSsmmSimulator();
		//orcd TableDataset
		try{
			set("orcd",Orcd.readORCDfile(Properties.getProperty(Properties.ORCD_FILE)));
			//orcd=Orcd.readORCDfile(Properties.getProperty(Properties.ORCD_FILE));
		}catch(Exception e){
			set("orcd",Orcd.readORCDfromJar());

			//orcd=Orcd.readORCDfromJar();
		}
		//MocPower TableDataset
		try{
			set("mocPower",MocPower.ReadFromFile(Properties.getProperty(Properties.PWPL_FILE)));
			//mocPower=MocPower.ReadFromFile(Properties.getProperty(Properties.PWPL_FILE));
		}catch (Exception e){
			set("mocPower",MocPower.ReadFromJar());

			//mocPower=MocPower.ReadFromJar();
		}
		set("modelState",ModelState.getModelState());
		//ms=ModelState.getModelState(); //CompositeDataset
		set("fecs",new Fecs());
		//fecs=new Fecs(); //TableDataset
		setProduct("ptr",new Ptr());
		set("log",new ArrayDataset(new String1d()));
		//ptr=new Ptr(); //MapContext
		

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
			//return null;
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public Por getDlPor(){
		try {
			return (Por) getProduct("dl_por");
		} catch (IOException e) {
			//return null;
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
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
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPtr(Ptr ptr){
		setProduct("ptr",ptr);
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
			result=log.get(i)+"\n";
		}
		return result;
	}
	
	/*public void setPor(SuperPor por){
		setProduct("por",por);
	}*/
	
	public void reset(){
		init();
		getModelState().reset();
	}
	
	/*public static SimulationContext getInstance(){
		if (context==null){
			context=new SimulationContext();
		}
		return context;
	}*/
	
	public SimulationContext copy(){
		return (SimulationContext) super.copy();
	}
}
