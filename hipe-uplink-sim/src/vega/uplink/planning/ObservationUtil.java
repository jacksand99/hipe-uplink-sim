package vega.uplink.planning;

import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.commanding.itl.ItlUtil;
import vega.uplink.planning.gui.ScheduleModel;
import vega.uplink.planning.period.Plan;
import vega.uplink.planning.period.Stp;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingAttitude;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.PointingMetadata;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.PtrParameters.OffsetRefAxis;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetFixed;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;
import vega.uplink.pointing.exclusion.AbstractExclusion;

public class ObservationUtil {
	private static final Logger LOG = Logger.getLogger(ObservationUtil.class.getName());
	
	public static void saveObservation(Observation obs) throws IOException{
		saveObservationToFile(obs.getFileName(),obs);
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
	
	public static Schedule readScheduleFromFile(String file) throws IOException{
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			Schedule result = readScheduleFromDoc(doc);
			result.setFileName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());

			return result;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
	}
	
	public static ObservationsSchedule readObservationScheduleFromNode(Node node) throws IOException{
		ObservationsSchedule result = new ObservationsSchedule();
		try{
			Element el = (Element) node;
			NodeList nListHeader = el.getElementsByTagName("observation");
			int l = nListHeader.getLength();
			for (int i=0;i<l;i++){
				Observation obs=readObservationFromNode(nListHeader.item(i));
				result.addObservation(obs);
			}
			return result;
		}catch (Exception e){
	    	e.printStackTrace();
	    	IOException io = new IOException(e.getMessage());
	    	io.initCause(e);
	    	throw(io);
			
		}
	}
	
	public static Schedule readScheduleFromDoc(Document doc) throws IOException{
		Schedule result;
		try{
			doc.getDocumentElement().normalize();
			 
			NodeList nListHeader = doc.getElementsByTagName("segment");
			Node nSegment =nListHeader.item(0);
			PtrSegment ptrSegment = PtrUtils.getPtrSegmentFromNode(nSegment);
			result=new Schedule(ptrSegment);
			NodeList nListHeader2 = doc.getElementsByTagName("observationsSchedule");
			Node nObsSchedule =nListHeader2.item(0);
			ObservationsSchedule obsSchedule=readObservationScheduleFromNode(nObsSchedule);
			result.setObservationsSchedule(obsSchedule);
			Pdfm pdfm=null;
			try{
				NodeList nListHeader3 = doc.getElementsByTagName("definition");
				Node nSegment3 =nListHeader3.item(0);
				pdfm =PtrUtils.readPdfmfromNode(nSegment3);
			}catch (Exception e){
				Log.info("Could not load PDFM from the schedule:"+e.getMessage());
				e.printStackTrace();
			}
			
			if (pdfm!=null) result.setPdfm(pdfm);
			
			Plan plan=null;
			try{
				plan = (Plan) Plan.readFromNode(doc.getElementsByTagName("PLAN").item(0));
			}catch (Exception e){
				Log.info("Could not load PLAN from the schedule:"+e.getMessage());
				e.printStackTrace();
			}
			if (plan!=null) result.setPlan(plan);
			
			AbstractExclusion exclusion=null;
			try{
				exclusion = (AbstractExclusion) AbstractExclusion.readFromNode(doc.getElementsByTagName("exclusionPeriods").item(0));
			}catch (Exception e){
				Log.info("Could not load PLAN from the schedule:"+e.getMessage());
				e.printStackTrace();
			}
			if (exclusion!=null) result.setExclusion(exclusion);

			return result;
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }

	}
	
