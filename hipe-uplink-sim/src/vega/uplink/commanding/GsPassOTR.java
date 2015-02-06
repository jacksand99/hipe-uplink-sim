package vega.uplink.commanding;

import java.util.Date;

public class GsPassOTR extends GsPassBSR{
	public GsPassOTR(Date startDate,Date endDate,String station){
		super(startDate,endDate,station);
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		String l1="<fcs id=\"BOTR\" time=\""+dateToZulu(getStartPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l4="<fcs id=\"EOTR\" time=\""+dateToZulu(getEndPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		return l1+l4;
	}
	public boolean equals(GsPassOTR pass){
		if (!this.getStartPass().equals(pass.getStartPass())) return false;
		if (!this.getEndPass().equals(pass.getEndPass())) return false;
		//if (!startDump.equals(pass.getStartDump())) return false;
		//if (!endDump.equals(pass.getEndDump())) return false;
		if (this.getGroundStation().equals(pass.getGroundStation())) return false;
		//if (tmRate!=pass.getTmRate()) return false;
		
		return true;
	}


}