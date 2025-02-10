/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class XmlData implements XmlDataInterface{
	private String data;
	private String fileName;
	private String path;
	public XmlData(){
		data=new String();
		fileName=""+new Date().getTime();
		path=System.getProperty("user.home");
	}
	public String getXmlData(){
		return data;
	}
	public void setFileName(String newFileName){
		fileName=newFileName;
	}
	public void setPath(String newPath){
		path=newPath;
	}
	public String getFileName(){
		return fileName;
	}
	public String getPath(){
		return path;
	}
	
	public void setXmlData(String data){
		this.data=data;
	}
	
	public static XmlData readFromFile(String file) throws IOException{
		File f = new File(file);
		XmlData result = new XmlData();
		result.setFileName(f.getName());
		result.setPath(f.getParent());
		result.setXmlData(XmlData.readFile(file, StandardCharsets.UTF_8));

		return result;
	}
	private static String readFile(String pathToFile, Charset encoding) throws IOException {
			  byte[] encoded = Files.readAllBytes(Paths.get(pathToFile));
			  return new String(encoded, encoding);
	}
	
	public void writeToFile(String file) throws FileNotFoundException, UnsupportedEncodingException{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(data);
			writer.close();
	}
	
	public void save() throws FileNotFoundException, UnsupportedEncodingException{
		writeToFile(path+"//"+fileName);
	}
	@Override
	public void saveAs(String file) throws FileNotFoundException,
			UnsupportedEncodingException {
		writeToFile(file);
		
	}
}
