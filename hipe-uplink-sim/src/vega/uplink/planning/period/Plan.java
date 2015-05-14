package vega.uplink.planning.period;

import herschel.share.fltdyn.time.FineTime;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;

public class Plan extends Period{
	//private static final Logger LOG = Logger.getLogger(Plan.class.getName());

	public static String TAG="PLAN";
	public Plan(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
	}
	
	public Plan(){
		this(0,new Date(2526802497000L), new Date(65277473000L));
	}
	public Period[] getSubPeriods(){
		return this.getLtps();
	}
	public String toXml(int indent){
		return toXml(TAG,indent);
	}
	
	public void addLtp(Ltp ltp){
		this.setProduct("LTP-"+ltp.getNumber(), ltp);

		if (this.getStartDate().after(ltp.getStartDate())) this.setStartDate(ltp.getStartDate());
		if (this.getEndDate().before(ltp.getEndDate())) this.setEndDate(ltp.getEndDate());
	}
	public Mtp getMtp(int mtpNumber){
		Mtp[] mtps = this.getMtps();
		for (int i=0;i<mtps.length;i++){
			if (mtps[i].getNumber()==mtpNumber) return mtps[i];
		}
		return null;
	}
	public Ltp getLtp(int ltpNumber){
		Ltp result=null;
		try {
			result= (Ltp)this.getProduct("LTP-"+ltpNumber);
		} catch (Exception e) {
			
			
		} 
		if (result==null){
			result = new Ltp(ltpNumber);
			this.addLtp(result);
		}
		return result;
	}

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
	public String getTag(){
		return Plan.TAG;
	}
	
	public static Plan readFromFile(String file){
		Plan result= new Plan();
	    BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			e.printStackTrace();
			throw(iae);
		} 
	    try {
	        String line;
			try {
				line = br.readLine();
			} catch (Exception e) {
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				e.printStackTrace();
				throw(iae);
			} 

	        while (line != null ) {
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
	        		int nv=vstpEnd-vstpStart+1;
	        		java.util.Date[] sDates=new java.util.Date[nv];
	        		for (int j=0;j<nv;j++){
	        			try {
							sDates[j]=DateUtil.zuluToDate(field[6+j]);
							VegaLog.info(stp+" "+mtp+" "+vstpStart+" "+vstpEnd+" "+ltp+" "+sDates[j]);
							Vstp vstpP =new Vstp(vstpStart+j,sDates[j]);
							Ltp ltpP=result.getLtp(ltp);
							Mtp mtpP = ltpP.getMtp(mtp);
							Stp stpP=mtpP.getStp(stp);
							stpP.addVstp(vstpP);
							
						} catch (Exception e) {
							VegaLog.info("Error:"+line);
							VegaLog.info(stp+" "+mtp+" "+vstpStart+" "+vstpEnd+" "+ltp+" ");
							IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
							iae.initCause(e);
							e.printStackTrace();
							throw(iae);
						}
	        		}
	        	}
	            try {
					line = br.readLine();
				} catch (Exception e) {
					IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
					iae.initCause(e);
					e.printStackTrace();
					throw(iae);
				} 
	        }
	    } finally {
	        try {
				br.close();
			} catch (Exception e) {
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
				sp[i-1].setEndPeriod(sp[i].getStartDate());
			}
		}
		this.setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
		this.getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().getLastSubPeriod().setEndDate(new FineTime(new Date(2526802497000L)));
	}
	private static void saveStringToFile(String file,String str) throws IOException {
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(str);
			writer.close();
		}catch (Exception e){
			IOException io = new IOException(e.getMessage());
			io.initCause(e);
			throw(io);
		}
	}
	public static void saveToXmlFile(String file,Plan plan) throws IOException{
		saveStringToFile(file,plan.toXml());
	}
	
	public static Plan readPlanFromXmlFile(String file) throws IOException{
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			 
			NodeList nListHeader = doc.getElementsByTagName("PLAN");
			Period result = Period.readFromNode(nListHeader.item(0));
			result.fixEndDate();
			return (Plan) result;

		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
	}


}
