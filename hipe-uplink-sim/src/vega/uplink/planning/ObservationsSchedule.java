package vega.uplink.planning;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import herschel.ia.pal.ListContext;
import herschel.ia.pal.ProductRef;

/**
 * AN observation Schedule is a timeline of observations
 * @author jarenas
 *
 */
public class ObservationsSchedule extends ListContext {
	private HashMap<Observation,ProductRef> map;
	private java.util.HashSet<ObservationListener> listeners;
	
	public ObservationsSchedule (){
		super();
		listeners=new java.util.HashSet<ObservationListener>();
		map=new HashMap<Observation,ProductRef>();
	}
	

	
	public void addObservation(Observation obs){
		boolean oldListen = Observation.LISTEN;
		Observation.LISTEN=false;
		ProductRef ref = new ProductRef(obs);
		map.put(obs, ref);
		getRefs().add(ref);
		Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			ObservationListener listener = it.next();
			obs.addObservationListener(listener);
			listener.scheduleChanged();
		}
		Observation.LISTEN=oldListen;
		
		
	}
	
	public void addObservationListener(ObservationListener newListener){
		Observation[] obs = this.getObservations();
		for (int i=0;i<obs.length;i++){
			obs[i].addObservationListener(newListener);
			
		}
		listeners.add(newListener);
	}
	
	public void removeObs(Observation obs){
		ProductRef ref = map.get(obs);
		int index=getRefs().indexOf(ref);
		getRefs().remove(index);
		Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			ObservationListener listener = it.next();
			obs.addObservationListener(listener);
			listener.scheduleChanged();
		}
		
	}
	public void removeObs(int index  ){

		getRefs().remove(index);
		Iterator<ObservationListener> it = listeners.iterator();
		while (it.hasNext()){
			ObservationListener listener = it.next();
			listener.scheduleChanged();
		}
		
	}
	
	public Observation[] getObservations(){
		Set<ProductRef> allRefs = this.getAllRefs();
		TreeSet<Observation> ordered=new TreeSet<Observation>();
		//TreeSet<ProductRef> ordered=new TreeSet<ProductRef>(allRefs);
		Observation[] result=new Observation[allRefs.size()];
		Iterator<ProductRef> it = allRefs.iterator();
		//int locator=0;
		while (it.hasNext()){
			try {
				ordered.add((Observation) it.next().getProduct());
				//result[locator]=(Observation) it.next().getProduct();
				//locator++;
				
			} catch (Exception e) {
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				throw (iae);
			} 
			
		}
		result=ordered.toArray(result);
		return result;
	}
	
	public String toXml(int indent){
		String result="";
		String iString="";
		for (int i=0;i<indent;i++){
			iString=iString+"\t";
		}
		result=result+iString+"<observationsSchedule>\n";
		Observation[] observations = this.getObservations();
		for (int i=0;i<observations.length;i++){
			result=result+observations[i].toXml(indent+1)+"\n";
		}

		result=result+iString+"</observationsSchedule>\n";
		
		
		return result;
	}
	
	public String toXml(){
		return toXml(0);
	}
	

	
	
}
