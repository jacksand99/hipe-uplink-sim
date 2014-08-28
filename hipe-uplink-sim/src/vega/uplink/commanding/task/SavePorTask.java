package vega.uplink.commanding.task;

import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

public class SavePorTask extends Task {
	public SavePorTask(){
		super("savePorTask");
		setDescription("Save the por into a file");
		TaskParameter parameter = new TaskParameter("por", Por.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The POR to be saved"); //6
		TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the POR will be saved"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the file will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		Por por = (Por) getParameter("por").getValue();
        if (por == null) {
            throw (new NullPointerException("Missing por value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=por.getName();
        }
        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            name=por.getPath();
        }
        
        PorUtils.writePORtofile(path+"/"+name, por);
	}
}
