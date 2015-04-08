package vega.uplink.commanding.itl;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.SortedMap;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Simulation;
import vega.uplink.commanding.itl.gui.ItlTokenMarker;
import vega.uplink.commanding.itl.gui.KeywordMap;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;

public class TestItl {
	
    public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty(Properties.MIB_LOCATION, "/Users/jarenas/OPS/mib");
		herschel.share.util.Configuration.setProperty(Properties.DEFAULT_EVT_DIRECTORY, "/Users/jarenas 1/Downloads/MAPPS/RMOC");
		herschel.share.util.Configuration.setProperty(Properties.PWPL_FILE, "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		herschel.share.util.Configuration.setProperty(Properties.ORCD_FILE, "Z:\\MAPPS\\MIB\\orcd.csv");
		

    	//ItlParser.parseItl("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\STP007\\ITLS_GD_M005_S007_01_A_RSM0PIM0.itl", null);
		/*String dir="/Users/jarenas 1/OPS/ROS_SGS/PLANNING/OBS_LIB/ITL_LIB/";
		String detDir="/Users/jarenas 1/Rosetta/ITL_LIB/";
		//File fDir = new File(dir);
    	try{
    		File fDir = new File(dir);
    		File[] files = fDir.listFiles();
    		for (int i=0;i<files.length;i++){
    			String name=files[i].getName();
    			if (name.endsWith(".itl") && !name.startsWith("ITL") ){
    				System.out.println(name);
    	    		long time=new Date().getTime();
    	    		Observation obs = ItlParser.itlToObs(dir+name, new Date(time), new Date(time+3600000));
    	    		String obsFileName = "OBS_"+name.replace(".itl", "").toUpperCase()+".ROS";
    	    		ObservationUtil.saveObservationToFile(detDir+obsFileName, obs);
    	    		//System.out.println(obs.toXml());

    			}
    		}
    		/*Por por=ItlParser.parseItl("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/TLIS_PL_M007______01_A_OPS0001A.itl", "/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/TLIS_PL_M007______01_A_OPS0001A.evf","/Users/jarenas 1/Downloads/MAPPS/RMOC/",5);
    		Simulation sim=new Simulation(por);
    		sim.runSimulation();
    		Por por2=por.getPORforInstrument("OSIRIS");
    		PorUtils.porToContext(por);
    		PorUtils.writePORtofile("/Users/jarenas 1/Downloads/tespor.xml", por);
    		PorUtils.writePORtofile("/Users/jarenas 1/Downloads/tespor-osiris.xml", por2);

    	}catch (Exception e){
    		e.printStackTrace();
    	}*/
		//Itl itl = new Itl(new Date(),new Date());
		try{
			File f = new File("/Users/jarenas 1/Downloads/MAPPS/RMOC/FECS____________RSGS_XXXXX.evf");
			System.out.println("File exist:"+f.exists());
			System.out.println("Can read :"+f.canRead());
			System.out.println(ItlParser.deltaToMilli("026_15:15:00"));
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/STP055/EVF__AL_M016_S055_01_P_RSUXPIYZ.evf");
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/TLIS_PL_M016______01_P_OPS0001A.evf");
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/STP058/EVF__GD_M016_S058_01_P_RSUXPIYZ.evf");
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/TLIS_PL_M016______01_P_OPS0001A.evf");
			//System.out.println("2015-05-27T11:25:00 "+DateUtil.parse("2015-05-27T11:25:00").getTime());
			//System.out.println("2015-06-02T23:25:00 "+DateUtil.parse("2015-06-02T23:25:00").getTime());
			
			//Date date = evf.getDate(new ItlEvent("GD_Dust_monitoring_________SO",160001),DateUtil.parse("2015-05-05T23:13:00"),DateUtil.parse("2015-06-24T11:37:00"));
			//System.out.println(DateUtil.defaultDateToString(date));
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP015P/STP053/TLIS_PL_M015_S053_01_P_OPS0001A.evf","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP015P/");
			//EventList evf=EventList.parseEvf("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP015P/STP053/EVF__AL_M015_S053_01_P_RSM0PIM0.evf","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP015P/");

			//itl.parseEvf("/Users/jarenas 1/Rosetta/testing/temp/STP051/EVF__AL_M015_S051_01_P_RSM0PIM0.evf");
			/*System.out.println(evf.toEvf());
			System.out.println(evf.getDate(new ItlEvent("AL_HIST_RIDER_10___________SO",160001),DateUtil.parse("2015-05-05T23:25:00"),DateUtil.parse("2015-05-12T23:25:00")));
			System.out.println("****"+DateUtil.defaultDateToString(evf.getDate(new ItlEvent("AL_HIST_RIDER_10___________SO",160001))));
			SortedMap<Date, String> sm = evf.subMap(DateUtil.parse("2015-05-05T23:25:00"),DateUtil.parse("2015-05-12T23:25:00"));
			Iterator<Entry<Date, String>> it = sm.entrySet().iterator();
			while (it.hasNext()){
				Entry<Date, String> en = it.next();
				System.out.println(""+DateUtil.defaultDateToString(en.getKey())+" "+en.getValue());
				//if (en.getValue().startsWith("AL_HIST_RIDER_10")) System.out.println(""+DateUtil.defaultDateToString(en.getKey())+" "+en.getValue());
			}
			System.out.println(evf.getEvent(DateUtil.parse("06-May-2015_07:01:02")));*/
			
			//itl.parseItl("/Users/jarenas 1/Rosetta/testing/temp/STP051/ITLS_AL_M015_S051_01_P_RSM0PIM0.itl");
			//System.out.println(itl.toItl());
			//KeywordMap kw = ItlTokenMarker.getKeywords();
			//kw.
			Por TLIS_PL_M016_01_P_OPS0001A = ItlParser.itlToPor("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/TLIS_PL_M016______01_P_OPS0001A.itl");
			//Por TLIS_PL_M016_01_P_OPS0001A = ItlParser.itlToPor("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/STP058/ITLS_GD_M016_S058_01_P_RSUXPIYZ.itl");
			
			//Por p=ItlParser.itlToPor("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP005/LTP005P/MTP016P/../../../OBS_LIB/ITL_LIB/ITL__NAV__________01_A_OPS0001A.itl");
		}catch (Exception e){
			e.printStackTrace();
		}
		
    }
}
