/*

   Licensed under GNU Lesser General Public License (http://www.gnu.org/licenses/lgpl.html)

 */
package vega.hipe.gui.xmlutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang3.StringEscapeUtils;

public class HtmlDocument extends XmlData{
	//String htmlString;
	String title;
    String[] rules={"body {color:#000; font-family:times; margin: 4px; }",
      		"h1 {color: blcak;}",
      		"h2 {color: black;}",
      		"pre {font : 10px monaco; color : black; background-color : #fafafa; }",
      		"table.gridtable {font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse;}",
      		"table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}",
      		"table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}"
      };

  public HtmlDocument(String title,String text){
	  super();
	  this.title=title;
	  setXmlData(text);
  }
  public static String escapeHTML(String original){
	  return StringEscapeUtils.escapeHtml4(original);
  }
  public String[] getRules(){
	  return rules;
  }
  private String getRulesHTML(){
	  String result="";
	  result=result+"<head><title>"+title+"</title>\n"+
			  "<style type=\"text/css\">\n";
      for (int i=0;i<rules.length;i++){
    	  result=result+rules[i]+"\n";
      }
      result=result+"</style> </head>\n";
	  return result;
  }
  public void saveReportToFile(String fileName) throws FileNotFoundException, UnsupportedEncodingException{
	  String text=new String(getXmlData());
	  text=text.replace("<html>", "<html>\n"+getRulesHTML());
		//try{
			PrintWriter writer = new PrintWriter(fileName, "UTF-8");
			writer.print(text);
			writer.close();
		//}catch (Exception e){
			//e.printStackTrace();
		//}
  }
  
  public void setHtmlString(String htmlString){
	  setXmlData(htmlString);
  }
  
  public String getHtmlString(){
	  return getXmlData();
  }
  
  public String getTitle(){
	  return title;
  }
  
	public static HtmlDocument readFromFile(String file) throws IOException{
		XmlData data = XmlData.readFromFile(file);
		HtmlDocument result = new HtmlDocument(data.getFileName(),data.getXmlData());

		return result;
	}
  


}
