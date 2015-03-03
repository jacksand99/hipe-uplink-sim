package vega.uplink.pointing;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;

public class PrmTime extends PointingElement{
	
	public PrmTime(PointingElement el){
		this(el.getName(),el.getValue());
	}
	public PrmTime(String name,Date date){
		super(name,DateUtil.dateToZulu(date));
	}
	public PrmTime(String name,String date) {
		super(name,date);
		try {
			setDate(DateUtil.zuluToDate(date));
		} catch (ParseException e) {
			IllegalArgumentException iae = new IllegalArgumentException("Incorrect Date:"+e.getMessage());
			iae.initCause(e);
			throw(iae);
			// TODO Auto-generated catch block
		}
	}
	
	public void setDate(Date date){
		this.setValue(DateUtil.dateToZulu(date));
	}
	
	public Date getDate() throws ParseException{
		return DateUtil.zuluToDate(this.getValue());
	}
	
	public boolean equals(PrmTime another){
		boolean result=true;
		if (!this.getName().equals(another.getName())) return false;
		try {
			if (!this.getDate().equals(another.getDate())) return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return result;
	}
}
