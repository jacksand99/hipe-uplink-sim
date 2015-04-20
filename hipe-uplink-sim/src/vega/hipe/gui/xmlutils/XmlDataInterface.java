package vega.hipe.gui.xmlutils;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;



/**
 * Interface to be implemented by classes that contains XML data
 * @author jarenas
 *
 */
public interface XmlDataInterface {
	/**
	 * Get the XML text representing this object
	 * @return
	 */
	public String getXmlData();
	/**
	 * Set the file name where this object is or will be stored
	 * @param newFileName
	 */
	public void setFileName(String newFileName);
	/**
	 * Set the path where this object is or will be stored
	 * @param newPath
	 */
	public void setPath(String newPath);
	/**
	 * Get the file name where this object is or will be stored
	 * @return
	 */
	public String getFileName();
	/**
	 * Get the path where this object is or will be stored
	 * @return
	 */
	public String getPath();	
	/**
	 * Set the XML text representing this object
	 * @param data
	 */
	public void setXmlData(String data);	
	/**
	 * Save this object into a file with the give file name
	 * @param file Complete path where to save the file
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void saveAs(String file) throws FileNotFoundException, UnsupportedEncodingException;
	
	/**
	 * Save this object into a file.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void save() throws FileNotFoundException, UnsupportedEncodingException;
	
}
