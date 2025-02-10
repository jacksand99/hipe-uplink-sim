package vega.uplink.commanding;

import herschel.ia.dataset.CompositeDataset;
//import herschel.ia.dataset.Product;

import herschel.ia.dataset.DoubleParameter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Class to store the power consumed by instruments at any give time
 * @author jarenas
 *
 */
public class PowerInstrument extends CompositeDataset{
	
	public PowerInstrument(){
		super();
		
	}
	
	/**
	 * Set the power consumption of a instrument
	 * @param instrument
	 * @param powerI
	 */
	public void setPower(String instrument,float powerI){
		getMeta().set(instrument, new DoubleParameter(powerI));
	}
	
	/**
	 * Get the power consumption of an instrument
	 * @param instrument
	 * @return
	 */
	public float getPower(String instrument){
		try{
			return ((Double) getMeta().get(instrument).getValue()).floatValue();
		}catch (Exception e){
			return 0;
		}
	}
	
	/**
	 * Summatory of the power consumption of all the instruments
	 * @return
	 */
	public float getTotalPower(){
		Set<String> keyset = getMeta().keySet();
		float result=0;
		Iterator<String> it = keyset.iterator();
		while (it.hasNext()){
			float value = getPower(it.next());
			result=result+value;
		}
		return result;
		
		
	}
}
