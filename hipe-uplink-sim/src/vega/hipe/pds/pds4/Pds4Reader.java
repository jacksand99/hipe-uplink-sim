package vega.hipe.pds.pds4;

import java.io.File;
import java.util.HashSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class Pds4Reader {
	public static ProductObservational[] readProductObservationalfromFile(String file) {

		//Ptr result = new Ptr();
		ProductObservational[] result;
		HashSet<ProductObservational> poSet=new HashSet<ProductObservational>();
		try {
			 
			File fXmlFile = new File(file);
			//result.setName(fXmlFile.getName());
			//result.setPath(fXmlFile.getParent());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListProductObservational = doc.getElementsByTagName(ProductObservational.PRODUCT_OBSERVATIONAL);
			for (int i=0;i<nListProductObservational.getLength();i++){
				Node pObservationalNode=nListProductObservational.item(i);
				ProductObservational po=ProductObservational.getFromNode(pObservationalNode);
				poSet.add(po);
				
			}
		}catch (Exception e){
			IllegalArgumentException iae=new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw iae;
		}
			/*Node nBody =nListHeader.item(0);
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
		    }*/
		result=new ProductObservational[poSet.size()];
		result=poSet.toArray(result);
		
		return result; 
	}

}
