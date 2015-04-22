package vega.uplink.planning.aspen;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationPointingSlice;
import vega.uplink.planning.ObservationUtil;


import org.w3c.dom.Element;

public class AspenObservationSchedule {
	private static final Logger LOG = Logger.getLogger(AspenObservationSchedule.class.getName());
	public static Observation[] readObservations(String file) throws IOException{
		java.util.Vector<Observation> vector =new java.util.Vector<Observation>();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			doc.getDocumentElement().normalize();
			NodeList nList = (doc.getDocumentElement()).getElementsByTagName("ObservationInstance");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node n = nList.item(temp);
				String instrument = n.getAttributes().getNamedItem("instrumentName").getTextContent();
				if (!instrument.equals("AOCS") && !instrument.equals("NAVCAM") && !instrument.equals("HGA") && !instrument.equals("ENG")){
					String name=n.getAttributes().getNamedItem("name").getTextContent();
					String startDateText = ((Element) n).getElementsByTagName("utcStart").item(0).getTextContent();
					String endDateText = ((Element) n).getElementsByTagName("utcEnd").item(0).getTextContent();
					String primeText = ((Element) n).getElementsByTagName("prime").item(0).getTextContent();
					boolean prime=Boolean.parseBoolean(primeText);
					Date startDate = DateUtil.parse(startDateText);
					Date endDate=DateUtil.parse(endDateText);
					Observation obs=new Observation(startDate,endDate);
					obs.setName(instrument.toUpperCase()+"_"+name);
					obs.setInstrument(instrument.toUpperCase());
					vector.add(populateObservation(obs,prime));
					
				}
			}
		} catch (Exception e) {
		    	e.printStackTrace();
		    	IOException io = new IOException(e.getMessage());
		    	io.initCause(e);
		    	throw(io);
		    	
		    }
		Observation[] result=new Observation[vector.size()];
		result=vector.toArray(result);
		return result;
		
	}
	
	public static void main (String[] args){
	   	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ALICE","AL");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.CONSERT","CN");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.COSIMA","CS");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.GIADA","GD");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.MIDAS","MD");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.MIRO","MR");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ROSINA","RN");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.RPC","RP");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.RSI","RS");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.OSIRIS","SR");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.VIRTIS","VR");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.SREM","SE");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.LANDER","ES");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.PTR","PTR");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.ANTENNA","SY");
    	herschel.share.util.Configuration.setProperty("vega.default.observationsDirectory","/Users/jarenas 1/git/hipe-uplink-sim/hipe-uplink-sim/OBS");
    	herschel.share.util.Configuration.setProperty("vega.instrument.names","{ALICE,CONSERT,COSIMA,GIADA,MIDAS,MIRO,ROSINA,RPC,RSI,OSIRIS,VIRTIS,SREM,LANDER,PTR,ANTENNA}");
    	try {
			Observation[] obs = readObservations("/Users/jarenas 1/Downloads/MTP011_LAC/ObservationSchedule.xml");
			for (int i=0;i<obs.length;i++){
				//System.out.println(obs[i].getName()+" "+obs[i].getInstrument());
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private static Observation populateObservation(Observation obs,boolean prime){
		String obsdir=Properties.getProperty("vega.default.observationsDirectory");
		String instrument = obs.getInstrument();
		String name = obs.getName();
		String type = name.replaceFirst(instrument+"_", "").toUpperCase();
		String acro = Properties.getProperty(Properties.SUBINSTRUMENT_ACRONYM_PROPERTY_PREFIX+instrument);
		File f = new File(obsdir+"/OBS_"+acro+"_"+type+".ROS");
		Observation template=null;
		if (f.exists()){
			try {
				template=ObservationUtil.readObservationFromFile(obsdir+"/OBS_"+acro+"_"+type+".ROS");
			} catch (IOException e) {

				e.printStackTrace();
			}
		}else{
			File f2 = new File(obsdir+"/OBS_"+acro+"_0"+type+".ROS");
			if (f2.exists()){
				try {
					template=ObservationUtil.readObservationFromFile(obsdir+"/OBS_"+acro+"_0"+type+".ROS");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				
				try {
					LOG.warning("Observation template for "+acro+" "+type+" not found. Using default");
					template=ObservationUtil.readObservationFromFile(obsdir+"/OBS_"+acro+"_DEFAULT.ROS");
				} catch (IOException e2) {

					e2.printStackTrace();
				}
			}

			
		}
		if (template!=null){
			template.setName(name);
			template.setStartDate(obs.getStartDate());
			template.setEndDate(obs.getEndDate());
			if (!prime) template.setPointing(new ObservationPointingSlice(template));
			return template;
			
		}
		return obs;
	}
	
}
