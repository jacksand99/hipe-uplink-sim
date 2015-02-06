package vega.uplink.planning.period;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Iterator;
//import java.util.HashMap;
import java.util.TreeMap;

public class Stp extends Period {
	//private TreeMap<Integer,Vstp> vstps;
	public Stp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
		//vstps=new TreeMap<Integer,Vstp>();
		// TODO Auto-generated constructor stub
	}
	public Stp(int number){
		this(number,new Date(2526802497000L), new Date(65277473000L));
	}

	
	public void addVstp(Vstp vstp){
		//vstps.put(vstp.getNumber(), vstp);
		this.setProduct("VSTP-"+vstp.getNumber(), vstp);
		if (this.getStartDate().after(vstp.getStartDate())) this.setStartDate(vstp.getStartDate());
		if (this.getEndDate().before(vstp.getEndDate())) this.setEndDate(vstp.getEndDate());
	}
	public Vstp getVstp(int vstpNumber){
		//return vstps.get(vstpNumber);
		try {
			return (Vstp) this.getProduct("VSTP-"+vstpNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);
		} 
	}
	
	public Vstp[] getVstps(){
		
		Vstp[] result=new Vstp[this.getRefs().size()];
		Iterator<String> it = this.getRefs().keySet().iterator();
		int counter=0;
		while(it.hasNext()){
			try {
				result[counter]=(Vstp)this.getProduct(it.next());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				e.printStackTrace();
				throw(iae);
			} 
			counter++;
		}
		//vstps.values().toArray(result);
		return result;
	}
	public Period[] getSubPeriods(){
		return this.getVstps();
	}
	public String toXml(int indent){
		return toXml("STP",indent);
	}
	

}
