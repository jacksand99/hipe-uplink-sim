package vega.uplink.commanding.itl;

import java.io.File;
import java.util.Date;

import vega.uplink.Properties;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Simulation;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;

public class TestItl {
	
    public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty(Properties.MIB_LOCATION, "Z:\\MAPPS\\MIB");
		herschel.share.util.Configuration.setProperty(Properties.DEFAULT_EVT_DIRECTORY, "Z:\\MAPPS\\RMOC\\");
		herschel.share.util.Configuration.setProperty(Properties.PWPL_FILE, "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		herschel.share.util.Configuration.setProperty(Properties.ORCD_FILE, "Z:\\MAPPS\\MIB\\orcd.csv");
		

    	//ItlParser.parseItl("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\STP007\\ITLS_GD_M005_S007_01_A_RSM0PIM0.itl", null);
		String dir="/Users/jarenas 1/OPS/ROS_SGS/PLANNING/OBS_LIB/ITL_LIB/";
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
*/
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
}
