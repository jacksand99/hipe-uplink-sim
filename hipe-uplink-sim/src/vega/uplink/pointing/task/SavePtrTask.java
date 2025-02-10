package vega.uplink.pointing.task;

import java.util.HashMap;
import java.util.Map;

import vega.uplink.Properties;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import herschel.ia.gui.apps.modifier.JFilePathModifier;
import herschel.ia.gui.apps.modifier.Modifier;
import herschel.ia.gui.kernel.util.field.FileSelectionMode;
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

		TaskParameter pdfm = new TaskParameter("pdfm", Pdfm.class);
        pdfm.setType(TaskParameter.IN);
        pdfm.setMandatory(false);
        pdfm.setDescription("The PDFM to be included unt the PTR"); //6

        
        TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the PTR will be saved"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the file will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(pdfm);

        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		Ptr ptr = (Ptr) getParameter("ptr").getValue();
		Pdfm pdfm=(Pdfm) getParameter("pdfm").getValue();
        if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=ptr.getName();
        }

        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            path=ptr.getPath();
        }
        if (pdfm!=null){
        	String pdfmName=name.replace("PTRM_", "");
        	pdfmName="PDFM_"+pdfmName;
        	pdfm.setName(pdfmName);
        	pdfm.setPath(path);
    		PtrSegment seg = ptr.getSegments()[0];
    		String[] includes={"FDDF.xml",pdfmName};
    		seg.setIncludes(includes);
    		PtrUtils.savePDFM(pdfm);
        }
        
        PtrUtils.writePTRtofile(path+"/"+name, ptr);
	}
	public Map<String,Modifier> getCustomModifiers(){

		HashMap<String,Modifier> result=new HashMap<String,Modifier>();
		JFilePathModifier filePathModifier = new JFilePathModifier(FileSelectionMode.DIRECTORY);
		//((Component) filePathModifier).
		filePathModifier.setValue(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY));
		result.put("path", filePathModifier);
		return result;
	}

}

