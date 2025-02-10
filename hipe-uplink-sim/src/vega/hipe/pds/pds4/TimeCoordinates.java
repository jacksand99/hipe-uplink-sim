package vega.hipe.pds.pds4;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import vega.hipe.logging.VegaLog;

/**
 * The Time_Coordinates class provides a list of time coordinates.
 * @author jarenas
 *
 */
public class TimeCoordinates {
	private String start_date_time;
	private String stop_date_time;
	private String local_mean_solar_time;
	private String local_true_solar_time;
	public static String START_DATA_TIME="start_date_time";
	public static String STOP_DATA_TIME="stop_date_time";
	public static String LOCAL_MEAN_SOLAR_TIME="local_mean_solar_time";
	public static String LOCAL_TRUE_SOLAR_TIME="local_true_solar_time";
	public static String TIME_COORDINATES="Time_Coordinates";
	
	public TimeCoordinates(){
		start_date_time="";
		stop_date_time="";
		local_mean_solar_time="";
		local_true_solar_time="";
	}
	
	public static TimeCoordinates getFromNode(Node node){
		TimeCoordinates result=new TimeCoordinates();
		//Element poElement = (Element) node;
		try{
			result.setStartDateTime(((Element) node).getElementsByTagName(START_DATA_TIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+START_DATA_TIME);
		}
		try{
			result.setStopDateTime(((Element) node).getElementsByTagName(STOP_DATA_TIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+STOP_DATA_TIME);
		}
		try{
			result.setLocalMeanSolarTime(((Element) node).getElementsByTagName(LOCAL_MEAN_SOLAR_TIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LOCAL_MEAN_SOLAR_TIME);
		}
		try{
			result.setLocalTrueSolarTime(((Element) node).getElementsByTagName(LOCAL_TRUE_SOLAR_TIME).item(0).getTextContent().trim());
		}catch (Exception e){
			VegaLog.info("Could not get "+LOCAL_TRUE_SOLAR_TIME);
		}
			return result;
	}
	
	public String getStartDateTime(){
		return start_date_time;
	}
	
	public String getStopDateTime(){
		return stop_date_time;
	}
	
	public String getLocalMeanSolarTime(){
		return local_mean_solar_time;
	}
	
	public String getLocalTrueSolarTime(){
		return local_true_solar_time;
	}
	
	public void setStartDateTime(String newStartDateTime){
		start_date_time=newStartDateTime;
	}
	
	public void setStopDateTime(String newStopDateTime){
		stop_date_time=newStopDateTime;
	}
	
	public void setLocalMeanSolarTime(String newLocalMeanSolarTime){
		local_mean_solar_time=newLocalMeanSolarTime;
	}
	
	public void setLocalTrueSolarTime(String newLocalTrueSolarTime){
		local_true_solar_time=newLocalTrueSolarTime;
	}
	
	public String toXml(){
		return toXml(0);
	}
	public String toXml(int indent){
		String id="";
		for (int i=0;i<indent;i++){
			id=id+"\t";
		}
		String result="";
		result=result+id+"<Time_Coordinates>\n";
		result=result+id+"\t<start_date_time>"+start_date_time+"</start_date_time>\n";
		result=result+id+"\t<stop_date_time>"+stop_date_time+"</stop_date_time>\n";
		result=result+id+"\t<local_mean_solar_time>"+local_mean_solar_time+"</local_mean_solar_time>\n";
		result=result+id+"\t<local_true_solar_time>"+local_true_solar_time+"</local_true_solar_time>\n";

		result=result+id+"</Time_Coordinates>\n";
		
		return result;
	}
}
