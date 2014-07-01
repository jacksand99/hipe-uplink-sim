package vega.uplink.commanding;

import herschel.ia.numeric.Float1d;
import herschel.ia.numeric.Long1d;
//import herschel.share.util.Configuration;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

import vega.uplink.Properties;

public class SsmmSimulator {
	java.util.HashMap<String,InstrumentSimulator> instrumentSimulators;
	
	private void init(){
		instrumentSimulators=new java.util.HashMap<String,InstrumentSimulator>();
		
	}
	
	protected InstrumentSimulator getSimulator(String instrument){
		InstrumentSimulator simulator = instrumentSimulators.get(instrument);
		if (simulator==null){
			simulator=new InstrumentSimulator(instrument);
			instrumentSimulators.put(instrument, simulator);
		}
		
		return simulator;
	}
	public SsmmSimulator(){
		init();
	}
	
	public void reset(){
		init();
	}

	public void addAction(String instrument, Date time,float rate){
		InstrumentSimulator simulator = getSimulator(instrument);
		simulator.add(time, rate);
	}
	
	public String  addGsPass(GsPass pass){
		SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String strategy="";
		TreeMap<Float,String> tm=new TreeMap<Float,String>();
		String[] instruments=this.getAllInstruments();
		for (int i=0;i<instruments.length;i++){
			InstrumentSimulator simulator = getSimulator(instruments[i]);
			float tmAtPass=simulator.getValueAt(pass.getStartPass());
			if (tmAtPass>0) tm.put(tmAtPass, instruments[i]);
			//if (tm.size()>0) strategy=strategy+"Dump strategy used for pass "+pass.getGroundStation()+" starting at "+dateFormat2.format(pass.getStartPass())+" and ending at "+dateFormat2.format(pass.getEndPass())+"\n";
			//System.out.println("Tm at pass for "+instruments[i]+" "+tmAtPass);
		}
		float tmToDownload=pass.getTmRate()*pass.getDumpDurationSecs();
		Iterator<Entry<Float, String>> it = tm.descendingMap().entrySet().iterator();
		Date startDate=pass.getStartDump();
		Date endDate=pass.getStartDump();

		while (it.hasNext()){
			Entry<Float, String> entry = it.next();
			InstrumentSimulator simulator = getSimulator(entry.getValue());
			long duration=0;
			if (entry.getKey()<tmToDownload){
				duration=new Float(entry.getKey()/pass.getTmRate()).longValue();
				endDate=new Date(startDate.getTime()+(duration*1000)+1);
				if(!startDate.equals(endDate)){
					simulator.addDump(startDate, endDate, pass.getTmRate());
					strategy=strategy+"Dump "+entry.getValue()+" from "+dateFormat2.format(startDate)+" to "+dateFormat2.format(endDate)+" at "+pass.getTmRate()+" bits/sec\n";
				}
			}else {
				duration=new Float(tmToDownload/pass.getTmRate()).longValue();
				endDate=new Date(startDate.getTime()+(duration*1000)+1);
				if(!startDate.equals(endDate)){
					simulator.addDump(startDate, endDate, pass.getTmRate());
					strategy=strategy+"Dump "+entry.getValue()+" from "+dateFormat2.format(startDate)+" to "+dateFormat2.format(endDate)+" at "+pass.getTmRate()+" bits/sec\n";

				}
				
			}
			startDate=endDate;
			tmToDownload=tmToDownload-entry.getKey();
			if (tmToDownload<0) tmToDownload=0;
		}
		if (!strategy.equals("")) strategy=strategy=strategy+"Dump strategy used for pass "+pass.getGroundStation()+" starting at "+dateFormat2.format(pass.getStartPass())+" and ending at "+dateFormat2.format(pass.getEndPass())+"\n"+strategy;
		return strategy;
		
	}
	
	public float getMemoryAt(String instrument,java.util.Date time){
		InstrumentSimulator simulator = getSimulator(instrument);
		return simulator.getValueAt(time);
	}
	
	public Float getTotalMemoryAt(java.util.Date time){
		Float result=new Float(0);
		String[] insNames=getAllInstruments();
		for (int i=0;i<insNames.length;i++){
			result=result+getMemoryAt(insNames[i],time);
		}
		return result;
	}
	
	
	public String[] getAllInstruments(){
		String[] result = new String[instrumentSimulators.size()];
		instrumentSimulators.keySet().toArray(result);
		return result;
	}
	
	
	public Float[] getAllMemoryAt(Long[] times){
		Float[] result=new Float[times.length];
		for (int i=0;i<times.length;i++){
			Long time=times[i];
			Float value=getTotalMemoryAt(new java.util.Date(time.longValue()));
			result[i]=value;
		}
		return result;
		
	}
	
	
	public Float[] getValueAt(String instrument,Long[] times){
		InstrumentSimulator simulator = getSimulator(instrument);
		return simulator.getValuesAt(times);
	}
	
