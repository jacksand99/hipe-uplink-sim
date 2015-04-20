package vega.uplink.commanding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.DoubleParameter;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.StringParameter;
import herschel.ia.numeric.String1d;

/**
 * Class to store the status of the spacecraft (states of the instruments and power consumed) at any given time.
 * Within this class the definition of the following items may be confusing:
 * Subsystem: a spacecraft subsystem. It is is not an instrument, as instruments may have different subsystems
 * State: state of an subinstrument
 * Mode: it is composed of the subinstrument and the state. It is build as subinstrument_state
 * example:
 * Mode or state : AL_HV_Off
 * SubSystem: AL_HV_
 * Instrument: AL (Alice)
 * @author jarenas
 *
 */
public class ModelState extends CompositeDataset{
	/*static String ALICE ="ALICE";
	static String CONSERT = "CONSERT";
	static String COSIMA ="COSIMA";
	static String GIADA="GIADA";
	static String MIDAS="MIDAS";
	static String MIRO="MIRO";
	static String OSIRIS="OSIRIS";
	static String ROSINA="ROSINA";
	static String RPC="RPC";
	static String RSI="RSI";
	static String SREM="SREM";
	static String VIRTIS="VIRTIS";
	static String LANDER="LANDER";*/
	static ModelState model;
	
	/**
	 * Creates an empty status
	 */
	public ModelState() {
		super();
	}
	
	
	/**
	 * Set the power consumption of an instrument in this status
	 * @param instrument
	 * @param newPower
	 */
	public void setInstrument(String instrument,float newPower){
		getMeta().set("power_"+instrument, new DoubleParameter(newPower));
	}
	
	
	/**
	 * Get the power consumption of an instrument
	 * @param instrument
	 * @return
	 */
	public float getInstrumentPower(String instrument){
		if (getMeta().containsKey("power_"+instrument)){
			return ((Double)getMeta().get("power_"+instrument).getValue()).floatValue();
		}else{
			return 0;
		}
		
	}
	
	/**
	 * Get the total power consumption from all the instrument
	 * @return
	 */
	public float getTotalPower(){
		float result=0;
		Iterator<String> it = getMeta().keySet().iterator();
		while (it.hasNext()){
			String key=it.next();
			if (key.startsWith("state_")){
				result=result+((Double)getMeta().get(key).getValue()).floatValue();
			}
		}
		
		return result;
	}
	
	
	/**
	 * Get the state of an subsystem
	 * @param subSystem
	 * @return
	 */
	public String getState(String subSystem){
		if (getMeta().containsKey("state_"+subSystem)){
			return (String) getMeta().get("state_"+subSystem).getValue();
		}else return subSystem+"Off";
	}
	
	/**
	 * Set the state for a subsystem
	 * @param subSystem
	 * @param state
	 */
	public void setState(String subSystem,String state){
		getMeta().set("state_"+subSystem, new StringParameter(state));
	}
	
	/**
	 * Set the state. The mode is build as subsystem_state
	 * @param mode
	 */
	public void setState(String mode){
		String[] arr=mode.split("_");
		String subSystem="";
		for (int i=0;i<arr.length-1;i++){
			subSystem=subSystem+arr[i]+"_";
		}
		setState(subSystem,mode);
		
	}
	
	/**
	 * For a give mode (subsystem_state) get the current state
	 * @param mode
	 * @return
	 */
	public String getStateForMode(String mode){
		String[] arr=mode.split("_");
		String subSystem="";
		for (int i=0;i<arr.length-1;i++){
			subSystem=subSystem+arr[i]+"_";
		}
		return getState(subSystem);
		
	}
	
	public static ModelState getModelState(){
		if (model==null){
			model=new ModelState();
		}
		return model;
	}
	
	/**
	 * Reset this model state
	 */
	public void reset(){
		model=new ModelState();
	}
	
	/**
	 * Get all subsystem names
	 * @return
	 */
	public String[] getSubsystemNames(){
		Iterator<String> it = getMeta().keySet().iterator();
		java.util.Vector<String> vector=new java.util.Vector<String>();
		while (it.hasNext()){
			String key=it.next();
			if (key.startsWith("state_")) vector.add(key.substring(6));
		}
		String[] result=new String[vector.capacity()];
		vector.copyInto(result);
		return result;
	}
	
	/**
	 * Get all states or modes
	 * @return
	 */
	public String[] getAllStates(){
		String[] names=getSubsystemNames();
		String result[]=new String[names.length];
		for (int i=0;i<names.length;i++){
			result[i]=getState(names[i]);
		}
		return result;
		
		
	}
	

	public String toString(){
		String[] names=getSubsystemNames();
		String result="";
		for (int i=0;i<names.length;i++){
			if(names[i]!=null) result=result+"["+names[i]+" = "+getState(names[i])+"]\n";
		}
		return result;
	}
	
	public String1d toString1d(){
		String1d result=new String1d(getAllStates());
		return result;
	}
	
	/**
	 * Save the current status to a file
	 * @param file
	 */
	public void saveStatus(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			String[] statesN=getAllStates();
			for (int i=0;i<statesN.length;i++){
				writer.print(statesN[i]+"\n");
			}
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Save the status as a init file in jython
	 * @param file
	 */
	public void saveStateAsInitScript(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			String[] statesN=getAllStates();
			for (int i=0;i<statesN.length;i++){
				if (!statesN[i].equals("nullOff"))
					writer.print("simulationContext.getModelState().setState(\""+statesN[i]+"\")\n");
			}
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Init the states from a file
	 * @param file
	 */
	public void initFromFile(String file){
		this.reset();
		BufferedReader br=null;
		try {
			 
			br = new BufferedReader(new FileReader(file));
			String line;
			while ((line = br.readLine()) != null) {
				this.setState(line);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		if (br!=null){
			try{
				br.close();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
			
	}
	
	public ModelState clone(){
		MetaData meta = getMeta().copy();
		ModelState result = new ModelState();
		result.setMeta(meta);
		return result;
	}
	

	
	
}
