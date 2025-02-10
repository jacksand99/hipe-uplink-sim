package vega.uplink.planning.period;

import java.util.Date;

public class Vstp extends Period {
	public static String TAG="VSTP";
	public Vstp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
	}
	public Vstp(int number,Date startDate){
		this(number,startDate,new Date(65277473000L));
	}
	
	public String toXml(int indent){
		return toXml(TAG,indent);
	}
	public String getTag(){
		return Vstp.TAG;
	}
	


}
