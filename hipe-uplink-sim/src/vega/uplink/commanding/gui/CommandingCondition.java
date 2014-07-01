package vega.uplink.commanding.gui;

import herschel.share.interpreter.InterpreterNameSpaceUtil.Condition;
import herschel.share.interpreter.InterpreterUtil;
import vega.uplink.commanding.*;
import vega.uplink.pointing.*;

public class CommandingCondition implements Condition {

	@Override
	public boolean satisfy(String arg0, Object arg1) {
		/*String className=arg1.getClass().getPackage().getName();
		boolean result=className.startsWith("rosetta.uplink");
		return result;*/
		//System.out.println(arg0+","+arg1);
		/*System.out.println("**"+arg1.getClass().getPackage().getName()+arg1.getClass().getName()+"**");
		if (arg1.getClass().getName().equals("Por")) return true;
		if (arg0.equals("hm")) return true;
		return false;*/
		boolean result=false;
		if (InterpreterUtil.isInstance(HistoryModes.class, arg1)){
			result=true;
		}
		if (InterpreterUtil.isInstance(Por.class, arg1)){
			result=true;
		}
		if (InterpreterUtil.isInstance(Ptr.class, arg1)){
			result=true;
		}
		if (InterpreterUtil.isInstance(Orcd.class, arg1)){
			result=true;
		}
		if (InterpreterUtil.isInstance(Fecs.class, arg1)){
			result=true;
		}
		if (InterpreterUtil.isInstance(HistoryModes.class, arg1)){
			result=true;
		}
		
		
		return result;
	}

}
