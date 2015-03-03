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
import static herschel.share.interpreter.InterpreterNameSpaceUtil.IS_VARIABLE;

public class TestGui {

	public static void main(String[] args) {
		//herschel.share.util.Configuration.
		//herschel.share.util.Configuration.lo
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "/Users/jarenas 1/Downloads/hcss.dp.core-13.0.2361");
    	herschel.share.util.Configuration.setProperty("vega.default.evtDirectory", "/Users/jarenas 1/Downloads/MAPPS/RMOC/");
    	herschel.share.util.Configuration.setProperty("vega.orcd.file","/Users/jarenas 1/Downloads/MAPPS/MIB/orcd.csv");
    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","/Users/jarenas 1/OPS/ROS_SGS/PLANNING/RMOC/FCT/PWPL_14_001_14_365__OPT_01.ROS");
    	herschel.share.util.Configuration.setProperty("vega.mib.location","/Users/jarenas 1/Downloads/MAPPS/MIB");
    	herschel.share.util.Configuration.setProperty("var.hcss.cfg.dir", "/Users/jarenas 1/.hcss");
		//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		//herschel.share.util.Configuration.setProperty("rosetta.default.evtDirectory", "Z:\\MAPPS\\RMOC\\");
		//herschel.share.util.Configuration.setProperty("rosetta.pwpl.file", "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		//herschel.share.util.Configuration.setProperty("rosetta.orcd.file", "Z:\\MAPPS\\MIB\\orcd.csv");
		//herschel.share.util.Configuration.setProperty("rosetta.default.planningDirectory", "C:\\ROS_SGS\\PLANNING\\");
    	try {
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
