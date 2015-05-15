package vega.uplink.pointing.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.util.HashMap;
import java.util.Iterator;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;


import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


import vega.hipe.logging.VegaLog;

public class FDClient {
	private static String OPPTR="OPPTR";
	private static String OPPDFM="OPPDFM";
	private static String EPSILON="EPSILON";
	private static String ZETA="ZETA";
	private static String ETA="ETA";
	private static String OPS="ops";
	private static String INPUT_CASE="inputCase";
	private static String SCENARIO_ID="scenarioId";
	private static String QUERY_TYPE="queryType";
	private static String SERVICE="service";	
	private static String MTP_NUM="MTP_NUM";	
	private static final String ERROR_MESSAGE = "error_message";
	private static final String FILE = "file";   
	private static final String LOGFILE = "logFile";
	private static final String LOGFILE_1 = "logFile_1";    
	private static final String RUNID = "runid";
	private static final String STATUS = "//SEQGEN/status";    
	private static final String PTRID = "ptrid";
    private static final String STATUS_OK = "ok";   
	private static final String STATUS_MAINTENANCE = "maintenance";    
	private static final String NUMBER_VIOLATIONS = "numberOfViolations";

	public static String Att2AscRequest(String url,String ptrId,String mission,String trajectory){
		Part[] parts = new Part[5];
        

                parts[0]=new StringPart("mission", mission);
                parts[1]=new StringPart(SCENARIO_ID, ptrId);
                parts[2]=new StringPart(QUERY_TYPE,"run");
                parts[3]=new StringPart(SERVICE, "ATT2ASC");
                parts[4]=new StringPart(OPS, trajectory);
                

        

                		HashMap<String, String> map = doRequest(url,
                parts);
        if (!map.containsKey(FILE)) {
            throw new IllegalArgumentException(
                    "Response does not contain the ATT2ASC URL");
        }

        String attitudeFileUrl = map.get(FILE);

        VegaLog.info("Success ATT2ASC: "+attitudeFileUrl);
        return attitudeFileUrl;
	}
	public static String Ptr2AttRequest(String url,File ptrFile,File pdfmFile,String mission,String mtpNum,String trajectory,String epsilon,String zeta,String eta) throws FileNotFoundException{
		Part[] parts=new Part[12];
		parts[0]=new StringPart(OPS,trajectory);
		
		parts[1]=new StringPart(INPUT_CASE,"MTP_empty");
		parts[2]=new StringPart(SCENARIO_ID,"NEW_ID");
		parts[3]=new StringPart(QUERY_TYPE,"run");
		parts[4]=new StringPart(SERVICE,"PTR2ATT");
		parts[5]=new FilePart(OPPTR,ptrFile);
		parts[6]=new FilePart(OPPDFM,pdfmFile);
		parts[7]=new StringPart(EPSILON,epsilon);
		parts[8]=new StringPart(ZETA,zeta);
		parts[9]=new StringPart(ETA,eta);
		parts[10]=new StringPart("mission",mission);
		parts[11]=new StringPart(MTP_NUM,mtpNum);
		HashMap<String, String> map = doRequest(url,parts);
		//Iterator<String> it = map.keySet().iterator();
		
		return map.get(SCENARIO_ID);

	}
	public static void main(String[] args){
			String url="http://www.fd-tasc.info/roscmd-cgi-bin/seqgen.pl";
			String ptr="/Users/jarenas 1/Rosetta/mike/PTRM_PL_M016______05_P_RSM2PIM0.ROS";
			String pdfm="/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/PTR/PDFM_DM_016_01____P__00057.ROS";
			String mission="ROS";
			String mtpNum = "16";
			String trajectory="RORL_DL_005_04____P__00167";
			try{
			String id = Ptr2AttRequest(url,
					new File(ptr),
					new File(pdfm),
					mission,
					
					mtpNum,
					trajectory,
					"0.0",
					"0.0",
					"0.0"
					);
			String fileUrl = Att2AscRequest(url,id,mission,trajectory);
			String conAttFile = conChkAttRequest(url,id,mission,trajectory);
			String conRwFile = conChkRwRequest(url,id,mission,trajectory);
			String saaFile = saaRequest(url,id,mission,trajectory);
			AngularMomentum ag=angMomentumRequest(url,id,mission,trajectory);
			String hgaFile = hgaCovRequest(url,id,mission,trajectory);
			//System.out.println(fileUrl);
			downloadFile(fileUrl,"/Users/jarenas/testatt.txt");
			downloadFile(conAttFile,"/Users/jarenas/testconatt.txt");
			downloadFile(conRwFile,"/Users/jarenas/testconrw.txt");
			downloadFile(saaFile,"/Users/jarenas/testsaa.txt");
			downloadFile(hgaFile,"/Users/jarenas/testhga.txt");
			System.out.println(ag);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String conChkAttRequest(String url,String ptrId,String mission,String trajectory){
		 Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"CONCHK_ATT");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                HashMap<String, String> map =  doRequest(url,parts);
	        if (!map.containsKey(LOGFILE)) {
	            throw new IllegalArgumentException("Response does not contain the CONCHK_ATT URL");
	        }

	        String fileUrl = map.get(LOGFILE);
	        
	        VegaLog.info("Success CONCHK_ATT: "+fileUrl);
	        return fileUrl;
	}
	public static String hgaCovRequest(String url,String ptrId,String mission,String trajectory){
		 Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"HGACOV");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                HashMap<String, String> map =  doRequest(url,parts);
	        if (!map.containsKey(LOGFILE)) {
	            throw new IllegalArgumentException("Response does not contain the HGACOV URL");
	        }

	        String fileUrl = map.get(LOGFILE);
	        
	        VegaLog.info("Success HGACOV: "+fileUrl);
	        return fileUrl;
	}
	

	
	public static String saaRequest(String url,String ptrId,String mission,String trajectory){
		 Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"MAKE_SAA");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                HashMap<String, String> map =  doRequest(url,parts);
	        if (!map.containsKey(FILE)) {
	            throw new IllegalArgumentException("Response does not contain the MAKE_SAA URL");
	        }

