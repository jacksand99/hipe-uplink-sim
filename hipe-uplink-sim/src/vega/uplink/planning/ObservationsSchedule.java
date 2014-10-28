package vega.uplink.planning;

//import java.io.IOException;
//import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/*import java.util.logging.Logger;

import vega.uplink.commanding.AbstractSequence;
import vega.uplink.planning.Schedule.ObservationScheduleListener;
import vega.uplink.pointing.PointingBlock;
import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.ProductListener;*/
//import vega.uplink.pointing.PtrSegment;
import herschel.ia.pal.ListContext;
import herschel.ia.pal.ProductRef;

public class ObservationsSchedule extends ListContext {
	private HashMap<Observation,ProductRef> map;
	private java.util.HashSet<ObservationListener> listeners;
	
	public ObservationsSchedule (){
		super();
		listeners=new java.util.HashSet<ObservationListener>();
		map=new HashMap<Observation,ProductRef>();
		//this.getRefs().add(0, new ProductRef(ptslSegment));
		//this.getRefs().add(new ProductRef(ptslSegment));
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
		
		//obs.addProductListener(new ObservationListener(this));
		
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
			//obs.addObservationListener(listener);
			listener.scheduleChanged();
		}
		
	}
	
	public Observation[] getObservations(){
		Set<ProductRef> allRefs = this.getAllRefs();
		Observation[] result=new Observation[allRefs.size()];
		Iterator<ProductRef> it = allRefs.iterator();
		int locator=0;
		while (it.hasNext()){
			try {
				result[locator]=(Observation) it.next().getProduct();
				locator++;
				
			} catch (Exception e) {
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				throw (iae);
				// TODO Auto-generated catch block
				//e.printStackTrace();
			} 
			
		}
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
		
		
		// TODO Auto-generated method stub
		return result;
	}
	
	public String toXml(){
		return toXml(0);
	}
	
	/*class ObservationListener implements ProductListener{
		private final Logger LOG = Logger.getLogger(ObservationScheduleListener.class.getName());
		private ObservationsSchedule parent;
		public ObservationListener(ObservationsSchedule parent){
			this.parent=parent;
		}
		@Override
		public void targetChanged(DatasetEvent<Product> arg0) {
			// TODO Auto-generated method stub
			
			LOG.info("Observation Changed");
			LOG.info(""+arg0);
			
			parent.refsChanged();
		}
		
	}*/

	
	
}
