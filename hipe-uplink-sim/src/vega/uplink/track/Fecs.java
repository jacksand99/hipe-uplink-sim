package vega.uplink.track;
import herschel.ia.dataset.Column;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.time.FineTime;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.hipe.gui.xmlutils.XmlDataInterface;
import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.EvtmEvent;
import vega.uplink.pointing.PointingBlock;

import org.w3c.dom.Document;



/**
 * The Fecs class is a data model to store the information from the FECS file.
 * This file stores the information about Ground station passes and downlink rate.
 * @author jarenas
 *
 */
public class Fecs extends TableDataset implements XmlDataInterface{
	private TreeSet<GsPass> passesSet;
	//private static final Logger LOG = Logger.getLogger(Fecs.class.getName());
	private static String PASS_TABLE_HEADER=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th>\n"
			+ "</tr>\n";
	private static String GLOBAL_CHANGE_TABLE_HEADER_DURATION=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th><th>Change</th>\n"
			+ "</tr>\n";


	/**
	 * Constructor of the Fecs class taking as parameters all the needed information, without taking any parameter with default value.
	 * @param fecsGenerationTime Generation time of the FECS file
	 * @param fecsValidityStart Start time from which this product is applicable.
	 * @param fecsValidityEnd End time until which this product is applicable
	 * @param fecsSpacecraft The name of the spacecraft for which this product is applicable
	 * @param fecsIcdVersion The version of the icd (Interface Control Document) that applies to this product
	 */
	public Fecs(Date fecsGenerationTime,Date fecsValidityStart,Date fecsValidityEnd,String fecsSpacecraft,String fecsIcdVersion){
		super();
		setName(""+new java.util.Date().getTime());
		setPath(Properties.getProperty("user.home"));

		passesSet=new TreeSet<GsPass>();
		setGenerationTime(fecsGenerationTime);
		setValidityStart(fecsValidityStart);
		setValidityEnd(fecsValidityEnd);
		setSpacecraft(fecsSpacecraft);
		setIcdVersion(fecsIcdVersion);
		Column gs=new Column(new String1d());
		Column stpass=new Column(new String1d());
		Column edpass=new Column(new String1d());
		Column stdump=new Column(new String1d());
		Column eddump=new Column(new String1d());
		Column tmRate=new Column(new String1d());
		Column totalTime=new Column(new String1d());
		Column dumpTime=new Column(new String1d());
		
		this.addColumn(gs);		
		this.addColumn(stpass);
		this.addColumn(edpass);
		this.addColumn(stdump);
		this.addColumn(eddump);
		this.addColumn(tmRate);
		this.addColumn(totalTime);
		this.addColumn(dumpTime);

		this.setColumnName(0, "Ground Station");
		this.setColumnName(1, "Start pass");
		this.setColumnName(2, "End Pass");
		this.setColumnName(3, "Start Dump");
		this.setColumnName(4, "End Dump");
		this.setColumnName(5, "TM rate");
		this.setColumnName(6, "Total time (s)");
		this.setColumnName(7, "Dump time (s)");

	}
	private void deleteAllPasses(){
		passesSet=new TreeSet<GsPass>();
		int rowCount=this.getRowCount();
		for (int i=0;i<rowCount;i++){
			this.removeRow(i);
		}
	}
	/**
	 * Set the filename of the FECS
	 * @param name
	 */
	public void setName(String name){
		getMeta().set("name", new StringParameter(name));
	}


	public void setPath(String path){
		getMeta().set("path", new StringParameter(path));
	}
	
	/**
	 * Get the file name of the FECS file
	 * @return
	 */
	public String getName(){
		return (String) getMeta().get("name").getValue();
	}
	public String getPath(){
		return (String) getMeta().get("path").getValue();
	}

	/**
	 * Constructor of the Fecs class taking as parameters all the needed information, but assuming the spacecraft to be ROSETTA.
	 * @param fecsGenerationTime Generation time of the FECS file
	 * @param fecsValidityStart Start time from which this product is applicable.
	 * @param fecsValidityEnd End time until which this product is applicable
	 * @param fecsIcdVersion The version of the icd (Interface Control Document) that applies to this product
	 */	
	/**
	 * @param fecsGenerationTime
	 * @param fecsValidityStart
	 * @param fecsValidityEnd
	 * @param fecsIcdVersion
	 */
	public Fecs(Date fecsGenerationTime,Date fecsValidityStart,Date fecsValidityEnd,String fecsIcdVersion){
		this(fecsGenerationTime,fecsValidityStart,fecsValidityEnd,"ROS",fecsIcdVersion);
	}
	/**
	 * Constructor of the Fecs class taking as parameters all the needed information,
	 *  but assuming the spacecraft to be ROSETTA 
	 *  and the generation time the creation time of this product.
	 * @param fecsValidityStart Start time from which this product is applicable.
	 * @param fecsValidityEnd End time until which this product is applicable
	 * @param fecsIcdVersion The version of the icd (Interface Control Document) that applies to this product
	 */	
	public Fecs(Date fecsValidityStart,Date fecsValidityEnd,String fecsIcdVersion){
		this(new java.util.Date(),fecsValidityStart,fecsValidityEnd,"ROS",fecsIcdVersion);
	}
	
