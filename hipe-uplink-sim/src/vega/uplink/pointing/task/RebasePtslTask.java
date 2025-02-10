package vega.uplink.pointing.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.util.logging.Logger;

import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrUtils;
//import vega.uplink.pointing.task.PtrSanityCheckTask.MessagesFrame;

public class RebasePtslTask extends Task {
	//private static final Logger LOGGER = Logger.getLogger(ComparePtrsTask.class.getName());

	public RebasePtslTask(){
		super("rebasePtslTask");
		setDescription("Re-base a PTR in the PTSL");
		TaskParameter parameter1 = new TaskParameter("ptr", Ptr.class);
        parameter1.setType(TaskParameter.IN);
        parameter1.setMandatory(true);
        parameter1.setDescription("The ptr to be rebased"); //6

		TaskParameter parameter2 = new TaskParameter("ptsl", Ptr.class);
        parameter2.setType(TaskParameter.IN);
        parameter2.setMandatory(true);
        parameter2.setDescription("The ptsl to be check againts"); //6



        addTaskParameter(parameter1);
        addTaskParameter(parameter2);

	}
	
	public void execute() { 
		Ptr ptr = (Ptr) getParameter("ptr").getValue();
		Ptr ptsl = (Ptr) getParameter("ptsl").getValue();

		if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
		if (ptsl==null){
	          throw (new NullPointerException("Missing ptr value"));
	          
		}
		PtrUtils.rebasePtrPtsl(ptr, ptsl);
	}

}
