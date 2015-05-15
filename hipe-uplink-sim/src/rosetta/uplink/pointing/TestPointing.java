package rosetta.uplink.pointing;

import java.io.File;





















import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.pal.MapContext;

/*import org.apache.commons.httpclient.HttpClient;
import org.esa.rsgs.fdsw.net.*;*/

import vega.uplink.Properties;
import vega.uplink.pointing.AttitudeGenerator;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.SolarAspectAngle;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;


public class TestPointing {

	public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
    	herschel.share.util.Configuration.setProperty("vega.default.evtDirectory", "/Users/jarenas 1/Downloads/MAPPS/RMOC/");
    	herschel.share.util.Configuration.setProperty("vega.orcd.file","/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/PWPL_14_001_14_365__OPT_01.ROS");
    	herschel.share.util.Configuration.setProperty("vega.mib.location","/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("var.hcss.cfg.dir", "/Users/jarenas 1/.hcss");
    	
    	//AngularMomentum agm = AngularMomentum.readFromFile("/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361/fddata/1407222816165_agm.txt");
    	//AngularMomentum agm = AngularMomentum.readFromFile("/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361/fddata/1407221834000_agm.txt");
    	//System.out.println(agm);
    	try{
    		RosettaDistance dis = RosettaDistance.getInstance();
    		System.out.println(dis.getDistanceSun(new Date()));
    		RosettaPtrCheckTask task = new RosettaPtrCheckTask();
    		Map<String, Modifier> map = task.getCustomModifiers();
    	Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/PTR/PTRM_DM_005_01____A__00007.ROS");
    	//Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/testing/MTP008val/PTRM_DM_008_01____A__00017.ROS.txt.new");
		Pdfm pdfm=PtrUtils.readPdfmfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/PTR/PDFM_DM_005_01____A__00006.ROS");
		//Ptsl 
		/*try {
			AttitudeGeneratorFDImpl.getAttitudeFile(ptr,pdfm,AttitudeGeneratorFDImpl.getMtpNum(ptr),"/Users/jarenas 1/Rosetta/testing/MTP008val/fdtest.txt","RORL_DL_001_02____A__00002");
		} catch (AttitudeGeneratorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(PtrChecker.checkPtr(ptr));
    	}catch (Exception e){
    		e.printStackTrace();
    	}
		//RosettaPtrChecker.checkPtr(ptr, ptr, pdfm);
		/*System.out.println(PtrChecker.checkPtr(ptr));
		AttitudeGeneratorFDImpl ag;
		try {
			ag = new AttitudeGeneratorFDImpl(ptr,pdfm);

		PtrSegment seg = ptr.getSegments()[0];
		PointingBlock block = seg.getAllBlocksOfType("OBS")[0];
		System.out.println(ag.getQuaternions(block));
		AttitudeConstrainEvent[] events = ag.getAttEvents();
		
		for (int i=0;i<events.length;i++){
			System.out.println(events[i]);
		}
		SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		HgaOutages[] hga=ag.getHgaOutages();
		Evtm evtm = ag.getEvtm();
		System.out.println(evtm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		/*Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_DM_007_01____A__00013.ROS");
		Ptr ptsl=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTSL_DM_007_02____A__00002.ROS");
		Pdfm pdfm=PtrUtils.readPdfmfromFile("/Users/jarenas 1//OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP008A/PTR/PDFM_DM_008_01____A__00015.ROS");
		System.out.println(RosettaPtrChecker.checkPtr(ptr, ptsl,pdfm));*/

		
		
		
	}

}