	/**
	 * Constructor of the Fecs class taking as parameters all the needed information,
	 *  but assuming the spacecraft to be ROSETTA,
	 *  the generation time the creation time of this product 
	 *  and the ICD version as 0.0.
	 * @param fecsValidityStart Start time from which this product is applicable.
	 * @param fecsValidityEnd End time until which this product is applicable
	 */	
	public Fecs(Date fecsValidityStart,Date fecsValidityEnd){
		this(new java.util.Date(),fecsValidityStart,fecsValidityEnd,"ROS","PLID-0.0");
	}
	/**
	 * Constructor of the Fecs class assuming the spacecraft to be ROSETTA,
	 * the generation time the creation time of this product,
	 * the ICD version as 0.0 and the validity start and end the creation time of this product.
	 */	
	public Fecs(){
		this(new java.util.Date(),new java.util.Date(),new java.util.Date(),"ROS","PLID-0.0");
	}
	
	/**
	 * Get the generation time of the Fecs
	 * @return Generation time of the Fecs
	 */
	public Date getGenerationTime(){
		return ((FineTime) getMeta().get("GenerationTime").getValue()).toDate();
		//return generationTime;
	}
	
	/**
	 * Set the generation time of the Fecs
	 * @param evtmGenerationTime Generation time of the Fecs
	 */
	public void setGenerationTime(Date evtmGenerationTime){
		
		getMeta().set("GenerationTime", new DateParameter(new FineTime(evtmGenerationTime)));
		//generationTime=evtmGenerationTime;
	}
	
	/**
	 * Get the start date of the validity of the FECS
	 * @return
	 */
	public Date getValidityStart(){
		return ((FineTime) getMeta().get("StartTime").getValue()).toDate();
	}
	
	/**
	 * Get the end date of the validity of the FECS
	 * @param evtmValidityStart
	 */
	public void setValidityStart(Date evtmValidityStart){
		getMeta().set("StartTime", new DateParameter(new FineTime(evtmValidityStart)));
	}
	
	/**
	 * Set the end date of the validity of the FECS
	 * @param validityEnd
	 */
	public void setValidityEnd(Date validityEnd){
		getMeta().set("EndTime", new DateParameter(new FineTime(validityEnd)));
	}
	
	/**
	 * Set the name of the spacecraft
	 * @param spacecraft
	 */
	public void setSpacecraft(String spacecraft){
		getMeta().set("Spacecraft", new StringParameter(spacecraft));
	}
	
	/**
	 * Set the icd version
	 * @param icdVersion
	 */
	public void setIcdVersion(String icdVersion){
		getMeta().set("ICDversion", new StringParameter(icdVersion));
	}
	
	/**
	 * Get the end date of the validity of the FECS
	 * @return
	 */
	public Date getValidityEnd(){
		return ((FineTime) getMeta().get("EndTime").getValue()).toDate();
	}
	
	/**
	 * Get the spacecraft name
	 * @return
	 */
	public String getSpacecraft(){
		return (String) getMeta().get("Spacecraft").getValue();
	}
	
	/**
	 * Get the ICD version
	 * @return
	 */
	public String getIcdVersion(){
		return (String) getMeta().get("ICDversion").getValue();
	}
	
	/**
	 * Add a GS pass to the FECS
	 * @param pass GS pass to add
	 */
	public void addPass(GsPass pass){
		passesSet.add(pass);
		String[] row=new String[8];
		row[0]=pass.getGroundStation();
		row[1]=DateUtil.defaultDateToString(pass.getStartPass());
		row[2]=DateUtil.defaultDateToString(pass.getEndPass());
		row[3]=DateUtil.defaultDateToString(pass.getStartDump());
		row[4]=DateUtil.defaultDateToString(pass.getEndDump());
		row[5]=new Float(pass.getTmRate()).toString();
		row[6]=new Long(pass.getPassDurationSecs()).toString();
		row[7]=new Long(pass.getDumpDurationSecs()).toString();
		addRow(row);
	}
	/**
	 * Remove a GS pass from the FECS
	 * @param pass
	 */
	public void removePass(GsPass pass){
		passesSet.remove(pass);
	}
	
