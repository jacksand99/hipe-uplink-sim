package vega.uplink.commanding.itl;
import herschel.ia.dataset.Product;
import herschel.ia.numeric.String1d;
import herschel.ia.pal.MapContext;
import herschel.ia.pal.ProductRef;
import herschel.share.interpreter.InterpreterUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Vector;
import java.util.logging.Logger;

import org.jfree.util.Log;

import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.track.Fecs;

//public class EventList extends MapContext implements SortedMap<Date,String>{
public class EventList extends MapContext {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date validityStart;
	private Date validityEnd;
	public String version;
	private static Date DEFAULT_START_DATE=new Date(65277473000L);
	private static Date DEAFULT_END_DATE=new Date(2526802497000L);
	//private static final Logger LOG = Logger.getLogger(Fecs.class.getName());
	private TreeMap<Date,String> map;
	
	//private java.util.Vector<EventList> includes;
	private String name;
	private String path;
	private String1d initValues;
	//private SortedMap<String, Date> cacheReverseMap;
	
	public EventList(){
		//super();
		this(DEFAULT_START_DATE,DEAFULT_END_DATE);
		
	}
	
	private java.util.Vector<EventList> getIncludeVector(){
		Vector<EventList> result = new java.util.Vector<EventList>();
		
		Iterator<ProductRef> it = this.getRefs().values().iterator();
		while(it.hasNext()){
			try {
				Product p = it.next().getProduct();
				if (InterpreterUtil.isInstance(EventList.class, p)){
					result.add((EventList) p);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
				iae.initCause(e);
				throw(iae);
			} 
			
		}
		return result;
		
	}
	public EventList(Date validityStart,Date validityEnd){
		super();
		this.validityStart=validityStart;
		this.validityEnd=validityEnd;
		//includes=new java.util.Vector<EventList>();
		map=new TreeMap<Date,String>();
		setName(""+new Date().getTime());
		setPath(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY));
		initValues=new String1d();
		version="1";
		//cacheReverseMap=new SortedMap<String, Date>();
	}
	
	public void regenerate(String text) throws ParseException{
		StringReader sr= new StringReader(text); // wrap your String
		BufferedReader br= new BufferedReader(sr); // wrap your StringReader
		EventList tempList = EventList.parseEvf(br,this.getPath());
		setValidityStart(tempList.getValidityStart());
		setValidityEnd(tempList.getValidityEnd());
		setIncludes(tempList.getIncludes());
		setMap(tempList.getMap());
		setInitValues(tempList.getInitValues());
		setVersion(tempList.getVersion());
	}
	private java.util.Vector<EventList> getIncludes(){
		return this.getIncludeVector();
	}
	private void setIncludes(java.util.Vector<EventList> includes){
		
		Iterator<String> it = this.getRefs().keySet().iterator();
		while (it.hasNext()){
			String key = it.next();
			this.getRefs().remove(key);
		}
		Iterator<EventList> it2 = includes.iterator();
		while(it2.hasNext()){
			this.addInclude(it2.next());
		}
		//this.includes=includes;
	}
	private TreeMap<Date,String> getMap(){
		return map;
	}
	private void setMap(TreeMap<Date,String> map){
		this.map=map;
	}
	private String1d getInitValues(){
		return initValues;
	}
	public void writeToFile(String file) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(file, "UTF-8");
		writer.print(toEvf());
		writer.close();
}

