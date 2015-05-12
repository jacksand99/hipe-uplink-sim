package vega.uplink.track.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.util.Date;
import java.util.logging.Logger;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.commanding.task.PorCheckTask;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.track.Fecs;

public class FecsSummaryTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(PorCheckTask.class.getName());

	public FecsSummaryTask(){
		super("fecsSummaryTask");
		setDescription("Get the summary information od a FECS");
		TaskParameter fecs = new TaskParameter("fecs", Fecs.class);
		fecs.setType(TaskParameter.IN);
		fecs.setMandatory(true);
		fecs.setDescription("The FECS to be summarized"); //6
        addTaskParameter(fecs);


		TaskParameter ptsl = new TaskParameter("ptsl", Ptr.class);
		ptsl.setType(TaskParameter.IN);
		ptsl.setMandatory(false);
		ptsl.setDescription("The PTSL to extract the MTP dates"); //6
        addTaskParameter(ptsl);

		TaskParameter station = new TaskParameter("station", String.class);
		station.setType(TaskParameter.IN);
		station.setMandatory(false);
		station.setDescription("The name of the station to extract"); //6
        addTaskParameter(station);
        
		TaskParameter report = new TaskParameter("fecsSummaryReport", HtmlDocument.class);
		report.setType(TaskParameter.OUT);
		 addTaskParameter(report);


	}
	
	public void execute() { 
		Fecs fecs = (Fecs) getParameter("fecs").getValue();
        if (fecs == null) {
            throw (new NullPointerException("Missing fecs value"));
        }

        String station=(String) getParameter("station").getValue();
        if (station!=null) {
        	if (station.equals("ESA")){
            	fecs = ((Fecs) getParameter("fecs").getValue()).getSubFecsESA();
            	//newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsESA();

        	}
        	if (station.equals("DSN")){
            	fecs = ((Fecs) getParameter("fecs").getValue()).getSubFecsDSN();
            	//newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsDSN();

        	}
        	if (!station.equals("DSN") && !station.equals("ESA")){
        		fecs = ((Fecs) getParameter("fecs").getValue()).getSubFecs(station);
        		//newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecs(station);
        	}
        }

        String message="";
        
        Ptr ptsl=(Ptr) getParameter("ptsl").getValue();
        if (ptsl==null){
        	try{
        		message=fecs.getFecsSummaryTableHTML();
        		/*message=message+olderFecs.getName()+" 34 m (h/day):"+olderFecs.getHoursDay35m()+"\n";
        		message=message+newerFecs.getName()+" 34 m (h/day):"+newerFecs.getHoursDay35m()+"\n";
        		message=message+olderFecs.getName()+" 70 m (h/day):"+olderFecs.getHoursDay70m()+"\n";
        		message=message+newerFecs.getName()+" 70 m (h/day):"+newerFecs.getHoursDay70m()+"\n";
        		message=message+olderFecs.getName()+" BSR (h):"+olderFecs.getBSRHours()+"\n";
        		message=message+newerFecs.getName()+" BSR (h):"+newerFecs.getBSRHours()+"\n";*/
         	}catch (Exception e){
    			message=message+e.getMessage();
    			LOGGER.throwing(CompareFecsTask.class.getName(), "execute", e);
    			LOGGER.severe(e.getMessage());
    			// TODO Auto-generated catch block
    			e.printStackTrace();
        	}
        }else{
        	PtrSegment[] segments = ptsl.getSegments();
    		message=message+ "<table class=\"gridtable\">\n"
    				+ "<tr>\n"
    				+ "	<th>Name</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
    				+ "</tr>\n";

        	for (int i=0;i<segments.length;i++){
        		String name=segments[i].getName();
        		Date start=segments[i].getSegmentStartDate();
        		Date end=segments[i].getSegmentEndDate();
        		String fecsName=fecs.getName()+"_"+name;
        		Fecs oldFecs = fecs.getSubFecs(start, end);
        		oldFecs.setName(fecsName);
        		//message=message+"**************** "+name+" ****************\n\n";
        		//message=message+"<h1>"+name+"</h1>";
        		message=message+oldFecs.getFecsSummaryRowHTML(name);
        		//message=message+oldFecs.getFecsSummaryTableHTML();
        		/*message=message+oldFecs.getName()+" 34 m (h/day):"+oldFecs.getHoursDay35m()+"\n";
        		message=message+newFecs.getName()+" 34 m (h/day):"+newFecs.getHoursDay35m()+"\n";
        		message=message+oldFecs.getName()+" 70 m (h/day):"+oldFecs.getHoursDay70m()+"\n";
        		message=message+newFecs.getName()+" 70 m (h/day):"+newFecs.getHoursDay70m()+"\n";
        		message=message+oldFecs.getName()+" BSR (h):"+oldFecs.getBSRHours()+"\n";
        		message=message+newFecs.getName()+" BSR (h):"+newFecs.getBSRHours()+"\n";*/
        		//message=message+"<br><br>";

        		

        	}
        	message=message+"</table>";
        }
        message="<html><body>"+message+"</body><html>";
        HtmlDocument result=new HtmlDocument("Summary FECS",message);
        HtmlEditorKit frame = new HtmlEditorKit(result);
        this.getParameter("fecsSummaryReport").setValue(result);
       	//MessagesFrame frame = new MessagesFrame(message);
    	//frame.setVisible(true);
		
	}

}
