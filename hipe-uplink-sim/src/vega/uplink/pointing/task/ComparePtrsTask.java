package vega.uplink.pointing.task;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
//import vega.uplink.pointing.PtrUtils;

public class ComparePtrsTask extends Task {
	private static final Logger LOGGER = Logger.getLogger(ComparePtrsTask.class.getName());

	public ComparePtrsTask(){
		super("comparePtrsTask");
		setDescription("Compare two PTRs");
		TaskParameter parameter1 = new TaskParameter("ptr1", Ptr.class);
        parameter1.setType(TaskParameter.IN);
        parameter1.setMandatory(true);
        parameter1.setDescription("The first ptr to be compared"); //6

		TaskParameter parameter2 = new TaskParameter("ptr2", Ptr.class);
        parameter2.setType(TaskParameter.IN);
        parameter2.setMandatory(true);
        parameter2.setDescription("The first ptr to be compared"); //6


        addTaskParameter(parameter1);
        addTaskParameter(parameter2);

	}
	
	public void execute() { 
		Ptr ptr1 = (Ptr) getParameter("ptr1").getValue();
		Ptr ptr2 = (Ptr) getParameter("ptr2").getValue();

		if (ptr2 == null) {
            throw (new NullPointerException("Missing ptr1 value"));
        }
        String result = PtrChecker.comparePtrs(ptr1, ptr2);
        LOGGER.warning(result);
        MessagesFrame frame = new MessagesFrame(result);
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
            this.setTitle("PTR compare");
    		//this.add(new JLabel(messages));
    		this.pack();
    	}
    }

}
