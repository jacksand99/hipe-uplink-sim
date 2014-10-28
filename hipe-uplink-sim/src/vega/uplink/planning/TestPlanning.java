package vega.uplink.planning;

import java.io.IOException;

public class TestPlanning {

	public static void main(String[] args) {
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");


		// TODO Auto-generated method stub
		System.out.println(ObservationUtil.getOffsetMilliSeconds("+00:54:54"));
		System.out.println(ObservationUtil.getOffset(ObservationUtil.getOffsetMilliSeconds("+00:54:54")));
		
		try {
			Observation obs = ObservationUtil.readObservationFromFile("/users/jarenas 1/Downloads/obs1.xml");
			System.out.println(obs.toXml());
			System.out.println(ObservationUtil.OBStoITL(obs));
			System.out.println(ObservationUtil.OBStoEVF(obs));

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
