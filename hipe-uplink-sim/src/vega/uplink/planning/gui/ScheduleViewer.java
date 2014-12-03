package vega.uplink.planning.gui;

//import EventInterval;

import herschel.ia.dataset.DatasetEvent;
import herschel.ia.dataset.Product;
import herschel.ia.dataset.ProductListener;
import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JOptionPane;
//import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.w3c.dom.Document;

import rosetta.uplink.pointing.RosettaPtrChecker;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.model.TimeBarModel;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import vega.uplink.Properties;
import vega.uplink.commanding.AbstractSequence;
import vega.uplink.commanding.HistoryModes;
import vega.uplink.commanding.Orcd;
import vega.uplink.commanding.PorChecker;
import vega.uplink.commanding.Sequence;
import vega.uplink.commanding.SequenceProfile;
import vega.uplink.commanding.Simulation;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.commanding.task.PorCheckTask;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.ObservationUtil;
//import vega.uplink.commanding.PorUtils;
import vega.uplink.planning.Schedule;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.PointingBlocksSlice;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.gui.PointingBlocksSliceXmlEditor;
import vega.uplink.pointing.gui.PtrXmlEditor;
import vega.uplink.pointing.gui.xmlutils.HtmlEditorKit;
//import rosetta.

public class ScheduleViewer extends AbstractVariableEditorComponent<Schedule> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ScheduleViewer.class.getName());
	ObservationPanel buttonPanel;
	JList<Observation> list;
	
	Schedule schedule;
	//XMLTextEditor editor;
	//JEditorPane editor;
	TimeBarViewer tbv;
	ScheduleModel model;
	public ScheduleViewer(){
		super(new BorderLayout());
		//this.setGlobalScroll(false);
	}
	
	public void refresh(){
		model= new ScheduleModel(schedule);
		tbv.setModel(model);
	}
	public void init(){
		JPanel globalPanel = new JPanel(new BorderLayout());
		//LOG.info("Starting init");
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.LINE_AXIS));
		JButton saveSchedule=new JButton("Save Schedule");
		saveSchedule.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	//String text = editor.getText();
				try {
					ObservationUtil.saveScheduleToFile(schedule.getPath()+"/"+schedule.getFileName(), schedule);

				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });
		JButton quickCheckPtr=new JButton("Check PTR sanity");
		
		quickCheckPtr.addActionListener(new ActionListener() {
	        	 
	            public void actionPerformed(ActionEvent e)
	            {
	            	//String text = editor.getText();
					try {
						Ptr ptsl = new Ptr();
						ptsl.addSegment( schedule.getPtslSegment());
				       	String result = PtrChecker.checkPtrHTML(schedule.getPtr(), ptsl);
				       	result="<html><body>"+result+"</body><html>";
			        	LOG.warning(result);
			        	HtmlEditorKit htmlEditor=new HtmlEditorKit("PTR sanity Check",result);

					}
					catch (Exception e1) {
						JOptionPane.showMessageDialog(ScheduleViewer.this,
							    e1.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
					} 
	    		 
	    			

	            }
	        });
		JButton fdCheckPtr=new JButton("Check PTR FD");
		fdCheckPtr.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	//String text = editor.getText();
				try {
					Ptr ptsl = new Ptr();
					ptsl.addSegment( schedule.getPtslSegment());
					Pdfm pdfm=schedule.getPdfm();
			       	String result = RosettaPtrChecker.checkPtrHTML(schedule.getPtr(), ptsl,pdfm);
			       	result="<html><body>"+result+"</body><html>";
		        	LOG.warning(result);
		        	HtmlEditorKit htmlEditor=new HtmlEditorKit("PTR sanity Check",result);

				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });
		JButton commandingCheck=new JButton("Commanding ORCD check");
		commandingCheck.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	//String text = editor.getText();
				try {
					SimulationContext context=new SimulationContext();
					//context.getPor().addPor(schedule.getPor());
					Ptr ptr = schedule.getPtr();
        			context.setPtr(ptr);
        			String segmentName=(String) schedule.getPtslSegment().getName();
        			context.setPlanningPeriod(segmentName);
        			//messages=messages+PtrChecker.checkPtr(ptr);
        			PtrSegment segment=ptr.getSegments()[0];
        			PointingBlock[] blocks=segment.getBlocks();
        			if (blocks.length==0) LOG.info("PTR has no blocks");
        			for (int i=0;i<blocks.length;i++){
        					context.getHistoryModes().add(blocks[i].getStartTime().getTime(), "PTR_"+blocks[i].getType(), "PTR", blocks[i].getStartTime().getTime());
        			}

					//context.setPtr(schedule.getPtr());
					Simulation simulation=new Simulation(schedule.getPor(),context);
					SimulationContext resultContext = simulation.runSimulation();
					String messages="";
					messages=messages+resultContext.getLog();
					messages=messages.replace("\n", "\n<br>");
		        	HtmlEditorKit htmlEditor=new HtmlEditorKit("Commanding Check",messages);

				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
				
    		 
    			

            }
        });
		JCheckBox recalculate = new JCheckBox("Recalculate");
		recalculate.setSelected(true);
		/*JButton recalculatePTR=new JButton("Recalculate PTR");*/
		recalculate.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	//String text = editor.getText();
            	model.recalculate=((JCheckBox)e.getSource()).isSelected();
            	model.commandingChanged(null);
            	model.recualculatePtr();
				
    		 
    			

            }
        });
		/*JButton createObservation=new JButton("Create Observation");
		createObservation.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					Observation newObs = Observation.getDefaultObservation();
 					schedule.addObservation(newObs);
 					buttonPanel.setObservation(newObs);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });	*/	
		topPanel.add(saveSchedule);
		topPanel.add(quickCheckPtr);
		topPanel.add(fdCheckPtr);
		topPanel.add(commandingCheck);
		topPanel.add(recalculate);
		//topPanel.add(recalculatePTR);
		//topPanel.add(createObservation);

		topPanel.add(Box.createHorizontalGlue());
		
		JPanel midPanel=new JPanel();
		midPanel.setLayout(new BorderLayout());
		JaretDate.setJaretDateFormatter(new DateFormatter());
		//LOG.info("Creating model");
		//TimeBarModel model= new ScheduleModel(schedule);
		model= new ScheduleModel(schedule);
		//LOG.info("finsih Creating model");
        //TimeBarViewer tbv = new TimeBarViewer(model);
		ScheduleTimeBarViewer tbv = new ScheduleTimeBarViewer(model);
        tbv.setTimeScaleRenderer(new PlanningTimeScaleRenderer());
        //System.out.println(tbv.getTimeScaleRenderer());
        tbv.setPixelPerSecond(0.005);
        buttonPanel = new ObservationPanel();
        //tbv.registerTimeBarRenderer(ObservationInterval.class, new PlanningTimeBarRenderer(buttonPanel));
       // tbv.registerTimeBarRenderer(CommandingInterval.class, new PlanningTimeBarRenderer(buttonPanel));

        tbv.getSelectionModel().setIntervalSelectionAllowed(true);
        //tbv.setTimeBarRenderer(new PlanningTimeBarRenderer());
        tbv.setStartDate(new JaretDate(schedule.getPtslSegment().getStartDate().toDate()));
        tbv.setTitle(schedule.getPtslSegment().getName());
        
        /*Action action = new AbstractAction("IntervalAction") {
  
			private static final long serialVersionUID = 1L;

			public void actionPerformed(ActionEvent e) {
				
                System.out.println("run " + getValue(NAME));
            }
        };
        JPopupMenu pop = new JPopupMenu("Operations");
        pop.add(action);
        tbv.registerPopupMenu(ObservationInterval.class, pop);*/
        
        //ObservationPanel buttonPanelbuttonPanel = new ObservationPanel();

        midPanel.add(tbv,BorderLayout.CENTER);
        JScrollPane westPanel = new JScrollPane();
        Vector<Observation> vector = new Vector<Observation>();
        Observation[] obs = schedule.getObservations();
        //DefaultListModel<Observation> listModel = new DefaultListModel<Observation>();
        //list = new JList<Observation>(new ObservationListModel(schedule));
        list = new ObservationList(new ObservationListModel(schedule));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setDropMode(DropMode.ON);
        //list.setTransferHandler(new ObservationListTransferHandler());
        list.setSelectedIndex(0);
        //list.addListSelectionListener(this);
        list.setVisibleRowCount(5);
        //ListSelectionEvent
        list.addListSelectionListener(new ListSelectionListener(){
        	
        	public void valueChanged(ListSelectionEvent e) {
        	    if (e.getValueIsAdjusting() == false) {

        	        if (list.getSelectedIndex() == -1) {
        	        //No selection, disable fire button.
        	            //fireButton.setEnabled(false);

        	        } else {
        	        //Selection, enable the fire button.
        	            //fireButton.setEnabled(true);
        	        	buttonPanel.setObservation(list.getSelectedValue());
        	        }
        	    }
        	}
        });
        JScrollPane listScrollPane = new JScrollPane(list);
        JPanel listPanel=new JPanel();
        BoxLayout listPanelLayout = new BoxLayout(listPanel,BoxLayout.PAGE_AXIS);
        listPanel.setLayout(listPanelLayout);
        listPanel.add(listScrollPane);
        JPanel listButtonsPanel=new JPanel();
        BoxLayout listButtonsPanelLayout=new BoxLayout(listButtonsPanel,BoxLayout.PAGE_AXIS);
        JButton plus=new JButton("+");
        plus.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					Observation newObs = Observation.getDefaultObservation();
 					schedule.addObservation(newObs);
 					list.setSelectedValue(newObs, true);
 					//buttonPanel.setObservation(newObs);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });		
        
        JButton minus=new JButton("-");
        minus.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					int index=list.getSelectedIndex();
 					//Observation obstoremove = list.getSelectedValue();
 					//Observation newObs = Observation.getDefaultObservation();
 					schedule.removeObservation(index);
 					//buttonPanel.setObservation(newObs);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });		
        JButton split=new JButton("/");
        split.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					boolean oldListen = Observation.LISTEN;
 					Observation.LISTEN=false;
 					Observation oldObs = list.getSelectedValue();
 					Observation newObs = oldObs.copy();
 					long oldDuration = oldObs.getDurationMilliSecs();
 					long newDuration = oldDuration/2;
 					
 					Date oldObsStartDate = oldObs.getObsStartDate();
 					Date oldObsEndDate = oldObs.getObsEndDate();
 					oldObs.setObsEndDate(new Date(oldObsStartDate.getTime()+newDuration));
 					newObs.setObsStartDate(new Date(oldObsEndDate.getTime()-newDuration));
 					//oldObs.setObsEndDate(old);
 					//int index=list.getSelectedIndex();
 					//Observation obstoremove = list.getSelectedValue();
 					//Observation newObs = Observation.getDefaultObservation();
 					//schedule.removeObservation(index);
 					schedule.addObservation(newObs);
 					Observation.LISTEN=oldListen;
 					//buttonPanel.setObservation(newObs);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });
        JButton copy=new JButton("C");
        copy.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					boolean oldListen = Observation.LISTEN;
 					Observation.LISTEN=false;
 					Observation oldObs = list.getSelectedValue();
 					Observation newObs = oldObs.copy();

 					schedule.addObservation(newObs);
 					Observation.LISTEN=oldListen;
 					list.setSelectedValue(newObs, true);
 					//buttonPanel.setObservation(newObs);
				}
				catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				} 
    		 
    			

            }
        });
        listButtonsPanel.setMaximumSize(new Dimension(400,40));
        listButtonsPanel.add(plus);
        listButtonsPanel.add(minus);
        listButtonsPanel.add(split);
        listButtonsPanel.add(copy);
        //listButtonsPanel.add(Box.createHorizontalGlue());
        
        //listPanel.add(Box.createVerticalGlue());
        listPanel.add(listButtonsPanel);
        tbv.registerTimeBarRenderer(ObservationInterval.class, new PlanningTimeBarRenderer(list));
        tbv.registerTimeBarRenderer(CommandingInterval.class, new PlanningTimeBarRenderer(list));
        tbv.registerTimeBarRenderer(BlockInterval.class, new PlanningTimeBarRenderer(list));
        
        /*for (int i=0;i<obs.length;i++){
        	listModel.addElement(obs[i]);
        }*/
        JPanel upPanel=new JPanel(new BorderLayout());
        JPanel downPanel=new JPanel(new BorderLayout());
        upPanel.add(topPanel,BorderLayout.NORTH);
        upPanel.add(listPanel,BorderLayout.WEST);
        
        upPanel.add(midPanel,BorderLayout.CENTER);
        downPanel.add(buttonPanel,BorderLayout.CENTER);
		
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                upPanel,
                downPanel);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.75);
        
        //this.add(splitPane,BorderLayout.CENTER);
        //globalPanel.add(new BottomPanel(xmlPanel, xmlEditor))
        globalPanel.add(splitPane,BorderLayout.CENTER);
        buttonPanel.setObservation(list.getSelectedValue());
        splitPane.setDividerLocation(0.75);
        this.add(new BottomPanel(this, globalPanel));
        //JPanel upPanel=new JPanel(new BorderLayout());
        /*this.add(topPanel,BorderLayout.NORTH);
        this.add(listPanel,BorderLayout.WEST);
        
		this.add(midPanel,BorderLayout.CENTER);
		this.add(buttonPanel,BorderLayout.SOUTH);
		buttonPanel.setObservation(list.getSelectedValue());*/
        //LOG.info("End init");
	}
	
	public void setSchedule(Schedule schedule){
		this.schedule=schedule;
		//schedule.addObservationListener(this);
		init();
	}
    public boolean makeEditorContent() {
    	setSchedule(getValue());
    	this.setGlobalScroll(false);
    	return true;
    }


	
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
        try {
            URL resource = PtrXmlEditor.class.getResource("/vega/vega.gif");
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends Schedule> getVariableType() {
		return Schedule.class;
		// TODO Auto-generated method stub
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }

    

	/*@Override
	public void targetChanged(DatasetEvent<Product> arg0) {
		LOG.info("Schedule changed");
		System.out.println("Schedule changed");
		// TODO Auto-generated method stub
		refresh();
		
	}*/

	/*@Override
	public void observationChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		LOG.info("Schedule changed");
		System.out.println("Schedule changed");
		// TODO Auto-generated method stub
		refresh();		
	}*/
    
    

}
