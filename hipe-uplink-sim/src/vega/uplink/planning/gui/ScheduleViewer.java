package vega.uplink.planning.gui;

import static herschel.ia.gui.kernel.menus.Insert.MAIN;
import static herschel.ia.gui.kernel.menus.Insert.Extension.EDIT_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Extension.FILE_ADDON;
import static herschel.ia.gui.kernel.menus.Insert.Scheme.MENU;
import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.SiteAction;
import herschel.ia.gui.kernel.menus.ActionBars;
import herschel.ia.gui.kernel.menus.Insert;
import herschel.ia.gui.kernel.menus.Retarget;
import herschel.ia.gui.kernel.parts.AbstractEditorAction;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.gui.kernel.util.PopupDialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DropMode;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import rosetta.uplink.pointing.RosettaPtrChecker;
import de.jaret.util.date.JaretDate;
import de.jaret.util.ui.timebars.swing.TimeBarViewer;
import vega.IconResources;
import vega.hipe.gui.xmlutils.HtmlEditorKit;
import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.commanding.Fecs;
import vega.uplink.commanding.Simulation;
import vega.uplink.commanding.SimulationContext;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;
import vega.uplink.planning.aspen.AspenObservationSchedule;
import vega.uplink.planning.period.Plan;
import vega.uplink.pointing.Pdfm;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrSegment;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.exclusion.AbstractExclusion;
import vega.uplink.pointing.gui.PtrXmlEditor;
//import vega.uplink.pointing.gui.PtrXmlEditor.PrintAction;

