package vega.uplink.commanding;
import herschel.ia.dataset.Column;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.time.FineTime;

import java.util.HashMap;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import vega.uplink.Properties;
import vega.uplink.pointing.EvtmEvent;
import vega.uplink.pointing.PointingBlock;

/**
* The Fecs class is a data model to store the information from the FECS file.
* This file stores the information about Ground station passes and downlink rate.
*
* @author  Javier Arenas
* @version 1.0
*/
/**
 * @author jarenas
 *
 */
/**
 * @author jarenas
 *
 */
public class Fecs extends TableDataset{
	private TreeSet<GsPass> passesSet;
	private static String PASS_TABLE_HEADER=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th>\n"
			+ "</tr>\n";
	private static String GLOBAL_CHANGE_TABLE_HEADER_DURATION=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th><th>Change</th>\n"
			+ "</tr>\n";

	private static String DUMP_TABLE_HEADER_DURATION=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th><th>Dump is (s)</th><th>Dump was (s)</th>\n"
			+ "</tr>\n";
	private static String PASS_DURATION_TABLE_HEADER=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th><th>Duration is (s)</th><th>Duration was (s)</th>\n"
			+ "</tr>\n";
	private static String BITRATE_CHANGE_TABLE_HEADER=""
			+ "<tr>\n"
			+ "	<th>Type</th><th>Station</th><th>Start Pass</th><th>End Pass</th><th>Start Dump</th><th>End Dump</th><th>Bitrate</th><th>Bitrate is </th><th>Bitrate was </th>\n"
			+ "</tr>\n";

	//private Date generationTime;
	//private Date validityStart;
	//private Date validityEnd;
	//private String spacecraft;
	//private String icdVersion;
	/*private Fecs(TreeSet<GsPass> passes){
		super();
		passesSet=passes;
		setGenerationTime(new Date());
		setValidityStart(passes.first().getStartPass());
		setValidityEnd(passes.last().getEndPass());
		setSpacecraft("ROS");
		setIcdVersion("PLID-0.0");
		Column gs=new Column(new String1d());
		Column stpass=new Column(new String1d());
		Column edpass=new Column(new String1d());
		Column stdump=new Column(new String1d());
		Column eddump=new Column(new String1d());
		Column tmRate=new Column(new String1d());
		
		this.addColumn(gs);		
		this.addColumn(stpass);
		this.addColumn(edpass);
		this.addColumn(stdump);
		this.addColumn(eddump);
		this.addColumn(tmRate);
		this.setColumnName(0, "Ground Station");
		this.setColumnName(1, "Start pass");
		this.setColumnName(2, "End Pass");
		this.setColumnName(3, "Start Dump");
		this.setColumnName(4, "End Dump");
		this.setColumnName(5, "TM rate");


	}*/
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

	/**
	 * Constructor of the Fecs class taking as parameters all the needed information, but assuming the spacecraft to be ROSETTA.
	 * @param fecsGenerationTime Generation time of the FECS file
	 * @param fecsValidityStart Start time from which this product is applicable.
	 * @param fecsValidityEnd End time until which this product is applicable
	 * @param fecsIcdVersion The version of the icd (Interface Control Document) that applies to this product
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
	
	public Date getValidityStart(){
		return ((FineTime) getMeta().get("StartTime").getValue()).toDate();
		//return validityStart;
	}
	
	public void setValidityStart(Date evtmValidityStart){
		getMeta().set("StartTime", new DateParameter(new FineTime(evtmValidityStart)));
		//validityStart=evtmValidityStart;
	}
	
	public void setValidityEnd(Date evtmValidityEnd){
		getMeta().set("EndTime", new DateParameter(new FineTime(evtmValidityEnd)));
		//validityEnd=evtmValidityEnd;
	}
	
	public void setSpacecraft(String evtmSpacecraft){
		getMeta().set("Spacecraft", new StringParameter(evtmSpacecraft));
		//spacecraft=evtmSpacecraft;
	}
	
	public void setIcdVersion(String evtmIcdVersion){
		getMeta().set("ICDversion", new StringParameter(evtmIcdVersion));

		//icdVersion=evtmIcdVersion;
	}
	
	public Date getValidityEnd(){
		return ((FineTime) getMeta().get("EndTime").getValue()).toDate();
		//return validityEnd;
	}
	
	public String getSpacecraft(){
		return (String) getMeta().get("Spacecraft").getValue();
		//return spacecraft;
	}
	
	public String getIcdVersion(){
		return (String) getMeta().get("ICDversion").getValue();

		//return icdVersion;
	}
	
	public void addPass(GsPass pass){
		passesSet.add(pass);
		String[] row=new String[8];
		row[0]=pass.getGroundStation();
		row[1]=GsPass.dateToZulu(pass.getStartPass());
		row[2]=GsPass.dateToZulu(pass.getEndPass());
		row[3]=GsPass.dateToZulu(pass.getStartDump());
		row[4]=GsPass.dateToZulu(pass.getEndDump());
		row[5]=new Float(pass.getTmRate()).toString();
		row[6]=new Long(pass.getPassDurationSecs()).toString();
		row[7]=new Long(pass.getDumpDurationSecs()).toString();

		addRow(row);
		
		//addRown(pass.get)
		
	}
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
	
	public int size(){
		return passesSet.size();
	}
	public static String compareFecsHTML(Fecs older,Fecs newer){
		System.out.println(compareFecs(older,newer));
		String result="";
		/*Fecs larger;
		Fecs shorter;
		if (fecs1.size()<fecs2.size()){
			larger=fecs2;
			shorter=fecs1;
		}else{
			larger=fecs1;
			shorter=fecs2;
		}
		//TreeSet<GsPass> passes1 = larger.getPasses();*/
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
	
