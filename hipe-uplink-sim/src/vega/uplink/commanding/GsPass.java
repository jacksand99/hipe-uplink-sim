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
	/**
	 * Antennas that are 70m (currently DSS-14, DSS-43 and DSS-63)
	 */
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
	
	/**
	 * Get the start date of the Ground Station Pass
	 * @return
	 */
	public Date getStartPass(){
		return startPass;
	}
	
	/**
	 * Set the start date of the Ground Station pass
	 * @param passStart
	 */
	public void setStartPass(Date passStart){
		startPass=passStart;
	}
	
	/**
	 * get the end date of the ground station pass
	 * @return
	 */
	public Date getEndPass(){
		return endPass;
	}
	
	/**
	 * Set the end date of the ground station pass
	 * @param passEnd
	 */
	public void setEndPass(Date passEnd){
		endPass=passEnd;
	}
	
	/**
	 * Get the start date of the data dump
	 * @return
	 */
	public Date getStartDump(){
		return startDump;
	}
	
	/**
	 * Set the start date of the data dump
	 * @param dumpStart
	 */
	public void setStartDump(Date dumpStart){
		startDump=dumpStart;
	}
	
	/**
	 * Get the end date of the data dump
	 * @return
	 */
	public Date getEndDump(){
		return endDump;
	}
	
	/**
	 * Set the end date of the data dump
	 * @param dumpEnd
	 */
	public void setEndDump(Date dumpEnd){
		endDump=dumpEnd;
	}
	
	/**
	 * Get the ground station name of the pass
	 * @return
	 */
	public String getGroundStation(){
		return groundStation;
	}
	
	/**
	 * Set the ground station name of the pass
	 * @param groundStationName
	 */
	public void setGroundStation(String groundStationName){
		groundStation=groundStationName;
	}
	
	/**
	 * Get the telemetry bitrate of the pass
	 * @return
	 */
	public float getTmRate(){
		return tmRate;
	}
	
	/**
	 * Set the telemetry bitrate of the pass
	 * @param passTmRate
	 */
	public void setTmRate(float passTmRate){
		tmRate=passTmRate;
	}
	
	
	public String toString(){
		return toString(0);
	}
	/**
	 * Get the XML representation of the pass
	 * @param count number to be put in the count attribute
	 * @return
	 */
	public String toString(int count){
		String l1="<fcs id=\"BOT_\" time=\""+DateUtil.dateToDOY(getStartPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l2="<fcs id=\"STAD\" time=\""+DateUtil.dateToDOY(getStartDump())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\" tm_rate=\""+getTmRate()+"\"/>\n";
		String l3="<fcs id=\"STOD\" time=\""+DateUtil.dateToDOY(getEndDump())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		String l4="<fcs id=\"EOT_\" time=\""+DateUtil.dateToDOY(getEndPass())+"\" count=\""+count+"\" duration=\"0\" ems:station=\""+getGroundStation()+"\"/>\n";
		return l1+l2+l3+l4;
	}
	

	public int compareTo(GsPass o) {
		return this.startDump.compareTo(o.startDump);
	}
	
	/**
	 * Get the TM dump duration in seconds
	 * @return number of seconds of the duration this dump
	 */
	public int getDumpDurationSecs(){
		return new Long((this.getEndDump().getTime()-this.getStartDump().getTime())/1000).intValue();
	}
	/**
	 * Get the pass duration in seconds
	 * @return
	 */
	public int getPassDurationSecs(){
		return new Long((this.getEndPass().getTime()-this.getStartPass().getTime())/1000).intValue();
	}
	
	/**
	 * Get the total number of bits to be downloaded in this pass (TM bit rate * dump duration)
	 * @return
	 */
	public float getTotalDataDump(){
		return this.getTmRate()*getDumpDurationSecs();
	}
	/**
	 * True if this pass is BSR
	 * @return
	 */
	public boolean isBSR(){
		return false;
	}
	/**
	 * True if this pass is from a 70m antenna
	 * @return
	 */
	public boolean is70m(){
		for (int i=0;i<DSN70m.length;i++){
			if (this.getGroundStation().equals(DSN70m[i])) return true;
		}
		return false;
	}
}
