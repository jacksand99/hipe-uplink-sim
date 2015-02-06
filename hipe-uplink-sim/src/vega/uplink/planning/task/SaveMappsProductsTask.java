package vega.uplink.planning.task;

/*public class SaveMappsProductsTask {

}*/
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import vega.uplink.Properties;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;
import vega.uplink.planning.period.Plan;
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

public class SaveMappsProductsTask extends Task {
	public SaveMappsProductsTask(){
		super("saveMappsProductsTask");
		setDescription("Save the mapps products into a file");
		TaskParameter parameter = new TaskParameter("schedule", Schedule.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The schedule to be exported"); //6

		/*TaskParameter pdfm = new TaskParameter("pdfm", Pdfm.class);
        pdfm.setType(TaskParameter.IN);
        pdfm.setMandatory(false);
        pdfm.setDescription("The PDFM to be included unt the PTR"); //6*/

        TaskParameter plan = new TaskParameter("planning", Plan.class);
        plan.setType(TaskParameter.IN);
        plan.setMandatory(false);
        plan.setDescription("The file name where the schedule will be exported"); //6

        
        TaskParameter name = new TaskParameter("name", String.class);
        name.setType(TaskParameter.IN);
        name.setMandatory(false);
        name.setDescription("The file name where the schedule will be exported"); //6
		TaskParameter path = new TaskParameter("path", String.class);
        path.setType(TaskParameter.IN);
        path.setMandatory(false);
        path.setDescription("The path where the files will be saved"); //6

        addTaskParameter(parameter);
        addTaskParameter(plan);

        addTaskParameter(name);
        addTaskParameter(path);

	}
	
	public void execute() { 
		Schedule schedule = (Schedule) getParameter("schedule").getValue();
		Plan plan = (Plan) getParameter("planning").getValue();
		Ptr ptr=schedule.getPtr();
		Pdfm pdfm=schedule.getPdfm();
		//Pdfm pdfm=(Pdfm) getParameter("pdfm").getValue();
        if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
        String name=(String ) getParameter("name").getValue();
        if (name == null) {
            name=schedule.getFileName();
        }

        String path=(String ) getParameter("path").getValue();
        if (path == null) {
            path=ptr.getPath();
        }
        String ptrName="PTRM_"+name+".ROS";
        ptr.setName(ptrName);
        if (pdfm!=null){
        	String pdfmName=ptrName.replace("PTRM_", "");
        	pdfmName="PDFM_"+pdfmName;
        	pdfm.setName(pdfmName);
        	pdfm.setPath(path);
    		PtrSegment seg = ptr.getSegments()[0];
    		String[] includes={"FDDF.xml",pdfmName};
    		seg.setIncludes(includes);
    		PtrUtils.savePDFM(pdfm);
        }
        
        PtrUtils.writePTRtofile(path+"/"+ptrName, ptr);
        try {
        	if (plan==null){
        		ObservationUtil.saveMappsProducts(path+"/"+name, schedule);
        	}else{
        		ObservationUtil.saveMappsProducts(path+"/"+name, schedule,plan);
        	}
		} catch (IOException e) {
			IllegalArgumentException ioe = new IllegalArgumentException(e.getMessage());
			ioe.initCause(e);
			throw(ioe);
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
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
