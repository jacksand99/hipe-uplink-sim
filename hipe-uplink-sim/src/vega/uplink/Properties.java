package vega.uplink;

import herschel.ia.gui.kernel.prefs.UserPreferences;
import herschel.ia.gui.kernel.prefs.handler.ColorPreferenceHandler;
import herschel.share.property.xml.Validator;
import herschel.share.util.Configuration;

import java.awt.Color;
import java.util.List;
import java.util.StringTokenizer;

public class Properties {
	public static String INSTRUMENT_NAMES_PROPERTIES="vega.instrument.names";
	public static String SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX="vega.instrument.subinstrument.start.";
	public static String SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX="vega.instrument.acronyms.";
	public static String SUBINSTRUMENT_COLOR_PROPERTY_PREFIX="vega.instrument.color.";
	public static String DEFAULT_PLANNING_DIRECTORY="vega.default.planningDirectory";
	public static String DEFAULT_OBSERVATIONS_DIRECTORY="vega.default.observationsDirectory";
	public static String DEFAULT_FECS_FILE="vega.default.FECS.file";
	public static String DEFAULT_EVT_DIRECTORY="vega.default.evtDirectory";
	public static String PWPL_FILE="vega.pwpl.file";
	public static String ORCD_FILE="vega.orcd.file";
	public static String MIB_LOCATION="vega.mib.location";
	public static String ANTENNA_PRIORITY_COMMAND="vega.antenna.priorities.command";
	public static String ANTENNA_PRIORITY_PARAMETER_PREFIX="vega.antenna.priorities.parameter.";
	public static String SSMM_PACKETSTORE_PREFIX="vega.packetstoresize.";
	public static String DEFAULT_INIT_SCRIPT="vega.default.initScript";
	public static String DEFAULT_POST_SCRIPT="vega.default.postScript";
	
	public static List<String> getList(String property) {
			StringTokenizer tokenizer=new StringTokenizer(property,".");
			String path="";
			String key="";
			for (int i=1;i<tokenizer.countTokens();i++){
				path=path+tokenizer.nextToken()+".";
			}
			key=tokenizer.nextToken();
			List<String> value;
			if (UserPreferences.hasKey(path, key)){
				String valueString = UserPreferences.get(path, key);
				//List<String> value;
			//if (valueString!=null){
				value = Validator.getList(valueString);
			}else{
				value=Configuration.getList(property);
				//UserPreferences.
			}
		
		// TODO Auto-generated method stub
		return value;
	}
	public static String getProperty(String property) {
		StringTokenizer tokenizer=new StringTokenizer(property,".");
		String path="";
		String key="";
		for (int i=1;i<tokenizer.countTokens();i++){
			path=path+tokenizer.nextToken()+".";
		}
		key=tokenizer.nextToken();
		String valueString=null;
		if (UserPreferences.hasKey(path, key)){
			valueString = UserPreferences.get(path, key);
		}else{
		//if (valueString==null){
			valueString=Configuration.getProperty(property);
		}
		// TODO Auto-generated method stub
		return valueString;
	}
	
	public static Color getColor(String property){
		StringTokenizer tokenizer=new StringTokenizer(property,".");
		String path="";
		String key="";
		for (int i=1;i<tokenizer.countTokens();i++){
			path=path+tokenizer.nextToken()+".";
		}
		key=tokenizer.nextToken();
		Color result=new Color(0);
		if (UserPreferences.hasKey(path, key)){
			result = new Color(Integer.parseInt(UserPreferences.get(path, key)));
		}else{
			try{
				result = new Color(Configuration.getInteger(property));
				//System.out.println("Color is an integer");
			}catch (herschel.share.util.ConfigurationException ce){
				result= getColorFromList(property);
				//result = new Color(getColorInt(property));
				//System.out.println("Color is a list");

			}
		//if (valueString==null){
			//return result;
		}
		return result;
	}
	
	private static Color getColorFromList(String preference){
		List<String> rgb = Configuration.getList(preference);
		//Color color = new Color(alignmentX, alignmentX, alignmentX);
		//color.
		Color color = new Color(Integer.parseInt(rgb.get(0)),Integer.parseInt(rgb.get(1)),Integer.parseInt(rgb.get(2)));
		return color;
		//return ColorPreferenceHandler.color2int(color);
	}
	



}
