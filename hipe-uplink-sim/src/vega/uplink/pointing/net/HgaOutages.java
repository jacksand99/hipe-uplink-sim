package vega.uplink.pointing.net;
import herschel.ia.dataset.TableDataset;

import java.text.SimpleDateFormat;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.pointing.PointingBlock;
public class HgaOutages {
	private Date startDate;
	private Date endDate;
	private Date firstLimit;
	private String strategy;
	private String reason;
	private int id;
	private String notes;
	private String blocks;
	public HgaOutages(Date start, Date end, Date first, String hgaStrategy, String outageReason,int reasonId){
		startDate=start;
		endDate=end;
		firstLimit=first;
		strategy=hgaStrategy;
		reason=outageReason.trim();
		id=reasonId;
		notes="";
		blocks="";
	}
	
	public Date getStartDate(){
		return startDate;
	}
	
	public Date getEndDate(){
		return endDate;
	}
	
	public Date getFirstLimitDate(){
		return firstLimit;
	}
	public String getHgaStrategy(){
		return strategy;		
	}
	public String getReason(){
		return reason;
	}
	
	public int getReasonId(){
		return id;
	}
	
	public String toString(){
		SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		//dateFormat2.setTimeZone(TimeZone);
		String result="From "+dateFormat2.format(getStartDate())+" to "+dateFormat2.format(getEndDate())+" "+getReason()+ " "+notes;
		return result;
	}
	public String toHTML(){
		String result="";
		String PASS_TABLE_HEADER=""
				+ "<tr>\n"
				+ "	<th>Start time</th><th>End time</th><th>Reason</th><th>Notes</th><th>Blocks</th>\n"
				+ "</tr>\n";
		result=result+"<table class=\"gridtable\">"+PASS_TABLE_HEADER;
		result=result+toHTMLRow();
		result=result+"</table>";
		return result;

	}
	public String toHTMLRow(){
		String result="";
		result=result+"<tr><td>"+DateUtil.dateToZulu(getStartDate())+"</td><td>"+DateUtil.dateToZulu(getEndDate())+"</td><td>"+getReason()+"</td><td>"+notes+"</td><td>"+blocks+"</td></tr>";
		return result;
	}
	public void addNote(String newNote){
		notes=notes+"\n"+newNote;
	}
	
	public void setBlocks(String bl){
		blocks=bl;
	}
}
