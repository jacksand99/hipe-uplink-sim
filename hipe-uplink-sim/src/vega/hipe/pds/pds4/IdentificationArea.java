package vega.hipe.pds.pds4;

import java.util.HashSet;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;

/**
 * The identification area consists of attributes that identify and name an object.
 * @author jarenas
 *
 */
public class IdentificationArea {
	private String logical_identifier;
	private String version_id;
	private String title;
	private String information_model_version;
	private String product_class;
	private HashSet<ModificationDetail> modifications;
	public static String IDENTIFICATION_AREA="Identification_Area";
	public static String LOGICAL_IDENTIFIER="logical_identifier";
	public static String  VERSION_ID="version_id";
	public static String   TITLE="title";
	public static String INFORMATION_MODEL_VERSION="information_model_version";
	public static String PRODUCT_CLASS="product_class";
	public static String MODIFICATION_HISTORY="Modification_History";
	public IdentificationArea(){
		logical_identifier="";
		version_id="";
		title="";
		information_model_version="";
		product_class="";
		modifications=new HashSet<ModificationDetail>();
		
	}
	
	public String getLogicalIdentifier(){
		return logical_identifier;
	}
	
	public String getVersionId(){
		return version_id;
	}
	public String getTitle(){
		return title;
	}
	public String getInformationModelVersion(){
		return information_model_version;
	}
	public String getProductClass(){
		return product_class;
	}
	public ModificationDetail[] getModifications(){
		ModificationDetail[] result = new ModificationDetail[modifications.size()];
		return modifications.toArray(result);
	}
	
	public void setLogicalIdentifier(String newLogicalIdentifier){
		logical_identifier=newLogicalIdentifier;
	}
	
	public void setVersionId(String newVersionId){
		version_id=newVersionId;
	}
	
	public void setTitle(String newTitle){
		title=newTitle;
	}
	
	public void setInformationModelVersion(String newInformationModelVersion){
		information_model_version=newInformationModelVersion;
	}
	
	public void setProductClass(String newProductClass){
		product_class=newProductClass;
		
	}
	
	public void addModificationDetail(ModificationDetail newModificationDetail){
		modifications.add(newModificationDetail);
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
		result=result+id+"<Identification_Area>\n";
		result=result+id+"\t<logical_identifier>"+logical_identifier+"</logical_identifier>\n";
		result=result+id+"\t<version_id>"+version_id+"</version_id>\n";
		result=result+id+"\t<title>"+title+"</title>\n";
		result=result+id+"\t<information_model_version>"+information_model_version+"</information_model_version>\n";
		result=result+id+"\t<product_class>"+product_class+"</product_class>\n";
		result=result+id+"\t<Modification_History>\n";		
		Iterator<ModificationDetail> it = modifications.iterator();


		while (it.hasNext()){
			result=result+it.next().toXml(indent+2);
		}
		result=result+id+"\t</Modification_History>\n";
		result=result+id+"</Identification_Area>\n";
		
		return result;
	}
	public static IdentificationArea getFromNode(Node node){
		IdentificationArea result=new IdentificationArea();
		Element poElement = (Element) node;
		//Node idNode = poElement.getElementsByTagName(IdentificationArea.IDENTIFICATION_AREA).item(0);
		try{
			result.setLogicalIdentifier(((Element) node).getElementsByTagName(LOGICAL_IDENTIFIER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LOGICAL_IDENTIFIER);
		}
		try{
			result.setVersionId(((Element) node).getElementsByTagName(VERSION_ID).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+VERSION_ID);
			
		}
		try{
			
			result.setTitle(((Element) node).getElementsByTagName(TITLE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+TITLE);
			
		}
		try{
			result.setInformationModelVersion(((Element) node).getElementsByTagName(INFORMATION_MODEL_VERSION).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+INFORMATION_MODEL_VERSION);
			
		}
		try{
			result.setProductClass(((Element) node).getElementsByTagName(PRODUCT_CLASS).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+PRODUCT_CLASS);
			
		}
		try{
			NodeList mdetails=((Element)((Element) node).getElementsByTagName(MODIFICATION_HISTORY).item(0)).getElementsByTagName(ModificationDetail.MODIFICATION_DETAIL);
			for (int i=0;i<mdetails.getLength();i++){
				try{
					result.addModificationDetail(ModificationDetail.getFromNode(mdetails.item(i)));
				}catch(Exception e){
					VegaLog.info("Could not get "+ModificationDetail.MODIFICATION_DETAIL);
				}
			}
		}catch (Exception e){
			VegaLog.info("Could not get "+MODIFICATION_HISTORY);
			
		}
		return result;
	}



}
