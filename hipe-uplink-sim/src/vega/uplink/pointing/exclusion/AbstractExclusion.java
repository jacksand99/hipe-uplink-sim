package vega.uplink.pointing.exclusion;

import java.text.ParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rosetta.uplink.pointing.ExclusionPeriod;
import vega.uplink.DateUtil;
import vega.uplink.commanding.itl.EventList;
import herschel.ia.dataset.Column;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.String1d;

public  class  AbstractExclusion extends TableDataset{

	private Vector<Period> cmd;
	private Vector<Period> nav;
	
	
	public AbstractExclusion(){
		cmd=new Vector<Period>();
		nav=new Vector<Period>();
		
		Column gs=new Column(new String1d());
		Column stpass=new Column(new String1d());
		Column edpass=new Column(new String1d());
		
		this.addColumn(gs);		
		this.addColumn(stpass);
		this.addColumn(edpass);

		this.setColumnName(0, "Type");
		this.setColumnName(1, "Start Date");
		this.setColumnName(2, "End Date");
		this.getMeta().set("name", new StringParameter(""+new Date().getTime()));

		
	}
	
	public void setName(String name){
		this.getMeta().set("name", new StringParameter(name));
	}
	
	public String getName(){
		return (String) this.getMeta().get("name").getValue();
	}
	
	public void addPeriod(Period period){
		if (period.getType().equals("CMD")) addCmdPeriod(period.getStartDate(),period.getEndDate());
		if (period.getType().equals("NAV")) addNavPeriod(period.getStartDate(),period.getEndDate());
		
	}
	public void addCmdPeriod(Date start,Date end){
		cmd.add(new Period(start,end,"CMD"));
		String[] row=new String[3];
		row[0]="CMD";
		row[1]=DateUtil.dateToZulu(start);
		row[2]=DateUtil.dateToZulu(end);

		addRow(row);

	}
	public void addNavPeriod(Date start,Date end){
		nav.add(new Period(start,end,"NAV"));
		String[] row=new String[3];
		row[0]="NAV";
		row[1]=DateUtil.dateToZulu(start);
		row[2]=DateUtil.dateToZulu(end);

		addRow(row);

	}
	
	public Period[] getCmdPeriods(){
		Period[] result= new Period[cmd.size()];
		result=cmd.toArray(result);
		return result;
	}
	public Period[] getNavPeriods(){
		Period[] result= new Period[nav.size()];
		result=nav.toArray(result);
		return result;
	}
	
	public Period[] getAllPeriods(){
		Period[] navP = getNavPeriods();
		Period[] cmdP = getCmdPeriods();
		Period[] result= new Period[navP.length+cmdP.length];
		int locator=0;
		for (int i=0;i<navP.length;i++){
			result[locator]=navP[i];
			locator++;
		}
		for (int i=0;i<cmdP.length;i++){
			result[locator]=cmdP[i];
			locator++;
		}
		
		return result;
		
		
	}
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String tag="exclusionPeriods";
		String iS="";
		for (int i=0;i<indent;i++){
			iS=iS+"\t";
		}
		String result="";
		result = result+iS+"<"+tag+">\n";
		
		Period[] allPeriods=this.getAllPeriods();
		for (int i=0;i<allPeriods.length;i++){
			result=result+allPeriods[i].toXml(indent+1);
		}

		result = result+iS+"</"+tag+">\n";
		return result;
	}

	
	public String toString(){
		String result="";
		Period[] periods = this.getAllPeriods();
		for (int i=0;i<periods.length;i++){
			result=result+periods[i]+"\n";
		}
		return result;
		
	}
	
	/*public static void main(String args[]){
		try {
			ExclusionPeriod periods = readFromFile("/Users/jarenas 1/Rosetta/hack 11/EXCL_DL_004_01____H__00076.evf");
			System.out.println(periods);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	public Period[] findOverlapingPeriods(Date startDate,Date endDate){
		java.util.Vector<Period> affected=new java.util.Vector<Period>();
		Iterator<Period> it = cmd.iterator();
		while (it.hasNext()){
			Period pass = it.next();
			boolean con1=false;
			boolean con2=false;
			if (pass.getStartDate().after(endDate)) con1=true;
			if (pass.getEndDate().before(startDate)) con2=true;
			if (!con1 && !con2) affected.add(pass);
			
		}
		it=nav.iterator();
		while (it.hasNext()){
			Period pass = it.next();
			boolean con1=false;
			boolean con2=false;
			if (pass.getStartDate().after(endDate)) con1=true;
			if (pass.getEndDate().before(startDate)) con2=true;
			if (!con1 && !con2) affected.add(pass);
			
		}
		
		Period[] result= new Period[affected.size()];
		result=affected.toArray(result);
		return result;
	}
	
	public static AbstractExclusion readFromNode(Node node){
		//Period result=null;
		AbstractExclusion result = new AbstractExclusion();
		try{
			NodeList childs = node.getChildNodes();
			int nChilds = childs.getLength();
			for (int i=0;i<nChilds;i++){
				if (childs.item(i).getNodeName().equals("exclusion")) result.addPeriod(Period.readFromNode(childs.item(i)));
			}
			return result;
		}catch (Exception e){
			IllegalArgumentException iae = new IllegalArgumentException("Could not read exclusion periods "+node.toString());
			iae.initCause(e);
			throw(iae);
		}
	}

}
