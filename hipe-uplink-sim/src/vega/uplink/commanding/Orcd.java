package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.String1d;
import herschel.share.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.zip.Adler32;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import vega.uplink.Properties;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;


/**
 * Implementation of the Operations Rules and Constrains Document.
 * It is the model of the spacecraft, defining what sequence is allowed, the mode transitions and
 * the power consumed by the different modes
 * @author jarenas
 *
 */
public class Orcd extends TableDataset{
	
	/**
	 * The index in the table for the MODE column. 0
	 */
	public static int INDEX_MODE=0;
	/**
	 * The index in the table for the POWER column. 1
	 */
	public static int INDEX_POWER=1;
	/**
	 * The index in the table for the SEQUENCE column. 2
	 */
	public static int INDEX_SEQUENCE=2;
	/**
	 * The index in the table for the OFFSET column. 3
	 */
	public static int INDEX_OFFSET=3;
	/**
	 * The index in the table for the ALLOWED column. 4
	 */
	public static int INDEX_ALLOWED=4;
	/**
	 * The index in the table for the NOT ALLOWED column. 5
	 */
	public static int INDEX_NOT_ALLOWED=5;
	
	
	private Orcd(){
		super();
		Column mode=new Column(new String1d());
		
		Column power=new Column(new String1d());
		Column sequence=new Column(new String1d());
		Column offSet=new Column(new String1d());
		Column allowed=new Column(new String1d());
		Column notAllowed=new Column(new String1d());

		this.addColumn(mode);
		this.addColumn(power);
		this.addColumn(sequence);
		this.addColumn(offSet);
		this.addColumn(allowed);
		this.addColumn(notAllowed);		
		this.setColumnName(INDEX_MODE, "Destination Mode");
		this.setColumnName(INDEX_POWER, "Power");
		this.setColumnName(INDEX_SEQUENCE, "Transition via sequence");
		this.setColumnName(INDEX_OFFSET, "Transition Offset");
		this.setColumnName(INDEX_ALLOWED, "Transition allowed from mode");
		this.setColumnName(INDEX_NOT_ALLOWED, "Transition forbidden from mode");
		this.setVersion("1.0");
	}
	/**
	 * Get the ORCD. It tries first to get it form the XML file as defined in the properties,
	 * then it will try to read it from the jars files present in the class path
	 * @return
	 */
	public static Orcd getOrcd(){
		try{
			return Orcd.readORCDXmlfile(Properties.getProperty(Properties.ORCD_FILE));
		}catch(Exception e){
			return Orcd.readORCDXmlfromJar();

		}

	}
	/**
	 * Set the version of the ORCD
	 * @param version
	 */
	public void setVersion(String version){
		this.getMeta().set("version", new StringParameter(version));
	}
	
	/**
	 * Get the version of the ORCD
	 * @return
	 */
	public String getVersion(){
		return (String) this.getMeta().get("version").getValue();
	}
	
	public herschel.ia.dataset.TableDataset asTable(){
		return this;
	}
	
	/**
	 * Get the modes and times that a sequence will trigger
	 * @param sequence
	 * @return a Hashmap containing couples of long (number of seconds) and the mode name
	 */
	public java.util.HashMap<Long,String> getModes(String sequence){
		java.util.HashMap<Long,String> result = new java.util.HashMap<Long,String>();
		TableDataset tb=findInTable(this,INDEX_SEQUENCE,sequence);
		for (int i=0;i<tb.getRowCount();i++){
			java.util.List<Object> row = tb.getRow(i);
			long os=getOffsetSeconds((String)row.get(INDEX_OFFSET));
			if (result.containsKey(os)) os=os+i;
			result.put(os , (String) row.get(INDEX_MODE));
			
		}
		return result;
	}
	/**
	 * Get the times and the modes that a sequence will trigger. So if the sequence 
	 * AAAA is defined in the MIB to make the spacecraft to go to mode A1 in 0 seconds and A2 in 30 seconds,
	 * it will add 0 to the execution time and 30 secs.
	 * 
	 * @param sequence
	 * @return a Hashmap containing couples of long (with the long representation of the date when the mode transition will happen) and the mode name
	 */
	public java.util.HashMap<Long,String> getModesAsHistory(AbstractSequence sequence){
		return getModesAsHistory(sequence.getName(), sequence.getExecutionDate());
	}
	/**
	 * Get the times and the modes that a sequence will trigger. So if the sequence 
	 * AAAA is defined in the MIB to make the spacecraft to go to mode A1 in 0 seconds and A2 in 30 seconds,
	 * it will add 0 to the initial time and 30 secs.
	 * 
	 * @param sequence
	 * @param initialTime
	 * @return a Hashmap containing couples of long (with the long representation of the date when the mode transition will happen) and the mode name
	 */
	public java.util.HashMap<Long,String> getModesAsHistory(String sequence,Date initialTime){
		return getModesAsHistory(sequence, initialTime.getTime());
	}
	
