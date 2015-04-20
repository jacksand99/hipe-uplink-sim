package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;


import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;



/**
 * Class to store the history of a simulation of a planning period
 * It stores a list of actions that occurred, this actions are actually defined by
 * the time when the action occurred,
 * the mode that the spacecraft goes by this action,
 * the command that triggered this action
 * the original execution time of the command that triggered this action
 * and a ModelState object with all the spacecraft modes when this action occurred.
 * @author jarenas
 *
 */
public class HistoryModes extends CompositeDataset{
	private boolean cacheDirty;
	HashSet<ModePeriod> periods;
	/**
	 * Constructor that creates a History empty
	 */
	public HistoryModes(){
		super();
		cacheDirty=true;
		periods=new HashSet<ModePeriod>();
		Column time=new Column(new Long1d());
		Column newmode=new Column(new String1d());
		Column command=new Column(new String1d());
		Column orexecution=new Column(new Long1d());

		TableDataset table=new TableDataset();
		table.addColumn(time);		
		table.addColumn(newmode);
		table.addColumn(command);
		table.addColumn(orexecution);

		table.setColumnName(0, "Action Time");
		table.setColumnName(1, "Mode");
		table.setColumnName(2, "Sequence");
		table.setColumnName(3, "Original Execution Time");
		this.set("table", table);


	}
	
