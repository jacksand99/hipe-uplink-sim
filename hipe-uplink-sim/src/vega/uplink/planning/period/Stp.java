package vega.uplink.planning.period;

import java.util.Date;
import java.util.Iterator;

public class Stp extends Period {
	public static String TAG="STP";
	public Stp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
	}
	public Stp(int number){
		this(number,new Date(2526802497000L), new Date(65277473000L));
	}
	public String getTag(){
		return Stp.TAG;
	}
	
	public void addVstp(Vstp vstp){
		this.setProduct("VSTP-"+vstp.getNumber(), vstp);
		if (this.getStartDate().after(vstp.getStartDate())) this.setStartDate(vstp.getStartDate());
		if (this.getEndDate().before(vstp.getEndDate())) this.setEndDate(vstp.getEndDate());
	}
	public Vstp getVstp(int vstpNumber){
		try {
			return (Vstp) this.getProduct("VSTP-"+vstpNumber);
		} catch (Exception e) {
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
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				e.printStackTrace();
				throw(iae);
			} 
			counter++;
		}
		return result;
	}
	public Period[] getSubPeriods(){
		return this.getVstps();
	}
	public String toXml(int indent){
		return toXml(TAG,indent);
	}
	

}
