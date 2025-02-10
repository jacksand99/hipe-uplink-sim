package vega.uplink.commanding.gui;

import vega.uplink.commanding.HistoryModes;
import vega.uplink.commanding.Orcd;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.planning.Observation;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.track.Fecs;
import herschel.ia.gui.apps.views.variables.VariablesView;
import herschel.share.interpreter.InterpreterUtil;
import herschel.share.interpreter.InterpreterNameSpaceUtil.Condition;

/*
 * This file is part of Herschel Common Science System (HCSS).
 * Copyright 2001-2012 Herschel Science Ground Segment Consortium
 *
 * HCSS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * HCSS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with HCSS.
 * If not, see <http://www.gnu.org/licenses/>.
 */

//package herschel.ia.gui.apps.views.variables;

 
public class SimulationContextView extends VariablesView {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SimulationContextView(){
		super();
		//super
		//super.registerCondition("Simulation Context", new CommandingCondition());
		super.registerCondition("Observations", new ObservationCondition());
	}
	public static void registerCondition(String title,Condition condition){
		/*if (InterpreterUtil.isInstance(CommandingCondition.class, condition)){
			super.registerCondition("Simulation Context", condition);
		}*/
		//Do nothing
	}
	public class ObservationCondition implements Condition {

		@Override
		public boolean satisfy(String arg0, Object arg1) {

			boolean result=false;
			if (InterpreterUtil.isInstance(Observation.class, arg1)){
				result=true;
			}

			
			
			return result;
		}

	}
}

