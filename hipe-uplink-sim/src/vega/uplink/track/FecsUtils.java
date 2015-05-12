package vega.uplink.track;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import vega.uplink.commanding.PorUtils;

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
		try {
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			result=Fecs.readFromDoc(doc);
			
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			
		}catch (Exception e){
			LOG.throwing("PORUtils", "readFecsFromFile", e);
			e.printStackTrace();
			throw(e);
		}
		
		return result;
	}

}
