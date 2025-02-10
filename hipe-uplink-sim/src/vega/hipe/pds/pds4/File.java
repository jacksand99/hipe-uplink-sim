package vega.hipe.pds.pds4;

import java.util.HashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;

/**
 * The File class consists of attributes that describe a file in a data store.
 * @author jarenas
 *
 */
public class File {
	private String file_name;
	private String local_identifier;
	private String creation_date_time;
	private String file_size;
	private String file_size_unit;
	private String records;
	private String md5_checksum;
	public static String FILE_NAME="file_name";
	public static String LOCAL_IDENTIFIER="local_identifier";
	public static String CREATION_DATE_TIME="creation_date_time";
	public static String FILE_SIZE="file_size";
	public static String UNIT="unit";
	public static String RECORDS="records";
	public static String MD5_CHECKSUM="md5_checksum";
	public static String FILE="File";
	
	public File(){
		file_name="";
		local_identifier="";
		creation_date_time="";
		file_size="";
		file_size_unit="";
		records="";
		md5_checksum="";	
	}
	
	public static File getFromNode(Node node){
		File result=new File();
		Element poElement = (Element) node;
		try{
			result.setFileName(poElement.getElementsByTagName(FILE_NAME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FILE_NAME);
			e.printStackTrace();
		}
		try{
			result.setLocalIdentifier(((Element) node).getElementsByTagName(LOCAL_IDENTIFIER).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LOCAL_IDENTIFIER);
		}
		try{
			result.setCreationDateTime(((Element) node).getElementsByTagName(CREATION_DATE_TIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+CREATION_DATE_TIME);
		}
		
		try{
			result.setFileSize(((Element) node).getElementsByTagName(FILE_SIZE).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FILE_SIZE);
		}
		
		try{
			result.setFileSizeUnit(((Element) node).getElementsByTagName(FILE_SIZE).item(0).getAttributes().getNamedItem(UNIT).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+FILE_SIZE+" "+UNIT);
		}
		
		try{
			result.setRecords(((Element) node).getElementsByTagName(RECORDS).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+RECORDS);
		}
		
		try{
			result.setMd5Checksum(((Element) node).getElementsByTagName(MD5_CHECKSUM).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+MD5_CHECKSUM);
		}
		

			return result;
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
		result=result+id+"<File>\n";
		result=result+id+"\t<"+FILE_NAME+">"+file_name+"</"+FILE_NAME+">\n";
		result=result+id+"\t<"+LOCAL_IDENTIFIER+">"+local_identifier+"</"+LOCAL_IDENTIFIER+">\n";
		result=result+id+"\t<"+CREATION_DATE_TIME+">"+creation_date_time+"</"+CREATION_DATE_TIME+">\n";
		result=result+id+"\t<"+FILE_SIZE+" unit=\""+file_size_unit+"\">"+file_size+"</"+FILE_SIZE+">\n";
		result=result+id+"\t<"+RECORDS+">"+records+"</"+RECORDS+">\n";
		result=result+id+"\t<"+MD5_CHECKSUM+">"+md5_checksum+"</"+MD5_CHECKSUM+">\n";
		

		result=result+id+"</File>\n";
		
		return result;
	}
	
	public String getFileName(){
		return file_name;
	}
	
	public void setFileName(String newFileName){
		file_name=newFileName;
	}
	
	public String getLocalIdentifier(){
		return local_identifier;
	}
	
	public void setLocalIdentifier(String newLocalIdentifier){
		local_identifier=newLocalIdentifier;
	}
	
	public String getCreationDateTime(){
		return creation_date_time;
	}
	
	public void setCreationDateTime(String newCreationDateTime){
		creation_date_time=newCreationDateTime;
	}
	
	public String getFileSize(){
		return file_size;
	}
	
	public void setFileSize(String newFileSize){
		file_size=newFileSize;
	}
	
	public String getFileSizeUnit(){
		return file_size_unit;
	}
	
	public void setFileSizeUnit(String newFileSizeUnit){
		file_size_unit=newFileSizeUnit;
	}
	
	public String getRecords(){
		return records;
	}
	
	public void setRecords(String newRecords){
		records=newRecords;
	}
	
	public String getMd5Checksum(){
		return md5_checksum;
	}
	
	public void setMd5Checksum(String newMd5Checksum){
		md5_checksum=newMd5Checksum;
	}
}
