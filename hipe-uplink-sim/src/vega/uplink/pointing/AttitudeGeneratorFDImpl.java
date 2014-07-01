package vega.uplink.pointing;

import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.time.*;
import herschel.share.util.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;

/*import org.apache.commons.httpclient.HttpClient;
import org.esa.rsgs.fdsw.net.AngMomRequest;
import org.esa.rsgs.fdsw.net.Att2AscRequest;
import org.esa.rsgs.fdsw.net.FDSWHttpClient;
import org.esa.rsgs.fdsw.net.FDSWHttpClientException;
import org.esa.rsgs.fdsw.net.Ptr2AttRequest;*/


public class AttitudeGeneratorFDImpl implements AttitudeGenerator {

	@Override
	public HashMap<Long, Quaternion> getAttitude(PointingBlock block) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Long, Quaternion> getAttitude(PtrSegment segment) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Long, Quaternion> getAttitude(Ptr ptr) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Quaternion getAttitude(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
	/*HashMap<Long, Quaternion> quaternions;
	Long[] times;
	public static long STEP=10000;
	
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm){
		this(ptr,pdfm,ptr.getSegments()[0].getName().substring(4));
	}
	public AttitudeGeneratorFDImpl(Ptr ptr,Pdfm pdfm,String mtpNum){
		String dir = Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.tempDir");
		String activityCase=Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.activityCase");
		String serverUrl=Configuration.getProperty("rosetta.uplink.pointing.AttitudeGeneratorFdImpl.serverUrl");
		String name=""+new java.util.Date().getTime();
		PtrSegment seg = ptr.getSegment("MTP_"+mtpNum);
		String[] includes={"FDDF.xml",name+"_pdfm.xml"};
		seg.setIncludes(includes);
		PtrUtils.writePTRtofile(dir+name+"_ptr.xml",ptr);
		PtrUtils.writePDFMtofile(dir+name+"_pdfm.xml", pdfm);
		HttpClient client = new HttpClient();
		Ptr2AttRequest req = new Ptr2AttRequest(client, new File(dir+name+"_ptr.xml"), activityCase);
		req.setMtpNum(mtpNum);
		File pdfmFile = new File(dir+name+"_pdfm.xml");
		req.setPdfmFile(pdfmFile);
		try {
			req.execute(serverUrl);
			Att2AscRequest gen = new Att2AscRequest(client, req.getPtrId(), activityCase);
			//AngMomRequest ang= new AngMomRequest(client,req.getPtrId(),activityCase);
			
			gen.execute(serverUrl);
			//ang.execute(serverUrl);
			//System.out.println(ang.getContent());
			File out = new File(dir+name+"_att.txt");			
			String fileUrl = gen.getFileUrl();
			//String fileUrl2 = ang.getFileUrl();
			FDSWHttpClient.downloadFile(client, fileUrl, out);
			init(dir+name+"_att.txt");
			new File(dir+name+"_ptr.xml").delete();
			new File(dir+name+"_pdfm.xml").delete();
			out.delete();

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	
	private void init(String file){
		BufferedReader br = null;
		String line = "";
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		quaternions=new HashMap<Long, Quaternion>();
	 
		try {
	 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("20")){
					//System.out.println("Skip");
				}else{
					//java.util.Date dt=dateFormat.parse(line.substring(0, 23));
					SimpleTimeFormat tdb = new SimpleTimeFormat(TimeScale.TDB);
					tdb.setDecimals(6);
					FineTime ftime = tdb.parse(line.substring(0, 26)+" TDB");
					Date dt = ftime.toDate();
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
			e.printStackTrace();
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
		long fac=1/(time2-time1);
		double delta=time*fac;
		if (compare(q1,q2)<compare(q1,q2.conjugate())){
			return q1.slerp(q2, delta);
		}
		else{
			return q1.conjugate().slerp(q2, delta);
		}
	}
	
	public Quaternion getAttitude(java.util.Date date){
		long time=date.getTime();
		if (time>times[times.length-1]) return quaternions.get(times[times.length-1]);
		int[] r=find(times,0,times.length-1,time);
		return interpolate(quaternions.get(times[r[0]]),new java.util.Date(times[r[0]]),quaternions.get(times[r[1]]),new java.util.Date(times[r[1]]),date);
		
	}

	@Override
	public HashMap<Long, Quaternion> getAttitude(PointingBlock block) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		long time1=block.getStartTime().getTime();
		long time2=block.getEndTime().getTime();
		for (long i=time1;i<time2;i=i+STEP){
			result.put(i, getAttitude(new java.util.Date(i)));
		}
		
		return result;
	}

	@Override
	public HashMap<Long, Quaternion> getAttitude(PtrSegment segment) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		PointingBlock[] blocks=segment.getBlocks();
		for (int i=0;i<blocks.length;i++){
			result.putAll(getAttitude(blocks[i]));
		}
		return result;
	}

	@Override
	public HashMap<Long, Quaternion> getAttitude(Ptr ptr) {
		HashMap<Long, Quaternion> result=new HashMap<Long, Quaternion>();
		PtrSegment[] segments=ptr.getSegments();
		for (int i=0;i<segments.length;i++){
			result.putAll(getAttitude(segments[i]));
		}
		
		return result;
	}*/

}