	/**
	 * Get the times and the modes that a sequence will trigger. So if the sequence 
	 * AAAA is defined in the MIB to make the spacecraft to go to mode A1 in 0 seconds and A2 in 30 seconds,
	 * it will add 0 to the initial time and 30 secs.
	 * 
	 * @param sequence
	 * @param initialTime
	 * @return a Hashmap containing couples of long (with the long representation of the date when the mode transition will happen) and the mode name
	 */
	public java.util.HashMap<Long,String> getModesAsHistory(String sequence,long initialTime){
		java.util.HashMap<Long,String> result = new java.util.HashMap<Long,String>();
		
		TableDataset tb=findInTable(this,INDEX_SEQUENCE,sequence);
		for (int i=0;i<tb.getRowCount();i++){
			java.util.List<Object> row = tb.getRow(i);
			long tm=(getOffsetSeconds((String)row.get(INDEX_OFFSET))*1000)+initialTime;
			if (result.containsKey(tm)) tm=tm+i;
			result.put(tm , (String) row.get(INDEX_MODE));
			
		}

		return result;
	}
	
	/**
	 * Check if transition from a mode to another is allowed in the ORCD
	 * @param mode1 current mode
	 * @param mode2 the mode to go to
	 * @return True if the transition is allowed or False otherwise
	 */
	public boolean checkTransion(String mode1, String mode2){
		boolean result=false;
		TableDataset tb=findInTable(this,INDEX_MODE,mode2);
		if (tb.getRowCount()>0){
			java.util.List<Object> row = tb.getRow(0);
			result=isSequenceInList(mode1,(String) row.get(INDEX_ALLOWED));
			
			if (!isSequenceInList("null",(String) row.get(INDEX_NOT_ALLOWED))){
				result=!isSequenceInList(mode1,(String) row.get(INDEX_NOT_ALLOWED));
				
			}
			
			
		}
		
		
		return result;
	}
	
	
	/**
	 * Get the power defined in the ORCD for a given mode
	 * @param mode 
	 * @return The power defined in the ORCD for the given mode
	 */
	public float getPowerForMode(String mode){
		float result=0;
		TableDataset tb=findInTable(this,INDEX_MODE,mode);
		if (tb.getRowCount()>0){
			java.util.List<Object> row = tb.getRow(0);
			result=new Float((String) row.get(INDEX_POWER)).floatValue();
		}
		
		return result;
	}
	
	/**
	 * Get the total power for a list of modes (just the sum of the power defined for each mode)
	 * @param modes Array of mode names
	 * @return the total power for all the modes given
	 */
	public float getTotalPowerForModes(String[] modes){
		float result=0;
		for (int i=0;i<modes.length;i++){
			result=result+getPowerForMode(modes[i]);
		}
		return result;
	}
	
	private boolean isSequenceInList(String seq,String list){
		String[] arr=list.split(" ");
		boolean result=false;
		for (int i=0;i<arr.length;i++){
			if (arr[i].equals(seq)) result=true;
		}
		return result;
	}
	
	private TableDataset findInTable(TableDataset table, int columnIndex, String search){
		TableDataset result=table.select(table.getColumn(columnIndex).getData().where(new herschel.binstruct.util.String1dRegex(search)));
		return result;
	}
	
