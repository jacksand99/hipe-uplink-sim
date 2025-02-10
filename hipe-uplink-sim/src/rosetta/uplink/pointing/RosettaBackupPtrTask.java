package rosetta.uplink.pointing;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;


//import java.util.List;
import java.util.logging.Logger;


//import rosetta.uplink.pointing.ExclusionPeriod.Period;
//import vega.uplink.Properties;
//import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.exclusion.Period;

public class RosettaBackupPtrTask extends Task{

	public RosettaBackupPtrTask(){
		
		super("rosettaBackupPtrTask");

		setDescription("Produce backup PTR for Rosetta ");
		TaskParameter parameter = new TaskParameter("ptr", Ptr.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The PTR "); //6
		TaskParameter ptsl = new TaskParameter("ptsl", Ptr.class);
        ptsl.setType(TaskParameter.IN);
        ptsl.setMandatory(true);
        ptsl.setDescription("The PTSL "); //6
		TaskParameter excl = new TaskParameter("excl", ExclusionPeriod.class);
		excl.setType(TaskParameter.IN);
		excl.setMandatory(true);
		excl.setDescription("The Exclusion Periods file used"); //6
		TaskParameter bkPtr = new TaskParameter("backupPtr", Ptr.class);
		bkPtr.setType(TaskParameter.OUT);
 		bkPtr.setDescription("The Backup PTR "); //6
		
        addTaskParameter(parameter);
        addTaskParameter(ptsl);
        addTaskParameter(excl);
        addTaskParameter(bkPtr);
        
	}
	
	public void execute() {
		Ptr ptr=(Ptr) getParameter("ptr").getValue();
		Ptr ptsl=(Ptr) getParameter("ptsl").getValue();
		ExclusionPeriod excl = (ExclusionPeriod) getParameter("excl").getValue();
		String segmentName=ptr.getSegments()[0].getName();
		Period[] periods = excl.getAllPeriods();
		Ptr result=new Ptr();
		PtrSegment seg = ptr.getSegment(segmentName).copy();
		result.addSegment(seg);
		for (int i=0;i<periods.length;i++){
			Period per = periods[i];
			PointingBlocksSlice blocks = ptsl.getSegment(segmentName).getBlocksAt(per.getStartDate(), per.getEndDate());
			seg.setSlice(blocks);
		}
		this.setValue("backupPtr", result);
	}
	
}
