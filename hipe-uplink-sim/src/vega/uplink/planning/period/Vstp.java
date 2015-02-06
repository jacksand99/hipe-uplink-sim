package vega.uplink.planning.period;

import java.util.Date;

public class Vstp extends Period {

	public Vstp(int number, Date startDate, Date endDate) {
		super(number, startDate, endDate);
		// TODO Auto-generated constructor stub
	}
	public Vstp(int number,Date startDate){
		this(number,startDate,new Date(65277473000L));
	}
	
	public String toXml(int indent){
		return toXml("VSTP",indent);
	}

}
