package vega.uplink.planning.gui;

import java.util.logging.Logger;

import vega.uplink.DateUtil;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import de.jaret.util.date.IntervalImpl;
import de.jaret.util.date.JaretDate;

public class ObservationInterval extends IntervalImpl implements ObservationListener{
	Observation observation;
	private final Logger LOG = Logger.getLogger(ObservationInterval.class.getName());
	
	public ObservationInterval(Observation obs){
		super();
		observation=obs;
		observation.addObservationListener(this);
		this.setBegin(new JaretDate(obs.getObsStartDate()));
		this.setEnd(new JaretDate(obs.getObsEndDate()));
	}

	public String toString(){
		return observation.getName()+" ["+DateUtil.dateToZulu(observation.getObsStartDate())+"-"+DateUtil.dateToZulu(observation.getObsEndDate())+"]";
	}
	
	public void addObservationListener(ObservationListener listener){
		observation.addObservationListener(listener);
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
		LOG.info("Observation changed");
		this.setBegin(new JaretDate(observation.getObsStartDate()));
		this.setEnd(new JaretDate(observation.getObsEndDate()));
		this.firePropertyChange(IntervalImpl.PROP_BEGIN, null, null);
		
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
