package vega.uplink.planning.period;

import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

import java.util.Date;

import vega.uplink.pointing.PointingBlock;

public abstract class Period extends MapContext implements Comparable<Period>{
	/*private Date startDate;
	private Date endDate;
	private int number;*/
	
	public Period (int number,Date startDate,Date endDate){
		super();
		this.getMeta().set("number", new StringParameter(""+number));
		this.setStartDate(new FineTime(startDate));
		this.setEndDate(new FineTime(endDate));
		//this.getMeta().set("startDate", new DateParameter(new FineTime(startDate)));
		//this.getMeta().set("endDate", new DateParameter(new FineTime(endDate)));
		
		//this.number=number;
		//this.startDate=startDate;
		//this.endDate=endDate;
	}
	
	public int getNumber(){
		return Integer.parseInt((String)this.getMeta().get("number").getValue());
		
	}
	
	/*public Date getStartDate(){
		return ((FineTime)this.getMeta().get("startDate").getValue()).toDate();
	}*/
	
	/*public Date getEndDate(){
		return endDate;
	}*/

	//@Override
	public int compareTo(Period o) {
		if (this.getStartDate().after(o.getStartDate())) return -1;
		if (this.getEndDate().before(o.getStartDate())) return 1;
		// TODO Auto-generated method stub
		return 0;
	}
	public Period[] getSubPeriods(){
		return new Period[0];
	}
	
	public Period getLastSubPeriod(){
		Period[] pers = this.getSubPeriods();
		if (pers.length==0) return null;
		return pers[pers.length-1];
	}
	public Period getFirstSubPeriod(){
		Period[] pers = this.getSubPeriods();
		if (pers.length==0) return null;
		return pers[0];
	}
	public String toXml(int indent){
		return toXml("PERIOD",indent);
	}
	public String toXml(){
		return toXml(0);
	}

	
	public String toXml(String tag,int indent){
		String iS="";
		for (int i=0;i<indent;i++){
			iS=iS+"\t";
		}
		String result="";
		result = result+iS+"<"+tag+" number="+this.getNumber()+">\n";
		result=result+iS+"\t<startTime="+PointingBlock.dateToZulu(this.getStartDate().toDate())+">\n";
		result=result+iS+"\t<endTime="+PointingBlock.dateToZulu(this.getEndDate().toDate())+">\n";
		Period[] sp = this.getSubPeriods();
		for (int j=0;j<sp.length;j++){
			result=result+sp[j].toXml(indent+1);
		}
		result = result+iS+"<"+tag+">\n";
		return result;
	}
	protected void fixEndDate(){
		Period[] sp = this.getSubPeriods();
		
		for (int i=0;i<sp.length;i++){
			sp[i].fixEndDate();
			if (i>0){
				sp[i-1].setEndDate(sp[i].getStartDate());
			}
		}
		if (sp.length>0){
			this.setStartDate(sp[0].getStartDate());
			this.setEndDate(sp[sp.length-1].getEndDate());
		}
	}
	
	/*public void setStartDate(Date startDate){
		this.startDate=startDate;
	}
	
	public void setEndDate(Date endDate){
		this.endDate=endDate;
	}*/

}
