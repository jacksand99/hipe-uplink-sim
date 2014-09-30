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
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.Properties;

/**
 * A block containing a pointing definition
 * @author jarenas
 *
 */
public class PointingBlock extends PointingElement{

	
		/**
		 * PointingBlock type for performing a dV manoeuvre
		 */
		public static String TYPE_MOCM="MOCM";

		/**
		 * Pointing block type performing a wheel of loading
		 */
		public static String TYPE_MWOL="MWOL";
		/**
		 * Pointing block type performing navigation slots that normally use the NAVCAM also contain a WOL
		 */
		public static String TYPE_MWNV="MWNV";
		/**
		 * a slew reserved by FD (e.g at a VSTP border) 
		 */
		public static String TYPE_MSLW="MSLW";
		/**
		 * Pointing block type performing navigation slots that nominally use the NAVCAM that do not contain a WOL.
		 */
		public static String TYPE_MNAV="MNAV";
		/**
		 * Slew between 2 blocks
		 */
		public static String TYPE_SLEW="SLEW";
		/**
		 * Pointing block type performing science observations.
		 */
		public static String TYPE_OBS="OBS";
		/**
		 * Pointing block type for on-board implemented autonomous guidance attitude
		 */
		public static String TYPE_GSEP="GSEP";
		public static String BLOCK_TAG="block";
		private java.util.HashMap<String, PointingElement> metadata;
		//private String id;
		
		public PointingBlock(PointingElement org){
			super(org);
		}
		public CompositeDataset asDataset(){
			return this;
			
		}

		
		/**
		 * Creates a pointing block of the given type.
		 * The time interval during which the pointing block is valid is specified by the start date and end date
		 * @param blockType Pointing block type 
		 * @param startDate start date of the time interval during which the pointing block is valid
		 * @param endDate start date of the time interval during which the pointing block is valid
		 */
		public PointingBlock(String blockType,java.util.Date startDate,java.util.Date endDate){
			super(BLOCK_TAG,"");
			
			this.addAttribute(new PointingElement("ref",blockType));
			this.addChild(new PointingElement("startTime",dateToZulu(startDate)));
			this.addChild(new PointingElement("endTime",dateToZulu(endDate)));
			this.addChild(new PointingMetadata());
			
		}
		
		private PointingBlock(){
			super(BLOCK_TAG,"");
		}
		
		/**
		 * @return the Type of the block
		 */
		public String getType(){
			return this.getAttribute("ref").getValue();
		}
		
		/**
		 * @return start date of the time interval during which the pointing block is valid
		 */
		public java.util.Date getStartTime(){
			try {
				return zuluToDate(this.getChild("startTime").getValue());
			} catch (ParseException e) {
				//e.printStackTrace();
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				throw(iae);
				//return new Date();
			}
		}
		/**
		 * @return end date of the time interval during which the pointing block is valid
		 */		
		public java.util.Date getEndTime(){
			try {
				return zuluToDate(this.getChild("endTime").getValue());
			} catch (ParseException e) {
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				throw(iae);
			}
		}
		
		/**
		 * Set start date of the time interval during which the pointing block is valid
		 * @param time
		 */
		public void setStartTime(java.util.Date time){
			this.getChild("startTime").setValue(dateToZulu(time));
		}
		/**
		 * Set end date of the time interval during which the pointing block is valid
		 * @param time
		 */		
		public void setEndTime(java.util.Date time){
			//System.out.println(getChild("endTime"));
			this.getChild("endTime").setValue(dateToZulu(time));
			//System.out.println(getChild("endTime"));
			
		}
		
		/**
		 * @return Duration of this block in milliseconds
		 */
		public long getDuration(){
			long start=getStartTime().getTime();
			long end=getEndTime().getTime();
			return end-start;
		}
		/**
		 * Set the attitude of this block
		 * @param newAttitude
		 */
		public void setAttitude(PointingAttitude newAttitude){
			this.addChild(newAttitude);
		}
		/**
		 * Get the attitude of this block
		 */
		public PointingAttitude getAttitude(){
			return (PointingAttitude) this.getChild("attitude");
		}
		
		/**
		 * Get a specific metadata of the given name (vstpNumber,positionError,hgaRequest,comment or planning)
		 * @param name
		 * @return
		 */
		public PointingElement getMetadata(String name){
			return this.getChild("metadata").getChild(name);
		}
		public PointingMetadata getMetadataElement(){
			PointingElement child = this.getChild("metadata");
			if (child==null) return null;
			try{
				return (PointingMetadata) child;
			}catch (ClassCastException cce){
				IllegalArgumentException iae = new IllegalArgumentException("Observation was not read properly:\n"+this.toXml(0)+"\n"+child.toXml(0));
				iae.initCause(cce);
				throw(iae);
			}
		}
		/**
		 * Get all the metadata that this block have or null if it has no metadata
		 * @return
		 */
		public PointingElement[] getMetadata(){
			return this.getChild("metadata").getChildren();
			/*PointingElement[] metaChildren = this.getChild("metadata").getChildren();
			PointingMetadata[] result= new PointingMetadata[metaChildren.length];
			for (int i=0;i<metaChildren.length;i++){
				result[i]=(PointingMetadata)metaChildren[i];
			}
			return result;*/
		}
		
