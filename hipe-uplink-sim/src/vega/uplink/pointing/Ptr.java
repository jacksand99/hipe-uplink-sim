package vega.uplink.pointing;

import herschel.ia.dataset.StringParameter;
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

import vega.uplink.DateUtil;
import vega.uplink.Properties;



/**
 * Class to represent both PTR and PTSL
 * PTRs can contain only one segment, PTSLs may contain several segments
 * @author jarenas
 *
 */
public class Ptr extends MapContext{
	public static String PRM_TAG="prm";
	public static String HEADER_TAG="header";
	public static String BODY_TAG="body";
	//java.util.HashMap<String, PtrSegment> segments;
	
	/**
	 * Creates a new, empty PTR/PTSL
	 */
	public Ptr(){
		super();
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));
		this.setType("PTR/PTSL");
		this.setCreationDate(new FineTime(new java.util.Date()));

		this.setStartDate(new FineTime(new Date()));
		this.setEndDate(new FineTime(new Date(63072000000l)));
		
		//segments=new java.util.HashMap<String, PtrSegment>();
	}
	public void setName(String name){
		getMeta().set("name", new StringParameter(name));
	}
	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	public String getName(){
		return (String) getMeta().get("name").getValue();
	}
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}

	public herschel.ia.pal.MapContext asContext(){
		return this;
	}
	public void regenerate(Ptr ptr){
		String name=this.getName();
		String path=this.getPath();
		String[] segNames = this.getPtrSegmentNames();
		for (int i=0;i<segNames.length;i++){
			this.getRefs().remove(segNames[i]);
			//this.remove(segNames[i]);
		}
		PtrSegment[] newSegments = ptr.getSegments();
		for (int i=0;i<newSegments.length;i++){
			this.addSegment(newSegments[i]);
		}
		this.setMeta(ptr.getMeta());
		this.setName(name);
		this.setPath(path);
	}
	
	/**
	 * Get all segments contained in this PTR/PTSL
	 * @return
	 */
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
	
	/**
	 * Add all segments contained in the array given
	 * @param newSegments
	 */
	public void setSegments(PtrSegment[] newSegments){
		for (int i=0;i<newSegments.length;i++){
			//segments.put(newSegments[i].getName(), newSegments[i]);
			addSegment(newSegments[i]);
		}
		
	}
	
	/**
	 * Add a new segment to this PTR/PTSL
	 * @param newSegment
	 */
	public void addSegment(PtrSegment newSegment){
		setProduct(newSegment.getName(),newSegment);
		//System.out.println("added "+newSegment.getName());
		if (this.getStartDate().after(newSegment.getStartDate())) this.setStartDate(newSegment.getStartDate());
		if (this.getEndDate().before(newSegment.getEndDate())) this.setEndDate(newSegment.getEndDate());
		
		//segments.put(newSegment.getName(), newSegment);
	}
	
	/**
	 * Get the segment with the given name
	 * @param segmentName
	 * @return
	 */
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
	
	/**
	 * Get the names of the segments contained in this PTR/PTSL
	 * @return
	 */
	public String[] getPtrSegmentNames(){
		
		String[] result=new String[this.getRefs().keySet().size()];
		this.getRefs().keySet().toArray(result);
		return result;
	}
	
	/**
	 * Get all pointing blocks contained in all segments of this PTR/PTSL
	 * @return
	 */
	public PointingBlock[] getAllBlocks(){
		PointingBlock[] result= new PointingBlock[0];
		PtrSegment[] segs=this.getSegments();
		for (int i=0;i<segs.length;i++){
			result=PointingBlock.addBlocks(result, segs[i].getBlocks());
		}
		return result;
	}
	/**
	 * Get a block at a given time
	 * @param time the time to search
	 * @return the pointing block at the given time
	 */	
	public PointingBlock getBlockAt(java.util.Date time){
		PtrSegment temp=new PtrSegment("temp");
		temp.setBlocks(getAllBlocks());
		return temp.getBlockAt(time);
	}
	
	/**
	 * Get a block at a given time
	 * @param time the time to search
	 * @return the pointing block at the given time
	 * @throws ParseException if the string can not converted to a date
	 */
	public PointingBlock getBlockAt(String time) throws ParseException{
			return this.getBlockAt(DateUtil.zuluToDate(time));
	}
	
	
	/**
	 * get a xml representation of this PTR or PTSL
	 * @return
	 */
	public String toXml(){
		String result="<"+PRM_TAG+">\n";
		result=result+"\t<"+HEADER_TAG+"/>\n";				
		result=result+"\t<"+BODY_TAG+">\n";
		PtrSegment[] seg=getSegments();
		for (int i=0;i<seg.length;i++){
			result=result+seg[i].toXml(2);
		}
		result=result+"\t</"+BODY_TAG+">\n";
		result=result+"</"+PRM_TAG+">\n";
		return result;
	}
	
	public PointingBlocksSlice getSlice(int number){
		return this.getSegments()[0].getSlice(number);
	}
	public PointingBlocksSlice getAllBlocksOfInstrument(String instrument){
		PointingBlocksSlice result=new PointingBlocksSlice();
		PtrSegment[] segs = this.getSegments();
		for (int i=0;i<segs.length;i++){
			PointingBlock[] blocks = segs[i].getAllBlocksOfInstrument(instrument).getBlocks();
			result.setBlocks(blocks);
		}
		
		return result;
	}
	public void setSlice(PointingBlocksSlice slice){
		this.getSegments()[0].setSlice(slice);
	}
}
