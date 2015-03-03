package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.CompositeDataset;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Map.Entry;
//import java.util.Set;
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

	
	/**
	 * Constructor that creates a History empty
	 */
	public HistoryModes(){
		super();

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
		/*if (history.get(time)!=null){
			add(time+1,mode,command,originalTime);
		}*/ else{
			
		
			//history.put(time, mode);
			//historyCommands.put(time, command);
			//historyExecution.put(time, originalTime);
			Object[] row=new Object[4];
			//row[0]=dateFormat2.format(new Date(time));
			row[0]=new Long(time);
			row[1]=mode;
			row[2]=command;
			row[3]=new Long(originalTime);
			((TableDataset) get("table")).addRow(row);
		}
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
		//System.out.println("tablesize is "+table.getRowCount());
		long[] times = ((Long1d) table.getColumn(0).getData()).toArray();
		//System.out.println("times size is "+times.length);
		
		List<Long> list = Arrays.asList(longToLongArray(times));
		/*System.out.println("The list size is "+list.size());
		System.out.println("The list is "+list);
		System.out.println("The 1st vale is "+list.get(0));*/

		return list.indexOf(time);
		//Arrays.sort(times);
		//return Arrays.binarySearch(times, time);

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
		//Long1d times = (Long1d) table.getColumn(0).getData();
		
		//return history.get(time);
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

		//return historyCommands.get(time);
	}
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return The original execution time of the command that triggered this action
	 */
	public long getOriginalTime(long time){
		TableDataset table = (TableDataset)get("table");
		int index = findIndex(time);
		//if (index<0) return null;

		return ((Long1d) table.getColumn(3).getData()).get(index);

		//return historyExecution.get(time);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @param states The ModelState object with all the states of the spacecraft at this time
	 */
	public void addStates(long time,ModelState states){
		set(""+time,states);
		//historyStates.put(time, states);
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
		//return historyStates.get(time);
	}
	
	public void putAll(java.util.HashMap<Long,String> newset,String command,long originalTime){

		Iterator<Long> it=newset.keySet().iterator();
		while (it.hasNext()){
			Long key=it.next();
			add(key.longValue(),newset.get(key),command,originalTime);
		}
		
	}
	
	public long[] getTimes(){
		long[] result = ((Long1d) ((TableDataset) get("table")).getColumn(0).getData()).toArray();
		Arrays.sort(result);
		return result;
		/*long[] result=new long[history.size()];
		Iterator<Long> it=history.keySet().iterator();
		int locator=0;
		while (it.hasNext()){
			result[locator]=it.next().longValue();
			locator++;
		}
		Arrays.sort(result);
		return result;*/
		
	}
	public long getCellingTime(long time){
		long[] allTimes=getTimes();
		TreeSet<Long> tree=new TreeSet<Long>();
		for (int i=0;i<allTimes.length;i++){
			tree.add(allTimes[i]);
		}
		return tree.ceiling(time);
	}
	public long getFloorTime(long time){
		long[] allTimes=getTimes();
		TreeSet<Long> tree=new TreeSet<Long>();
		for (int i=0;i<allTimes.length;i++){
			tree.add(allTimes[i]);
		}
		return tree.floor(time);
	}
	public ModelState getStatesAt(long time){
		long floorTime = getFloorTime(time);
		return getStates(floorTime);
	}
	
	public java.util.Date[] getBoundarieDates(){
		long[] temp=getTimes();
		java.util.Date[] result= new java.util.Date[2];
		result[0]=new java.util.Date(temp[0]);
		result[1]=new java.util.Date(temp[temp.length-1]);
		return result;
	}
	
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
	
	public String[] getAllStates(){
		HashMap<String,String> hm=new HashMap<String,String>();
		long[] temp=getTimes();
		//System.out.println("*******");
		//System.out.println(temp.length);
		//System.out.println("*******");

		for (int i=0;i<temp.length;i++){
			//System.out.println(i);
			ModelState ms=getStates(temp[i]);
			if (ms!=null){
				String[] as=ms.getAllStates();
				/*System.out.println("*******");
				System.out.println(as.length);
				System.out.println("*******");*/
				
				for (int j=0;j<as.length;j++){
					if (!as[j].startsWith("null")){
						hm.put(as[j], "");
						//System.out.println("**** put *****"+as[j]);

					}
				}
			}
		}
		String[] result= new String[hm.size()];
		hm.keySet().toArray(result);
		return result;
	}

}