	        String fileUrl = map.get(FILE);
	        
	        VegaLog.info("Success MAKE_SAA: "+fileUrl);
	        return fileUrl;
	}
	public static String conChkRwRequest(String url,String ptrId,String mission,String trajectory){
		 Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"CONCHK_RW");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                HashMap<String, String> map =  doRequest(url,parts);
	        if (!map.containsKey(LOGFILE)) {
	            throw new IllegalArgumentException("Response does not contain the CONCHK_RW URL");
	        }

	        String fileUrl = map.get(LOGFILE);
	        
	        VegaLog.info("Success CONCHK_RW: "+fileUrl);
	        return fileUrl;
	}
	
	public static AngularMomentum angMomentumRequest(String url,String ptrId,String mission,String trajectory){
		 Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"ANGMOM");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                AngularMomentum result = doRequestAngMom(url,parts);
	                
	        return result;
	}

    public static HashMap<String,String> doRequest( String url,  final Part[] parts) {

    	HttpClient client=new HttpClient();
        //Properties props = new Properties();
        HashMap<String,String> result=new HashMap<String,String>();
        try {
            PostMethod filePost = new PostMethod(url);
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, filePost.getParams())
                    );
            //VegaLog.info(new MultipartRequestEntity(parts, filePost.getParams()).toString());
            HttpMethodParams par = filePost.getParams();
            
            int statusCode = client.executeMethod(filePost);
            if (statusCode == HttpStatus.SC_OK) {

            	VegaLog.info(String.format("HTTP status:"+ filePost.getStatusLine()));

                
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(filePost.getResponseBodyAsStream());
                
                XPathFactory xPathFactory =  XPathFactory.newInstance();
                        
                String status = getXpathText(doc, xPathFactory, STATUS);
                VegaLog.finest(String.format("Web Service status: "+ status));
                if (status == null) {
                    throw new IllegalArgumentException("The response does not have status");
                } else if (status.equalsIgnoreCase(STATUS_MAINTENANCE)) {
                    throw new IllegalArgumentException("Server under maintenance");
                } else if (!status.equalsIgnoreCase(STATUS_OK)) {
                    throw new IllegalArgumentException(String.format("Failed request: "+ getValueOf(doc, ERROR_MESSAGE)));
                }

                result.put(STATUS, status);
                result.put(PTRID, getValueOf(doc, PTRID));
                result.put(SCENARIO_ID, getValueOf(doc, SCENARIO_ID));
                result.put(RUNID, getValueOf(doc, RUNID));
                result.put(ERROR_MESSAGE, getValueOf(doc, ERROR_MESSAGE));
                result.put( FILE, getValueOf(doc, FILE));
                result.put(LOGFILE, getXpathText(doc, xPathFactory, "//logFile[1]/text()"));
                result.put(LOGFILE_1, getXpathText(doc, xPathFactory, "//logFile[2]/text()"));
                result.put(NUMBER_VIOLATIONS, getValueOf(doc, NUMBER_VIOLATIONS));
                
   
                
            } else {
                throw new IllegalArgumentException("Request failed: " + statusCode);
            }

            filePost.releaseConnection();

        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
            iae.initCause(e);
            throw iae;
        } 

        return result;
    } 
    
    public static AngularMomentum doRequestAngMom( String url,  final Part[] parts) {
    	
    	HttpClient client=new HttpClient();
        //Properties props = new Properties();
        //HashMap<String,String> result=new HashMap<String,String>();
        try {
            PostMethod filePost = new PostMethod(url);
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, filePost.getParams())
                    );
            //VegaLog.info(new MultipartRequestEntity(parts, filePost.getParams()).toString());
            HttpMethodParams par = filePost.getParams();
            
            int statusCode = client.executeMethod(filePost);
            if (statusCode == HttpStatus.SC_OK) {

            	VegaLog.info(String.format("HTTP status:"+ filePost.getStatusLine()));

                
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(filePost.getResponseBodyAsStream());
                
                AngularMomentum agMom = AngularMomentum.readFromDoc(doc);
                filePost.releaseConnection();
                return agMom;
 
                
   
                
            } else {
                throw new IllegalArgumentException("Request failed: " + statusCode);
            }

            

        } catch (Exception e) {
            IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
            iae.initCause(e);
            throw iae;
        } 

        
    } 
    

    
    public static void downloadFile(String url,String file) throws IOException{
    	URL website = new URL(url);
    	ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    	FileOutputStream fos = new FileOutputStream(file);
    	fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
    
    private static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc), 
             new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }
    
    
    private static String getValueOf(Document doc, String field) {
        String result = null;
        NodeList nodes = doc.getElementsByTagName(field);
        if (nodes.getLength() == 1) {
            result = nodes.item(0).getTextContent();
        }
        return result;
    }


 
    private static String getXpathText(final Document doc, final XPathFactory xPathFactory, final String xpathStr) {
        String text = null;
        try {
            XPath xpath = xPathFactory.newXPath();
            XPathExpression expr = xpath.compile(xpathStr);
            
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList nodes = (NodeList) result;
            if (nodes.getLength() == 1) {
                text = nodes.item(0).getTextContent();
            } 
        } catch (Exception e) {
            VegaLog.severe("Xpath not available "+ e.getMessage());
        }
        return text;
    }

}
