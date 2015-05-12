package vega.uplink.track;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;



/**
 * Utility class to read and save FECS to/from files
 * @author jarenas
 *
 */
public class FecsUtils {
	private static final Logger LOG = Logger.getLogger(FecsUtils.class.getName());

	/**
	 * Read FECS from XML file
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Fecs readFecsFromFile(String file) throws Exception{
		Fecs result=new Fecs();
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			result=Fecs.readFromDoc(doc);
			
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
		
		return result;
	}
	
	/**
	 * Save FECS to XML file
	 * @param fecs fecs to be saved
	 * @param file file name, with full path
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void saveFecsToFile(Fecs fecs,String file) throws FileNotFoundException, UnsupportedEncodingException{

			fecs.writeToFile(file);

	}

}
