package vega.uplink.commanding;

import java.util.Collection;
import java.util.Iterator;

public class PowerInstrument {
	java.util.HashMap<String, Float> power;
	
	public PowerInstrument(){
		power=new java.util.HashMap<String, Float>();
	}
	
	public void setPower(String instrument,float powerI){
		power.put(instrument, powerI);
	}
	
	public float getPower(String instrument){
		return power.get(instrument).floatValue();
	}
	
	public float getTotalPower(){
		float result=0;
		Collection<Float> values=power.values();
		Iterator<Float> it=values.iterator();
		while (it.hasNext()){
			result=result+it.next().floatValue();
		}
		return result;
		
		
	}
}
