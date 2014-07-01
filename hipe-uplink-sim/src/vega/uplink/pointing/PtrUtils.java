package vega.uplink.pointing;

import java.io.File;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;




//import rosetta.uplink.Evtm;
//import rosetta.uplink.EvtmEvent;



import vega.uplink.pointing.EvtmEvents.EvtmEventAnt;
import vega.uplink.pointing.EvtmEvents.EvtmEventAos;
import vega.uplink.pointing.EvtmEvents.EvtmEventBdi;
import vega.uplink.pointing.EvtmEvents.EvtmEventCon;
import vega.uplink.pointing.EvtmEvents.EvtmEventLos;
import vega.uplink.pointing.EvtmEvents.EvtmEventOrb;
import vega.uplink.pointing.EvtmEvents.EvtmEventVis;
import vega.uplink.pointing.PtrParameters.*;
import vega.uplink.pointing.PtrParameters.Offset.*;
import vega.uplink.pointing.attitudes.*;


public class PtrUtils {

	private static PointingMetadata recursiveTranslation(PointingMetadata item){
		if (!item.hasChildren()) return translateChild(item);
		PointingMetadata[] children=item.getChildren();
		for (int i=0;i<children.length;i++){
			 children[i]=recursiveTranslation(children[i]);
			 item.addChild(children[i]);
			 //System.out.println(children[i].getClass());
		}
		item=translateChild(item);
		
		return item;
	}
	private static PointingMetadata translateChild(PointingMetadata item){
		if (item.getName().equals("block")) return new PointingBlock(item);
		if (item.getName().equals("attitude")){
			//System.out.println("attitude detected");
			if (item.getAttribute("ref").getValue().startsWith("illuminatedPoint")) return new IlluminatedPoint(item);
			if (item.getAttribute("ref").getValue().startsWith("inertial")) return new Inertial(item);
			if (item.getAttribute("ref").getValue().startsWith("limb")) return new Limb(item);
			if (item.getAttribute("ref").getValue().startsWith("specular")) return new Specular(item);
			if (item.getAttribute("ref").getValue().startsWith("terminator")) return new Terminator(item);
			if (item.getAttribute("ref").getValue().startsWith("track")) return new Track(item);
			if (item.getAttribute("ref").getValue().startsWith("velocity")) return new Velocity(item);
			//System.out.println("NOt detected which type of attitude "+item.getAttribute("ref").getValue());
			return new PointingAttitude(item);
		}
		if (item.getName().equals("boresight")) return new Boresight(item);
		if (item.getName().equals("height")) return new Height(item);
		if (item.getName().equals("phaseAngle")) return new PhaseAngle(item);
		if (item.getName().equals("surface")) return new Surface(item);
		if (item.getName().equals("target")) return new Target(item);
		if (item.getName().equals("targetDir")) return new TargetDir(item);
		if (item.getName().equals("offsetAngles")){
			if (item.getAttribute("ref").getValue().equals("custom")) return new OffsetCustom(item);
			if (item.getAttribute("ref").getValue().equals("fixed")) return new OffsetFixed(item);
			if (item.getAttribute("ref").getValue().equals("raster")) return new OffsetRaster(item);
			if (item.getAttribute("ref").getValue().equals("scan")) return new OffsetScan(item);
			return new OffsetAngles(item);
		}
		if (item.getName().equals("offsetRefAxis")) return new OffsetRefAxis(item);

		
		return item;
	}
	
