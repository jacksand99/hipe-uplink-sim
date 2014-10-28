package vega.uplink.planning;

import herschel.share.fltdyn.time.FineTime;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
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

import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Parameter;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.planning.gui.ScheduleModel;
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

public class ObservationUtil {
	private static final Logger LOG = Logger.getLogger(ObservationUtil.class.getName());
	
	public static void saveObservation(Observation obs) throws IOException{
		saveObservationToFile(obs.getFileName(),obs);
	}
	
	public static Schedule readScheduleFromFile(String file) throws IOException{
		//Observation result=new Observation(new Date(),new Date());
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			Schedule result = readScheduleFromDoc(doc);
			result.setFileName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());

			//result.setName(newName);
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
		//Schedule result=new Schedule();
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
			Date startDate=PointingBlock.zuluToDate(el.getElementsByTagName("startDate").item(0).getTextContent());
			Date endDate=PointingBlock.zuluToDate(el.getElementsByTagName("endDate").item(0).getTextContent());
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

			return result;
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
		
	}
	public static Observation readObservationFromDoc(Document doc) throws IOException{
		//Observation result=new Observation(new Date(),new Date());

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
		//Observation result=new Observation(new Date(),new Date());
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			Observation result = readObservationFromDoc(doc);
			result.setFileName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());

			//result.setName(newName);
			return result;
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
	}
	
	protected static OffsetAngles readOffsetPointing(Observation obs,PointingElement pe){
		//System.out.println(pe.toXml(0));
		if (pe.getChild("startTime")!=null){
			String sStartTime = pe.getChild("startTime").getValue();
			//System.out.println("********"+sStartTime);
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
			//System.out.println("********"+startEventName);
			//System.out.println("********"+startOffSet);

			pe.getChild("startTime").setValue(PointingBlock.dateToZulu(new Date()));
			try{
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM)){
				//System.out.println("It is custom");
				//try{
					result=new ObservationOffsetCustom(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetCustom(pe));
			}
			/*if (pe.getAttribute("ref").equals(OffsetAngles.OFFSETANGLES_TYPE_FIXED)){
				result=new ObservationOffsetFixed(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetFixed(pe));
			}*/
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_RASTER)){
				//System.out.println("It is raster");
				result=new ObservationOffsetRaster(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new OffsetRaster(pe));
			}
			if (pe.getAttribute("ref").getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_SCAN)){
				//System.out.println("It is scan");
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
	
	//private pointingElement
	private static PointingBlock translatePointingBlock(PointingElement pe,Observation obs){
		PointingBlock result;
		pe.getChild("startTime").setValue(PointingBlock.dateToZulu(new Date()));
		pe.getChild("endTime").setValue(PointingBlock.dateToZulu(new Date()));
		PointingElement at = pe.getChild(PointingAttitude.ATTITUDE_TAG);
		PointingElement os = at.getChild(OffsetAngles.OFFSETANGLES_TAG);
		if (os!=null){
			OffsetAngles ofAngles=readOffsetPointing(obs,os);
			//System.out.println(ofAngles.toXml(0));
			//OffsetRefAxis axis=new OffsetRefAxis(at.getChild(OffsetRefAxis.OFFSETREFAXIS_TAG));
			at.remove(OffsetAngles.OFFSETANGLES_TAG);
			//at.remove(OffsetRefAxis.OFFSETREFAXIS_TAG);
			PointingAttitude attitude=new PointingAttitude(at);
			//attitude.SetOffset(axis, ofAngles);
			attitude.addChild(ofAngles);
			result=new PointingBlock(pe);
			result.setAttitude(attitude);
		}else{
			PointingAttitude attitude=new PointingAttitude(at);
			
			result=new PointingBlock(pe);
			result.setAttitude(attitude);
		}
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
				/*pe.getChild("startTime").setValue(PointingBlock.dateToZulu(new Date()));
				pe.getChild("endTime").setValue(PointingBlock.dateToZulu(new Date()));
				PointingBlock pb = new PointingBlock(pe);*/
				PointingBlock pb =translatePointingBlock(pe,obs);
				if (nNode.hasChildNodes()){
					NodeList children = nNode.getChildNodes();
					int size=children.getLength();
					for (int i=0;i<size;i++){
						PointingElement child=null;
						if (children.item(i).getNodeName().equals(PointingMetadata.METADATA_TAG)){
							child = PointingMetadata.readFrom(children.item(i));
						}
						//else child = PointingElement.readFrom(children.item(i));
						//System.out.println(child);
						if (child!=null) pb.addChild(child);
					}			
				}
				try{
					result[temp]=new ObservationPointingBlock(obs,new ObservationEvent(startEventName),ObservationUtil.getOffsetMilliSeconds(startOffSet),new ObservationEvent(endEventName),ObservationUtil.getOffsetMilliSeconds(endOffSet),pb);
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
				//System.out.println(obs.getDateForEvent(event));
				result[temp]=new ObservationSequence (obs,event,getOffsetMilliSeconds(offSet),sName,"P"+String.format("%05d", temp),sParameters,sProfiles);
				
			}
		}
		return result;
	}
	public static String scheduleToEVF(Schedule schedule){
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		String result="";
		Por POR = schedule.getPor();
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String l03="Start_time: "+dateFormat2.format(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+dateFormat2.format(POR.getValidityDates()[1])+"\n\n\n";
		Observation[] observations = schedule.getObservations();
		String l05="";
		for (int i=0;i<observations.length;i++){
			Integer count = counter.get(observations[i].getName());
			if (count==null) count=1;
			else count=count+1;
			counter.put(observations[i].getName(), count);
			l05=l05+dateFormat2.format(observations[i].getStartDate().toDate())+" "+observations[i].getName()+"_"+ObservationEvent.START_OBS.getName()+" (COUNT = "+String.format("%06d", count)+" )\n";
			l05=l05+dateFormat2.format(observations[i].getEndDate().toDate())+" "+observations[i].getName()+"_"+ObservationEvent.END_OBS.getName()+" (COUNT = "+String.format("%06d", count)+" )\n";

		}
		result=l03+l04+l05;
		return result;
	}
	
	public static String OBStoEVF(Observation obs){
		String result="";
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String l03="Start_time: "+dateFormat2.format(obs.getStartDate().toDate())+"\n";
		String l04="End_time: "+dateFormat2.format(obs.getEndDate().toDate())+"\n\n\n";
		String l05=dateFormat2.format(obs.getStartDate().toDate())+" "+obs.getName()+"_"+ObservationEvent.START_OBS.getName()+" (COUNT = 000001)\n";
		String l06=dateFormat2.format(obs.getEndDate().toDate())+" "+obs.getName()+"_"+ObservationEvent.END_OBS.getName()+" (COUNT = 000001)\n";
		result=l03+l04+l05+l06;
		return result;
	}
	
	public static String scheduleToITL(Schedule schedule){
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		LOG.info("Getting the POR");
		Por POR = schedule.getPor();
		LOG.info("Finsih getting the POR");
		String result="";
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String l01="Version: 1\n";
		String l02="Ref_date: "+dateFormat.format(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+dateFormat2.format(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+dateFormat2.format(POR.getValidityDates()[1])+"\n\n\n";
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
		"# Date: "+dateFormat2.format(new Date())+"\n"+
		"#======================================================================== \n\n\n";
		Observation[] observations = schedule.getObservations();		
		String l05="";
		for (int i=0;i<observations.length;i++){
			Integer count = counter.get(observations[i].getName());
			if (count==null) count=1;
			else count=count+1;
			counter.put(observations[i].getName(), count);
			AbstractSequence[] tempSeq = observations[i].getCommanding().getSequences();
			Parameter[] tempParam;
			SequenceProfile[] tempPro;
			for (int j=0;j<tempSeq.length;j++){
				l05=l05+((ObservationSequence)tempSeq[j]).getObservationName()+"_"+((ObservationSequence)tempSeq[j]).getExecutionTimeEvent().getName()+" "+"(COUNT = "+String.format("%06d", count)+" ) "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[j]).getExecutionTimeDelta())+" "+ tempSeq[j].getInstrument()+"\t*\t"+tempSeq[j].getName()+" (\\"+"\n";
				tempParam = tempSeq[j].getParameters();
				tempParam = tempSeq[j].getParameters();
				for (int z=0;z<tempParam.length;z++){
					l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ \n";
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
						//l05 =l05+ "\tDATA_RATE_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]\\\n"; 
					}
					if (tempPro[k].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
						powerProfile=powerProfile+" "+tempPro[k].getOffSetString()+"\t"+new Double(tempPro[k].getValue()).toString()+"\t[Watts]";
						powerProfilePresent=true;
						//l05 = l05+"\tPOWER_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]\\\n"; 
					}
					
				}
				if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
				if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

				l05=l05+"\t\t\t\t)\n";

			}
			

		}
		result=l01+l02+l03+l04+l045+l05;
		return result;

	}

	
	public static String OBStoITL(Observation obs){
		TreeMap<String,Integer> counter=new TreeMap<String,Integer>();
		Por POR = obs.getCommanding();
		String result="";
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String l01="Version: 1\n";
		String l02="Ref_date: "+dateFormat.format(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+dateFormat2.format(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+dateFormat2.format(POR.getValidityDates()[1])+"\n\n\n";
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
		"# Date: "+dateFormat2.format(new Date())+"\n"+
		"#======================================================================== \n\n\n";
		String l05="";
		AbstractSequence[] tempSeq=POR.getSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			String eventName=((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName();
			Integer count = counter.get(eventName);
			if (count==null) count=0;
			else count=count+1;
			counter.put(eventName, count);
			//l05=l05+obs.getName()+"_"+((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName()+" "+"(COUNT = "+String.format("%06d", count)+" ) "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[i]).getExecutionTimeDelta())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\"+"\n";
			l05=l05+obs.getName()+"_"+((ObservationSequence)tempSeq[i]).getExecutionTimeEvent().getName()+" "+"(COUNT = 000001 ) "+ObservationUtil.getOffset(((ObservationSequence)tempSeq[i]).getExecutionTimeDelta())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\"+"\n";
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ \n";
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
					//l05 =l05+ "\tDATA_RATE_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]\\\n"; 
				}
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
					powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
					powerProfilePresent=true;
					//l05 = l05+"\tPOWER_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]\\\n"; 
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
