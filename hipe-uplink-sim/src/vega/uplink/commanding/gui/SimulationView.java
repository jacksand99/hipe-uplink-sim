package vega.uplink.commanding.gui;
//import java.awt.BorderLayout;
import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import herschel.ia.gui.kernel.parts.ViewPart;
import herschel.ia.gui.kernel.parts.Viewable;
import herschel.ia.gui.kernel.menus.ActionBarsHolder;
import herschel.ia.gui.kernel.menus.ActionMaker;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
//import javax.swing.JList;
import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTextArea;
//import javax.swing.ScrollPaneLayout;


import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

import herschel.ia.gui.kernel.SiteEvent;
import herschel.ia.gui.kernel.SiteEventListener;
import herschel.share.interpreter.InterpreterFactory;
import herschel.share.interpreter.InterpreterNameSpaceUtil;
//import herschel.share.util.Configuration;
import vega.uplink.Properties;
//import rosetta.uplink.commanding.Simulation;
import vega.uplink.commanding.*;
import vega.uplink.commanding.itl.ItlParser;
import vega.uplink.pointing.Evtm;
import vega.uplink.pointing.EvtmEvent;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;

import java.lang.Runnable;

import javax.swing.*; 


public class SimulationView extends JPanel implements Viewable, ActionMaker, SiteEventListener {
	private static final long serialVersionUID = 1L;
	public static final String ID = "site.view.simulation";
	private File itlFile;
	private File evtFile;
	private File evtmFile;
	private File[] porFiles;
	private File ptrFile;
	private File pdfmFile;

	private File fecsFile;
	private File initScript;
	private File postScript;
	
