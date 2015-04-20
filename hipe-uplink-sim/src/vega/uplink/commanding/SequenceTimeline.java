package vega.uplink.commanding;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;

import vega.uplink.DateUtil;
import vega.uplink.Properties;

public class SequenceTimeline {
		HashSet<SequenceExecution> set;
		Mib mib;
		public SequenceTimeline(){
			set=new HashSet<SequenceExecution>();
			try {
				mib=Mib.getMib();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				IllegalArgumentException iae = new IllegalArgumentException("Could not get Mib:"+e.getMessage());
				iae.initCause(e);
				throw(iae);
			}
		}
		public AbstractSequence[] getSequencesBetween(Date startDate,Date endDate){
			java.util.Vector<AbstractSequence> affected=new java.util.Vector<AbstractSequence>();
			Iterator<SequenceExecution> it = set.iterator();
			while (it.hasNext()){

				SequenceExecution pass = it.next();
					boolean con1=false;
					boolean con2=false;
					if (pass.getStartTime().after(endDate) ) con1=true;
					if (pass.getEndTime().before(startDate)) con2=true;
					if (!con1 && !con2) affected.add(pass.getSequence());
			}
			AbstractSequence[] result= new AbstractSequence[affected.size()];
			result=affected.toArray(result);
			return result;
			
		}
		
		public SequenceExecution[] findOverlapingExecution(SequenceExecution se){
			Date endDate = se.getEndTime();
			Date startDate = se.getStartTime();
			java.util.Vector<SequenceExecution> affected=new java.util.Vector<SequenceExecution>();
			Iterator<SequenceExecution> it = set.iterator();
			while (it.hasNext()){

				SequenceExecution pass = it.next();
				if (!pass.equals(se)){
					boolean con1=false;
					boolean con2=false;
					if (pass.getStartTime().after(endDate) ) con1=true;
					if (pass.getEndTime().before(startDate)) con2=true;
					if (!con1 && !con2) affected.add(pass);
				}
			}
			SequenceExecution[] result= new SequenceExecution[affected.size()];
			result=affected.toArray(result);
			return result;
		}
		public String findAllOverlapping(){
			AbstractAllowOverlapChecker checker;
			String className=null;
			try{
				className=Properties.getProperty("vega.uplink.overlapChecker");
			}
			catch(Exception e){
				className="vega.uplink.commanding.RosettaOverlapChecker";
			}
			if (className==null) className="vega.uplink.commanding.RosettaOverlapChecker";
			try {
				checker = (AbstractAllowOverlapChecker)Class.forName(className).newInstance();
			} catch (InstantiationException | IllegalAccessException
					| ClassNotFoundException e) {
				// TODO Auto-generated catch block
				checker = new AbstractAllowOverlapChecker(){
					public boolean allowOverlap(AbstractSequence a,AbstractSequence b){
						return true;
					}
				};
				
			}
			String message="";
			Iterator<SequenceExecution> it = set.iterator();
			while (it.hasNext()){

				SequenceExecution pass = it.next();
				SequenceExecution[] over = findOverlapingExecution(pass);
				for (int i=0;i<over.length;i++){
					if (!checker.allowOverlap(pass.getSequence(), over[i].getSequence())) message=message+pass.getName()+" ("+mib.getSequenceDescription(pass.getName())+") at "+DateUtil.defaultDateToString(pass.getStartTime())+" overlaps with "+over[i].getName()+" ("+mib.getSequenceDescription(over[i].getName())+") at "+DateUtil.defaultDateToString(over[i].getStartTime())+"\n";
					//if (pass.getInstrument().equals(over[i].getInstrument())) message=message+pass.getName()+" ("+mib.getSequenceDescription(pass.getName())+") at "+DateUtil.defaultDateToString(pass.getStartTime())+" overlaps with "+over[i].getName()+" ("+mib.getSequenceDescription(over[i].getName())+") at "+DateUtil.defaultDateToString(over[i].getStartTime())+"\n";
				}
			}
			return message;
		}

		public void execute(AbstractSequence seq){
			Date startTime=seq.getExecutionDate();
			String sequenceName=seq.getName();
			long duration=mib.getTotalSequenceDuration(sequenceName);
			Date endTime=new Date(startTime.getTime()+(duration*1000));
			SequenceExecution execution = new SequenceExecution(startTime,endTime,seq);
			set.add(execution);
		}
		private class SequenceExecution implements Comparable<SequenceExecution>{
			Date startTime;
			Date endTime;
			AbstractSequence sequence;
			@Override
			public int compareTo(SequenceExecution o) {
				return startTime.compareTo(o.startTime);
			}
			SequenceExecution(Date sTime,Date eTime,AbstractSequence seq){
				startTime=sTime;
				endTime=eTime;
				sequence=seq;
			}
			Date getStartTime(){
				return startTime;
			}
			Date getEndTime(){
				return endTime;
			}
			String getName(){
				return sequence.getName();
			}
			public AbstractSequence getSequence(){
				return sequence;
			}
			public String getInstrument(){
				return sequence.getInstrument();
			}
			public boolean equals(SequenceExecution other){
				if (!startTime.equals(other.getStartTime())) return false;
				if (!endTime.equals(other.getEndTime())) return false;
				if (!getName().equals(other.getName())) return false;
				return true;
			}
			
			
		}
}