public class ScheduleViewer extends AbstractVariableEditorComponent<Schedule> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(ScheduleViewer.class.getName());
	ObservationPanel buttonPanel;
	JList<Observation> list;
	
	Schedule schedule;
	TimeBarViewer tbv;
	ScheduleModel model;
	public ScheduleViewer(){
		super(new BorderLayout());
	}
	
	public void refresh(){
		model= new ScheduleModel(schedule);
		model.viewer=this;
		tbv.setModel(model);
	}
	public void init(){
		JPanel globalPanel = new JPanel(new BorderLayout());
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel,BoxLayout.LINE_AXIS));
		JButton saveSchedule=new JButton("Save Schedule");
		saveSchedule.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	saveAs();
    		 
    			

            }
        });
		JButton quickCheckPtr=new JButton("Check PTR sanity");
		
		quickCheckPtr.addActionListener(new ActionListener() {
	        	 
	            public void actionPerformed(ActionEvent e)
	            {
	            	checkPtrSanity();
	    		 
	    			

	            }
	        });
		JButton fdCheckPtr=new JButton("Check PTR FD");
		fdCheckPtr.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	checkFD();
    		 
    			

            }
        });
		
		JButton generateMapps=new JButton("Generate Mapps Files");
		generateMapps.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	generateMapps();
    		 
    			

            }
        });
		
		JButton commandingCheck=new JButton("Commanding ORCD check");
		commandingCheck.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
				ORCDCheck(); 
				
    		 
    			

            }
        });
		JCheckBox recalculate = new JCheckBox("Recalculate");
		recalculate.setSelected(true);
		recalculate.addActionListener(new ActionListener() {
       	 
            public void actionPerformed(ActionEvent e)
            {
            	model.recalculate=((JCheckBox)e.getSource()).isSelected();
            	model.commandingChanged(null);
            	model.recualculatePtr();
				
    		 
    			

            }
        });
		topPanel.add(saveSchedule);
		topPanel.add(quickCheckPtr);
		topPanel.add(fdCheckPtr);
		topPanel.add(commandingCheck);
		topPanel.add(generateMapps);
		topPanel.add(recalculate);
		topPanel.add(Box.createHorizontalGlue());
		
		JPanel midPanel=new JPanel();
		midPanel.setLayout(new BorderLayout());
		JaretDate.setJaretDateFormatter(new DateFormatter());
		model= new ScheduleModel(schedule);
		ScheduleTimeBarViewer tbv = new ScheduleTimeBarViewer(model);
        tbv.setTimeScaleRenderer(new PlanningTimeScaleRenderer());
        tbv.setPixelPerSecond(0.005);
        buttonPanel = new ObservationPanel();
        tbv.getSelectionModel().setIntervalSelectionAllowed(true);
        tbv.setStartDate(new JaretDate(schedule.getPtslSegment().getStartDate().toDate()));
        tbv.setTitle(schedule.getPtslSegment().getName());
        midPanel.add(tbv,BorderLayout.CENTER);
        list = new ObservationList(new ObservationListModel(schedule));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setDropMode(DropMode.ON);
        list.setSelectedIndex(0);
        list.setVisibleRowCount(5);
        list.addListSelectionListener(new ListSelectionListener(){
        	
        	public void valueChanged(ListSelectionEvent e) {
        	    if (e.getValueIsAdjusting() == false) {

        	        if (list.getSelectedIndex() == -1) {
        	        //No selection, disable fire button.

        	        } else {
        	        //Selection, enable the fire button.
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
        JButton plus=new JButton("+");
        plus.addActionListener(new ActionListener() {
	       	 
            public void actionPerformed(ActionEvent e)
            {
 				try {
 					Observation newObs = Observation.getDefaultObservation();
 					long duration=newObs.getDurationMilliSecs();
 					newObs.setObsStartDate(schedule.getPtslSegment().getSegmentStartDate());
 					newObs.setObsEndDate(new Date(schedule.getPtslSegment().getSegmentStartDate().getTime()+duration));
 					schedule.addObservation(newObs);
 					list.setSelectedValue(newObs, true);
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
 					Observation obsToDel = list.getSelectedValue();
 					//int index=list.getSelectedIndex();
 					//LOG.info("Request to delete the observation index "+index);
 					//schedule.removeObservation(index);
 					schedule.removeObservation(obsToDel);
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
 					schedule.addObservation(newObs);
 					Observation.LISTEN=oldListen;
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
 					long oldDuration = oldObs.getDurationMilliSecs();
 					newObs.setObsStartDate(oldObs.getObsEndDate());
 					newObs.setObsEndDate(new Date(oldObs.getObsEndDate().getTime()+oldDuration));
 					schedule.addObservation(newObs);
 					Observation.LISTEN=oldListen;
 					list.setSelectedValue(newObs, true);
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
        listPanel.add(listButtonsPanel);
        tbv.registerTimeBarRenderer(ObservationInterval.class, new PlanningTimeBarRenderer(list));
        tbv.registerTimeBarRenderer(CommandingInterval.class, new PlanningTimeBarRenderer(list));
        tbv.registerTimeBarRenderer(BlockInterval.class, new PlanningTimeBarRenderer(list));
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
        globalPanel.add(splitPane,BorderLayout.CENTER);
        buttonPanel.setObservation(list.getSelectedValue());
        splitPane.setDividerLocation(0.75);
        this.add(new BottomPanel(this, globalPanel));
        
		ActionBars menus = getArea().getActionBars(getId());
		SiteAction saveAs = new AbstractEditorAction("SaveAs") {
            @Override public void actionPerformed(ActionEvent e) {
                saveAs();
            }
        };
		SiteAction saveAsAspenSchedule = new AbstractEditorAction("SaveAsAspenSchedule") {
            @Override public void actionPerformed(ActionEvent e) {
                saveAsAspenSch();
            }
        };

        SiteAction save = new AbstractEditorAction("Save") {
            @Override public void actionPerformed(ActionEvent e) {
                save();
            }
        };
		SiteAction checkFD = new AbstractEditorAction("CheckPtrFd") {
            @Override public void actionPerformed(ActionEvent e) {
                checkFD();
            }
        };
		SiteAction checkSanity = new AbstractEditorAction("CheckPtrSanity") {
            @Override public void actionPerformed(ActionEvent e) {
                checkPtrSanity();
            }
        };
		SiteAction gMapps = new AbstractEditorAction("GenerateMappsFiles") {
            @Override public void actionPerformed(ActionEvent e) {
                generateMapps();
            }
        };
        
		SiteAction orcdCheck = new AbstractEditorAction("OrcdCheck") {
            @Override public void actionPerformed(ActionEvent e) {
                ORCDCheck();
            }
        };

		SiteAction savePtrAs = new AbstractEditorAction("SavePtrAs") {
            @Override public void actionPerformed(ActionEvent e) {
                savePtrAs();
            }
        };

		SiteAction savePors = new AbstractEditorAction("SavePors") {
            @Override public void actionPerformed(ActionEvent e) {
                savePors();
            }
        };

        
		menus.retarget(Retarget.SAVE_AS, saveAs);
		menus.retarget(Retarget.SAVE, save);
        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), saveAsAspenSchedule);
		
        //menus.insert(new Insert(MENU, MAIN, FILE_ADDON), saveastxt);

        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), checkFD);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), checkSanity);
        menus.insert(new Insert(MENU, MAIN, EDIT_ADDON), orcdCheck);
        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), gMapps);
        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), savePtrAs);
        menus.insert(new Insert(MENU, MAIN, FILE_ADDON), savePors);

	}
	
	public void setSchedule(Schedule schedule){
		this.schedule=schedule;
		init();
	}
    public boolean makeEditorContent() {
    	setSchedule(getValue());
    	this.setGlobalScroll(false);
    	return true;
    }


	
	public Icon getComponentIcon() {
        try {
            URL resource = PtrXmlEditor.class.getResource(IconResources.HUS_ICON);
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
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }
    
	public void saveAs(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(schedule.getPath()+"/"+schedule.getFileName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            schedule.setFileName(sf.getName());
	            schedule.setPath(sf.getParent());
 
	            save();
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}	
	}
	
	public void saveAsAspenSch(){
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(schedule.getPath()+"/"+"ObservationSchedule.xml"));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            //schedule.setFileName(sf.getName());
	            //schedule.setPath(sf.getParent());
 
	            AspenObservationSchedule.saveAspenScheduleToFile(sf.getAbsolutePath(), schedule);
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}	
	}
	
	public void save(){
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
	
	public void ORCDCheck(){
		try {
			SimulationContext context=new SimulationContext();
			Ptr ptr = schedule.getPtr();
			context.setPtr(ptr);
			String segmentName=(String) schedule.getPtslSegment().getName();
			context.setPlanningPeriod(segmentName);
			PtrSegment segment=ptr.getSegments()[0];
			PointingBlock[] blocks=segment.getBlocks();
			if (blocks.length==0) LOG.info("PTR has no blocks");
			for (int i=0;i<blocks.length;i++){
					context.getHistoryModes().add(blocks[i].getStartTime().getTime(), "PTR_"+blocks[i].getType(), "PTR", blocks[i].getStartTime().getTime());
			}
			Fecs fecs = schedule.getFecs();
			if (fecs!=null) context.setFecs(fecs);
			Simulation simulation=new Simulation(schedule.getPor(),context);
			SimulationContext resultContext = simulation.runOnlyText(false);
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
	public void savePors(){
		String path=null;
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }catch (Exception e3){
	        	//chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            path=sf.getAbsolutePath();
	            ObservationUtil.savePors(path, schedule);
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

      } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					return;
				}

	}
	
	public void generateMapps(){
        String name = PopupDialog.askForInput(ScheduleViewer.this, "Enter sufix for the files (example 01_P_RSM0PIM0):");
        String path=null;
        if (name == null) {
            return;
        }
        try{
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }catch (Exception e3){
	        	//chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            path=sf.getAbsolutePath();
	            
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

        } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					return;
				}
        Ptr ptr = schedule.getPtr();
        Pdfm pdfm= schedule.getPdfm();
        if (pdfm==null) {
				JOptionPane.showMessageDialog(ScheduleViewer.this,
					    "There is no PDFM defined in the schedule",
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				return;
        }
        String mtp=schedule.getPtslSegment().getName().replace("MTP_", "");
        String ptrName="PTRM_DM_"+mtp+"_"+name+".ROS";
        if (schedule.getFileName()==null) schedule.setFileName("SCH_DM_"+mtp+"_"+name+".ROS");
        ptr.setName(ptrName);
        if (pdfm!=null){
        	String pdfmName=ptrName.replace("PTRM_", "");
        	pdfmName="PDFM_"+pdfmName;
        	pdfm.setName(pdfmName);
        	pdfm.setPath(path);
    		PtrSegment seg = ptr.getSegments()[0];
    		String[] includes={"FDDF.xml",pdfmName};
    		seg.setIncludes(includes);
    		PtrUtils.savePDFM(pdfm);
        }
        
        PtrUtils.writePTRtofile(path+"/"+ptrName, ptr);
        Plan plan = schedule.getPlan();
        try {
        	if (plan==null){
        		ObservationUtil.saveMappsProducts(path+"/"+name, schedule);
        	}else{
        		ObservationUtil.saveMappsProducts(path+"/"+name, schedule,plan);
        	}
		} catch (IOException e2) {
			
				JOptionPane.showMessageDialog(ScheduleViewer.this,
					    e2.getMessage(),
					    "Error",
					    JOptionPane.ERROR_MESSAGE);
				e2.printStackTrace();
				return;

			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
	}
	
	public void checkFD(){
		try {
			Ptr ptsl = new Ptr();
			ptsl.addSegment( schedule.getPtslSegment());
			Pdfm pdfm=schedule.getPdfm();
			Fecs fecs=schedule.getFecs();
			AbstractExclusion exclusion = schedule.getExclusion();
			String result="";
			if (fecs==null && exclusion==null) result = RosettaPtrChecker.checkPtrHTML(schedule.getPtr(), ptsl,pdfm);
			if (fecs!=null && exclusion==null) result = RosettaPtrChecker.checkPtrHTML(schedule.getPtr(), ptsl,pdfm,fecs);
			if (fecs!=null && exclusion!=null) result = RosettaPtrChecker.checkPtrHTML(schedule.getPtr(), ptsl,pdfm,fecs,exclusion);
	       	result="<html><body>"+result+"</body><html>";
        	LOG.warning(result);
        	HtmlEditorKit htmlEditor=new HtmlEditorKit("PTR FD Check",result);

		}
		catch (Exception e1) {
			JOptionPane.showMessageDialog(ScheduleViewer.this,
				    e1.getMessage(),
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
			e1.printStackTrace();
		} 
	}
	
	public void checkPtrSanity(){
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
	
	public void savePtr(){
        Ptr ptr;
		try{


			
				ptr=schedule.getPtr();
				String warnings = PtrChecker.checkPtr(ptr);
				
				if (!warnings.equals("")) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
							warnings,
						    "Warning",
						    JOptionPane.WARNING_MESSAGE);
				}

        	PtrUtils.savePTR(ptr);
	            

	         

        } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
				}		
	}
	public void savePtrAsTxt(){
		Ptr ptr;
        try{
        	  ptr=schedule.getPtr();
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(ptr.getPath()+"/"+ptr.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            ptr.setName(sf.getName());
	            ptr.setPath(sf.getParent());
 
	    		try{
	    			PrintWriter writer = new PrintWriter(sf.getAbsolutePath(), "UTF-8");
	    			writer.print(ptr.toXml());
	    			writer.close();
	    		}catch (Exception e){
	    			e.printStackTrace();
	    		}
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					//editor.setText(ptr.toXml());
				}	
	}
	public void savePtrAs(){
		Ptr ptr;
        try{
        	  ptr=schedule.getPtr();
              String mtp=schedule.getPtslSegment().getName().replace("MTP_", "");
              String ptrName="PTRM_DM_"+mtp+".ROS";
              
              ptr.setName(ptrName);
	          JFileChooser chooser = new JFileChooser();
	          try{
	        	  chooser.setSelectedFile(new File(ptr.getPath()+"/"+ptr.getName()));
	          }catch (Exception e3){
	        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_PLANNING_DIRECTORY)));
	          }
	          chooser.setMultiSelectionEnabled(false);
	          
	          int option = chooser.showSaveDialog(ScheduleViewer.this);
	          //File sf;
	          if (option == JFileChooser.APPROVE_OPTION) {
	            File sf = chooser.getSelectedFile();
	            ptr.setName(sf.getName());
	            ptr.setPath(sf.getParent());
 
	            savePtr();
	            
	          }
	          else {
	            //itlbar.setText("You canceled.");
	          }
	         

          } catch (Exception e1) {
					JOptionPane.showMessageDialog(ScheduleViewer.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					//editor.setText(ptr.toXml());
				}	
	}
}
