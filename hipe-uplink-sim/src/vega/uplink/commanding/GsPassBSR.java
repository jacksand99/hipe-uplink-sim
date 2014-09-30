package vega.uplink.commanding;

import java.util.Date;

public class GsPassBSR extends GsPass {
	public GsPassBSR(Date startDate,Date endDate,String station){
		super(startDate,endDate,startDate,endDate,station,0);
	}
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		String l1="<fcs id=\"BOTB\" time=\""+dateToZulu(getStartPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l4="<fcs id=\"EOTB\" time=\""+dateToZulu(getEndPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		return l1+l4;
	}
	public boolean isBSR(){
		return true;
	}


}
