package rosetta.uplink.pointing;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;

import vega.uplink.DateUtil;
import vega.uplink.pointing.exclusion.Period;

public class LanderVisibility extends ExclusionPeriod {
	public static LanderVisibility readFromFile(String evtFile) throws ParseException{
		LanderVisibility result=new LanderVisibility();
		String[] evtlines;
		try {
			File f=new File(evtFile);
			result.setName(f.getName());
			evtlines = readFile(evtFile);
		} catch (IOException e1) {
			ParseException e2=new ParseException(e1.getMessage(),0);
			e2.initCause(e1);
			throw e2;
		}
		evtlines=removeComments(evtlines);
		evtlines=removeSpaces(evtlines);
		Date start=new Date();
		Date end;
		for (int i=0;i<evtlines.length;i++){
			//System.out.println(evtlines[i]);
			String[] parts=evtlines[i].split(" ");
			//System.out.println("****"+parts[0]+"***");
			if (parts[0].equals("AORF")){
				start=DateUtil.parse(parts[3]);
			}
			if (parts[0].equals("LORF")){
				//System.out.println("Detected LORF")
				end=DateUtil.parse(parts[3]);
				result.addNavPeriod(start, end);
			}

		}
		
		return result;
		
	}

	
	public static void main (String[] args){
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
    	herschel.share.util.Configuration.setProperty("vega.default.evtDirectory", "/Users/jarenas 1/Downloads/MAPPS/RMOC/");
    	herschel.share.util.Configuration.setProperty("vega.orcd.file","/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/PWPL_14_001_14_365__OPT_01.ROS");
    	herschel.share.util.Configuration.setProperty("vega.mib.location","/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("var.hcss.cfg.dir", "/Users/jarenas 1/.hcss");

		try {
			LanderVisibility lander = readFromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LANDER/OEF_________01_______XXXXX.evf");
			Period[] periods = lander.getAllPeriods();
			for (int i=0;i<periods.length;i++){
				System.out.println(periods[i]);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
