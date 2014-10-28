package vega.uplink.commanding;

import herschel.ia.dataset.Column;
import herschel.ia.dataset.Dataset;
import herschel.ia.dataset.DateParameter;
import herschel.ia.dataset.MetaData;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.StringParameter;
import herschel.ia.dataset.TableDataset;
import herschel.ia.numeric.String1d;
import herschel.share.fltdyn.time.FineTime;
import herschel.share.io.FileUtil;


import herschel.share.io.archive.ArchiveReader;
import herschel.share.io.archive.FileArchive;
import herschel.share.io.archive.ZipReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;















//import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.EvtmEvent;

public class PorUtils {
	private static final Logger LOG = Logger.getLogger(PorUtils.class.getName());
	public static void writePORGtofile(String file,SuperPor por){
		File porg = new File(file);
		File tdir = null;
		try {
            tdir = FileUtil.createTempDir(porg.getName()+"_temp_porg", "zip");
            Por[] pors = por.getPors();
            for (int i=0;i<pors.length;i++){
            	writePORtofile(tdir.getAbsolutePath()+"/"+pors[i].getName(),pors[i]);
            }
            zipIt(file,tdir.getAbsolutePath());
            LOG.info("Deleting temp zip of " + file + " in " + tdir);
            FileUtil.delete(tdir);

		}catch (Exception e){
			LOG.throwing("PorUtils", "writePORGtofile", e);
			e.printStackTrace();
		}
		finally {
        if (tdir != null) {
            LOG.info("Deleting temp unzip of " + file + " in " + tdir);
            FileUtil.delete(tdir);
        }
    }
	}
	public static SuperPor readPORGfromFile(String fileName) throws IOException{
		File file=new File(fileName);
        File tdir = null;
        boolean error = true;
        SuperPor result=new SuperPor();
        result.setName(file.getName());
        result.setPath(file.getParent());
        try {
            try {
                tdir = FileUtil.createTempDir(file.getName()+"_temp_porg", "unzip");
                //java.util.zip.ZipFile zipfile=new java.util.zip.ZipFile(file);
                //zipfile.
                //FileArchive fa = new FileArchive(file);
                ZipReader ar= new ZipReader(file);
                //ArchiveReader ar = fa.createReader();
                ar.extract(tdir);
                for (String name : tdir.list()) {
                    if (!name.startsWith("."))  {
                        error = false;
                        String porFile = new File(tdir, name).getAbsolutePath();
                        //por.setName(name);
                        //String porName = name.toLowerCase();
                        //porName = porName.replaceAll("[.]ROS$", "");
                        //porName = porName.replaceAll("[.]xml$", "");
                        Por por = readPORfromFile(porFile);
                        por.setName(name);
                        
                        result.addPor(por);
                    }
                }
                LOG.info("Deleting temp unzip of " + file + " in " + tdir);
                FileUtil.delete(tdir);
                result.setType("PORG");
                
                return result;
                //throw new IOException("No PORG file found in " + file);
            } catch (ZipException e) {
                throw new IOException("Could not unzip " + file + " in " + tdir, e);
            } catch (IOException e) {
                throw new IOException("Problems with temp unzipping " + file, e);
            }
        } finally {
            if (error && tdir != null) {
                LOG.info("Deleting temp unzip of " + file + " in " + tdir);
                FileUtil.delete(tdir);
            }
        }
	}
	public static Por readPORfromFile(String file){

		Por result = new Por();
		try {
			 
			File fXmlFile = new File(file);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListHeader = doc.getElementsByTagName("header");
			Node nHeader =nListHeader.item(0);
			Element el = (Element) nHeader;
			String pGenTime=el.getElementsByTagName("genTime").item(0).getTextContent();
			String pStartTime=el.getElementsByTagName("startTime").item(0).getTextContent();
			String pEndTime=el.getElementsByTagName("stopTime").item(0).getTextContent();
		 
			NodeList nListSequences = doc.getElementsByTagName("sequence");
		 
			Sequence[] seq=readSequences(nListSequences);
			result.setSequences(seq);
			result.setGenerationTime(pGenTime);
			String[] vTimes={pStartTime,pEndTime};
			result.setValidityTimes(vTimes);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			
		    } catch (Exception e) {
		    	e.printStackTrace();
		    }
		
		return result; 
	}
	
