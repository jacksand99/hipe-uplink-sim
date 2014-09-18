package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.DatasetListener;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.interpreter.InterpreterUtil;
//import herschel.share.util.Configuration;
import herschel.ia.dataset.*;
import herschel.ia.numeric.Double1d;
import herschel.ia.numeric.String1d;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import vega.uplink.Properties;

public class Sequence extends Product {
	public static String INSERT_FLAG = "Insert";
	public static String DELETE_FLAG = "Delete";
	//Parameter[] parameters;
	
	//TableDataset profilesTable;
	
	public Sequence (String sequenceName,String sequenceID,String sequenceFlag,char sequenceSource,char sequenceDestination,java.util.Date sequenceExecutionTime,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles){
		super();
		getMeta().set("name", new StringParameter(sequenceName));
		getMeta().set("uniqueID", new StringParameter(sequenceID));
		getMeta().set("executiontime",new DateParameter(new FineTime(sequenceExecutionTime)));
		getMeta().set("source", new StringParameter(""+sequenceSource));
		getMeta().set("destination", new StringParameter(""+sequenceDestination));
		getMeta().set("flag", new StringParameter(sequenceFlag));
		setInstrument(getInstrumentName());

		//parameters=sequenceParamaters;
		if (sequenceParamaters!=null)setParameters(sequenceParamaters);
		else set("parameters",new CompositeDataset());
		TableDataset profilesTable=createProfilesTable();
		if (sequenceProfiles!=null){
			for (int i=0;i<sequenceProfiles.length;i++){
				profilesTable.concatenate(sequenceProfiles[i]);
			}
		}
		set("profiles",profilesTable);

	}
	
	private TableDataset createProfilesTable(){
		TableDataset proTable=new TableDataset();
		Column cType=new Column(new String1d());
		Column cOffset=new Column(new String1d());
		Column cValue=new Column(new Double1d());
		proTable.addColumn(cType);
		proTable.addColumn(cOffset);
		proTable.addColumn(cValue);
		//addColumn(cValue);
		proTable.setColumnName(0, SequenceProfile.COLUMN_NAME_TYPE);
		proTable.setColumnName(1, SequenceProfile.COLUMN_NAME_OFFSET);
		proTable.setColumnName(2, SequenceProfile.COLUMN_NAME_VALUE);
		return proTable;

	}
	
	public Sequence(Sequence seq) throws ParseException{
		this(new String(seq.getName()),new String(seq.getUniqueID()),new String(seq.getFlag()),new Character(seq.getSource()),new Character(seq.getDestination()),new String(seq.getExecutionTime()),seq.getParameters(),seq.getProfiles());
	}
	public Sequence copy(){
		Sequence result;
		try {
			result=new Sequence(this);
		} catch (ParseException e) {
			result= new Sequence(new String(getName()),new String(getUniqueID()),new String(getFlag()),new Character(getSource()),new Character(getDestination()),new java.util.Date(),getParameters(),getProfiles().clone());

			e.printStackTrace();
		}
		return result;
	}
	
	public Sequence (String sequenceName,String sequenceID,String sequenceFlag,char sequenceSource,char sequenceDestination,String sequenceExecutionTime,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles) throws ParseException{
		this(sequenceName,sequenceID,sequenceFlag,sequenceSource,sequenceDestination,zuluToDate(sequenceExecutionTime),sequenceParamaters,sequenceProfiles);
	}
	
	public Sequence (String sequenceName,String sequenceID,String sequenceFlag,String sequenceExecutionTime,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles) throws ParseException{
		this(sequenceName,sequenceID,sequenceFlag,'P','S',zuluToDate(sequenceExecutionTime),sequenceParamaters,sequenceProfiles);
	}
	
	public Sequence (String sequenceName,String sequenceID,String sequenceExecutionTime,Parameter[] sequenceParamaters,SequenceProfile[] sequenceProfiles) throws ParseException{
		this(sequenceName,sequenceID,INSERT_FLAG,'P','S',zuluToDate(sequenceExecutionTime),sequenceParamaters,sequenceProfiles);
	}
	
	public Sequence (String sequenceName,String sequenceID,String sequenceExecutionTime) throws ParseException{

		this(sequenceName,sequenceID,INSERT_FLAG,'P','S',zuluToDate(sequenceExecutionTime),null,null);
	
	}
	