	/**
	 * Get all the Ground Station passes included in this Fecs, as an ordered set by GS pass start date
	 * @return the ordered set of GS passes
	 */
	public TreeSet<GsPass> getPasses(){
		return passesSet;
	}
	
	public String toString(){
		java.util.HashMap<String,Integer> counters=new java.util.HashMap<String,Integer>();
		String result="";
		result=result+"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		result=result+"<eventfile xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://esa.esoc.events rosetta_event_definitions.xsd\" xmlns=\"http://esa.esoc.events\" xmlns:ems=\"http://esa.esoc.ems\">\n";
		result=result+"\t<header format_version=\"1\" spacecraft=\""+this.getSpacecraft()+"\" icd_version=\""+this.getIcdVersion()+"\" gen_time=\""+EvtmEvent.dateToZulu(this.getGenerationTime())+"\" validity_start=\""+EvtmEvent.dateToZulu(this.getValidityStart())+"\" validity_end=\""+EvtmEvent.dateToZulu(this.getValidityEnd())+"\"/>\n";
		result=result+"\t<events>\n";
		Iterator<GsPass> it = passesSet.iterator();
		int count=1;
		while (it.hasNext()){
			result=result+it.next().toString(count);
			count++;
		}
		result=result+"\t</events>\n";
		result=result+"</eventfile>";
		return result;
		
	}
	
