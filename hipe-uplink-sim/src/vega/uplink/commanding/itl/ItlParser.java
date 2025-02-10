package vega.uplink.commanding.itl;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.ParameterFloat;
import vega.uplink.commanding.ParameterString;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.commanding.SuperPor;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationEvent;
import vega.uplink.planning.ObservationPor;
import vega.uplink.planning.ObservationSequence;
import vega.uplink.track.Fecs;

public class ItlParser {
	public static String path="";
	public static String dEvtPath="";
	//private static final Logger LOG = Logger.getLogger(Fecs.class.getName());
	public static Observation itlToObs(String st,Date startDate,Date endDate) throws ParseException{
		String[] itllines;
		try {
			itllines = readString(st);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			throw e2;
		}
		return itlToObs(itllines,startDate,endDate);	
	}
	public static Observation itlToObs(File file,Date startDate,Date endDate) throws ParseException{
		String[] itllines;
		try {
			itllines = readFile(file);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			throw e2;
		}
		return itlToObs(itllines,startDate,endDate);
	}
	public static Observation itlToObs(String[] itllines,Date startDate,Date endDate) throws ParseException{
		Observation result=new Observation(startDate,endDate);
		java.util.Vector<String> commandLines=new java.util.Vector<String>();
		int uIDSed=1;
		
		for (int i=0;i<itllines.length;i++){
			if(itllines[i].startsWith("Comment") || itllines[i].startsWith("comment")){
				itllines[i]="";
			}

				itllines[i]=itllines[i].replace("#POWER_ON#", "0");
				itllines[i]=itllines[i].replace("#DATA_ON#", "0");
				if (!itllines[i].equals("")) commandLines.add(itllines[i]);
			
		}
		try{
			for (int i=0;i<commandLines.size();i++){
				try{
					String cl=commandLines.get(i);
					String[] parts=cl.split(Pattern.quote("("));
					String sComm=parts[0];
					String[] pComm=sComm.split(" ");
					String commandName=pComm[pComm.length-1];
					String commandTime=pComm[0];
					String executionevent=commandTime;
					String commandDelta="";
					if (pComm.length>4){
						commandDelta=pComm[1];
					}
					String instrument="UNKNOWN";
					instrument=pComm[2].toUpperCase();
					String sParam="";
					if (parts.length>1){
						sParam=parts[1].replace(")", "");
					}
					String[] sParamArr=separateParameters(sParam);
					DecimalFormat df=new DecimalFormat("000000000");
					String uid="P"+df.format((uIDSed*10000)+(i*100));
					if (commandName.equals("")){
						System.out.println("DEBUG :"+cl);
					}
					ObservationSequence seq=new ObservationSequence (result,new ObservationEvent(executionevent),deltaToMilli(commandDelta),commandName,uid);
					int repeat=1;
					long separation=0;
	
					for (int j=0;j<sParamArr.length;j++){
						if (!sParamArr[j].startsWith("DATA_RATE") && !sParamArr[j].startsWith("POWER") && !sParamArr[j].startsWith("REPEAT") && !sParamArr[j].startsWith("SEPARATION")){
							try{
								seq.addParameter(parseParameter(sParamArr[j]));
							}catch (ParseException e){
								ParseException nex = new ParseException("At line:"+cl+"."+e.getMessage(),0);
								nex.initCause(e);
								throw nex;
							}
						}
						if (sParamArr[j].startsWith("DATA_RATE") || sParamArr[j].startsWith("POWER")){
							SequenceProfile[] profs=parseProfile(sParamArr[j]);
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
						ObservationSequence newSeq=new ObservationSequence (result,new ObservationEvent(executionevent),deltaToMilli(commandDelta)+(separation*k),commandName,uid);
						newSeq.setParameters(seq.getParameters());
						newSeq.setProfiles(seq.getProfiles());
						result.setInstrument(instrument);
						newSeq.setUniqueID("P"+df.format((uIDSed*10000)+(i*100)+k));
						result.addObservationSequence(newSeq);
	
					}
				}catch (ParseException e){
					throw(e);
					//System.out.println("Could not parse line");
					//e.printStackTrace();
				}
			}
		}catch (Exception e){
			System.out.println("Could not parse line");
			e.printStackTrace();
		}
		//result.setName(name);
		return result;
	}
	public static Por parseItlExplicitTime(BufferedReader br,int uIDSed) throws ParseException, IOException{
		return parseItl(br,new EventList(),System.getProperty("user.home"),new Date(),new Date());
	}
	public static Por parseItlExplicitTime(String itlFile,int uIDSed) throws ParseException, IOException{
		return parseItl(itlFile,new EventList(),uIDSed);

	}
	
	public static EventList getDefaultEvents(String itlFile) throws IOException, ParseException{
		String evtFile = itlFile.replace(".itl", ".evf");
		evtFile=evtFile.replace("ITLS", "EVF_");
		File f = new File(evtFile);
		EventList events=new EventList();
		if (f.exists()){
			events=EventList.parseEvf(evtFile);
		}
		return events;
		
	}
	public static SuperPor itlToPor(String itlFile) throws IOException, ParseException{
		return parseItl(itlFile,getDefaultEvents(itlFile),1);
	}
	public static SuperPor parseItl(String itlFile,String evtFile,String dEvtPath,int uIDSed) throws ParseException, IOException{
		EventList events=EventList.parseEvf(evtFile);
		return ItlParser.parseItl(itlFile, events, uIDSed);
	}
	public static SuperPor parseItl(String itlFile,String evtFile,int uIDSed) throws ParseException, IOException{
		EventList events=EventList.parseEvf(evtFile);
		return ItlParser.parseItl(itlFile, events, uIDSed);
	}
	public static SuperPor parseItl(String itlFile,EventList events,int uIDSed) throws ParseException, IOException{
		File f = new File(itlFile);
		String basePath = f.getParent();
		return parseItl(itlFile,events,uIDSed,basePath);
	}
	public static SuperPor parseItl(String itlFile,EventList events,int uIDSed,String basePath) throws ParseException, IOException{	
		
		return parseItl(itlFile,events,basePath,new Date(65277473000L),new Date(2526802497000L));
	}
	public static SuperPor parseItl(String itlFile,EventList events,String basePath,Date validityStart,Date validityEnd) throws ParseException, IOException{	
		java.io.File file=new java.io.File(itlFile);
		String[] itllines=readFile(itlFile);
		SuperPor result = parseItl(itllines,events,basePath,validityStart,validityEnd);
		result.setName(file.getName());
		result.setPath(file.getParent());
		return result;
	}
	
	public static SuperPor parseItl(BufferedReader bf,EventList events,String basePath,Date validityStart,Date validityEnd) throws ParseException, IOException{	
		String[] itllines=readFile(bf);
		SuperPor result = parseItl(itllines,events,basePath,validityStart,validityEnd);
		return result;
	}

	public static SuperPor parseItl(String[] itllines,EventList events,String basePath,Date validityStart,Date validityEnd) throws ParseException, IOException{	
		SuperPor result = new SuperPor();
		
		result.setCalculateValidity(false);

		result.setStartDate(new FineTime(validityStart));
		result.setEndDate(new FineTime(validityEnd));
		Date refDate=new Date();
		for (int i=0;i<itllines.length;i++){
			
			if(itllines[i].startsWith("Comment") || itllines[i].startsWith("comment")){
				itllines[i]="";
			}
			if (itllines[i].startsWith("Version") || itllines[i].startsWith("Start") || itllines[i].startsWith("End") || itllines[i].startsWith("Init") || itllines[i].startsWith("Source_file") || itllines[i].startsWith("Include") || itllines[i].startsWith("Ref_date")||itllines[i].contains("ANTENNA")){
				
				if (itllines[i].startsWith("Version:")){
					String sSD=itllines[i].replace("Version:", "");

					result.setVersion(sSD);
				}

				if (itllines[i].startsWith("Init_mode:")){
					String sSD=itllines[i].replace("Init_mode:", "");

					result.getInitModes().append(sSD);
				}
				if (itllines[i].startsWith("Init_MS:")){
					String sSD=itllines[i].replace("Init_MS:", "");

					result.getInitMS().append(sSD);
				}
				if (itllines[i].startsWith("Init_memory:")){
					String sSD=itllines[i].replace("Init_memory:", "");

					result.getInitMemory().append(sSD);
				}
				if (itllines[i].startsWith("Init_data_store:")){
					String sSD=itllines[i].replace("Init_data_store:", "");

					result.getInitDataStore().append(sSD);
				}

				if (itllines[i].startsWith("Ref_date:")){
					String sSD=itllines[i].replace("Ref_date:", "");
					refDate=DateUtil.literalToDateNoTime(sSD);
				}

				if (itllines[i].startsWith("Start")){
					String sSD=itllines[i].replace("Start_time:", "");
					try{
						result.setStartDate(new FineTime(DateUtil.literalToDate(sSD)));
					}catch (ParseException pe){
						result.setStartDate(new FineTime(addDelta(refDate,sSD)));
					}
				}
				if (itllines[i].startsWith("End")){
					String sSD=itllines[i].replace("End_time:", "");
					try{
						result.setEndDate(new FineTime(DateUtil.literalToDate(sSD)));
					}catch (ParseException pe){
						result.setEndDate(new FineTime(addDelta(refDate,sSD)));
					}
				}
				if (itllines[i].startsWith("Include")){
					String sSD=itllines[i].replace("Include: ", "");
					sSD=sSD.replace("Include:", "");
					sSD=sSD.replace("Include_file:", "");
					sSD=sSD.replace("\"", "");
					Por tempPor=null;
					File f=new File(sSD);
					if (!f.exists()){
						f=new File(basePath+"/"+sSD);
						if (!f.exists()){
							throw(new IllegalArgumentException("Could not resolve include: "+itllines[i]+"Current Path is "+path+".Neither "+sSD+" or "+path+"/"+sSD+" exists"));
						}
					}
					try{
	
						SuperPor sp = parseItl(f.getAbsolutePath(),events,basePath,result.getStartDate().toDate(),result.getEndDate().toDate());
						tempPor=sp;
						
					}catch (Exception e){
						e.printStackTrace();
						try{
							tempPor=PorUtils.readPORfromFile(f.getAbsolutePath());
						}catch(Exception e2){
							throw(new IllegalArgumentException("Could not resolve include:"+sSD+":"+e.getMessage()+"."+e2.getMessage()));
						}
					}
					result.addPor(tempPor);
				}

				
			}else{
				if (!itllines[i].equals("")){
					Date[] exTimes=new Date[1];
					ItlEvent[] allEvents=new ItlEvent[1];
					allEvents[0]=null;
					exTimes[0]=null;
					
					String originalDelta=null;
					try{
						if (itllines[i].contains("COUNT")){
							String[] parts=itllines[i].split(" ");
							String[] oParts=parts[1].split("=");
							if (oParts.length<2){
								ParseException e= new ParseException("Could not parse line:"+itllines[i],i);
								throw(e);
							}
							String ct=oParts[1].replace(")", "");
							int count=Integer.parseInt(ct);
							String key=parts[0]+" "+"(COUNT="+ct+")";
							ItlEvent event=new ItlEvent(parts[0],count);
							allEvents[0]=event;
							itllines[i]=itllines[i].replace(key, "SKIP");
							exTimes[0]=events.getDate(event, result.getStartDate().toDate(), result.getEndDate().toDate());

						}
							String cl=itllines[i];
							String[] parts=cl.split(Pattern.quote("("));
							String sComm=parts[0];
							String[] pComm=sComm.split(" ");
							String commandName=pComm[pComm.length-1];
							String commandTime=pComm[0];
							String instrument="";
							String commandDelta="";
							if (pComm.length>4){
								commandDelta=pComm[1];
								originalDelta=pComm[1];
								instrument=pComm[2];
							}
							if (pComm.length==4){
								commandDelta="";
								instrument=pComm[1];
							}

							if (pComm.length<4){
		
								throw new ParseException("Could not get instrument name:"+itllines[i],i);
							}
							
							instrument=instrument.toUpperCase();
							if (!commandTime.equals("SKIP")){
								try{
									exTimes[0]=DateUtil.parse(commandTime);
								}catch (Exception e){
									allEvents = events.getAllEvents(commandTime, result.getValidityDates()[0], result.getValidityDates()[1]);
									exTimes=new Date[allEvents.length];
									for (int ae=0;ae<allEvents.length;ae++){
										exTimes[ae]=events.getDate(allEvents[ae], result.getValidityDates()[0], result.getValidityDates()[1]);
									}
									if (exTimes.length==0){
										VegaLog.warning("Could not find any valid event "+commandTime+" between "+DateUtil.defaultDateToString(result.getStartDate().toDate())+" "+DateUtil.defaultDateToString(result.getEndDate().toDate()));
									}
								}
							}else{
								Date startDateSearch=new Date(result.getStartDate().toDate().getTime()-deltaToMilli(commandDelta));
								Date endDateSearch=new Date(result.getEndDate().toDate().getTime()-deltaToMilli(commandDelta));
								exTimes[0]=events.getDate(allEvents[0], startDateSearch, endDateSearch);
								if (exTimes[0]==null){

									throw new IllegalArgumentException("Could not get a map for event "+allEvents[0]+ " in the time frame "+DateUtil.defaultDateToString(startDateSearch)+"-"+DateUtil.defaultDateToString(endDateSearch));
								}

							}
							if (exTimes.length>0 && exTimes[0]==null){
								
								throw new IllegalArgumentException("Could not parse extime "+itllines[i]+" validity dates: "+DateUtil.defaultDateToString(result.getStartDate().toDate())+" "+DateUtil.defaultDateToString(result.getEndDate().toDate()));
							}
							String sParam="";
							if (parts.length>1){
								sParam=parts[1].replace(")", "");
							}
							String[] sParamArr=separateParameters(sParam);
							DecimalFormat df=new DecimalFormat("000000000");
							String uid=ObservationPor.getUniqueID();
							InstrumentSequence seq=new InstrumentSequence(commandName,uid,DateUtil.dateToDOY(new Date()),instrument);
							int repeat=1;
							long separation=0;
							
							for (int j=0;j<sParamArr.length;j++){
								if (!sParamArr[j].startsWith("DATA_RATE") && !sParamArr[j].startsWith("POWER") && !sParamArr[j].startsWith("REPEAT") && !sParamArr[j].startsWith("SEPARATION")){
									try{
										seq.addParameter(parseParameter(sParamArr[j]));
									}catch (ParseException e){
										ParseException nex = new ParseException("At line:"+cl+"."+e.getMessage(),0);
										nex.initCause(e);
										throw nex;
									}
								}
								if (sParamArr[j].startsWith("DATA_RATE") || sParamArr[j].startsWith("POWER")){
									SequenceProfile[] profs=parseProfile(sParamArr[j]);
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
							for (int l=0;l<exTimes.length;l++){
								ItlEvent originalEvent = allEvents[l];
								exTimes[l]=new Date(exTimes[l].getTime()+deltaToMilli(commandDelta));
								seq.setExecutionDate(exTimes[l]);
								for (int k=0;k<repeat;k++){
									InstrumentSequence newSeq=new InstrumentSequence(new String(seq.getName()),new String(seq.getUniqueID()),new String(seq.getFlag()),new String(seq.getSource()),new Character(seq.getDestination()),new String(seq.getExecutionTime()),seq.getParameters(),seq.getProfiles(),instrument);
									newSeq.setExecutionDate(new java.util.Date(seq.getExecutionDate().getTime()+(separation*k)));
									newSeq.setUniqueID(ObservationPor.getUniqueID());
									if(k==0){
										newSeq.setEvent(originalEvent);
										newSeq.setDelta(originalDelta);
									}
									result.addSequence(newSeq);
				
								}
							}
					}catch (ParseException e){
						throw(e);
					}
				}
			}
		}
		//System.out.println("end loop lines");
		
		result.setCalculateValidity(true);
		return result;
	}
	
	
	protected static SequenceProfile[] parseProfile(String profile){
		SequenceProfile[] result;
		java.util.Vector<SequenceProfile> proVector=new java.util.Vector<SequenceProfile>();
		profile=profile.replace(" [Watts]", "");
		profile=profile.replace(" [W]", "");
		profile=profile.replace("[W]", "");

		profile=profile.replace(" [bits/sec]", "");
		profile=profile.replace("[Watts]", "");
		profile=profile.replace("[bits/sec]", "");
		String type="";
		if (profile.startsWith("DATA_RATE_PROFILE")) type=SequenceProfile.PROFILE_TYPE_DR;
		if (profile.startsWith("POWER_PROFILE")) type=SequenceProfile.PROFILE_TYPE_PW;
		profile=profile.replace("DATA_RATE_PROFILE=", "");
		profile=profile.replace("POWER_PROFILE=", "");
		String[] temp=profile.split(" ");
		for (int i=0;i<temp.length;i=i+2){
			proVector.add(new SequenceProfile(type,temp[i],Double.parseDouble(temp[i+1])));
		}
		result=new SequenceProfile[proVector.size()];
		proVector.toArray(result);
		return result;
	}
	protected static Parameter parseParameter(String param) throws ParseException{
		if (param.contains("\"")){
			int loc1=param.indexOf("\"");
			int loc2=param.indexOf("\"",loc1+1);
			String sub=param.substring(loc1,loc2);
			String sub2=sub.replace(" ", "-");
			
			param=param.replace(sub, sub2);
			
		}
		Parameter result=new Parameter("","","");
		String type="[RAW]";
		String[] temp=param.split("=");
		String[] temp2=temp[1].split(" ");
		if (temp2.length>1){
			type=temp2[1];
		}
		
		if (temp2[0].contains("\"")){
			int loc1=param.indexOf("\"");
			int loc2=param.indexOf("\"",loc1+1);
			String sub=param.substring(loc1,loc2);
			String sub2=sub.replace("-", " ");

			
			temp2[0]=temp2[0].replace(sub, sub2);
			
		}
		
		if (type.equals("[ENG]")){
			result=new ParameterString(temp[0],Parameter.REPRESENTATION_ENGINEERING,temp2[0]);
		}else{
			String rad=Parameter.RADIX_DECIMAL;
			float value;
			if (temp2[0].startsWith("0x") || temp2[0].startsWith("0X")){
				rad=Parameter.RADIX_HEX;
				value=new Integer(Integer.parseInt(temp2[0].substring(2), 16)).floatValue();
				result=new ParameterFloat(temp[0],Parameter.REPRESENTATION_RAW,rad,value);
			}else{
				try{
					value=Float.parseFloat(temp2[0]);
					result=new ParameterFloat(temp[0],Parameter.REPRESENTATION_RAW,rad,value);
				}catch (java.lang.NumberFormatException e){
					result=new ParameterString(temp[0],Parameter.REPRESENTATION_ENGINEERING,temp2[0]);
					/*ParseException e2=new ParseException(e.getMessage(),0);
					e2.initCause(e);
					throw e2;*/
				}

			}
			
			//result=new ParameterFloat(temp[0],Parameter.REPRESENTATION_RAW,rad,value);
		}
		
		return result;
	}
	
	protected static java.util.Date addDelta(java.util.Date initial,String delta){
		if (delta.equals("")) return initial;
		long ms=initial.getTime();
		boolean symbol=true;
		int days=0;
		int hours=0;
		int minutes=0;
		int seconds=0;
		if (delta.startsWith("-")) symbol=false;
		delta=delta.replace("+", "");
		delta=delta.replace("-", "");
		int index = delta.indexOf("_");
		if (index>0){
			String sDays=delta.substring(0,index);
			days=Integer.parseInt(sDays);
			delta=delta.replace(sDays+"_","");
			
		}
		
		
		String[] sTimes=delta.split(":");
		hours=Integer.parseInt(sTimes[0]);
		minutes=Integer.parseInt(sTimes[1]);
		seconds=Integer.parseInt(sTimes[2]);
		long deltams=((days*86400)+(hours*3600)+(minutes*60)+seconds)*1000;
		if (symbol){
			ms=ms+deltams;
		}
		else{
			ms=ms-deltams;
		}
		java.util.Date result=new java.util.Date(ms);

		return result;
		
	}
	
	public static long deltaToMilli(String delta){
		if (delta.equals("")) return 0;
		//long ms=initial.getTime();
		long ms=0;
		boolean symbol=true;
		int days=0;
		int hours=0;
		int minutes=0;
		int seconds=0;
		if (delta.startsWith("-")) symbol=false;
		delta=delta.replace("+", "");
		delta=delta.replace("-", "");
		if (delta.contains("_")){
			int index = delta.indexOf("_");
			String sDays=delta.substring(0,index);
			days=Integer.parseInt(sDays);
			delta=delta.replace(sDays+"_","");
		}
		String[] sTimes=delta.split(":");
		hours=Integer.parseInt(sTimes[0]);
		minutes=Integer.parseInt(sTimes[1]);
		seconds=Integer.parseInt(sTimes[2]);
		long dt=((days*86400)+(hours*3600)+(minutes*60)+seconds);
		long deltams=dt*1000;
		if (symbol){
			ms=ms+deltams;
		}
		else{
			ms=ms-deltams;
		}

		return ms;
		
	}
	
	protected static java.util.Date parseExDate(String exDate) throws ParseException{
		return DateUtil.parse(exDate);
	}
	
	protected static String[] separateParameters(String params){
		java.util.Vector<String> parNames=new java.util.Vector<String>();
		java.util.Vector<String> parVal=new java.util.Vector<String>();
		String[] result;
		String[] temp=params.split("=");
		for (int i=0;i<temp.length-1;i++){
			String[] temp2=temp[i].split(" ");
			parNames.add(temp2[temp2.length-1]);
		}
		result=new String[parNames.size()];
		for (int i=0;i<parNames.size();i++){
			int loc1=params.indexOf(parNames.get(i))+parNames.get(i).length()+1;
			int loc2=params.length();
			if (i!=parNames.size()-1){
				loc2=params.indexOf(parNames.get(i+1));
			}

			parVal.add(params.substring(loc1, loc2));
			result[i]=parNames.get(i)+"="+params.substring(loc1, loc2);
		}
		return result;
	}
	protected static String[] clean(String[] sourceLines) throws ParseException{
		String[] result;
		result=removeComments(sourceLines);
		result=removeBreaks(result);
		result=removeSpaces(result);
		//result=resolveIncludes(result,path);
		result=removeDuplicates(result,"Version");
		result=removeDuplicates(result,"Start_time");
		result=removeDuplicates(result,"End_time");
		
		return result;
	}
	
	protected static String[] resolveIncludes(String[] sourceLines,String filePath) throws ParseException{
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			if (line.startsWith("Include:") || line.startsWith("Include_file")){
				String relativePath=line.split(":")[1];
				relativePath=relativePath.replaceAll("\"", "");

				String[] includeLines;
				try{
					includeLines=readFile(filePath+"/"+relativePath.trim());

				}
				catch (java.io.IOException e){
					try{
						includeLines=readFile(dEvtPath+"/"+relativePath.trim());

					}catch (java.io.IOException e2){
						e2.printStackTrace();
						
						ParseException pae=new ParseException("At line:"+line+". File "+relativePath+" can not be resolved from "+filePath+" or "+dEvtPath,0);
						pae.initCause(e);
						throw pae;
					}
				}
				for (int j=0;j<includeLines.length;j++){
					lines.add(includeLines[j]);
				}
			}else{
				lines.add(line);
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	protected static String[] readString(String st) throws IOException, ParseException{
		BufferedReader br = null;

		
				br = new BufferedReader(new StringReader(st));
				try{
					return readFile(br);
				}catch (java.text.ParseException pex){
					ParseException nEx = new ParseException("In string "+st+"."+pex.getMessage(),0);
					nEx.initCause(pex);
					throw nEx;
				}		
	}
	protected static String[] readFile(BufferedReader br) throws IOException, ParseException{
		String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		
			while ((line = br.readLine()) != null){
				line=line.replace("#POWER_ON#", "0");
				line=line.replace("#DATA_ON#", "0");

				lines.add(line);
			}
		
		result = new String[lines.size()];
		lines.toArray(result);
		br.close();
		try{
			return clean(result);
		}catch (java.text.ParseException pex){
			ParseException nEx = new ParseException(pex.getMessage(),0);
			nEx.initCause(pex);
			throw nEx;
		}
	}
	protected static String[] readFile(File f) throws IOException, ParseException{
		BufferedReader br = null;
		
				br = new BufferedReader(new FileReader(f));
				try{
					return readFile(br);
				}catch (java.text.ParseException pex){
					ParseException nEx = new ParseException("In file "+f+"."+pex.getMessage(),0);
					nEx.initCause(pex);
					throw nEx;
				}
	}
	protected static String[] readFile(String file) throws IOException, ParseException{
		BufferedReader br = null;
		String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		
				br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null){
				line=line.replace("#POWER_ON#", "0");
				line=line.replace("#DATA_ON#", "0");

				lines.add(line);
			}
		
		result = new String[lines.size()];
		lines.toArray(result);
		br.close();
		try{
			return clean(result);
		}catch (java.text.ParseException pex){
			ParseException nEx = new ParseException("In file "+file+"."+pex.getMessage(),0);
			nEx.initCause(pex);
			throw nEx;
		}
	}
	
	protected static String[] removeComments(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			int index=line.indexOf("#");
			if (index==-1){
				lines.add(line);
			}
			if (index>0){
				lines.add(line.substring(0, index));
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	protected static String[] removeBreaks(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			int index=line.indexOf("\\");
			if (index==-1){
				lines.add(line);
			}else{
				String newLine=line.substring(0, index)+sourceLines[i+1];
				sourceLines[i+1]=newLine;
			}
			
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	protected static String[] removeSpaces(String[] sourceLines){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		for (int i=0;i<sourceLines.length;i++){
			String newLine=removeMultipleSpaces(sourceLines[i]);
			newLine=removeUselesSpaces(newLine);
			if(newLine.length()>1){
				lines.add(newLine);
			}
		}
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	protected static String removeMultipleSpaces(String line){
		line=line.replaceAll("\t", " ");
		int index=line.indexOf("  ");
		if (index==-1){
			return line;
		}else{
			return removeMultipleSpaces(line.replaceAll("  ", " "));
		}
	}
	
	protected static String removeUselesSpaces(String line){
		if (line.startsWith(" ")) line=line.substring(1);
		line=line.replaceAll(Pattern.quote("(")+" ", "(");
		line=line.replaceAll(" "+Pattern.quote(")"), ")");
		line=line.replaceAll(" =", "=");
		line=line.replaceAll("= ", "=");
		line=line.replaceAll(": ", ":");
		return line;
		

	}
	
	protected static String[] removeDuplicates(String[] sourceLines,String pattern){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		boolean found=false;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			if (line.startsWith(pattern)){
				if (!found){
					lines.add(line);
					found=true;
				}
			}else{
				lines.add(line);
			}
		}
		
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	
	public static String[] substitute(String[] sourceLines,String pattern,String newString){
		java.util.Vector<String> lines=new java.util.Vector<String>();
		String[] result;
		//boolean found=false;
		for (int i=0;i<sourceLines.length;i++){
			String line=sourceLines[i];
			line=line.replaceAll(pattern, newString);
			lines.add(line);
			//if (line.indexOf(newString)>-1) found=true;
		}
		//if (!found) System.out.println("Pattern: "+pattern+" not found");
		result = new String[lines.size()];
		lines.toArray(result);
		return result;
	}
	public static String SuperPortoITL(SuperPor POR){
		Mib mib;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
		//String result="";
		StringBuilder result=new StringBuilder();
		result.append("");
		String l01="Version: "+POR.getVersion()+"\n";
		//String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[1])+"\n\n\n";
		//String l05="";
		StringBuilder l05=new StringBuilder();
		l05.append("");
		String[] initModes = POR.getInitModes().toArray();
		for (int i=0;i<initModes.length;i++){
			l05.append("Init_mode: "+initModes[i]+"\n");
		}
		String[] initMS = POR.getInitMS().toArray();
		for (int i=0;i<initMS.length;i++){
			l05.append("Init_MS: "+initMS[i]+"\n");
		}
		String[] initMemory = POR.getInitMemory().toArray();
		for (int i=0;i<initMemory.length;i++){
			l05.append("Init_memory: "+initMemory[i]+"\n");
		}
		String[] initDataStore = POR.getInitDataStore().toArray();
		for (int i=0;i<initDataStore.length;i++){
			l05.append("Init_data_store: "+initDataStore[i]+"\n");
		}
		Por[] includes = POR.getPors();
		for (int i=0;i<includes.length;i++){
			l05.append("Include: "+includes[i].getPath()+"/"+includes[i].getName()+"\n");
		}
		AbstractSequence[] tempSeq=POR.getOrderedInternalSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			l05.append(DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				String val=tempParam[z].getStringValue();
				if (tempParam[z].getRepresentation().equals("Engineering")){
					if (val!=null && !val.startsWith("\"")){
						val="\""+val+"\"";
					}
				}
				l05.append("\t"+tempParam[z].getName()+"="+val+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+"\n");
			}
			tempPro=tempSeq[i].getProfiles();
			String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
			String powerProfile="\tPOWER_PROFILE = \t\t\t";
			boolean dataRatePresent=false;
			boolean powerProfilePresent=false;
			for (int j=0;j<tempPro.length;j++){
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
					dataRateProfile=dataRateProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]";
					dataRatePresent=true;
				}
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
					powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
					powerProfilePresent=true;
				}
				
			}
			if (dataRatePresent) l05.append(dataRateProfile+"\\\n");
			if (powerProfilePresent) l05.append(powerProfile+"\\\n");

			l05.append("\t\t\t\t)\n");

		}
		result.append(l01);
		result.append(l02);
		result.append(l03);
		result.append(l04);
		result.append(l05);
		return result.toString();
	}
	
	public static String SuperPortoITLEvents(SuperPor POR){
		Mib mib;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
		//String result="";
		StringBuilder result=new StringBuilder();
		result.append("");
		String l01="Version: "+POR.getVersion()+"\n";
		//String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[1])+"\n\n\n";
		StringBuilder l05=new StringBuilder();
		l05.append("");
		//String l05="";
		String[] initModes = POR.getInitModes().toArray();
		for (int i=0;i<initModes.length;i++){
			l05.append("Init_mode: "+initModes[i]+"\n");
		}
		String[] initMS = POR.getInitMS().toArray();
		for (int i=0;i<initMS.length;i++){
			l05.append("Init_MS: "+initMS[i]+"\n");
		}
		String[] initMemory = POR.getInitMemory().toArray();
		for (int i=0;i<initMemory.length;i++){
			l05.append("Init_memory: "+initMemory[i]+"\n");
		}
		String[] initDataStore = POR.getInitDataStore().toArray();
		for (int i=0;i<initDataStore.length;i++){
			l05.append("Init_data_store: "+initDataStore[i]+"\n");
		}
		Por[] includes = POR.getPors();
		for (int i=0;i<includes.length;i++){
			l05.append("Include: "+includes[i].getPath()+"/"+includes[i].getName()+"\n");
		}
		AbstractSequence[] tempSeq=POR.getOrderedInternalSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			if (InterpreterUtil.isInstance(InstrumentSequence.class, tempSeq[i])){
				ItlEvent event = ((InstrumentSequence) tempSeq[i]).getEvent();
				String delta = ((InstrumentSequence) tempSeq[i]).getDelta();
				if (event==null){
					l05.append(DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
				}
				if (event!=null && delta==null){
					l05.append("#"+DateUtil.defaultDateToString(tempSeq[i].getExecutionDate())+"\n");
					l05.append(event.toString()+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
				}
				if (event!=null && delta!=null){
					l05.append("#"+DateUtil.defaultDateToString(tempSeq[i].getExecutionDate())+"\n");
					l05.append(event.toString()+" "+delta+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
				}

			}else{
				l05.append(DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
			}
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				String val=tempParam[z].getStringValue();
				if (tempParam[z].getRepresentation().equals("Engineering")){
					if (val!=null && !val.startsWith("\"")){
						val="\""+val+"\"";
					}
				}
				l05.append("\t"+tempParam[z].getName()+"="+val+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+"\n");
			}
			tempPro=tempSeq[i].getProfiles();
			String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
			String powerProfile="\tPOWER_PROFILE = \t\t\t";
			boolean dataRatePresent=false;
			boolean powerProfilePresent=false;
			for (int j=0;j<tempPro.length;j++){
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
					dataRateProfile=dataRateProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]";
					dataRatePresent=true;
				}
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
					powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
					powerProfilePresent=true;
				}
				
			}
			if (dataRatePresent) l05.append("\\\n");
			if (powerProfilePresent) l05.append(powerProfile+"\\\n");

			l05.append("\t\t\t\t)\n");

		}
		result.append(l01);
		result.append(l02);
		result.append(l03);
		result.append(l04);
		result.append(l05);
		return result.toString();
	}
	

	

}
