package vega.uplink.pointing;

import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;
import vega.uplink.Properties;

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
		    try {
		        setDate(DateUtil.DOYToDate(date));
		    }catch (ParseException e2) {
		        IllegalArgumentException iae = new IllegalArgumentException("Incorrect Date:"+e.getMessage());
		        iae.initCause(e);
		        throw(iae);
		    }
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
    public String toXml(int indent){
        String format = herschel.share.util.Configuration.getProperty(Properties.POINTING_DATE_FORMAT);
        StringBuilder result=new StringBuilder();
        StringBuilder iString=new StringBuilder();
        for (int i=0;i<indent;i++){
            iString.append("\t");
        }
        if (format.equals("DOY")) {
            result.append(iString+"<"+getName());
            result.append(" format=\"DOY\">");
            Date date;
            try {
                date = DateUtil.parse(getValue());
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                return super.toXml(indent);
            }
            result.append(DateUtil.dateToDOYNoMilli(date));
            result.append("</"+getName()+">"+"\n");
            return result.toString();
        }
        else {
            return super.toXml(indent);
        }
   
    }
}
