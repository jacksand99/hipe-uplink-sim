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

import vega.uplink.Properties;
import vega.uplink.pointing.EvtmEvent;

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
	//private Date generationTime;
	//private Date validityStart;
	//private Date validityEnd;
	//private String spacecraft;
	//private String icdVersion;
	
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
		String[] row=new String[6];
		row[0]=pass.getGroundStation();
		row[1]=GsPass.dateToZulu(pass.getStartPass());
		row[2]=GsPass.dateToZulu(pass.getEndPass());
		row[3]=GsPass.dateToZulu(pass.getStartDump());
		row[4]=GsPass.dateToZulu(pass.getEndDump());
		row[5]=new Float(pass.getTmRate()).toString();
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
	
	public GsPass getGsPass(Date date){
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
			if (pass.getStartPass().after(endDate)) con1=true;
			if (pass.getEndPass().before(startDate)) con2=true;
			if (!con1 && !con2) affected.add(pass);
			
		}
		GsPass[] result= new GsPass[affected.size()];
		result=affected.toArray(result);
		return result;
	}

}
	


