package vega.uplink.pointing;

import java.io.File;
import java.io.PrintWriter;

//import javafx.scene.transform.Rotate;














import java.text.ParseException;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.DateUtil;
import vega.uplink.planning.gui.ScheduleModel;
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
	private static final Logger LOG = Logger.getLogger(PtrUtils.class.getName());

	private static PointingElement recursiveTranslation(PointingElement item){
		if (!item.hasChildren()) return translateChild(item);
		PointingElement[] children=item.getChildren();
		for (int i=0;i<children.length;i++){
			 children[i]=recursiveTranslation(children[i]);
			 item.addChild(children[i]);
			 //System.out.println(children[i].getClass());
		}
		item=translateChild(item);
		
		return item;
	}
	private static PointingElement translateChild(PointingElement item){
		if (item.getName().equals(PointingBlock.STARTTIME_TAG) || item.getName().equals(PointingBlock.ENDTIME_TAG)) return new PrmTime(item);

		//if (item.getName().equals(PointingMetadata.))
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
	public static PointingBlocksSlice readBlocksfromDoc(Document doc){
		PointingBlocksSlice result = new PointingBlocksSlice();
		  try{
			doc.getDocumentElement().normalize();
			 
				NodeList nlBlocks=doc.getElementsByTagName(PointingBlock.BLOCK_TAG);
				for (int j=0;j<nlBlocks.getLength();j++){
					//String type=
					PointingBlock block=PointingBlock.readFrom(nlBlocks.item(j));
					if (block.hasChildren()){
						PointingElement[] children=block.getChildren();
						for (int h=0;h<children.length;h++){
							 children[h]=recursiveTranslation(children[h]);
							 block.addChild(children[h]);
						}
					}
					if (block.getType().equals(PointingBlock.TYPE_SLEW)) block=new PointingBlockSlew();
					result.addBlock(block);
				}
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	throw(e);
		    }
		
		return result; //to be remove

		}

	
	public static Ptr readPTRfromDoc(Document doc){
	  Ptr result = new Ptr();
	  try{
		doc.getDocumentElement().normalize();
		 
		NodeList nListHeader = doc.getElementsByTagName(Ptr.BODY_TAG);
		Node nBody =nListHeader.item(0);
		Element elBody = (Element) nBody;
		NodeList nlSegments=elBody.getElementsByTagName(PtrSegment.SEGMENT_TAG);
		for (int i=0;i<nlSegments.getLength();i++){
			Node nSegment=nlSegments.item(i);
			/*String pName=((Element) nSegment).getAttribute(PtrSegment.NAME_TAG).trim();
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
					PointingElement[] children=block.getChildren();
					for (int h=0;h<children.length;h++){
						 children[h]=recursiveTranslation(children[h]);
						 block.addChild(children[h]);
					}
				}
				if (block.getType().equals(PointingBlock.TYPE_SLEW)) block=new PointingBlockSlew();
				segment.addBlock(block);
			}*/
			PtrSegment segment = getPtrSegmentFromNode(nSegment);
			result.addSegment(segment);
		}
		
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw(e);
	    }
	
	return result; //to be remove

	}
	public static PtrSegment getPtrSegmentFromDoc(Document doc){
		  try{
				doc.getDocumentElement().normalize();
				 
				NodeList nListHeader = doc.getElementsByTagName(PtrSegment.SEGMENT_TAG);
				Node nBody =nListHeader.item(0);
				return getPtrSegmentFromNode(nBody);
		  }catch (Exception e) {
		    	e.printStackTrace();
		    	throw(e);
		   }
	}
	public static PtrSegment getPtrSegmentFromNode(Node nSegment){
		String pName=((Element) nSegment).getAttribute(PtrSegment.NAME_TAG).trim();
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
				PointingElement[] children=block.getChildren();
				for (int h=0;h<children.length;h++){
					 children[h]=recursiveTranslation(children[h]);
					 block.addChild(children[h]);
				}
			}
			if (block.getType().equals(PointingBlock.TYPE_SLEW)) block=new PointingBlockSlew();
			segment.addBlock(block);
		}
		return segment;
	}
	public static Ptr readPTRfromFile(String file) throws Exception{

		Ptr result = new Ptr();
		try {
			 
			File fXmlFile = new File(file);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
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
				String pName=((Element) nSegment).getAttribute(PtrSegment.NAME_TAG).trim();
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
				java.util.Vector<PointingBlock> blocks=new java.util.Vector<PointingBlock>();
				for (int j=0;j<nlBlocks.getLength();j++){
					//String type=
					PointingBlock block=PointingBlock.readFrom(nlBlocks.item(j));
					if (block.hasChildren()){
						PointingElement[] children=block.getChildren();
						for (int h=0;h<children.length;h++){
							 children[h]=recursiveTranslation(children[h]);
							 block.addChild(children[h]);
						}
					}
					if (block.getType().equals(PointingBlock.TYPE_SLEW)) block=new PointingBlockSlew();
					//segment.addBlock(block);
					blocks.add(block);
				}
				PointingBlock[] arrayBlocks=new PointingBlock[blocks.size()];
				arrayBlocks=blocks.toArray(arrayBlocks);
				segment.hardInsertBlocks(arrayBlocks);
				result.addSegment(segment);
			}
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	throw(e);
		    }
		
		return result; //to be remove
	}
	
	public static void savePTR(Ptr ptr){
		writePTRtofile(ptr.getPath()+"/"+ptr.getName(),ptr);
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
	public static void savePDFM(Pdfm pdfm){
		writePDFMtofile(pdfm.getPath()+"/"+pdfm.getName(),pdfm);
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
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());

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
					String eventId=eventAttributes.getNamedItem(EvtmEvent.ID_TAG).getTextContent().trim();
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
	
	public static Pdfm readPdfmfromNode(Node nBody) throws Exception{
		Pdfm result = new Pdfm();
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
		return result;
	}
	public static Pdfm readPdfmfromDoc(Document doc) throws Exception{
		Pdfm result;
		//Pdfm result = new Pdfm();
		//try {
			 

		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("definition");
			Node nBody =nListHeader.item(0);
			result=readPdfmfromNode(nBody);
			/*Element elBody = (Element) nBody;
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
			}*/

			
		    /*} catch (Exception e) {
		    	e.printStackTrace();
		    }*/
		return result;
	}
	public static Pdfm readPdfmfromFile(String file) throws Exception{
		Pdfm result;
		//Pdfm result = new Pdfm();
		//try {
			 
			File fXmlFile = new File(file);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
			result=readPdfmfromDoc(doc);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());

			/*NodeList nListHeader = doc.getElementsByTagName("definition");
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
			}*/

			
		    /*} catch (Exception e) {
		    	e.printStackTrace();
		    }*/
		
		return result; 
	}
	
	public static void mergePtrs(Ptr master, Ptr ptr, Ptr target){
		PtrSegment ptr1Segment = master.getSegments()[0];
		PtrSegment ptr2Segment = ptr.getSegment(ptr1Segment.getName());
		PtrSegment targetSegment = target.getSegment(ptr1Segment.getName());

		PointingBlock[] ptr2Blocks = ptr2Segment.getBlocks();
		for (int i=0;i<ptr2Blocks.length;i++){
			PointingBlock ptr2Block = ptr2Blocks[i];
			if (!ptr2Block.getType().equals(PointingBlock.TYPE_SLEW)){
				PointingBlock ptrBlock = ptr1Segment.getBlockAt(ptr2Block.getStartTime());
				PointingBlock targetBlock = targetSegment.getBlockAt(ptr2Block.getStartTime());

					if (!ptr2Block.equals(ptrBlock)){
						PointingBlock[] blocksToRemove = targetSegment.getBlocksAt(ptr2Block.getStartTime(), ptr2Block.getEndTime()).getBlocks();
						targetSegment.removeBlocks(blocksToRemove);
						targetSegment.addBlock(ptr2Block);
						repairGaps(targetSegment);

					}
			}else{
				PointingBlock[] blocksInSlew = ptr1Segment.getBlocksAt(ptr2Block.getStartTime(),ptr2Block.getEndTime()).getBlocks();
				targetSegment.removeBlocks(blocksInSlew);
				repairGaps(targetSegment);
				/*for (int j=0;j<blocksInSlew.length;j++){
					PointingBlock targetBlock = targetSegment.getBlockAt(blocksInSlew[j].getStartTime());
					if (!targetBlock.getType().equals("SLEW")) 
				}*/
			}
		}
		repairGaps(targetSegment);
		
	}
	public static void repairOrphanSlews(PtrSegment segment){
		PointingBlock[] blocks = segment.getBlocks();
		
		for (int j=1;j<blocks.length;j++){
			PointingBlock blockBefore = blocks[j-1];
			if (blockBefore.getType().equals("SLEW")) {
				if (!blocks[j].getType().equals("SLEW")){
					((PointingBlockSlew) blockBefore).setBlockAfter(blocks[j]);
				}
				else{
					segment.hardRemoveBlock(blockBefore);
					((PointingBlockSlew)blocks[j]).setBlockBefore(((PointingBlockSlew) blockBefore).getBlockBefore());
					LOG.warning("unificated SLEW at "+DateUtil.defaultDateToString(blocks[j].getStartTime()));
				}
			}
		}
		blocks = segment.getBlocks();
		for (int j=0;j<blocks.length-1;j++){
			PointingBlock blockAfter = blocks[j+1];
			if (blockAfter.getType().equals("SLEW")) {
				if (!blocks[j].getType().equals("SLEW")){
					((PointingBlockSlew) blockAfter).setBlockBefore(blocks[j]);
				}else{
					segment.hardRemoveBlock(blockAfter);
					((PointingBlockSlew)blocks[j]).setBlockAfter(((PointingBlockSlew)blockAfter).getBlockAfter());
					LOG.warning("unificated SLEW at "+DateUtil.defaultDateToString(blocks[j].getStartTime()));
				}
				//((PointingBlockSlew) blockAfter).setBlockBefore(blocks[j]);
			}
		}
		
	}
	
	public static void repairGaps(PtrSegment segment){
		repairOrphanSlews(segment);
			PointingBlock[] blocks = segment.getBlocks();
			
			for (int j=1;j<blocks.length;j++){
				PointingBlock blockBefore = blocks[j-1];
				if (!blockBefore.getEndTime().equals(blocks[j].getStartTime()) && !blockBefore.isSlew() && !blocks[j].isSlew()){
					PointingBlockSlew slew = new PointingBlockSlew();
					slew.setBlockBefore(blockBefore);
					slew.setBlockAfter(blocks[j]);
					segment.hardInsertBlock(slew);

				}

			}
	}
	
	/*public static Ptr rebasePtrPtsl(Ptr ptr,Ptr ptsl){
		PtrSegment ptrSegment = ptr.getSegments()[0];
		PtrSegment ptslSegment = ptsl.getSegment(ptrSegment.getName());
		//PointingBlocksSlice ptslObs = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_OBS).copy();
		PointingBlocksSlice obs = ptrSegment.getAllBlocksOfType(PointingBlock.TYPE_OBS);
		PointingBlocksSlice ptslObs = new PointingBlocksSlice();
		ptslObs.setSlice(obs);
		PointingBlocksSlice manv = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MNAV).copy();
		PointingBlocksSlice mocm = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MOCM).copy();
		PointingBlocksSlice mslw = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MSLW).copy();
		PointingBlocksSlice mwnv = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MWNV).copy();
		PointingBlocksSlice mwol = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MWOL).copy();
		PointingBlocksSlice mwac = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_MWAC).copy();
		PointingBlocksSlice maintenance=new PointingBlocksSlice();
		maintenance.setSlice(manv);
		maintenance.setSlice(mocm);
		maintenance.setSlice(mslw);
		maintenance.setSlice(mwnv);
		maintenance.setSlice(mwol);
		maintenance.setSlice(mwac);
		ptslObs.setSlice(maintenance);
		PtrSegment newSegment = new PtrSegment(ptrSegment.getName());
		newSegment.setSlice(ptslObs);
		newSegment.repairMocm();
		newSegment.repairSlews();
		newSegment.repairConsecutiveBlocks();
		repairGaps(newSegment);

		ptr.remove(ptrSegment.getName());
		
		ptr.addSegment(newSegment);

		return ptr;
		//ptslObs.setSlice(obs);
		
	}*/
	
	public static Ptr rebasePtrPtsl(Ptr ptr,Ptr ptsl){
		LOG.info("Starting rebase PTR");
		//Thread.dumpStack();
		PtrSegment ptrSegment = ptr.getSegments()[0];
		PtrSegment ptslSegment = ptsl.getSegment(ptrSegment.getName());
		//LOG.info("Starting get all maintenance blocks");
		PointingBlocksSlice maintenance = ptslSegment.getAllMaintenanceBlocks().copy();
		//LOG.info("stop get all maintenance blocks");
		//LOG.info("Starting get all blocks of type");
		PointingBlocksSlice ptslObs = ptslSegment.getAllBlocksOfType(PointingBlock.TYPE_OBS).copy();
		//LOG.info("stop get all blocks of type");
		//LOG.info("Starting get all blocks of type II");
		PointingBlocksSlice obs = ptrSegment.getAllBlocksOfType(PointingBlock.TYPE_OBS);
		//LOG.info("stop get all blocks of type II");	
		
		//PointingBlocksSlice merged=new PointingBlocksSlice();
		PointingBlocksSlice merged=ptslObs.copy();
		//LOG.info("Starting setslice");
		//merged.setSlice(ptslObs);
		//LOG.info("stop setslice");
		//LOG.info("Starting get all obs blocks");
		PointingBlock[] allObsBlocks = obs.getBlocks();
		//LOG.info("stop get all obs blocks");
		//LOG.info("Starting loop");
		for (int i=0;i<allObsBlocks.length;i++){
			PointingBlock o = allObsBlocks[i];
			PointingBlock ptslBlockAtStart = ptslSegment.getBlockAt(o.getStartTime());
			PointingBlock ptslBlockAtEnd = ptslSegment.getBlockAt(o.getEndTime());

			if (ptslBlockAtStart!=null && ptslBlockAtStart.isMaintenance()) o.setStartTime(ptslBlockAtStart.getEndTime());
			if (ptslBlockAtEnd!=null && ptslBlockAtEnd.isMaintenance()) o.setEndTime(ptslBlockAtEnd.getStartTime());
			
		}
		//LOG.info("end loop");
		//LOG.info("Starting setslice II");
		merged.setSlice(obs);
		//LOG.info("stop setslice II");
		//LOG.info("Starting setslice III");
		merged.setSlice(maintenance);
		//LOG.info("stop setslice III");
		
		
		PtrSegment newSegment = new PtrSegment(ptrSegment.getName());
		//LOG.info("Starting setslice IV");
		//newSegment.setSlice(merged);
		newSegment.hardSetSlice(merged);
		//LOG.info("stop setslice IV");
		//LOG.info("Starting repair mocm");
		newSegment.repairMocm();
		//LOG.info("stop repair mocm");
		//LOG.info("Starting repair slews");
		newSegment.repairSlews();
		//LOG.info("stop repair slews");
		//LOG.info("Starting repair consecitive blocks");
		newSegment.repairConsecutiveBlocks();
		//LOG.info("stop repair consecitive blocks");
		//LOG.info("Starting repair gaps");
		repairGaps(newSegment);
		//LOG.info("stoping repair gaps");
		ptr.remove(ptrSegment.getName());
		
		ptr.addSegment(newSegment);
		LOG.info("stop rebase PTR");
		return ptr;
	
	}


}