						//result=result+"Pass in "+larger.getName()+" larger than pass in "+shorter.getName()+":\n";
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

						//result=result+"Pass in "+larger.getName()+" larger than pass in "+shorter.getName()+":\n";
					}
					if (newerPass.getTmRate()!=olderPasses[0].getTmRate()){
						bitrateChanged.add(newerPass);
						//System.out.println("bitrate changed");
						String m = globalChanges.get(newerPass);
						if (m==null) m="";
						m=m+"Bitrate changed. Bitrate was "+olderPasses[0].getTmRate()+" bits/s, new pass is "+newerPass.getTmRate()+" bits/s\n<br>";
						globalChanges.put(newerPass, m);

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
		/*if (shorterDump.size()>0){
			result=result+"<h2>GS passes dump shortened in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+DUMP_TABLE_HEADER_DURATION;

			Iterator<GsPass> shortIt = shorterDump.iterator();
			while (shortIt.hasNext()){
				GsPass pass = shortIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+pass.getDumpDurationSecs()+"</td>"+"<td>"+oldPass.getDumpDurationSecs()+"</td></tr>";
				result=result+row;
				
			}
			result=result+"</table><br>";

		}
		if (largerDump.size()>0){
			result=result+"<h2>GS passes dump enlarged in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+DUMP_TABLE_HEADER_DURATION;


			Iterator<GsPass> largerIt = largerDump.iterator();
			while (largerIt.hasNext()){
				GsPass pass = largerIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+pass.getDumpDurationSecs()+"</td>"+"<td>"+oldPass.getDumpDurationSecs()+"</td></tr>";
				result=result+row;

				
			}
			result=result+"</table><br>";

		}

		if (shorterPass.size()>0){
			result=result+"<h2>GS passes duration shortened in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+PASS_DURATION_TABLE_HEADER;

			Iterator<GsPass> shortIt = shorterPass.iterator();
			while (shortIt.hasNext()){
				GsPass pass = shortIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+pass.getPassDurationSecs()+"</td>"+"<td>"+oldPass.getPassDurationSecs()+"</td></tr>";
				result=result+row;
				
			}
			result=result+"</table><br>";

		}
		if (largerPass.size()>0){
			result=result+"<h2>GS passes duration enlarged in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+PASS_DURATION_TABLE_HEADER;


			Iterator<GsPass> largerIt = largerPass.iterator();
			while (largerIt.hasNext()){
				GsPass pass = largerIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+pass.getPassDurationSecs()+"</td>"+"<td>"+oldPass.getPassDurationSecs()+"</td></tr>";
				result=result+row;

				
			}
			result=result+"</table><br>";

		}
		if (bitrateChanged.size()>0){
			result=result+"<h2>GS passes bitrate changed in "+newer.getName()+"</h2>\n";
			result=result+"<br>\n";
			
			result=result+"<table class=\"gridtable\">\n"+BITRATE_CHANGE_TABLE_HEADER;


			Iterator<GsPass> largerIt = bitrateChanged.iterator();
			while (largerIt.hasNext()){
				GsPass pass = largerIt.next();
				GsPass oldPass = older.findOverlapingPasses(pass.getStartPass(), pass.getEndPass())[0];
				String row = passToHTMLRow(pass);
				row=row.replace("</tr>", "");
				row=row+"<td>"+pass.getTmRate()+"</td>"+"<td>"+oldPass.getTmRate()+"</td></tr>";
				result=result+row;

				
			}
			result=result+"</table><br>";

		}*/

		return result;
		
	}
	
	public static String compareFecs(Fecs older,Fecs newer){
		String result="";
		/*Fecs larger;
		Fecs shorter;
		if (fecs1.size()<fecs2.size()){
			larger=fecs2;
			shorter=fecs1;
		}else{
			larger=fecs1;
			shorter=fecs2;
		}
		//TreeSet<GsPass> passes1 = larger.getPasses();*/
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
					+"</td><td>"+PointingBlock.dateToZulu(pass.getStartPass())
					+"</td><td>"+PointingBlock.dateToZulu(pass.getEndPass())
					+"</td><td>"+PointingBlock.dateToZulu(pass.getStartDump())
					+"</td><td>"+PointingBlock.dateToZulu(pass.getEndDump())
					+"</td><td>"+pass.getTmRate()+"</td>\n"
					+ "</tr>\n"
					+ "\n";

			//result=result+"Pass "+pass.getGroundStation()+" from "+PointingBlock.dateToZulu(pass.getStartPass())+" to "+PointingBlock.dateToZulu(pass.getEndPass())+"\n";
			//result=result+"\tDump Start at:"+PointingBlock.dateToZulu(pass.getStartDump())+"\n";
			//result=result+"\tDump End at:"+PointingBlock.dateToZulu(pass.getEndDump())+"\n";
			//result=result+"TM bitrate:"+pass.getTmRate()+"\n";
			
			return result;
		}
		else{
			result=result+ "<tr>\n"
					+ "	<td>BSR</td><td>"+pass.getGroundStation()
					+"</td><td>"+PointingBlock.dateToZulu(pass.getStartPass())
					+"</td><td>"+PointingBlock.dateToZulu(pass.getEndPass())
					+"</td><td>-"
					+"</td><td>-"
					+"</td><td>-"
					+ "</tr>\n"
					+ "\n";

			//result=result+"BSR Pass "+pass.getGroundStation()+" from "+PointingBlock.dateToZulu(pass.getStartPass())+" to "+PointingBlock.dateToZulu(pass.getEndPass())+"\n";
			
			return result;
			
		}
	}
	
	public static String passToString(GsPass pass){
		if (!pass.isBSR()){
			String result="";
			result=result+"Pass "+pass.getGroundStation()+" from "+PointingBlock.dateToZulu(pass.getStartPass())+" to "+PointingBlock.dateToZulu(pass.getEndPass())+"\n";
			result=result+"\tDump Start at:"+PointingBlock.dateToZulu(pass.getStartDump())+"\n";
			result=result+"\tDump End at:"+PointingBlock.dateToZulu(pass.getEndDump())+"\n";
			result=result+"TM bitrate:"+pass.getTmRate()+"\n";
			
			return result;
		}
		else{
			String result="";
			result=result+"BSR Pass "+pass.getGroundStation()+" from "+PointingBlock.dateToZulu(pass.getStartPass())+" to "+PointingBlock.dateToZulu(pass.getEndPass())+"\n";
			
			return result;
			
		}
	}
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
	
	public Fecs getSubFecsDSN(){
		return getSubFecs("DSS");
	}
	
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
	public float getTotalDataDump(){
		float result=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			result=result+pass.getTotalDataDump();
		}
		return result;
		
	}
	public float getTotalDataDump(String station){
		float result=0;
		Iterator<GsPass> it = passesSet.iterator();
		while (it.hasNext()){
			GsPass pass = it.next();
			if (pass.getGroundStation().startsWith(station)) result=result+pass.getTotalDataDump();
		}
		return result;
	}
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

	public float getBSRHoursDay(){
		float totalHours=getBSRHours();
		float fecsDays =  ((this.getValidityEnd().getTime()-this.getValidityStart().getTime()) / (1000*60*60*24));

		//float fecsDays=((((this.getValidityEnd().getTime()-this.getValidityStart().getTime())*1000)/60)/60)/24;
		
		return totalHours/fecsDays;
		
	}
	
	public String getFecsSummaryTableHTML(){
		String message="";
		//message=message+"<h1>"+getName()+"</h1>";
		message=message+ "<table class=\"gridtable\">\n"
				+ "<tr>\n"
				+ "	<th>Name</th><th>Start Date</th><th>End Date</th>\n"
				+ "</tr>\n";
		message=message+ "<tr>\n"
				+ "	<td>"+getName()+"</td><td>"+PointingBlock.dateToZulu(this.getValidityStart())+"</td><td>"+PointingBlock.dateToZulu(this.getValidityEnd())+"</td>\n"
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

	//public GsPass()

}
	


