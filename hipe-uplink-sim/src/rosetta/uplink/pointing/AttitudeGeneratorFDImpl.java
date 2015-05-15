package rosetta.uplink.pointing;

import herschel.share.fltdyn.math.Attitude;
import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.time.*;
import vega.hipe.FileUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.httpclient.HttpClient;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.AttitudeGenerator;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.EvtmEvents.*;
import vega.uplink.pointing.net.AngularMomentum;
import vega.uplink.pointing.net.FDClient;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.SolarAspectAngle;


public class AttitudeGeneratorFDImpl implements AttitudeGenerator {
	HashMap<Long, Quaternion> quaternions;
	Long[] times;
	HashMap<Date,AttitudeConstrainEvent> eventsMap;
	TreeMap<Date,SolarAspectAngle> saaTree;
	TreeMap<Date,HgaOutages> outagesTree;
	AngularMomentum agmom;
	public static long STEP=10000;
	public static boolean DELETE_TEMP_FILES=true;
	private static final Logger LOG = Logger.getLogger(AttitudeGeneratorFDImpl.class.getName());
	
	//Evtm evtm;
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		this(ptr,pdfm,getMtpNum(ptr),eta,zeta,epsilon);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm) throws AttitudeGeneratorException{
			//this(ptr,pdfm,getMtpNum(ptr));
		this(ptr,pdfm,getMtpNum(ptr),null,null,null);
	}
	public static String getMtpNum(Ptr ptr) throws AttitudeGeneratorException{
		try{
			return ptr.getSegments()[0].getName().substring(4);
		}catch (ArrayIndexOutOfBoundsException aiob){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("The PTR does not have any segment",aiob);
			LOG.throwing("AttitudeGeneratorFDImpl", "getMtpNum", newExp);
			throw(newExp);		
		}
		catch (Exception e ){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Can not read the MTP number:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "getMtpNum", newExp);
			throw(newExp);
		}
	}

	public static void getAttitudeFile(Ptr ptr,Pdfm pdfm,String mtpNum,String file,String trajectory) throws AttitudeGeneratorException{
		//String activityCase=trajectory;
		String serverUrl=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl");
		PtrSegment seg = ptr.getSegment("MTP_"+mtpNum);
		String[] includes={"FDDF.xml",pdfm.getName()};
		seg.setIncludes(includes);
		String path = new File(file).getParent();
		PtrUtils.writePTRtofile(path+"/"+ptr.getName(),ptr);
		PtrUtils.writePDFMtofile(path+"/"+pdfm.getName(), pdfm);
		String mission = vega.uplink.Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.mission");
		try {
		String id = FDClient.Ptr2AttRequest(serverUrl, new File(path+"/"+ptr.getName()),  new File(path+"/"+pdfm.getName()), mission, mtpNum, trajectory, "0.0", "0.0", "0.0");
		
		
			LOG.info("Requesting attitude from the FD server");
			String fileUrl = FDClient.Att2AscRequest(serverUrl, id, mission, trajectory);
			FDClient.downloadFile(fileUrl, file);

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting attitude from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "getAttitudeFile", newExp);
			throw(newExp);

		}
		
	}
	public static void getAttitudeFile(Ptr ptr,Pdfm pdfm,String mtpNum,String file) throws AttitudeGeneratorException{
		String activityCase=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase");
		getAttitudeFile(ptr,pdfm,mtpNum,file,activityCase);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,Float eta, Float zeta,Float epsilon) throws AttitudeGeneratorException{
		String activityCase=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase");
		init(ptr,pdfm,mtpNum,activityCase,eta,zeta,epsilon);
	}

	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum) throws AttitudeGeneratorException{
		/*String activityCase=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase");
		init(ptr,pdfm,mtpNum,activityCase);*/
		this(ptr,pdfm,mtpNum,null,null,null);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,activityCase,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		init(ptr,pdfm,mtpNum,activityCase,eta,zeta,epsilon);
	}

	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,activityCase,null,null,null);
		//init(ptr,pdfm,mtpNum,activityCase);
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase) throws AttitudeGeneratorException{
		init (ptr,pdfm,mtpNum,activityCase,null,null,null);
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,ErrorBoxPoint point) throws AttitudeGeneratorException{
		init(ptr,pdfm,mtpNum,activityCase,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		eventsMap=new HashMap<Date,AttitudeConstrainEvent>();
		saaTree=new TreeMap<Date,SolarAspectAngle>();
		outagesTree=new TreeMap<Date,HgaOutages>();
		//String dir = Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir")+"/";
		File directory=null;
		String dir=null;
		try {
			directory = FileUtil.createTempDir("fd_temp_", "fd");
			dir=directory.getAbsolutePath()+"/";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			AttitudeGeneratorException age = new AttitudeGeneratorException("Can not create temp dir to download the fd files",e1);
			LOG.throwing("AttitudeGeneratorFDImpl", "constructor", age);
			throw(age);
			//e1.printStackTrace();
		}
		LOG.info("Using temporal dir for FD files:"+dir);

		//System.out.println(dir);
		//File directory=new File(dir);
		/*if (!directory.isDirectory()){
			directory.mkdir();
		}*/
		//String activityCase=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase");
		String serverUrl=Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl");
		String name=""+new java.util.Date().getTime();
		PtrSegment seg = ptr.getSegment("MTP_"+mtpNum);
		String[] includes={"FDDF.xml",name+"_pdfm.xml"};
		seg.setIncludes(includes);
		PtrUtils.writePTRtofile(dir+name+"_ptr.xml",ptr);
		PtrUtils.writePDFMtofile(dir+name+"_pdfm.xml", pdfm);
		String mission = vega.uplink.Properties.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.mission");

		String id;
		try {
			id = FDClient.Ptr2AttRequest(serverUrl, new File(dir+name+"_ptr.xml"), new File(dir+name+"_pdfm.xml"), mission, mtpNum, activityCase, ""+epsilon, ""+zeta, ""+eta);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting attitude from FD:"+e1.getMessage(),e1);
			throw(newExp);
		}
		try {
			LOG.info("Requesting attitude from the FD server");
			String fileUrl = FDClient.Att2AscRequest(serverUrl, id, mission, activityCase);
			FDClient.downloadFile(fileUrl, dir+name+"_att.txt");
			File out = new File(dir+name+"_att.txt");
			init(dir+name+"_att.txt");
			if (DELETE_TEMP_FILES) out.delete();

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting attitude from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}
		try {
			LOG.info("Requesting attitude constrain checks from the FD server");
			String fileUrl = FDClient.conChkAttRequest(serverUrl, id, mission, activityCase);
			File out = new File(dir+name+"_con.txt");	
			FDClient.downloadFile(fileUrl, dir+name+"_con.txt");
			parseEvtm(dir+name+"_con.txt");
			if (DELETE_TEMP_FILES) out.delete();

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting attitude constrain checks from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}
		try {
			LOG.info("Requesting reaction wheels constrain checks from the FD server");
			String fileUrl = FDClient.conChkRwRequest(serverUrl, id, mission, activityCase);
			File out = new File(dir+name+"_rw.txt");	
			FDClient.downloadFile(fileUrl, dir+name+"_rw.txt");
			parseEvtm(dir+name+"_rw.txt");
			if (DELETE_TEMP_FILES) out.delete();

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting reaction wheels constrain checks from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}
		try {
			LOG.info("Requesting solar aspect angle from the FD server");
			String fileUrl = FDClient.saaRequest(serverUrl, id, mission, activityCase);
			File out = new File(dir+name+"_saa.txt");
			FDClient.downloadFile(fileUrl, dir+name+"_saa.txt");
			parseSaa(dir+name+"_saa.txt");
			if (DELETE_TEMP_FILES) out.delete();

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting solar aspect angle from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}

		try {
			LOG.info("Requesting angular momentum from the FD server");

			agmom=FDClient.angMomentumRequest(serverUrl, id, mission, activityCase);

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting angular momentum from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}

		try {
			LOG.info("Requesting High Gain Antenna coverage from the FD server");
			String fileUrl = FDClient.hgaCovRequest(serverUrl, id, mission, activityCase);
			File out = new File(dir+name+"_hga.txt");	
			FDClient.downloadFile(fileUrl, dir+name+"_hga.txt");
			parseHga(dir+name+"_hga.txt");
			new File(dir+name+"_ptr.xml").delete();
			new File(dir+name+"_pdfm.xml").delete();
			if (DELETE_TEMP_FILES) new File(dir+name+"_ptr.xml").delete();
			if (DELETE_TEMP_FILES) new File(dir+name+"_pdfm.xml").delete();
			if (DELETE_TEMP_FILES) out.delete();

		} catch (Exception e) {
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error getting High Gain Antenna coverage from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "Constructor", newExp);
			throw(newExp);

		}
		FileUtil.delete(directory);

		
	}
	
	public AngularMomentum getAngularMomentum(){
		return agmom;
	}
	
	public AttitudeConstrainEvent[] getAttEvents(){
		AttitudeConstrainEvent[] result=new AttitudeConstrainEvent[eventsMap.size()];
		Iterator<Date> it = eventsMap.keySet().iterator();
		int counter=0;
		while (it.hasNext()){
			Date key=it.next();
			result[counter]=eventsMap.get(key);
			counter++;
		}
		java.util.Arrays.sort(result);
		return result;
	}
	
	public HgaOutages[] getHgaOutages(){
		HgaOutages[] result=new HgaOutages[outagesTree.size()];
		Iterator<Date> it = outagesTree.keySet().iterator();
		int counter=0;
		while (it.hasNext()){
			result[counter]=outagesTree.get(it.next());
			counter++;
		}
		return result;
	}
	
	public SolarAspectAngle getSaa(Date time){
		try{
			return saaTree.floorEntry(time).getValue();
		}catch (Exception e){
			LOG.severe("Earliest date in this dataset "+saaTree.firstEntry().getKey());
			return null;
		}
	}
	
	public SolarAspectAngle[] getAllSaa(){
		SolarAspectAngle[] result=new SolarAspectAngle[saaTree.size()];
		saaTree.values().toArray(result);
		java.util.Arrays.sort(result);
		return result;
	}
	
	protected void parseSaa(String file) throws AttitudeGeneratorException{
		BufferedReader br = null;
		String line = "";
		SimpleTimeFormat timeFormat = new SimpleTimeFormat(TimeScale.TDB);
		timeFormat.setDecimals(3);
		//java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd'T'HH:mm:ss.SSS");
		//java.text.SimpleDateFormat dateFormat2=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		//dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		boolean status = false;
		try {
			 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.startsWith("20")){
						StringTokenizer tokenizer =new StringTokenizer(line);
						Date time=DateUtil.zuluToDateSlash(tokenizer.nextToken());
						float yp=Float.parseFloat(tokenizer.nextToken());
						float yn=Float.parseFloat(tokenizer.nextToken());
						float xa=Float.parseFloat(tokenizer.nextToken());
						float ya=Float.parseFloat(tokenizer.nextToken());
						float za=Float.parseFloat(tokenizer.nextToken());

						SolarAspectAngle saa = new SolarAspectAngle(time,yp,yn,xa,ya,za);
						saaTree.put(time, saa);
				}
				
			}
			br.close();
		}catch (Exception e){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the SAA file from FD:"+e.getMessage(),e);
			LOG.throwing("AttitudeGeneratorFDImpl", "parseSaa", newExp);
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}

	}
	protected void parseHga(String file) throws AttitudeGeneratorException{
		BufferedReader br = null;
		String line = "";
		SimpleTimeFormat timeFormat = new SimpleTimeFormat(TimeScale.TDB);
		timeFormat.setDecimals(3);
		//java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy/MM/dd' 'HH:mm:ss.SSS");
		//dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		boolean status = false;
		try {
			 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer =new StringTokenizer(line);
				if (tokenizer.hasMoreTokens()){
					try{
						int i=Integer.parseInt(tokenizer.nextToken());
						Date startDate=DateUtil.zuluToDateSlashSpace(tokenizer.nextToken()+" "+tokenizer.nextToken());
						Date endDate=DateUtil.zuluToDateSlashSpace(tokenizer.nextToken()+" "+tokenizer.nextToken());
						tokenizer.nextToken();
						tokenizer.nextToken();
						Date fLimit=DateUtil.zuluToDateSlashSpace(tokenizer.nextToken()+" "+tokenizer.nextToken());
						boolean beforeID=true;
						String strategy="";
						String reason="";
						int id=0;
						while (tokenizer.hasMoreTokens()){
							String token=tokenizer.nextToken();
							try{
								id=Integer.parseInt(token);
								beforeID=false;
							}catch (Exception ex){
								if (beforeID){
									strategy=strategy+token+" ";
								}else{
									reason=reason+token+" ";
								}
							}
						}
						outagesTree.put(startDate, new HgaOutages(startDate,endDate,fLimit,strategy,reason,id));
						
						
					}catch(NumberFormatException nfe){
						//The line do not start by a number, just go on
					}
					catch (Exception e){
						AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the HGA file from FD:"+e.getMessage(),e);
						LOG.throwing("AttitudeGeneratorFDImpl", "parseHGA", newExp);
						br.close();
						throw(newExp);

						
	
					}
				}

				
			}
			br.close();
		}catch (Exception e){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the HGA file from FD",e);
			LOG.throwing("AttitudeGeneratorFDImpl", "parseHGA", newExp);
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}

	}
	
	protected  void parseEvtm(String file) throws AttitudeGeneratorException{
		BufferedReader br = null;
		String line = "";
		/*java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));*/
		boolean status = false;
		try {
			 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (status){
					if (line.startsWith("[CON]") || line.startsWith("[VIO]")){
						StringTokenizer tokenizer =new StringTokenizer(line);
						String con=tokenizer.nextToken();
						String number=tokenizer.nextToken();
						Date startTime=DateUtil.zuluToDate(tokenizer.nextToken());
						Date endTime=DateUtil.zuluToDate(tokenizer.nextToken());
						float min=Float.parseFloat(tokenizer.nextToken());
						float max=Float.parseFloat(tokenizer.nextToken());
						String mess="";
						while (tokenizer.hasMoreTokens()){
							mess=mess+tokenizer.nextToken();
							if (tokenizer.hasMoreTokens()) mess=mess+" ";
						}
						AttitudeConstrainEvent event = new AttitudeConstrainEvent(con,Integer.parseInt(number),startTime,endTime,min,max,mess);
						eventsMap.put(startTime, event);
						//System.out.println(event);
					}
				}
				if (line.startsWith("STATUS")){
					status=true;
				}else{
					
				}
				
			}
			br.close();
		}catch (Exception e){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the attitude constrains file from FD",e);
			LOG.throwing("AttitudeGeneratorFDImpl", "parseEVTM", newExp);
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}
		
	}
	
	protected void init(String file) throws AttitudeGeneratorException{
		BufferedReader br = null;
		String line = "";
		/*java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));*/
		quaternions=new HashMap<Long, Quaternion>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("20")){
				}else{
					/*SimpleTimeFormat tdb = new SimpleTimeFormat(TimeScale.TDB);
					tdb.setDecimals(6);
					FineTime ftime = tdb.parse(line.substring(0, 26)+" TDB");
					Date dt = ftime.toDate();*/
					Date dt =DateUtil.TDBToDate(line.substring(0, 26));
					float ax1=Float.parseFloat(line.substring(29, 49)+"E"+line.substring(50, 53));
					float ax2=Float.parseFloat(line.substring(54, 74)+"E"+line.substring(75, 78));
					float ax3=Float.parseFloat(line.substring(79, 99)+"E"+line.substring(100, 103));
					float ag=Float.parseFloat(line.substring(104, 124)+"E"+line.substring(125, 128));
					Quaternion q=new Quaternion(ax1,ax2,ax3,ag);
					quaternions.put(new Long(dt.getTime()), q);
					
				}
				
			}
			br.close();
		}catch (Exception e){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the attitude file from FD",e);
			LOG.throwing("AttitudeGeneratorFDImpl", "init", newExp);
			try {
				br.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}
		times=new Long[quaternions.size()];
		quaternions.keySet().toArray(times);
	}
	
	private int[] find(Long[] array,int loc1,int loc2,Long nToFind){
		int[] result=new int[2];
		result[0]=0;
		result[1]=0;
		if (nToFind>=array[loc1]){
			if (nToFind<=array[loc2]){
				if (loc2==loc1+1){
					result[0]=loc1;
					result[1]=loc2;
					return result;
				}
				result=find(array,loc1,loc1+((loc2-loc1)/2),nToFind);
				if (result[0]==0){
					return find(array,loc1+((loc2-loc1)/2),loc2,nToFind);
				}
				else return result;
			}
		}
		return result;
	}
	
	private double compare(Quaternion q1,Quaternion q2){
		return q1.normalizeSign().conjugate().multiply(q2.normalizeSign()).angle();
	}
	
	private Quaternion interpolate(Quaternion q1,java.util.Date date1,Quaternion q2,java.util.Date date2,java.util.Date desiredDate){
		if (date1.equals(date2)) return q1;
		long time1=date1.getTime();
		long time2=date2.getTime();
		long time=desiredDate.getTime();
		//long fac=1/(time2-time1);
		//double delta=time*fac;
		double delta=(time-time1)/(time2-time1);
		if (compare(q1,q2)<compare(q1,q2.conjugate())){
			return q1.slerp(q2, delta);
		}
		else{
			return q1.conjugate().slerp(q2, delta);
		}
	}
	
	public Quaternion getQuaternion(java.util.Date date){
		long time=date.getTime();
		if (time>times[times.length-1]) return quaternions.get(times[times.length-1]);
		int[] r=find(times,0,times.length-1,time);
		return interpolate(quaternions.get(times[r[0]]),new java.util.Date(times[r[0]]),quaternions.get(times[r[1]]),new java.util.Date(times[r[1]]),date);
		
	}

	@Override
	public HashMap<Long, Quaternion> getQuaternions(PointingBlock block) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		long time1=block.getStartTime().getTime();
		long time2=block.getEndTime().getTime();
		for (long i=time1;i<time2;i=i+STEP){
			result.put(i, getQuaternion(new java.util.Date(i)));
		}
		
		return result;
	}

	@Override
	public HashMap<Long, Quaternion> getQuaternions(PtrSegment segment) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		PointingBlock[] blocks=segment.getBlocks();
		for (int i=0;i<blocks.length;i++){
			result.putAll(getQuaternions(blocks[i]));
		}
		return result;
	}
	public HashMap<Long, Quaternion> getAllQuaternions(){
		return quaternions;
	}
	@Override
	public HashMap<Long, Quaternion> getQuaternions(Ptr ptr) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		PtrSegment[] segments=ptr.getSegments();
		for (int i=0;i<segments.length;i++){
			result.putAll(getQuaternions(segments[i]));
		}
		
		return result;
	}
	
	public SolarAspectAngle[] getSaa(Date fromTime, Date toTime){
		SortedMap<Date, SolarAspectAngle> submap = saaTree.subMap(fromTime, toTime);
		SolarAspectAngle[] result=new SolarAspectAngle[submap.size()];
		submap.values().toArray(result);
		java.util.Arrays.sort(result);
		return result;
	
	}
	@Override
	public SolarAspectAngle[] getSaa(PointingBlock block) {
		return getSaa(block.getStartTime(),block.getEndTime());
	}
	@Override
	public SolarAspectAngle[] getSaa(PtrSegment segment) {
		// TODO Auto-generated method stub
		return getSaa(segment.getStartDate().toDate(),segment.getEndDate().toDate());
	}
	@Override
	public SolarAspectAngle[] getSaa(Ptr ptr) {
		// TODO Auto-generated method stub
		return getSaa(ptr.getStartDate().toDate(),ptr.getEndDate().toDate());
	}
	
	public Evtm getEvtm(){
		RosettaDistance distance=RosettaDistance.getInstance();
		Evtm result=new Evtm();
		EvtmEventBdi ALIS=null;
		EvtmEventBdi CSIS=null;
		EvtmEventBdi MRIS=null;
		EvtmEventBdi SRHS=null;
		EvtmEventBdi SRIS=null;
		EvtmEventBdi VRIS=null;
		EvtmEventBdi LDIS=null;
		EvtmEventBdi Z02S=null;
		EvtmEventBdi Z18S=null;
		EvtmEventBdi Z14S=null;
		EvtmEventBdi Z17S=null;
		EvtmEventBdi Z19S=null;
		EvtmEventBdi YPIS=null;
		EvtmEventBdi YP3S=null;
		EvtmEventBdi YP4S=null;
		EvtmEventBdi YMIS=null;
		EvtmEventBdi YM3S=null;
		EvtmEventBdi YM4S=null;
		SolarAspectAngle[] saaArray = getAllSaa();
		if (saaArray.length==0) return result;
		result.setValidityStart(saaArray[0].getTime());
		result.setValidityEnd(saaArray[saaArray.length-1].getTime());
		for (int i=0;i<saaArray.length;i++){
			if (saaArray[i].getSCAnglePositiveZAxis()<11){
				if (ALIS==null) ALIS=new EvtmEventBdi( "ALIS", saaArray[i].getTime(), 0,0);
				if (SRHS==null) SRHS=new EvtmEventBdi( "SRHS", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>11){
				if (ALIS!=null){
					EvtmEventBdi ALIE=new EvtmEventBdi( "ALIE", saaArray[i].getTime(), 0,0);
					long duration=(ALIE.getTime().getTime()-ALIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("ALIS",ALIS.getTime(),duration,0));
					result.addEvent(ALIE);
					ALIS=null;
				}
				if (SRHS!=null){
					EvtmEventBdi SRHE=new EvtmEventBdi( "SRHE", saaArray[i].getTime(), 0,0);
					long duration=(SRHE.getTime().getTime()-SRHS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("SRHS",SRHS.getTime(),duration,0));
					result.addEvent(SRHE);
					SRHS=null;
				}
			}
			
			if (saaArray[i].getSCAnglePositiveZAxis()<5){
				if (MRIS==null) MRIS=new EvtmEventBdi( "MRIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>5){
				if (MRIS!=null){
					EvtmEventBdi MRIE=new EvtmEventBdi( "MRIE", saaArray[i].getTime(), 0,0);
					long duration=(MRIE.getTime().getTime()-MRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("MRIS",MRIS.getTime(),duration,0));
					result.addEvent(MRIE);
					MRIS=null;

				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<20){
				if (SRIS==null) SRIS=new EvtmEventBdi( "SRIS", saaArray[i].getTime(), 0,0);
				if (Z02S==null) Z02S=new EvtmEventBdi( "Z02S", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>20){
				if (SRIS!=null){
					EvtmEventBdi SRIE=new EvtmEventBdi( "SRIE", saaArray[i].getTime(), 0,0);
					long duration=(SRIE.getTime().getTime()-SRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("SRIS",SRIS.getTime(),duration,0));
					result.addEvent(SRIE);
					SRIS=null;
				}
				if (Z02S!=null){
					EvtmEventBdi Z02E=new EvtmEventBdi( "Z02E", saaArray[i].getTime(), 0,0);
					long duration=(Z02E.getTime().getTime()-Z02S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z02S",Z02S.getTime(),duration,0));
					result.addEvent(Z02E);
					Z02S=null;
				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<10){
				if (VRIS==null) VRIS=new EvtmEventBdi( "VRIS", saaArray[i].getTime(), 0,0);
				if (LDIS==null) LDIS=new EvtmEventBdi( "LDIS", saaArray[i].getTime(), 0,0);				
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>10){
				if (VRIS!=null){
					EvtmEventBdi VRIE=new EvtmEventBdi( "VRIE", saaArray[i].getTime(), 0,0);
					long duration=(VRIE.getTime().getTime()-VRIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("VRIS",VRIS.getTime(),duration,0));
					result.addEvent(VRIE);
					VRIS=null;
				}
				if (LDIS!=null){
					EvtmEventBdi LDIE=new EvtmEventBdi( "LDIE", saaArray[i].getTime(), 0,0);
					long duration=(LDIE.getTime().getTime()-LDIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("LDIS",LDIS.getTime(),duration,0));
					result.addEvent(LDIE);
					LDIS=null;
				}
			}
			if (saaArray[i].getSCAnglePositiveZAxis()>180){
				if (Z18S==null) Z18S=new EvtmEventBdi( "Z18S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<180){
				if (Z18S!=null){
					EvtmEventBdi Z18E=new EvtmEventBdi( "Z18E", saaArray[i].getTime(), 0,0);
					long duration=(Z18E.getTime().getTime()-Z18S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z18S",Z18S.getTime(),duration,0));
					result.addEvent(Z18E);
					Z18S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>140 && distance.getDistanceSun(saaArray[i].getTime())<1.20){
				if (Z14S==null) Z14S=new EvtmEventBdi( "Z14S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<140 && distance.getDistanceSun(saaArray[i].getTime())<1.20){
				if (Z14S!=null){
					EvtmEventBdi Z14E=new EvtmEventBdi( "Z14E", saaArray[i].getTime(), 0,0);
					long duration=(Z14E.getTime().getTime()-Z14S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z14S",Z14S.getTime(),duration,0));
					result.addEvent(Z14E);
					Z14S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>175 && distance.getDistanceSun(saaArray[i].getTime())<2.20 ){
				if (Z17S==null) Z17S=new EvtmEventBdi( "Z17S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<175 && distance.getDistanceSun(saaArray[i].getTime())<2.20){
				if (Z17S!=null){
					EvtmEventBdi Z17E=new EvtmEventBdi( "Z17E", saaArray[i].getTime(), 0,0);
					long duration=(Z17E.getTime().getTime()-Z17S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z17S",Z17S.getTime(),duration,0));
					result.addEvent(Z17E);
					Z17S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveZAxis()>192 && distance.getDistanceSun(saaArray[i].getTime())<2.21){
				if (Z19S==null) Z19S=new EvtmEventBdi( "Z19S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveZAxis()<192 && distance.getDistanceSun(saaArray[i].getTime())<2.21){
				if (Z19S!=null){
					EvtmEventBdi Z19E=new EvtmEventBdi( "Z19E", saaArray[i].getTime(), 0,0);
					long duration=(Z19E.getTime().getTime()-Z19S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("Z19S",Z19S.getTime(),duration,0));
					result.addEvent(Z19E);
					Z19S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>5){
				if (YPIS==null) YPIS=new EvtmEventBdi( "YPIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<5){
				if (YPIS!=null){
					EvtmEventBdi YPIE=new EvtmEventBdi( "YPIE", saaArray[i].getTime(), 0,0);
					long duration=(YPIE.getTime().getTime()-YPIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YPIS",YPIS.getTime(),duration,0));
					result.addEvent(YPIE);
					YPIS=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>30){
				if (YP3S==null) YP3S=new EvtmEventBdi( "YP3S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<30){
				if (YP3S!=null){
					EvtmEventBdi YP3E=new EvtmEventBdi( "YP3E", saaArray[i].getTime(), 0,0);
					long duration=(YP3E.getTime().getTime()-YP3S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YP3S",YP3S.getTime(),duration,0));
					result.addEvent(YP3E);
					YP3S=null;
				}
			}

			if (saaArray[i].getSCAnglePositiveYAxis()>40){
				if (YP4S==null) YP4S=new EvtmEventBdi( "YP4S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAnglePositiveYAxis()<40){
				if (YP4S!=null){
					EvtmEventBdi YP4E=new EvtmEventBdi( "YP4E", saaArray[i].getTime(), 0,0);
					long duration=(YP4E.getTime().getTime()-YP4S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YP4S",YP4S.getTime(),duration,0));
					result.addEvent(YP4E);
					YP4S=null;
				}
			}
			
			if (saaArray[i].getSCAngleNegativeYAxis()>5){
				if (YMIS==null) YMIS=new EvtmEventBdi( "YMIS", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<5){
				if (YMIS!=null){
					EvtmEventBdi YMIE=new EvtmEventBdi( "YMIE", saaArray[i].getTime(), 0,0);
					long duration=(YMIE.getTime().getTime()-YMIS.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YMIS",YPIS.getTime(),duration,0));
					result.addEvent(YMIE);
					YMIS=null;
				}
			}

			if (saaArray[i].getSCAngleNegativeYAxis()>30){
				if (YM3S==null) YM3S=new EvtmEventBdi( "YM3S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<30){
				if (YP3S!=null){
					EvtmEventBdi YM3E=new EvtmEventBdi( "YM3E", saaArray[i].getTime(), 0,0);
					long duration=0;
					if (YM3E!=null) duration=(YM3E.getTime().getTime()-YM3S.getTime().getTime())/1000;					
					result.addEvent(new EvtmEventBdi("YM3S",YM3S.getTime(),duration,0));
					result.addEvent(YM3E);
					YM3S=null;
				}
			}

			if (saaArray[i].getSCAngleNegativeYAxis()>40){
				if (YM4S==null) YM4S=new EvtmEventBdi( "YM4S", saaArray[i].getTime(), 0,0);
			}
			if (saaArray[i].getSCAngleNegativeYAxis()<40){
				if (YM4S!=null){
					EvtmEventBdi YM4E=new EvtmEventBdi( "YM4E", saaArray[i].getTime(), 0,0);
					long duration=(YM4E.getTime().getTime()-YM4S.getTime().getTime())/1000;
					result.addEvent(new EvtmEventBdi("YM4S",YM4S.getTime(),duration,0));
					result.addEvent(YM4E);
					YM4S=null;
				}
			}

			
		}
		
		return result;
	}
	@Override
	public Attitude getAttitude(Date date) {
		return new Attitude(getQuaternion(date));
	}
	@Override
	public Map<Long, Attitude> getAttitudes(PointingBlock block) {
		return fromQuaternions(getQuaternions(block));
	}
	@Override
	public Map<Long, Attitude> getAttitudes(PtrSegment segment) {
		return fromQuaternions(getQuaternions(segment));
	}
	@Override
	public Map<Long, Attitude> getAttitudes(Ptr ptr) {
		return fromQuaternions(getQuaternions(ptr));
	}
	
	private Map<Long,Attitude> fromQuaternions(Map<Long,Quaternion> quaternions){
		HashMap<Long,Attitude> result= new HashMap<Long,Attitude>();
		Iterator<Entry<Long, Quaternion>> it = quaternions.entrySet().iterator();
		while (it.hasNext()){
			Entry<Long, Quaternion> entry = it.next();
			result.put(entry.getKey(), new Attitude(entry.getValue()));
		}
		
		return result;
	}
	
	
	 

}