package vega.uplink.pointing.net;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import java.util.SortedMap;
import java.util.StringTokenizer;
import java.util.TreeMap;

import herschel.share.fltdyn.math.Attitude;
import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.time.*;

import vega.hipe.FileUtil;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.AttitudeGenerator;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.AttitudeMap;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.SolarAspectAngle;
import vega.uplink.track.Fecs;


public class AttitudeGeneratorFDImpl implements AttitudeGenerator {
	AttitudeMap quaternions;
	Long[] times;
	HashMap<Date,AttitudeConstrainEvent> eventsMap;
	TreeMap<Date,SolarAspectAngle> saaTree;
	TreeMap<Date,HgaOutages> outagesTree;
	AngularMomentum agmom;
	public static long STEP=10000;
	public static boolean DELETE_TEMP_FILES=true;
	private static final Logger LOG = Logger.getLogger(AttitudeGeneratorFDImpl.class.getName());
	private String trajectory;

	public String getTrajectory(){
		return trajectory;
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		this(ptr,pdfm,getMtpNum(ptr),eta,zeta,epsilon);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm) throws AttitudeGeneratorException{
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
	public static AttitudeMap getAttitudeMap(Ptr ptr,Pdfm pdfm,String mtpNum,String trajectory) throws AttitudeGeneratorException{
		File directory=null;
		String dir=null;
		try {
			directory = FileUtil.createTempDir("fd_temp_", "fd");
			dir=directory.getAbsolutePath()+"/";
		} catch (IOException e1) {
			AttitudeGeneratorException age = new AttitudeGeneratorException("Can not create temp dir to download the fd files",e1);
			LOG.throwing("AttitudeGeneratorFDImpl", "constructor", age);
			throw(age);
		}
		String name=""+new java.util.Date().getTime();
		File out = new File(dir+name+"_att.txt");
		getAttitudeFile(ptr,pdfm,mtpNum,out.getAbsolutePath(),trajectory);
		return readAttFile(out.getAbsolutePath());
	}

	public static void getAttitudeFile(Ptr ptr,Pdfm pdfm,String mtpNum,String file,String trajectory) throws AttitudeGeneratorException{
		String serverUrl=Properties.getProperty(FDClient.FD_SERVER_URL_PROPERTY);
		PtrSegment seg = ptr.getSegment("MTP_"+mtpNum);
		String[] includes={"FDDF.xml",pdfm.getName()};
		seg.setIncludes(includes);
		String path = new File(file).getParent();
		PtrUtils.writePTRtofile(path+"/"+ptr.getName(),ptr);
		PtrUtils.writePDFMtofile(path+"/"+pdfm.getName(), pdfm);
		String mission = vega.uplink.Properties.getProperty(FDClient.MISSION_ID_PROPERTY);
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
		String activityCase=Properties.getProperty(FDClient.TRAJECTORY_PROPERTY);
		getAttitudeFile(ptr,pdfm,mtpNum,file,activityCase);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,Float eta, Float zeta,Float epsilon) throws AttitudeGeneratorException{
		String activityCase=Properties.getProperty(FDClient.TRAJECTORY_PROPERTY);
		init(ptr,pdfm,mtpNum,activityCase,eta,zeta,epsilon);
	}

	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,null,null,null);
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,ErrorBoxPoint point) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,activityCase,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		init(ptr,pdfm,mtpNum,activityCase,eta,zeta,epsilon);
	}

	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase) throws AttitudeGeneratorException{
		this(ptr,pdfm,mtpNum,activityCase,new ErrorBoxPoint(0.0f,0.0f,0.0f));
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase) throws AttitudeGeneratorException{
		init (ptr,pdfm,mtpNum,activityCase,null,null,null);
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,ErrorBoxPoint point) throws AttitudeGeneratorException{
		init(ptr,pdfm,mtpNum,activityCase,point.getEta(),point.getZeta(),point.getEpsilon());
	}
	public void init(Ptr ptr,Pdfm pdfm,String mtpNum,String activityCase,Float eta,Float zeta,Float epsilon) throws AttitudeGeneratorException{
		trajectory=activityCase;
		eventsMap=new HashMap<Date,AttitudeConstrainEvent>();
		saaTree=new TreeMap<Date,SolarAspectAngle>();
		outagesTree=new TreeMap<Date,HgaOutages>();
		File directory=null;
		String dir=null;
		try {
			directory = FileUtil.createTempDir("fd_temp_", "fd");
			dir=directory.getAbsolutePath()+"/";
		} catch (IOException e1) {
			AttitudeGeneratorException age = new AttitudeGeneratorException("Can not create temp dir to download the fd files",e1);
			LOG.throwing("AttitudeGeneratorFDImpl", "constructor", age);
			throw(age);
		}
		LOG.info("Using temporal dir for FD files:"+dir);

		String serverUrl=Properties.getProperty(FDClient.FD_SERVER_URL_PROPERTY);
		String name=""+new java.util.Date().getTime();
		PtrSegment seg = ptr.getSegment("MTP_"+mtpNum);
		String[] includes={"FDDF.xml",name+"_pdfm.xml"};
		seg.setIncludes(includes);
		PtrUtils.writePTRtofile(dir+name+"_ptr.xml",ptr);
		PtrUtils.writePDFMtofile(dir+name+"_pdfm.xml", pdfm);
		String mission = vega.uplink.Properties.getProperty(FDClient.MISSION_ID_PROPERTY);

		String id;
		try {
			id = FDClient.Ptr2AttRequest(serverUrl, new File(dir+name+"_ptr.xml"), new File(dir+name+"_pdfm.xml"), mission, mtpNum, activityCase, ""+epsilon, ""+zeta, ""+eta);
		} catch (FileNotFoundException e1) {
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
		try {
			 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				StringTokenizer tokenizer =new StringTokenizer(line);
				if (tokenizer.hasMoreTokens()){
					try{
						int number=Integer.parseInt(tokenizer.nextToken());
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
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}
		
	}
	protected void init(String file) throws AttitudeGeneratorException{
		quaternions=readAttFile(file);
		times=new Long[quaternions.size()];
		quaternions.keySet().toArray(times);	
	}
	protected static AttitudeMap  readAttFile(String file) throws AttitudeGeneratorException{
		BufferedReader br = null;
		String line = "";
		AttitudeMap result = new AttitudeMap();
	 
		try {
	 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("20")){
				}else{
					Date dt =DateUtil.TDBToDate(line.substring(0, 26));
					float ax1=Float.parseFloat(line.substring(29, 49)+"E"+line.substring(50, 53));
					float ax2=Float.parseFloat(line.substring(54, 74)+"E"+line.substring(75, 78));
					float ax3=Float.parseFloat(line.substring(79, 99)+"E"+line.substring(100, 103));
					float ag=Float.parseFloat(line.substring(104, 124)+"E"+line.substring(125, 128));
					Quaternion q=new Quaternion(ax1,ax2,ax3,ag).normalizeSign();
					result.put(dt, q);
					
				}
				
			}
			br.close();
		}catch (Exception e){
			AttitudeGeneratorException newExp = new AttitudeGeneratorException ("Error parsing the attitude file from FD",e);
			LOG.throwing("AttitudeGeneratorFDImpl", "init", newExp);
			try {
				if (br!=null) br.close();
			} catch (IOException e1) {
				AttitudeGeneratorException newExp2 = new AttitudeGeneratorException ("Error closing file",e);
				newExp.initCause(newExp2);
				e1.printStackTrace();
			}
			throw(newExp);

		}
		return result;
	}
	

	
	
	public Quaternion getQuaternion(java.util.Date date){
		return quaternions.get(date);
	}

	@Override
	public AttitudeMap getQuaternions(PointingBlock block) {
		return quaternions.subMap(block.getStartTime(), block.getEndTime());
	}

	@Override
	public AttitudeMap getQuaternions(PtrSegment segment) {
		return quaternions.subMap(segment.getSegmentStartDate(), segment.getSegmentEndDate());
	}
	public AttitudeMap getAllQuaternions(){
		return quaternions;
	}
	@Override
	public AttitudeMap getQuaternions(Ptr ptr) {
		return quaternions.subMap(ptr.getStartDate().toDate(), ptr.getEndDate().toDate());
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
		return getSaa(segment.getStartDate().toDate(),segment.getEndDate().toDate());
	}
	@Override
	public SolarAspectAngle[] getSaa(Ptr ptr) {
		return getSaa(ptr.getStartDate().toDate(),ptr.getEndDate().toDate());
	}
	
	public Evtm getEvtm(){
		
		return new Evtm();

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
	@Override
	public String checkPtr(Ptr ptr, Ptr ptsl, Pdfm pdfm) {
		return "";
	}
	@Override
	public String checkPtr(Ptr ptr, Ptr ptsl, Pdfm pdfm, Fecs fecs) {
		return "";
	}
	@Override
	public String checkPtrHTML(Ptr ptr, Ptr ptsl, Pdfm pdfm) {
		return "";
	}
	@Override
	public String checkPtrHTML(Ptr ptr, Ptr ptsl, Pdfm pdfm, Fecs fecs) {
		return "";
	}
	
	
	 

}