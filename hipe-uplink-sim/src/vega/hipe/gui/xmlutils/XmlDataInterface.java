package vega.hipe.gui.xmlutils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;



public interface XmlDataInterface {



	public String getXmlData();
	public void setFileName(String newFileName);
	public void setPath(String newPath);
	public String getFileName();
	public String getPath();
	
	public void setXmlData(String data);
	
	

	
	public void saveAs(String file) throws FileNotFoundException, UnsupportedEncodingException;
	
	public void save() throws FileNotFoundException, UnsupportedEncodingException;
	
}