public void save() throws FileNotFoundException, UnsupportedEncodingException{
	writeToFile(getPath()+"//"+getName());
}
	
	private void setInitValues(String1d initValues){
		this.initValues=initValues;
	}
	public void addInitValue(String value){
		initValues.append(value);
	}
	
	public void setName(String name){
		this.name=name;
		
	}
	
	public void setPath(String path){
		this.path=path;
	}
	
	public String getName(){
		return name;
	}
	
	public String getPath(){
		return path;
	}
	
	public Date getValidityStart(){
		return validityStart;
	}
	
	public Date getValidityEnd(){
		return validityEnd;
	}
	public void setValidityStart(Date start){
		validityStart=start;
		
	}
	
	public boolean hasValidityStart(){
		if (validityStart.equals(DEFAULT_START_DATE)) return false;
		else return true;
	}
	public boolean hasValidityEnd(){
		if (validityEnd.equals(DEAFULT_END_DATE)) return false;
		else return true;
	}
	
	public void setValidityEnd(Date end){
		validityEnd=end;
	}
	public String put(Date date,String event){
		String prev = map.get(date);
		if (prev==null) return map.put(date, event);
		if (prev.equals(event)) return event;
		return put(new Date(date.getTime()+1),event);
	}
	public String put(Date date,ItlEvent event){
		return put(date,event.getName());
	}
	
	public void addInclude(EventList include){
		this.setProduct(include.getName(), include);
		//includes.add(include);
	}
	//@Override
	public int size() {
		return map.size();
	}
	//@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}
	//@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}
	//@Override
	public boolean containsValue(Object value) {
		Vector<EventList> includes = this.getIncludeVector();
		// TODO Auto-generated method stub
		boolean result = map.containsKey(value);
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			if (it.next().containsKey(value)) result=true;
		}
		return result;
	}
	//@Override
	public String get(Object key) {
		Vector<EventList> includes = this.getIncludeVector();
		String result = map.get(key);
		//System.out.println(DateUtil.dateToLiteral((Date)key));
		//System.out.println(map.containsKey(key));
		if (result==null){
			Iterator<EventList> it = includes.iterator();
			while(it.hasNext()){
				EventList in = it.next();
				if (in.containsKey(key)) result=in.get(key);
				
			}
			
		}
		return result;
	}
	//@Override
	public ItlEvent getEvent(Date key) {
		String result = get(key);
		if (result==null) return null;
		return new ItlEvent(result);
	}
	//@Override
	public String remove(Object key) {
		Vector<EventList> includes = this.getIncludeVector();
		String result = map.remove(key);
		if (result==null){
			Iterator<EventList> it = includes.iterator();
			while(it.hasNext()){
				EventList in = it.next();
				if (in.containsKey(key)) result=in.remove(key);
				
			}
			
		}
		return result;

	}
	//@Override
	public void putAll(EventList m) {
		Iterator<? extends Date> it = m.dateSet().iterator();
		while(it.hasNext()){
			Date date = it.next();
			put(date,m.get(date));
			//put()
		}
		//map.putAll(m);
		
	}
	public void putAll(SortedMap<Date,String> m) {
		Iterator<? extends Date> it = m.keySet().iterator();
		while(it.hasNext()){
			Date date = it.next();
			put(date,m.get(date));
			//put()
		}
		//map.putAll(m);
		
	}

	//@Override
	public void clear() {
		map.clear();
		
	}
	//@Override
	public Comparator<? super Date> comparator() {
		return map.comparator();
	}
	//@Override
	//public SortedMap<Date, String> subMap(Date fromKey, Date toKey) {
	public EventList subMap(Date fromKey, Date toKey) {
		Vector<EventList> includes = this.getIncludeVector();
		toKey=new Date(toKey.getTime()+998);
		EventList result = new EventList(this.getValidityStart(),this.getValidityEnd());
		result.putAll(map.subMap(fromKey, toKey));
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			EventList in = it.next();

			result.putAll(in.subMap(fromKey, toKey));
			
			
		}

		
		return result;
	}
	//@Override
	public EventList headMap(Date toKey) {
		Vector<EventList> includes = this.getIncludeVector();
		toKey=new Date(toKey.getTime()+998);
		EventList result = new EventList(this.getValidityStart(),this.getValidityEnd());
		result.putAll(map.headMap( toKey));
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			EventList in = it.next();
			result.putAll(in.headMap(toKey));
			
		}
		
		return result;
	}
	//@Override
	public EventList tailMap(Date fromKey) {
		Vector<EventList> includes = this.getIncludeVector();
		EventList result = new EventList(this.getValidityStart(),this.getValidityEnd());
		result.putAll(map.tailMap( fromKey));
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			EventList in = it.next();
			result.putAll(in.tailMap(fromKey));
			
		}
		
		return result;
	}
	//@Override
	public Date firstKey() {
		Vector<EventList> includes = this.getIncludeVector();
		Date result = map.firstKey();
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			EventList in = it.next();
			Date fk = in.firstKey();
			if (fk.before(result)) result=fk;
			
		}
		return result;
	}
	//@Override
	public Date lastKey() {
		Vector<EventList> includes = this.getIncludeVector();
		Date result = map.lastKey();
		Iterator<EventList> it = includes.iterator();
		while(it.hasNext()){
			EventList in = it.next();
			Date fk = in.lastKey();
			if (fk.after(result)) result=fk;
			
		}
		return result;
	}
	//@Override
	public Set<Date> dateSet() {
		return map.keySet();
	}
	//@Override
	public Collection<String> values() {
		return map.values();
	}
	//@Override
	public Set<java.util.Map.Entry<Date, String>> entrySet() {
		return map.entrySet();
	}
	private SortedMap<String, Date> getInverseMap(){
		Vector<EventList> includes = this.getIncludeVector();
		TreeMap<String, Date> result = new TreeMap<String,Date>();
		Iterator<java.util.Map.Entry<Date, String>> it = this.entrySet().iterator();
		while (it.hasNext()){
			java.util.Map.Entry<Date, String> entry = it.next();
			result.put(entry.getValue(), entry.getKey());
		}
		Iterator<EventList> it2 = includes.iterator();
		while (it2.hasNext()){
			EventList include = it2.next();
			
			result.putAll(include.getInverseMap());
		}
		return result;
	}
	private SortedMap<String, Date> getInverseMap(Date validityStart,Date validityEnd){
		
		TreeMap<String, Date> result = new TreeMap<String,Date>();
		EventList submap = this.subMap(validityStart, validityEnd);
		Iterator<java.util.Map.Entry<Date, String>> it = submap.entrySet().iterator();
		while (it.hasNext()){
			java.util.Map.Entry<Date, String> entry = it.next();
			result.put(entry.getValue(), entry.getKey());
		}
		/*Iterator<EventList> it2 = includes.iterator();
		while (it2.hasNext()){
			EventList include = it2.next();
			
			result.putAll(include.getInverseMap());
		}*/
		return result;
	}
	
	public Date getDate(String event){
		return getInverseMap().get(event);
	}
	public Date getDate(String event,Date validityStart,Date validityEnd){
		return subMap(validityStart, validityEnd).getInverseMap().get(event);
		//return getInverseMap(validityStart,validityEnd).get(event);
	}
	
	public Date getDate(ItlEvent event){
		return getDate(event.getName());
	}
	public Date getDate(ItlEvent event,Date validityStart,Date validityEnd){
		Date result = getDate(event.getName(),validityStart,validityEnd);
		if (result!=null) return result;
		else return getDate(event);
	}
	
	public String toEvf(){
		Vector<EventList> includes = this.getIncludeVector();
		String result="";
		result=result+"Version: "+getVersion()+"\n";
		if (this.hasValidityStart()) result=result+"Start_time: "+DateUtil.dateToLiteral(getValidityStart())+"\n";
		if (this.hasValidityEnd()) result=result+"End_time: "+DateUtil.dateToLiteral(getValidityEnd())+"\n";
		result=result+"\n";
		Iterator<EventList> it = includes.iterator();
		while (it.hasNext()){
			EventList include = it.next();
			result=result+"Include: \""+include.getPath()+"/"+include.getName()+"\"\n";
			
		}
		result=result+"\n";
		for (int i=0;i<initValues.length();i++){
			result=result+"Init_value: "+initValues.get(i);
		}
		Iterator<Date> it2 = dateSet().iterator();
		while (it2.hasNext()){
			Date date = it2.next();
			result=result+DateUtil.dateToLiteral(date)+" "+getEvent(date)+"\n";
		}
		
		return result;
		
		

	}
	
	public void setVersion(String version){
		this.version=version;
	}
	public String getVersion(){
		return version;
	}
	public static EventList  parseEvf(BufferedReader br,String path) throws ParseException{

		//java.util.HashMap<String,String> events=new java.util.HashMap<String,String>();
		EventList result=new EventList();
		String[] evtlines;
		try {
			evtlines = readFile(br);

			for (int i=0;i<evtlines.length;i++){
				boolean processed=false;

				if (evtlines[i].startsWith("Version")){
					String sSD=evtlines[i].replace("Version:", "");
					result.setVersion(sSD);
					//Date startDate = DateUtil.literalToDate(sSD);
					//result.setValidityStart(startDate);
					processed=true;
				}
				if (evtlines[i].startsWith("Start_time")){
					String sSD=evtlines[i].replace("Start_time:", "");

					Date startDate = DateUtil.literalToDate(sSD);
					result.setValidityStart(startDate);
					processed=true;
				}
				if (evtlines[i].startsWith("End_time")){
					String sSD=evtlines[i].replace("End_time:", "");

					Date endDate = DateUtil.literalToDate(sSD);
					result.setValidityEnd(endDate);

					processed=true;
				}
				if (evtlines[i].startsWith("Init_value:")){
					String sSD=evtlines[i].replace("Init_value:", "");

					//Date endDate = DateUtil.literalToDate(sSD);
					result.addInitValue(sSD);

					processed=true;
				}

				if (evtlines[i].startsWith("Include")){
					String sSD=evtlines[i].replace("Include: ", "");
					sSD=sSD.replace("Include:", "");
					sSD=sSD.replace("Include_file:", "");
					sSD=sSD.replace("\"", "");
					if (sSD.startsWith("LOEV")){
						VegaLog.warning("LOEV import still not implemented. Skipping LOEV.");
					}else{
						try{
							//LOG.info("Trying to parse "+sSD+" with root path "+path);
							result.addInclude(parseEvf(sSD,path));
							//LOG.info("Parsing "+sSD+" with root path "+path+" succesful");
						}catch (Exception e){
							try{
								//LOG.info("Parsing  "+sSD+" with root path "+path+" was not succesful");
								//LOG.info("Trying to parse "+path+"/"+sSD+" with root path "+path);
								//e.printStackTrace();
								result.addInclude(parseEvf(path+"/"+sSD,path));
								//LOG.info("Parsing "+path+"/"+sSD+" with root path "+path+" succesful");
							}catch (Exception e2){
								//LOG.info("Parsing  "+path+"/"+sSD+" with root path "+path+" was not succesful");
								
								String dp = Properties.getProperty(Properties.DEFAULT_EVT_DIRECTORY);
								try{
									//e2.printStackTrace();
									//LOG.info("Trying to parse "+sSD+" with root path "+dp);
									//result.addInclude(parseEvf(dp+"/"+sSD,path));
									result.addInclude(parseEvf(sSD,dp));
	
									//LOG.info("Parsing "+sSD+" with root path "+dp+" succesful");
									
								}catch (Exception e3){
									IllegalArgumentException iae = new IllegalArgumentException(e.getMessage()+". "+e2.getMessage()+". "+e3.getMessage());
									iae.initCause(e3);
									throw(iae);
								}
							}
						}
					}
					processed=true;
				
				}
				if (evtlines[i].contains("COUNT")){
					String[] parts=evtlines[i].split(" ");
					if (parts.length==3){
						parts[0]=parts[0].replaceAll(" ", "");
						parts[1]=parts[1].replaceAll(" ", "");
						parts[2]=parts[2].replaceAll(" ", "");
						int count=Integer.parseInt(parts[2].split("=")[1].replace(")", ""));
						ItlEvent itlEvent = new ItlEvent(parts[1],count);
						Date date=DateUtil.parse(parts[0]);
						result.put(date, itlEvent);
						//if (parts[1].startsWith("AL_HIST_RIDER_10___________SO")) System.out.println(DateUtil.defaultDateToString(date));
						//System.out.println("Inserting "+itlEvent.toString());
					}

					processed=true;
				}
				if (!processed){
					String[] parts=evtlines[i].split(" ");
					try{
						ItlEvent itlEvent = new ItlEvent(parts[1],-1);
						Date date=DateUtil.parse(parts[0]);
						result.put(date, itlEvent);
						
					}catch (java.lang.ArrayIndexOutOfBoundsException ai){
						IllegalArgumentException iae = new IllegalArgumentException("Could not parse line "+evtlines[i]);
						iae.initCause(ai);
						throw(iae);
					}
					
				}



			}
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			e2.initCause(e1);
			throw e2;
		}
		return result;
	}

	
	public static String[] readFile(BufferedReader br) throws IOException, ParseException{
		//BufferedReader br = null;
		String line = "";
		String[] result;
		java.util.Vector<String> lines=new java.util.Vector<String>();
		
				//br = new BufferedReader(new FileReader(f));
			while ((line = br.readLine()) != null){
				line=line.replace("#POWER_ON#", "0");
				line=line.replace("#DATA_ON#", "0");

				lines.add(line);
			}
		
		result = new String[lines.size()];
		lines.toArray(result);
		br.close();
		try{
			result=ItlParser.removeComments(result);
			result=ItlParser.removeBreaks(result);
			result=ItlParser.removeSpaces(result);
			//result=resolveIncludes(result,path);
			result=ItlParser.removeDuplicates(result,"Version");
			result=ItlParser.removeDuplicates(result,"Start_time");
			result=ItlParser.removeDuplicates(result,"End_time");

		}catch (Exception pex){
			ParseException nEx = new ParseException(pex.getMessage(),0);
			nEx.initCause(pex);
			throw nEx;
		}
		return result;
	}
	public static EventList parseEvf(String file,String path) throws IOException, ParseException{
		file=file.trim();
		path=path.trim();
		File f = new File(path+"/"+file);
		if (f.exists()){
			return parseEvf(f,path);
		}else{
			VegaLog.info("Can reads file "+f.canRead());
			VegaLog.info("\""+f.getAbsolutePath()+"\""+" does not exist 1 in "+f.getParent());
		}
		f = new File(file);
		if (f.exists()){
			return parseEvf(f,path);
		}else{
			VegaLog.info(f.getAbsolutePath()+" does not exist 2");
		}
		FileNotFoundException fnf = new FileNotFoundException("Neither "+path+"/"+file+" or "+file+" were found");
		throw(fnf);
	}
	public static EventList parseEvf(String file) throws IOException, ParseException{
		return parseEvf(new File(file));
	}
	public static EventList parseEvf(File f) throws IOException, ParseException{
		return parseEvf(f,f.getParent());
	}
	public static EventList parseEvf(File f,String path) throws IOException, ParseException{
		BufferedReader br = null;

		
				br = new BufferedReader(new FileReader(f));
				try{
					EventList result = parseEvf(br,path);
					result.setName(f.getName());
					result.setPath(f.getParent());
					return result;
				}catch (java.text.ParseException pex){
					ParseException nEx = new ParseException("In file "+f+"."+pex.getMessage(),0);
					nEx.initCause(pex);
					throw nEx;
				}
	}
	
	public ItlEvent[] getAllEvents(String name,Date startDate,Date endDate){
		//System.out.println("Start get inverse map");
		SortedMap<String, Date> rMap = this.getInverseMap(startDate,endDate);
		//System.out.println("End get inverse map");
		//System.out.println(startDate);
		//System.out.println(endDate);
		java.util.Vector<ItlEvent> vector=new java.util.Vector<ItlEvent>();
		Iterator<String> it = rMap.keySet().iterator();
		while (it.hasNext()){
			String st = it.next();
			//System.out.println(st);
			if (st.startsWith(name)) vector.add(new ItlEvent(st));
		}
		ItlEvent[] result = new ItlEvent[vector.size()];
		result=vector.toArray(result);
		return result;
	}
	
	public Date[] getAllDates(String name,Date startDate,Date endDate){
		SortedMap<String, Date> rMap = this.getInverseMap(startDate,endDate);
		//System.out.println(startDate);
		//System.out.println(endDate);
		java.util.Vector<Date> vector=new java.util.Vector<Date>();
		Iterator<String> it = rMap.keySet().iterator();
		while (it.hasNext()){
			String st = it.next();
			//System.out.println(st);
			if (st.startsWith(name)) vector.add(rMap.get(st));
		}
		Date[] result = new Date[vector.size()];
		result=vector.toArray(result);
		return result;
	}

}