	/**
	 * Check if a given date is within a pass
	 * @param date
	 * @return True if the date is within a pass. False otherwise
	 */
	public boolean isInPass(Date date){
		boolean result=false;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass=it.next();
			if (date.equals(pass.getStartDump()));
			if (date.after(pass.getStartDump()) && date.before(pass.getEndDump())){
				result=true;
			}
		}
		return result;
	}
	
	public GsPass getGsDump(Date date){
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass=it.next();
			if (date.equals(pass.getStartDump())){
				return pass;
			}
			if (date.after(pass.getStartDump()) && date.before(pass.getEndDump())){
				return pass;
				//result=true;
			}
		}
		return null;
		
	}
	
	/*public void 	addRow(Object[] array) {
		//do nothing
	}*/
	public GsPass[] findOverlapingPasses(Date startDate,Date endDate){
		java.util.Vector<GsPass> affected=new java.util.Vector<GsPass>();
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			boolean con1=false;
			boolean con2=false;
			if (pass.getStartPass().after(endDate) ) con1=true;
			if (pass.getEndPass().before(startDate)) con2=true;
			if (!con1 && !con2) affected.add(pass);
			
		}
		GsPass[] result= new GsPass[affected.size()];
		result=affected.toArray(result);
		return result;
	}
	
	/**
	 * Get the number of passes in this FECS
	 * @return
	 */
	public int size(){
		return passesSet.size();
	}
	
	/**
	 * Compare 2 FECS and produce a report in HTML with the differences
	 * @param older Older fecs to compare
	 * @param newer Newer fecs to compare
	 * @return a String with the HTML code for the report
	 */
	public static String compareFecsHTML(Fecs older,Fecs newer){
		System.out.println(compareFecs(older,newer));
		String result="";
		TreeSet<GsPass> added=new TreeSet<GsPass>();
		TreeSet<GsPass> removed=new TreeSet<GsPass>();
		TreeSet<GsPass> shorterDump=new TreeSet<GsPass>();
		TreeSet<GsPass> largerDump=new TreeSet<GsPass>();
		TreeSet<GsPass> shorterPass=new TreeSet<GsPass>();
		TreeSet<GsPass> largerPass=new TreeSet<GsPass>();
		TreeSet<GsPass> bitrateChanged=new TreeSet<GsPass>();
		HashMap<GsPass,String> globalChanges=new HashMap<GsPass,String>();
		Iterator<GsPass> it = newer.getPasses().iterator();
		while (it.hasNext()){
			GsPass newerPass = it.next();
			GsPass[] olderPasses = older.getSubFecs(newerPass.getGroundStation()).findOverlapingPasses(newerPass.getStartPass(), newerPass.getEndPass());
			if (olderPasses.length==1){
				if (!newerPass.equals(olderPasses[0])){
					if (newerPass.getDumpDurationSecs()<olderPasses[0].getDumpDurationSecs()){
						shorterDump.add(newerPass);
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Dump shortened. Dump was "+olderPasses[0].getDumpDurationSecs()+" s, new dump duration is "+newerPass.getDumpDurationSecs()+" s\n<br>";
						globalChanges.put(newerPass, m);
					}
					if (newerPass.getDumpDurationSecs()>olderPasses[0].getDumpDurationSecs()){
						largerDump.add(newerPass);
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Dump enlarged. Dump was "+olderPasses[0].getDumpDurationSecs()+" s, new dump duration is "+newerPass.getDumpDurationSecs()+" s\n<br>";
						globalChanges.put(newerPass, m);
					}
					if (newerPass.getPassDurationSecs()<olderPasses[0].getPassDurationSecs()){
						shorterPass.add(newerPass);
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Pass shortened. Pass was "+olderPasses[0].getPassDurationSecs()+" s, new pass is "+newerPass.getPassDurationSecs()+" s\n<br>";
						globalChanges.put(newerPass, m);
	
					}
					if (newerPass.getPassDurationSecs()>olderPasses[0].getPassDurationSecs()){
						largerPass.add(newerPass);
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Pass enlarged. Pass was "+olderPasses[0].getPassDurationSecs()+" s, new pass is "+newerPass.getPassDurationSecs()+" s\n<br>";
						globalChanges.put(newerPass, m);

					}
					if (newerPass.getTmRate()!=olderPasses[0].getTmRate()){
						bitrateChanged.add(newerPass);
						//System.out.println("bitrate changed");
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Bitrate changed. Bitrate was "+olderPasses[0].getTmRate()+" bits/s, new pass is "+newerPass.getTmRate()+" bits/s\n<br>";
						globalChanges.put(newerPass, m);
					}

				}
			}
			else {
					if (olderPasses.length==0){
						added.add(newerPass);
					}else{
						for (int i=0;i<olderPasses.length;i++){
							removed.add(olderPasses[i]);
						}
						added.add(newerPass);

					}
					
			}
			
		}
		Iterator<GsPass> it2 = older.getPasses().iterator();
		while (it2.hasNext()){
			GsPass olderPass = it2.next();
			GsPass[] newerPasses = newer.getSubFecs(olderPass.getGroundStation()).findOverlapingPasses(olderPass.getStartPass(), olderPass.getEndPass());
			if (newerPasses.length<1){
				if (added.contains(olderPass)){
					added.remove(olderPass);
				}else{
					removed.add(olderPass);
				}
			}
		}
		if (added.size()>0){
			result=result+"<h2>GS passes added in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+PASS_TABLE_HEADER;
			Iterator<GsPass> addedIt = added.iterator();
			while (addedIt.hasNext()){
				result=result+passToHTMLRow(addedIt.next());
				//result=result+"--------------------------------------\n";

			}
			result=result+"</table><br>";

		}
		if (removed.size()>0){
			result=result+"<h2>GS passes removed in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+PASS_TABLE_HEADER;

			
			Iterator<GsPass> removedIt = removed.iterator();
			while (removedIt.hasNext()){
				result=result+passToHTMLRow(removedIt.next());
				//result=result+"--------------------------------------\n";

			}
			result=result+"</table><br>";

		}
		if (globalChanges.size()>0){
			result=result+"<h2>GS passes changed in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+GLOBAL_CHANGE_TABLE_HEADER_DURATION;
			Iterator<GsPass> changedIt = globalChanges.keySet().iterator();
			while (changedIt.hasNext()){
				GsPass pass = changedIt.next();
				//GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+globalChanges.get(pass)+"</td></tr>";
				result=result+row;
				
			}
			result=result+"</table><br>";

		}

		return result;
		
	}
	/**
	 * Compare 2 FECS and produce a report in ASCII with the differences
	 * @param older Older fecs to compare
	 * @param newer Newer fecs to compare
	 * @return a String with the ASCII code for the report
	 */	
	public static String compareFecs(Fecs older,Fecs newer){
		String result="";
		TreeSet<GsPass> added=new TreeSet<GsPass>();
		TreeSet<GsPass> removed=new TreeSet<GsPass>();
		TreeSet<GsPass> shorterDump=new TreeSet<GsPass>();
		TreeSet<GsPass> largerDump=new TreeSet<GsPass>();
		TreeSet<GsPass> shorterPass=new TreeSet<GsPass>();
		TreeSet<GsPass> largerPass=new TreeSet<GsPass>();
		TreeSet<GsPass> bitrateChanged=new TreeSet<GsPass>();
		
		Iterator<GsPass> it = newer.getPasses().iterator();
		while (it.hasNext()){
			GsPass newerPass = it.next();
			GsPass[] olderPasses = older.findOverlapingPasses(newerPass.getStartPass(), newerPass.getEndPass());
			if (olderPasses.length==1){
				if (!newerPass.equals(olderPasses[0])){
					if (newerPass.getDumpDurationSecs()<olderPasses[0].getDumpDurationSecs()){
						shorterDump.add(newerPass);
					}
					if (newerPass.getDumpDurationSecs()>olderPasses[0].getDumpDurationSecs()){
						largerDump.add(newerPass);
						//result=result+"Pass in "+larger.getName()+" larger than pass in "+shorter.getName()+":\n";
					}
					if (newerPass.getPassDurationSecs()<olderPasses[0].getPassDurationSecs()){
						shorterPass.add(newerPass);
					}
					if (newerPass.getPassDurationSecs()>olderPasses[0].getPassDurationSecs()){
						largerPass.add(newerPass);
						//result=result+"Pass in "+larger.getName()+" larger than pass in "+shorter.getName()+":\n";
					}
					if (newerPass.getTmRate()!=olderPasses[0].getTmRate()){
						bitrateChanged.add(newerPass);
						//result=result+"Pass in "+larger.getName()+" larger than pass in "+shorter.getName()+":\n";
					}


				}
			}
			else {
					if (olderPasses.length==0){
						added.add(newerPass);
					}else{
						for (int i=0;i<olderPasses.length;i++){
							removed.add(olderPasses[i]);
						}
						added.add(newerPass);

					}
					
			}
			
		}
		Iterator<GsPass> it2 = older.getPasses().iterator();
		while (it2.hasNext()){
			GsPass olderPass = it2.next();
			GsPass[] newerPasses = newer.findOverlapingPasses(olderPass.getStartPass(), olderPass.getEndPass());
			if (newerPasses.length<1){
				if (added.contains(olderPass)){
					added.remove(olderPass);
				}else{
					removed.add(olderPass);
				}
			}
		}
		if (added.size()>0){
			result=result+"**************************\n";
			result=result+"GS passes added in "+newer.getName()+":\n";
			result=result+"**************************\n";
			Iterator<GsPass> addedIt = added.iterator();
			while (addedIt.hasNext()){
				result=result+passToString(addedIt.next());
				result=result+"--------------------------------------\n";

			}

		}
		if (removed.size()>0){
			result=result+"**************************\n";
			result=result+"GS passes removed in "+newer.getName()+":\n";
			result=result+"**************************\n";
			Iterator<GsPass> removedIt = removed.iterator();
			while (removedIt.hasNext()){
				result=result+passToString(removedIt.next());
				result=result+"--------------------------------------\n";

			}

		}

		if (shorterDump.size()>0){
			result=result+"**************************\n";
			result=result+"GS passes shortened in "+newer.getName()+":\n";
			result=result+"**************************\n";
			Iterator<GsPass> shortIt = shorterDump.iterator();
			while (shortIt.hasNext()){
				GsPass pass = shortIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				result=result+passToString(pass);
				result=result+"Duration is:"+pass.getPassDurationSecs()+" s\n";
				result=result+"Duration was:"+oldPass.getPassDurationSecs()+" s\n";
				result=result+"--------------------------------------\n";

				
			}

		}
		if (largerDump.size()>0){
			result=result+"**************************\n";
			result=result+"GS passes enlarged in "+newer.getName()+":\n";
			result=result+"**************************\n";
			Iterator<GsPass> largerIt = largerDump.iterator();
			while (largerIt.hasNext()){
				GsPass pass = largerIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				result=result+passToString(pass);
				result=result+"Duration is:"+pass.getPassDurationSecs()+" s\n";
				result=result+"Duration was:"+oldPass.getPassDurationSecs()+" s\n";
				result=result+"--------------------------------------\n";

				
			}

		}

		return result;
		
	}
	/**
	 * Convert a pass to a HTML table row. The row has 7 columns:
	 * Pass type, Ground station,start pass date, end pass date,start dump date, end dump date and tm rate
	 * @param pass
	 * @return
	 */
	public static String passToHTMLRow(GsPass pass){
		String result="";
		String type="-";
		if (pass.is70m()) type="70 m";
		else {
			if (pass.getGroundStation().startsWith("DSS")) type="34 m";
			else type="35 m";
			
		}
		if (!pass.isBSR()){
			result=result+ "<tr>\n"
					+ "	<td>"+type+"</td><td>"+pass.getGroundStation()
					+"</td><td>"+DateUtil.defaultDateToString(pass.getStartPass())
					+"</td><td>"+DateUtil.defaultDateToString(pass.getEndPass())
					+"</td><td>"+DateUtil.defaultDateToString(pass.getStartDump())
					+"</td><td>"+DateUtil.defaultDateToString(pass.getEndDump())
					+"</td><td>"+pass.getTmRate()+"</td>\n"
					+ "</tr>\n"
					+ "\n";

			
			return result;
		}
		else{
			result=result+ "<tr>\n"
					+ "	<td>BSR</td><td>"+pass.getGroundStation()
					+"</td><td>"+DateUtil.defaultDateToString(pass.getStartPass())
					+"</td><td>"+DateUtil.defaultDateToString(pass.getEndPass())
					+"</td><td>-"
					+"</td><td>-"
					+"</td><td>-"
					+ "</tr>\n"
					+ "\n";

			
			return result;
			
		}
	}
	
	/**
	 * Get a string representation of a pass
	 * @param pass
	 * @return
	 */
	public static String passToString(GsPass pass){
		if (!pass.isBSR()){
			String result="";
			result=result+"Pass "+pass.getGroundStation()+" from "+DateUtil.defaultDateToString(pass.getStartPass())+" to "+DateUtil.defaultDateToString(pass.getEndPass())+"\n";
			result=result+"\tDump Start at:"+DateUtil.defaultDateToString(pass.getStartDump())+"\n";
			result=result+"\tDump End at:"+DateUtil.defaultDateToString(pass.getEndDump())+"\n";
			result=result+"TM bitrate:"+pass.getTmRate()+"\n";
			
			return result;
		}
		else{
			String result="";
			result=result+"BSR Pass "+pass.getGroundStation()+" from "+DateUtil.defaultDateToString(pass.getStartPass())+" to "+DateUtil.defaultDateToString(pass.getEndPass())+"\n";
			
			return result;
			
		}
	}
	/**
	 * Get a subfecs with all the GS passes from a station
	 * @param station Ground Station Name
	 * @return Fecs with all the passes from that station
	 */
	public Fecs getSubFecs(String station){
		Fecs result=new Fecs(this.getValidityStart(),this.getValidityEnd());
		result.setName(getName()+"_"+station);
		Iterator<GsPass> it = passesSet.iterator();
		while(it.hasNext()){
			GsPass pass = it.next();
			if (pass.getGroundStation().startsWith(station)) result.addPass(pass);
		}
		return result;
	}
	
	/**
	 * Get subfecs with all passes from DSN stations
	 * @return
	 */
	public Fecs getSubFecsDSN(){
		return getSubFecs("DSS");
	}
	/**
	 * Get subfecs with all passes from ESA stations
	 * @return
	 */	
	public Fecs getSubFecsESA(){
		Fecs result=new Fecs(this.getValidityStart(),this.getValidityEnd());
		result.setName(getName()+"_ESA");
		Iterator<GsPass> it = passesSet.iterator();
		while(it.hasNext()){
			GsPass pass = it.next();
			if (!pass.getGroundStation().startsWith("DSS")) result.addPass(pass);
		}
		return result;
		
	}
	
	/**
	 * Get subfecs with all pasess froma a date to a date
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Fecs getSubFecs(Date startDate,Date endDate){
		Fecs result=new Fecs(startDate,endDate);
		GsPass[] passes = this.findOverlapingPasses(startDate, endDate);
		for (int i=0;i<passes.length;i++){
			if (passes[i].getStartPass().after(startDate) || passes[i].getStartPass().equals(startDate)){
				result.addPass(passes[i]);
			}
		}
		
		return result;
	}
	
	/**
	 * Get all Ground station names in this FECS
	 * @return
	 */
	public String[] getStations(){
		TreeSet<String> stationsSet=new TreeSet<String>();
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			stationsSet.add(it.next().getGroundStation());
		}
		String[] result=new String[stationsSet.size()];
		result=stationsSet.toArray(result);
		return result;
	}
	/**
	 * Get total data dump in this FECS
	 * @return
	 */
	public float getTotalDataDump(){
		float result=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			result=result+pass.getTotalDataDump();
		}
		return result;
		
	}
	/**
	 * Get total data dump from a particular station
	 * @param station Ground Station Name
	 * @return
	 */
	public float getTotalDataDump(String station){
		float result=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (pass.getGroundStation().startsWith(station)) result=result+pass.getTotalDataDump();
		}
		return result;
	}
	/**
	 * Get total number of hours of passes per day
	 * @return
	 */
	public float getHoursDay(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			totalSecs=totalSecs+pass.getPassDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		
		return totalHours/fecsDays;
		
	}
	/**
	 * Get the total number of hours of dump per day of 70m antennas
	 * @return
	 */
	public float getDumpHoursDay70m(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (pass.is70m() && !pass.isBSR()) totalSecs=totalSecs+pass.getDumpDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));

		
		return totalHours/fecsDays;
		
	}
	/**
	 * Get the total number of hours of passes per day of 70m antennas
	 * @return
	 */
	public float getPassHoursDay70m(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (pass.is70m() && !pass.isBSR()) totalSecs=totalSecs+pass.getPassDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));

		
		return totalHours/fecsDays;
		
	}
	/**
	 * Get the total number of hours of dump per day of 35m antennas
	 * @return
	 */
	public float getDumpHoursDay35m(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (!pass.is70m() && !pass.isBSR()) totalSecs=totalSecs+pass.getDumpDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));
		System.out.println("Fecs Days:"+fecsDays);
		return totalHours/fecsDays;
		
	}
	/**
	 * Get the total number of hours of passes per day of 35m antennas
	 * @return
	 */
	public float getPassHoursDay35m(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (!pass.is70m() && !pass.isBSR()) totalSecs=totalSecs+pass.getPassDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));
		System.out.println("Fecs Days:"+fecsDays);
		return totalHours/fecsDays;
		
	}
	/**
	 * Get the total number of hours of BSR passes
	 * @return
	 */
	public float getBSRHours(){
		float totalSecs=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (pass.isBSR()) totalSecs=totalSecs+pass.getPassDurationSecs();
		}
		float totalHours=(totalSecs/60)/60;
		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		
		return totalHours;
		
	}
	/**
	 * Get the total number of hours of BSR per day
	 * @return
	 */
	public float getBSRHoursDay(){
		float totalHours=getBSRHours();
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));

		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		
		return totalHours/fecsDays;
		
	}
	
	/**
	 * Get summary of the FECS in html
	 * @return astring with the HTML code
	 */
	public String getFecsSummaryTableHTML(){
		String message="";
		//message=message+"<h1>"+getName()+"</h1>";
		message=message+ "<table class=\"gridtable\">\n"
				+ "<tr>\n"
				+ "	<th>Name</th><th>Start Date</th><th>End Date</th>\n"
				+ "</tr>\n";
		message=message+ "<tr>\n"
				+ "	<td>"+getName()+"</td><td>"+DateUtil.defaultDateToString(this.getValidityStart())+"</td><td>"+DateUtil.defaultDateToString(this.getValidityEnd())+"</td>\n"
				+ "</tr>\n"
				+ "</table>\n";
		message=message+"<br><br>";
		message=message+ "<table class=\"gridtable\">\n"
				+ "<tr>\n"
				+ "	<th>FECS</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
				+ "</tr>\n";
		/*message=message+ "<tr>\n"
				+ "	<td>"+getName()+"</td><td>"+getHoursDay35m()+"</td><td>"+getHoursDay70m()+"</td><td>"+getBSRHoursDay()+"</td><td>"+getTotalDataDump()+"</td>\n"
				+ "</tr></table>\n";*/
		message=message+getFecsSummaryRowHTML(getName());
		message=message+"</table>\n";
		
		return message;

	}
	public String getFecsSummaryRowHTML(String name){
		String message="";
		//message=message+"<h1>"+getName()+"</h1>";
		message=message+ "<tr>\n"
				+ "	<td>"+name+"</td><td>"+getPassHoursDay35m()+"</td><td>"+getDumpHoursDay35m()+"</td><td>"+getPassHoursDay70m()+"</td><td>"+getDumpHoursDay70m()+"</td><td>"+getBSRHoursDay()+"</td><td>"+getTotalDataDump()+"</td>\n"
				+ "</tr>";
		
		return message;

	}
	@Override
	public String getXmlData() {
		// TODO Auto-generated method stub
		return toString();
	}
	@Override
	public void setFileName(String newFileName) {
		setName(newFileName);
		
	}
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return this.getName();
	}
	@Override
	public void setXmlData(String data) {
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
			Document doc;
	
			doc = dBuilder.parse(stream);
			Fecs tempFecs = Fecs.readFromDoc(doc);
			setGenerationTime(tempFecs.getGenerationTime());
			setValidityStart(tempFecs.getValidityStart());
			setValidityEnd(tempFecs.getValidityEnd());
			setSpacecraft(tempFecs.getSpacecraft());
			setIcdVersion(tempFecs.getIcdVersion());
			this.deleteAllPasses();
			TreeSet<GsPass> passes = tempFecs.getPasses();
			Iterator<GsPass> it = passes.iterator();
			while(it.hasNext()){
				this.addPass(it.next());
			}
		}catch (Exception e){
			IllegalArgumentException iae=new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		// TODO Auto-generated method stub
		
	}
	/**
	 * Write FECS to a file
	 * @param file full path of the file to save
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void writeToFile(String file) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.print(getXmlData());
		writer.close();
}


	public void save() throws FileNotFoundException, UnsupportedEncodingException{
		writeToFile(getPath()+"//"+getFileName());
	}
	@Override
	public void saveAs(String file) throws FileNotFoundException,UnsupportedEncodingException {
		writeToFile(file);
	}

	protected static Fecs readFromDoc(Document doc) throws DOMException, ParseException{
		Fecs result=new Fecs();


		doc.getDocumentElement().normalize();

		NodeList nListHeader = doc.getElementsByTagName("header");
		NamedNodeMap headerAttributes = nListHeader.item(0).getAttributes();
		result.setSpacecraft(headerAttributes.getNamedItem("spacecraft").getNodeValue());
		result.setIcdVersion(headerAttributes.getNamedItem("icd_version").getNodeValue());
		result.setGenerationTime(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("gen_time").getNodeValue()));
		result.setValidityStart(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_start").getNodeValue()));
		result.setValidityEnd(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_end").getNodeValue()));
		Node nodeEvents = doc.getElementsByTagName("events").item(0);
		NodeList fcsNodeList = nodeEvents.getChildNodes();
		TreeMap<Date,Node> botNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> stadNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> stodNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> eotNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> boabNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> eoabNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> boalNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> eoalNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> botrNodes=new TreeMap<Date,Node>();
		TreeMap<Date,Node> eotrNodes=new TreeMap<Date,Node>();

		int size=fcsNodeList.getLength();
		for (int i=0;i<size;i++){
			Node item = fcsNodeList.item(i);
			NamedNodeMap attributes = item.getAttributes();
			/*if (attributes!=null){
				Date time=DateUtil.DOYToDate(item.getAttributes().getNamedItem("time").getTextContent());
				NamedNodeMap att = item.getAttributes();
				int attsize = att.getLength();
				for (int u=0;u<attsize;u++) {
				    System.out.println(att.item(u).getNodeName());
				}
				System.out.println(item.getAttributes().getNamedItem("id"));
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOT_")){
					botNodes.put(time,item);

				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STAD")){
					stadNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STOD")){
					stodNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOT_")){
					eotNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOAB")){
					boabNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOAB")){
					eoabNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOAL")){
					boalNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOAL")){
					eoalNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOTR")){
					//System.out.println("Detected BOTR");
					botrNodes.put(time,item);
				}
				if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOTR")){
					eotrNodes.put(time,item);
				}



			}*/
		}
		Iterator<Date> it = botNodes.keySet().iterator();
		while (it.hasNext()){
			Date passStart=it.next();
			Date passEnd=eotNodes.ceilingKey(new Date(passStart.getTime()+1));
			if (passEnd==null){
				VegaLog.severe("Could not get pass end (EOT) for pass starting "+DateUtil.defaultDateToString(passStart));
			}else{
				Node endNode = eotNodes.get(passEnd);
				String stationEnd = endNode.getAttributes().getNamedItem("ems:station").getNodeValue();
				Node startNode=botNodes.get(passStart);
				String stationStart=startNode.getAttributes().getNamedItem("ems:station").getNodeValue();
				if (!stationEnd.equals(stationStart)){
					VegaLog.severe("Pass not included. Overlapping passes BOT station "+stationStart+" EOT station "+stationEnd);
					VegaLog.severe("BOT count "+startNode.getAttributes().getNamedItem("count").getNodeValue());
					VegaLog.severe("EOT count "+endNode.getAttributes().getNamedItem("count").getNodeValue());
					//throw new IllegalArgumentException("FECS format invalid. Overlapping passes BOT count "+countStart+" EOT count "+countEnd);
				}
				else{
					SortedMap<Date, Node> subStadMap = stadNodes.subMap(passStart, passEnd);
					Iterator<Date> it2 = subStadMap.keySet().iterator();
					while(it2.hasNext()){
						Date dumpStart=it2.next();
						Date dumpEnd=stodNodes.ceilingKey(new Date(dumpStart.getTime()+1));
						//Node dumpStartNode=subStadMap.get(dumpStart);
						//Node dumpStopNode=
						String station=botNodes.get(passStart).getAttributes().getNamedItem("ems:station").getTextContent();
						float tmRate=Float.parseFloat(stadNodes.get(dumpStart).getAttributes().getNamedItem("tm_rate").getTextContent());
						String stadStation = stadNodes.get(dumpStart).getAttributes().getNamedItem("ems:station").getTextContent();
						String stodStation = stodNodes.get(dumpEnd).getAttributes().getNamedItem("ems:station").getTextContent();
						if (stadStation.equals(stodStation)){
							GsPass pass=new GsPass(passStart,passEnd,dumpStart,dumpEnd,station,tmRate);
							result.addPass(pass);
						}else{
							VegaLog.severe("Pass not included. Overlapping passes STAD station "+stadStation+" STOD station "+stodStation);
							VegaLog.severe("STAD count "+stadNodes.get(dumpStart).getAttributes().getNamedItem("count").getTextContent());
							VegaLog.severe("STOD count "+stodNodes.get(dumpEnd).getAttributes().getNamedItem("count").getTextContent());
	
						}
					}
				}

			}


		}
		return result;
	}
			
}
	


