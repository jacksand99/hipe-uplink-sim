package rosetta.uplink.pointing;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.math.Quaternion;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.fltdyn.time.SimpleTimeFormat;
import herschel.share.fltdyn.time.TimeScale;
import herschel.share.util.ObjectUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;
import java.util.TreeMap;

import vega.uplink.DateUtil;
import vega.uplink.commanding.Orcd;
import vega.uplink.commanding.Por;
import vega.uplink.pointing.PointingBlock;

public class RosettaDistance extends TableDataset{
	private TreeMap<Date,Float> distanceCG;
	private TreeMap<Date,Float> distanceSun;
	private TreeMap<Date,Float> distanceEarth;
	private static RosettaDistance instance;
	public RosettaDistance(){
		distanceCG=new TreeMap<Date,Float>();
		distanceSun=new TreeMap<Date,Float>();
		distanceEarth=new TreeMap<Date,Float>();
		Column dates=new Column(new String1d());
		Column valSun=new Column(new String1d());
		Column valEarth=new Column(new String1d());
		Column valComet=new Column(new String1d());
		
		this.addColumn(dates);		
		this.addColumn(valSun);
		this.addColumn(valEarth);
		this.addColumn(valComet);

		this.setColumnName(0, "Date");
		this.setColumnName(1, "Distance to Sun");
		this.setColumnName(2, "Distance to Earth");
		this.setColumnName(3, "Distance to Comet");
	}
	
	private void populateTable(){
		Iterator<Date> it = distanceSun.keySet().iterator();
		while(it.hasNext()){
			Date key = it.next();
			String[] row=new String[4];
			row[0]=DateUtil.dateToZulu(key);
			row[1]=""+distanceSun.get(key);
			row[2]=""+distanceEarth.get(key);
			row[3]=""+distanceCG.get(key);
			this.addRow(row);
		}
	}
	private void addDistanceCG(Date date,Float distance){
		distanceCG.put(date, distance);
	}

	private void addDistanceSun(Date date,Float distance){
		distanceSun.put(date, distance);
	}
	
	private void addDistanceEarth(Date date,Float distance){
		distanceEarth.put(date, distance);
	}
	
	public Float getDistanceCG(Date date){
		if (distanceCG.containsKey(date)) return distanceCG.get(date);
		if (date.after(distanceCG.lastKey())) return distanceCG.get(distanceCG.lastKey());
		Date date1 = distanceCG.floorKey(date);
		Date date2 = distanceCG.ceilingKey(date);
		Float value1 = distanceCG.get(date1);
		Float value2 = distanceCG.get(date2);
		return interpolate(date1,value1,date2,value2,date);
	}
	
	

	public Float getDistanceSun(Date date){
		if (distanceSun.containsKey(date)) return distanceSun.get(date);
		if (date.after(distanceSun.lastKey())) return distanceSun.get(distanceSun.lastKey());
		Date date1 = distanceSun.floorKey(date);
		Date date2 = distanceSun.ceilingKey(date);
		Float value1 = distanceSun.get(date1);
		Float value2 = distanceSun.get(date2);
		return interpolate(date1,value1,date2,value2,date);
	}
	public Float getDistanceEarth(Date date){
		if (distanceEarth.containsKey(date)) return distanceEarth.get(date);
		if (date.after(distanceEarth.lastKey())) return distanceEarth.get(distanceEarth.lastKey());
		Date date1 = distanceEarth.floorKey(date);
		Date date2 = distanceEarth.ceilingKey(date);
		Float value1 = distanceEarth.get(date1);
		Float value2 = distanceEarth.get(date2);
		return interpolate(date1,value1,date2,value2,date);
	}
	
