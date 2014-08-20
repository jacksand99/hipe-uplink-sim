package vega.uplink.pointing;

import java.io.File;
import java.io.PrintWriter;

//import javafx.scene.transform.Rotate;












import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
		if (item.getName().equals(PointingBlock.BLOCK_TAG)) return new PointingBlock(item);
		if (item.getName().equals(PointingAttitude.ATTITUDE_TAG)){
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_ILLUMINATEDPOINT)) return new IlluminatedPoint(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_INERTIAL)) return new Inertial(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_LIMB)) return new Limb(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_SPECULAR)) return new Specular(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_TERMINATOR)) return new Terminator(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_TRACK)) return new Track(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_VELOCITY)) return new Velocity(item);
			if (item.getAttribute(PointingAttitude.REF_TAG).getValue().startsWith(PointingAttitude.POINTING_ATTITUDE_TYPE_CAPTURE)) return new Capture(item);
			return new PointingAttitude(item);
		}
		if (item.getName().equals(Boresight.BORESIGHT_TAG)) return new Boresight(item);
		if (item.getName().equals(Height.HEIGHT_TAG)) return new Height(item);
		if (item.getName().equals(PhaseAngle.PHASEANGLE_TAG)) return new PhaseAngle(item);
		if (item.getName().equals(Surface.SURFACE_TAG)) return new Surface(item);
		if (item.getName().equals(TargetTrack.TARGET_TAG)){
			if (item.getChild(TargetTrack.POSITION_FIELD)!=null) return new TargetTrack(item);
			else{
				return new TargetInert(item);
			}
		}
		if (item.getName().equals(TargetDir.TARGETDIR_TAG)){
			return new TargetDir(item);
		}
		if (item.getName().equals(OffsetAngles.OFFSETANGLES_TAG)){
			if (item.getAttribute(OffsetAngles.REF_TAG).getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_CUSTOM)) return new OffsetCustom(item);
			if (item.getAttribute(OffsetAngles.REF_TAG).getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_FIXED)) return new OffsetFixed(item);
			if (item.getAttribute(OffsetAngles.REF_TAG).getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_RASTER)) return new OffsetRaster(item);
			if (item.getAttribute(OffsetAngles.REF_TAG).getValue().equals(OffsetAngles.OFFSETANGLES_TYPE_SCAN)) return new OffsetScan(item);
		}
		if (item.getName().equals(OffsetRefAxis.OFFSETREFAXIS_TAG)) return new OffsetRefAxis(item);

		
		return item;
	}
	
	public static Ptr readPTRfromFile(String file){

		Ptr result = new Ptr();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListHeader = doc.getElementsByTagName(Ptr.BODY_TAG);
			Node nBody =nListHeader.item(0);
			Element elBody = (Element) nBody;
			NodeList nlSegments=elBody.getElementsByTagName(PtrSegment.SEGMENT_TAG);
			for (int i=0;i<nlSegments.getLength();i++){
				Node nSegment=nlSegments.item(i);
				String pName=((Element) nSegment).getAttribute(PtrSegment.NAME_TAG);
				PtrSegment segment=new PtrSegment(pName);
				Node nMetadata=((Element) nSegment).getElementsByTagName(PtrSegment.METADATA_TAG).item(0);
				NodeList nlIncludes=((Element) nMetadata).getElementsByTagName(PtrSegment.INCLUDE_TAG);
				for (int j=0;j<nlIncludes.getLength();j++){
					segment.addInclude(((Element)nlIncludes.item(j)).getAttribute(PtrSegment.HREF_TAG));
				}
				Node nData=((Element) nSegment).getElementsByTagName(PtrSegment.DATA_TAG).item(0);
				Node nTimeline=((Element) nData).getElementsByTagName(PtrSegment.TIMELINE_TAG).item(0);
				String tlframe=((Element) nTimeline).getAttribute(PtrSegment.FRAME_TAG);
				segment.setTimeLineFrame(tlframe);
				NodeList nlBlocks=((Element) nTimeline).getElementsByTagName(PointingBlock.BLOCK_TAG);
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
					if (block.getType().equals(PointingBlock.TYPE_SLEW)) block=new PointingBlockSlew();
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
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListHeader = doc.getElementsByTagName(Evtm.HEADER_TAG);
			NamedNodeMap headerAttributes = nListHeader.item(0).getAttributes();
			result.setSpacecraft(headerAttributes.getNamedItem(Evtm.SPACECRAFT_TAG).getNodeValue());
			result.setIcdVersion(headerAttributes.getNamedItem(Evtm.ICDVERION_TAG).getNodeValue());
			result.setGenerationTime(EvtmEvent.zuluToDate(headerAttributes.getNamedItem(Evtm.GENTIME_TAG).getNodeValue()));
			result.setValidityStart(EvtmEvent.zuluToDate(headerAttributes.getNamedItem(Evtm.VALIDITY_START_TAG).getNodeValue()));
			result.setValidityEnd(EvtmEvent.zuluToDate(headerAttributes.getNamedItem(Evtm.VALIDITY_END_TAG).getNodeValue()));
			NodeList nListEvents = doc.getElementsByTagName(Evtm.EVENTS_TAG);
			NodeList eventNodes = nListEvents.item(0).getChildNodes();
			for (int i=0;i<eventNodes.getLength();i++){
				if(eventNodes.item(i).hasAttributes()){
					String eventType=eventNodes.item(i).getNodeName();
					NamedNodeMap eventAttributes = eventNodes.item(i).getAttributes();
					String eventId=eventAttributes.getNamedItem(EvtmEvent.ID_TAG).getTextContent();
					java.util.Date eventTime=EvtmEvent.zuluToDate(eventAttributes.getNamedItem(EvtmEvent.TIME_TAG).getNodeValue());
					long eventDuration=Long.parseLong(eventAttributes.getNamedItem(EvtmEvent.DURATION_TAG).getNodeValue().replace(" ", ""));
					boolean created=false;
					if (eventType.equals(EvtmEvent.EVENT_TYPE_BDI)){
						float eventSundistance=Float.parseFloat(eventAttributes.getNamedItem(EvtmEventBdi.SUNDISTANCE_TAG).getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventBdi(eventId,eventTime,eventDuration,eventSundistance));
						created=true;
					}
					if (eventType.equals(EvtmEvent.EVENT_TYPE_CON)){
						int eventAngle=Integer.parseInt(eventAttributes.getNamedItem(EvtmEventCon.ANGLE_TAG).getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventCon(eventId,eventTime,eventDuration,eventAngle));
						created=true;
					}

					if (eventType.equals(EvtmEvent.EVENT_TYPE_ORB)){
						float eventDistance=Float.parseFloat(eventAttributes.getNamedItem(EvtmEventOrb.DISTANCE_TAG).getNodeValue().replace(" ", ""));
						result.addEvent(new EvtmEventOrb(eventId,eventTime,eventDuration,eventDistance));
						created=true;
					}
					if (eventType.equals(EvtmEvent.EVENT_TYPE_VIS)){
						result.addEvent(new EvtmEventVis(eventId,eventTime,eventDuration));
						created=true;
					}

					if (eventType.equals(EvtmEvent.EVENT_TYPE_ANT)){
						Node item = eventAttributes.getNamedItem(EvtmEventAnt.EMS_STATION_TAG);
						if (item!=null){
							String eventStation=eventAttributes.getNamedItem(EvtmEventAnt.EMS_STATION_TAG).getNodeValue();
							result.addEvent(new EvtmEventAnt(eventId,eventTime,eventDuration,eventStation));
							created=true;
						}else{
							result.addEvent(new EvtmEventAnt(eventId,eventTime,eventDuration,null));
							created=true;
						}
					}
					
					if (eventType.equals(EvtmEvent.EVENT_TYPE_AOS) || eventType.equals(EvtmEvent.EVENT_TYPE_LOS)){
						String eventStation=eventAttributes.getNamedItem(EvtmEventAos.EMS_STATION_TAG).getNodeValue();
						String eventcriteria=eventAttributes.getNamedItem(EvtmEventAos.CRITERIA_TAG).getNodeValue();
						int eventElevation=Integer.parseInt(eventAttributes.getNamedItem(EvtmEventAos.ELEVATION_TAG).getNodeValue());
						long eventRtlt=Long.parseLong(eventAttributes.getNamedItem(EvtmEventAos.EMS_RTLT_TAG).getNodeValue());
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
				result.addDefinition(bore);
			}
			NodeList nlSurface=elBody.getElementsByTagName("surface");
			for (int i=0;i<nlSurface.getLength();i++){
				Node nSegment=nlSurface.item(i);
				String pName=((Element) nSegment).getAttribute("name");
				PdfmSurface bore=PdfmSurface.readFrom(nSegment);
				result.addDefinition(bore);
			}

			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		
		return result; //to be remove
	}

}
