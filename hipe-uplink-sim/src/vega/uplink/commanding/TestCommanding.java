package vega.uplink.commanding;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JFrame;

import org.jfree.ui.RefineryUtilities;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.EvtmEvent;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PtrUtils;
import herschel.ia.dataset.Product;
import vega.uplink.track.Fecs;
import vega.uplink.track.FecsUtils;

public class TestCommanding {
    public static void main(String[] args) {
    	herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/");
    	herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas/Downloads/");
    	herschel.share.util.Configuration.setProperty("vega.mib.location", "/Users/jarenas/esa/solar-orbiter/MIB");
    	herschel.share.util.Configuration.setProperty("vega.instrument.names","{SoloHI,EPD,MAG,SPICE,SWA,STIX,EUI,Metis,PHI,RPW}");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.SoloHI","IH");
    	herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.EPD","ID");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.MAG","IM");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.SPICE","IC");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.SWA","IA");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.STIX","IX");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.EUI","IU");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.Metis","IT");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.PHI","IP");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.RPW","IW");
        herschel.share.util.Configuration.setProperty("vega.instrument.acronyms.PTR","PTR");
        herschel.share.util.Configuration.setProperty("vega.file.type.POR","regex:^POR_[a-zA-Z0-9_\\-]*.SOL");
        herschel.share.util.Configuration.setProperty("vega.default.FECS.file","/Users/jarenas/esa/solar-orbiter/planning/EFECS_M06_V02.xml");

    	
    	System.out.println(Properties.getProperty("vega.file.type.POR"));

    	try {
    		Mib mib=Mib.getMib();
    		SuperPor porg_original = PorUtils.readPORGfromFile("/Users/jarenas/Downloads/PORG_SSGS_S200_E_00001.ZIP");
    		PorUtils.writePORGtofile("/Users/jarenas/Downloads/PORG_example.ZIP", porg_original);
    		
    		Por por1 = PorUtils.readPORfromFile("/Users/jarenas/Downloads/PORG_SSGS_S345_E_00001-original/POR__SSGS_S_S345F15_SM_I00V1_2031345f.SOL");
    		System.out.println(por1.toXml());
    		System.out.println( por1.getSequences()[0].getParameters()[0].toXML(0));
    		//PorUtils.writePORtofile("/Users/jarenas/example.SOL", por1);
    		Parameter[] dp = mib.getDefaultParameters("AMRF011A");
    		for (int i=0;i<dp.length;i++){
    		    //dp[i].setDescription("Test description");
    		    System.out.println(dp[i].getDescription());
    		    System.out.println(dp[i].toXML(0));
    		}
    		//Por POR_DM_016_01_SR_P1_01242 = PorUtils.readPORfromFile("/Users/jarenas/Downloads/PORG_SSGS_S176_D_00001/POR__SEPD_S_S176F01_ID_I01V1_21011761.SOL");
    		//SuperPor por2 = PorUtils.readPORGfromFile("/Users/jarenas/Downloads/PORG_SSGS_S175_D_00001.ZIP");
    		//MocPower mocPower = MocPower.readFromFecs("/Users/jarenas/esa/solar-orbiter/planning/EFECS_M05_V02.xml");
    		//MocPower mocPower = MocPower.readFromDefaultFecs();
    		//System.out.println(mocPower.getRowCount());
    		//Date d3 = DateUtil.parse("2022-72T02:13:00.000Z");
    		//System.out.println(mocPower.getPowerAt(d3));
    		//SimulationContext sc = new SimulationContext(new Date(),new Date());
    		
    		//System.out.println(por2.toXml());
    		//AbstractSequence seq1 = (AbstractSequence) POR_DM_016_01_SR_P1_01242.getProduct("P012420007");
    		//AbstractSequence seq2 = (AbstractSequence) POR_DM_016_01_SR_P1_01242.getProduct("P012420001");
    		//System.out.println(AllowOverlapChecker.allowOverlap(seq1, seq2));
    		//Fecs FECS_XXXXX = PorUtils.readFecsFromFile("/Users/jarenas/ptr-tool/tests/EFECS_M05_V02.xml");
    		//Orcd orcd = Orcd.readORCDXmlfile("/Users/jarenas/esa/solar-orbiter/ORCD.xml");
    		//Fecs fecs = FecsUtils.readFecsFromFile("/Users/jarenas/esa/solar-orbiter/planning/Events_S109_V1_00036.SOL");
    		//System.out.println(fecs.toString());
    		//Evtm evtm = PtrUtils.readEvtmFromFile("/Users/jarenas/esa/solar-orbiter/planning/PTEL_L005_V1_00166.SOL");
            //EvtmEvent[] events = evtm.getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
            //EvtmEvent[] events = evtm.getAllEvents();
            /*
             * for event in events:
newmodes=SimulationContext.getInstance().orcd.getModesAsHistory("EVTM_"+event.getId(),event.getTime().getTime())
SimulationContext.getInstance().historyModes.putAll(newmodes,"EVTM_"+event.getId(),event.getTime().getTime())

             */
            /*for (int i=0;i<events.length;i++){
                EvtmEvent event = events[i];
                HashMap<Long, String> newmodes = Orcd.getOrcd().getModesAsHistory("EVTM_"+event.getId(),event.getTime().getTime());
                
                //System.out.println(newmodes,"EVTM_"+event.getId(),event.getTime().getTime());
            }*/
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	/*try {
			Mib actMIB=Mib.getMib();
			Parameter[] params = actMIB.getDefaultParameters("ACSF001A");
			for (int i=0;i<params.length;i++){
				System.out.println(params[i].toXML(1,0));
			}
		} catch (IOException e1) {
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	
    	/*Orcd testOrcd=Orcd.readORCDfile("/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
    	System.out.println(testOrcd.toXml());
    	PorUtils.writeORCDtoXMLfile("/Users/jarenas 1/Downloads/MAPPS/orcd.xml", testOrcd);*/
    	//testOrcd.writeToFile("z:\\testorcd.csv");
    	//Orcd testOrcd2=Orcd.readORCDfile("z:\\testorcd.csv");
    	//System.out.println("Orcd is equal:"+testOrcd2.equals(testOrcd));
    	/*SuperPor sp;
		try {
			sp = PorUtils.readPORGfromFile("/Users/jarenas 1/OPS/ROS_SGS/PLANNING/LTP001/LTP001A/MTP004A/PORM/PORG_DM_004_01____A1_00006.ROS");
			System.out.println(PorChecker.checkPor(sp));
			//PorUtils.writePORGtofile("/Users/jarenas 1/Downloads/testporg.zip", sp);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	//SuperPor sp=new SuperPor();
    	//sp.addPor(por);
    	//System.out.println(sp.getOrderedSequences().length);
    	
    	/*Mib mib;
		try {
			mib = Mib.getMibFromJar();
			System.out.println(mib.csp_table.getColumnName(1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	//System.out.println(mib.csp_table.getColumnName(1));
		/*herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		herschel.share.util.Configuration.setProperty("rosetta.instrument.names", "{ALICE,CONSERT,COSIMA,GIADA,MIDAS,MIRO,ROSINA,RPC,RSI,OSIRIS,VIRTIS,SREM,LANDER}");*/
		/*List<String> list = Properties.getList(Properties.INSTRUMENT_NAMES_PROPERTIES);
		Iterator<String> it = list.iterator();
		while (it.hasNext()){
			System.out.println(it.next());
		}*/
		//Fecs fecs = PorUtils.readFecsFromFile("Z:\\MAPPS\\FECS_DL_001_02_______00018.ROS");
		/*TreeSet passes = fecs.getPasses();
		Iterator it = passes.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}*/
		//System.out.println(fecs);
		
		/*int[] test={1,2,3,3,6,5};
		Arrays.sort(test);
		for (int i=0;i<test.length;i++){
			System.out.println(test[i]);
		}*/

		/*ParameterFloat param1 = new ParameterFloat("VAL01341",Parameter.REPRESENTATION_RAW,Parameter.RADIX_DECIMAL,65535);
        ParameterString param2 = new ParameterString("VAL01340",Parameter.REPRESENTATION_ENGINEERING,"PROM");*/
        //System.out.println(param1.toXML(1,5));
        //System.out.println(param2.toXML(2));
        
        /*SequenceProfile profile = new SequenceProfile(SequenceProfile.PROFILE_TYPE_DR,"00:00:00",1.104);
        //System.out.println(profile.toXml());
        try{
        	Sequence command=new Sequence ("AALS102A","P000010002","2014-083T06:15:00Z");
        	Parameter[] paramArray=new Parameter[2];
        	paramArray[0]=param1;
        	paramArray[1]=param2;
        	SequenceProfile[] proArray=new SequenceProfile[1];
        	proArray[0]=profile;
        	Sequence command2=new Sequence ("AALS102A","P000010002",Sequence.INSERT_FLAG,'P','S',Sequence.zuluToDate("2014-084T06:15:00Z"),paramArray,proArray);
        	command.setExecutionTime("2014-083T06:15:00Z");
        	command.addParameter(param1);
        	command.addParameter(param2);
        	command.addProfile(profile);
        	Por POR = new Por();
        	POR.addSequence(command);
        	POR.addSequence(command2);
        	AbstractSequence[] hh=POR.getOrderedSequences();
        	SequenceProfile[] pro = hh[0].getProfiles();
        	System.out.println(pro.length);
        	for (int i=0;i<pro.length;i++){
        		System.out.println(pro[i].getType());
        		System.out.println(pro[i].getOffSetString());
        		System.out.println(pro[i].getOffSetSeconds());

        		System.out.println(pro[i].getValue());
        		
        	}
        	
        } catch (Exception e){
        	e.printStackTrace();
        }*/
        
        /*Por exPor=PorUtils.readPORfromFile("C:\\ROS_SGS\\PLANNING\\LTP001\\LTP001A\\MTP004A\\PORM\\POR__DM_004_01_VR_A1_00010.ROS");
        //Por exPor2=PorUtils.readPORfromFile("Z:\\MAPPS\\POR\\POR__DV_034_03_MD_A1_00001.ROS");
        SuperPor sp=new SuperPor();
        sp.addPor(exPor);
        //sp.addPor(exPor2);
        System.out.println(exPor.getSequences().length);
        //System.out.println(exPor2.getSequences().length);
        System.out.println(sp.getSequences().length);
        System.out.println(sp.getOrderedSequences().length);
        //System.out.println(exPor.getOrderedSequences().length);

        //System.out.println(sp.toXml());
        //Sequence[] sequences=exPor.getOrderedSequences();       
        //System.out.println(sequences);
        //System.out.println(PorUtils.PORtoITL(exPor));
        //PorUtils.writePORtofile("Z:\\MAPPS\\POR\\testpor.ROS",exPor);
        //PorUtils.PORtoITL(exPor);
        //System.out.println(exPor.toXml());
        Orcd orcd=Orcd.readORCDfile("Z:\\MAPPS\\MIB\\orcd.csv");
        System.out.println("Size:");
        System.out.println(orcd.getModes("AGDF060A").size());
        //System.out.println(orcd.getMode("AMDF041A"));
        System.out.println("****");
        System.out.println(orcd.checkTransion("GD_PW_Off","GD_PW_Safe"));
        //System.out.println(orcd.getPower("AMDF040A"));*/
        /*HistoryModes hd=new HistoryModes();
        String mode="";
        long dt=0;
        		
        try{
        	mode="MD_PW_Exposure";
        	dt=MocPower.zuluToDate("14-074T01:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
	        
        	mode="PTR_SLEW";
        	dt=MocPower.zuluToDate("14-074T01:30:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        mode="AL_PW_On";
        	dt=MocPower.zuluToDate("14-074T03:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
        	mode="AL_PW_Operational";
        	dt=MocPower.zuluToDate("14-074T05:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
	        mode="AL_PW_Off";
        	dt=MocPower.zuluToDate("14-074T05:30:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
	        mode="AL_PW_On";
        	dt=MocPower.zuluToDate("14-074T06:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
        	mode="CS_PW_Science";
        	dt=MocPower.zuluToDate("14-074T07:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
        	mode="GD_PW_On";
        	dt=MocPower.zuluToDate("14-074T10:00:00Z").getTime();
	        ModelState.getModelState().setState(mode);
	        hd.add(dt,mode,"cmd",dt);
	        hd.addStates(dt,ModelState.getModelState().clone());
	        System.out.println(dt);
	        System.out.println(hd.getStates(dt));
	        
	        /*hd.addStates(MocPower.zuluToDate("14-074T01:00:00Z").getTime(), ModelState.getModelState().clone());
	        ModelState.getModelState().setState("AL_PW_On");
	        hd.addStates(MocPower.zuluToDate("14-074T03:00:00Z").getTime(), ModelState.getModelState().clone());
	        ModelState.getModelState().setState("AL_PW_Operational");
	        hd.addStates(MocPower.zuluToDate("14-074T05:00:00Z").getTime(), ModelState.getModelState().clone());
	        ModelState.getModelState().setState("CS_PW_On");
	        hd.addStates(MocPower.zuluToDate("14-074T07:00:00Z").getTime(), ModelState.getModelState().clone());
	        ModelState.getModelState().setState("CS_PW_Science");
	        hd.addStates(MocPower.zuluToDate("14-074T09:00:00Z").getTime(), ModelState.getModelState().clone());
	        ModelState.getModelState().setState("GD_PW_On");
	        hd.addStates(MocPower.zuluToDate("14-074T12:00:00Z").getTime(), ModelState.getModelState().clone());
	    	rosetta.uplink.commanding.gui.HistoryModesPlot plot= new rosetta.uplink.commanding.gui.HistoryModesPlot("Title",hd);
			plot.setPreferredSize(new java.awt.Dimension(500, 270));
	    	JFrame frame = new JFrame("Title");
	    	frame.setContentPane(plot);
			//plot.setPreferredSize(new java.awt.Dimension(500, 270));
	    	frame.pack();
	    	RefineryUtilities.centerFrameOnScreen(frame);
	    	frame.setVisible(true);
        }catch (Exception e){
        	e.printStackTrace();
        	
        }*/
        
        /*System.out.println(ModelState.getModelState().getState("MD_PW_"));
        System.out.println(ModelState.getModelState().getStateForMode("MD_PW_Exposure"));
        System.out.println(ModelState.getModelState().toString());
        System.out.println(orcd.getTotalPowerForModes(ModelState.getModelState().getAllStates()));
        //HistoryModes hd=new HistoryModes();
        //ModelState other=ModelState.getModelState().clone();
        //hd.addStates(1000001, other);
        //System.out.println(orcd.getOffsetSeconds("ARPS808C"));
        MocPower moc=MocPower.ReadFromFile("C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
        //ModelState.getModelState().setStatePower("MD_PW_Off", 10);
        System.out.println("Total power");        
        System.out.println(ModelState.getModelState().getTotalPower());
        try{
        	System.out.println(MocPower.zuluToDate("14-074T01:00:00Z"));
        	System.out.println(moc.getPowerAt(MocPower.zuluToDate("14-074T01:00:00Z")));
        }catch(Exception e){
        	e.printStackTrace();
        }*/
        

    }
}
