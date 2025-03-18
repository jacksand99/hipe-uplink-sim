package vega.uplink;

import java.text.ParseException;
import java.util.Date;

public class test {
	public static void main(String[] args){
		String d="2015-098T11:25:11Z";
		try {
		    Evtm evtm = new Evtm();
		    
			Date date = DateUtil.parse(d);
			System.out.println(DateUtil.defaultDateToString(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
