package vega.uplink.planning.gui;

import java.util.Iterator;

import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.ProductListener;
import vega.uplink.DateUtil;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.pointing.PointingBlock;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

public class ObservationInterval extends IntervalImpl implements ObservationListener{
	Observation observation;
	//java.util.Vector<ObservationListener> listeners;
	
	public ObservationInterval(Observation obs){
		super();
		observation=obs;
		observation.addObservationListener(this);
		this.setBegin(new JaretDate(obs.getObsStartDate()));
		this.setEnd(new JaretDate(obs.getObsEndDate()));
	}

	@Override
	/*public void targetChanged(DatasetEvent<Product> arg0) {
		// TODO Auto-generated method stub
		this.setBegin(new JaretDate(observation.getObsStartDate()));
		this.setEnd(new JaretDate(observation.getObsEndDate()));
		Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			it.next().observationChanged(null);
		}
		
	}*/
	public String toString(){
		return observation.getName()+" ["+DateUtil.dateToZulu(observation.getObsStartDate())+"-"+DateUtil.dateToZulu(observation.getObsEndDate())+"]";
	}
	
	public void addObservationListener(ObservationListener listener){
		observation.addObservationListener(listener);
		//listeners.add(listener);
	}
	
	public Observation getObservation(){
		return observation;
	}

	@Override
	public void observationChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scheduleChanged() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		System.out.println("Observation cjanged");
		this.setBegin(new JaretDate(observation.getObsStartDate()));
		this.setEnd(new JaretDate(observation.getObsEndDate()));
		this.firePropertyChange(IntervalImpl.PROP_BEGIN, null, null);
		//IntervalImpl.
		//this.
		//Iterator<ObservationListener> it = listeners.iterator();
		
	}

	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

}
