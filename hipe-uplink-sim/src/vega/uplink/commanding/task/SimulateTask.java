package vega.uplink.commanding.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.Simulation;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.track.Fecs;

public class SimulateTask extends Task {
	public SimulateTask(){
		super("simulateTask");
		setDescription("SImulate a set of PORs, a PTR, a PDFM and a FECS");
		TaskParameter parameter = new TaskParameter("por", Por.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The POR to be simulates"); //6
		TaskParameter ptr = new TaskParameter("ptr", Ptr.class);
        ptr.setType(TaskParameter.IN);
        ptr.setMandatory(true);
        ptr.setDescription("The PTR used for the simulation"); //6
		TaskParameter pdfm = new TaskParameter("pdfm", Pdfm.class);
        pdfm.setType(TaskParameter.IN);
        pdfm.setMandatory(true);
        pdfm.setDescription("The PDFM used for the simulation"); //6
		TaskParameter fecs = new TaskParameter("fecs", Fecs.class);
        fecs.setType(TaskParameter.IN);
        fecs.setMandatory(true);
        fecs.setDescription("The FECS used for the simulation"); //6
        
		TaskParameter plot = new TaskParameter("plot", Boolean.class);
		plot.setType(TaskParameter.IN);
		plot.setMandatory(true);
		plot.setDefaultValue(false);
		plot.setDescription("Show or not plots"); //6

		TaskParameter result = new TaskParameter("simulationContext", SimulationContext.class);
		result.setType(TaskParameter.OUT);

		result.setDescription("The result of the simulation"); //6

        addTaskParameter(parameter);
        addTaskParameter(ptr);
        addTaskParameter(pdfm);
        addTaskParameter(fecs);
        addTaskParameter(plot);
        addTaskParameter(result);

	}
	
	public void execute() { 
		SimulationContext context=new SimulationContext();
		
		Por por = (Por) getParameter("por").getValue();
        if (por == null) {
            throw (new NullPointerException("Missing por value"));
        }
        Ptr ptr=(Ptr ) getParameter("ptr").getValue();
        if (ptr == null) {
        	throw (new NullPointerException("Missing ptr value"));
        }
        Pdfm pdfm=(Pdfm ) getParameter("pdfm").getValue();
        if (pdfm == null) {
        	throw (new NullPointerException("Missing pdfm value"));
        }
        Fecs fecs=(Fecs ) getParameter("fecs").getValue();
        if (fecs == null) {
        	throw (new NullPointerException("Missing fecs value"));
        }
        context.setPdfm(pdfm);
        context.setPtr(ptr);
        context.setFecs(fecs);
        Boolean plot=(Boolean)getParameter("plot").getValue();
        SimulationContext result;
        context.setPlanningPeriod(ptr.getSegments()[0].getName());
        if (plot){
        	Simulation sim = new Simulation(por,context);
        
        	result=sim.runSimulation();
        }else{
        	Simulation sim = new Simulation(por,context);
            
        	result=sim.runOnlyText(false);
        }
        String messages = result.getLog();
        messages=messages.replaceAll("\n", "<br>\n");
        HtmlDocument doc=new HtmlDocument("Simulation Results",messages);
        HtmlEditorKit kit=new HtmlEditorKit(doc);
        getParameter("simulationContext").setValue(result);
 	}
}
