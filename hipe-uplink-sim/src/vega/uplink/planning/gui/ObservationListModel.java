package vega.uplink.planning.gui;

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultListModel;

import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.Schedule;

public class ObservationListModel extends DefaultListModel<Observation> implements ObservationListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Schedule schedule;
	
	public ObservationListModel(Schedule schedule){
		super();
		this.schedule=schedule;
		schedule.addObservationListener(this);
		scheduleChanged();
	}
	@Override
	public void observationChanged(ObservationChangeEvent event) {
		scheduleChanged();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void scheduleChanged() {
		Enumeration<Observation> en = this.elements();
		while (en.hasMoreElements()){
			en.nextElement().removeObservationListener(this);
		}
		this.removeAllElements();
		Observation[] obs = schedule.getObservations();
		// TODO Auto-generated method stub
        for (int i=0;i<obs.length;i++){
        	this.addElement(obs[i]);
        	obs[i].addObservationListener(this);
        }
	}
	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		scheduleChanged();
	}
	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	
	public void addObservation(Observation obs){
		schedule.addObservation(obs);
	}

}
