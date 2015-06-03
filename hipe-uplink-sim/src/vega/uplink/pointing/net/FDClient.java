package vega.uplink.pointing.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
//import java.io.OutputStream;
//import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
//import java.util.Iterator;















import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerException;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
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
//import org.apache.commons.httpclient.params.HttpMethodParams;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import vega.hipe.logging.VegaLog;
import vega.uplink.Properties;

/**
 * Class to connect to to Flight Dynamic web server following ROS-SGS-FD-SW-ICD (ROS-ESC-IF-5504)
 * @author jarenas
 *
 */
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
	private static String latst_ptrId=null;
	private static long TIMEOUT_1=60000;
	private static long TIMEOUT_2=300000;
	private static long TIMEOUT_3=900000;
	public static String TIMEOUT_1_PROPERTY="vega.uplink.pointing.net.timeout_1";
	public static String TIMEOUT_2_PROPERTY="vega.uplink.pointing.net.timeout_2";
	public static String TIMEOUT_3_PROPERTY="vega.uplink.pointing.net.timeout_3";
	 
	
	/**
	 * vega.uplink.pointing.net.serverUrl
	 */
	public static String FD_SERVER_URL_PROPERTY="vega.uplink.pointing.net.serverUrl";
	/**
	 * vega.uplink.pointing.net.mission
	 */
	public static String MISSION_ID_PROPERTY="vega.uplink.pointing.net.mission";
	/**
	 * vega.uplink.pointing.net.activityCase
	 */
	public static String TRAJECTORY_PROPERTY="vega.uplink.pointing.net.activityCase";

	/**
	 * vega.uplink.pointing.net.trajectories
	 */
	public static String TREJECTORIES_PROPERTY="vega.uplink.pointing.net.trajectories";
	/**
	 * Request the quaternion file from the FD server.
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file
	 * the scenario id to be used is the last one obtained from FD server
	 * @return The URL of the quaternion file
	 */
	public static String Att2AscRequest(){
		return Att2AscRequest(null,null,null,null);
	}
	/**
	 * Request the quaternion file from the FD server
	 * @param url URL of the FD server
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return The URL of the quaternion file
	 */
	public static String Att2AscRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}
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
	/**
	 * Initialize service by submitting the PTR to FD server.
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * epsilon,zeta, eta used is point 0.0,0.0,0.0
	 * 
	 * @param ptrFile The PTR file to init the service
	 * @param pdfmFile The PDFM file to init the service
	 * @param mtpNum The MTP number that is defined in the PTR
	 * @return The scenario id to be used in the following requests
	 * @throws FileNotFoundException
	 */
	public static String Ptr2AttRequest(File ptrFile,File pdfmFile,String mtpNum) throws FileNotFoundException{
		return Ptr2AttRequest(null,ptrFile,pdfmFile,null,mtpNum,null,null,null,null);
	}

	/**
	 * Initialize service by submitting the PTR to FD server
	 * @param url URL of FD server
	 * @param ptrFile The PTR file to init the service
	 * @param pdfmFile The PDFM file to init the service
	 * @param mission The mission identifier (ROS for Rosetta)
	 * @param mtpNum The MTP number that is defined in the PTR
	 * @param trajectory Trajectory to use
	 * @param epsilon Epsilon error box point
	 * @param zeta Zeta error box point
	 * @param eta Eta error box point
	 * @return The scenario id to be used in the following requests
	 * @throws FileNotFoundException If either the PTR or or the PDFM were not found
	 */
	public static String Ptr2AttRequest(String url,File ptrFile,File pdfmFile,String mission,String mtpNum,String trajectory,String epsilon,String zeta,String eta) throws FileNotFoundException{
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (epsilon==null){
			epsilon="0.0";
		}
		if (zeta==null){
			zeta="0.0";
		}
		if (eta==null){
			eta="0.0";
		}

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
		latst_ptrId=map.get(SCENARIO_ID);
		return latst_ptrId;

	}
	public static void main(String[] args){
			String url="http://rostasc01.n1data.lan/roscmd-cgi-bin/seqgen.pl";
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
	/**
	 * Get the attitude constrains check file from FD server
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * the scenario id to be used is the last one obtained from FD server
	 * 
	 * @return The URL of the attitude constrains check file file
	 */
	public static String conChkAttRequest(){
		return conChkAttRequest(null,null,null,null);
	}
	
	/**
	 * Get the attitude constrains check file from FD server
	 * @param url FD server url
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return The URL of the attitude constrains check file file
	 */
	public static String conChkAttRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}


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
	/**
	 * Get the High Gain Antenna coverage file
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * the scenario id to be used is the last one obtained from FD server
	 * 
	 * @return The URL of the HGA coverage file
	 */
	public static String hgaCovRequest(){
		return hgaCovRequest(null,null,null,null);
	}

	/**
	 * Get the High Gain Antenna coverage file
	 * @param url URL of the FD server
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return The URL of the HGA coverage file
	 */
	public static String hgaCovRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}


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
	
	/**
	 * Get the Solar Aspect Angle file form FD server
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * the scenario id to be used is the last one obtained from FD server
	 * 
	 * @return The URL of the SAA file
	 */
	public static String saaRequest(){
		return saaRequest(null,null,null,null);
	}

		
	
	/**
	 * Get the Solar Aspect Angle file form FD server
	 * @param url URL of the FD server
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return The URL of the SAA file
	 */
	public static String saaRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}


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
	
	/**
	 * Get the reaction wheels constrains file from FD server 
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * the scenario id to be used is the last one obtained from FD server
	 * 
	 * @return The URL of the reaction wheels constrains file
	 */
	public static String conChkRwRequest(){
		return conChkRwRequest(null,null,null,null);
	}

	
	/**
	 * Get the reaction wheels constrains file from FD server 
	 * @param url URL of the FD server
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return The URL of the reaction wheels constrains file
	 */
	public static String conChkRwRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}


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

	/**
	 * Get the angular momentum from FD server
	 * the URL of FD server, the mission identifier and the trajectory must be defined in the properties file.
	 * the scenario id to be used is the last one obtained from FD server
	 * 
	 * @return
	 */
	public static AngularMomentum angMomentumRequest(){
		return angMomentumRequest(null,null,null,null);
	}

		
	/**
	 * Get the angular momentum from FD server
	 * @param url URL of the FD server
	 * @param ptrId The scenario id (as returned by Ptr2AttRequest method)
	 * @param mission Mission identifier (ROS for Rosetta)
	 * @param trajectory Trajectory to be used
	 * @return Angular Momentum
	 */
	public static AngularMomentum angMomentumRequest(String url,String ptrId,String mission,String trajectory){
		if (url==null){
			url=Properties.getProperty(FD_SERVER_URL_PROPERTY);

		}
		if (mission==null){
			mission=Properties.getProperty(MISSION_ID_PROPERTY);
		}
		if (trajectory==null){
			trajectory=Properties.getProperty(TRAJECTORY_PROPERTY);
		}
		if (ptrId==null){
			ptrId=latst_ptrId;
		}


		Part[] parts = new Part[5];

	                parts[0]=new StringPart("mission", mission);
	                parts[1]=new StringPart(SCENARIO_ID, ptrId);
	                parts[2]=new StringPart(QUERY_TYPE,"run");
	                parts[3]=new StringPart(SERVICE,"ANGMOM");
	                parts[4]=new StringPart(OPS, trajectory);
	                       
	       

	                AngularMomentum result = doRequestAngMom(url,parts);
	                
	        return result;
	}

    private static HashMap<String,String> doRequest( String url,  final Part[] parts) {

    	HttpClient client=new HttpClient();
        //Properties props = new Properties();
        HashMap<String,String> result=new HashMap<String,String>();
        try {
            PostMethod filePost = new PostMethod(url);
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, filePost.getParams())
                    );
            //VegaLog.info(new MultipartRequestEntity(parts, filePost.getParams()).toString());
            //HttpMethodParams par = filePost.getParams();
            
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
    
    private static AngularMomentum doRequestAngMom( String url,  final Part[] parts) {
    	
    	HttpClient client=new HttpClient();
        //Properties props = new Properties();
        //HashMap<String,String> result=new HashMap<String,String>();
        try {
            PostMethod filePost = new PostMethod(url);
            filePost.setRequestEntity(
                    new MultipartRequestEntity(parts, filePost.getParams())
                    );
            //VegaLog.info(new MultipartRequestEntity(parts, filePost.getParams()).toString());
            //HttpMethodParams par = filePost.getParams();
            
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
    	try{
    		TIMEOUT_1=Long.parseLong(Properties.getProperty(TIMEOUT_1_PROPERTY));
    		TIMEOUT_2=Long.parseLong(Properties.getProperty(TIMEOUT_1_PROPERTY));
    		TIMEOUT_3=Long.parseLong(Properties.getProperty(TIMEOUT_1_PROPERTY));

    		
    	}catch (Exception e){
    		vega.hipe.logging.VegaLog.info("Could not get time out for fd connection from preferences. Using default.");
    	}
    	try{
    		downloadFileRaw(url,file);
    	}catch (Exception e){
    		
    			vega.hipe.logging.VegaLog.info("Could not get "+url+" "+e.getMessage());
				try{
					vega.hipe.logging.VegaLog.info("waiting  "+TIMEOUT_1+" ms");
					Thread.sleep(TIMEOUT_1);
					downloadFileRaw(url,file);
				}catch (Exception e2){
					try{
						vega.hipe.logging.VegaLog.info("waiting  "+TIMEOUT_2+" ms");
						Thread.sleep(TIMEOUT_2);
						downloadFileRaw(url,file);
					}catch (Exception e3){
						try{
							vega.hipe.logging.VegaLog.info("waiting  "+TIMEOUT_3+" ms");
							Thread.sleep(TIMEOUT_3);
							downloadFileRaw(url,file);
						}catch (Exception e4){
							IOException ioe = new IOException(e4.getMessage());
							ioe.initCause(e4);
						}
					}
				}
			
    		
    	}
    }
    
    /**
     * Download a file from a URL
     * @param url URl of the file to be downloaded
     * @param file The full path where the file will be saved
     * @throws IOException
     */
    public static void downloadFileRaw(String url,String file) throws IOException{
    	disableSslVerification();
    	String newurl=url;
    	try{
    		newurl=checkURL(url);
    	}catch (Exception e){
    		IOException ioe = new IOException(e.getMessage());
    		ioe.initCause(e);
    		throw(ioe);
    	}
    	URL website = new URL(newurl);
    	ReadableByteChannel rbc = Channels.newChannel(website.openStream());
    	FileOutputStream fos = new FileOutputStream(file);
    	fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    	fos.close();
    }
    
    private static String checkURL(String url) throws IOException{
    	URL obj = new URL(url);
    	HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
    	conn.setReadTimeout(5000);
    	conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
    	conn.addRequestProperty("User-Agent", "Mozilla");
    	conn.addRequestProperty("Referer", "esa.int");
     
    	vega.hipe.logging.VegaLog.info("Request URL ... " + url);
     
    	boolean redirect = false;
     
    	// normally, 3xx is redirect
    	int status = conn.getResponseCode();
    	if (status != HttpURLConnection.HTTP_OK) {
    		if (status == HttpURLConnection.HTTP_MOVED_TEMP
    			|| status == HttpURLConnection.HTTP_MOVED_PERM
    				|| status == HttpURLConnection.HTTP_SEE_OTHER)
    		redirect = true;
    	}
     
    	vega.hipe.logging.VegaLog.info("Response Code ... " + status);
     
    	if (redirect) {
     
    		// get redirect url from "location" header field
    		String newUrl = conn.getHeaderField("Location");
     
    		// get the cookie if need, for login
    		String cookies = conn.getHeaderField("Set-Cookie");
     
    		// open the new connnection again
    		conn = (HttpURLConnection) new URL(newUrl).openConnection();
    		conn.setRequestProperty("Cookie", cookies);
    		conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
    		conn.addRequestProperty("User-Agent", "Mozilla");
    		conn.addRequestProperty("Referer", "google.com");
     
    		vega.hipe.logging.VegaLog.info("Redirect to URL : " + newUrl);
    		return newUrl;
    	}else{
    		return url;
    	}
    }
    
    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }

				
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
    }


    
    /*private static void printDocument(Document doc, OutputStream out) throws IOException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

        transformer.transform(new DOMSource(doc), 
             new StreamResult(new OutputStreamWriter(out, "UTF-8")));
    }*/
    
    
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
