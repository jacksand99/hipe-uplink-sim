package vega.uplink.pointing.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import vega.hipe.gui.xmlutils.HtmlDocument;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
//import vega.uplink.pointing.task.ComparePtrsTask.MessagesFrame;

public class PtrSanityCheckTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(ComparePtrsTask.class.getName());

	public PtrSanityCheckTask(){
		super("ptrSanityCheckTask");
		setDescription("Check PTR againts FD ICD");
		TaskParameter parameter1 = new TaskParameter("ptr", Ptr.class);
        parameter1.setType(TaskParameter.IN);
        parameter1.setMandatory(true);
        parameter1.setDescription("The ptr to be check"); //6

		TaskParameter parameter2 = new TaskParameter("ptsl", Ptr.class);
        parameter2.setType(TaskParameter.IN);
        parameter2.setMandatory(false);
        parameter2.setDescription("The ptsl to be check againts"); //6
		TaskParameter report = new TaskParameter("ptrSanityReport", HtmlDocument.class);
		report.setType(TaskParameter.OUT);
		 addTaskParameter(report);


        addTaskParameter(parameter1);
        addTaskParameter(parameter2);

	}
	
	public void execute() { 
		Ptr ptr = (Ptr) getParameter("ptr").getValue();
		Ptr ptsl = (Ptr) getParameter("ptsl").getValue();

		if (ptr == null) {
            throw (new NullPointerException("Missing ptr value"));
        }
		if (ptsl==null){
        	String result = PtrChecker.checkPtrHTML(ptr);
        	LOGGER.warning(result);
        	HtmlDocument re=new HtmlDocument("PTR sanity Check",result);
        	HtmlEditorKit htmlEditor=new HtmlEditorKit(re);
        	this.getParameter("ptrSanityReport").setValue(re);
        	//MessagesFrame frame = new MessagesFrame(result);
        	//frame.setVisible(true);
		}else{
	       	String result = PtrChecker.checkPtrHTML(ptr, ptsl);
	       	result="<html><body>"+result+"</body><html>";
        	LOGGER.warning(result);
        	//HtmlEditorKit htmlEditor=new HtmlEditorKit("PTR sanity Check",result);
           	HtmlDocument re=new HtmlDocument("PTR sanity Check",result);
        	HtmlEditorKit htmlEditor=new HtmlEditorKit(re);
        	this.getParameter("ptrSanityReport").setValue(re);
 
		}
	}
	
    private class MessagesFrame extends JFrame{
    	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

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
                /**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
                public void actionPerformed( ActionEvent e ) {
                	frame.dispose();
                }
            }), BorderLayout.SOUTH);
            this.setContentPane(pnl);
            this.setTitle("PTR compare");
    		//this.add(new JLabel(messages));
    		this.pack();
    	}
    }

}