	private Ptr ptr;
	JLabel itlbar;
	JLabel evfbar;
	JLabel porbar;
	JLabel ptrbar;
	JLabel pdfmbar;
	JLabel evtmbar;
	JLabel fecsbar;
	JLabel initbar;
	JLabel postbar;
	JComboBox<String> ptrbox;
	String[] segmentNames;
	JButton runButton;
	JButton resetButton;
	String defaultDirectory=Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY);
	String defaultScriptsDirectory=Properties.getProperty("user.home");

	SimulationContext context;
	
    public SimulationView(){
    	super();
    	context=new SimulationContext();
    	itlFile=null;
    	evtFile=null;
    	evtmFile=null;
    	fecsFile=null;
    	pdfmFile=null;
    	initScript=null;
    	postScript=null;
		try{
			fecsFile=new File(Properties.getProperty(Properties.DEFAULT_FECS_FILE));
		}catch (herschel.share.util.ConfigurationException e){
			//e.printStackTrace();
			fecsFile=null;
		}
		try{
			initScript=new File(Properties.getProperty(Properties.DEFAULT_INIT_SCRIPT));
		}catch (Exception e){
			initScript=null;
		}
		try{
			postScript=new File(Properties.getProperty(Properties.DEFAULT_POST_SCRIPT));
		}catch (Exception e){
			postScript=null;
		}

    	
    	
    	//super(new FlowLayout());
		/*String[] items = {"A", "B", "C", "D"};
		JList list = new JList(items);
		JTextArea text = new JTextArea(10, 40);
		this.add(list);
		this.add(text);*/
	    Container c = this;
	    //c.setLayout(new ScrollPaneLayout());
	    
	    JButton itlButton = new JButton("Select ITL file:");
	    JButton evfButton = new JButton("Select EVT file:");
	    JButton porButton = new JButton("Select POR files:");
	    JButton ptrButton = new JButton("Select PTR/PTSL file:");
	    JButton pdfmButton = new JButton("Select PDFM file:");
	    JButton evtmButton = new JButton("Select EVTM file:");
	    JButton fecsButton = new JButton("Select FECS file:");
	    JButton initButton = new JButton("Select Init script:");
	    JButton postButton = new JButton("Select Post script:");

	    JLabel ptrLabel = new JLabel("Select PTR/PTSL Segment:");
	    
	    runButton = new JButton("Run Simulation");
	    runButton.setEnabled(false);
	    resetButton=new JButton("Reset");
	    
	    //JButton saveButton = new JButton("Save");
	    //Button dirButton = new JButton("Pick Dir");
	    itlbar = 
	                 new JLabel("Output of your selection will go here");

	    evfbar = 
                new JLabel("Output of your selection will go here");
	    porbar = 
                new JLabel("Output of your selection will go here");
	    ptrbar = 
                new JLabel("Output of your selection will go here");
	    pdfmbar = 
                new JLabel("Output of your selection will go here");

	    evtmbar = 
                new JLabel("Output of your selection will go here");
	    fecsbar = 
                new JLabel("Output of your selection will go here");
	    initbar = 
                new JLabel("Output of your selection will go here");
	    postbar = 
                new JLabel("Output of your selection will go here");

	    segmentNames=new String[1];
	    segmentNames[0]="PTR_SEGMENT";
	    ptrbox = 
                new JComboBox(segmentNames);


	    //itlbar.setMaximumSize(new Dimension(this.getSize().width/2,this.getSize().height));
	    

	    ActionListener ptral=new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          chooser.setCurrentDirectory(new File(defaultDirectory));
	          int option = chooser.showOpenDialog(SimulationView.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            ptrFile=sf;
	            defaultDirectory=ptrFile.getParent();
	            refreshLabels();
	            //String filelist="Nothing";
	            //filelist=sf.getName();
	            //itlbar.setText("ITL file: " + filelist);
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	        }

	      };
	      ptrButton.addActionListener(ptral);
	      
		    ActionListener pdfmal=new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		          JFileChooser chooser = new JFileChooser();
		          chooser.setMultiSelectionEnabled(false);
		          chooser.setCurrentDirectory(new File(defaultDirectory));
		          int option = chooser.showOpenDialog(SimulationView.this);
		          if (option == JFileChooser.APPROVE_OPTION) {
		            File sf = chooser.getSelectedFile();
		            pdfmFile=sf;
		            defaultDirectory=pdfmFile.getParent();
		            refreshLabels();
		            //String filelist="Nothing";
		            //filelist=sf.getName();
		            //itlbar.setText("ITL file: " + filelist);
		          }
		          else {
		            //itlbar.setText("You canceled.");
		          }
		        }

		      };
		      
		      pdfmButton.addActionListener(pdfmal);


	    ActionListener poral=new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(true);
	          chooser.setCurrentDirectory(new File(defaultDirectory));
	          int option = chooser.showOpenDialog(SimulationView.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File[] sf = chooser.getSelectedFiles();
	            porFiles=sf;
	            defaultDirectory=porFiles[0].getParent();
	            refreshLabels();
	            //String filelist="Nothing";
	            //filelist=sf.getName();
	            //itlbar.setText("ITL file: " + filelist);
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	        }

	      };
	      porButton.addActionListener(poral);

		    ActionListener fecsal=new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		          JFileChooser chooser = new JFileChooser();
		          chooser.setMultiSelectionEnabled(false);
		          chooser.setCurrentDirectory(new File(defaultDirectory));
		          int option = chooser.showOpenDialog(SimulationView.this);
		          if (option == JFileChooser.APPROVE_OPTION) {
		            File sf = chooser.getSelectedFile();
		            fecsFile=sf;
		            defaultDirectory=fecsFile.getParent();
		            refreshLabels();
		            //String filelist="Nothing";
		            //filelist=sf.getName();
		            //itlbar.setText("ITL file: " + filelist);
		          }
		          else {
		            //itlbar.setText("You canceled.");
		          }
		        }

		      };
		      
		      fecsButton.addActionListener(fecsal);
		      
			    ActionListener inital=new ActionListener() {
			        public void actionPerformed(ActionEvent ae) {
			          JFileChooser chooser = new JFileChooser();
			          chooser.setMultiSelectionEnabled(false);
			          chooser.setCurrentDirectory(new File(defaultScriptsDirectory));
			          int option = chooser.showOpenDialog(SimulationView.this);
			          if (option == JFileChooser.APPROVE_OPTION) {
			            File sf = chooser.getSelectedFile();
			            initScript=sf;
			            defaultScriptsDirectory=initScript.getParent();
			            refreshLabels();
			            //String filelist="Nothing";
			            //filelist=sf.getName();
			            //itlbar.setText("ITL file: " + filelist);
			          }
			          else {
			            //itlbar.setText("You canceled.");
			          }
			        }

			      };
			      
			      initButton.addActionListener(inital);
			      
				    ActionListener postal=new ActionListener() {
				        public void actionPerformed(ActionEvent ae) {
				          JFileChooser chooser = new JFileChooser();
				          chooser.setMultiSelectionEnabled(false);
				          chooser.setCurrentDirectory(new File(defaultScriptsDirectory));
				          int option = chooser.showOpenDialog(SimulationView.this);
				          if (option == JFileChooser.APPROVE_OPTION) {
				            File sf = chooser.getSelectedFile();
				            postScript=sf;
				            defaultScriptsDirectory=postScript.getParent();
				            refreshLabels();
				            //String filelist="Nothing";
				            //filelist=sf.getName();
				            //itlbar.setText("ITL file: " + filelist);
				          }
				          else {
				            //itlbar.setText("You canceled.");
				          }
				        }

				      };
				      
				      postButton.addActionListener(postal);


	      ActionListener evtmal=new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		          JFileChooser chooser = new JFileChooser();
		          chooser.setMultiSelectionEnabled(false);
		          chooser.setCurrentDirectory(new File(defaultDirectory));
		          int option = chooser.showOpenDialog(SimulationView.this);
		          if (option == JFileChooser.APPROVE_OPTION) {
		            File sf = chooser.getSelectedFile();
		            evtmFile=sf;
		            defaultDirectory=evtmFile.getParent();
		            refreshLabels();
		            //String filelist="Nothing";
		            //filelist=sf.getName();
		            //itlbar.setText("ITL file: " + filelist);
		          }
		          else {
		            //itlbar.setText("You canceled.");
		          }
		        }

		      };
		      
		      evtmButton.addActionListener(evtmal);

	      ActionListener itlal=new ActionListener() {
	        public void actionPerformed(ActionEvent ae) {
	          JFileChooser chooser = new JFileChooser();
	          chooser.setMultiSelectionEnabled(false);
	          chooser.setCurrentDirectory(new File(defaultDirectory));
	          int option = chooser.showOpenDialog(SimulationView.this);
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            itlFile=sf;
	            defaultDirectory=itlFile.getParent();
	            refreshLabels();
	            //String filelist="Nothing";
	            //filelist=sf.getName();
	            //itlbar.setText("ITL file: " + filelist);
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	        }

	      };
	      
	      itlButton.addActionListener(itlal);
	      
		    ActionListener evfal=new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		          JFileChooser chooser = new JFileChooser();
		          chooser.setMultiSelectionEnabled(false);
		          chooser.setCurrentDirectory(new File(defaultDirectory));

		          int option = chooser.showOpenDialog(SimulationView.this);
		          if (option == JFileChooser.APPROVE_OPTION) {
			            File sf = chooser.getSelectedFile();
			            evtFile=sf;
			            defaultDirectory=evtFile.getParent();

			            refreshLabels();
			            //String filelist="Nothing";
			            //filelist=sf.getName();
		            //evfbar.setText("EVF file: " + filelist);
		          }
		          else {
		            //evfbar.setText("You canceled.");
		          }
		        }

		      };
		      
		      evfButton.addActionListener(evfal);

			    ActionListener resetal=new ActionListener() {
			        public void actionPerformed(ActionEvent ae) {
			        	itlFile=null;
			        	evtFile=null;
			        	porFiles=null;
			        	ptrFile=null;
			        	ptr=null;
			        	pdfmFile=null;
			    	    segmentNames=new String[1];
			    	    segmentNames[0]="PTR_SEGMENT";
			    	    refreshLabels();
			    	    context.reset();
			        	
			        }
			    };
			    
			    resetButton.addActionListener(resetal);
			    
			    ActionListener runal=new ActionRunnable() {
			        public void actionPerformed(ActionEvent ae) {
			        	   Thread thread = new Thread(this);
			        	   thread.start(); 
			        }
			        public void run(){
			        	String messages="";
			        	SuperPor spor=new SuperPor();
			        	//Simulation sim=new Simulation(por);
			        	try{
			        		if (initScript!=null){
			        			context.setInitScript(initScript.getAbsolutePath());
			        			//System.out.println("intit script is NOT null:"+context.getInitScript());
			        		}else {
			        			//System.out.println("intit script is null");
			        		}
			        		if (postScript!=null){
			        			context.setPostScript(postScript.getAbsolutePath());
			        		}

			        		if (itlFile!=null && evtFile!=null){
			        			String defaultDirectory=Properties.getProperty(Properties.DEFAULT_EVT_DIRECTORY);

			        			Por itlpor = ItlParser.parseItl(itlFile.getAbsolutePath(), evtFile.getAbsolutePath(), defaultDirectory, 1);
			        			itlpor.setName(itlFile.getName());
			        			spor.addPor(itlpor);
			        		}
			        		
			        		if (porFiles!=null){
			        			for (int i=0;i<porFiles.length;i++){
			        				Por por = PorUtils.readPORfromFile(porFiles[i].getAbsolutePath());
			        				por.setName(porFiles[i].getName());
			        				spor.addPor(por);
			        			}
			        			if (!((String)ptrbox.getSelectedItem()).equals("PTR_SEGMENT")) spor.setName((String)ptrbox.getSelectedItem());
			        			
			        		}
			        		if (fecsFile!=null){
			        			Fecs fecs=PorUtils.readFecsFromFile(fecsFile.getAbsolutePath());
			        			//SimulationContext.getInstance().ptr=ptr;
			        			//PtrSegment segment=ptr.getSegment((String) ptrbox.getSelectedItem());
			        			/*Iterator<GsPass> it = fecs.getPasses().iterator();
			        			while (it.hasNext()){
			        				GsPass pass = it.next();
			        				Date sDump = pass.getStartDump();
			        				Date eDump=pass.getEndDump();
			        				SimulationContext.getInstance().historyModes.add(sDump.getTime(), "GS_Dump_Possible", "FECS",sDump.getTime());
			        				SimulationContext.getInstance().historyModes.add(eDump.getTime(), "GS_Dump_Off", "FECS",eDump.getTime());
			        				
			        			}*/

			        			context.setFecs(fecs);
			        		}
			        		if (pdfmFile!=null){
			        			Pdfm pdfm=PtrUtils.readPdfmfromFile(pdfmFile.getAbsolutePath());
			        			//SimulationContext.getInstance().ptr=ptr;
			        			//PtrSegment segment=ptr.getSegment((String) ptrbox.getSelectedItem());
			        			/*Iterator<GsPass> it = fecs.getPasses().iterator();
			        			while (it.hasNext()){
			        				GsPass pass = it.next();
			        				Date sDump = pass.getStartDump();
			        				Date eDump=pass.getEndDump();
			        				SimulationContext.getInstance().historyModes.add(sDump.getTime(), "GS_Dump_Possible", "FECS",sDump.getTime());
			        				SimulationContext.getInstance().historyModes.add(eDump.getTime(), "GS_Dump_Off", "FECS",eDump.getTime());
			        				
			        			}*/

			        			context.setPdfm(pdfm);
			        		}

			        		if (evtmFile!=null){
			        			Evtm evtm = PtrUtils.readEvtmFromFile(evtmFile.getAbsolutePath());
			        			EvtmEvent[] events = evtm.getEventsByType(EvtmEvent.EVENT_TYPE_BDI);
			        			/*
			        			 * for event in events:
	newmodes=SimulationContext.getInstance().orcd.getModesAsHistory("EVTM_"+event.getId(),event.getTime().getTime())
	SimulationContext.getInstance().historyModes.putAll(newmodes,"EVTM_"+event.getId(),event.getTime().getTime())

			        			 */
			        			for (int i=0;i<events.length;i++){
			        				EvtmEvent event = events[i];
			        				HashMap<Long, String> newmodes = context.getOrcd().getModesAsHistory("EVTM_"+event.getId(),event.getTime().getTime());
			        				context.getHistoryModes().putAll(newmodes,"EVTM_"+event.getId(),event.getTime().getTime());
			        			}
			        		}
			        		if (ptr!=null){
			        			context.setPtr(ptr);
			        			String segmentName=(String) ptrbox.getSelectedItem();
			        			context.setPlanningPeriod(segmentName);
			        			messages=messages+PtrChecker.checkPtr(ptr);
			        			PtrSegment segment=ptr.getSegment(segmentName);
			        			PointingBlock[] blocks=segment.getBlocks();
			        			for (int i=0;i<blocks.length;i++){
			        					context.getHistoryModes().add(blocks[i].getStartTime().getTime(), "PTR_"+blocks[i].getType(), "PTR", blocks[i].getStartTime().getTime());
			        			}
			        		}
			        		context.setStartDate(spor.getStartDate());
			        		context.setEndDate(spor.getEndDate());
				        	Simulation sim=new Simulation(spor,context);

				        	SimulationContext resultContext = sim.runSimulation();
				        	try{
				        		herschel.ia.gui.kernel.util.VariablesUtil.addVariable("simulationContext"+new Date().getTime(), resultContext);
				        		//herschel.ia.jconsole.jython.Interpreter.getInterpreter().set("simulationContext"+new Date().getTime(), resultContext);
				        		//herschel.ia.jconsole.jython.Interpreter.getInterpreter().
				        		//InterpreterFactory.getInterpreter().set(""+new Date().getTime()+"_simulationContext", resultContext);
				        		//InterpreterNameSpaceUtil.getInstance().getInterpreter().set(""+new Date().getTime()+"_simulationContext", resultContext);
				        	}catch (Exception e){
				        		Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception:"+e.getMessage(),e);
				        		messages=messages+e.getMessage()+"\n";
				        	}
				        	messages=messages+resultContext.getLog();
			        		//messages=messages+sim.run();
			        	}
			        	catch (Exception e){
			        		Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Exception:"+e.getMessage(),e.getMessage());
			        		messages=messages+e.getMessage()+"\n";
			        		e.printStackTrace();
			        	}
			        	//if (messages!=null)
			        	if (!messages.equals("")){
			        		
			        	
			        		MessagesFrame mFrame=new MessagesFrame(messages);
			        		mFrame.setVisible(true);
			        	}
			        }

			      };
			      
			      runButton.addActionListener(runal);
			      itlButton.setMaximumSize(new Dimension(200, 30));
			      evfButton.setMaximumSize(new Dimension(200, 30));
			      porButton.setMaximumSize(new Dimension(200, 30));
			      ptrButton.setMaximumSize(new Dimension(200, 30));
			      ptrLabel.setMaximumSize(new Dimension(200, 30));
			      pdfmButton.setMaximumSize(new Dimension(200, 30));

			      evtmButton.setMaximumSize(new Dimension(200, 30));
			      fecsButton.setMaximumSize(new Dimension(200, 30));
			      initButton.setMaximumSize(new Dimension(200, 30));
			      postButton.setMaximumSize(new Dimension(200, 30));
			      
			      itlbar.setMaximumSize(new Dimension(400,20));
			      evfbar.setMaximumSize(new Dimension(400,20));
			      evfbar.setMaximumSize(new Dimension(400,20));
			      ptrbar.setMaximumSize(new Dimension(400,20));
			      ptrbox.setMaximumSize(new Dimension(400,20));
			      pdfmbar.setMaximumSize(new Dimension(400,20));

			      evtmbar.setMaximumSize(new Dimension(400,20));
			      fecsbar.setMaximumSize(new Dimension(400,20));
			      initbar.setMaximumSize(new Dimension(400,20));
			      postbar.setMaximumSize(new Dimension(400,20));
			      
			        GridLayout gl=new GridLayout(0,2);
			        
			        JPanel panel1=new JPanel();
			        panel1.setLayout(gl);
			        
			        JPanel initButtonPanel=new JPanel();
			        initButtonPanel.setLayout(new BoxLayout(initButtonPanel,BoxLayout.LINE_AXIS));
			        initButtonPanel.add(initButton);
			        panel1.add(initButtonPanel);
			        
			        JPanel initBarPanel=new JPanel();
			        initBarPanel.setLayout(new BoxLayout(initBarPanel,BoxLayout.LINE_AXIS));
			        initBarPanel.add(initbar);
			        panel1.add(initBarPanel);

			        JPanel postButtonPanel=new JPanel();
			        postButtonPanel.setLayout(new BoxLayout(postButtonPanel,BoxLayout.LINE_AXIS));
			        postButtonPanel.add(postButton);
			        panel1.add(postButtonPanel);
			        
			        JPanel postBarPanel=new JPanel();
			        postBarPanel.setLayout(new BoxLayout(postBarPanel,BoxLayout.LINE_AXIS));
			        postBarPanel.add(postbar);
			        panel1.add(postBarPanel);

			        
			        JPanel itlButtonPanel=new JPanel();
			        itlButtonPanel.setLayout(new BoxLayout(itlButtonPanel,BoxLayout.LINE_AXIS));
			        itlButtonPanel.add(itlButton);
			        panel1.add(itlButtonPanel);
			        
			        JPanel itlBarPanel=new JPanel();
			        itlBarPanel.setLayout(new BoxLayout(itlBarPanel,BoxLayout.LINE_AXIS));
			        itlBarPanel.add(itlbar);
			        panel1.add(itlBarPanel);
			        
			        JPanel evfButtonPanel=new JPanel();
			        evfButtonPanel.setLayout(new BoxLayout(evfButtonPanel,BoxLayout.LINE_AXIS));
			        evfButtonPanel.add(evfButton);
			        panel1.add(evfButtonPanel);
			        
			        JPanel evfBarPanel=new JPanel();
			        evfBarPanel.setLayout(new BoxLayout(evfBarPanel,BoxLayout.LINE_AXIS));
			        evfBarPanel.add(evfbar);
			        panel1.add(evfBarPanel);

			        
			        JPanel porButtonPanel=new JPanel();
			        porButtonPanel.setLayout(new BoxLayout(porButtonPanel,BoxLayout.LINE_AXIS));
			        porButtonPanel.add(porButton);
			        panel1.add(porButtonPanel);

			        JPanel porBarPanel=new JPanel();
			        porBarPanel.setLayout(new BoxLayout(porBarPanel,BoxLayout.LINE_AXIS));
			        porBarPanel.add(porbar);
			        panel1.add(porBarPanel);
			        
			        JPanel ptrButtonPanel=new JPanel();
			        ptrButtonPanel.setLayout(new BoxLayout(ptrButtonPanel,BoxLayout.LINE_AXIS));
			        ptrButtonPanel.add(ptrButton);
			        panel1.add(ptrButtonPanel);

			        JPanel ptrBarPanel=new JPanel();
			        ptrBarPanel.setLayout(new BoxLayout(ptrBarPanel,BoxLayout.LINE_AXIS));
			        ptrBarPanel.add(ptrbar);
			        panel1.add(ptrBarPanel);

			        JPanel ptrBoxLabelPanel=new JPanel();
			        ptrBoxLabelPanel.setLayout(new BoxLayout(ptrBoxLabelPanel,BoxLayout.LINE_AXIS));
			        ptrBoxLabelPanel.add(ptrLabel);
			        panel1.add(ptrBoxLabelPanel);

			        JPanel ptrBoxComboPanel=new JPanel();
			        ptrBoxComboPanel.setLayout(new BoxLayout(ptrBoxComboPanel,BoxLayout.LINE_AXIS));
			        ptrBoxComboPanel.add(ptrbox);
			        panel1.add(ptrBoxComboPanel);
			        
			        JPanel pdfmButtonPanel=new JPanel();
			        pdfmButtonPanel.setLayout(new BoxLayout(pdfmButtonPanel,BoxLayout.LINE_AXIS));
			        pdfmButtonPanel.add(pdfmButton);
			        panel1.add(pdfmButtonPanel);

			        JPanel pdfmBarPanel=new JPanel();
			        pdfmBarPanel.setLayout(new BoxLayout(pdfmBarPanel,BoxLayout.LINE_AXIS));
			        pdfmBarPanel.add(pdfmbar);
			        panel1.add(pdfmBarPanel);

			        
			        JPanel evtmButtonPanel=new JPanel();
			        evtmButtonPanel.setLayout(new BoxLayout(evtmButtonPanel,BoxLayout.LINE_AXIS));
			        evtmButtonPanel.add(evtmButton);
			        panel1.add(evtmButtonPanel);

			        JPanel evtmBarPanel=new JPanel();
			        evtmBarPanel.setLayout(new BoxLayout(evtmBarPanel,BoxLayout.LINE_AXIS));
			        evtmBarPanel.add(evtmbar);
			        panel1.add(evtmBarPanel);

			        JPanel fecsButtonPanel=new JPanel();
			        fecsButtonPanel.setLayout(new BoxLayout(fecsButtonPanel,BoxLayout.LINE_AXIS));
			        fecsButtonPanel.add(fecsButton);
			        panel1.add(fecsButtonPanel);

			        JPanel fecsBarPanel=new JPanel();
			        fecsBarPanel.setLayout(new BoxLayout(fecsBarPanel,BoxLayout.LINE_AXIS));
			        fecsBarPanel.add(fecsbar);
			        panel1.add(fecsBarPanel);

			        
			        panel1.setMaximumSize(new Dimension(800,350));
			       // panel1.add(porbar);
			        
			    	BoxLayout bl=new BoxLayout(this,BoxLayout.PAGE_AXIS);
			    	this.setLayout(bl);

	      
	      add(panel1);
	      JPanel panel2=new JPanel();
	      panel2.setLayout(new BoxLayout(panel2,BoxLayout.LINE_AXIS));
	      panel2.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	      panel2.add(Box.createHorizontalGlue());
	      //panel2.add(Box.)
	      panel2.add(runButton);
	      panel2.add(Box.createRigidArea(new Dimension(10, 0)));
	      panel2.add(resetButton);
	      
	      add(panel2);
	      //add(runButton);
	      //add(resetButton);
	      this.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
	      refreshLabels();
    }
	
    private void refreshLabels(){
    	runButton.setEnabled(false);
    	if (initScript==null){
    		initbar.setText("Select an init jython script");
    	}else{
    		initbar.setText(initScript.getName());
    	}
       	if (postScript==null){
    		postbar.setText("Select an post jython script");
    	}else{
    		postbar.setText(postScript.getName());
    	}

		if (pdfmFile==null){
			pdfmbar.setText("Select a PDFM file");
			//ptrbox.setEnabled(false);
		}
		else{
			pdfmbar.setText(pdfmFile.getName());
		}

       	if (fecsFile==null){
			fecsbar.setText("Select a FECS file");
			//ptrbox.setEnabled(false);
		}
		else{
			fecsbar.setText(fecsFile.getName());
		}

    	if (evtmFile==null){
			evtmbar.setText("Select a EVTM file");
			//ptrbox.setEnabled(false);
		}
		else{
			evtmbar.setText(evtmFile.getName());
		}

		if (ptrFile==null){
			ptrbar.setText("Select a PTR/PTSL file");
			ptrbox.setEnabled(false);
		}
		else{
			ptrbar.setText(ptrFile.getName());
			try {
				ptr=PtrUtils.readPTRfromFile(ptrFile.getAbsolutePath());
				segmentNames=ptr.getPtrSegmentNames();
				ptrbox.removeAllItems();
				for (int i=0;i<segmentNames.length;i++){
					ptrbox.addItem(segmentNames[i]);
				}
				ptrbox.setEnabled(true);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ptrbar.setText("Error readinf PTR/PTSL file");
				ptrbox.setEnabled(false);

			}
			//ptrbox.addItem(segmentNames);
			//ptrbox=new JComboBox(segmentNames);
		}

    	if (itlFile==null){
			itlbar.setText("Select a top ITL file");
		}
		else{
			itlbar.setText(itlFile.getName());
		}
		if (evtFile==null){
			evfbar.setText("Select a top EVT file");
		}else{
			evfbar.setText(evtFile.getName());
		}

		if (porFiles==null){
			porbar.setText("Select POR files");
		}else{
			String porNames="";
			for (int i=0;i<porFiles.length;i++){
				porNames=porNames+porFiles[i].getName()+" ";
			}
			if (porNames.length()>20){
				porNames=porNames.substring(0, 19)+"...";
			}
			porbar.setText(porNames);
		}

    	if (itlFile!=null && evtFile!=null){
    		runButton.setEnabled(true);
    		//evfbar.setText("EVT file: " + evtFile.getName());
    		//itlbar.setText("ITL file: " + itlFile.getName());
    		
    	}
    	if (porFiles!=null){
    		runButton.setEnabled(true);
    		
    	}
    }
    
    private class MessagesFrame extends JFrame{
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
                @Override
                public void actionPerformed( ActionEvent e ) {
                	frame.dispose();
                }
            }), BorderLayout.SOUTH);
            this.setContentPane(pnl);
            this.setTitle("Simulation Errors");
    		//this.add(new JLabel(messages));
    		this.pack();
    	}
    }
	private ViewPart _part;
	@Override
	public void selectionChanged(SiteEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public short getPriority() {
		return NORMAL_PRIORITY;
	}

	@Override
	public void makeActions(ActionBarsHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return _part.getIcon();
		//return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return _part.getId();
	}

	@Override
	public ViewPart getPart() {
		// TODO Auto-generated method stub
		return _part;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return _part.getTitle();
	}

	@Override
	public void init(ViewPart arg0) {
		_part=arg0;

	}
	
	private interface ActionRunnable extends ActionListener,Runnable{
		
	}

}
