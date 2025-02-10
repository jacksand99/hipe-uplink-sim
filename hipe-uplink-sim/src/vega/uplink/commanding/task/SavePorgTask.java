package vega.uplink.commanding.task;

import java.util.HashMap;
import java.util.Map;

import herschel.ia.gui.apps.modifier.JFilePathModifier;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.kernel.util.field.FileSelectionMode;
import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
import vega.uplink.Properties;
//import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.SuperPor;

public class SavePorgTask extends Task {
	public SavePorgTask(){
		super("savePorgTask");
		setDescription("Save the por into a file");
		TaskParameter parameter = new TaskParameter("porg", SuperPor.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The PORG to be saved"); //6
		TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the PORG will be saved"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the file will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		SuperPor spor = (SuperPor) getParameter("porg").getValue();
        if (spor == null) {
            throw (new NullPointerException("Missing porg value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=spor.getName();
        }
        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            path=spor.getPath();
        }
        
        PorUtils.writePORGtofile(path+"/"+name, spor);
	}
	public Map<String,Modifier> getCustomModifiers(){

		HashMap<String,Modifier> result=new HashMap<String,Modifier>();
		JFilePathModifier filePathModifier = new JFilePathModifier(FileSelectionMode.DIRECTORY);
		filePathModifier.setValue(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY));
		result.put("path", filePathModifier);
		return result;
	}

}

