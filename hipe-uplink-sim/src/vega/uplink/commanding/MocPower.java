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

import vega.uplink.DateUtil;

import com.kenai.jffi.Array;

/**
 * Object that contains the data read form a PWPL file (the power limits defined by MOC)
 * @author jarenas
 *
 */
public class MocPower extends TableDataset{
	MocPower instance;
	
	private MocPower(){
		super();
		Column time=new Column(new Long1d());
		Column power=new Column(new Float1d());
		
		this.addColumn(time);		
		this.addColumn(power);
		this.setColumnName(0, "Time");
		this.setColumnName(1, "Power");
		
	}
	
	/**
	 * Add a record (time and max power to this object)
	 * @param newDate
	 * @param newPower
	 */
	private void addRecord(long newDate,float newPower){
		Object[] array=new Object[2];
		array[0]=newDate;
		array[1]=newPower;
		this.addRow(array);
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
	}
	
	/**
	 * Get an instance of this object
	 * @return
	 */
	public MocPower getInstance(){
		if (instance==null){
			instance=new MocPower();
		}
		return instance;
	}
	
	private float getPower(java.util.Date index){
		long[] dates = ((Long1d) this.getColumn(0).getData()).toArray();
		int ind=Arrays.binarySearch(dates, index.getTime());
		return ((Float1d) this.getColumn(1).getData()).get(ind);
	}
	protected static String removeMultipleSpaces(String line){
		line=line.replaceAll("\t", " ");
		int index=line.indexOf("  ");
		if (index==-1){
			return line;
		}else{
			return removeMultipleSpaces(line.replaceAll("  ", " "));
		}
	}
	/**
	 * Read this object from a buffered reader
	 * @param br
	 * @return
	 */
	public static MocPower readFromBuffer(BufferedReader br){
		MocPower result = new MocPower();
		String line = "";
		String cvsSplitBy = " ";
	 
		try {
			while ((line = br.readLine()) != null) {
				
				line=removeMultipleSpaces(line);
				if (!line.equals(" ") &&  !line.equals("")){
					String[] fields = line.split(cvsSplitBy);
					long newTime=DateUtil.parse(fields[0]).getTime();
					float newPower=Float.parseFloat(fields[1]);
					result.addRecord(newTime, newPower);
				}
	 
			}
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
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
		return result;

	}
	/**
	 * Read this object from a file
	 * @param file
	 * @return
	 */
	public static MocPower readFromFile (String file){
		try {
			 
			return readFromBuffer(new BufferedReader(new FileReader(file)));
			
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not read power file "+e.getMessage());
			iae.initCause(e);
			throw iae;

		}
	}
	/**
	 * Read the file from jar file. It will try to read the file from the jars in the class path from the path /moc/PWPL_14_001_14_365__OPT_01.ROS
	 * @return
	 */
	public static MocPower readFromJar(){
		System.out.println("reading from jar");

			InputStream is = ObjectUtil.getClass("vega.uplink.commanding.Por").getResourceAsStream("/moc/PWPL_14_001_14_365__OPT_01.ROS");
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			return readFromBuffer(br);

		
		
	}
	
	/**
	 * Get maximum power at a given date
	 * @param aproxDate
	 * @return
	 */
	public float getPowerAt(java.util.Date aproxDate){
		return getPower(findCloserDate(aproxDate));
	}
	
	private java.util.Date findCloserDate(java.util.Date dateToFind){
		java.util.Date result=new java.util.Date();
		long[] dates = ((Long1d) this.getColumn(0).getData()).toArray();
		long[] orderedDates=dates;
		Arrays.sort(orderedDates);
		for (int i=0;i<orderedDates.length;i++){
			if (dateToFind.after(new java.util.Date(orderedDates[i]))) result=new java.util.Date(orderedDates[i]);
		}
		return result;
	}
	


}
