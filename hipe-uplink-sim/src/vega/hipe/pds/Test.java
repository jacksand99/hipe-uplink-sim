package vega.hipe.pds;

import java.io.IOException;

import javax.swing.JFrame;
import vega.hipe.pds.gui.GraphicImagePanel;

public class Test {
	public static void main (String[] args){
		try {
	    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
	    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
	    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
	    	herschel.share.util.Configuration.setProperty("vega.instrument.names","{ALICE,CONSERT,COSIMA,GIADA,MIDAS,MIRO,ROSINA,RPC,RSI,OSIRIS,VIRTIS,SREM,LANDER,PTR,ANTENNA}");
	    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ALICE","AL");
	    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.OSIRIS","SR");
	    	/*PDSImage si3 = PDSImage.readPdsFile("/Users/jarenas 1/Rosetta/NAC_WAC/W20100710T195953275ID30F12.IMG");
			//PDSImage si = PDSImage.readPdsFile("/Users/jarenas 1/Rosetta/NAC_WAC/ROS_CAM1_20141121T230002-b.IMG");
			PDSImage si2 = PDSImage.readLBLFile("/Users/jarenas 1/Rosetta/NAC_WAC/ROS_CAM1_20141121T230002.LBL");
			
			PDSImage si4 = PDSImage.readLBLFile("/Users/jarenas 1/Rosetta/NAC_WAC/0051ML0002320190102197E02_XXXX.LBL");*/
			//PDSImage P01_001495_1717_XN_08S053W = PDSImage.readImgFile("/Users/jarenas 1/Rosetta/NAC_WAC/P01_001495_1717_XN_08S053W.IMG");
	    	//PDSImage si4 = PDSImage.readImgFile("/Users/jarenas 1/Rosetta/NAC_WAC/1p129954668esf0352p2564l3c1.IMG");
	    	//PDSImage MDIS_MD3_128PPD_H04NW0 = PDSImage.readLBLFile("/Users/jarenas 1/Rosetta/NAC_WAC/MDIS_MD3_128PPD_H04NW0.LBL");
	    	//PDSImage lbl0487MR0019320020303007C00_DRXX = PDSImage.readLBLFile("/Users/jarenas 1/Rosetta/NAC_WAC/0487MR0019320020303007C00_DRXX.LBL");
	    	PDSImage N1364287855_2_1 = PDSImage.readLBLFile("/Users/jarenas 1/Rosetta/NAC_WAC/IMG_DISPLY_0002_000223_5790.LBL");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
