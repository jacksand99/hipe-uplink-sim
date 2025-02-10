package vega.uplink.commanding.itl;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

import vega.uplink.DateUtil;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceInterface;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.planning.Observation;
//import vega.uplink.planning.ObservationEvent;
import vega.uplink.planning.ObservationEvent;
import vega.uplink.planning.ObservationPor;
import vega.uplink.planning.ObservationSequence;

public class Itl extends Observation{
	private int version;
	
	public Itl(Date startDate,Date endDate) {
		super(startDate,endDate);
		version=1;

		
	}
	
	public int getVersion(){
		return version;
	}
	
	public java.util.Date getItlStartTime(){
		return getObsStartDate();
	}
	
	public java.util.Date getItlEndTime(){
		return getObsEndDate();
	}
	
	public void setItlVersion(int newVersion){
		version=newVersion;
	}
	
	public void setItlStartTime(java.util.Date time){
		setObsStartDate(time);
	}
	
	public void setItlEndTime(java.util.Date time){
		setObsEndDate(time);
	}
	
	public void addItlEvent(ItlEvent itlEvent,Date date){
		addEvent(itlEvent,date);
	}
	
	public Date getDateItlEvent(ItlEvent itlEvent){
		return getDateForEvent(itlEvent);
	}
	
	public String toEvf(){
		String result="";
		ObservationEvent[] defined = this.getDefinedEvents();
		for (int i=0;i<defined.length;i++){
			String dateString = DateUtil.dateToLiteral(this.getDateForEvent(defined[i]));
			String event=new ItlEvent(defined[i].getName()).toString();
			result=result+dateString+" "+event+"\n";
			
		}
		return result;
	}
	
	public String toItl(){
		String result="";
		SequenceInterface[] seqs = this.getSequences();
		for (int i=0;i<seqs.length;i++){
			result=result+((ItlSequence) seqs[i]).toString();
		}
		return result;
	}
	
	public void  parseEvf(String evtFile) throws ParseException{
		
		java.util.HashMap<String,String> events=new java.util.HashMap<String,String>();
		String[] evtlines;
		try {
			evtlines = ItlParser.readFile(evtFile);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			e2.initCause(e1);
			throw e2;
		}
		for (int i=0;i<evtlines.length;i++){
			String[] parts=evtlines[i].split(" ");
			if (parts.length==3){
				parts[0]=parts[0].replaceAll(" ", "");
				parts[1]=parts[1].replaceAll(" ", "");
				parts[2]=parts[2].replaceAll(" ", "");
				int count=Integer.parseInt(parts[2].split("=")[1].replace(")", ""));
				parts[2]="(COUNT="+new Integer(count).toString()+")";
				events.put(parts[1]+" "+parts[2], parts[0]);
				ItlEvent itlEvent = new ItlEvent(parts[1],new Integer(count));
				Date date=DateUtil.parse(parts[0]);
				this.addItlEvent(itlEvent, date);
			}
		}
	}
	
