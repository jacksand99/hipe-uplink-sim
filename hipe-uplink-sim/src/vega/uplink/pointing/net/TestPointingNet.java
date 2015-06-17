package vega.uplink.pointing.net;

import rosetta.uplink.pointing.RosettaAttitudeGenerator;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrUtils;

public class TestPointingNet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{
			herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
			herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
	    	herschel.share.util.Configuration.setProperty("vega.default.evtDirectory", "/Users/jarenas 1/Downloads/MAPPS/RMOC/");
	    	herschel.share.util.Configuration.setProperty("vega.orcd.file","/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
	    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/PWPL_14_001_14_365__OPT_01.ROS");
	    	herschel.share.util.Configuration.setProperty("vega.mib.location","/Users/jarenas 1/Downloads/MAPPS/MIB");
	    	herschel.share.util.Configuration.setProperty("var.hcss.cfg.dir", "/Users/jarenas 1/.hcss");
	    	herschel.share.util.Configuration.setProperty("vega.uplink.pointing.net.serverUrl", "http://www.fd-tasc.info/roscmd-cgi-bin/seqgen.pl");
	    	herschel.share.util.Configuration.setProperty("vega.uplink.pointing.net.mission", "ROS");

			Ptr ptr = PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/stp063/PTRM_PL_M018______01_P_RSMXPIMZ_STP063.ROS");
			Pdfm pdfm = PtrUtils.readPdfmfromFile("/Users/jarenas 1/Rosetta/stp063/PDFM_DM_018_01___P_00000.ROS");
			String mtpNum = AttitudeGeneratorFDImpl.getMtpNum(ptr);
			String trajectory = "Routine";
			ErrorBoxPoint point=new ErrorBoxPoint(0.0f,0.0f,0.0f);
			//RosettaAttitudeGenerator ag = new RosettaAttitudeGenerator(ptr,pdfm,AttitudeGeneratorFDImpl.getMtpNum(ptr),trajectory,point);
			AttitudeGeneratorFDImpl attGen=new AttitudeGeneratorFDImpl(ptr,pdfm,mtpNum,trajectory);
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
