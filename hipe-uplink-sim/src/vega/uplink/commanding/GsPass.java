package vega.uplink.commanding;
import java.text.ParseException;
import java.util.Date;

import vega.uplink.DateUtil;

/**
 * Data model to store Ground staion passes included in a Fecs file
 * @author jarenas
 *
 */
public class GsPass implements Comparable<GsPass>{
	private Date startPass;
	private Date endPass;
	private Date startDump;
	private Date endDump;
	private String groundStation;
	private float tmRate;
	public static String[] DSN70m={"DSS-14","DSS-43","DSS-63"};
	
	/**
	 * Constructor of the Ground Station pass, taking as parameters all the required information
	 * @param passStart Acquisition of Signal of the GS pass
	 * @param passEnd Loss of Signal of the GS pass
	 * @param dumpStart start date of the actual dump of data
	 * @param dumpEnd end date of the actual dump of data
	 * @param groundStationName ground station used for this pass
	 * @param passTmRate telemetry downlink rate of this pass
	 */
	public GsPass(Date passStart,Date passEnd,Date dumpStart,Date dumpEnd,String groundStationName,float passTmRate){
		startPass=passStart;
		endPass=passEnd;
		startDump=dumpStart;
		endDump=dumpEnd;
		groundStation=groundStationName;
		tmRate=passTmRate;
	}
	public boolean equals(GsPass pass){
		if (!startPass.equals(pass.getStartPass())) return false;
		if (!endPass.equals(pass.getEndPass())) return false;
		if (!startDump.equals(pass.getStartDump())) return false;
		if (!endDump.equals(pass.getEndDump())) return false;
		if (!groundStation.equals(pass.getGroundStation())) return false;
		if (tmRate!=pass.getTmRate()) return false;
		
		return true;
	}
	
	public GsPass(GsPass org){
		this(org.getStartPass(),org.getEndPass(),org.getStartDump(),org.getEndDump(),org.getGroundStation(),org.getTmRate());
	}
	
	public Date getStartPass(){
		return startPass;
	}
	
	public void setStartPass(Date passStart){
		startPass=passStart;
	}
	
	public Date getEndPass(){
		return endPass;
	}
	
	public void setEndPass(Date passEnd){
		endPass=passEnd;
	}
	
	public Date getStartDump(){
		return startDump;
	}
	
	public void setStartDump(Date dumpStart){
		startDump=dumpStart;
	}
	
	public Date getEndDump(){
		return endDump;
	}
	
	public void setEndDump(Date dumpEnd){
		endDump=dumpEnd;
	}
	
	public String getGroundStation(){
		return groundStation;
	}
	
	public void setGroundStation(String groundStationName){
		groundStation=groundStationName;
	}
	
	public float getTmRate(){
		return tmRate;
	}
	
	public void setTmRate(float passTmRate){
		tmRate=passTmRate;
	}
	
	
	public String toString(){
		return toString(0);
	}
	public String toString(int count){
		String l1="<fcs id=\"BOT_\" time=\""+dateToZulu(getStartPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l2="<fcs id=\"STAD\" time=\""+dateToZulu(getStartDump())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\" tm_rate=\""+getTmRate()+"\"/>\n";
		String l3="<fcs id=\"STOD\" time=\""+dateToZulu(getEndDump())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l4="<fcs id=\"EOT_\" time=\""+dateToZulu(getEndPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		return l1+l2+l3+l4;
	}
	
	public static java.util.Date zuluToDate(String zuluTime) throws ParseException{

		return DateUtil.DOYToDate(zuluTime);
	}

	public static String dateToZulu(java.util.Date date){

		return DateUtil.dateToDOY(date);
	}

	public int compareTo(GsPass o) {
		return this.startDump.compareTo(o.startDump);
	}
	
	/**
	 * Get the TM dump duration in secons
	 * @return number of seconds of the duration this dump
	 */
	public int getDumpDurationSecs(){
		//int result=0;
		return new Long((this.getEndDump().getTime()-this.getStartDump().getTime())/1000).intValue();
		//return result;
	}
	public int getPassDurationSecs(){
		//int result=0;
		return new Long((this.getEndPass().getTime()-this.getStartPass().getTime())/1000).intValue();
		//return result;
	}
	
	public float getTotalDataDump(){
		return this.getTmRate()*getDumpDurationSecs();
	}
	public boolean isBSR(){
		return false;
	}
	public boolean is70m(){
		for (int i=0;i<DSN70m.length;i++){
			if (this.getGroundStation().equals(DSN70m[i])) return true;
		}
		//if (this.getTmRate()<90000) return false;
		return false;
	}
}