	public Float[] getValuesAt(String instrument,Long[] times){
		InstrumentSimulator simulator = getSimulator(instrument);
		return simulator.getValuesAt(times);
	}


	
	public Long1d toLong1d(Long[] array){
		Long1d result=new Long1d();
		for (int i=0;i<array.length;i++){
			result.append(array[i].longValue());
		}
		return result;

	}
	public Float1d toFloat1d(Float[] array){
		Float1d result=new Float1d();
		if (array==null) return result;
		for (int i=0;i<array.length;i++){
			result.append(array[i].floatValue());
			
		}
		return result;

	}

	

	
	
	protected class InstrumentSimulator{
		String instrument;
		java.util.TreeMap<Date,Float> rates;
		/*java.util.TreeMap<Date,Float> startDump;
		java.util.TreeSet<Date> endDump;*/
		long packetStoreSize;
		
		InstrumentSimulator(String instrumentName){
			instrument=instrumentName;
			rates=new java.util.TreeMap<Date,Float>();
			//startDump=new java.util.TreeMap<Date,Float>();
			//endDump=new java.util.TreeSet<Date>();
			try{
				packetStoreSize=Long.parseLong(Properties.getProperty(Properties.SSMM_PACKETSTORE_PREFIX+instrumentName));
			}catch (herschel.share.util.ConfigurationException e){
				//e.printStackTrace();
				packetStoreSize=104857600;
			}
		}
		
		float percentageFull(Date time){
			return (getValueAt(time)/100)*packetStoreSize;
		}
		
		String getInstrument(){
			return instrument;
		}
		
		void add(Date time,float rate){
			rates.put(time, rate);
		}
		
		void addDump(Date startTime,Date endTime,float rate){
			java.util.TreeMap<Date,Float> ratesToAdd=new java.util.TreeMap<Date,Float>();
			Entry<Date, Float> formerRateBeforeStartDump = rates.floorEntry(startTime);
			/*System.out.println("***************");
			System.out.println(startTime);
			System.out.println(rate);
			System.out.println(this.getInstrument());
			System.out.println(formerRateBeforeStartDump.getValue());

			System.out.println("***************");*/

			ratesToAdd.put(startTime, formerRateBeforeStartDump.getValue()-rate);
			Entry<Date, Float> formerRateBeforeEndDump = rates.floorEntry(startTime);
			//ratesToAdd.put(endTime, formerRateBeforeEndDump.getValue());
			
			SortedMap<Date, Float> mapInMiddle = rates.subMap(startTime, endTime);
			Iterator<Entry<Date, Float>> it = mapInMiddle.entrySet().iterator();
			while(it.hasNext()){
				Entry<Date, Float> entry = it.next();
				ratesToAdd.put(entry.getKey(), entry.getValue()-rate);
			}
			ratesToAdd.put(endTime, formerRateBeforeEndDump.getValue());
			rates.putAll(ratesToAdd);
			
		}
		
		
		Float getValueAt(java.util.Date time){
			Entry<Date, Float> ratEntry = rates.floorEntry(time);
			if (ratEntry==null) return new Float(0);
			float value=0;

			try{
				SortedMap<Date, Float> submap = rates.subMap(rates.firstKey(), new java.util.Date(ratEntry.getKey().getTime()-1));
				Iterator<Entry<Date, Float>> it = submap.entrySet().iterator();
				while (it.hasNext()){
					Entry<Date, Float> tempEntry = it.next();
					Entry<Date, Float> nextEntry = rates.ceilingEntry(new Date(tempEntry.getKey().getTime()+1));
					long durationSecs=(nextEntry.getKey().getTime()-tempEntry.getKey().getTime())/1000;
					value=value+(tempEntry.getValue()*durationSecs);
					if (value<0) value=0;
					if (value>packetStoreSize) value=packetStoreSize;
				}

			}
			catch (IllegalArgumentException iae){
			}
			long durationSecs=(time.getTime()-ratEntry.getKey().getTime())/1000;
			float result=new Float(value+(ratEntry.getValue()*durationSecs));
			if (result<0) result=0;
			if (result>packetStoreSize) result=packetStoreSize;
			return result;
		}
		
		
		Float[] getValuesAt(Long[] times){
			Float[] result=new Float[times.length];
			for (int i=0;i<times.length;i++){
				result[i]=getValueAt(new Date(times[i]));
			}
			return result;

		}
	}
	public void addSequence(Sequence seq){
		//Do nothing. To be implemented by inheriting classes
	}
		
}