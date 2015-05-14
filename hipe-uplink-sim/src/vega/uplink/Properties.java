package vega.uplink;

import herschel.ia.gui.kernel.prefs.UserPreferences;
import herschel.ia.gui.kernel.prefs.handler.ColorPreferenceHandler;
import herschel.share.property.xml.Validator;
import herschel.share.util.Configuration;

import java.awt.Color;
import java.util.List;
import java.util.StringTokenizer;

public class Properties {
	/**
	 * vega.instrument.names
	 */
	public static String INSTRUMENT_NAMES_PROPERTIES="vega.instrument.names";
	/**
	 * vega.instrument.subinstrument.start.
	 */
	public static String SUBINSTRUMENT_MODE_START_PROPERTY_PREFIX="vega.instrument.subinstrument.start.";
	/**
	 * vega.instrument.acronyms.
	 */
	public static String SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX="vega.instrument.acronyms.";
	/**
	 * vega.instrument.color.
	 */
	public static String SUBINSTRUMENT_COLOR_PROPERTY_PREFIX="vega.instrument.color.";
	/**
	 * vega.default.planningDirectory
	 */
	public static String DEFAULT_PLANNING_DIRECTORY="vega.default.planningDirectory";
	/**
	 * vega.default.observationsDirectory
	 */
	public static String DEFAULT_OBSERVATIONS_DIRECTORY="vega.default.observationsDirectory";
	/**
	 * vega.default.FECS.file
	 */
	public static String DEFAULT_FECS_FILE="vega.default.FECS.file";
	/**
	 * vega.default.evtDirectory
	 */
	public static String DEFAULT_EVT_DIRECTORY="vega.default.evtDirectory";
	/**
	 * vega.pwpl.file
	 */
	public static String PWPL_FILE="vega.pwpl.file";
	/**
	 * vega.orcd.file
	 */
	public static String ORCD_FILE="vega.orcd.file";
	/**
	 * vega.mib.location
	 */
	public static String MIB_LOCATION="vega.mib.location";
	/**
	 * vega.antenna.priorities.command
	 */
	public static String ANTENNA_PRIORITY_COMMAND="vega.antenna.priorities.command";
	/**
	 * vega.antenna.priorities.parameter.
	 */
	public static String ANTENNA_PRIORITY_PARAMETER_PREFIX="vega.antenna.priorities.parameter.";
	/**
	 * vega.packetstoresize.
	 */
	public static String SSMM_PACKETSTORE_PREFIX="vega.packetstoresize.";
	/**
	 * vega.default.initScript
	 */
	public static String DEFAULT_INIT_SCRIPT="vega.default.initScript";
	/**
	 * vega.default.postScript
	 */
	public static String DEFAULT_POST_SCRIPT="vega.default.postScript";
	/**
	 * vega.file.type.POR
	 */
	public static String POR_FILE_PROPERTY="vega.file.type.POR";
	/**
	 * vega.file.type.PORG
	 */
	public static String PORG_FILE_PROPERTY="vega.file.type.PORG";
	/**
	 * vega.file.type.PTR
	 */
	public static String PTR_FILE_PROPERTY="vega.file.type.PTR";
	/**
	 * vega.file.type.PTSL
	 */
	public static String PTSL_FILE_PROPERTY="vega.file.type.PTSL";
	/**
	 * vega.file.type.PDFM
	 */
	public static String PDFM_FILE_PROPERTY="vega.file.type.PDFM";
	/**
	 * vega.file.type.EVTM
	 */
	public static String EVTM_FILE_PROPERTY="vega.file.type.EVTM";
	/**
	 * 
	 */
	public static String FECS_FILE_PROPERTY="vega.file.type.FECS";
	/**
	 * vega.file.type.FECS
	 */
	public static String OBS_FILE_PROPERTY="vega.file.type.OBS";
	/**
	 * vega.file.type.SCH
	 */
	public static String SCH_FILE_PROPERTY="vega.file.type.SCH";
	/**
	 * vega.file.type.PER
	 */
	public static String PER_FILE_PROPERTY="vega.file.type.PER";
	/**
	 * vega.file.type.PDOR
	 */
	public static String PDOR_FILE_PROPERTY="vega.file.type.PDOR";
	/**
	 * vega.file.type.EVF
	 */
	public static String EVF_FILE_PROPERTY="vega.file.type.EVF";
	/**
	 * vega.file.type.ITL
	 */
	public static String ITL_FILE_PROPERTY="vega.file.type.ITL";
	/**
	 * vega.file.type.PWPL
	 */
	public static String PWPL_FILE_PROPERTY="vega.file.type.PWPL";
	/**
	 * vega.file.type.PWTL
	 */
	public static String PWTL_FILE_PROPERTY="vega.file.type.PWTL";
	
	/**
	 * Get a property as a list. The property value must be defined as {val1,val2,val3}
	 * @param property
	 * @return
	 */
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
				value = Validator.getList(valueString);
			}else{
				value=Configuration.getList(property);
			}
		
		return value;
	}
	public static String getProperty(String property,String defaultvalue){
		try{
			return getProperty(property);
		}catch (Exception e){
			return defaultvalue;
		}
	}
	/**
	 * Get the value of a property
	 * @param property
	 * @return
	 */
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
			valueString=Configuration.getProperty(property);
		}
		return valueString;
	}
	
	/**
	 * Get the value of a property as a color. The value must be a integer like -16776961 having the RGB value
	 * @param property
	 * @return
	 */
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
			}catch (herschel.share.util.ConfigurationException ce){
				result= getColorFromList(property);

			}
			
		}
		return result;
	}
	
	private static Color getColorFromList(String preference){
		List<String> rgb = Configuration.getList(preference);
		Color color = new Color(Integer.parseInt(rgb.get(0)),Integer.parseInt(rgb.get(1)),Integer.parseInt(rgb.get(2)));
		return color;
	}
	



}
