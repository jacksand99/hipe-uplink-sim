package vega.uplink.planning.period;

import herschel.share.fltdyn.time.FineTime;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
//import java.util.HashMap;
import java.util.TreeMap;

import vega.uplink.pointing.PointingBlock;

public class Plan extends Period{
	//private TreeMap<Integer,Ltp> ltps;

	public Plan(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
		//ltps=new TreeMap<Integer,Ltp>();
		// TODO Auto-generated constructor stub
	}
	
	public Plan(){
		this(0,new Date(2526802497000L), new Date(65277473000L));
	}
	public Period[] getSubPeriods(){
		return this.getLtps();
	}
	public String toXml(int indent){
		return toXml("PLAN",indent);
	}
	
	public void addLtp(Ltp ltp){
		//ltps.put(ltp.getNumber(), ltp);
		this.setProduct("LTP-"+ltp.getNumber(), ltp);

		if (this.getStartDate().after(ltp.getStartDate())) this.setStartDate(ltp.getStartDate());
		if (this.getEndDate().before(ltp.getEndDate())) this.setEndDate(ltp.getEndDate());
	}
	/*public Ltp getLtp(int ltpNumber){
		return ltps.get(ltpNumber);
	}*/
	public Mtp getMtp(int mtpNumber){
		Mtp[] mtps = this.getMtps();
		for (int i=0;i<mtps.length;i++){
			if (mtps[i].getNumber()==mtpNumber) return mtps[i];
		}
		//Mtp result = new Mtp(mtpNumber);
		return null;
	}
	public Ltp getLtp(int ltpNumber){
		//return vstps.get(vstpNumber);
		Ltp result=null;
		try {
			result= (Ltp)this.getProduct("LTP-"+ltpNumber);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			/*IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);*/
			
			
		} 
		if (result==null){
			result = new Ltp(ltpNumber);
			this.addLtp(result);
		}
		return result;
	}

	
	/*public Ltp[] getLtps(){
		Ltp[] result=new Ltp[ltps.size()];
		ltps.values().toArray(result);
		return result;
	}*/
	public Ltp[] getLtps(){
		
		Ltp[] result=new Ltp[this.getRefs().size()];
		Iterator<String> it = this.getRefs().keySet().iterator();
		int counter=0;
		while(it.hasNext()){
			try {
				result[counter]=(Ltp)this.getProduct(it.next());
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
	public Mtp[] getMtps(){
		java.util.Vector<Mtp> result=new java.util.Vector<Mtp>();
		Ltp[] ltps = this.getLtps();
		for (int i=0;i<ltps.length;i++){
			Mtp[] mtps = ltps[i].getMtps();
			for (int j=0;j<mtps.length;j++){
				result.add(mtps[j]);
			}
		}
		Mtp[] r = new Mtp[result.size()];
		r=result.toArray(r);
		return r;
	}
	public Mtp[] getStps(){
		java.util.Vector<Stp> result=new java.util.Vector<Stp>();
		Ltp[] ltps = this.getLtps();
		for (int i=0;i<ltps.length;i++){
			Stp[] stps = ltps[i].getStps();
			for (int j=0;j<stps.length;j++){
				result.add(stps[j]);
			}
		}
		Mtp[] r = new Mtp[result.size()];
		r=result.toArray(r);
		return r;
	}
	public Vstp[] getVstps(){
		java.util.Vector<Vstp> result=new java.util.Vector<Vstp>();
		Ltp[] ltps = this.getLtps();
		for (int i=0;i<ltps.length;i++){
			Vstp[] vstps = ltps[i].getVstps();
			for (int j=0;j<vstps.length;j++){
				result.add(vstps[j]);
			}
		}
		Vstp[] r = new Vstp[result.size()];
		r=result.toArray(r);
		return r;
	}

	
	public String toString(){
		String result="";
		result=result+
"# STP   MTP   VSTP_Start VSTP_End #  LTP   Start 1st VSTP      Start 2nd VSTP      Start 3th VSTP      Start 4th VSTP      Start 5th VSTP\n"+ 
"#----------------------------------------------------------------------------------------------------------------------------------------\n";
		Ltp[] lt=this.getLtps();
		for (int l=0;l<lt.length;l++){
			Mtp[] mt=lt[l].getMtps();
			for (int m=0;m<mt.length;m++){
				Stp[] st=mt[m].getStps();
				for (int s=0;s<st.length;s++){
					Vstp[] vt=st[s].getVstps();
				}
			}
		}
		return result;
	}
	
	public static Plan readFromFile(String file){
		Plan result= new Plan();
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);
		} 
	    try {
	        //StringBuilder sb = new StringBuilder();
	        String line;
			try {
				line = br.readLine();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				e.printStackTrace();
				throw(iae);
			} 

	        while (line != null ) {
	            /*sb.append(line);
	            sb.append(System.lineSeparator());*/
	        	if (!line.startsWith("#")){
	        		line = line.replaceAll("\\s+", " ");
	        		StringTokenizer tk=new StringTokenizer(line, " ");
	        		String[] field=new String[tk.countTokens()];
	        		int count=0;
	        		while (tk.hasMoreTokens()){
	        			field[count]=tk.nextToken();
	        			count++;
	        		}
	        		int stp=Integer.parseInt(field[0]);
	        		int mtp=Integer.parseInt(field[1]);
	        		int vstpStart=Integer.parseInt(field[2]);
	        		int vstpEnd=Integer.parseInt(field[3]);
	        		int ltp=Integer.parseInt(field[5]);
	        		//System.out.println(stp+" "+mtp+" "+vstpStart+" "+vstpEnd+" "+ltp+" ");
	        		int nv=vstpEnd-vstpStart+1;
	        		java.util.Date[] sDates=new java.util.Date[nv];
	        		for (int j=0;j<nv;j++){
	        			try {
							sDates[j]=PointingBlock.zuluToDate(field[6+j]);
							System.out.println(stp+" "+mtp+" "+vstpStart+" "+vstpEnd+" "+ltp+" "+sDates[j]);
							Vstp vstpP =new Vstp(vstpStart+j,sDates[j]);
							Ltp ltpP=result.getLtp(ltp);
							Mtp mtpP = ltpP.getMtp(mtp);
							Stp stpP=mtpP.getStp(stp);
							stpP.addVstp(vstpP);
							
						} catch (Exception e) {
							System.out.println("Error:"+line);
							System.out.println(stp+" "+mtp+" "+vstpStart+" "+vstpEnd+" "+ltp+" ");
							// TODO Auto-generated catch block
							IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
							iae.initCause(e);
							e.printStackTrace();
							throw(iae);
						}
	        		}
	        		//System.out.println(line);
	        	}
	            try {
					line = br.readLine();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
					iae.initCause(e);
					e.printStackTrace();
					throw(iae);
				} 
	        }
	        //String everything = sb.toString();
	    } finally {
	        try {
				br.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				e.printStackTrace();
				throw(iae);
			} 
	    }
	    result.fixEndDate();
		return result;
	}
	
	protected void fixEndDate(){
		super.fixEndDate();
		Period[] sp = this.getVstps();
		
		for (int i=0;i<sp.length;i++){
			//sp[i].fixEndDate();
			if (i>0){
				sp[i-1].setEndDate(sp[i].getStartDate());
			}
		}
		this.setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		
		//if (sp.length>0) this.setEndDate(sp[sp.length-1].getEndDate());
	}


}
