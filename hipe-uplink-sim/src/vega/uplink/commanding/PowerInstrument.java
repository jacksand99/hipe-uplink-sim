package vega.uplink.commanding;

import herschel.ia.dataset.CompositeDataset;
//import herschel.ia.dataset.Product;

import herschel.ia.dataset.DoubleParameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class PowerInstrument extends CompositeDataset{
	//java.util.HashMap<String, Float> power;
	
	public PowerInstrument(){
		super();
		//power=new java.util.HashMap<String, Float>();
		
	}
	
	public void setPower(String instrument,float powerI){
		getMeta().set(instrument, new DoubleParameter(powerI));
		//power.put(instrument, powerI);
	}
	
	public float getPower(String instrument){
		try{
			return ((Double) getMeta().get(instrument).getValue()).floatValue();
		}catch (Exception e){
			return 0;
		}
		//return power.get(instrument).floatValue();
	}
	
	public float getTotalPower(){
		Set<String> keyset = getMeta().keySet();
		float result=0;
		Iterator<String> it = keyset.iterator();
		//Collection<Float> values=power.values();
		//Iterator<Float> it=values.iterator();
		while (it.hasNext()){
			float value = getPower(it.next());
			//result=result+it.next().floatValue();
			result=result+value;
		}
		return result;
		
		
	}
}
