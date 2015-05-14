package vega.uplink.commanding.task;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
import vega.hipe.logging.VegaLog;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorChecker;
//import vega.uplink.commanding.PorUtils;
//import vega.uplink.pointing.task.ComparePtrsTask;
//import vega.uplink.pointing.task.PtrSanityCheckTask.MessagesFrame;

public class PorCheckTask extends Task {
	//private static final Logger LOGGER = Logger.getLogger(PorCheckTask.class.getName());

	public PorCheckTask(){
		super("porCheckTask");
		setDescription("Check sanity of a POR");
		TaskParameter parameter = new TaskParameter("por", Por.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The POR to be checked"); //6

        addTaskParameter(parameter);

	}
	
	public void execute() { 
		Por por = (Por) getParameter("por").getValue();
        if (por == null) {
            throw (new NullPointerException("Missing por value"));
        }
        String message="";
        try {
			message=PorChecker.checkPor(por);
		} catch (IOException e) {
			message=e.getMessage();
			VegaLog.throwing(PorCheckTask.class, "execute", e);
			VegaLog.severe(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       	MessagesFrame frame = new MessagesFrame(message);
    	frame.setVisible(true);
		
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
	        this.setTitle("POR check");
			//this.add(new JLabel(messages));
			this.pack();
		}
	}

}

