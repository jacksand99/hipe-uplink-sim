package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.AbstractOrdered1dData;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.Long1d;
import herschel.ia.numeric.String1d;
import herschel.share.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Arrays;

import com.kenai.jffi.Array;

public class MocPower extends TableDataset{
	MocPower instance;
	//long[] dates;
	//float[] power;
	
	private MocPower(){
		super();
		Column time=new Column(new Long1d());
		Column power=new Column(new Float1d());
		//Column eddump=new Column(new String1d());
		//Column tmRate=new Column(new String1d());
		
		this.addColumn(time);		
		this.addColumn(power);
		//this.addColumn(eddump);
		//this.addColumn(tmRate);
		this.setColumnName(0, "Time");
		this.setColumnName(1, "Power");

		/*dates=new long[0];
		power=new float[0];*/
		
	}
	
	private void addRecord(long newDate,float newPower){
		/*AbstractOrdered1dData[] array=new AbstractOrdered1dData[2];
		array*/
		Object[] array=new Object[2];
		array[0]=newDate;
		array[1]=newPower;
		this.addRow(array);
		/*long[] nArrayDates=new long[dates.length+1];
		float[] nArrayPower=new float[power.length+1];
		for (int i=0;i<dates.length;i++){
			nArrayDates[i]=dates[i];
			nArrayPower[i]=power[i];
		}
		nArrayDates[dates.length]=newDate;
		nArrayPower[dates.length]=newPower;
		
		
		setRecords(nArrayDates,nArrayPower);*/
	}
	
	private void setRecords(long[] newDates, float[] newPowers){
		Long1d col1=new Long1d();
		Float1d col2=new Float1d();
		for (int i=0;i<newDates.length;i++){
			col1.append(newDates[i]);
			col2.append(newPowers[i]);
		}
		this.getColumn(0).setData(col1);
		this.getColumn(1).setData(col2);
		
		/*dates=newDates;
		power=newPowers;*/
	}
	
	public MocPower getInstance(){
		if (instance==null){
			instance=new MocPower();
		}
		return instance;
	}
	
	public float getPower(java.util.Date index){
		long[] dates = ((Long1d) this.getColumn(0).getData()).toArray();
		int ind=Arrays.binarySearch(dates, index.getTime());
		return ((Float1d) this.getColumn(1).getData()).get(ind);
		//return power[ind];
	}
	public static MocPower ReadFromBuffer(BufferedReader br){
		MocPower result = new MocPower();
		//BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "      ";
	 
		try {
	 
			//br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] fields = line.split(cvsSplitBy);
				long newTime=zuluToDate(fields[0]).getTime();
				float newPower=Float.parseFloat(fields[1]);
				//System.out.println(zuluToDate(fields[0]).toString()+","+fields[1]);
				result.addRecord(newTime, newPower);
	 
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
		//System.out.println(result.power.length);
		return result;

	}
	public static MocPower ReadFromFile (String file){
		try {
			 
			return ReadFromBuffer(new BufferedReader(new FileReader(file)));
			
		}catch (Exception e){
			e.printStackTrace();
			return new MocPower();
		}
	}
	public static MocPower ReadFromJar(){
		System.out.println("reading from jar");
		//MocPower result = new MocPower();
		//try{
			InputStream is = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/moc/PWPL_14_001_14_365__OPT_01.ROS");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			return ReadFromBuffer(br);
		//}catch (Exception e){
			//e.printStackTrace();
			//return result;
		//}
		/*BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "      ";
	 
		try {
	 
			br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
	 
			        // use comma as separator
				String[] fields = line.split(cvsSplitBy);
				long newTime=zuluToDate(fields[0]).getTime();
				float newPower=Float.parseFloat(fields[1]);
				//System.out.println(zuluToDate(fields[0]).toString()+","+fields[1]);
				result.addRecord(newTime, newPower);
	 
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
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
		//System.out.println(result.power.length);
		return result;*/
		
		
	}
	
	public float getPowerAt(java.util.Date aproxDate){
		//System.out.println(findCloserDate(aproxDate));
		return getPower(findCloserDate(aproxDate));
	}
	
	private java.util.Date findCloserDate(java.util.Date dateToFind){
		java.util.Date result=new java.util.Date();
		long[] dates = ((Long1d) this.getColumn(0).getData()).toArray();
		long[] orderedDates=dates;
		Arrays.sort(orderedDates);
		//System.out.println(dates.length);
		for (int i=0;i<orderedDates.length;i++){
			if (dateToFind.after(new java.util.Date(orderedDates[i]))) result=new java.util.Date(orderedDates[i]);
			//System.out.println(result);
		}
		
		return result;
	}
	
	static java.util.Date zuluToDate(String zuluTime) throws ParseException{
		java.text.SimpleDateFormat dateFormat=new java.text.SimpleDateFormat("yy-D'T'HH:mm:ss'Z'");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		return dateFormat.parse(zuluTime);
	}

}