	/**
	 * Add a history record
	 * @param time time when the action occurred (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @param mode mode that the spacecraft goes by this action
	 * @param command command that triggered this action
	 * @param originalTime original execution time of the command that triggered this action
	 */
	public void add(long time,String mode,String command,long originalTime){
		if (getCommand(time)!=null){
			add(time+1,mode,command,originalTime);
		}
		else{
			Object[] row=new Object[4];
			row[0]=new Long(time);
			row[1]=mode;
			row[2]=command;
			row[3]=new Long(originalTime);
			((TableDataset) get("table")).addRow(row);
		}
		cacheDirty=true;
	}
	private Long[] longToLongArray(long[] data){
		Long[] result=new Long[data.length];
		for (int i=0;i<data.length;i++){
			result[i]=data[i];
			
		}
		return result;
	}
	private int findIndex(long time){
		TableDataset table = (TableDataset)get("table");
		long[] times = ((Long1d) table.getColumn(0).getData()).toArray();
		List<Long> list = Arrays.asList(longToLongArray(times));
		return list.indexOf(time);
	}
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return the name of the mode where the spacecraft is going by this action
	 */
	public String get(long time){
		TableDataset table = (TableDataset)get("table");
		int index = findIndex(time);
		if (index<0) return null;
		return ((String1d) table.getColumn(1).getData()).get(index);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return command that triggered this action
	 */
	public String getCommand(long time){
		TableDataset table = (TableDataset)get("table");
		int index = findIndex(time);
		if (index<0) return null;

		return ((String1d) table.getColumn(2).getData()).get(index);
	}
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return The original execution time of the command that triggered this action
	 */
	public long getOriginalTime(long time){
		TableDataset table = (TableDataset)get("table");
		int index = findIndex(time);
		return ((Long1d) table.getColumn(3).getData()).get(index);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @param states The ModelState object with all the states of the spacecraft at this time
	 */
	public void addStates(long time,ModelState states){
		set(""+time,states);
		cacheDirty=true;
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return The ModelState object with all the states of the spacecraft at this time, or null if the action has not been executed yet over the modelstate.
	 */
	public ModelState getStates(long time){
		try{
			return (ModelState) get(""+time);
		}catch (java.lang.NullPointerException ne){
			return null;
		}
	}
	
	/**
	 * Put a group of actions into the history, all originated by the same sequence executed at the same time
	 * @param newset Set of times and new modes
	 * @param command Sequence that originates the transitions
	 * @param originalTime execution time of the sequence
	 */
	public void putAll(java.util.HashMap<Long,String> newset,String command,long originalTime){

		Iterator<Long> it=newset.keySet().iterator();
		while (it.hasNext()){
			Long key=it.next();
			add(key.longValue(),newset.get(key),command,originalTime);
		}
		cacheDirty=true;
		
	}
	
	/**
	 * Get all times (as long) of actions contained in this history
	 * @return
	 */
	public long[] getTimes(){
		long[] result = ((Long1d) ((TableDataset) get("table")).getColumn(0).getData()).toArray();
		Arrays.sort(result);
		return result;
	}

	/**
	 * Get the least time greater than or equal to the given time (as long)
	 * @param time
	 * @return
	 */
	public long getCellingTime(long time){
		long[] allTimes=getTimes();
		TreeSet<Long> tree=new TreeSet<Long>();
		for (int i=0;i<allTimes.length;i++){
			tree.add(allTimes[i]);
		}
		return tree.ceiling(time);
	}
	/**
	 * Get the greatest time less than or equal to the given time
	 * @param time
	 * @return
	 */
	public long getFloorTime(long time){
		long[] allTimes=getTimes();
		TreeSet<Long> tree=new TreeSet<Long>();
		for (int i=0;i<allTimes.length;i++){
			tree.add(allTimes[i]);
		}
		return tree.floor(time);
	}
	/**
	 * Get all the states where the spacecraft was at the given time (as long)
	 * @param time
	 * @return
	 */
	public ModelState getStatesAt(long time){
		long floorTime = getFloorTime(time);
		return getStates(floorTime);
	}
	
	/**
	 * Get the earlier and latest date contained in this history
	 * @return
	 */
	public java.util.Date[] getBoundarieDates(){
		long[] temp=getTimes();
		java.util.Date[] result= new java.util.Date[2];
		result[0]=new java.util.Date(temp[0]);
		result[1]=new java.util.Date(temp[temp.length-1]);
		return result;
	}
	
	/**
	 * For a given mode, get all start date and end date of this mode. So if this mode start at time
	 * 1, then end at 3, then occurs again at 5 and end at 7, you wil get an array [1,3,5,7]
	 * @param modeName
	 * @return
	 */
	public java.util.Date[] getBoundarieDatesForState(String modeName){
		java.util.Vector<java.util.Date> result_v=new java.util.Vector<java.util.Date>();
		long[] temp=getTimes();
		String[] arr=modeName.split("_");
		String subSystem="";
		for (int i=0;i<arr.length-1;i++){
			subSystem=subSystem+arr[i]+"_";
		}
		
		boolean in=false;
		for (int i=0;i<temp.length;i++){
			ModelState st=getStates(temp[i]);
			if (st==null){
				//System.out.println(temp[i]+ " Not found");
			}else{
				
				String sfm=st.getStateForMode(modeName);
			
				if (sfm.equals(modeName) && !in){
					result_v.add(new java.util.Date(temp[i]));
					in=true;
				}
				
				if (!sfm.equals(modeName) && in){
					result_v.add(new java.util.Date(temp[i]));
					in=false;
				}
			}
		}
		if (in){
			result_v.add(new java.util.Date(temp[temp.length-1]));
			
		}
		java.util.Date[] result=new java.util.Date[result_v.size()];
		result_v.toArray(result);
		return result;
	}
	
	
	
	/**
	 * Get the name of all states contained in this history
	 * @return
	 */
	public String[] getAllStates(){
		HashMap<String,String> hm=new HashMap<String,String>();
		long[] temp=getTimes();

		for (int i=0;i<temp.length;i++){
			ModelState ms=getStates(temp[i]);
			if (ms!=null){
				String[] as=ms.getAllStates();
				
				for (int j=0;j<as.length;j++){
					if (!as[j].startsWith("null")){
						hm.put(as[j], "");

					}
				}
			}
		}
		String[] result= new String[hm.size()];
		hm.keySet().toArray(result);
		return result;
	}
	
	private HashSet<ModePeriod> getPeriods(){
		if (!cacheDirty) return periods;
		else{
			HashSet<ModePeriod> result=new HashSet<ModePeriod>();
			String[] states = getAllStates();
			for (int i=0;i<states.length;i++){
				Date[] dates = this.getBoundarieDatesForState(states[i]);
				for (int j=0;j<dates.length;j=j+2){
					Date start = dates[j];
					Date end = dates[j+1];
					ModePeriod period = new ModePeriod(start,end,states[i]);
					result.add(period);
				}
			}
			cacheDirty=false;
			periods=result;
		}
		return periods;
	}
	
	/**
	 * Get all the modes between two dates
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String[] getModesBetween(Date startDate,Date endDate){
		ModePeriod[] periods = findOverlapingModes(startDate,endDate);
		HashSet<String> modes=new HashSet<String>();
		for (int i=0;i<periods.length;i++){
			modes.add(periods[i].getName());
		}
		String[] result=new String[modes.size()];
		result=modes.toArray(result);
		return result;
	}
	
	private ModePeriod[] findOverlapingModes(Date startDate,Date endDate){
		java.util.Vector<ModePeriod> affected=new java.util.Vector<ModePeriod>();
		Iterator<ModePeriod> it = getPeriods().iterator();
		while (it.hasNext()){

			ModePeriod pass = it.next();
				boolean con1=false;
				boolean con2=false;
				if (pass.getStartTime().after(endDate) ) con1=true;
				if (pass.getEndTime().before(startDate)) con2=true;
				if (!con1 && !con2) affected.add(pass);
		}
		ModePeriod[] result= new ModePeriod[affected.size()];
		result=affected.toArray(result);
		return result;
	}
	
	private class ModePeriod implements Comparable<ModePeriod>{
		Date startTime;
		Date endTime;
		String mode;
		@Override
		public int compareTo(ModePeriod o) {
			return startTime.compareTo(o.startTime);
		}
		ModePeriod(Date sTime,Date eTime,String instrumentMode){
			startTime=sTime;
			endTime=eTime;
			mode=instrumentMode;
		}
		Date getStartTime(){
			return startTime;
		}
		Date getEndTime(){
			return endTime;
		}
		String getName(){
			return mode;
		}
		public boolean equals(ModePeriod other){
			if (!startTime.equals(other.getStartTime())) return false;
			if (!endTime.equals(other.getEndTime())) return false;
			if (!getName().equals(other.getName())) return false;
			return true;
		}
		
		
	}

}