	public static Ptr readPTRfromFile(String file){

		Ptr result = new Ptr();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("body");
			Node nBody =nListHeader.item(0);
			Element elBody = (Element) nBody;
			NodeList nlSegments=elBody.getElementsByTagName("segment");
			for (int i=0;i<nlSegments.getLength();i++){
				Node nSegment=nlSegments.item(i);
				String pName=((Element) nSegment).getAttribute("name");
				//System.out.println("name:"+pName);
				PtrSegment segment=new PtrSegment(pName);
				Node nMetadata=((Element) nSegment).getElementsByTagName("metadata").item(0);
				NodeList nlIncludes=((Element) nMetadata).getElementsByTagName("include");
				for (int j=0;j<nlIncludes.getLength();j++){
					segment.addInclude(((Element)nlIncludes.item(j)).getAttribute("href"));
				}
				Node nData=((Element) nSegment).getElementsByTagName("data").item(0);
				Node nTimeline=((Element) nData).getElementsByTagName("timeline").item(0);
				String tlframe=((Element) nTimeline).getAttribute("frame");
				segment.setTimeLineFrame(tlframe);
				NodeList nlBlocks=((Element) nTimeline).getElementsByTagName("block");
				//PointingBlock oldBlock=null;
				for (int j=0;j<nlBlocks.getLength();j++){
					//String type=
					PointingBlock block=PointingBlock.readFrom(nlBlocks.item(j));
					if (block.hasChildren()){
						PointingMetadata[] children=block.getChildren();
						for (int h=0;h<children.length;h++){
							 children[h]=recursiveTranslation(children[h]);
							 block.addChild(children[h]);
						}
					}
					//block=recursiveTranslation(block);
					if (block.getType().equals("SLEW")) block=new PointingBlockSlew();
					/*
					String type=((Element)nlBlocks.item(j)).getAttribute("ref");
					java.util.Date sDate=new java.util.Date();
					java.util.Date eDate=new java.util.Date();
					NodeList nlsd=((Element)nlBlocks.item(j)).getElementsByTagName("startTime");
					if (nlsd.getLength()>0) sDate=PointingBlock.zuluToDate(nlsd.item(0).getTextContent());
					NodeList nled=((Element)nlBlocks.item(j)).getElementsByTagName("endTime");
					if (nled.getLength()>0) eDate=PointingBlock.zuluToDate(nled.item(0).getTextContent());
					PointingBlock block;
					if (type.equals("SLEW")) block=new PointingBlockSlew();
					else block=new PointingBlock(type,sDate,eDate);
					if (nlBlocks.item(j).hasChildNodes()){
						NodeList nlBLocksChMd = nlBlocks.item(j).getChildNodes();
						int ssi = nlBLocksChMd.getLength();
						for (int h=0;h<ssi;h++){
							block.addChild(PointingMetadata.readFrom(nlBLocksChMd.item(h)));
						}
						//block.addChild(PointingMetadata.readFrom(nlBlocks.item(j)));
					}*/
					segment.addBlock(block);
				}
				result.addSegment(segment);
			}
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		
		return result; //to be remove
	}
	
