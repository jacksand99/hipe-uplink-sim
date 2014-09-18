package vega.uplink.commanding.task;

import java.util.HashMap;
import java.util.Map;

import vega.uplink.Properties;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import herschel.ia.gui.apps.modifier.JFilePathModifier;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.kernel.util.field.FileSelectionMode;
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
            path=por.getPath();
        }
        
        PorUtils.writePORtofile(path+"/"+name, por);
	}
	public Map<String,Modifier> getCustomModifiers(){

		HashMap<String,Modifier> result=new HashMap<String,Modifier>();
		JFilePathModifier filePathModifier = new JFilePathModifier(FileSelectionMode.DIRECTORY);
		filePathModifier.setValue(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY));
		result.put("path", filePathModifier);
		return result;
	}

}
