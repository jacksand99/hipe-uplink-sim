package vega.uplink.planning.period;

import java.util.Date;
import java.util.Iterator;
//import java.util.HashMap;
import java.util.TreeMap;

public class Ltp extends Period{
	public static String TAG="LTP";
	//private TreeMap<Integer,Mtp> mtps;
	public Ltp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
		//mtps=new TreeMap<Integer,Mtp>();
		// TODO Auto-generated constructor stub
	}
	public Ltp( int number){
		this(number,new Date(2526802497000L), new Date(65277473000L));
	}

	public String getTag(){
		return Ltp.TAG;
	}
	public void addMtp(Mtp mtp){
		//mtps.put(mtp.getNumber(), mtp);
		this.setProduct("MTP-"+mtp.getNumber(), mtp);

		if (this.getStartDate().after(mtp.getStartDate())) this.setStartDate(mtp.getStartDate());
		if (this.getEndDate().before(mtp.getEndDate())) this.setEndDate(mtp.getEndDate());
	}
	/*public Mtp getMtp(int mtpNumber){
		return mtps.get(mtpNumber);
	}*/
	public Mtp getMtp(int mtpNumber){
		Mtp result=null;
		//return vstps.get(vstpNumber);
		try {
			result= (Mtp) this.getProduct("MTP-"+mtpNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);*/
			/*Mtp result = new Mtp(mtpNumber);
			this.addMtp(result);
			return result;*/
		} 
		if (result==null){
			result = new Mtp(mtpNumber);
			this.addMtp(result);
			return result;
		}
		return result;
	}

	
	/*public Mtp[] getMtps(){
		Mtp[] result=new Mtp[mtps.size()];
		mtps.values().toArray(result);
		return result;
	}*/
	public Mtp[] getMtps(){
		
		Mtp[] result=new Mtp[this.getRefs().size()];
		Iterator<String> it = this.getRefs().keySet().iterator();
		int counter=0;
		while(it.hasNext()){
			try {
				result[counter]=(Mtp)this.getProduct(it.next());
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
	public Stp[] getStps(){
		java.util.Vector<Stp> result=new java.util.Vector<Stp>();
		Mtp[] mtps = this.getMtps();
		for (int i=0;i<mtps.length;i++){
			Stp[] stps = mtps[i].getStps();
			for (int j=0;j<stps.length;j++){
				result.add(stps[j]);
			}
		}
		Stp[] r = new Stp[result.size()];
		r=result.toArray(r);
		return r;
	}
	
	public Vstp[] getVstps(){
		java.util.Vector<Vstp> result=new java.util.Vector<Vstp>();
		Stp[] stps = this.getStps();
		for (int i=0;i<stps.length;i++){
			Vstp[] vstps = stps[i].getVstps();
			for (int j=0;j<vstps.length;j++){
				result.add(vstps[j]);
			}
		}
		Vstp[] r = new Vstp[result.size()];
		r=result.toArray(r);
		return r;
	}
	public Period[] getSubPeriods(){
		return this.getMtps();
	}
	public String toXml(int indent){
		return toXml(TAG,indent);
	}


}
