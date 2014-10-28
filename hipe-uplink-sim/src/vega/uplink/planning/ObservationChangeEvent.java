package vega.uplink.planning;

import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.Product;

public class ObservationChangeEvent {
	Observation source;
	public ObservationChangeEvent(Observation source){
		this.source=source;
	}
	

	
	public Observation getSource(){
		return source;
	}
}