	private void addRow(String mode,String power,String sequence,String offSet,String allowed,String notAllowed){
		String[] array=new String[6];
		array[0]=mode;
		array[1]=power;
		array[2]=sequence;
		array[3]=offSet;
		array[4]=allowed;
		array[5]=notAllowed;
		this.addRow(array);
	}
	/*public void writeToFile(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print("#Mode,Cons,transition via,allowed,not allowed\n");
			int size=this.getRowCount();
			for (int i=0;i<size;i++){
				List<Object> row = this.getRow(i);
				String mode = (String) row.get(INDEX_MODE);
				String power = (String) row.get(INDEX_POWER);
				String sequence = (String) row.get(INDEX_SEQUENCE);
				String offset = (String) row.get(INDEX_OFFSET);
				String allowed = (String) row.get(INDEX_ALLOWED);
				String notAllowed = (String) row.get(INDEX_NOT_ALLOWED);
				writer.print(mode+","+power+","+sequence+"+"+offset+","+allowed+","+notAllowed+"\n");

			}
			//writer.print(PORtoITL(POR));
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}*/
	/**
	 * Write the ORCD representation to a XML file
	 * @param file full path of the target XML file
	 */
	public void writeToXmlFile(String file){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(this.toXml());

			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private static Orcd readORCDXmlBuffer(BufferedReader br){
		Orcd result= new Orcd();
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		Document doc;
		try{
			dBuilder=dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new InputSource(br));
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			e.initCause(e);
			throw(iae);
		}
	 
		doc.getDocumentElement().normalize();
	 
		NodeList nListHeader = doc.getElementsByTagName("orcd");
		Node nBody =nListHeader.item(0);
		Element elBody = (Element) nBody;
		String version=elBody.getAttribute("version");
		result.setVersion(version);
		NodeList nlSegments=elBody.getElementsByTagName("powerModel");
		Element powerBody = (Element) nlSegments.item(0);
		NodeList nlmodes=powerBody.getElementsByTagName("mode");
		int nModes = nlmodes.getLength();
		for (int i=0;i<nModes;i++){
			Element mode = (Element) nlmodes.item(i);
			String name=mode.getAttribute("name");
			String power=mode.getElementsByTagName("power").item(0).getTextContent();
			String transitionAllowed="";
			try{
				NodeList taNodes=mode.getElementsByTagName("transitionAllowed");
				int nTaModes = taNodes.getLength();
				for (int j=0;j<nTaModes;j++){
					Element ta=(Element) taNodes.item(j);
					transitionAllowed=transitionAllowed+" "+ta.getTextContent();
				}
			}catch(Exception e){
				transitionAllowed="null";
			}
			if (transitionAllowed.equals("")) transitionAllowed="null";
			transitionAllowed=transitionAllowed.trim();

			String transitionNotAllowed="";
			try{
				NodeList tnaNodes=mode.getElementsByTagName("transitionNotAllowed");
				int nTnaModes = tnaNodes.getLength();
				for (int j=0;j<nTnaModes;j++){
					Element tna=(Element) tnaNodes.item(j);
					transitionNotAllowed=transitionNotAllowed+" "+tna.getTextContent();
				}
			}catch (Exception e){
				transitionNotAllowed="null";
			}
			if (transitionNotAllowed.equals("")) transitionNotAllowed="null";
			transitionNotAllowed=transitionNotAllowed.trim();

			NodeList sequenceNodes=mode.getElementsByTagName("sequence");
			int nSNodes=sequenceNodes.getLength();
			for (int j=0;j<nSNodes;j++){
				Element seq=(Element) sequenceNodes.item(j);
				String seqName=seq.getTextContent();
				String offset="00:00:00";
				try{
					offset=seq.getAttribute("offset");
					if (offset.equals("")) offset="00:00:00";
				}catch (Exception e){
					
				}
				result.addRow(name, power, seqName, offset, transitionAllowed, transitionNotAllowed);
			}
		}
		return result;
	}
	/**
	 * Read the XML from a jar file in the class path. It will try to locate the file /mib/orcd.xml in the class path
	 * @return The ORCD
	 */
	public static Orcd readORCDXmlfromJar(){
		InputStream is = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/orcd.xml");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		return readORCDXmlBuffer(br);
	}
	
