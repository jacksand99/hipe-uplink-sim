package vega.uplink.commanding.itl;

import vega.uplink.Properties;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Simulation;

public class TestItl {
	
    public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty(Properties.MIB_LOCATION, "Z:\\MAPPS\\MIB");
		herschel.share.util.Configuration.setProperty(Properties.DEFAULT_EVT_DIRECTORY, "Z:\\MAPPS\\RMOC\\");
		herschel.share.util.Configuration.setProperty(Properties.PWPL_FILE, "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		herschel.share.util.Configuration.setProperty(Properties.ORCD_FILE, "Z:\\MAPPS\\MIB\\orcd.csv");
		

    	//ItlParser.parseItl("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\STP007\\ITLS_GD_M005_S007_01_A_RSM0PIM0.itl", null);
    	try{
    		Por por=ItlParser.parseItl("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\TLIS_PL_M005______01_A_OPS0001A.itl", "C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\TLIS_PL_M005______01_A_OPS0001A.evf","Z:\\MAPPS\\RMOC\\",5);
    		Simulation sim=new Simulation(por);
    		sim.run();
    		Por por2=por.getPORforInstrument("OSIRIS");
    		PorUtils.porToContext(por);
    		PorUtils.writePORtofile("z:\\tespor.xml", por);
    		PorUtils.writePORtofile("z:\\tespor-osiris.xml", por2);

    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
}
