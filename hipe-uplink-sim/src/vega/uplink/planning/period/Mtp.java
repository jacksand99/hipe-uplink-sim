package vega.uplink.planning.period;

import java.util.Date;
import java.util.Iterator;
//import java.util.HashMap;
import java.util.TreeMap;

public class Mtp extends Period{
	//private TreeMap<Integer,Stp> stps;
	public Mtp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
		//stps=new TreeMap<Integer,Stp>();
		// TODO Auto-generated constructor stub
	}
	public Mtp( int number){
		this(number,new Date(2526802497000L), new Date(65277473000L));
	}

	
	public void addStp(Stp stp){
		//stps.put(stp.getNumber(), stp);
		this.setProduct("STP-"+stp.getNumber(), stp);

		if (this.getStartDate().after(stp.getStartDate())) this.setStartDate(stp.getStartDate());
		if (this.getEndDate().before(stp.getEndDate())) this.setEndDate(stp.getEndDate());
	}
	/*public Stp getStp(int stpNumber){
		return stps.get(stpNumber);
	}*/
	public Stp getStp(int stpNumber){
		//return vstps.get(vstpNumber);
		Stp result=null;
		try {
			result= (Stp) this.getProduct("STP-"+stpNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);*/
			/*Stp result = new Stp(stpNumber);
			this.addStp(result);
			return result;*/
		} 
		if (result==null){
			result = new Stp(stpNumber);
			this.addStp(result);
			return result;
		}
		return result;
	}

	
	/*public Stp[] getStps(){
		Stp[] result=new Stp[stps.size()];
		stps.values().toArray(result);
		return result;
	}*/
	public Stp[] getStps(){
		
		Stp[] result=new Stp[this.getRefs().size()];
		Iterator<String> it = this.getRefs().keySet().iterator();
		int counter=0;
		while(it.hasNext()){
			try {
				result[counter]=(Stp)this.getProduct(it.next());
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
	
	public Vstp[] getVstps(){
		java.util.Vector<Vstp> result=new java.util.Vector<Vstp>();
		Stp[] stps = this.getStps();
		for (int i=0;i<stps.length;i++){
			Vstp[] vstps = stps[i].getVstps();
			for (int j=0;j<stps.length;j++){
				result.add(vstps[j]);
			}
		}
		Vstp[] r = new Vstp[result.size()];
		r=result.toArray(r);
		return r;
	}
	public Period[] getSubPeriods(){
		return this.getStps();
	}
	public String toXml(int indent){
		return toXml("MTP",indent);
	}

}
