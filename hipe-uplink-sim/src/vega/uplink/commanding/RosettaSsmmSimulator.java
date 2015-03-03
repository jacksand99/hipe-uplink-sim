package vega.uplink.commanding;

import herschel.share.util.Configuration;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Vector;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.SsmmSimulator.InstrumentSimulator;

public class RosettaSsmmSimulator extends SsmmSimulator {
	//public HashMap<String,Integer> priorities;
	public HashMap<String,String> priorityParameter;
	public HashMap<String,String> priorityParameterReverse;
	
	public TreeMap<Date,HashMap<String,Integer>> priorities;
	
	public String priorityCommand;
	public RosettaSsmmSimulator(SimulationContext context){
		super(context);
		HashMap<String,Integer> prio=new HashMap<String,Integer>();
		priorities=new TreeMap<Date,HashMap<String,Integer>>();
		priorityParameter=new HashMap<String,String>();
		priorityParameterReverse=new HashMap<String,String>();
		List<String> insList = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = insList.iterator();
		while (it.hasNext()){
			String ins=it.next();
			prio.put(ins, 18);
			try{
				priorityParameter.put(Properties.getProperty(Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+ins),ins);
				priorityParameterReverse.put(ins, Properties.getProperty(Properties.ANTENNA_PRIORITY_PARAMETER_PREFIX+ins));
			}catch (Exception e){
				e.printStackTrace();
				priorityParameter.put("VOID",ins);
			}
		}
		priorities.put(new Date(0L), prio);
		try{
			priorityCommand=Properties.getProperty(Properties.ANTENNA_PRIORITY_COMMAND);
		}catch(Exception e){
			e.printStackTrace();
			priorityCommand="ASYF033A";
		}

	}
	
	public void addSequence(Sequence seq){

		HashMap<String,Integer> prio=new HashMap<String,Integer>();
		if (seq.getName().equals(priorityCommand)){
			Parameter[] params = seq.getParameters();
			for (int i=0;i<params.length;i++){
				ParameterFloat param = (ParameterFloat) params[i];
				String paramName = param.getName();
				int paramValue = new Float(param.getValue()).intValue();
				prio.put(priorityParameter.get(paramName), paramValue);
			}
			priorities.put(seq.getExecutionDate(), prio);
		}
	}
	