	public static void saveScheduleToFile(String file,Schedule sch) throws IOException {
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(sch.toXml());
			writer.close();
		}catch (Exception e){
			IOException io = new IOException(e.getMessage());
			io.initCause(e);
			throw(io);
		}
	}
	public static void saveObservationToFile(String file,Observation obs) throws IOException {
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(obs.toXml());
			writer.close();
		}catch (Exception e){
			IOException io = new IOException(e.getMessage());
			io.initCause(e);
			throw(io);
		}
	}
	
	public static long getOffsetMilliSeconds(String sOffset){
		int symbol=1;
		if (sOffset.startsWith("-")){
			symbol=-1;
			sOffset=sOffset.replace("-", "");
		}
		sOffset=sOffset.replace("+", "");
		long offset=0;
		String[] times=sOffset.split(":");
		try{
			int hours=Integer.parseInt(times[0]);
			int minutes=Integer.parseInt(times[1]);
			int seconds=Integer.parseInt(times[2]);
			offset=(hours*3600)+(minutes*60)+seconds;
			return offset*1000*symbol;
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not parse "+sOffset+" "+e.getMessage());
			iae.initCause(e);
			throw(iae);
		}

	}
	
	public static String getOffset(long milliseconds){
		String symbol="+";
		if (milliseconds<0){
			symbol="-";
			milliseconds=milliseconds*-1;
		}
		
		long totalSec = milliseconds/1000;
		int hours = (int) totalSec / 3600;
		int remainder = (int) totalSec - hours * 3600;
		int mins = remainder / 60;
		remainder = remainder - mins * 60;
		int secs = remainder;
		String result=""+String.format("%02d", hours)+":"+String.format("%02d", mins)+":"+String.format("%02d", secs);
		return symbol+result;
	}
	
	public static Observation readObservationFromNode(Node node) throws IOException{
		Observation result=new Observation(new Date(),new Date());

		try{
			Element el = (Element) node;
			String name=el.getElementsByTagName("name").item(0).getTextContent();
			String type=el.getElementsByTagName("type").item(0).getTextContent();
			String creator=el.getElementsByTagName("creator").item(0).getTextContent();
			String description=el.getElementsByTagName("description").item(0).getTextContent();
			String instrument=el.getElementsByTagName("instrument").item(0).getTextContent();
			String formatVersion=el.getElementsByTagName("formatVersion").item(0).getTextContent();
			Date startDate=DateUtil.zuluToDate(el.getElementsByTagName("startDate").item(0).getTextContent());
			Date endDate=DateUtil.zuluToDate(el.getElementsByTagName("endDate").item(0).getTextContent());
			result.setName(name);
			result.setType(type);
			result.setCreator(creator);
			result.setDescription(description);
			result.setInstrument(instrument);
			result.setFormatVersion(formatVersion);
			result.setStartDate(new FineTime(startDate));
			result.setEndDate(new FineTime(endDate));
			
			Node seqNode = el.getElementsByTagName("sequences").item(0);
			NodeList nListSequences = ((Element) seqNode).getElementsByTagName("sequence");
			ObservationSequence[] seq=readSequences(nListSequences,result);
			for (int i=0;i<seq.length;i++){
				result.addObservationSequence(seq[i]);
			}
			
			Node poiNode = el.getElementsByTagName("pointingBlocks").item(0);
			NodeList nListBlocks = ((Element) poiNode).getElementsByTagName("block");
			ObservationPointingBlock[] blocks=readBlocks(nListBlocks,result);
			for (int i=0;i<blocks.length;i++){
				result.addObservationBlock(blocks[i]);
			}
			//System.out.println(result.getPointing().toXml(0));
			return result;
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
		
	}
	public static ObservationPor readObsPorfromDoc(Document doc) throws IOException{
		try{
			doc.getDocumentElement().normalize();
			NodeList nListSeqs = (doc.getDocumentElement()).getElementsByTagName("sequence");
			Observation obs=new Observation(new Date(),new Date());
			ObservationSequence[] seq = readSequences(nListSeqs,obs);
			//Observation result=new Observation(new Date(),new Date());
			//ObservationPointingBlock[] blocks=readBlocks(nListBlocks,obs);
			for (int i=0;i<seq.length;i++){
				obs.addObservationSequence(seq[i]);
			}
			return (ObservationPor) obs.getCommanding();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	IOException io = new IOException(e.getMessage());
	    	io.initCause(e);
	    	throw(io);
	    	
	    }
	}

	public static ObservationPointingSlice readSlicefromDoc(Document doc) throws IOException{
		try{
			doc.getDocumentElement().normalize();
			NodeList nListBlocks = (doc.getDocumentElement()).getElementsByTagName("block");
			//Observation result=new Observation(new Date(),new Date());
			Observation obs = new Observation(new Date(),new Date());
			ObservationPointingBlock[] blocks=readBlocks(nListBlocks,obs);
			for (int i=0;i<blocks.length;i++){
				obs.addObservationBlock(blocks[i]);
			}
			return (ObservationPointingSlice) obs.getPointing();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	IOException io = new IOException(e.getMessage());
	    	io.initCause(e);
	    	throw(io);
	    	
	    }
	}
	public static Observation readObservationFromDoc(Document doc) throws IOException{

		try{
			doc.getDocumentElement().normalize();
			 
			NodeList nListHeader = doc.getElementsByTagName("observation");
			Node nHeader =nListHeader.item(0);
			return readObservationFromNode(nHeader);

			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }

	}
	public static Observation readObservationFromFile(String file) throws IOException{
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			Observation result = readObservationFromDoc(doc);
			result.setFileName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			return result;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
	}
	
	protected static OffsetAngles readOffsetPointing(Observation obs,PointingElement pe){
		if (pe.getChild("startTime")!=null){
			String sStartTime = pe.getChild("startTime").getValue();
			String startEventName="";
			String startOffSet="";
			String eventTokens[];
			OffsetAngles result=null;
			if (sStartTime.contains("+")){
				eventTokens = sStartTime.split("\\+");
				startEventName=eventTokens[0];
				startOffSet="+"+eventTokens[1];
			}else{
				eventTokens = sStartTime.split("\\-");
				startEventName=eventTokens[0];
				startOffSet="-"+eventTokens[1];
		
			}

			pe.getChild("startTime").setValue(DateUtil.dateToZulu(new Date()));
			try{
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM)){
					result=new ObservationOffsetCustom(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetCustom(pe));
			}
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_RASTER)){
				result=new ObservationOffsetRaster(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetRaster(pe));
			}
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_SCAN)){
				result=new ObservationOffsetScan(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetScan(pe));
			}
			}catch (IllegalArgumentException iae){
				IllegalArgumentException i = new IllegalArgumentException("Could not read offset custom in Observation:"+pe.toXml(0)+" "+iae.getMessage());
				i.initCause(iae);
				throw (i);
			}

			return result;
		}
		else return new OffsetFixed(pe);
	}
	
	private static PointingBlock translatePointingBlock(PointingElement pe,Observation obs){
		PointingBlock result;
		pe.getChild("startTime").setValue(DateUtil.dateToZulu(obs.getObsStartDate()));
		pe.getChild("endTime").setValue(DateUtil.dateToZulu(obs.getObsEndDate()));
		PointingElement at = pe.getChild(PointingAttitude.ATTITUDE_TAG);
		PointingElement os = at.getChild(OffsetAngles.OFFSETANGLES_TAG);
		if (os!=null){
			OffsetAngles ofAngles=readOffsetPointing(obs,os);
			at.remove(OffsetAngles.OFFSETANGLES_TAG);
			PointingAttitude attitude=new PointingAttitude(at);
			attitude.addChild(ofAngles);
			result=new PointingBlock(pe);
			result.setAttitude(attitude);
			//System.out.println(result.toXml(0));
		}else{
			PointingAttitude attitude=new PointingAttitude(at);
			
			result=new PointingBlock(pe);
			result.setAttitude(attitude);
		}
		//System.out.println(result.toXml(0));
		return result;

	}
	private static ObservationPointingBlock[] readBlocks(NodeList nList,Observation obs) throws ParseException, IOException{
		 ObservationPointingBlock[] result = new ObservationPointingBlock[nList.getLength()];
		 for (int temp = 0; temp < nList.getLength(); temp++) {
			 
				Node nNode = nList.item(temp);
				PointingElement pe = PointingElement.readFrom(nNode);
				String sStartTime = pe.getChild("startTime").getValue();
				String sEndTime = pe.getChild("endTime").getValue();
				String startEventName="";
				String startOffSet="";
				String endEventName="";
				String endOffSet="";

				String eventTokens[];
				if (sStartTime.contains("+")){
					eventTokens = sStartTime.split("\\+");
					startEventName=eventTokens[0];
					startOffSet="+"+eventTokens[1];
				}else{
					eventTokens = sStartTime.split("\\-");
					startEventName=eventTokens[0];
					startOffSet="-"+eventTokens[1];
			
				}
				if (sEndTime.contains("+")){
					eventTokens = sEndTime.split("\\+");
					endEventName=eventTokens[0];
					endOffSet="+"+eventTokens[1];
				}else{
					eventTokens = sEndTime.split("\\-");
					endEventName=eventTokens[0];
					endOffSet="-"+eventTokens[1];
			
				}
				PointingBlock pb =translatePointingBlock(pe,obs);
				if (nNode.hasChildNodes()){
					NodeList children = nNode.getChildNodes();
					int size=children.getLength();
					for (int i=0;i<size;i++){
						PointingElement child=null;
						if (children.item(i).getNodeName().equals(PointingMetadata.METADATA_TAG)){
							child = PointingMetadata.readFrom(children.item(i));
						}
						if (child!=null) pb.addChild(child);
					}			
				}
				try{
					result[temp]=new ObservationPointingBlock(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new ObservationEvent(endEventName),ObservationUtil.getOffsetMilliSeconds(endOffSet),pb);
					//System.out.println(result[temp].toXml(0));
				}catch (Exception e){
					IllegalArgumentException iae = new IllegalArgumentException("Could not read ObservationPointingBlock "+pe.toXml(0)+" "+e.getMessage());
					iae.initCause(e);
					throw(iae);
				}
				
		 }
		 
		 return result;
	}
	private static ObservationSequence[] readSequences(NodeList nList,Observation obs) throws ParseException, IOException{
		ObservationSequence[] result = new ObservationSequence[nList.getLength()];
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	 
				String sName=eElement.getAttribute("name");
				String sExecutionTime=eElement.getElementsByTagName("actionTime").item(0).getTextContent();
				String eventTokens[];
				
				String eventName="";
				String offSet="";
				if (sExecutionTime.contains("+")){
					eventTokens = sExecutionTime.split("\\+");
					eventName=eventTokens[0];
					offSet="+"+eventTokens[1];
				}else{
					eventTokens = sExecutionTime.split("\\-");
					eventName=eventTokens[0];
					offSet="-"+eventTokens[1];
			
				}
				Parameter[] sParameters=new Parameter[0];
				SequenceProfile[] sProfiles=new SequenceProfile[0];
				try{
					Node parList=eElement.getElementsByTagName("parameterList").item(0);
					Element el2=(Element) parList;
					NodeList params=el2.getElementsByTagName("parameter");
					sParameters=PorUtils.readParameters(params);
				}catch (NullPointerException npe){
					
				}
				try{
					Node proList=eElement.getElementsByTagName("profileList").item(0);
					Element el3=(Element) proList;
					NodeList profiles=el3.getElementsByTagName("profile");
					sProfiles=PorUtils.readProfiles(profiles);
				}catch (NullPointerException npe){
					
				}
				ObservationEvent event = new ObservationEvent(eventName);
				result[temp]=new ObservationSequence (obs,event,getOffsetMilliSeconds(offSet),sName,"P"+String.format("%05d", temp),sParameters,sProfiles);
				
			}
		}
		return result;
	}
	
	public static String scheduleToMappsEvents(Schedule schedule){
		String result = "";
		HashSet<String> observationNames=new HashSet<String>();
		Observation[] observations = schedule.getObservations();
		for (int i=0;i<observations.length;i++){
			observationNames.add(getEventString(observations[i]));
		}
		Iterator<String> it = observationNames.iterator();
		int mtpNumber=Integer.parseInt(schedule.getPtslSegment().getName().replace("MTP_", ""));;
		int counter=mtpNumber*1000;
		while (it.hasNext()){
		//for (int i=0;i<observations.length;i++){
			String name=it.next();
			//name=name.replace(" ", "_");
			//name=getEventString(observations[i]);
			result=result+counter+"\t"+name+"_\t"+name+"_"+"SO"+"\t"+name+"_"+"EO"+"        -     -   FALSE   -   0   PTB  CONTINUOUS  INACTIVE\n";
			counter++;
		}
		return result;
	}
	public static String getEventString(Observation obs){
		String event=obs.getName().toUpperCase();
		String insAc=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+obs.getInstrument());
		event=event.replace(obs.getInstrument(), insAc);
		event=event.replace(" ", "_");
		event=event.replace("-", "_");
		event=event.replace("(", "");
		event=event.replace(")", "");
		event=event.replace("/", "_");
		event=event.replace("\\", "_");
		event=event.replace(".", "_");
		event=event.replace("=", "_");
		event=event.replace(",", "_");
		event=event+"______________________________";	
		event=event.substring(0,26);	
		return event;
	}
	public static String scheduleToEVF(Schedule schedule){
		return scheduleToEVF(schedule,Integer.parseInt(schedule.getPtslSegment().getName().replace("MTP_", ""))*10000);
	}
	public static String scheduleToEVF(Schedule schedule,int sed){
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		String result="";
		Por POR = schedule.getPor();
		Observation[] observations = schedule.getObservations();
		String l05="";
		for (int i=0;i<observations.length;i++){
			String eventName=getEventString(observations[i]);
			Integer count = counter.get(eventName);
			if (count==null) count=sed;
			else count=count+1;
			counter.put(eventName, count);
			String itlEventStart=eventName+"_SO";
			l05=l05+DateUtil.dateToLiteral(observations[i].getStartDate().toDate())+" "+itlEventStart+" (COUNT = "+String.format("%06d", count)+" )\n";
			String itlEventEnd=eventName+"_EO";
			l05=l05+DateUtil.dateToLiteral(observations[i].getEndDate().toDate())+" "+itlEventEnd+" (COUNT = "+String.format("%06d", count)+" )\n";

		}
		result=l05;
		return result;
	}
	
	public static String OBStoEVF(Observation obs){
		String result="";
		String l03="Start_time: "+DateUtil.dateToLiteral(obs.getStartDate().toDate())+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(obs.getEndDate().toDate())+"\n\n\n";
		String itlEventStart=obs.getName()+"_"+ObservationEvent.START_OBS.getName();
		itlEventStart=itlEventStart.replace(" ", "_");
		String l05=DateUtil.dateToLiteral(obs.getStartDate().toDate())+" "+itlEventStart+" (COUNT = 000001)\n";
		String itlEventEnd=obs.getName()+"_"+ObservationEvent.END_OBS.getName();
		itlEventEnd=itlEventEnd.replace(" ", "_");
		String l06=DateUtil.dateToLiteral(obs.getEndDate().toDate())+" "+itlEventEnd+" (COUNT = 000001)\n";
		result=l03+l04+l05+l06;
		return result;
	}
	public static String scheduleToITL(Schedule schedule){
		//return scheduleToITL(schedule,Integer.parseInt(schedule.getPtslSegment().getName().replace("MTP_", ""))*10000);
		return ItlUtil.porToITL(schedule.getPor(), Integer.parseInt(schedule.getPtslSegment().getName().replace("MTP_", ""))*10000);
	}
	/*public static String scheduleToITL(Schedule schedule, int sed){
		Mib mib;
		try {
			mib=Mib.getMib();
		} catch (IOException e) {
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		LOG.info("Getting the POR");
		Por POR = schedule.getPor();
		LOG.info("Finsih getting the POR");
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(schedule.getPtslSegment().getStartDate().toDate())+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getStartDate().toDate())+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getEndDate().toDate())+"\n\n\n";
		String l045="#========================================================================\n"+
		"#\n"+
		"# Name: "+schedule.getFileName()+"\n"+
		"#\n"+ 
		"# Description: "+schedule.getDescription()+"\n"+
		"#\n"+ 
		"# Author: "+schedule.getCreator()+"\n"+ 
		"#\n"+ 
		"# Type: "+schedule.getType()+"\n"+ 
		"#\n"+ 
		"# Date: "+DateUtil.dateToLiteral(new Date())+"\n"+
		"#======================================================================== \n\n\n";
		Observation[] observations = schedule.getObservations();		
		String l05="";
		for (int i=0;i<observations.length;i++){
			String eventName=getEventString(observations[i]);
			Integer count = counter.get(eventName);
			if (count==null) count=sed;
			else count=count+1;
			counter.put(eventName, count);
			AbstractSequence[] tempSeq = observations[i].getCommanding().getSequences();
			Parameter[] tempParam;
			SequenceProfile[] tempPro;
			for (int j=0;j<tempSeq.length;j++){
				if (InterpreterUtil.isInstance(ObservationSequence.class, tempSeq[j])){
					String itlEvent="";
					if (((ObservationSequence)tempSeq[j]).getExecutionTimeEvent().getName().equals("START_OBS")){
						itlEvent=eventName+"_SO";
					}
					if (((ObservationSequence)tempSeq[j]).getExecutionTimeEvent().getName().equals("END_OBS")){
						itlEvent=eventName+"_EO";
					}
	
					l05=l05+itlEvent+" "+"(COUNT = "+String.format("%06d", count)+" ) "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[j]).getExecutionTimeDelta())+" "+ tempSeq[j].getInstrumentName()+"\t*\t"+tempSeq[j].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[j].getName())+"\n";
				}else{
					l05=l05+DateUtil.dateToLiteral(tempSeq[j].getExecutionDate())+" "+ tempSeq[j].getInstrument()+"\t*\t"+tempSeq[j].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[j].getName())+"\n";
					LOG.info("WARNING:"+DateUtil.dateToLiteral(tempSeq[j].getExecutionDate())+" "+ tempSeq[j].getInstrument()+" "+tempSeq[j].getName()+" is a literal sequence");

				}
				tempParam = tempSeq[j].getParameters();
				tempParam = tempSeq[j].getParameters();
				for (int z=0;z<tempParam.length;z++){
					l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+" \n";
				}
				tempPro=tempSeq[j].getProfiles();
				String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
				String powerProfile="\tPOWER_PROFILE = \t\t\t";
				boolean dataRatePresent=false;
				boolean powerProfilePresent=false;
				for (int k=0;k<tempPro.length;k++){
					if (tempPro[k].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
						dataRateProfile=dataRateProfile+" "+tempPro[k].getOffSetString()+"\t"+new Double(tempPro[k].getValue()).toString()+"\t[bits/sec]";
						dataRatePresent=true;
					}
					if (tempPro[k].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
						powerProfile=powerProfile+" "+tempPro[k].getOffSetString()+"\t"+new Double(tempPro[k].getValue()).toString()+"\t[Watts]";
						powerProfilePresent=true;
					}
					
				}
				if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
				if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

				l05=l05+"\t\t\t\t)\n";

			}
			

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;

	}*/
	public static void savePors(String directory,Schedule sch){
		String sufix=".POR";
		try{
			String porType=Properties.getProperty("vega.file.type.POR");
			int indexOfDot = porType.indexOf(".");
			sufix=porType.substring(indexOfDot);
		}catch (Exception e){
			sufix=".POR";
		}
		savePors(directory,sch,sufix);
	}
	public static void savePors(String directory,Schedule sch,String sufix){
		File dir=new File(directory);
		if (!dir.isDirectory()) throw new IllegalArgumentException("Pors need to be saved in a directory");
		Por por=sch.getPor();
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		String mtpNumber=sch.getPtslSegment().getName().replace("MTP_", "");
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			//Schedule insSch=subSch.getInstrumentSchedule(instrument);
			Por insPor=por.getSubPor(instrument);
			//saveStringToFile(stpDir.getAbsolutePath()+"/EVF__"+acro+"_"+prefix+".evf",scheduleToEVF(insSch,sed));
			//saveStringToFile(stpDir.getAbsolutePath()+"/ITLS_"+acro+"_"+prefix+".itl",scheduleToITL(insSch,sed));
			try {
				if (insPor.getSequences().length>0){
					saveStringToFile(dir.getAbsolutePath()+"/POR__DM_"+mtpNumber+"_01_"+acro+sufix,insPor.toXml());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException("Could not save POR:"+e.getMessage());
				iae.initCause(e);
				throw(iae);
			}

		}
	}
	public static void saveMappsProducts(String file,Schedule sch,Plan plan) throws IOException{
		String mtpName = sch.getPtslSegment().getName();
		int mtpNumber=Integer.parseInt(mtpName.replace("MTP_", ""));
		Stp[] stps = plan.getMtp(mtpNumber).getStps();
		File f=new File(file);
		String dir = f.getParent();
		String fn=f.getName();
		for (int i=0;i<stps.length;i++){
			int sed=((mtpNumber*10)+i)*1000;
			File stpDir = new File(dir+"/STP"+String.format("%03d", stps[i].getNumber()));
			stpDir.mkdir();
			String prefix="M"+String.format("%03d", mtpNumber)+"_S"+String.format("%03d", stps[i].getNumber())+"_"+fn;
			//Schedule subSch = sch.getPeriodSchedule(stps[i].getStartDate().toDate(), stps[i].getEndDate().toDate());
			Por subPor=sch.getPor().getSubPor(stps[i].getStartDate().toDate(), stps[i].getEndDate().toDate());
			List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
			Iterator<String> it = ins.iterator();
			while(it.hasNext()){
				String instrument = it.next();
				String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
				//Schedule insSch=subSch.getInstrumentSchedule(instrument);
				Por insPor=subPor.getSubPor(instrument);
				//saveStringToFile(stpDir.getAbsolutePath()+"/EVF__"+acro+"_"+prefix+".evf",scheduleToEVF(insSch,sed));
				//saveStringToFile(stpDir.getAbsolutePath()+"/ITLS_"+acro+"_"+prefix+".itl",scheduleToITL(insSch,sed));
				saveStringToFile(stpDir.getAbsolutePath()+"/EVF__"+acro+"_"+prefix+".evf",ItlUtil.porToEVF(insPor, sed));
				saveStringToFile(stpDir.getAbsolutePath()+"/ITLS_"+acro+"_"+prefix+".itl",ItlUtil.porToITL(insPor, sed,stps[i].getStartDate().toDate(),stps[i].getEndDate().toDate()));

			}
			saveStringToFile(stpDir.getAbsolutePath()+"/TLIS_PL_"+prefix+".itl",schToTLISITL(prefix,sch,"STP"+String.format("%03d", stps[i].getNumber())));
			saveStringToFile(stpDir.getAbsolutePath()+"/TLIS_PL_"+prefix+".evf",schToTLISEVF(prefix,sch,"STP"+String.format("%03d", stps[i].getNumber())));

		}
		saveStringToFile(dir+"/TLIS_PL_"+"M"+String.format("%03d", mtpNumber)+"______"+fn+".itl",schToTLISITL(fn,sch,mtpNumber,stps));
		saveStringToFile(dir+"/TLIS_PL_"+"M"+String.format("%03d", mtpNumber)+"______"+fn+".evf",schToTLISEVF(fn,sch,mtpNumber,stps));
		saveStringToFile(file+".def",scheduleToMappsEvents(sch));
	}

	public static void saveMappsProducts(String file,Schedule sch) throws IOException{
		File f=new File(file);
		String dir = f.getParent();
		String fn=f.getName();
		saveStringToFile(file+".evf",scheduleToEVF(sch));
		saveStringToFile(file+".itl",scheduleToITL(sch));
		saveStringToFile(file+".def",scheduleToMappsEvents(sch));
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			Schedule insSch=sch.getInstrumentSchedule(instrument);
			saveStringToFile(dir+"/EVF__"+acro+"_"+fn+".evf",scheduleToEVF(insSch));
			saveStringToFile(dir+"/ITLS_"+acro+"_"+fn+".itl",scheduleToITL(insSch));

		}
		saveStringToFile(dir+"/TLIS_PL_"+fn+".itl",schToTLISITL(fn,sch));
		saveStringToFile(dir+"/TLIS_PL_"+fn+".evf",schToTLISEVF(fn,sch));
		
	}
	
	public static String schToTLISEVF(String file,Schedule schedule,int mtp,Stp[] stps){
		String l03="Start_time: 01-January-2014_00:00:00\n";
		String l04="End_time:   01-January-2017_00:00:00\n";
		String result="";

		String l05="";
		String[] includes=schedule.getEvfIncludes();
		for (int i=0;i<includes.length;i++){
			l05=l05+"Include: \""+includes[i]+"\"\n";
		}

		for (int i=0;i<stps.length;i++){
			String prefix="M"+String.format("%03d", mtp)+"_S"+String.format("%03d", stps[i].getNumber());
			l05=l05+"Include: \"STP"+String.format("%03d", stps[i].getNumber())+"/TLIS_PL_"+prefix+"_"+file+".evf\""+"\n";

		}
		result=l03+l04+l05;
		return result;


	}
	public static String schToTLISEVF(String file,Schedule schedule,String dir){
		String l03="Start_time: 01-January-2014_00:00:00\n";
		String l04="End_time:   01-January-2017_00:00:00\n";
		
		String result="";

		String l05="";
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			l05=l05+"Include: \""+dir+"/EVF__"+acro+"_"+file+".evf\""+"\n";

		}
		result=l03+l04+l05;
		return result;


	}
	
	public static String schToTLISEVF(String file,Schedule schedule){
		String l03="Start_time: 20-January-2013_00:00:00\n";
		String l04="End_time: 26-December-2018_17:39:40\n";

		String result="";

		String l05="";
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			l05=l05+"Include: \"EVF__"+acro+"_"+file+".evf\""+"\n";

		}
		result=l03+l04+l05;
		return result;


	}
	
	public static String schToTLISITL(String file,Schedule schedule,int mtp,Stp[] stps){
		
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(schedule.getPtslSegment().getStartDate().toDate())+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getStartDate().toDate())+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getEndDate().toDate())+"\n\n\n";
		String l045="#========================================================================\n"+
		"#\n"+
		"# Name: "+schedule.getFileName()+"\n"+
		"#\n"+ 
		"# Description: "+schedule.getDescription()+"\n"+
		"#\n"+ 
		"# Author: "+schedule.getCreator()+"\n"+ 
		"#\n"+ 
		"# Type: "+schedule.getType()+"\n"+ 
		"#\n"+ 
		"# Date: "+DateUtil.dateToLiteral(new Date())+"\n"+
		"#======================================================================== \n\n\n";

		String l05="";
		String[] includes=schedule.getItlIncludes();
		for (int i=0;i<includes.length;i++){
			l05=l05+"Include: \""+includes[i]+"\"\n";
		}
		for (int i=0;i<stps.length;i++){
			String prefix="M"+String.format("%03d", mtp)+"_S"+String.format("%03d", stps[i].getNumber());
			l05=l05+"Include: \"STP"+String.format("%03d", stps[i].getNumber())+"/TLIS_PL_"+prefix+"_"+file+".itl\""+"\n";

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;


	}
	public static String schToTLISITL(String file,Schedule schedule,String dir){
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(schedule.getPtslSegment().getStartDate().toDate())+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getStartDate().toDate())+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getEndDate().toDate())+"\n\n\n";
		String l045="#========================================================================\n"+
		"#\n"+
		"# Name: "+schedule.getFileName()+"\n"+
		"#\n"+ 
		"# Description: "+schedule.getDescription()+"\n"+
		"#\n"+ 
		"# Author: "+schedule.getCreator()+"\n"+ 
		"#\n"+ 
		"# Type: "+schedule.getType()+"\n"+ 
		"#\n"+ 
		"# Date: "+DateUtil.dateToLiteral(new Date())+"\n"+
		"#======================================================================== \n\n\n";

		String l05="";
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			l05=l05+"Include: \""+dir+"/ITLS_"+acro+"_"+file+".itl\""+"\n";

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;


	}
	public static String schToTLISITL(String file,Schedule schedule){
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(schedule.getPtslSegment().getStartDate().toDate())+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getStartDate().toDate())+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(schedule.getPtslSegment().getEndDate().toDate())+"\n\n\n";
		String l045="#========================================================================\n"+
		"#\n"+
		"# Name: "+schedule.getFileName()+"\n"+
		"#\n"+ 
		"# Description: "+schedule.getDescription()+"\n"+
		"#\n"+ 
		"# Author: "+schedule.getCreator()+"\n"+ 
		"#\n"+ 
		"# Type: "+schedule.getType()+"\n"+ 
		"#\n"+ 
		"# Date: "+DateUtil.dateToLiteral(new Date())+"\n"+
		"#======================================================================== \n\n\n";

		String l05="";
		List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = ins.iterator();
		while(it.hasNext()){
			String instrument = it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
			l05=l05+"Include: \"ITLS_"+acro+"_"+file+".itl\""+"\n";

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;


	}
	public static String OBStoITL(Observation obs){
		Mib mib;
		try {
			mib=Mib.getMib();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
		
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		Por POR = obs.getCommanding();
		String result="";
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[1])+"\n\n\n";
		String l045="#========================================================================\n"+
		"#\n"+
		"# Name: "+obs.getName()+"\n"+
		"#\n"+ 
		"# Description: "+obs.getDescription()+"\n"+
		"#\n"+ 
		"# Author: "+obs.getCreator()+"\n"+ 
		"#\n"+ 
		"# Instrument: "+obs.getInstrument()+"\n"+ 
		"#\n"+ 
		"# Type: "+obs.getType()+"\n"+ 
		"#\n"+ 
		"# Date: "+DateUtil.dateToLiteral(new Date())+"\n"+
		"#======================================================================== \n\n\n";
		String l05="";
		AbstractSequence[] tempSeq=POR.getSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			if (InterpreterUtil.isInstance(ObservationSequence.class, tempSeq[i])){
				String eventName=((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName();
				Integer count = counter.get(eventName);
				if (count==null) count=0;
				else count=count+1;
				counter.put(eventName, count);
				String itlEvent=obs.getName()+"_"+((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName();
				itlEvent=itlEvent.replace(" ", "_");
				l05=l05+itlEvent+" "+"(COUNT = 000001 ) "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[i]).getExecutionTimeDelta())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n";
			}else{
				l05=l05+DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n";
				LOG.info("WARNING:"+DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+" "+tempSeq[i].getName()+" is a literal sequence");
			}
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+"\n";
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
			if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
			if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

			l05=l05+"\t\t\t\t)\n";

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;
	}

	
	

}
