package vega.uplink.commanding;


import vega.hipe.FileUtil;
import vega.hipe.logging.VegaLog;
import herschel.share.io.archive.ZipReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.DateUtil;


/**
 * Class to perform a set of utility methods over PORs and other classes from this package
 * @author jarenas
 *
 */
public class PorUtils {
    public static void writePORGtofile(String file,SuperPor por){
        File f = new File(file);
        
        String newName="MAN__"+f.getName();
        if (file.contains("PORG_")) {
            newName="MAN__"+f.getName().substring(5);
        }
        writePORGtofile(file,por,newName);
    }
	//private static final Logger LOG = Logger.getLogger(PorUtils.class.getName());
	/**
	 * Write a SuperPor as a PORG (zip file containing individual PORs)
	 * @param file
	 * @param por
	 */
	public static void writePORGtofile(String file,SuperPor por,String manifestFileName){
		File porg = new File(file);
		File tdir = null;
		String manifest="";
		
		try {
            tdir = FileUtil.createTempDir(porg.getName()+"_temp_porg", "zip");
            Por[] pors = por.getPors();
            manifest=new Integer(pors.length).toString()+"\n";
            for (int i=0;i<pors.length;i++){
            	writePORtofile(tdir.getAbsolutePath()+"/"+pors[i].getName(),pors[i]);
            	manifest=manifest+calculateHash(tdir.getAbsolutePath()+"/"+pors[i].getName())+"  "+pors[i].getName()+"\n";
            }
            PrintWriter writer = new PrintWriter(tdir.getAbsolutePath()+"/"+manifestFileName, "UTF-8");
            writer.print(manifest);
            writer.close();
            zipIt(file,tdir.getAbsolutePath());
            VegaLog.info("Deleting temp zip of " + file + " in " + tdir);
            FileUtil.delete(tdir);

		}catch (Exception e){
			VegaLog.throwing(PorUtils.class, "writePORGtofile", e);
			e.printStackTrace();
		}
		finally {
        if (tdir != null) {
        	VegaLog.info("Deleting temp unzip of " + file + " in " + tdir);
            FileUtil.delete(tdir);
        }
    }

	}
	   public static String calculateHash(String path) {
	        String result=null;
	        try {
	            byte[] data = Files.readAllBytes(Paths.get(path));
	            byte[] hash = MessageDigest.getInstance("MD5").digest(data);
	            String checksum = new BigInteger(1, hash).toString(16);
	            
	            if (checksum.length()<32) {
	                for (int i=0;i<32-checksum.length();i++) {
	                    checksum="0"+checksum;
	                }
	            }
	            result=checksum.toUpperCase();
	        /*MessageDigest digest = MessageDigest.getInstance("md5Hex");
	        InputStream fis = new FileInputStream(new File(path));
	        int n = 0;
	        byte[] buffer = new byte[8192];
	        while (n != -1) {
	            n = fis.read(buffer);
	            if (n > 0) {
	                digest.update(buffer, 0, n);
	            }
	        }
	        byte[] hash = digest.digest();
	        result = new String(hash);
	        StringBuilder hexString = new StringBuilder(2 * hash.length);
	        for (int i = 0; i < hash.length; i++) {
	            String hex = Integer.toHexString(0xff & hash[i]);
	            if(hex.length() == 1) {
	                hexString.append('0');
	            }
	            hexString.append(hex);
	        }
	       result=hexString.toString();*/
	        }catch (Exception e) {
	            VegaLog.throwing(PorUtils.class, "calculateHash", e);
	            e.printStackTrace();
	        }
	        
	        return result;
	    }
	   public static SuperPor readPORGfromFile(String fileName) throws IOException{
	       return readPORGfromFile(fileName, true);
	       
	   }
	/**
	 * Read a SuperPor from a PORG (zip file containing individual PORs)
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static SuperPor readPORGfromFile(String fileName,boolean recalculateVaidity) throws IOException{
		File file=new File(fileName);
        File tdir = null;
        boolean error = true;
        SuperPor result=new SuperPor();
        result.setCalculateValidity(false);
        result.setName(file.getName());
        result.setPath(file.getParent());
        try {
            try {
                tdir = FileUtil.createTempDir(file.getName()+"_temp_porg", "unzip");
                ZipReader ar= new ZipReader(file);
                ar.extract(tdir);
                for (String name : tdir.list()) {
                    if (!name.startsWith(".") && !name.startsWith("MAN"))  {
                        error = false;
                        String porFile = new File(tdir, name).getAbsolutePath();
                        Por por = readPORfromFile(porFile,recalculateVaidity);
                        por.setName(name);
                        
                        result.addPor(por);
                    }
                }
                VegaLog.info("Deleting temp unzip of " + file + " in " + tdir);
                FileUtil.delete(tdir);
                result.setType("PORG");
                result.setCalculateValidity(recalculateVaidity);
                return result;
            } catch (ZipException e) {
                throw new IOException("Could not unzip " + file + " in " + tdir, e);
            } catch (IOException e) {
                throw new IOException("Problems with temp unzipping " + file, e);
            }
        } finally {
            if (error && tdir != null) {
            	VegaLog.info("Deleting temp unzip of " + file + " in " + tdir);
                FileUtil.delete(tdir);
            }
        }
	}
	public static Por readPorfromDocument(Document doc) {
	    return readPorfromDocument(doc,true);
	}
	/**
	 * Read POR from a XML document
	 * @param doc
	 * @return
	 */
	public static Por readPorfromDocument(Document doc,boolean calculateValidity){
		Por result = new Por();
		result.setCalculateValidity(false);
		try {
			 
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListHeader = doc.getElementsByTagName("header");
			Node nHeader =nListHeader.item(0);
			Element el = (Element) nHeader;
			String pGenTime=el.getElementsByTagName("genTime").item(0).getTextContent();
			String pStartTime=el.getElementsByTagName("startTime").item(0).getTextContent();
			String pEndTime=el.getElementsByTagName("stopTime").item(0).getTextContent();
		 
			NodeList nListSequences = doc.getElementsByTagName("sequence");

			Sequence[] seq=readSequences(nListSequences);
			result.setSequences(seq);
			result.setGenerationTime(pGenTime);
			String[] vTimes={pStartTime,pEndTime};
			result.setValidityTimes(vTimes);
		    } catch (Exception e) {
		    	IllegalArgumentException iae = new IllegalArgumentException("Could not read Por:"+e.getMessage());
		    	iae.initCause(e);
		    	throw(iae);
		    }
		result.setCalculateValidity(calculateValidity);
		return result; 
	}
	public static Por readPORfromFile(String file){
	    return readPORfromFile(file,true);
	    
	}
	/**
	 * read POR from a xml file
	 * @param file
	 * @return
	 */
	public static Por readPORfromFile(String file,boolean calculateValidity){

		Por result = new Por();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			result=readPorfromDocument(doc,calculateValidity);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			
		    } catch (Exception e) {
		    	IllegalArgumentException iae = new IllegalArgumentException("Could not read Por:"+e.getMessage());
		    	iae.initCause(e);
		    	throw(iae);
		    }
		
