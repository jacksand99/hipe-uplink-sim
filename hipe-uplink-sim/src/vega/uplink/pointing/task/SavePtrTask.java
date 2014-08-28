package vega.uplink.pointing.task;

import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrUtils;
import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
/*import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;*/

public class SavePtrTask extends Task {
	public SavePtrTask(){
		super("savePtrTask");
		setDescription("Save the ptr into a file");
		TaskParameter parameter = new TaskParameter("ptr", Ptr.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The PTR to be saved"); //6
		TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the PTR will be saved"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the file will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		Ptr ptr = (Ptr) getParameter("ptr").getValue();
        if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=ptr.getName();
        }
        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            name=ptr.getPath();
        }
        
        PtrUtils.writePTRtofile(path+"/"+name, ptr);
	}
}

