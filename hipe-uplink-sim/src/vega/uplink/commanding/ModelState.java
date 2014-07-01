package vega.uplink.commanding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;

import herschel.ia.numeric.String1d;

public class ModelState {
	static String ALICE ="ALICE";
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
	static String LANDER="LANDER";
	java.util.HashMap<String,String> states;
	java.util.HashMap<String,Float> power;
	static ModelState model;
	
	public ModelState(){
		states=new java.util.HashMap<String,String>();
		power=new java.util.HashMap<String,Float>();
	}
	
	/*public ModelState clone(){
		ModelState result=new ModelState();
		
		return result;
	}*/
	
	
	private void setStates(java.util.HashMap<String,String> newStates){
		states=newStates;
	}
	
	private void setPower(java.util.HashMap<String,Float> newPower){
		power=newPower;
	}
	
	public void setInstrument(String instrument,float newPower){
		//System.out.println("Setting subsystem power:"+subsystem);
		//System.out.println(newPower);
		
		power.put(instrument, new Float(newPower));
		//System.out.println(power.get(subsystem));
	}
	
	/*public void setStatePower(String stateName,float newPower){
		String[] arr=stateName.split("_");
		String subSystem="";
		for (int i=0;i<arr.length-1;i++){
			subSystem=subSystem+arr[i]+"_";
		}
		setSubsystemPower(subSystem, newPower);
	}*/
	
	public float getInstrumentPower(String instrument){
		if (power.containsKey(instrument)) return power.get(instrument).floatValue();
		else return 0;
		
	}
	
	public float getTotalPower(){
		float result=0;
		java.util.Iterator<String> it=power.keySet().iterator();
		while (it.hasNext()){
			String key=it.next();
			result=result+this.getInstrumentPower(key);
			
		}
		
		return result;
	}
	
	
	public String getState(String subSystem){
		if (states.containsKey(subSystem)) return states.get(subSystem);
		else return subSystem+"Off";
	}
	
	public void setState(String subSystem,String state){
		states.put(subSystem, state);
	}
	
	public void setState(String mode){
		String[] arr=mode.split("_");
		String subSystem="";
		for (int i=0;i<arr.length-1;i++){
			subSystem=subSystem+arr[i]+"_";
		}
		//String subSystem=arr[0]+arr[1];
		setState(subSystem,mode);
		
	}
	
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
	
	public void reset(){
		model=new ModelState();
	}
	
	public String[] getStateNames(){
		java.util.Vector<String> vector=new java.util.Vector<String>();
		java.util.Iterator<String> it=states.keySet().iterator();
		while (it.hasNext()){
			String key=it.next();
			
				vector.add(key);
			
		}
		String[] result=new String[vector.capacity()];
		vector.copyInto(result);
		return result;
	}
	
	public String[] getAllStates(){
		String[] names=getStateNames();
		String result[]=new String[names.length];
		for (int i=0;i<names.length;i++){
			result[i]=getState(names[i]);
			//System.out.println("***** "+result[i]);
		}
		return result;
		
		
	}
	
	public String toString(){
		String[] names=getStateNames();
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
	
	public void saveState(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			String[] statesN=getAllStates();
			for (int i=0;i<statesN.length;i++){
				writer.print(statesN[i]+"\n");
			}
			//writer.print(PORtoITL(POR));
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	
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
		ModelState result =new ModelState();
		result.setStates(new java.util.HashMap<String,String>(states));
		result.setPower(new java.util.HashMap<String,Float>(power));
		return result;
	}
	

	
	
}