		return result; 
	}
	
	private static Sequence[] readSequences(NodeList nList) throws ParseException, IOException{
		Sequence[] result = new Sequence[nList.getLength()];
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	 
				String sName=eElement.getAttribute("name");
				String sUniqueID=eElement.getElementsByTagName("uniqueID").item(0).getTextContent();
				String sFlag=Sequence.INSERT_FLAG;
				try {
				    sFlag=eElement.getElementsByTagName("insertOrDeleteFlag").item(0).getTextContent();
				}catch (Exception e) {
				    e.printStackTrace();
				}
				String sSource=eElement.getElementsByTagName("source").item(0).getTextContent();
				String sDescription="";
				try {
				    sDescription=eElement.getElementsByTagName("description").item(0).getTextContent();
				}catch (Exception noDes) {
				    vega.hipe.logging.VegaLog.info("Sequence without description");
				}
				String sDestination=eElement.getElementsByTagName("destination").item(0).getTextContent();
				String sExecutionTime=eElement.getElementsByTagName("actionTime").item(0).getTextContent();
				Node parList=eElement.getElementsByTagName("parameterList").item(0);
				Parameter[] sParameters=new Parameter[0];
				if (parList!=null){
					Element el2=(Element) parList;
					NodeList params=el2.getElementsByTagName("parameter");
					sParameters=readParameters(params);
				}
				Node proList=eElement.getElementsByTagName("profileList").item(0);
				SequenceProfile[] sProfiles=new SequenceProfile[0];
				if (proList!=null){
					Element el3=(Element) proList;
					NodeList profiles=el3.getElementsByTagName("profile");
					sProfiles=readProfiles(profiles);	
				}
				result[temp]=new Sequence (sName,sUniqueID,sFlag,sSource,sDestination.charAt(0),DateUtil.DOYToDate(sExecutionTime),sParameters,sProfiles);
				result[temp].setDescription(sDescription);
			}
		}
		return result;
	}
	
	/**
	 * Read parameters from a xml node list
	 * @param nList
	 * @return
	 * @throws IOException
	 */
	public static Parameter[] readParameters(NodeList nList) throws IOException{
		Mib MIB = Mib.getMib();
		Parameter[] result = new Parameter[nList.getLength()];

		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);

	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				String name=eElement.getAttribute("name");


				Node val=eElement.getElementsByTagName("value").item(0);
				int nelements=eElement.getElementsByTagName("value").getLength();
				if (nelements==0){
					result[temp]=MIB.getDefaultParameter(name);
				}else{
					
				
					Element el=(Element) val;
					String representation=el.getAttribute("representation");
					String radix=el.getAttribute("radix");
					String value=val.getTextContent();
		               String elDescription="";
		                try {
		                    elDescription=eElement.getElementsByTagName("description").item(0).getTextContent();

		                    //result[temp].setDescription(elDescription);
		                }catch (Exception noDes) {
		                    vega.hipe.logging.VegaLog.info("Parameter without description");
		                }
		                
					
					if (radix==""){
						result[temp]=new ParameterString(name,representation,value);
					}
					if (radix.equals(Parameter.RADIX_HEX)){
						try{
							String hexValue = value.replace("0x", "");
							result[temp]=new ParameterFloat(name,representation,radix,new BigInteger(hexValue, 16).floatValue());
							//result[temp]=new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt(hexValue, 16)).floatValue());
						}
						catch (java.lang.NumberFormatException e){
							try{
								result[temp]=new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt("0x"+value, 16)).floatValue());
							}
							catch (java.lang.NumberFormatException e1){
								e.printStackTrace();
								e1.printStackTrace();
								result[temp]=new ParameterFloat(name,representation,radix,0);
							}
						}
						
					}
					if (radix.equals(Parameter.RADIX_DECIMAL)) {
					    try {
					        result[temp]=new ParameterFloat(name,representation,radix,new Float(value).floatValue());
					    }
					    catch (Exception e) {
					        result[temp]=new ParameterFloat(name,representation,radix,new Float(0).floatValue());
					        e.printStackTrace();
					    }
					}
                    result[temp].setDescription(elDescription);
				}
				
				

			}
			
		}
		
		return result;
	}
	
	/**
	 * Read profiles from a xml node list
	 * @param nList
	 * @return
	 */
	public static SequenceProfile[] readProfiles(NodeList nList){
		SequenceProfile[] result = new SequenceProfile[nList.getLength()];
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	 
				String type=eElement.getAttribute("type");
				String value=eElement.getElementsByTagName("value").item(0).getTextContent();
				String offSet=eElement.getElementsByTagName("timeOffset").item(0).getTextContent();
				
				
				result[temp]=new SequenceProfile(type,offSet,new Double(value).doubleValue());
								

			}
		}

		return result;
	}
	/**
	 * Save POR into a xml file
	 * @param POR
	 */
	public static void savePor(Por POR){
		writePORtofile(POR.getPath()+"/"+POR.getName(),POR);
	}
	/**
	 * save POR into a xml file
	 * @param file
	 * @param POR
	 */
	public static void writePORtofile(String file,Por POR){
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(POR.getXMLDocument());
			StreamResult result = new StreamResult(new File(file));
			transformer.transform(source, result);
			VegaLog.info("Writted POR to file " + file );
		}catch (Exception e){
			VegaLog.severe(e.getMessage());
			VegaLog.throwing(PorUtils.class, "writePORtofile", e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * save POR as ITL file
	 * @param file
	 * @param POR
	 */
	public static void writeITLtofile(String file,Por POR){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(PORtoITL(POR));
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	/**
	 * Write the ORCD into a XML file
	 * @param file
	 * @param orcd
	 */
	public static void writeORCDtoXMLfile(String file,Orcd orcd){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(orcd.toXml());
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Get a POR as ITL
	 * @param POR
	 * @return
	 */
	public static String PORtoITL(Por POR){
		Mib mib;
		try{
			mib=Mib.getMib();
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not get MIB "+e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
		StringBuilder result=new StringBuilder();
		result.append("");
		String l01="Version: 1\n";
		String l02="Ref_date: "+DateUtil.dateToLiteralNoTime(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+DateUtil.dateToLiteral(POR.getValidityDates()[1])+"\n\n\n";
		StringBuilder l05=new StringBuilder();
		l05.append("");
		String[] initModes = POR.getInitModes().toArray();
		for (int i=0;i<initModes.length;i++){
			l05.append("Init_mode: "+initModes[i]+"\n");
		}
		String[] initMS = POR.getInitMS().toArray();
		for (int i=0;i<initMS.length;i++){
			l05.append("Init_MS: "+initMS[i]+"\n");
		}
		String[] initMemory = POR.getInitMemory().toArray();
		for (int i=0;i<initMemory.length;i++){
			l05.append("Init_memory: "+initMemory[i]+"\n");
		}
		String[] initDataStore = POR.getInitDataStore().toArray();
		for (int i=0;i<initDataStore.length;i++){
			l05.append("Init_data_store: "+initDataStore[i]+"\n");
		}
		AbstractSequence[] tempSeq=POR.getOrderedSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			l05.append(DateUtil.dateToLiteral(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\ #"+mib.getSequenceDescription(tempSeq[i].getName())+"\n");
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				String val=tempParam[z].getStringValue();
				if (tempParam[z].getRepresentation().equals("Engineering")){
					if (val!=null && !val.startsWith("\"")){
						val="\""+val+"\"";
					}
				}
				l05.append("\t"+tempParam[z].getName()+"="+val+" ["+tempParam[z].getRepresentation()+"] \\ #"+mib.getParameterDescription(tempParam[z].getName())+"\n");
			}
			tempPro=tempSeq[i].getProfiles();
			String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
			String powerProfile="\tPOWER_PROFILE = \t\t\t";
			boolean dataRatePresent=false;
			boolean powerProfilePresent=false;
			for (int j=0;j<tempPro.length;j++){
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
					dataRateProfile=dataRateProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]";
					dataRatePresent=true;
				}
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
					powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
					powerProfilePresent=true;
				}
				
			}
			if (dataRatePresent) l05.append(dataRateProfile+"\\\n");
			if (powerProfilePresent) l05.append(powerProfile+"\\\n");

			l05.append("\t\t\t\t)\n");

		}
		result.append(l01);
		result.append(l02);
		result.append(l03);
		result.append(l04);
		result.append(l05);
		return result.toString();
	}
	


	
	
 
    /**
     * ZIP the contents of folder into a zip file
     * @param zipFile
     * @param sourceFolder
     */
    public static void zipIt(String zipFile,String sourceFolder){
    	 
        byte[] buffer = new byte[1024];
    
        try{
    
       	FileOutputStream fos = new FileOutputStream(zipFile);
       	ZipOutputStream zos = new ZipOutputStream(fos);
    
       	VegaLog.info("Output to Zip : " + zipFile);
       	String[] fileList=new File(sourceFolder).list();
       	for(String file : fileList){
    
       		VegaLog.info("File Added : " + file);
       		ZipEntry ze= new ZipEntry(file);
           	zos.putNextEntry(ze);
    
           	FileInputStream in = 
                          new FileInputStream(sourceFolder + File.separator + file);
    
           	int len;
           	while ((len = in.read(buffer)) > 0) {
           		zos.write(buffer, 0, len);
           	}
    
           	in.close();
       	}
    
       	zos.closeEntry();
       	//remember close it
       	zos.close();
    
       	VegaLog.info("Zip file Done");
       }catch(IOException ex){
    	   VegaLog.throwing(PorUtils.class, "zipIt", ex);
          ex.printStackTrace();   
       }
      }
	
}
