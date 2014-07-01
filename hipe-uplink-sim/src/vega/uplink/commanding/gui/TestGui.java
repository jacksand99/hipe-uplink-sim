package vega.uplink.commanding.gui;

import java.util.List;

import herschel.share.interpreter.InterpreterFactory;
import herschel.share.interpreter.InterpreterNameSpaceUtil;
import herschel.share.interpreter.InterpreterNameSpaceUtil.And;
import vega.uplink.commanding.Mib;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SimulationContext;
import static herschel.share.interpreter.InterpreterNameSpaceUtil.IS_VARIABLE;

public class TestGui {

	public static void main(String[] args) {
		//herschel.share.util.Configuration.
		//herschel.share.util.Configuration.lo
		herschel.share.util.Configuration.setProperty("var.hcss.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
		herschel.share.util.Configuration.setProperty("var.hcsstest.dir", "C:\\Users\\jarenas\\Downloads\\hcss-12.0.2524");
    	herschel.share.util.Configuration.setProperty("vega.default.evtDirectory", "Z:\\MAPPS\\RMOC\\");
    	herschel.share.util.Configuration.setProperty("vega.orcd.file","Z:\\MAPPS\\MIB\\orcd.csv");
    	herschel.share.util.Configuration.setProperty("vega.pwpl.file","C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
    	herschel.share.util.Configuration.setProperty("vega.mib.location","Z:\\MAPPS\\MIB");

		//herschel.share.util.Configuration.setProperty(Mib.ROSETTA_MIB_LOCATION, "Z:\\MAPPS\\MIB");
		//herschel.share.util.Configuration.setProperty("rosetta.default.evtDirectory", "Z:\\MAPPS\\RMOC\\");
		//herschel.share.util.Configuration.setProperty("rosetta.pwpl.file", "C:\\ROS_SGS\\PLANNING\\RMOC\\FCT\\PWPL_14_001_14_365__OPT_01.ROS");
		//herschel.share.util.Configuration.setProperty("rosetta.orcd.file", "Z:\\MAPPS\\MIB\\orcd.csv");
		//herschel.share.util.Configuration.setProperty("rosetta.default.planningDirectory", "C:\\ROS_SGS\\PLANNING\\");
		
		Por a=new Por();
		System.out.println("Is variable:");
		System.out.println(IS_VARIABLE.satisfy("", a));
		And an=new And(IS_VARIABLE, new CommandingCondition());
		InterpreterNameSpaceUtil nameSpace = InterpreterFactory.getNameSpaceUtil();
		List<String> variables = nameSpace.getVariables();
		for (int i=0;i<variables.size();i++){
			System.out.println(variables.get(i));
		}
		System.out.println(an.satisfy("", a));
		
		//System.out.println(SimulationContext.getInstance().historyModes.getTimes().length);

		// TODO Auto-generated method stub
		javax.swing.JFrame frame=new javax.swing.JFrame();
		SimulationView view=new SimulationView();
		frame.add(view);
		frame.pack();
		frame.setVisible(true);

	}

}
