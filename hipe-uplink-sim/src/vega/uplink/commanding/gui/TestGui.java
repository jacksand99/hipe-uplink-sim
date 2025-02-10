package vega.uplink.commanding.gui;

import java.text.ParseException;
import java.util.List;

import javax.swing.JFrame;

import herschel.share.interpreter.InterpreterFactory;
import herschel.share.interpreter.InterpreterNameSpaceUtil;
import herschel.share.interpreter.InterpreterNameSpaceUtil.And;
import vega.uplink.DateUtil;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.EvtmEvent;
import vega.uplink.pointing.PtrUtils;

import static herschel.share.interpreter.InterpreterNameSpaceUtil.IS_VARIABLE;

public class TestGui {

	public static void main(String[] args) {
		//herschel.share.util.Configuration.
		//herschel.share.util.Configuration.lo
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
		//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		//herschel.share.util.Configuration.setProperty("rosetta.default.evtDirectory", "Z:\\MAPPS\\RMOC\\");
		//herschel.share.util.Configuration.setProperty("rosetta.pwpl.file", "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		//herschel.share.util.Configuration.setProperty("rosetta.orcd.file", "Z:\\MAPPS\\MIB\\orcd.csv");
		//herschel.share.util.Configuration.setProperty("rosetta.default.planningDirectory", "C:\\ROS_SGS\\PLANNING\\");
    	/*try {
			Sequence seq=new Sequence("AGDF060A", "P1009090", DateUtil.dateToDOY(new java.util.Date()));
			JFrame frame=new JFrame();
			SequenceEditor editor=new SequenceEditor();
			editor.setSequence(seq);
			editor.makeContent();
			frame.add(editor);
			frame.setVisible(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	Evtm evtm = PtrUtils.readEvtmFromFile("/Users/jarenas/esa/solar-orbiter/planning/STP207/PTEL_L007_V2_00209.SOL");
        //EvtmEvent[] events = evtm.getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
        EvtmEvent[] events = evtm.getAllEvents();
        /*
         * for event in events:
newmodes=SimulationContext.getInstance().orcd.getModesAsHistory("EVTM_"+event.getId(),event.getTime().getTime())
SimulationContext.getInstance().historyModes.putAll(newmodes,"EVTM_"+event.getId(),event.getTime().getTime())

         */
        for (int i=0;i<events.length;i++){
            EvtmEvent event = events[i];
            System.out.println("*"+event.getId());
            System.out.println("**"+event.getType());
            System.out.println("***"+event.toString());
        }
		/*Por a=new Por();
		System.out.println("Is variable:");
		System.out.println(IS_VARIABLE.satisfy("", a));
		And an=new And(IS_VARIABLE, new CommandingCondition());
		InterpreterNameSpaceUtil nameSpace = InterpreterFactory.getNameSpaceUtil();
		List<String> variables = nameSpace.getVariables();
		for (int i=0;i<variables.size();i++){
			System.out.println(variables.get(i));
		}
		System.out.println(an.satisfy("", a));*/
		
		//System.out.println(SimulationContext.getInstance().historyModes.getTimes().length);

		// TODO Auto-generated method stub
		/*javax.swing.JFrame frame=new javax.swing.JFrame();
		SimulationView view=new SimulationView();
		frame.add(view);
		frame.pack();
		frame.setVisible(true);*/

	}

}
