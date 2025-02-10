package vega.simplewebserver;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import vega.uplink.Properties;
import vega.uplink.commanding.Por;
public class Utils {
	/**
	 * vega.simplewebserver.logfile
	 */
	public static String LOG_FILE_PROPERTY="vega.simplewebserver.logfile";
    static String[] rules={"body {color:#000; font-family:times; margin: 4px; }",
      		"h1 {color: blcak;}",
      		"h2 {color: black;}",
      		"pre {font : 10px monaco; color : black; background-color : #fafafa; }",
      		"table.gridtable {font-family: verdana,arial,sans-serif; font-size:11px; color:#333333; border-width: 1px; border-color: #666666; border-collapse: collapse;}",
      		"table.gridtable th {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #dedede;}",
      		"table.gridtable td {border-width: 1px;padding: 8px;border-style: solid;border-color: #666666;background-color: #ffffff;}"
      };

/**
* Work out the filename extension. If there isn't one, we keep it as the empty string ("").
* @param file
* @return the file's extension, or an empty string otherwise
*/
	static String getExtension(File file) {
		String extension = "";
		String filename = file.getName();
		int dotPos = filename.lastIndexOf(".");
		if (dotPos >= 0) {
		extension = filename.substring(dotPos);
		}
		return extension.toLowerCase();
	}
	

	public static void log(String text){
		String logfile="log.txt";
		try{
			logfile=Properties.getProperty(LOG_FILE_PROPERTY);
		}catch (Exception e){
			//Use default lof file
		}
		try{
			
			FileWriter writer = new FileWriter(logfile, true);
			writer.write("\n"+text);
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	  protected static String getHeaderHTML(String title){
		  String result="";
		  result=result+"<head><title>"+title+"</title>\n"+
				  "<style type=\"text/css\">\n";
	      for (int i=0;i<rules.length;i++){
	    	  result=result+rules[i]+"\n";
	      }
	      result=result+"</style> </head>\n";
		  return result;
	  }
}