	public String  addGsPass(GsPass pass){
		//Get first propoused priorities
		TreeMap<Float,String> tm=new TreeMap<Float,String>();
		String[] instruments=this.getAllInstruments();
		for (int i=0;i<instruments.length;i++){
			InstrumentSimulator simulator = getSimulator(instruments[i]);
			float percentage=simulator.percentageFull(pass.getStartPass());
			if (percentage>0) tm.put(percentage,instruments[i]);
		}
		
		
		
		int sPrio=17;
		Iterator<Entry<Float, String>> perIt = tm.entrySet().iterator();
		//String dString=(""+new Date().getTime()).substring(5);
		try{
			//Sequence priSeq=new Sequence(priorityCommand,"P"+dString,Sequence.dateToZulu(pass.getStartPass()));
			Sequence priSeq=new PrioritySequence();
			priSeq.setExecutionDate(pass.getStartPass());
			while(perIt.hasNext()){
				Entry<Float, String> entry = perIt.next();
				String ins=entry.getValue();
				String param=priorityParameterReverse.get(ins);
				//priSeq.addParameter(sequenceParameter);
				//priSeq.addParameter(new ParameterFloat(param,Parameter.REPRESENTATION_RAW,Parameter.RADIX_DECIMAL,sPrio));
				priSeq.set(param,new ParameterFloat(param,Parameter.REPRESENTATION_RAW,Parameter.RADIX_DECIMAL,sPrio));
				sPrio--;
			}
			//SimulationContext.getInstance().getDlPor().addSequence(priSeq);
		}catch (Exception e){
			e.printStackTrace();
		}
		return addPass(pass);
		
	}
	private String addPass(GsPass pass){

		String strategy="";
		float tmToDownload=pass.getTmRate()*pass.getDumpDurationSecs();
		Date startDate=pass.getStartDump();
		Date endDate=pass.getEndDump();
		Vector<String>[] arrayPriorities=new Vector[18];
		HashMap<String,Integer> prio=priorities.floorEntry(pass.getStartDump()).getValue();
		for (int i=0;i<18;i++){
			arrayPriorities[i]=new Vector<String>();
		}
		Iterator<Entry<String, Integer>> it2 = prio.entrySet().iterator();
		while (it2.hasNext()){
			Entry<String, Integer> entry = it2.next();
			Vector<String> vector = arrayPriorities[entry.getValue()-1];
			vector.add(entry.getKey());
		}
		for (int i=0;i<18;i++){
			Vector<String> vector = arrayPriorities[i];
			float maxTmPerInstrument=tmToDownload/vector.size();
			for (int j=0;j<vector.size();j++){
				InstrumentSimulator simulator = getSimulator(vector.get(j));
				Float tmAtPass = simulator.getValueAt(startDate);
				//System.out.println(simulator.getInstrument()+" with priority "+i+" has "+tmAtPass+" and the max per instrument is "+maxTmPerInstrument);
				if (tmAtPass<=maxTmPerInstrument ){
					if (tmAtPass>0){
						//float rate = tmAtPass/pass.getDumpDurationSecs();
						float duration = tmAtPass/pass.getTmRate();
						//System.out.println("duration:"+duration);
						if (duration>1){
							
							long eDate = startDate.getTime()+(new Float(duration).longValue()*1000);
							simulator.addDump(startDate, new Date(eDate), pass.getTmRate());
							tmToDownload=tmToDownload-tmAtPass;
							//startDate=new Date(eDate);
							//System.out.println("Dump "+simulator.getInstrument()+" from "+dateFormat2.format(startDate)+" to "+dateFormat2.format(new Date(eDate))+" at "+pass.getTmRate()+" bits/sec\n");
							strategy=strategy+"Dump "+simulator.getInstrument()+" from "+DateUtil.dateToZulu(startDate)+" to "+DateUtil.dateToZulu(new Date(eDate))+" at "+pass.getTmRate()+" bits/sec\n";
							startDate=new Date(eDate+1);
						}else{
							simulator.addDump(startDate, new Date(startDate.getTime()+1000), pass.getTmRate());
							startDate=new Date(startDate.getTime()+1002);							
							tmToDownload=tmToDownload-tmAtPass;
						}
					}
				}else{
					if (maxTmPerInstrument>0){
						//float rate = maxTmPerInstrument/pass.getDumpDurationSecs();
						float duration = maxTmPerInstrument/pass.getTmRate();
						if (duration>1){

							long eDate = startDate.getTime()+(new Float(duration).longValue()*1000);
							simulator.addDump(startDate, new Date(eDate), pass.getTmRate());
							tmToDownload=tmToDownload-maxTmPerInstrument;
							
							strategy=strategy+"Dump "+simulator.getInstrument()+" from "+DateUtil.dateToZulu(startDate)+" to "+DateUtil.dateToZulu(endDate)+" at "+pass.getTmRate()+" bits/sec\n";
							startDate=new Date(eDate+1);
						}
						else{
							simulator.addDump(startDate, new Date(startDate.getTime()+1000), pass.getTmRate());
							startDate=new Date(startDate.getTime()+1002);							
							tmToDownload=tmToDownload-maxTmPerInstrument;

						}
					}
				}
			}
		}
		/*System.out.println("********************");
		System.out.println("Still "+tmToDownload+" can be downloaded and the memory has "+getTotalMemoryAt(startDate));
		System.out.println("orginal start date was"+pass.getStartDump()); 
		System.out.println(startDate);
		System.out.println(pass.getEndDump());

		System.out.println("********************");*/

		if (this.getTotalMemoryAt(new Date(startDate.getTime()+1))>pass.getTmRate() && tmToDownload>0 && startDate.before(pass.getEndDump())){
			/*System.out.println("********************");
			System.out.println("Still "+tmToDownload+" can be downloaded and the memory has "+getTotalMemoryAt(startDate));
			//System.out.println("orginal start date was 
			System.out.println(startDate);
			System.out.println(pass.getEndDump());

			System.out.println("********************");*/

			GsPass temp = new GsPass(pass);
			temp.setStartDump(startDate);
			addPass(temp);
		}
		if (!strategy.equals("")) strategy=strategy+"Dump strategy used for pass "+pass.getGroundStation()+" starting at "+DateUtil.dateToZulu(pass.getStartPass())+" and ending at "+DateUtil.dateToZulu(pass.getEndPass())+"\n"+strategy;
		return strategy;
	}
}
		/*while (it.hasNext()){
			Entry<String, Float> entry = it.next();
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
		
	}*/


//}