	public static java.util.Date zuluToDate(String zuluTime) throws ParseException{
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime);
	}
	
	public String getName(){
		try{
			return (String) getMeta().get("name").getValue();
		}catch (java.lang.NullPointerException nullEx){
			nullEx.printStackTrace();
			System.out.println(this.getClass());
			System.out.println(this.getUniqueID());
			System.out.println(this.getProfiles());
			System.out.println(this.getExecutionTime());
			
			System.out.println(this.toString());

			return "UNKNOWN";
		}
	}
	
	public String getUniqueID(){
		return (String) getMeta().get("uniqueID").getValue();

	}
	
	public String getFlag(){
		return (String) getMeta().get("flag").getValue();
	}
	
	public char getSource(){
		return ((String) getMeta().get("flag").getValue()).charAt(0);

	}
	
	public char getDestination(){
		return ((String) getMeta().get("destination").getValue()).charAt(0);

	}
	
	public java.util.Date getExecutionDate(){
		return ((FineTime) getMeta().get("executiontime").getValue()).toDate();

	}
	
	public static String dateToZulu(java.util.Date date){
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yyyy-D'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.format(date);
	}
	
	public String getExecutionTime(){
		return dateToZulu(getExecutionDate());
	}
	public Parameter getParameter(String parameterName){
		CompositeDataset parameters = (CompositeDataset) get("parameters");
		return (Parameter)parameters.get(parameterName);
		
	}
	public void setEngParameter(String paramName,String value){
		((ParameterString) getParameter(paramName)).setValue(value);
	}
	public Parameter[] getParameters(){
		CompositeDataset parameters = (CompositeDataset) get("parameters");
		Parameter[] result=new Parameter[parameters.size()];
		Iterator<String> it = parameters.keySet().iterator();
		for (int i=0;i<result.length;i++){
			String key=it.next();
			result[i]=(Parameter) parameters.get(key);
		}
		return result;
		/*if (parameters!=null){
			return parameters;
			
		}
		else{
			return new Parameter[0];
		}*/
	}
	
	public SequenceProfile[] getProfiles(){
		TableDataset profilesTable = (TableDataset) get("profiles");
		if (profilesTable==null) return new SequenceProfile[0];
		int size=profilesTable.getRowCount();
		SequenceProfile[] result=new SequenceProfile[size];
		for (int i=0;i<size;i++){
			result[i]=new SequenceProfile((String) profilesTable.getValueAt(i, 0),(String) profilesTable.getValueAt(i, 1),(Double) profilesTable.getValueAt(i, 2));
		}
		return result;
	}
	
	public void setName(String sequenceName){
		getMeta().set("name", new StringParameter(sequenceName));
		setInstrument(getInstrumentName());
	}
	
	public void setUniqueID(String sequenceID){
		getMeta().set("uniqueID", new StringParameter(sequenceID));

	}
	
	public void setFlag (String sequenceFlag){
		getMeta().set("flag", new StringParameter(sequenceFlag));

	}
	
	public void setSource(char sequenceSource){
		getMeta().set("source", new StringParameter(""+sequenceSource));

	}
	
	public void setDestination(char sequenceDestination){
		getMeta().set("destination", new StringParameter(""+sequenceDestination));

	}
	
	public void setExecutionDate(java.util.Date date){
		getMeta().set("executiontime",new DateParameter(new FineTime(date)));

	}
	
	public void setExecutionTime(String time) throws ParseException{
		setExecutionDate(zuluToDate(time));

	}
	
	/*public Dataset get(String name){
		if (name.equals("profiles")){
			return profilesTable;
		}else{
			Parameter[] params=getParameters();
			for (int i=0;i<params.length;i++){
				if (params[i].getName().equals(name)) return params[i];
			}
			
		}
		
		return null;
	}*/
	
	/*public void set(String name, Dataset newDataset){
		if (InterpreterUtil.isInstance(Parameter.class, newDataset)){
			Parameter newParameter=(Parameter) newDataset;
			Parameter[] params=getParameters();
			for (int i=0;i<params.length;i++){
				if (params[i].getName().equals(name)){
					params[i]=newParameter;
					return;
				}
			}
			
		}
		if (name.equals("profiles")){
			if (!InterpreterUtil.isInstance(TableDataset.class, newDataset)) return;
			TableDataset newTableDataset=(TableDataset) newDataset;
			if (newTableDataset.getColumnCount()!=3) return;
			if (!InterpreterUtil.isInstance(String1d.class,newTableDataset.getColumn(0).getData())) return;
			if (!InterpreterUtil.isInstance(String1d.class,newTableDataset.getColumn(1).getData())) return;
			if (!InterpreterUtil.isInstance(Double1d.class,newTableDataset.getColumn(2).getData())) return;
			profilesTable=newTableDataset;

		}
	}*/
	/*public boolean containsKey(String name){
		if (name.equals("profiles")) return true;
		Parameter[] params=getParameters();
		for (int i=0;i<params.length;i++){
			if (params[i].getName().equals(name)) return true;
			
		}
		return false;

		
	}*/
	
	/*public int size(){
		return getParameters().length+1;
	}*/
	/*public Set<String> keySet(){
		HashSet<String> result=new HashSet<String>();
		Parameter[] params=getParameters();
		for (int i=0;i<params.length;i++){
			result.add(params[i].getName());
		}
		result.add("profiles");
		return result;

	}*/
	
	/*public boolean isEmpty(){
		return false;
	}*/
	
	/*public boolean contains(Object o){
		if (InterpreterUtil.isInstance(Parameter.class, o)){
			String name=((Parameter) o).getName();
			return containsKey(name);
		}
		if (InterpreterUtil.isInstance(SequenceProfile.class, o)){
			SequenceProfile obj=(SequenceProfile) o;
			
			SequenceProfile[] sProfiles=getProfiles();
			for (int i=0;i<sProfiles.length;i++){
				if (sProfiles[i].equal(obj)) return true;
			}
		}
		return false;
	}*/
	
	/*public Map<String,Dataset> getSets(){
		HashMap<String,Dataset> result=new HashMap<String,Dataset>();
		Set<String> names = this.keySet();
		Iterator<String> it = names.iterator();
		while (it.hasNext()){
			String name=it.next();
			result.put(name, get(name));
		}
		
		return result;
	}*/

	public void setParameters(Parameter[] sequenceParameters){
		CompositeDataset parameters=new CompositeDataset();
		for (int i=0;i<sequenceParameters.length;i++){
			parameters.set(sequenceParameters[i].getName(),sequenceParameters[i]);
		}
		set("parameters",parameters);
		//parameters.set(arg0, arg1);
		//parameters=sequenceParameters;
	}
	
	public void setProfiles(SequenceProfile[] sequenceProfiles){
		TableDataset profilesTable=createProfilesTable();
		for (int i=0;i<sequenceProfiles.length;i++){
			profilesTable.concatenate(sequenceProfiles[i]);
		}
		set("profiles",profilesTable);

	}
	
	public void addParameter(Parameter sequenceParameter){
		CompositeDataset parameter = (CompositeDataset) get("parameters");
		parameter.set(sequenceParameter.getName(), sequenceParameter);
		/*Parameter[] newParameters;
		int oldSize=0;
		if (parameters!=null){
			oldSize=parameters.length;
			newParameters=new Parameter[oldSize+1];
			for (int i=0;i<oldSize;i++){
				newParameters[i]=parameters[i];
			}
			newParameters[oldSize]=sequenceParameter;
			setParameters(newParameters);
		} else {
			newParameters=new Parameter[1];
			newParameters[0]=sequenceParameter;
			setParameters(newParameters);
		}*/
	}
	
	public void addProfile(SequenceProfile sequenceProfile){
		TableDataset profilesTable = (TableDataset) get("profiles");
		profilesTable.concatenate(sequenceProfile);
		
	}
	
	public String getInstrumentName(){
		List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		HashMap<String,String> acronyms=new HashMap<String,String>();
		Iterator<String> it = insList.iterator();
		while(it.hasNext()){
			String ins=it.next();
			String acro=Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+ins);
			acronyms.put(acro, ins);
		}
		char[] acro=new char[2];
		char[] sName=getName().toCharArray();
		if (sName.length<1){
			System.out.println("DEBUG:"+getName());
		}
		acro[0]=sName[1];
		acro[1]=sName[2];
		String acronim=new String(acro);
		String result = acronyms.get(acronim);
		if (result!=null) return acronyms.get(acronim);
		else {
			System.out.println("Acronim for "+acronim+" not known");
			return "UNKNOWN";
		}



	}
	
	protected Element getXMLElement(Document doc){
		Element eleSequence=null;
		  try {
				eleSequence = doc.createElement("sequence");
				eleSequence.setAttribute("name", getName());
				Element eleUID=doc.createElement("uniqueID");
				eleUID.setTextContent(getUniqueID());
				Element eleFlag=doc.createElement("insertOrDeleteFlag");
				eleFlag.setTextContent(getFlag());
				Element eleSource=doc.createElement("source");
				eleSource.setTextContent(""+getSource());
				Element eleDestination=doc.createElement("destination");
				eleDestination.setTextContent(""+getDestination());
				Element eleExecution=doc.createElement("executionTime");
				Element eleAction=doc.createElement("actionTime");
				eleAction.setTextContent(getExecutionTime());
				eleExecution.appendChild(eleAction);
				eleSequence.appendChild(eleUID);
				eleSequence.appendChild(eleFlag);
				eleSequence.appendChild(eleSource);
				eleSequence.appendChild(eleDestination);
				eleSequence.appendChild(eleExecution);
				Parameter[] parameters = getParameters();
				if (parameters!=null){
					Element eleParameterList=doc.createElement("parameterList");
					eleParameterList.setAttribute("count", ""+parameters.length);
					for (int i=0;i<parameters.length;i++){
						eleParameterList.appendChild(parameters[i].getXMLElement(i+1, doc));
					}
					eleSequence.appendChild(eleParameterList);
					
				}
				SequenceProfile[] profiles=getProfiles();
				if (profiles!=null){
					Element eleProfileList=doc.createElement("profileList");
					eleProfileList.setAttribute("count", ""+profiles.length);
					for (int i=0;i<profiles.length;i++){
						eleProfileList.appendChild(profiles[i].getXMLElement(doc));
					}
					eleSequence.appendChild(eleProfileList);
				}
				
				
		  }catch (Exception e){
			  e.printStackTrace();
		  }
		  return eleSequence;
	}

	
	public String toXml(int indent){
		String indentString="";
		for (int i=0;i<indent;i++){
			indentString=indentString+"\t";
		}
		String l1=indentString+"<sequence name=\""+getName()+"\">\n";
		String l2=indentString+"\t<uniqueID>"+getUniqueID()+"</uniqueID>\n";
		String l3=indentString+"\t<insertOrDeleteFlag>"+getFlag()+"</insertOrDeleteFlag>\n";
		String l4=indentString+"\t<source>"+getSource()+"</source>\n";
		String l5=indentString+"\t<destination>"+getDestination()+"</destination>\n";
		String l6=indentString+"\t<executionTime>\n";
		String l7=indentString+"\t\t<actionTime>"+getExecutionTime()+"</actionTime>\n";
		String l8=indentString+"\t</executionTime>\n";
		String l9="";
		Parameter[] parameters = getParameters();
		if (parameters!=null) l9=indentString+"\t<parameterList count=\""+parameters.length+"\">\n";
		String l10="";
		if (parameters!=null){
			for (int i=0;i<parameters.length;i++){
				if (parameters[i]==null) System.out.println("parameter is nulll:"+new Integer(i).toString()+getUniqueID());
				l10=l10+parameters[i].toXML(i+1, indent+2)+"\n";
			}
		}
		String l11=indentString+"\t</parameterList>\n";
		String l12="";
		SequenceProfile[] profiles=getProfiles();
		if (profiles.length>0) l12=indentString+"\t<profileList count=\""+profiles.length+"\">\n";
		String l13="";
		if (profiles.length>0){
			for (int i=0;i<profiles.length;i++){
				l13=l13+profiles[i].toXml(indent+2)+"\n";
			}
		}
		String l14=indentString+"\t </profileList>\n";
		String l15=indentString+"</sequence>\n";
		return l1+l2+l3+l4+l5+l6+l7+l8+l9+l10+l11+l12+l13+l14+l15;
	}
	
	public String toXml(){
		return toXml(0);
	}
	
	public void setMeta(MetaData metadata){
		super.setMeta(metadata);
		System.out.println("The metadata is being altered");
	}

	
}
