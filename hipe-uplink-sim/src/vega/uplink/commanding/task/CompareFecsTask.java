package vega.uplink.commanding.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
//import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.DateUtil;
import vega.uplink.commanding.Fecs;
import vega.uplink.pointing.PointingBlock;
//import vega.uplink.commanding.Por;
//import vega.uplink.commanding.PorChecker;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrSegment;
//import vega.uplink.commanding.task.PorCheckTask.MessagesFrame;

public class CompareFecsTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(PorCheckTask.class.getName());

	public CompareFecsTask(){
		super("compareFecsTask");
		setDescription("Compare differences between 2 FECS");
		TaskParameter olderFecs = new TaskParameter("olderFecs", Fecs.class);
		olderFecs.setType(TaskParameter.IN);
		olderFecs.setMandatory(true);
		olderFecs.setDescription("The older FECS to be checked"); //6
        addTaskParameter(olderFecs);

		TaskParameter newerFecs = new TaskParameter("newerFecs", Fecs.class);
		newerFecs.setType(TaskParameter.IN);
		newerFecs.setMandatory(true);
		newerFecs.setDescription("The newer FECS to be checked"); //6
        addTaskParameter(newerFecs);

		TaskParameter ptsl = new TaskParameter("ptsl", Ptr.class);
		ptsl.setType(TaskParameter.IN);
		ptsl.setMandatory(false);
		ptsl.setDescription("The PTSL to extract the MTP dates"); //6
        addTaskParameter(ptsl);

		TaskParameter station = new TaskParameter("station", String.class);
		station.setType(TaskParameter.IN);
		station.setMandatory(false);
		station.setDescription("The name of the station to compare"); //6
        addTaskParameter(station);
        
		TaskParameter report = new TaskParameter("fecsCompareReport", HtmlDocument.class);
		report.setType(TaskParameter.OUT);
		 addTaskParameter(report);

	}
	
	public void execute() { 
		Fecs olderFecs = (Fecs) getParameter("olderFecs").getValue();
        if (olderFecs == null) {
            throw (new NullPointerException("Missing olderFecs value"));
        }
 		Fecs newerFecs = (Fecs) getParameter("newerFecs").getValue();
        if (newerFecs == null) {
            throw (new NullPointerException("Missing newerFecs value"));
        }
        String oldName = olderFecs.getName();
        Date startDate = newerFecs.getValidityStart();
        //if (new Date().after(startDate)) startDate=new Date();
        olderFecs=olderFecs.getSubFecs(startDate, newerFecs.getValidityEnd());
        olderFecs.setName(oldName);
        String station=(String) getParameter("station").getValue();
        if (station!=null) {
        	if (station.equals("ESA")){
            	olderFecs = ((Fecs) getParameter("olderFecs").getValue()).getSubFecsESA();
            	newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsESA();

        	}
        	if (station.equals("DSN")){
            	olderFecs = ((Fecs) getParameter("olderFecs").getValue()).getSubFecsDSN();
            	newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecsDSN();

        	}
        	if (!station.equals("DSN") && !station.equals("ESA")){
        		olderFecs = ((Fecs) getParameter("olderFecs").getValue()).getSubFecs(station);
        		newerFecs = ((Fecs) getParameter("newerFecs").getValue()).getSubFecs(station);
        	}
        }

        String message="";
        
        Ptr ptsl=(Ptr) getParameter("ptsl").getValue();
        if (ptsl==null){
        	try{
        		/*message=message+olderFecs.getName()+" 34 m (h/day):"+olderFecs.getHoursDay35m()+"\n";
        		message=message+newerFecs.getName()+" 34 m (h/day):"+newerFecs.getHoursDay35m()+"\n";
        		message=message+olderFecs.getName()+" 70 m (h/day):"+olderFecs.getHoursDay70m()+"\n";
        		message=message+newerFecs.getName()+" 70 m (h/day):"+newerFecs.getHoursDay70m()+"\n";
        		message=message+olderFecs.getName()+" BSR (h):"+olderFecs.getBSRHours()+"\n";
        		message=message+newerFecs.getName()+" BSR (h):"+newerFecs.getBSRHours()+"\n";*/
        		/*message=message+ "<table class=\"gridtable\">\n"
        				+ "<tr>\n"
        				+ "	<th>FECS</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
        				+ "</tr>\n";
        		message=message+ "<tr>\n"
        				+ "	<td>"+olderFecs.getName()+"</td><td>"+olderFecs.getPassHoursDay35m()+"</td><td>"+olderFecs.getDumpHoursDay35m()+"</td><td>"+olderFecs.getPassHoursDay70m()+"</td><td>"+olderFecs.getDumpHoursDay70m()+"</td><td>"+olderFecs.getBSRHoursDay()+"</td><td>"+olderFecs.getTotalDataDump()+"</td></tr>\n"
        				+ "	<td>"+newerFecs.getName()+"</td><td>"+newerFecs.getPassHoursDay35m()+"</td><td>"+newerFecs.getDumpHoursDay35m()+"</td><td>"+newerFecs.getPassHoursDay70m()+"</td><td>"+newerFecs.getDumpHoursDay70m()+"</td><td>"+newerFecs.getBSRHoursDay()+"</td><td>"+newerFecs.getTotalDataDump()+"</td>\n"
        				+ "</tr></table>\n";    */
	        	message=message+ "<table class=\"gridtable\">\n"
        				+ "<tr>\n"
        				+ "	<th>FECS</th><th>ESA pass duration (h/day)</th><th>ESA dump duration (h/day)</th><th>DSN 34m pass duration (h/day)</th><th>DSN 34m dump duration (h/day)</th><th>DSN 70m pass duration (h/day)</th><th>DSN 70m dump duration (h/day)</th><th>BSR/LBS/USO passes (h/day)</th>\n"
        				+ "</tr>\n";
        		message=message+ "<tr>\n"
        				+ "	<td>"+olderFecs.getName()+"</td><td>"+olderFecs.getSubFecsESA().getPassHoursDay35m()+"</td><td>"+olderFecs.getSubFecsESA().getDumpHoursDay35m()+"</td><td>"+olderFecs.getSubFecsDSN().getPassHoursDay35m()+"</td><td>"+olderFecs.getSubFecsDSN().getDumpHoursDay35m()+"</td><td>"+olderFecs.getSubFecsDSN().getPassHoursDay70m()+"</td><td>"+olderFecs.getSubFecsDSN().getDumpHoursDay70m()+"</td><td>"+olderFecs.getBSRHoursDay()+"</td></tr>\n"
        				+ "	<td>"+newerFecs.getName()+"</td><td>"+newerFecs.getSubFecsESA().getPassHoursDay35m()+"</td><td>"+newerFecs.getSubFecsESA().getDumpHoursDay35m()+"</td><td>"+newerFecs.getSubFecsDSN().getPassHoursDay35m()+"</td><td>"+newerFecs.getSubFecsDSN().getDumpHoursDay35m()+"</td><td>"+newerFecs.getSubFecsDSN().getPassHoursDay70m()+"</td><td>"+newerFecs.getSubFecsDSN().getDumpHoursDay70m()+"</td><td>"+newerFecs.getBSRHoursDay()+"</td></tr>\n"
        				+ "</tr></table>\n";  


        		
        		
        		message=message+Fecs.compareFecsHTML(olderFecs, newerFecs);
        	}catch (Exception e){
    			message=message+e.getMessage();
    			LOGGER.throwing(CompareFecsTask.class.getName(), "execute", e);
    			LOGGER.severe(e.getMessage());
    			// TODO Auto-generated catch block
    			e.printStackTrace();
        	}
        }else{
        	PtrSegment[] segments = ptsl.getSegments();
        	for (int i=0;i<segments.length;i++){
        		//if (segments[i].getEndDate().toDate().after(newerFecs.getValidityStart())){
        		if (segments[i].getEndDate().toDate().after(new Date())){
	        		String name=segments[i].getName();
	        		Date start=segments[i].getSegmentStartDate();
	        		Date end=segments[i].getSegmentEndDate();
	        		String oldFecsName=olderFecs.getName();
	        		String newFecsName=newerFecs.getName();
	        		Fecs oldFecs = olderFecs.getSubFecs(start, end);
	        		oldFecs.setName(oldFecsName);
	        		Fecs newFecs = newerFecs.getSubFecs(start, end);
	        		newFecs.setName(newFecsName);
	        		//message=message+"**************** "+name+" ****************\n\n";
	        		message=message+"<h1>"+name+"</h1>";
	        		message=message+ "<table class=\"gridtable\">\n"
	        				+ "<tr>\n"
	        				+ "	<th>Start Date</th><th>End Date</th>\n"
	        				+ "</tr>\n";
	        		message=message+ "<tr>\n"
	        				+ "	<td>"+DateUtil.defaultDateToString(start)+"</td><td>"+DateUtil.defaultDateToString(end)+"</td>\n"
	        				+ "</tr>\n"
	        				+ "</table>\n";
	        		message=message+"<br><br>";
	        		/*message=message+ "<table class=\"gridtable\">\n"
	        				+ "<tr>\n"
	        				+ "	<th>FECS</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
	        				+ "</tr>\n";
	        		message=message+ "<tr>\n"
	        				+ "	<td>"+oldFecs.getName()+"</td><td>"+oldFecs.getPassHoursDay35m()+"</td><td>"+oldFecs.getDumpHoursDay35m()+"</td><td>"+oldFecs.getPassHoursDay70m()+"</td><td>"+oldFecs.getDumpHoursDay70m()+"</td><td>"+oldFecs.getBSRHoursDay()+"</td><td>"+oldFecs.getTotalDataDump()+"</td></tr>\n"
	        				+ "	<td>"+newFecs.getName()+"</td><td>"+newFecs.getPassHoursDay35m()+"</td><td>"+newFecs.getDumpHoursDay35m()+"</td><td>"+newFecs.getPassHoursDay70m()+"</td><td>"+newFecs.getDumpHoursDay70m()+"</td><td>"+newFecs.getBSRHoursDay()+"</td><td>"+newFecs.getTotalDataDump()+"</td>\n"
	        				+ "</tr></table>\n";      		
	
	        		message=message+"<br><br>";*/
	        	message=message+ "<table class=\"gridtable\">\n"
	        				+ "<tr>\n"
	        				+ "	<th>FECS</th><th>ESA pass duration (h/day)</th><th>ESA dump duration (h/day)</th><th>DSN 34m pass duration (h/day)</th><th>DSN 34m dump duration (h/day)</th><th>DSN 70m pass duration (h/day)</th><th>DSN 70m dump duration (h/day)</th><th>BSR/LBS/USO passes (h/day)</th>\n"
	        				+ "</tr>\n";
	        		message=message+ "<tr>\n"
	        				+ "	<td>"+oldFecs.getName()+"</td><td>"+oldFecs.getSubFecsESA().getPassHoursDay35m()+"</td><td>"+oldFecs.getSubFecsESA().getDumpHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getPassHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getDumpHoursDay35m()+"</td><td>"+oldFecs.getSubFecsDSN().getPassHoursDay70m()+"</td><td>"+oldFecs.getSubFecsDSN().getDumpHoursDay70m()+"</td><td>"+oldFecs.getBSRHoursDay()+"</td></tr>\n"
	        				+ "	<td>"+newFecs.getName()+"</td><td>"+newFecs.getSubFecsESA().getPassHoursDay35m()+"</td><td>"+newFecs.getSubFecsESA().getDumpHoursDay35m()+"</td><td>"+newFecs.getSubFecsDSN().getPassHoursDay35m()+"</td><td>"+newFecs.getSubFecsDSN().getDumpHoursDay35m()+"</td><td>"+newFecs.getSubFecsDSN().getPassHoursDay70m()+"</td><td>"+newFecs.getSubFecsDSN().getDumpHoursDay70m()+"</td><td>"+newFecs.getBSRHoursDay()+"</td></tr>\n"
	        				+ "</tr></table>\n";  
	           		/*message=message+ "<table class=\"gridtable\">\n"
	        				+ "<tr>\n"
	        				+ "	<th>FECS</th><th>34 m pass (h/day)</th><th>34 m dump (h/day)</th><th>70 m pass (h/day)</th><th>70 m dump (h/day)</th><th>BSR (h/day)</th><th>Total data dump</th>\n"
	        				+ "</tr>\n";
	        		message=message+ "<tr>\n"
	        				+ "	<td>"+olderFecs.getName()+"</td><td>"+olderFecs.getPassHoursDay35m()+"</td><td>"+olderFecs.getDumpHoursDay35m()+"</td><td>"+olderFecs.getPassHoursDay70m()+"</td><td>"+olderFecs.getDumpHoursDay70m()+"</td><td>"+olderFecs.getBSRHoursDay()+"</td><td>"+olderFecs.getTotalDataDump()+"</td></tr>\n"
	        				+ "	<td>"+newerFecs.getName()+"</td><td>"+newerFecs.getPassHoursDay35m()+"</td><td>"+newerFecs.getDumpHoursDay35m()+"</td><td>"+newerFecs.getPassHoursDay70m()+"</td><td>"+newerFecs.getDumpHoursDay70m()+"</td><td>"+newerFecs.getBSRHoursDay()+"</td><td>"+newerFecs.getTotalDataDump()+"</td>\n"
	        				+ "</tr></table>\n"; */     		

	
	        		
	            	try{
	            		message=message+Fecs.compareFecsHTML(oldFecs, newFecs);
	            	}catch (Exception e){
	        			message=message+e.getMessage();
	        			LOGGER.throwing(CompareFecsTask.class.getName(), "execute", e);
	        			LOGGER.severe(e.getMessage());
	        			// TODO Auto-generated catch block
	        			e.printStackTrace();
	            	}
        		}
        	}
        }
        message="<html><body>"+message+"</body><html>";
        HtmlDocument result=new HtmlDocument("Compare Fecs",message);
        //HtmlEditorKit frame = new HtmlEditorKit("Compare FECS",message);
        HtmlEditorKit frame = new HtmlEditorKit(result);
        this.getParameter("fecsCompareReport").setValue(result);
       	//MessagesFrame frame = new MessagesFrame(message);
    	//frame.setVisible(true);
		
	}
}

/*		private static final long serialVersionUID = 1L;
	
		MessagesFrame(String messages){
			super();
			final JFrame frame=this;
	        final JTextPane pane = new JTextPane();
	        pane.setText(messages+"\n\n\n\n");
	        JPanel pnl = new JPanel(new BorderLayout());
	        JScrollPane scrollPane = new JScrollPane();
	        scrollPane.getViewport().add(pane);
	        pnl.add( scrollPane, BorderLayout.CENTER );
	        pnl.add(new JButton(new AbstractAction("Close") {
				private static final long serialVersionUID = 1L;
	
				@Override
	            public void actionPerformed( ActionEvent e ) {
	            	frame.dispose();
	            }
	        }), BorderLayout.SOUTH);
	        this.setContentPane(pnl);
	        this.setTitle("FECS compare");
			//this.add(new JLabel(messages));
			this.pack();
		}
	}*/
	
	