	/**
	 * Read the ORCD from a XML file
	 * @param xmlFile The xml file full path
	 * @return The ORCD
	 */
	public static Orcd readORCDXmlfile(String xmlFile){
		try{
			return readORCDXmlBuffer(new BufferedReader(new FileReader(xmlFile)));
		}catch(Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			e.initCause(e);
			throw(iae);
		}
	  
	}

	
	/*private static Orcd readORCDBuffer(BufferedReader br){
		Orcd result= new Orcd();
		String line = "";
		String cvsSplitBy = ",";
		try {
			 
			while ((line = br.readLine()) != null) {
				if (!line.startsWith("#")){
					String[] fields = line.split(cvsSplitBy);
					String[] cArray=fields[2].split(" ");
					for (int i=0;i<cArray.length;i++){
						String seq="";
						String off="00:00:00";
						try{
							String[] temp=cArray[i].split("\\+");
							seq=temp[0];
							if (temp.length==2){
								off=temp[1];
							}
						}catch (Exception e){
								e.printStackTrace();
								seq=cArray[i];
								off="00:00:00";
						}
		
						result.addRow(fields[0], fields[1], seq, off, fields[3], fields[4]);
					}
				}
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	 
		System.out.println(result);
		return result;


	}*/
	/*private static Orcd readORCDfromJar(){
		
		InputStream is = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/mib/orcd.csv");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		return readORCDBuffer(br);
	}
	
	private static Orcd readORCDfile(String csvFile){
		try{
			return readORCDBuffer(new BufferedReader(new FileReader(csvFile)));
		}catch(Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			e.initCause(e);
			throw(iae);
			//e.printStackTrace();
			//return new Orcd();
		}
	  
	}*/
	
	private long getOffsetSeconds(String sOffset){
		long offset=0;
		String[] times=sOffset.split(":");
		int hours=Integer.parseInt(times[0]);
		int minutes=Integer.parseInt(times[1]);
		int seconds=Integer.parseInt(times[2]);
		offset=(hours*3600)+(minutes*60)+seconds;
		return offset;
	}
	
	/**
	 * Get a XML representation of the ORCD
	 * @return
	 */
	public String toXml(){
		TreeSet<String> modes=new TreeSet<String>();
		int rowCount=this.getRowCount();
		for (int i=0;i<rowCount;i++){
			String[] array=new String[6];
			array = this.getRow(i).toArray(array);
			String mode=array[0];
			modes.add(mode);
		}
		String result="<!-- Operations Rules and Constrains Model -->\n";
		result=result+"<orcd version=\""+getVersion()+"\">\n";
		result=result+"\t<powerModel>\n";
		Iterator<String> it = modes.iterator();
		while (it.hasNext()){
			String mode=it.next();
			result=result+"\t\t<mode name=\""+mode+"\">\n";

			result=result+"\t\t\t<power>"+this.getPowerForMode(mode)+"</power>\n";
			TableDataset table = findInTable(this, INDEX_MODE, mode);
			int rowCount2=table.getRowCount();
			String allowed="null";
			String notAllowed="null";
			for (int i=0;i<rowCount2;i++){
				String[] array=new String[6];
				array = table.getRow(i).toArray(array);

				String sequence=array[2];
				String offSet=array[3];
				allowed=array[4];
				notAllowed=array[5];

				if (!offSet.equals("00:00:00")) result=result+"\t\t\t<sequence offset=\""+offSet+"\""+">"+sequence+"</sequence>\n";
				else result=result+"\t\t\t<sequence>"+sequence+"</sequence>\n";

				
				
			}
			if (!allowed.equals("null")){
				StringTokenizer allTok = new StringTokenizer(allowed);
				while(allTok.hasMoreElements()){
					result=result+"\t\t\t<transitionAllowed>"+allTok.nextToken()+"</transitionAllowed>\n";
				}

			}
			if (!notAllowed.equals("null")){
				StringTokenizer notallTok = new StringTokenizer(notAllowed);
				while(notallTok.hasMoreElements()){
					result=result+"\t\t\t<transitionNotAllowed>"+notallTok.nextToken()+"</transitionNotAllowed>\n";
				}
			}

			result=result+"\t\t</mode>\n";
		}
		result = result+"\t</powerModel>\n";

		result = result+"</orcd>";
		return result;
	}
	
}