	public void parseItl(String itlFile) throws ParseException{
		//String dEvtPath = defaultEvtPath;
		//Por result = new Por();
		java.io.File file=new java.io.File(itlFile);
		java.util.HashMap<String,String> events=new java.util.HashMap<String,String>();
		java.util.Vector<String> commandLines=new java.util.Vector<String>();
		//String path = file.getParent();
		String[] itllines;
		try {
			itllines = ItlParser.readFile(itlFile);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			throw e2;
		}
		for (int i=0;i<itllines.length;i++){
			if(itllines[i].startsWith("Comment") || itllines[i].startsWith("comment")){
				itllines[i]="";
			}
			if(itllines[i].contains("COUNT") || itllines[i].contains("count")){
				String[] parts=itllines[i].split(" ");
				String evtString=parts[0]+" "+parts[1];
				String[] oParts=parts[1].split("=");
				if (oParts.length<2){
					System.out.println("DEBUG:"+itllines[i]);
					
					System.out.println("DEBUG:"+parts[1]);
				}
				int count=Integer.parseInt(oParts[1].replace(")", ""));
				String key=parts[0]+" "+"(COUNT="+new Integer(count).toString()+")";
				ItlEvent event=new ItlEvent(parts[0],new Integer(count));
				System.out.println(itllines[i]);
				itllines[i]=itllines[i].replace(key, event.getName());
				System.out.println(itllines[i]);
			}
			java.util.Date[] vDates=new java.util.Date[2];
			vDates[0]=new java.util.Date();
			vDates[1]=new java.util.Date();
			
			if (itllines[i].startsWith("Version") || itllines[i].startsWith("Start") || itllines[i].startsWith("End") || itllines[i].startsWith("Init") || itllines[i].startsWith("Source_file") || itllines[i].startsWith("Ref_date") || itllines[i].contains("ANTENNA")){
				if (itllines[i].startsWith("Start")){
					String sSD=itllines[i].replace("Start_time:", "");

					vDates[0]=DateUtil.literalToDate(sSD);
				}
				if (itllines[i].startsWith("End")){
					String sSD=itllines[i].replace("End_time:", "");

					vDates[1]=DateUtil.literalToDate(sSD);
				}
				
			}else{
				if (!itllines[i].equals("")) commandLines.add(itllines[i]);
			}
		}
		try{
			for (int i=0;i<commandLines.size();i++){
				try{
					String cl=commandLines.get(i);
					String[] parts=cl.split(Pattern.quote("("));
					String sComm=parts[0];
					String[] pComm=sComm.split(" ");
					String commandName=pComm[pComm.length-1];
					/*if (commandName==null | commandName.equals("")){
						System.out.println(cl);
					}*/
					/*if (i==0){
						System.out.println(cl);
					}*/
					String commandTime=pComm[0];
					System.out.println(commandTime);
					ItlEvent event = new ItlEvent(commandTime);
					//java.util.Date exTime=parseExDate(commandTime);
					String commandDelta="";
					System.out.println(pComm.length);
					if (pComm.length>4){
						commandDelta=pComm[1];
					}
					System.out.println(commandDelta);
					String sParam="";
					if (parts.length>1){
						sParam=parts[1].replace(")", "");
					}
					String[] sParamArr=ItlParser.separateParameters(sParam);
					DecimalFormat df=new DecimalFormat("000000000");
					//String uid="P"+new Integer((uIDSed*10000)+(i*100)).toString();
					//String uid="P"+df.format((uIDSed*10000)+(i*100));
					/*if (uid.equals("P000010000")){
						System.out.println(sComm);
						System.out.println("Command name:"+commandName);
	
					}*/
					if (commandName.equals("")){
						System.out.println("DEBUG :"+cl);
					}
					System.out.println(event.getName());
					ObservationEvent[] allEvents = this.getDefinedEvents();
					for (int e=0;e<allEvents.length;e++){
						System.out.println(allEvents[e].getName());
					}
					System.out.println(this.getDateForEvent(event));
					ItlSequence seq=new ItlSequence(this,event,ItlParser.deltaToMilli(commandDelta),commandName);
					int repeat=1;
					long separation=0;
	
					for (int j=0;j<sParamArr.length;j++){
						if (!sParamArr[j].startsWith("DATA_RATE") && !sParamArr[j].startsWith("POWER") && !sParamArr[j].startsWith("REPEAT") && !sParamArr[j].startsWith("SEPARATION")){
							try{
								seq.addParameter(ItlParser.parseParameter(sParamArr[j]));
							}catch (ParseException e){
								ParseException nex = new ParseException("At line:"+cl+"."+e.getMessage(),0);
								nex.initCause(e);
								throw nex;
							}
						}
						if (sParamArr[j].startsWith("DATA_RATE") || sParamArr[j].startsWith("POWER")){
							SequenceProfile[] profs=ItlParser.parseProfile(sParamArr[j]);
							for (int k=0;k<profs.length;k++){
								seq.addProfile(profs[k]);
							}
						}
						if (sParamArr[j].startsWith("REPEAT")){
							String[] temp=sParamArr[j].split("=");
							repeat=Integer.parseInt(temp[1].replace(" ", ""));
							
	
						}
						if (sParamArr[j].startsWith("SEPARATION")){
							String[] temp=sParamArr[j].split("=");
							temp[1]=temp[1].replace(" ", "");
							String temp2[]=temp[1].split(":");
							separation=((Integer.parseInt(temp2[0])*3600)+(Integer.parseInt(temp2[1])*60)+Integer.parseInt(temp2[2]))*1000;
						}
					}
					for (int k=0;k<repeat;k++){
						//Sequence newSeq=new Sequence(seq);//=seq.copy();
						ItlSequence newSeq=new ItlSequence(this,event,ItlParser.deltaToMilli(commandDelta)+(separation*k),commandName);

						//Sequence newSeq=new Sequence(new String(seq.getName()),new String(seq.getUniqueID()),new String(seq.getFlag()),new Character(seq.getSource()),new Character(seq.getDestination()),new String(seq.getExecutionTime()),seq.getParameters(),seq.getProfiles());
						//newSeq.setExecutionDate(new java.util.Date(seq.getExecutionDate().getTime()+(separation*k)));
						//newSeq.setUniqueID("P"+new Integer((uIDSed*10000)+(i*100)+k).toString());
						//newSeq.setUniqueID("P"+df.format((uIDSed*10000)+(i*100)+k));
						this.addObservationSequence(newSeq);
	
					}
				}catch (ParseException e){
					System.out.println("Could not parse line");
					e.printStackTrace();
				}

				
					
					
					
	
			}
		}catch (Exception e){
			ParseException e2=new ParseException(e.getMessage(),0);
			e2.initCause(e);
			//e.printStackTrace();
			throw e2;
		}
		

		//return result;
	}

}
