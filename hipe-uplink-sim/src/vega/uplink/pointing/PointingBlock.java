package vega.uplink.pointing;

import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Parameter;
import herschel.ia.dataset.StringParameter;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PointingBlock extends PointingMetadata{

		public static String TYPE_MWOL="MWOL";
		public static String TYPE_MWNV="MWNV";
		public static String TYPE_MSLW="MSLW";
		public static String TYPE_MNAV="MNAV";
		public static String TYPE_SLEW="SLEW";
		public static String TYPE_OBS="OBS";
		private java.util.HashMap<String, PointingMetadata> metadata;
		//private String id;
		
		public PointingBlock(PointingMetadata org){
			super(org);
		}
		public CompositeDataset asDataset(){
			return this;
			
		}

		
		public PointingBlock(String blockType,java.util.Date startDate,java.util.Date endDate){
			super("block","");
			
			this.addAttribute(new PointingMetadata("ref",blockType));
			this.addChild(new PointingMetadata("startTime",dateToZulu(startDate)));
			this.addChild(new PointingMetadata("endTime",dateToZulu(endDate)));
			this.addChild(new PointingMetadata("metadata",""));
			
		}
		
		private PointingBlock(){
			super("block","");
		}
		
		public String getType(){
			return this.getAttribute("ref").getValue();
		}
		
		public java.util.Date getStartTime(){
			try {
				return zuluToDate(this.getChild("startTime").getValue());
			} catch (ParseException e) {
				e.printStackTrace();

				return new Date();
			}
		}
		
		public java.util.Date getEndTime(){
			try {
				return zuluToDate(this.getChild("endTime").getValue());
			} catch (ParseException e) {
				e.printStackTrace();

				return new Date();
			}
		}
		
		public void setStartTime(java.util.Date time){
			this.getChild("startTime").setValue(dateToZulu(time));
		}
		
		public void setEndTime(java.util.Date time){
			//System.out.println(getChild("endTime"));
			this.getChild("endTime").setValue(dateToZulu(time));
			//System.out.println(getChild("endTime"));
			
		}
		
		public long getDuration(){
			long start=getStartTime().getTime();
			long end=getEndTime().getTime();
			return end-start;
		}
		public void setAttitude(PointingAttitude newAttitude){
			this.addChild(newAttitude);
		}
		
		public PointingAttitude getAttitude(){
			return (PointingAttitude) this.getChild("attitude");
		}
		
		public PointingMetadata getMetadata(String name){
			return this.getChild("metadata").getChild(name);
		}
		public PointingMetadata[] getMetadata(){
			return this.getChild("metadata").getChildren();
		}
		
		public void addMetadata(PointingMetadata newMetadata){
			this.getChild("metadata").addChild(newMetadata);
		}
		
		
		public static String dateToZulu(java.util.Date date){
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			return dateFormat.format(date);
		}
		
		public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			return dateFormat.parse(zuluTime.trim().replaceAll("\n", ""));
		}
		
		static PointingBlock[] addBlocks(PointingBlock[] first,PointingBlock[] second){
			PointingBlock[] result=new PointingBlock[first.length+second.length];
			for (int i=0;i<first.length;i++){
				result[i]=first[i];
			}
			for (int j=0;j<second.length;j++){
				result[j+first.length]=second[j];
			}
			return result;
		}
		
		
		public boolean equals(PointingBlock other){
			
			boolean result=true;
			if (!this.getStartTime().equals(other.getStartTime())){
				System.out.println("Start time of two blocks different");
				return false;
			}
			if (!this.getEndTime().equals(other.getEndTime())){
				System.out.println("end time of two blocks different");
				
				return false;
			}
			if (!this.getType().equals(other.getType())){
				System.out.println("type of two blocks different:"+this.getType()+" "+other.getType());

				return false;
			}
			PointingAttitude attitude = getAttitude();
			if (attitude!=null) if (!this.getAttitude().equals(other.getAttitude())){
				System.out.println("attitude of two blocks different");

				return false;
			}
			PointingMetadata[] metas=getMetadata();
			if (metas.length!=other.getMetadata().length) return false;
			for (int i=0;i<metas.length;i++){
				if (!metas[i].equals(other.getMetadata(metas[i].getName()))){
					System.out.println("Metadata of two blocks different");

					return false;
				}
			}
			return result;
		}
		
		public static PointingBlock readFrom(Node node){
			String name=node.getNodeName();
			if (!name.equals("block")) return null;
			//if (name.equals("#comment")) return null;

			//String name=node.getNodeName();
			String value="";
			PointingBlock result = new PointingBlock();

			if (node.hasAttributes()){
				NamedNodeMap att = node.getAttributes();
				int size=att.getLength();
				for (int i=0;i<size;i++){
					String attname=att.item(i).getNodeName();
					String attvalue=att.item(i).getNodeValue();
					if (attname!="" && attvalue!="") result.addAttribute(new PointingMetadata(attname,attvalue));
				}
			}
			if (node.hasChildNodes()){
				NodeList children = node.getChildNodes();
				int size=children.getLength();
				for (int i=0;i<size;i++){
					PointingMetadata child = PointingMetadata.readFrom(children.item(i));
					//System.out.println(child);
					if (child!=null) result.addChild(child);
				}			
			}
			//else{
				value=node.getNodeValue();
				if (value!="" && value!=null)result.setValue(value);
			//}
			return result;
		}

		
}
