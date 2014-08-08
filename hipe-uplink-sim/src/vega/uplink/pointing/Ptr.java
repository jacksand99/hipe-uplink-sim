package vega.uplink.pointing;

import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

import java.io.IOException;
import java.security.GeneralSecurityException;
//import java.sql.Date;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;



public class Ptr extends MapContext{
	//java.util.HashMap<String, PtrSegment> segments;
	
	public Ptr(){
		super();
		this.setStartDate(new FineTime(new Date()));
		this.setEndDate(new FineTime(new Date(63072000000l)));
		
		//segments=new java.util.HashMap<String, PtrSegment>();
	}
	public herschel.ia.pal.MapContext asContext(){
		return this;
	}
	
	public PtrSegment[] getSegments(){
		Set<String> keys=this.getRefs().keySet();
		PtrSegment[] result=new PtrSegment[keys.size()];
		String[] sKeys=new String[keys.size()];
		keys.toArray(sKeys);
		Arrays.sort(sKeys);
		for (int i=0;i<sKeys.length;i++){
			result[i]=getSegment(sKeys[i]);
			//result[i]=segments.get(sKeys[i]);
		}
		return result;
		
	}
	
	public void setSegments(PtrSegment[] newSegments){
		for (int i=0;i<newSegments.length;i++){
			//segments.put(newSegments[i].getName(), newSegments[i]);
			addSegment(newSegments[i]);
		}
		
	}
	
	public void addSegment(PtrSegment newSegment){
		setProduct(newSegment.getName(),newSegment);
		//System.out.println("added "+newSegment.getName());
		if (this.getStartDate().after(newSegment.getStartDate())) this.setStartDate(newSegment.getStartDate());
		if (this.getEndDate().before(newSegment.getEndDate())) this.setEndDate(newSegment.getEndDate());
		
		//segments.put(newSegment.getName(), newSegment);
	}
	
	public PtrSegment getSegment(String segmentName){
		try {
			return (PtrSegment) getProduct(segmentName);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		//return segments.get(segmentName);
	}
	
	public String[] getPtrSegmentNames(){
		
		String[] result=new String[this.getRefs().keySet().size()];
		this.getRefs().keySet().toArray(result);
		return result;
	}
	
	public PointingBlock[] getAllBlocks(){
		PointingBlock[] result= new PointingBlock[0];
		PtrSegment[] segs=this.getSegments();
		for (int i=0;i<segs.length;i++){
			result=PointingBlock.addBlocks(result, segs[i].getBlocks());
		}
		return result;
	}
	
	public PointingBlock getBlockAt(java.util.Date time){
		PtrSegment temp=new PtrSegment("temp");
		temp.setBlocks(getAllBlocks());
		return temp.getBlockAt(time);
	}
	
	public PointingBlock getBlockAt(String time) throws ParseException{
			return this.getBlockAt(PointingBlock.zuluToDate(time));
	}
	
	public PointingBlock getBlockAt(String startTime,String endTime) throws ParseException{
		PtrSegment temp=new PtrSegment("temp");
		temp.setBlocks(getAllBlocks());
		return temp.getBlockAt(startTime, endTime);
	}
	public PointingBlock getBlockAt(java.util.Date startTime,java.util.Date endTime){
		PtrSegment temp=new PtrSegment("temp");
		temp.setBlocks(getAllBlocks());
		return temp.getBlockAt(startTime, endTime);

	}
	public String toXml(){
		String result="<prm>\n";
		result=result+"\t<header/>\n";				
		result=result+"\t<body>\n";
		PtrSegment[] seg=getSegments();
		for (int i=0;i<seg.length;i++){
			result=result+seg[i].toXml(2);
		}
		result=result+"\t</body>\n";
		result=result+"</prm>\n";
		return result;
	}
	
}
