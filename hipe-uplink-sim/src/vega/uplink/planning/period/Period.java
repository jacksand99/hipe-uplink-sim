package vega.uplink.planning.period;

import herschel.ia.dataset.StringParameter;
import herschel.ia.pal.MapContext;
import herschel.share.fltdyn.time.FineTime;

import java.util.Date;

import vega.uplink.DateUtil;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Period extends MapContext implements Comparable<Period>{
	public static String TAG="PERIOD";
	public Period (int number,Date startDate,Date endDate){
		super();
		this.getMeta().set("number", new StringParameter(""+number));
		this.setStartDate(new FineTime(startDate));
		this.setEndDate(new FineTime(endDate));
	}
	public void addSubPeriod(Period period){
		this.setProduct(period.getTag()+"-"+period.getNumber(), period);

		if (this.getStartDate().after(period.getStartDate())) this.setStartDate(period.getStartDate());
		if (this.getEndDate().before(period.getEndDate())) this.setEndDate(period.getEndDate());
	}
	
	public String getTag(){
		return Period.TAG;
	}
	
	public int getNumber(){
		return Integer.parseInt((String)this.getMeta().get("number").getValue());
		
	}
	

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
		result = result+iS+"<"+tag+" number='"+this.getNumber()+"'>\n";
		
		result=result+iS+"\t<startTime>"+DateUtil.dateToZulu(this.getStartDate().toDate())+"</startTime>\n";
		result=result+iS+"\t<endTime>"+DateUtil.dateToZulu(this.getEndDate().toDate())+"</endTime>\n";
		Period[] sp = this.getSubPeriods();
		for (int j=0;j<sp.length;j++){
			result=result+sp[j].toXml(indent+1);
		}
		result = result+iS+"</"+tag+">\n";
		return result;
	}
	protected void fixEndDate(){
		Period[] sp = this.getSubPeriods();
		
		for (int i=0;i<sp.length;i++){
			sp[i].fixEndDate();
			if (i>0){
				sp[i-1].setEndPeriod(sp[i].getStartDate());
			}
		}
		if (sp.length>0){
			this.setStartDate(sp[0].getStartDate());
			this.setEndPeriod(sp[sp.length-1].getEndDate());
		}
	}
	
	public void setEndPeriod(FineTime date){
		this.setEndDate(date);
		//Period[] sp = this.getSubPeriods();
		Period lp = getLastSubPeriod();
		if (lp!=null) lp.setEndPeriod(date);
	}
	public static Period readFromNode(Node node){
		Period result=null;
		try{
			if (node.getAttributes()==null){
				return null;
			}
			Node attributeNumber = node.getAttributes().getNamedItem("number");
			if (attributeNumber==null) throw new IllegalArgumentException("Could not get the attribute number:"+node.toString());
			int number=Integer.parseInt(node.getAttributes().getNamedItem("number").getNodeValue());
			NodeList childs = node.getChildNodes();
			int nChilds = childs.getLength();
			Date startTime=null;
			Date endTime=null;
			for (int i=0;i<nChilds;i++){
				Node cNode = childs.item(i);
				if (cNode.getNodeName().equals("startTime") ){
					startTime = DateUtil.zuluToDate(cNode.getTextContent());
				}
				if (cNode.getNodeName().equals("endTime") ){
					endTime = DateUtil.zuluToDate(cNode.getTextContent());
				}

			}
			if (startTime==null || endTime==null) throw new IllegalArgumentException("Could not find startTime or endTime of the node");
			if (node.getNodeName().equals("VSTP")) result=new Vstp(number,startTime,endTime);
			if (node.getNodeName().equals("STP")) result=new Stp(number,startTime,endTime);
			if (node.getNodeName().equals("MTP")) result=new Mtp(number,startTime,endTime);
			if (node.getNodeName().equals("LTP")) result=new Ltp(number,startTime,endTime);
			if (node.getNodeName().equals("PLAN")) result=new Plan(number,startTime,endTime);
			for (int i=0;i<nChilds;i++){
				Node cNode = childs.item(i);
				if (!cNode.getNodeName().equals("startTime") && !cNode.getNodeName().equals("endTime")){
					Period tempPeriod = Period.readFromNode(cNode);
					if (tempPeriod!=null){
						result.addSubPeriod(tempPeriod);
					}
				}
			}
			if (result!=null){
				return result;
			}
			else {
				IllegalArgumentException iae = new IllegalArgumentException("Could not read period "+node.getNodeName());
				throw (iae);
				
			}
			
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not read period "+e.getMessage());
			iae.initCause(e);
			throw (iae);
		}
	}
	

}
