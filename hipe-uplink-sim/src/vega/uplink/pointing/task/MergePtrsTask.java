package vega.uplink.pointing.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.util.logging.Logger;

import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
//import vega.uplink.pointing.task.ComparePtrsTask.MessagesFrame;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;

public class MergePtrsTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(MergePtrsTask.class.getName());

	public MergePtrsTask(){
		super("mergePtrsTask");
		setDescription("Merge two PTRs inot a third one");
		TaskParameter parameter1 = new TaskParameter("masterPtr", Ptr.class);
        parameter1.setType(TaskParameter.IN);
        parameter1.setMandatory(true);
        parameter1.setDescription("Master Ptr to be compared againts"); //6

		TaskParameter parameter2 = new TaskParameter("ptr", Ptr.class);
        parameter2.setType(TaskParameter.IN);
        parameter2.setMandatory(true);
        parameter2.setDescription("Ptr to be merged"); //6

		TaskParameter parameter3 = new TaskParameter("targetPtr", Ptr.class);
        parameter3.setType(TaskParameter.IN);
        parameter3.setMandatory(true);
        parameter3.setDescription("The target PTR where the changes will be integrated"); //6

        addTaskParameter(parameter1);
        addTaskParameter(parameter2);
        addTaskParameter(parameter3);

	}
	
	public void execute() { 
		Ptr master = (Ptr) getParameter("masterPtr").getValue();
		Ptr ptr = (Ptr) getParameter("ptr").getValue();
		Ptr target = (Ptr) getParameter("targetPtr").getValue();

		if (master == null) {
			LOGGER.severe("Missing master value");
            throw (new NullPointerException("Missing master value"));
        }
		if (ptr == null) {
			LOGGER.severe("Missing ptr value");

            throw (new NullPointerException("Missing ptr value"));
        }
		if (target == null) {
			LOGGER.severe("Missing target value");

            throw (new NullPointerException("Missing target value"));
        }
		
		PtrUtils.mergePtrs(master, ptr, target);


        //String result = PtrChecker.comparePtrs(ptr1, ptr2);
        //LOGGER.warning(result);
        //MessagesFrame frame = new MessagesFrame(result);
        //frame.setVisible(true);
	}

}