	public void saveToFile(String file){
		
			try{
				PrintWriter writer = new PrintWriter(file, "UTF-8");
				writer.print("#UTC,Distance to Sun, Distance to Earth, Distance to CG"+"\n");
				Iterator<Date> it = distanceSun.keySet().iterator();
				while(it.hasNext()){
					Date key = it.next();
					writer.print(DateUtil.dateToZulu(key)+","+distanceSun.get(key)+","+distanceEarth.get(key)+","+distanceCG.get(key)+"\n");
				}

				writer.close();
			}catch (Exception e){
				e.printStackTrace();
			}
	}
	public static RosettaDistance readFromFile(String file){
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			return readFromBufferedReader(br);
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
			
		}
	}
	private static RosettaDistance readFromBufferedReader(BufferedReader br){
		RosettaDistance result=new RosettaDistance();
		try {
		String line;
		
			while ((line = br.readLine()) != null) {
				if (line.startsWith("#")){
				}else{
					String[] fields = line.split(",");
					Date date = DateUtil.zuluToDate(fields[0]);
					Float sun=Float.parseFloat(fields[1]);
					Float earth=Float.parseFloat(fields[2]);
					Float comet=Float.parseFloat(fields[3]);
					result.addDistanceSun(date, sun);
					result.addDistanceEarth(date, earth);
					result.addDistanceCG(date, comet);
					
				}
				
			}
			br.close();
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		
		result.populateTable();
		return result;
	}
	
	public static  RosettaDistance readDataFromURL(){
		RosettaDistance result=new RosettaDistance();
		String leadingURL="http://comsim.esac.esa.int/rossim/bgrieger/dat/";
		try {
			Date initDate=DateUtil.zuluToDate("2014-11-01T00:00:00");
			Date endDate=DateUtil.zuluToDate("2015-03-01T00:00:00");
			long iDateLong = initDate.getTime();
			long eDateLong=endDate.getTime();
			//for (long d=iDateLong;d<=eDateLong;d=d+86400000){
			for (long d=iDateLong;d<=eDateLong;d=d+5000){	
				Date cDate = new Date(d);
				Calendar cal=Calendar.getInstance();
				cal.setTimeZone(TimeZone.getTimeZone("UTC") );
				cal.setTime(cDate);
				int year=cal.get(Calendar.YEAR);
				int month=cal.get(Calendar.MONTH)+1;
				int day=cal.get(Calendar.DAY_OF_MONTH);
				String url=leadingURL+year+"-"+String.format("%02d", month)+"/sundist"+String.format("%02d", day)+".js";
				String earthurl=leadingURL+year+"-"+String.format("%02d", month)+"/earthdist"+String.format("%02d", day)+".js";
				String cometurl=leadingURL+year+"-"+String.format("%02d", month)+"/distance"+String.format("%02d", day)+".js";

				URL stockURL = new URL(url);
				BufferedReader in = new BufferedReader(new InputStreamReader(stockURL.openStream()));
				BufferedReader earth = new BufferedReader(new InputStreamReader(new URL(earthurl).openStream()));
				BufferedReader comet = new BufferedReader(new InputStreamReader(new URL(cometurl).openStream()));

				String line;
				d=DateUtil.zuluToDate(year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day)+"T00:00:00").getTime();
				while ((line = in.readLine()) != null) {
					if (line.startsWith("var") || line.startsWith("]")){
					}else{
						//System.out.println(line);
						line=line.replace(",", "");
						result.addDistanceSun(new Date(d), Float.parseFloat(line));
						d=d+5000;
						
					}
					
				}
				in.close();
				
				d=DateUtil.zuluToDate(year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day)+"T00:00:00").getTime();
				while ((line = earth.readLine()) != null) {
					if (line.startsWith("var") || line.startsWith("]")){
					}else{
						//System.out.println(line);
						line=line.replace(",", "");
						result.addDistanceEarth(new Date(d), Float.parseFloat(line));
						d=d+5000;
						
					}
					
				}
				earth.close();

				d=DateUtil.zuluToDate(year+"-"+String.format("%02d", month)+"-"+String.format("%02d", day)+"T00:00:00").getTime();
				while ((line = comet.readLine()) != null) {
					if (line.startsWith("var") || line.startsWith("]")){
					}else{
						line=line.replace(",", "");
						result.addDistanceCG(new Date(d), Float.parseFloat(line));
						d=d+5000;
						
					}
					
				}
				comet.close();
				
			}
		} catch (Exception e) {
			IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
			iae.initCause(e);
			throw(iae);
		}
		result.populateTable();
		return result;
	}
	
	private static RosettaDistance readDistancefromJar(){
		
		InputStream is = ObjectUtil.getClass("rosetta.uplink.pointing.RosettaDistance").getResourceAsStream("/rosetta/uplink/pointing/DIS-002.ROS");
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		return readFromBufferedReader(br);
	}
	
	public static RosettaDistance getInstance(){
		if (instance==null){
			instance= readDistancefromJar();
		}
		return instance;
	}
	Float interpolate(Date time1,float distance1,Date time2,float distance2, Date desiredTime) {
		return interpolate(time1.getTime(),distance1,time2.getTime(),distance2,desiredTime.getTime());
	}
	Float interpolate(long time1,float distance1,long time2,float distance2, long desiredTime) {
		assert time1<desiredTime;
		assert desiredTime<time2;
		float m = (distance2-distance1)/(time2-time1);
		float y = ((desiredTime-time1)*m)+distance1;
		return y;
	}
	


}
