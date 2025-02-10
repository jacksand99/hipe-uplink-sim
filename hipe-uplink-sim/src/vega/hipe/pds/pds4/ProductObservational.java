package vega.hipe.pds.pds4;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

import vega.hipe.logging.VegaLog;

/**
 * A Product_Observational is a set of one or more information objects produced by an observing system.
 * @author jarenas
 *
 */
public class ProductObservational {
	private IdentificationArea identifiaction;
	private ObservationArea observationArea;
	private ReferenceList referenceList;
	private FileAreaObservational fileAreaObservational;
	public static String PRODUCT_OBSERVATIONAL="Product_Observational";
	
	public ProductObservational(){
		identifiaction=new IdentificationArea();
		observationArea=new ObservationArea();
		referenceList=new ReferenceList();
		fileAreaObservational=new FileAreaObservational();
	}
	public IdentificationArea getIdentificationArea(){
		return identifiaction;
	}
	
	public void setIdentificationArea(IdentificationArea newIdentificationArea){
		identifiaction=newIdentificationArea;
	}
	
	public ObservationArea getObservationArea(){
		return observationArea;
	}
	
	public void setObservationArea(ObservationArea newObservationArea){
		observationArea=newObservationArea;
	}
	
	public ReferenceList getReferenceList(){
		return referenceList;
	}
	
	public void setReferenceList(ReferenceList newReferenceList){
		referenceList=newReferenceList;
	}
	
	public FileAreaObservational getFileAreaObservational(){
		return fileAreaObservational;
	}
	
	public void setFileAreaObservational(FileAreaObservational newFileAreaObservational){
		fileAreaObservational=newFileAreaObservational;
	}
	
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<Product_Observational xmlns=\"http://pds.nasa.gov/pds4/pds/v1\"xmlns:pds=\"http://pds.nasa.gov/pds4/pds/v1\" xmlns:dph=\"http://pds.nasa.gov/pds4/dph/v01\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://pds.nasa.gov/pds4/pds/v1\">\n";
		result=result+identifiaction.toXml(indent+1);
		result=result+observationArea.toXml(indent+1);
		result=result+referenceList.toXml(indent+1);
		result=result+fileAreaObservational.toXml(indent+1);
		result=result+id+"</Product_Observational>\n";
		
		return result;
	}
	
	public static ProductObservational getFromNode(Node node){
		ProductObservational result=new ProductObservational();
		Element poElement = (Element) node;
		poElement.normalize();
		try{
			Node identificationNode = poElement.getElementsByTagName(IdentificationArea.IDENTIFICATION_AREA).item(0);
			result.setIdentificationArea(IdentificationArea.getFromNode(identificationNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+IdentificationArea.IDENTIFICATION_AREA);
		}
		try{
			Node observationAreaNode = poElement.getElementsByTagName(ObservationArea.OBSERVING_AREA).item(0);
			result.setObservationArea(ObservationArea.getFromNode(observationAreaNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+ObservationArea.OBSERVING_AREA);
		}
		try{
			Node referenceListNode = poElement.getElementsByTagName(ReferenceList.REFERENCE_LIST).item(0);
			result.setReferenceList(ReferenceList.getFromNode(referenceListNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+ObservationArea.OBSERVING_AREA);
		}
		
		try{
			Node fileAreaObservationalNode = poElement.getElementsByTagName(FileAreaObservational.FILE_AREA_OBSERVATIONAL).item(0);
			result.setFileAreaObservational(FileAreaObservational.getFromNode(fileAreaObservationalNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+FileAreaObservational.FILE_AREA_OBSERVATIONAL);
		}
		return result;
	}
	
}
