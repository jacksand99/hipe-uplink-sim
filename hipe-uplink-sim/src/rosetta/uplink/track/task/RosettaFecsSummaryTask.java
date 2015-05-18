package rosetta.uplink.track.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.util.Date;
import java.util.logging.Logger;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.hipe.logging.VegaLog;
import vega.uplink.commanding.PorUtils;
import vega.uplink.commanding.task.PorCheckTask;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.track.Fecs;
import vega.uplink.track.FecsUtils;
import vega.uplink.track.task.CompareFecsTask;

public class RosettaFecsSummaryTask extends Task {
	//private static final Logger LOGGER = Logger.getLogger(PorCheckTask.class.getName());

	public RosettaFecsSummaryTask(){
		super("rosettaFecsSummaryTask");
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
        
		TaskParameter report = new TaskParameter("fecsRosettaSummaryReport", HtmlDocument.class);
		report.setType(TaskParameter.OUT);
		 addTaskParameter(report);



	}
	public static void main(String[] args){
		
		if (args.length!=3){
			System.out.println("Usage: RosettaFecsSummaryTask fullPathFECS fullPathPTSL fullPathDestinationReport");
			System.out.println("Number of arguments provided:"+args.length);
			for (int i=0;i<args.length;i++){
				System.out.println(args[i]);
			}
			System.exit(0);
		}
		Fecs fecs=null;
		try {
			fecs=FecsUtils.readFecsFromFile(args[0]);
		}catch (Exception e){
			System.out.println("Could not read FECS");
			e.printStackTrace();
			System.exit(0);
		}
		Ptr ptsl=null;
		try{
			ptsl=PtrUtils.readPTRfromFile(args[1]);
		}catch (Exception e){
			System.out.println("Could not read PTSL");
			e.printStackTrace();
			System.exit(0);
		}
		HtmlDocument report = produceReport(fecs,ptsl,false);
		try{
			report.saveReportToFile(args[2]);
		}catch (Exception e){
			System.out.println("Could not save report");
			e.printStackTrace();
			System.exit(0);
		}
	}
	public void execute() {
		Fecs fecs = (Fecs) getParameter("fecs").getValue();
		Ptr ptsl=(Ptr) getParameter("ptsl").getValue();
		HtmlDocument result = produceReport(fecs,ptsl,true);
		this.getParameter("fecsRosettaSummaryReport").setValue(result);
	}
	public static HtmlDocument produceReport(Fecs fecs,Ptr ptsl,boolean show){
		
	//}
	//public void execute() { 
		//Fecs fecs = (Fecs) getParameter("fecs").getValue();
		Fecs fecsESA;
		Fecs fecsDSN;
        if (fecs == null) {
            throw (new NullPointerException("Missing fecs value"));
        }




            	fecsESA = fecs.getSubFecsESA();
            	//newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsESA();


            	fecsDSN = fecs.getSubFecsDSN();
            	//newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsDSN();



        String message="";
        
        //Ptr ptsl=(Ptr) getParameter("ptsl").getValue();
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
    			VegaLog.throwing(CompareFecsTask.class, "execute", e);
    			VegaLog.severe(e.getMessage());
    			// TODO Auto-generated catch block
    			e.printStackTrace();
        	}
        }else{
        	PtrSegment[] segments = ptsl.getSegments();
        	message=message+"<h1>Global</h1>";
    		/*message=message+ "<table class=\"gridtable\">\n"
    				+ "<tr>\n"
       				+ "	<th>Name</th><th>34 m pass duration (h/day)</th><th>34 m dump duration (h/day)</th><th>70 m pass duration (h/day)</th><th>70 m dump duration (h/day)</th><th>BSR duration (h/day)</th><th>Total data dump (bits)</th>\n"       			 
    				+ "</tr>\n";*/
       		/*message=message+ "<table class=\"gridtable\">\n"
    				+ "<tr>\n"
    				+ "	<th>MTP</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
    				+ "</tr>\n";*/
        	message=message+ "<table class=\"gridtable\">\n"
    				+ "<tr>\n"
    				+ "	<th>FECS</th><th>ESA pass duration (h/day)</th><th>ESA dump duration (h/day)</th><th>DSN 34m pass duration (h/day)</th><th>DSN 34m dump duration (h/day)</th><th>DSN 70m pass duration (h/day)</th><th>DSN 70m dump duration (h/day)</th><th>BSR/LBS/USO passes (h/day)</th><th>Total data dump (bits)</th>\n"
    				+ "</tr>\n";



        	for (int i=0;i<segments.length;i++){
        		String name=segments[i].getName();
        		Date start=segments[i].getSegmentStartDate();
        		Date end=segments[i].getSegmentEndDate();
        		String fecsName=fecs.getName()+"_"+name;
        		Fecs oldFecs = fecs.getSubFecs(start, end);
        		oldFecs.setName(fecsName);
    		message=message+ "<tr>\n"
    				+ "	<td>"+name+"</td><td>"+oldFecs.getSubFecsESA().getPassHoursDay35m()+"</td><td>"+oldFecs.getSubFecsESA().getDumpHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getPassHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getDumpHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getPassHoursDay70m()+"</td><td>"+oldFecs.getSubFecsDSN().getDumpHoursDay70m()+"</td><td>"+oldFecs.getBSRHoursDay()+"</td><td>"+oldFecs.getTotalDataDump()+"</td></tr>\n";

 
        	}
        	message=message+"</table>";
        	
           	message=message+"<h1>DSN</h1>";
        		/*message=message+ "<table class=\"gridtable\">\n"
        				+ "<tr>\n"
           				+ "	<th>Name</th><th>34 m pass duration (h/day)</th><th>34 m dump duration (h/day)</th><th>70 m pass duration (h/day)</th><th>70 m dump duration (h/day)</th><th>BSR duration (h/day)</th><th>Total data dump (bits)</th>\n"
           			    + "</tr>\n";

            	for (int i=0;i<segments.length;i++){
            		String name=segments[i].getName();
            		Date start=segments[i].getSegmentStartDate();
            		Date end=segments[i].getSegmentEndDate();
            		String fecsName=fecsDSN.getName()+"_"+name;
            		Fecs oldFecs = fecsDSN.getSubFecs(start, end);
            		oldFecs.setName(fecsName);
            		message=message+oldFecs.getFecsSummaryRowHTML(name);
     
            	}
            	message=message+"</table>";*/
        	message=message+ "<table class=\"gridtable\">\n"
    				+ "<tr>\n"
    				+ "	<th>FECS</th><th>DSN 34m pass duration (h/day)</th><th>DSN 34m dump duration (h/day)</th><th>DSN 70m pass duration (h/day)</th><th>DSN 70m dump duration (h/day)</th><th>BSR/LBS/USO passes (h/day)</th><th>Total data dump (bits)</th>\n"
    				+ "</tr>\n";



        	for (int i=0;i<segments.length;i++){
          		String name=segments[i].getName();
        		Date start=segments[i].getSegmentStartDate();
        		Date end=segments[i].getSegmentEndDate();
        		String fecsName=fecsDSN.getName()+"_"+name;
        		Fecs oldFecs = fecsDSN.getSubFecs(start, end);
        		oldFecs.setName(fecsName);
    		message=message+ "<tr>\n"
    				+ "	<td>"+name+"</td><td>"+oldFecs.getPassHoursDay35m()+"</td><td>"+oldFecs.getDumpHoursDay35m()+"</td><td>"+oldFecs.getPassHoursDay70m()+"</td><td>"+oldFecs.getDumpHoursDay70m()+"</td><td>"+oldFecs.getBSRHoursDay()+"</td><td>"+oldFecs.getTotalDataDump()+"</td></tr>\n";

 
        	}
        	message=message+"</table>";


               	message=message+"<h1>ESA</h1>";
        		/*message=message+ "<table class=\"gridtable\">\n"
        				+ "<tr>\n"
        				+ "	<th>Name</th><th>34 m pass duration (h/day)</th><th>34 m dump duration (h/day)</th><th>70 m pass duration (h/day)</th><th>70 m dump duration (h/day)</th><th>BSR duration (h/day)</th><th>Total data dump (bits)</th>\n"
        				+ "</tr>\n";

            	for (int i=0;i<segments.length;i++){
            		String name=segments[i].getName();
            		Date start=segments[i].getSegmentStartDate();
            		Date end=segments[i].getSegmentEndDate();
            		String fecsName=fecsESA.getName()+"_"+name;
            		Fecs oldFecs = fecsESA.getSubFecs(start, end);
            		oldFecs.setName(fecsName);
            		message=message+oldFecs.getFecsSummaryRowHTML(name);
     
            	}
            	message=message+"</table>";*/
            	message=message+ "<table class=\"gridtable\">\n"
        				+ "<tr>\n"
        				+ "	<th>FECS</th><th>ESA pass duration (h/day)</th><th>ESA dump duration (h/day)</th><th>BSR/LBS/USO passes (h/day)</th><th>Total data dump (bits)</th>\n"
        				+ "</tr>\n";



            	for (int i=0;i<segments.length;i++){
            		String name=segments[i].getName();
            		Date start=segments[i].getSegmentStartDate();
            		Date end=segments[i].getSegmentEndDate();
            		String fecsName=fecsESA.getName()+"_"+name;
            		Fecs oldFecs = fecsESA.getSubFecs(start, end);
            		oldFecs.setName(fecsName);
        		message=message+ "<tr>\n"
        				+ "	<td>"+name+"</td><td>"+oldFecs.getPassHoursDay35m()+"</td><td>"+oldFecs.getDumpHoursDay35m()+"</td><td>"+oldFecs.getBSRHoursDay()+"</td><td>"+oldFecs.getTotalDataDump()+"</td></tr>\n";

     
            	}
            	message=message+"</table>";


        }
        message="<html><body>"+message+"</body><html>";
        HtmlDocument result=new HtmlDocument("Summary FECS", message);
        if (show) {
        	HtmlEditorKit frame = new HtmlEditorKit(result);
        }
        return result;
        //this.getParameter("fecsRosettaSummaryReport").setValue(result);
       	//MessagesFrame frame = new MessagesFrame(message);
    	//frame.setVisible(true);
		
	}
	
	//private String reportPerSegment

}
