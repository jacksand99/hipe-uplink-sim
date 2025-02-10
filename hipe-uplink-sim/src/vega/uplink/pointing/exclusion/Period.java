package vega.uplink.pointing.exclusion;

import java.util.Date;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.DateUtil;
//import vega.uplink.planning.period.Period;

public class Period{
	Date start;
	Date end;
	String type;
	
	public Date getStartDate(){
		return start;
	}
	
	public Date getEndDate(){
		return end;
	}
	
	public String getType(){
		return type;
	}
	
	public Period(Date start, Date end,String type){
		this.start=start;
		this.end=end;
		this.type=type;
	}
	
	public String toString(){
		return type+": "+DateUtil.dateToZulu(start)+" - "+DateUtil.dateToZulu(end);
	}
	
	public String toHtmlRow(){
		return "<tr><td>"+type+"</td><td>"+DateUtil.dateToZulu(start)+"</td><td>"+DateUtil.dateToZulu(end)+"</td></tr>";
		
	}
	
	public String toXml(){
		return toXml(0);
	}
	
	public String toXml(int indent){
		String tag="exclusion";
		String iS="";
		for (int i=0;i<indent;i++){
			iS=iS+"\t";
		}
		String result="";
		result = result+iS+"<"+tag+" type='"+type+"'>\n";
		
		result=result+iS+"\t<startTime>"+DateUtil.dateToZulu(this.getStartDate())+"</startTime>\n";
		result=result+iS+"\t<endTime>"+DateUtil.dateToZulu(this.getEndDate())+"</endTime>\n";

		result = result+iS+"</"+tag+">\n";
		return result;
	}
	
	public static Period readFromNode(Node node){
		//Period result=null;
		try{
			if (node.getAttributes()==null){
				throw new IllegalArgumentException("Could not read type "+node.toString());
			}
			Node attributeType = node.getAttributes().getNamedItem("type");
			if (attributeType==null) throw new IllegalArgumentException("Could not get the attribute type:"+node.toString());
			String type=node.getAttributes().getNamedItem("type").getNodeValue();
			//int number=Integer.parseInt(node.getAttributes().getNamedItem("number").getNodeValue());
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
			if (startTime!=null && endTime!=null ){
				return new Period(startTime,endTime,type);
			}else throw new IllegalArgumentException("Could not read dates "+node.toString());
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not read exclusion period "+node.toString());
			iae.initCause(e);
			throw(iae);
		}
	}
	
}
