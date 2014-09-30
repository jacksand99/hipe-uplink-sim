package vega.uplink.pointing;

import java.io.ByteArrayInputStream;
import java.io.File;













import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import herschel.ia.pal.MapContext;

/*import org.apache.commons.httpclient.HttpClient;
import org.esa.rsgs.fdsw.net.*;*/

import vega.uplink.Properties;
import vega.uplink.pointing.PtrParameters.Offset.OffsetAngles;
import vega.uplink.pointing.PtrParameters.Offset.OffsetCustom;
import vega.uplink.pointing.PtrParameters.Offset.OffsetRaster;
import vega.uplink.pointing.PtrParameters.Offset.OffsetScan;


public class TestPointing {

	public static void main(String[] args) {
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		herschel.share.util.Configuration.setProperty(Properties.DEFAULT_EVT_DIRECTORY, "Z:\\MAPPS\\RMOC\\");
		herschel.share.util.Configuration.setProperty(Properties.PWPL_FILE, "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		herschel.share.util.Configuration.setProperty(Properties.ORCD_FILE, "Z:\\MAPPS\\MIB\\orcd.csv");
		herschel.share.util.Configuration.setProperty(Properties.DEFAULT_PLANNING_DIRECTORY, "C:\\ROS_SGS\\PLANNING\\");
		//Pdfm pdfm=PtrUtils.readPdfmfromFile("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PTR\\PDFM_DM_004_01____A__00005.ROS");
		//Ptr ptr=PtrUtils.readPTRfromFile("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PTR\\PTRM_DM_004_01____A__00005.ROS");
		//Ptr ptr=PtrUtils.readPTRfromFile("Z:\\PTRM_DM_006_01____A__00008.ROS");
		//Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_DM_007_01____A__00013.ROS");
		//Ptr ptr=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTRM_DM_007_01____A__00013.ROS");
		//try{
		try {
			Ptr ptr = PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP005A/PTR/PTRM_DM_005_01____A__00007.ROS");
			//PtrSegment seg = ptr.getSegments()[0];
			PointingBlocksSlice miroSlice = ptr.getAllBlocksOfInstrument("MIRO");
			System.out.println(miroSlice.toXml(0));
			/*PointingBlock[] miroObs = seg.getAllBlocksOfInstrument("MIRO");
			for (int i=0;i<miroObs.length;i++){
				System.out.println(miroObs[i].toXml(0));
			}*
			/*PointingBlocksSlice slice = ptr.getSlice(1);

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			String tempText="<slice>\n"+slice.toXml(0)+"</slice>\n";
			InputStream stream = new ByteArrayInputStream(tempText.getBytes(StandardCharsets.UTF_8));
			Document doc;

			doc = dBuilder.parse(stream);
			doc.getDocumentElement().normalize();
			//Node node = (Node) doc;
			//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
			PointingBlocksSlice tempSlice = PtrUtils.readBlocksfromDoc(doc);
			//System.out.println(tempSlice.toXml(0));
			slice.regenerate(tempSlice);
			System.out.println(slice.toXml(0));*/
			
		}catch (Exception e){
			e.printStackTrace();
		}

		//PointingBlocksSlice slice2=PtrUtils.readBlocksfromDoc(doc);
		/*Ptr ptr1=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/testing/comparePtr/PTRM_001.ROS");
		Ptr ptr2=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/testing/comparePtr/PTRM_001_move.ROS");
		Ptr target=PtrUtils.readPTRfromFile("/Users/jarenas 1/Rosetta/testing/comparePtr/PTRM_001_v2.ROS");
		PtrUtils.mergePtrs(ptr1, ptr2, target);
		PtrUtils.writePTRtofile("/Users/jarenas 1/Rosetta/testing/comparePtr/PTRM_001_move_result.ROS", target);
		
		System.out.println(PtrChecker.comparePtrs(ptr1, target));
		}catch (Exception e){
			e.printStackTrace();
		}*/
		//System.out.println(PtrChecker.comparePtrs(target,ptr1));

		//Ptr ptsl=PtrUtils.readPTRfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PTSL_DM_007_02____A__00002.ROS");
		//System.out.println(PtrChecker.checkPtr(ptr, ptsl));
/*				String[] segmentNames = ptr.getPtrSegmentNames();
		for (int i=0;i<segmentNames.length;i++){
			System.out.println(segmentNames[i]);
		}
		PtrSegment segment = ptr.getSegment("MTP_007");
		PointingBlock[] blocks = segment.getBlocks();
		for (int i=0;i<blocks.length;i++){
			PointingBlock block = blocks[i];
			if (block.getType().equals("OBS")){
				PointingAttitude attitude = block.getAttitude();
				if (attitude!=null){
					OffsetAngles offset = attitude.getOffsetAngles();
					if (offset!=null){
						if (!offset.isFixed());
							System.out.println(offset.getDurationMilliSecs());
					}
				}
			}
		}*/
		//System.out.println(ptr.toXml());
		//System.out.println(ptr.getSegment("MTP_007").toXml(0));
		//System.out.println(ptr.toXml());
		

		//Pdfm pdfm=PtrUtils.readPdfmfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP002/LTP002A/MTP007A/PTR/PDFM_DM_007_01____A__00014.ROS");
		//System.out.println(pdfm.toXml());
		//System.out.println(PtrChecker.checkPtr(ptr));
		/*PtrSegment seg = ptr.getSegments()[0];
		PointingBlock[] allObs = seg.getAllBlocksOfType("OBS");
		for (int i=0;i<allObs.length;i++){
			PointingAttitude att = allObs[i].getAttitude();
			if (att!=null){
				OffsetAngles offset = att.getOffsetAngles();
				if (offset!=null && offset.isRaster()){
					OffsetRaster raster = (OffsetRaster) offset;
					System.out.println(raster.getEndDate());
				}
				if (offset!=null && offset.isCustom()){
					OffsetCustom raster = (OffsetCustom) offset;
					System.out.println(raster.getEndDate());
				}
				if (offset!=null && offset.isScan()){
					OffsetScan raster = (OffsetScan) offset;
					System.out.println(raster.getEndDate());
				}

			}
		}*/
		/*AttitudeGenerator ag=new AttitudeGeneratorFDImpl(ptr,pdfm);
		PtrSegment seg = ptr.getSegments()[0];
		PointingBlock block = seg.getAllBlocksOfType("OBS")[0];
		System.out.println(ag.getAttitude(block));*/
		
		/*String[] params={"-server","ESAC2","-type","AT","-activity","MTP_A","-pdfm","C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\PTR\\PDFM_DM_005_01____A__00006.ROS","-mtpNum","5","C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP005A\\PTR\\PTRM_DM_005_01____A__00007.ROS"};
		HttpClient client = new HttpClient();
		Ptr2AttRequest req = new Ptr2AttRequest(client, new File("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PTR\\PTRM_DM_004_01____A__00005.ROS"), "MTP_A");
		req.setMtpNum("4");
		File pdfmFile = new File("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PTR\\PDFM_DM_004_01____A__00005.ROS");
		req.setPdfmFile(pdfmFile);
		try {
			req.execute("http://rostasc01.n1data.lan/roscmd-cgi-bin/seqgen.pl");
			Att2AscRequest gen = new Att2AscRequest(client, req.getPtrId(), "MTP_A");
			gen.execute("http://rostasc01.n1data.lan/roscmd-cgi-bin/seqgen.pl");
			File out = new File("z:\\testfile.txt");			
			String fileUrl = gen.getFileUrl();
			System.out.println(fileUrl);
			FDSWHttpClient.downloadFile(client, fileUrl, out);
		} catch (FDSWHttpClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}

}
