package vega.uplink.commanding;

import java.util.Date;

import vega.uplink.DateUtil;

public class GsPassOAL extends GsPassBSR{
	public GsPassOAL(Date startDate,Date endDate,String station){
		super(startDate,endDate,station);
	}
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		String l1="<fcs id=\"BOAL\" time=\""+DateUtil.dateToDOY(getStartPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l4="<fcs id=\"EOAL\" time=\""+DateUtil.dateToDOY(getEndPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		return l1+l4;
	}
	public boolean equals(GsPassOAL pass){
		if (!this.getStartPass().equals(pass.getStartPass())) return false;
		if (!this.getEndPass().equals(pass.getEndPass())) return false;
		if (this.getGroundStation().equals(pass.getGroundStation())) return false;
		return true;
	}


}