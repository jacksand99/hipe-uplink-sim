package vega.uplink.commanding;

import vega.uplink.Properties;
import vega.uplink.pointing.Ptr;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.Long1d;
//import herschel.share.util.Configuration;

public class SimulationContext {
	public HistoryModes historyModes;
	public PowerInstrument powerInstrument;
	//java.text.SimpleDateFormat dateFormat2;
	public Long1d executionDates;
	public Long1d zRecordDates;
	public Float1d historyPower;
	public Float1d mocPowerHistory;
	public Float1d historyPowerZ;
	public Fecs fecs;
	public SuperPor sp;
	public Orcd orcd;
	public MocPower mocPower;
	public ModelState ms;
	public SsmmSimulator ssmm;
	public Ptr ptr;
	public Por dl_por;
	private static SimulationContext context;
	//public SsmmHistory ssmmHistory;
	private SimulationContext(){
		init();
	}
	
	private void init(){
		historyModes=new HistoryModes();
		powerInstrument=new PowerInstrument();
		//dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		executionDates = new Long1d(); 
		zRecordDates=new Long1d();
		historyPower =new Float1d(); 
		mocPowerHistory= new Float1d(); 
		historyPowerZ=new Float1d();
		sp=new SuperPor();
		dl_por=new Por();
		//ssmmHistory=new SsmmHistory();
		//ssmm=new SsmmSimulator();
		ssmm=new RosettaSsmmSimulator();
		try{
			orcd=Orcd.readORCDfile(Properties.getProperty(Properties.ORCD_FILE));
		}catch(Exception e){
			orcd=Orcd.readORCDfromJar();
		}
		try{
			mocPower=MocPower.ReadFromFile(Properties.getProperty(Properties.PWPL_FILE));
		}catch (Exception e){
			mocPower=MocPower.ReadFromJar();
		}
		ms=ModelState.getModelState();
		fecs=new Fecs();
		ptr=new Ptr();

	}
	
	public void reset(){
		init();
		ms.reset();
	}
	
	public static SimulationContext getInstance(){
		if (context==null){
			context=new SimulationContext();
		}
		return context;
	}
}