		/**
		 * The child element <metadata> can have the child elements:
		 * <vstpNumber> is present in the first block of a VSTP and contains the VSTPnumber
		 * (three digits with leading zero).
		 * <positionError> contains the maximum position error for this and the
		 * following blocks that do not contain an updated position error in metadata.
		 * <positionError> contains the three simple child elements <alongtrack
		 * units=km>, <crosstrack units=km> and <radial units=km>. Each
		 * element contains a list of maximum position offset along the respective direction. If
		 * the length of these lists is longer than 1 then the additional element <epoch> must
		 * be provided that contains a list of times. Each time must have the format YYYYMM-
		 * DDTHH:MM:SS[.SSS]Z. The length of the lists in all elements must be the
		 * same.
		 * <hgaRequest> can be used to specify the required HGA articulation.
		 * <comment> optional element for inserting comments.
		 * <planning> the SGS may use this element to store any information
		 * If <metadata> has no child elements the element may be omitted.
		 * @param newMetadata
		 */
		public void addMetadata(PointingMetadata newMetadata){
			this.getChild("metadata").addChild(newMetadata);
		}
		
		
		/**
		 * Transform a date into a string of the format yyyy-MM-ddTHH:mm:ss
		 * @param date date to be transformed
		 * @return a string representation of the date
		 */
		public static String dateToZulu(java.util.Date date){
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			return dateFormat.format(date);
		}
		
		/**
		 * Transform a string of the format yyyy-MM-ddTHH:mm:ss into a date
		 * @param zuluTime the string to be read
		 * @return the date represented by the string
		 * @throws ParseException if string is malformed
		 */
		public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
			return dateFormat.parse(zuluTime.trim().replaceAll("\n", ""));
		}
		
		/**
		 * Convenience method to add to arrays of pointing blocks into a single array
		 * @param first First array to be added
		 * @param second Second array to be added
		 * @return an array containing all elements of first array and second array
		 */
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
		
		
		/**
		 * Compare two pointing blocks
		 * @param other block to be compared with this one
		 * @return true if the blocks are equal or false otherwise
		 */
		/*public boolean equals(PointingBlock other){
			
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
			PointingElement[]  metas=getMetadata();
			//PointingMetadata[] metas=getMetadata();
			if (metas.length!=other.getMetadata().length) return false;
			for (int i=0;i<metas.length;i++){
				if (!metas[i].equals(other.getMetadata(metas[i].getName()))){
					System.out.println("Metadata of two blocks different");

					return false;
				}
			}
			return result;
		}*/
		
		/**
		 * Read a block from a XML node
		 * @param node node to be read
		 * @return the pointing block read
		 */
		public static PointingBlock readFrom(Node node){
			String name=node.getNodeName();
			if (!name.equals(BLOCK_TAG)) return null;
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
					if (attname!="" && attvalue!="") result.addAttribute(new PointingElement(attname,attvalue));
				}
			}
			if (node.hasChildNodes()){
				NodeList children = node.getChildNodes();
				int size=children.getLength();
				for (int i=0;i<size;i++){
					PointingElement child=null;
					if (children.item(i).getNodeName().equals(PointingMetadata.METADATA_TAG)){
						child = PointingMetadata.readFrom(children.item(i));
					}
					else child = PointingElement.readFrom(children.item(i));
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
		
		public  boolean isSlew(){
			boolean result=false;
			if (getType().equals("SLEW") || getType().equals("MOCM") || getType().equals("MWOL") || getType().equals("MSLW")){
				result=true;
			}
			return result;
		}
		
		public boolean validate(){
			//System.out.println("validating pointing block");
			boolean result = true;
			try {
				zuluToDate(this.getChild("startTime").getValue());
				zuluToDate(this.getChild("endTime").getValue());
			} catch (ParseException e) {
				System.out.println("detected problem");
				e.printStackTrace();
				IllegalArgumentException iae = new IllegalArgumentException("Date not in the correct format./n"+this.toXml(0)+"/n"+e.getMessage());
				iae.initCause(e);
				throw (iae);
				//return false;
			}

			if (!super.validate()) result=false;
			return result;
		}
		
		public String getInstrument(){
			List<String> ins = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
			//PointingMetadata meta = (PointingMetadata)this.getChild(PointingMetadata.METADATA_TAG);
			PointingMetadata meta = this.getMetadataElement();
			if (meta==null) return null;
			String[] comments = meta.getComments().getArray();
			for (int i=0;i<comments.length;i++){
				Iterator<String> it = ins.iterator();
				while (it.hasNext()){
					String in_name = it.next();
					if (comments[i].toUpperCase().startsWith(in_name)) return in_name;
				}
			}
			return null;
			
		}
		
		public int getVstpNumberMeta(){
			PointingMetadata meta = this.getMetadataElement();
			if (meta==null) return -1;
			return meta.getVstpNumber();
		}

		



		
}
