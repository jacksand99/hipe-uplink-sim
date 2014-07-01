package vega.uplink.pointing;

import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

//import rosetta.uplink.commanding.Por;
//import rosetta.uplink.commanding.Sequence;


public class Ptr extends MapContext{
	java.util.HashMap<String, PtrSegment> segments;
	//PtrSegment[] segments;
	
	public Ptr(){
		super();
		segments=new java.util.HashMap<String, PtrSegment>();
	}
	public herschel.ia.pal.MapContext asContext(){
		/*herschel.ia.pal.MapContext result = new herschel.ia.pal.MapContext();
		Iterator<Entry<String, PtrSegment>> it = segments.entrySet().iterator();
		while(it.hasNext()){
			PtrSegment seg = it.next().getValue();
			result.setProduct(seg.getName(), seg.asProduct());
		}
		/*for (int i=0;i<seqs.length;i++){
			result.setProduct(seqs[i].getUniqueID(), sequenceToProduct(seqs[i]));
		}*/
		return this;
	}
	
	public PtrSegment[] getSegments(){
		Set<String> keys=segments.keySet();
		PtrSegment[] result=new PtrSegment[keys.size()];
		String[] sKeys=new String[keys.size()];
		keys.toArray(sKeys);
		Arrays.sort(sKeys);
		for (int i=0;i<sKeys.length;i++){
			result[i]=segments.get(sKeys[i]);
		}
		return result;
		
	}
	
	public void setSegments(PtrSegment[] newSegments){
		for (int i=0;i<newSegments.length;i++){
			segments.put(newSegments[i].getName(), newSegments[i]);
		}
		
	}
	
	public void addSegment(PtrSegment newSegment){
		setProduct(newSegment.getName(),newSegment);
		segments.put(newSegment.getName(), newSegment);
	}
	
	public PtrSegment getSegment(String segmentName){
		//getProduct(segmentName);
		return segments.get(segmentName);
	}
	
	public String[] getPtrSegmentNames(){
		String[] result=new String[this.segments.size()];
		segments.keySet().toArray(result);
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