	public static void writePTRtofile(String file,Ptr ptr){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(ptr.toXml());
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static void writePDFMtofile(String file,Pdfm pdfm){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(pdfm.toXml());
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	public static Evtm readEvtmFromFile(String file){
		Evtm result=new Evtm();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("header");
			NamedNodeMap headerAttributes = nListHeader.item(0).getAttributes();
			result.setSpacecraft(headerAttributes.getNamedItem("spacecraft").getNodeValue());
			result.setIcdVersion(headerAttributes.getNamedItem("icd_version").getNodeValue());
			result.setGenerationTime(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("gen_time").getNodeValue()));
			result.setValidityStart(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_start").getNodeValue()));
			result.setValidityEnd(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_end").getNodeValue()));
			NodeList nListEvents = doc.getElementsByTagName("events");
			NodeList eventNodes = nListEvents.item(0).getChildNodes();
			//System.out.println(eventNodes.item(1));
			for (int i=0;i<eventNodes.getLength();i++){
				if(eventNodes.item(i).hasAttributes()){
					String eventType=eventNodes.item(i).getNodeName();
					NamedNodeMap eventAttributes = eventNodes.item(i).getAttributes();
					String eventId=eventAttributes.getNamedItem("id").getTextContent();
					java.util.Date eventTime=EvtmEvent.zuluToDate(eventAttributes.getNamedItem("time").getNodeValue());
					long eventDuration=Long.parseLong(eventAttributes.getNamedItem("duration").getNodeValue().replace(" ", ""));
					boolean created=false;
					if (eventType.equals(EvtmEvent.EVENT_TYPE_BDI)){
						float eventSundistance=Float.parseFloat(eventAttributes.getNamedItem("sundistance").getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventBdi(eventId,eventTime,eventDuration,eventSundistance));
						created=true;
					}
					if (eventType.equals(EvtmEvent.EVENT_TYPE_CON)){
						int eventAngle=Integer.parseInt(eventAttributes.getNamedItem("angle").getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventCon(eventId,eventTime,eventDuration,eventAngle));
						created=true;
					}

					if (eventType.equals(EvtmEvent.EVENT_TYPE_ORB)){
						float eventDistance=Float.parseFloat(eventAttributes.getNamedItem("distance").getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventOrb(eventId,eventTime,eventDuration,eventDistance));
						created=true;
					}
					if (eventType.equals(EvtmEvent.EVENT_TYPE_VIS)){
						result.addEvent(new EvtmEventVis(eventId,eventTime,eventDuration));
						created=true;
					}

					if (eventType.equals(EvtmEvent.EVENT_TYPE_ANT)){
						Node item = eventAttributes.getNamedItem("ems:station");
						if (item!=null){
							String eventStation=eventAttributes.getNamedItem("ems:station").getNodeValue();
							result.addEvent(new EvtmEventAnt(eventId,eventTime,eventDuration,eventStation));
							created=true;
						}else{
							result.addEvent(new EvtmEventAnt(eventId,eventTime,eventDuration,null));
							created=true;
						}
					}
					
					if (eventType.equals(EvtmEvent.EVENT_TYPE_AOS) || eventType.equals(EvtmEvent.EVENT_TYPE_LOS)){
						String eventStation=eventAttributes.getNamedItem("ems:station").getNodeValue();
						String eventcriteria=eventAttributes.getNamedItem("criteria").getNodeValue();
						int eventElevation=Integer.parseInt(eventAttributes.getNamedItem("elevation").getNodeValue());
						long eventRtlt=Long.parseLong(eventAttributes.getNamedItem("ems:rtlt").getNodeValue());
						if (eventType.equals(EvtmEvent.EVENT_TYPE_AOS)){
							result.addEvent(new EvtmEventAos(eventId,eventTime,eventDuration,eventStation,eventcriteria,eventElevation,eventRtlt));
							created=true;
						}
						if (eventType.equals(EvtmEvent.EVENT_TYPE_LOS)){
							result.addEvent(new EvtmEventLos(eventId,eventTime,eventDuration,eventStation,eventcriteria,eventElevation,eventRtlt));
							created=true;
						}
						
						
					}

					if (!created) result.addEvent(new EvtmEvent(eventType,eventId,eventTime,eventDuration));
				}
			}
			
			
		}catch (Exception e){
			e.printStackTrace();
		}

		return result;
	}
	
	public static Pdfm readPdfmfromFile(String file){

		Pdfm result = new Pdfm();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("definition");
			Node nBody =nListHeader.item(0);
			Element elBody = (Element) nBody;
			NodeList nlSegments=elBody.getElementsByTagName("dirVector");
			for (int i=0;i<nlSegments.getLength();i++){
				Node nSegment=nlSegments.item(i);
				String pName=((Element) nSegment).getAttribute("name");
				PdfmDirVector bore=PdfmDirVector.readFrom(nSegment);
				result.addChild(bore);
			}
			NodeList nlSurface=elBody.getElementsByTagName("surface");
			for (int i=0;i<nlSurface.getLength();i++){
				Node nSegment=nlSurface.item(i);
				String pName=((Element) nSegment).getAttribute("name");
				PdfmSurface bore=PdfmSurface.readFrom(nSegment);
				result.addChild(bore);
			}

			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		
		return result; //to be remove
	}

}
