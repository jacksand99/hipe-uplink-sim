package vega.uplink.pointing.net;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class AngularMomentum {
	
	TreeSet<AMInterval> intervals;
	private static final Logger LOGGER = Logger.getLogger(AngularMomentum.class.getName());
	
	public AngularMomentum(){
		intervals=new TreeSet<AMInterval>();
	}
	
	public void addInterval(AMInterval amInterval){
		intervals.add(amInterval);
	}
	
	public AMInterval[] getIntervals(){
		AMInterval[] result=new AMInterval[intervals.size()];
		result=intervals.toArray(result);
		return result;
	}
	
	public String toString(){
		if (intervals.size()<1) return "";
		String result="Angular momentum:";
		Iterator<AMInterval> it = intervals.iterator();
		while (it.hasNext()){
			result=result+it.next().toString()+"\n";
		}
		
		return result;
	}
	
	public static AngularMomentum readFromFile(String file){
		AngularMomentum result = new AngularMomentum();
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("ANGMOM");
			Node nBody =nListHeader.item(0);
			if (nBody==null){
				IllegalArgumentException iae = new IllegalArgumentException(doc.getTextContent());
				throw iae;
			}
			Element elBody = (Element) nBody;
			NodeList nlSegments=elBody.getElementsByTagName("interval");
			for (int i=0;i<nlSegments.getLength();i++){
				Node nSegment=nlSegments.item(i);
				Date startTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("startTime").item(0).getTextContent());
				Date endTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("endTime").item(0).getTextContent());
				Date violationTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("violationTime").item(0).getTextContent());
				AMInterval interval = result.new AMInterval(startTime,endTime,violationTime);
				
				NodeList profiles = ((Element) nSegment).getElementsByTagName("profile");
				for (int x=0;x<profiles.getLength();x++){
					Node profile=profiles.item(x);
					NodeList nlLevels = ((Element) profile).getElementsByTagName("level");
					for (int z=0;z<nlLevels.getLength();z++){
						Node nLevel=nlLevels.item(z);
						Date time = dateFormat.parse(((Element) nLevel).getElementsByTagName("time").item(0).getTextContent());
						double angmom=Double.parseDouble(((Element) nLevel).getElementsByTagName("angmom").item(0).getTextContent().trim());
						interval.addLevel(time, angmom);
					}
				}
				result.addInterval(interval);
				//System.out.println(interval);
				
				//PdfmDirVector bore=PdfmDirVector.readFrom(nSegment);
				//result.addChild(bore);
			}

			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	LOGGER.severe("Could not read Angular Momentum:"+e.getMessage());
		    	//return result;
		    	IllegalArgumentException iae = new IllegalArgumentException("Colud not read Angular Momentum:"+e.getMessage());
		    	iae.initCause(e);
		    	throw(iae);
		    	//throw(e);
		    }
		return result;
	}
	public static AngularMomentum readFromDoc(Document doc){
		AngularMomentum result = new AngularMomentum();
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

		try {
			 
			//Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("ANGMOM");
			Node nBody =nListHeader.item(0);
			if (nBody==null){
				IllegalArgumentException iae = new IllegalArgumentException(doc.getTextContent());
				throw iae;
			}
			Element elBody = (Element) nBody;
			NodeList nlSegments=elBody.getElementsByTagName("interval");
			for (int i=0;i<nlSegments.getLength();i++){
				Node nSegment=nlSegments.item(i);
				Date startTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("startTime").item(0).getTextContent());
				Date endTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("endTime").item(0).getTextContent());
				Date violationTime=dateFormat.parse(((Element) nSegment).getElementsByTagName("violationTime").item(0).getTextContent());
				AMInterval interval = result.new AMInterval(startTime,endTime,violationTime);
				
				NodeList profiles = ((Element) nSegment).getElementsByTagName("profile");
				for (int x=0;x<profiles.getLength();x++){
					Node profile=profiles.item(x);
					NodeList nlLevels = ((Element) profile).getElementsByTagName("level");
					for (int z=0;z<nlLevels.getLength();z++){
						Node nLevel=nlLevels.item(z);
						Date time = dateFormat.parse(((Element) nLevel).getElementsByTagName("time").item(0).getTextContent());
						double angmom=Double.parseDouble(((Element) nLevel).getElementsByTagName("angmom").item(0).getTextContent().trim());
						interval.addLevel(time, angmom);
					}
				}
				result.addInterval(interval);
				//System.out.println(interval);
				
				//PdfmDirVector bore=PdfmDirVector.readFrom(nSegment);
				//result.addChild(bore);
			}

			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    	LOGGER.severe("Could not read Angular Momentum:"+e.getMessage());
		    	//return result;
		    	IllegalArgumentException iae = new IllegalArgumentException("Colud not read Angular Momentum:"+e.getMessage());
		    	iae.initCause(e);
		    	throw(iae);
		    	//throw(e);
		    }
		return result;
	}
	
	/*public static void Main(String[] args){
		readFromFile("/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361/fddata/1407222816165_agm.txt");
	}*/

	public class AMInterval implements Comparable<AMInterval>{
		TreeSet<AMIntervalLevel> levels;
		Date startTime;
		Date endTime;
		Date violationTime;
		AMInterval(Date start,Date end,Date violation){
			startTime=start;
			endTime=end;
			violationTime=violation;
			levels=new TreeSet<AMIntervalLevel>();
		}
		
		public void addLevel(AMIntervalLevel level){
			levels.add(level);
		}
		
		public void addLevel(Date time,double andmom){
			levels.add(new AMIntervalLevel(time,andmom));
		}
		
		public AMIntervalLevel[] getLevels(){
			AMIntervalLevel[] result=new AMIntervalLevel[levels.size()];
			result=levels.toArray(result);
			return result;
		}
		
		public Date getStartTime(){
			return startTime;
		}
		
		public Date getEndTime(){
			return endTime;
		}
		
		public Date getViolationTime(){
			return violationTime;
		}

		@Override
		public int compareTo(AMInterval o) {
			return this.startTime.compareTo(o.getStartTime());
			// TODO Auto-generated method stub
			//return 0;
		}
		
		public String toString(){
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

			String result= "From "+dateFormat.format(startTime)+" to "+dateFormat.format(endTime)+". Violation time:"+dateFormat.format(violationTime);
			Iterator<AMIntervalLevel> it = levels.iterator();
			while (it.hasNext()){
				result=result+"\n"+"\t"+it.next().toString();
			}
			return result;
		}
	}
	
	public class AMIntervalLevel implements Comparable<AMIntervalLevel>{
		Date time;
		double angmom;
		AMIntervalLevel(Date time,double angmom){
			this.time=time;
			this.angmom=angmom;
		}
		public Date getTime(){
			return time;
		}
		
		public double getAngMom(){
			return angmom;
		}
		
		public int compareTo(AMIntervalLevel o) {
			return this.time.compareTo(o.getTime());
			//return 0;
		}
		
		public String toString(){
			java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

			return "Time:"+dateFormat.format(time)+" "+angmom;
		}


	}
}
