package vega.uplink.commanding.task;

import herschel.ia.task.Task;
import herschel.ia.task.TaskParameter;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Constructor;
//import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;


//import rosetta.uplink.commanding.RosettaSsmmSimulator;
import vega.hipe.logging.VegaLog;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.Por;
import vega.uplink.commanding.PorChecker;
//import vega.uplink.commanding.task.PorCheckTask.MessagesFrame;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.commanding.SsmmSimulator;
import vega.uplink.track.GsPass;

public class CreateTimelineTask extends Task {
	//private static final Logger LOGGER = Logger.getLogger(CreateTimelineTask.class.getName());

	public CreateTimelineTask(){
		super("createTimelineTask");

		setDescription("Produce timeline of a POR");
		TaskParameter parameter = new TaskParameter("por", Por.class);
        parameter.setType(TaskParameter.IN);
        parameter.setMandatory(true);
        parameter.setDescription("The POR to be checked"); //6

        //addTaskParameter(parameter);
        
		TaskParameter context = new TaskParameter("context", SimulationContext.class);
		context.setType(TaskParameter.OUT);
		context.setMandatory(true);
		context.setDescription("The simulation context"); //6

        addTaskParameter(parameter);
        addTaskParameter(context);

	}
	
	public void execute() { 
		//String message="";
		Por por = (Por) getParameter("por").getValue();
        if (por == null) {
            throw (new NullPointerException("Missing por value"));
        }
        String message="";
        try {
        	SimulationContext context = new SimulationContext();
        	context.getPor().addPor(por);
        	//this.setValue(arg0, arg1);
        	AbstractSequence[] seqs=context.getPor().getOrderedSequences();
    		//SsmmSimulator memorySimulator=context.ssmm;
    		//SsmmSimulator memorySimulator=new RosettaSsmmSimulator(context);
        	SsmmSimulator memorySimulator;
    		String memSimulatorClassName = Properties.getProperty("vega.uplink.ssmmSimulator");
    		Constructor c;
    		try {
    			c = Class.forName(memSimulatorClassName).getConstructor(SimulationContext.class);
    			//SsmmSimulator actualSimulator;
    			memorySimulator = (SsmmSimulator) c.newInstance(this);

    			

    		} catch (Exception e) {
    			IllegalArgumentException iae = new IllegalArgumentException("Could not get registered SSMM simulator: "+e.getMessage());
    			iae.initCause(e);
    			throw(iae);
    		}
    		//SsmmSimulator memorySimulator=SsmmSimulator.getRegisteredSsmmSimulator(context);
    		VegaLog.info("Inserting commands into the model");
    		//String messages="";
    		for (int i=0;i<seqs.length;i++){
    			//if (seqs[i].getName())
    			if (seqs[i].getInstrument().equals("ANTENNA")){
    				//System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
    				memorySimulator.addSequence(seqs[i]);
    			}
    			HashMap<Long,String> newmodes=context.getOrcd().getModesAsHistory(seqs[i].getName(),seqs[i].getExecutionDate().getTime());
    			context.getHistoryModes().putAll(newmodes, seqs[i].getName(), seqs[i].getExecutionDate().getTime());
    			SequenceProfile[] profiles=seqs[i].getProfiles();
    			if (profiles!=null){
    				for (int j=0;j<profiles.length;j++){
    					if (profiles[j].getType().equals("PW")){
    						context.getPowerInstrument().setPower(seqs[i].getInstrument(), new Float(profiles[j].getValue()));
    						context.getZRecordDates().append(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000));
    						context.getHistoryPowerZ().append(context.getPowerInstrument().getTotalPower());
    					}
    					if (profiles[j].getType().equals("DR")){
    						memorySimulator.addAction(seqs[i].getInstrument(), new Date(seqs[i].getExecutionDate().getTime()+(profiles[j].getOffSetSeconds()*1000)), new Float(profiles[j].getValue()));
    						//System.out.println("Detected Z record. Inserted action in memorySimulator");
    					}
    				}
    			}
    		}
    		java.util.Date start=context.getPor().getValidityDates()[0];
    		java.util.Date end=context.getPor().getValidityDates()[1];
    		VegaLog.info("Inserting the GS passes from the FECS into the model");
    		TreeSet<GsPass> passes=context.getFecs().getPasses();
    		Iterator<GsPass> it = passes.iterator();
    		String strategy="";
    		while (it.hasNext()){
    			GsPass pass=it.next();
    			if (pass.getStartPass().after(start) && pass.getStartPass().before(end)){
    				
    				strategy=strategy+memorySimulator.addGsPass(pass);
    			}
    			//messages=messages+memorySimulator.addGsPass(pass);
    		}
    		//if (printStrategy) context.log(strategy);
    		
    		//System.out.println("Run full simulation:");
    		//Synchronize()
    		//boolean r=memorySimulator.runSimulation(context.sp.getValidityDates()[1]);
    		long[] timess=context.getHistoryModes().getTimes();
    		java.util.Vector<Long> temp=new java.util.Vector<Long>();
    		for (int i=0;i<timess.length;i++){
    			if (timess[i]>=start.getTime() && timess[i]<=end.getTime()){
    				temp.add(timess[i]);
    			}
    		}
    		Long[] times=new Long[temp.size()];
    		temp.toArray(times);
    		VegaLog.info("Calculating mode transitions");
    		for (int i=0;i<times.length;i++){
    			long j=times[i];
    			String oldmode=context.getModelState().getStateForMode(context.getHistoryModes().get(j));
    			String newmode=context.getHistoryModes().get(j);
    			if (!context.getOrcd().checkTransion(oldmode, newmode)){
    				String mess="Forbidden transition "+oldmode+"--->"+newmode+" at "+DateUtil.dateToZulu(new Date(j))+" via "+context.getHistoryModes().getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(context.getHistoryModes().getOriginalTime(j)));
    				//messages=messages+mess;
    				context.log(mess);
    				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);
    			}
    			context.getModelState().setState(newmode);
    			context.getHistoryModes().addStates(j,context.getModelState().clone());
    			context.getExecutionDates().append(j);
    			float modelPower = context.getOrcd().getTotalPowerForModes(context.getModelState().getAllStates());
    			context.getHistoryPower().append(modelPower);
    			float mPower = context.getMocPower().getPowerAt(new Date(j));
    			context.getMocPowerHistory().append(mPower);
    			if (modelPower>mPower){
    				String mess="ALARM: Power over due via sequence "+context.getHistoryModes().getCommand(j)+" executed at "+DateUtil.dateToZulu(new Date(context.getHistoryModes().getOriginalTime(j)));
    				//messages=messages+mess;
    				context.log(mess);
    				Logger.getLogger(getClass().getName()).log(Level.SEVERE, mess);

    			}
    			
    		}
    		message=context.getLog();
    		this.setValue("context", context);
		} catch (Exception e) {
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
