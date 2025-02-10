package vega.uplink.pointing.task;

import java.util.HashMap;
import java.util.Map;

import herschel.ia.gui.apps.modifier.JFilePathModifier;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.kernel.util.field.FileSelectionMode;
import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
import vega.uplink.Properties;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PtrUtils;

public class SavePdfmTask extends Task {
	public SavePdfmTask(){
		super("savePdfmTask");
		setDescription("Save the pdfm into a file");
		TaskParameter parameter = new TaskParameter("pdfm", Pdfm.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The PDFM to be saved"); //6
		TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the PDFM will be saved"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the file will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		Pdfm pdfm = (Pdfm) getParameter("pdfm").getValue();
        if (pdfm == null) {
            throw (new NullPointerException("Missing pdfm value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=pdfm.getName();
        }
        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            path=pdfm.getPath();
        }
        
        PtrUtils.writePDFMtofile(path+"/"+name, pdfm);
	}
	public Map<String,Modifier> getCustomModifiers(){

		HashMap<String,Modifier> result=new HashMap<String,Modifier>();
		JFilePathModifier filePathModifier = new JFilePathModifier(FileSelectionMode.DIRECTORY);
		filePathModifier.setValue(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY));
		result.put("path", filePathModifier);
		return result;
	}

}


