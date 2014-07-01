package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.String1d;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
//import java.util.Map.Entry;
//import java.util.Set;

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
public class HistoryModes extends TableDataset{
	java.util.HashMap<Long,String> history;
	java.util.HashMap<Long,String> historyCommands;
	java.util.HashMap<Long,Long> historyExecution;
	java.util.HashMap<Long,ModelState> historyStates;
	java.text.SimpleDateFormat dateFormat2;

	
	/**
	 * Constructor that creates a History empty
	 */
	public HistoryModes(){
		super();
		dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		history=new java.util.HashMap<Long,String>();
		historyCommands=new java.util.HashMap<Long,String>();
		historyExecution=new java.util.HashMap<Long,Long>();
		historyStates=new java.util.HashMap<Long,ModelState>();
		Column time=new Column(new String1d());
		Column newmode=new Column(new String1d());
		Column command=new Column(new String1d());
		Column orexecution=new Column(new String1d());
		//Column eddump=new Column(new String1d());
		//Column tmRate=new Column(new String1d());
		
		this.addColumn(time);		
		this.addColumn(newmode);
		this.addColumn(command);
		this.addColumn(orexecution);
		//this.addColumn(eddump);
		//this.addColumn(tmRate);
		this.setColumnName(0, "Action Time");
		this.setColumnName(1, "Mode");
		this.setColumnName(2, "Sequence");
		this.setColumnName(3, "Original Execution Time");
		//this.setColumnName(4, "End Dump");
		//this.setColumnName(5, "TM rate");

	}
	
	/**
	 * Add a history record
	 * @param time time when the action occurred (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @param mode mode that the spacecraft goes by this action
	 * @param command command that triggered this action
	 * @param originalTime original execution time of the command that triggered this action
	 */
	public void add(long time,String mode,String command,long originalTime){
		if (history.get(time)!=null){
			add(time+1,mode,command,originalTime);
		} else{
			
		
			history.put(time, mode);
			historyCommands.put(time, command);
			historyExecution.put(time, originalTime);
			String[] row=new String[4];
			row[0]=dateFormat2.format(new Date(time));
			row[1]=mode;
			row[2]=command;
			row[3]=dateFormat2.format(new Date(originalTime));
			addRow(row);
		}
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return the name of the mode where the spacecraft is going by this action
	 */
	public String get(long time){
		return history.get(time);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return command that triggered this action
	 */
	public String getCommand(long time){
		return historyCommands.get(time);
	}
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return The original execution time of the command that triggered this action
	 */
	public long getOriginalTime(long time){
		return historyExecution.get(time);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @param states The ModelState object with all the states of the spacecraft at this time
	 */
	public void addStates(long time,ModelState states){
		historyStates.put(time, states);
	}
	
	/**
	 * @param time Time of the action (number of milliseconds since January 1, 1970, 00:00:00 GMT)
	 * @return The ModelState object with all the states of the spacecraft at this time, or null if the action has not been executed yet over the modelstate.
	 */
	public ModelState getStates(long time){
		return historyStates.get(time);
	}
	
	public void putAll(java.util.HashMap<Long,String> newset,String command,long originalTime){

		Iterator<Long> it=newset.keySet().iterator();
		while (it.hasNext()){
			Long key=it.next();
			add(key.longValue(),newset.get(key),command,originalTime);
		}
		
	}
	
	public long[] getTimes(){
		long[] result=new long[history.size()];
		Iterator<Long> it=history.keySet().iterator();
		int locator=0;
		while (it.hasNext()){
			result[locator]=it.next().longValue();
			locator++;
		}
		Arrays.sort(result);
		return result;
		
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