	private static Sequence[] readSequences(NodeList nList) throws ParseException, IOException{
		Sequence[] result = new Sequence[nList.getLength()];
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	 
				String sName=eElement.getAttribute("name");
				String sUniqueID=eElement.getElementsByTagName("uniqueID").item(0).getTextContent();
				String sFlag=eElement.getElementsByTagName("insertOrDeleteFlag").item(0).getTextContent();
				String sSource=eElement.getElementsByTagName("source").item(0).getTextContent();
				String sDestination=eElement.getElementsByTagName("destination").item(0).getTextContent();
				String sExecutionTime=eElement.getElementsByTagName("actionTime").item(0).getTextContent();
				Node parList=eElement.getElementsByTagName("parameterList").item(0);
				Element el2=(Element) parList;
				NodeList params=el2.getElementsByTagName("parameter");
				Parameter[] sParameters=readParameters(params);
				Node proList=eElement.getElementsByTagName("profileList").item(0);
				Element el3=(Element) proList;
				NodeList profiles=el3.getElementsByTagName("profile");
				SequenceProfile[] sProfiles=readProfiles(profiles);			
				result[temp]=new Sequence (sName,sUniqueID,sFlag,sSource.charAt(0),sDestination.charAt(0),Sequence.zuluToDate(sExecutionTime),sParameters,sProfiles);
				
			}
		}
		return result;
	}
	
	public static Parameter[] readParameters(NodeList nList) throws IOException{
		Mib MIB = Mib.getMib();
		Parameter[] result = new Parameter[nList.getLength()];

		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
			
			Node nNode = nList.item(temp);

	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
				String name=eElement.getAttribute("name");

				Node val=eElement.getElementsByTagName("value").item(0);
				int nelements=eElement.getElementsByTagName("value").getLength();
				if (nelements==0){
					result[temp]=MIB.getDefaultParameter(name);
				}else{
					
				
					Element el=(Element) val;
					String representation=el.getAttribute("representation");
					String radix=el.getAttribute("radix");
	
					String value=val.getTextContent();
					
					if (radix==""){
						result[temp]=new ParameterString(name,representation,value);
					}
					if (radix.equals(Parameter.RADIX_HEX)){
						try{
							String hexValue = value.replace("0x", "");
							result[temp]=new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt(hexValue, 16)).floatValue());
						}
						catch (java.lang.NumberFormatException e){
							try{
								result[temp]=new ParameterFloat(name,representation,radix,new Integer(Integer.parseInt("0x"+value, 16)).floatValue());
							}
							catch (java.lang.NumberFormatException e1){
								e.printStackTrace();
								e1.printStackTrace();
								result[temp]=new ParameterFloat(name,representation,radix,0);
							}
						}
						
					}
					if (radix.equals(Parameter.RADIX_DECIMAL)) {
						result[temp]=new ParameterFloat(name,representation,radix,new Float(value).floatValue());
					}
				}
				
				

			}
			
		}
		
		return result;
	}
	
	public static SequenceProfile[] readProfiles(NodeList nList){
		SequenceProfile[] result = new SequenceProfile[nList.getLength()];
		 
		for (int temp = 0; temp < nList.getLength(); temp++) {
	 
			Node nNode = nList.item(temp);
	 
	 
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
	 
				Element eElement = (Element) nNode;
	 
				String type=eElement.getAttribute("type");
				String value=eElement.getElementsByTagName("value").item(0).getTextContent();
				String offSet=eElement.getElementsByTagName("timeOffset").item(0).getTextContent();
				
				
				result[temp]=new SequenceProfile(type,offSet,new Double(value).doubleValue());
								

			}
		}

		return result;
	}
	public static void savePor(Por POR){
		writePORtofile(POR.getPath()+"/"+POR.getName(),POR);
	}
	public static void writePORtofile(String file,Por POR){
		try{
			/*PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(POR.toXml());
			writer.close();*/
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			DOMSource source = new DOMSource(POR.getXMLDocument());
			StreamResult result = new StreamResult(new File(file));
	 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
	 
			transformer.transform(source, result);
			LOG.info("Writted POR to file " + file );
	 
			//System.out.println("File saved!");
		}catch (Exception e){
			LOG.severe(e.getMessage());
			LOG.throwing("PorUtils", "writePORtofile", e);
			e.printStackTrace();
		}
		
	}
	
	public static void writeITLtofile(String file,Por POR){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(PORtoITL(POR));
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	public static void writeORCDtoXMLfile(String file,Orcd orcd){
		try{
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(orcd.toXml());
			writer.close();
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	public static String PORtoITL(Por POR){
		String result="";
		java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy");
		dateFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		java.text.SimpleDateFormat dateFormat2 = new java.text.SimpleDateFormat("dd-MMMMMMMMM-yyyy'_'HH:mm:ss");
		dateFormat2.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
		String l01="Version: 1\n";
		String l02="Ref_date: "+dateFormat.format(POR.getValidityDates()[0])+"\n\n\n";
		String l03="Start_time: "+dateFormat2.format(POR.getValidityDates()[0])+"\n";
		String l04="End_time: "+dateFormat2.format(POR.getValidityDates()[1])+"\n\n\n";
		String l05="";
		AbstractSequence[] tempSeq=POR.getSequences();
		Parameter[] tempParam;
		SequenceProfile[] tempPro;
		for (int i=0;i<tempSeq.length;i++){
			l05=l05+dateFormat2.format(tempSeq[i].getExecutionDate())+" "+ tempSeq[i].getInstrument()+"\t*\t"+tempSeq[i].getName()+" (\\"+"\n";
			tempParam = tempSeq[i].getParameters();
			for (int z=0;z<tempParam.length;z++){
				l05=l05+"\t"+tempParam[z].getName()+"="+tempParam[z].getStringValue()+" ["+tempParam[z].getRepresentation()+"] \\ \n";
			}
			tempPro=tempSeq[i].getProfiles();
			String dataRateProfile="\tDATA_RATE_PROFILE = \t\t\t";
			String powerProfile="\tPOWER_PROFILE = \t\t\t";
			boolean dataRatePresent=false;
			boolean powerProfilePresent=false;
			for (int j=0;j<tempPro.length;j++){
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_DR)){
					dataRateProfile=dataRateProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]";
					dataRatePresent=true;
					//l05 =l05+ "\tDATA_RATE_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[bits/sec]\\\n"; 
				}
				if (tempPro[j].getType().equals(SequenceProfile.PROFILE_TYPE_PW)){
					powerProfile=powerProfile+" "+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]";
					powerProfilePresent=true;
					//l05 = l05+"\tPOWER_PROFILE = \t\t\t"+tempPro[j].getOffSetString()+"\t"+new Double(tempPro[j].getValue()).toString()+"\t[Watts]\\\n"; 
				}
				
			}
			if (dataRatePresent) l05 =l05+dataRateProfile+"\\\n";
			if (powerProfilePresent) l05 =l05+powerProfile+"\\\n";

			l05=l05+"\t\t\t\t)\n";

		}
		result=l01+l02+l03+l04+l05;
		return result;
	}
	
	public static TableDataset profileToTabledataset(SequenceProfile profile){
		/*herschel.ia.dataset.TableDataset result=new herschel.ia.dataset.TableDataset();
		Column cType=new Column(new String1d());
		Column cValue=new Column(new String1d());
		Column cOffset=new Column(new String1d());
		result.addColumn(cType);
		result.addColumn(cValue);
		result.addColumn(cOffset);
		String[] array=new String[3];
		array[0]=profile.getType();
		array[1]=new Double(profile.getValue()).toString();
		array[2]=profile.getOffSetString();
		result.addRow(array);
		result.setColumnName(0, "Type");
		result.setColumnName(1, "Value");
		result.setColumnName(2, "OffSet");

		return result;*/
		return profile;
	}
	
	public static TableDataset parameterToTabledataset(Parameter parameter){
		return parameter;
		/*herschel.ia.dataset.TableDataset result=new herschel.ia.dataset.TableDataset();
		Column cName=new Column(new String1d());
		Column cRepresentation=new Column(new String1d());
		Column cRadix=new Column(new String1d());
		Column cValue=new Column(new String1d());

		result.addColumn(cName);
		result.addColumn(cRepresentation);
		result.addColumn(cRadix);
		result.addColumn(cValue);

		String[] array=new String[4];
		array[0]=parameter.getName();
		array[1]=parameter.getRepresentation();
		array[2]=parameter.getRadix();
		array[3]=parameter.getStringValue();
		result.addRow(array);
		result.setColumnName(0, "Name");
		result.setColumnName(1, "Representation");
		result.setColumnName(2, "Radix");
		result.setColumnName(3, "Value");

		return result;	*/
	}
	
	
	public static Product sequenceToProduct(Sequence seq){
		return seq;
		/*Product result=new Product();
		TableDataset parameters=new TableDataset();
		TableDataset profiles=new TableDataset();
		Parameter[] params=seq.getParameters();
		SequenceProfile[] prof=seq.getProfiles();
		if (params!=null){
			for (int i=0;i<params.length;i++){
				parameters.concatenate(parameterToTabledataset(params[i]));
			}
		}
		if (prof!=null){
			for (int i=0;i<prof.length;i++){
				profiles.concatenate(profileToTabledataset(prof[i]));
			}
		}

		result.set("parameters", parameters);
		result.set("profiles", profiles);
		MetaData meta=new MetaData();
		meta.set("name", new StringParameter(seq.getName()));
		meta.set("uniqueID", new StringParameter(seq.getUniqueID()));
		meta.set("executiontime",new DateParameter(new FineTime(seq.getExecutionDate())));
		meta.set("source", new StringParameter(""+seq.getSource()));
		meta.set("destination", new StringParameter(""+seq.getDestination()));
		meta.set("flag", new StringParameter(seq.getFlag()));
		
		result.setMeta(meta);
		return result;*/
	}
	
	public static herschel.ia.pal.MapContext porToContext(Por POR){
		return POR;
		/*herschel.ia.pal.MapContext result = new herschel.ia.pal.MapContext();
		result.setStartDate(new FineTime(POR.getValidityDates()[0]));
		result.setEndDate(new FineTime(POR.getValidityDates()[1]));
		result.setCreationDate(new FineTime(POR.getGenerationDate()));
		Sequence[] seqs=POR.getOrderedSequences();
		for (int i=0;i<seqs.length;i++){
			result.setProduct(seqs[i].getUniqueID(), sequenceToProduct(seqs[i]));
		}
		return result;*/
	}
	public static Fecs readFecsFromFile(String file) throws Exception{
		Fecs result=new Fecs();
		try {
			 
			File fXmlFile = new File(file);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 
			//System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nListHeader = doc.getElementsByTagName("header");
			NamedNodeMap headerAttributes = nListHeader.item(0).getAttributes();
			result.setSpacecraft(headerAttributes.getNamedItem("spacecraft").getNodeValue());
			result.setIcdVersion(headerAttributes.getNamedItem("icd_version").getNodeValue());
			result.setGenerationTime(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("gen_time").getNodeValue()));
			result.setValidityStart(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_start").getNodeValue()));
			result.setValidityEnd(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_end").getNodeValue()));
			Node nodeEvents = doc.getElementsByTagName("events").item(0);
			NodeList fcsNodeList = nodeEvents.getChildNodes();
			TreeMap<Date,Node> botNodes=new TreeMap<Date,Node>();
			TreeMap<Date,Node> stadNodes=new TreeMap<Date,Node>();
			TreeMap<Date,Node> stodNodes=new TreeMap<Date,Node>();
			TreeMap<Date,Node> eotNodes=new TreeMap<Date,Node>();
			//TreeMap<Date,Node> botbNodes=new TreeMap<Date,Node>();
			//TreeMap<Date,Node> eotbNodes=new TreeMap<Date,Node>();
			TreeMap<Date,Node> boabNodes=new TreeMap<Date,Node>();
			TreeMap<Date,Node> eoabNodes=new TreeMap<Date,Node>();
			
			int size=fcsNodeList.getLength();
			for (int i=0;i<size;i++){
				Node item = fcsNodeList.item(i);
				NamedNodeMap attributes = item.getAttributes();
				/*int s=attributes.getLength();
				System.out.println("**********************"+s);
				for (int j=0;j<s;j++){
					System.out.println(attributes.item(j).getNodeName());
				}*/
				if (attributes!=null){
					Date time=GsPass.zuluToDate(item.getAttributes().getNamedItem("time").getTextContent());
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOT_")){
						botNodes.put(time,item);
						
					}
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STAD")){
						stadNodes.put(time,item);
					}
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STOD")){
						stodNodes.put(time,item);
					}
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOT_")){
						eotNodes.put(time,item);
					}
					/*if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOTB")){
						botbNodes.put(time,item);
					}
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOTB")){
						eotbNodes.put(time,item);
					}*/
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOAB")){
						boabNodes.put(time,item);
					}
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOAB")){
						eoabNodes.put(time,item);
					}
					

				}
			}
			Iterator<Date> it = botNodes.keySet().iterator();
			while (it.hasNext()){
				Date passStart=it.next();
				Date passEnd=eotNodes.ceilingKey(passStart);
				SortedMap<Date, Node> subStadMap = stadNodes.subMap(passStart, passEnd);
				Iterator<Date> it2 = subStadMap.keySet().iterator();
				while(it2.hasNext()){
					Date dumpStart=it2.next();
					Date dumpEnd=stodNodes.ceilingKey(dumpStart);
					String station=botNodes.get(passStart).getAttributes().getNamedItem("ems:station").getTextContent();
					float tmRate=Float.parseFloat(stadNodes.get(dumpStart).getAttributes().getNamedItem("tm_rate").getTextContent());
					GsPass pass=new GsPass(passStart,passEnd,dumpStart,dumpEnd,station,tmRate);
					result.addPass(pass);
				}
			}
			/*Iterator<Date> it2 = botbNodes.keySet().iterator();
			while (it2.hasNext()){
				Date passStart=it2.next();
				Date passEnd=eotbNodes.ceilingKey(passStart);
				String station=botbNodes.get(passStart).getAttributes().getNamedItem("ems:station").getTextContent();
				result.addPass(new GsPassBSR(passStart,passEnd,station));
				
			}*/

			Iterator<Date> it3 = boabNodes.keySet().iterator();
			while (it3.hasNext()){
				Date passStart=it3.next();
				Date passEnd=eoabNodes.ceilingKey(passStart);
				String station=boabNodes.get(passStart).getAttributes().getNamedItem("ems:station").getTextContent();
				result.addPass(new GsPassOAB(passStart,passEnd,station));
				
			}

			
		}catch (Exception e){
			LOG.throwing("PORUtils", "readFecsFromFile", e);
			e.printStackTrace();
			throw(e);
		}
		
		return result;
	}

	
	/*public static Fecs readFecsFromFile(String file){
		Fecs result=new Fecs();
		try {
			 
			File fXmlFile = new File(file);
			result.setName(fXmlFile.getName());
			result.setPath(fXmlFile.getParent());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
		 
			doc.getDocumentElement().normalize();
		 
			NodeList nListHeader = doc.getElementsByTagName("header");
			NamedNodeMap headerAttributes = nListHeader.item(0).getAttributes();
			result.setSpacecraft(headerAttributes.getNamedItem("spacecraft").getNodeValue());
			result.setIcdVersion(headerAttributes.getNamedItem("icd_version").getNodeValue());
			result.setGenerationTime(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("gen_time").getNodeValue()));
			result.setValidityStart(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_start").getNodeValue()));
			result.setValidityEnd(EvtmEvent.zuluToDate(headerAttributes.getNamedItem("validity_end").getNodeValue()));
			Node nodeEvents = doc.getElementsByTagName("events").item(0);
			NodeList fcsNodeList = nodeEvents.getChildNodes();
			Vector<Node> botNodes=new Vector<Node>();
			Vector<Node> stadNodes=new Vector<Node>();
			Vector<Node> stodNodes=new Vector<Node>();
			Vector<Node> eotNodes=new Vector<Node>();
			int size=fcsNodeList.getLength();
			for (int i=0;i<size;i++){
				Node item = fcsNodeList.item(i);
				NamedNodeMap attributes = item.getAttributes();
				if (attributes!=null){
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("BOT_")) botNodes.add(item);
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STAD")) stadNodes.add(item);
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("STOD")) stodNodes.add(item);
					if (item.getAttributes().getNamedItem("id").getNodeValue().equals("EOT_")) eotNodes.add(item);
				}
			}
			
			size=botNodes.size();
			for (int i=0;i<size;i++){
				Node botItem = botNodes.get(i);
				Node stadItem = stadNodes.get(i);
				Node stodItem = stodNodes.get(i);
				Node eotItem = eotNodes.get(i);
				Date passStart=GsPass.zuluToDate(botItem.getAttributes().getNamedItem("time").getTextContent());
				Date passEnd=GsPass.zuluToDate(eotItem.getAttributes().getNamedItem("time").getTextContent());
				Date dumpStart=GsPass.zuluToDate(stadItem.getAttributes().getNamedItem("time").getTextContent());
				Date dumpEnd=GsPass.zuluToDate(stodItem.getAttributes().getNamedItem("time").getTextContent());
				String station=botItem.getAttributes().getNamedItem("ems:station").getTextContent();
				float tmRate=Float.parseFloat(stadItem.getAttributes().getNamedItem("tm_rate").getTextContent());
				GsPass pass=new GsPass(passStart,passEnd,dumpStart,dumpEnd,station,tmRate);
				result.addPass(pass);
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		
		return result;
	}*/
	
 
    /**
     * Format the file path for zip
     * @param file file path
     * @return Formatted file path
     */
    /*private static String generateZipEntry(String sourceFolder,String file){
    	//System.out.println(sourceFolder);
    	//System.out.println(file);
    	if (!sourceFolder.equals(file)) return file.substring(sourceFolder.length()+1, file.length());
    	else return file;
    }*/
    public static void zipIt(String zipFile,String sourceFolder){
    	 
        byte[] buffer = new byte[1024];
    
        try{
    
       	FileOutputStream fos = new FileOutputStream(zipFile);
       	ZipOutputStream zos = new ZipOutputStream(fos);
    
       	LOG.info("Output to Zip : " + zipFile);
       	//List<String> fileList = generateFileList(new File(sourceFolder));
       	String[] fileList=new File(sourceFolder).list();
       	for(String file : fileList){
    
       		LOG.info("File Added : " + file);
       		ZipEntry ze= new ZipEntry(file);
           	zos.putNextEntry(ze);
    
           	FileInputStream in = 
                          new FileInputStream(sourceFolder + File.separator + file);
    
           	int len;
           	while ((len = in.read(buffer)) > 0) {
           		zos.write(buffer, 0, len);
           	}
    
           	in.close();
       	}
    
       	zos.closeEntry();
       	//remember close it
       	zos.close();
    
       	LOG.info("Zip file Done");
       }catch(IOException ex){
    	   LOG.throwing("PorUtils", "zipIt", ex);
          ex.printStackTrace();   
       }
      }
	
}
