package vega.hipe.pds.pds4;

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The File Area Observational class describes, for an observational product, a file and one or more tagged_data_objects contained within the file.
 * @author jarenas
 *
 */
public class FileAreaObservational {
	private File file;
	private TableBase table;
	public static String FILE_AREA_OBSERVATIONAL="File_Area_Observational";
	
	public static FileAreaObservational getFromNode(Node node){
		FileAreaObservational result=new FileAreaObservational();
		Element poElement = (Element) node;
		try{
			Node fileNode = poElement.getElementsByTagName(File.FILE).item(0);
			result.setFile(File.getFromNode(fileNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+File.FILE);
		}
		try{
			Node tableNode = poElement.getElementsByTagName(TableCharacter.TABLE_CHARACTER).item(0);
			result.setTable(TableCharacter.getFromNode(tableNode));
		}catch (Exception e){
			VegaLog.info("Could not get "+TableCharacter.TABLE_CHARACTER);
		}

		return result;
	}
	
	public FileAreaObservational(){
		file=null;
		table=null;
	}
	
	public void setFile(File newFile){
		file=newFile;
	}
	
	public File getFile(){
		return file;
	}
	
	public void setTable(TableBase newTable){
		table=newTable;
	}
	
	public TableBase getTable(){
		return table;
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
		result=result+id+"<File_Area_Observational>\n";
		if (file!=null) result=result+file.toXml(indent+1);
		if (table!=null) result=result+table.toXml(indent+1);
		result=result+id+"<File_Area_Observational>\n";		
		
		return result;
	}
}
