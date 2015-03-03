package vega.uplink.planning.gui;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import herschel.ia.gui.apps.components.util.BottomPanel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jfree.util.Log;
import org.w3c.dom.Document;

import vega.uplink.DateUtil;
import vega.uplink.Properties;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.ObservationPointingSlice;
import vega.uplink.planning.ObservationPor;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.pointing.PointingBlock;
import vega.uplink.pointing.gui.PointingBlocksSliceXmlEditor;
import vega.uplink.pointing.gui.PtrXmlEditor;

public class ObservationPanel extends JTabbedPane implements ObservationListener{
//public class ObservationPanel{
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		Observation obs=null;
		JTextField _fieldA;
		JTextField _fieldB;
		JTextField _fieldC;
		JTextField _fieldD;
		JTextField _fieldE;
		JTextField _fieldF;
		JButton saveButton;
		JLabel    insLabel2;
		JPanel metadataPanel;
		//JPanel xmlPanel;
		JPanel pointingPanel;
		//JPanel sequencesPanel;
		JPanel itlPanel;
		//ObservationEditor xmlEditor;
		ObservationPointingSliceXmlEditor pointingEditor;
		//ObservationPorXmlEditor sequencesEditor;
		ObservationItlEditor itlEditor;
		boolean initialized=false;
		boolean disableRefresh=false;
		private final Logger LOG = Logger.getLogger(ObservationPanel.class.getName());
		//JScrollPane sp;
		//JScrollPane sp;
		//JPanel commandingPanel = new JPanel();
		
		public ObservationPanel(){
			super();
			metadataPanel=new JPanel();
			//xmlPanel=new JPanel();
			pointingPanel=new JPanel();
			//sequencesPanel=new JPanel();
			itlPanel=new JPanel();

			this.addTab("Metadata", metadataPanel);
			//xmlEditor= new ObservationEditor();

			//BoxLayout xmlLayout = new BoxLayout(xmlPanel,BoxLayout.PAGE_AXIS);
			//xmlPanel.setLayout(xmlLayout);
			

			//xmlPanel.add(new BottomPanel(xmlPanel, xmlEditor));


			//this.addTab("XML", new BottomPanel(this, xmlPanel));
			
			pointingEditor=new ObservationPointingSliceXmlEditor();
			BoxLayout pointingLayout = new BoxLayout(pointingPanel,BoxLayout.PAGE_AXIS);
			pointingPanel.setLayout(pointingLayout);			
			pointingPanel.add(new BottomPanel(pointingPanel, pointingEditor));
			this.addTab("Pointing", new BottomPanel(this, pointingPanel));

			//sequencesEditor=new ObservationPorXmlEditor();
			//BoxLayout sequencesLayout = new BoxLayout(sequencesPanel,BoxLayout.PAGE_AXIS);
			//sequencesPanel.setLayout(sequencesLayout);			
			//sequencesPanel.add(new BottomPanel(sequencesPanel, sequencesEditor));
			//this.addTab("Commanding", new BottomPanel(this, sequencesPanel));
			
			itlEditor=new ObservationItlEditor();
			BoxLayout itlLayout = new BoxLayout(itlPanel,BoxLayout.PAGE_AXIS);
			itlPanel.setLayout(itlLayout);			
			itlPanel.add(new BottomPanel(itlPanel, itlEditor));
			this.addTab("Itl", new BottomPanel(this, itlPanel));

			this.setObservation(new Observation(new Date(),new Date()));

			

		}
		public void refreshMetadata(){
			if (!disableRefresh){
				_fieldA.setText(obs.getName());
				_fieldB.setText(DateUtil.dateToZulu(obs.getObsStartDate()));
				_fieldC.setText(DateUtil.dateToZulu(obs.getObsEndDate()));
				_fieldD.setText(""+obs.getInstrument());
				_fieldE.setText(""+obs.getDescription());
				 _fieldF.setText(""+obs.getCreator());
			}
		}

		public void init(){


			metadataPanel.removeAll();
			

	        GroupLayout layout = new GroupLayout(metadataPanel);
	        metadataPanel.setLayout(layout);
	        layout.setAutoCreateGaps(true);
	        layout.setAutoCreateContainerGaps(true);
	        SequentialGroup hGroup = layout.createSequentialGroup();
	        SequentialGroup vGroup = layout.createSequentialGroup();
	        ParallelGroup hLabelGroup = layout.createParallelGroup();
	        ParallelGroup hComboGroup = layout.createParallelGroup();
	        ParallelGroup vPropsGroup;



	        JLabel    labelA = new JLabel("Observation Name:");
	        _fieldA = new JTextField();
	        _fieldA.setText(obs.getName());
	        _fieldA.setMaximumSize(new Dimension(400,20));
	        
	        ActionListener listener=new ActionListener() {
		        public void actionPerformed(ActionEvent ae) {
		        	System.out.println("action performed"+ae);
		        	try {
		        		disableRefresh=true;
		        		final Logger LOG = Logger.getLogger(this.getClass().getName());
		        		LOG.info("Setting new metadata");
		        		//System.out.println("Setting new metadata");
		        		boolean oldListen = Observation.LISTEN;
		        		Observation.LISTEN=false;
			        	obs.setName(_fieldA.getText());
			        	obs.setObsStartDate(DateUtil.zuluToDate(_fieldB.getText()));
			        	obs.setObsEndDate(DateUtil.zuluToDate(_fieldC.getText()));
			        	obs.setInstrument(_fieldD.getText());
			        	obs.setDescription(_fieldE.getText());
			        	obs.setCreator(_fieldF.getText());
			        	Observation.LISTEN=oldListen;
			        	obs.metaChange(null);
			        	disableRefresh=false;
					} catch (ParseException e) {
						e.printStackTrace();
						_fieldB.setText(DateUtil.dateToZulu(obs.getObsStartDate()));
						_fieldC.setText(DateUtil.dateToZulu(obs.getObsEndDate()));
						IllegalArgumentException iae = new IllegalArgumentException(e.getMessage());
						iae.initCause(e);
						throw(iae);
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
		        	//insLabel2.setText(seq.getInstrument());
		        }

		      };
	        _fieldA.addActionListener(listener);
	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelA).addComponent(_fieldA));
	        hLabelGroup.addComponent(labelA);
	        hComboGroup.addComponent(_fieldA);

	        JLabel    labelB = new JLabel("Observation Start Time:");
	        _fieldB = new JTextField();
	        _fieldB.setText(DateUtil.dateToZulu(obs.getObsStartDate()));
	        _fieldB.setMaximumSize(new Dimension(400,20));
	        _fieldB.addActionListener(listener);

	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelB).addComponent(_fieldB));
	        hLabelGroup.addComponent(labelB);
	        hComboGroup.addComponent(_fieldB);

	        JLabel    labelC = new JLabel("Observation End Time:");
	        _fieldC = new JTextField();
	        _fieldC.setText(DateUtil.dateToZulu(obs.getObsEndDate()));
	        _fieldC.setMaximumSize(new Dimension(400,20));
	        _fieldC.addActionListener(listener);

	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelC).addComponent(_fieldC));
	        hLabelGroup.addComponent(labelC);
	        hComboGroup.addComponent(_fieldC);

	        JLabel    labelD = new JLabel("Instrument:");
	        _fieldD = new JTextField(1);
	        _fieldD.setText(""+obs.getInstrument());
	        _fieldD.setMaximumSize(new Dimension(400,20));
	        _fieldD.addActionListener(listener);

	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelD).addComponent(_fieldD));
	        hLabelGroup.addComponent(labelD);
	        hComboGroup.addComponent(_fieldD);

	        JLabel    labelE = new JLabel("Description:");
	        _fieldE = new JTextField(1);
	        _fieldE.setText(""+obs.getDescription());
	        _fieldE.setMaximumSize(new Dimension(400,20));
	        _fieldE.addActionListener(listener);

	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelE).addComponent(_fieldE));
	        hLabelGroup.addComponent(labelE);
	        hComboGroup.addComponent(_fieldE);
	        
	        JLabel    labelF = new JLabel("Author:");
	        _fieldF = new JTextField();
	        _fieldF.setText(""+obs.getCreator());
	        _fieldF.setMaximumSize(new Dimension(400,20));
	        _fieldF.addActionListener(listener);
	        

	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(labelF).addComponent(_fieldF));
	        hLabelGroup.addComponent(labelF);
	        hComboGroup.addComponent(_fieldF);
	        
	        saveButton=new JButton("Save Observation");
	        vPropsGroup = layout.createParallelGroup(BASELINE);
	        vGroup.addGroup(vPropsGroup.addComponent(saveButton));
	        hLabelGroup.addComponent(saveButton);
	        
	        hGroup.addGroup(hLabelGroup);
	        hGroup.addGroup(hComboGroup);
	        layout.setHorizontalGroup(hGroup);
	        layout.setVerticalGroup(vGroup);

			//xmlEditor.setObservation(this.obs);
	        saveButton.addActionListener(new ActionListener() {
	        	 
	            public void actionPerformed(ActionEvent e)
	            {
	            	saveAs();
	    		 
	    			
	            }
	        });
			
			initialized=true;

		}
		
		public void saveAs(){
	        try{
		          JFileChooser chooser = new JFileChooser();
		          try{
		        	  chooser.setSelectedFile(new File(obs.getPath()+"/"+obs.getFileName()));
		          }catch (Exception e3){
		        	chooser.setCurrentDirectory(new File(Properties.getProperty(Properties.DEFAULT_OBSERVATIONS_DIRECTORY)));
		          }
		          chooser.setMultiSelectionEnabled(false);
		          
		          int option = chooser.showSaveDialog(ObservationPanel.this);
		          //File sf;
		          if (option == JFileChooser.APPROVE_OPTION) {
		            File sf = chooser.getSelectedFile();
		            obs.setFileName(sf.getName());
		            obs.setPath(sf.getParent());
	 
		            save();
		            
		          }
		          else {
		            //itlbar.setText("You canceled.");
		          }
		         

	          } catch (Exception e1) {
						JOptionPane.showMessageDialog(ObservationPanel.this,
							    e1.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
						//editor.setText(ptr.toXml());
					}	
		}
		
		public void save() throws IOException{
			ObservationUtil.saveObservation(obs);
		}
		
		public void setObservation(Observation obs){
			if (obs==null) return;
			if (this.obs!=null)this.obs.removeObservationListener(this);
			this.obs=obs;
			obs.addObservationListener(this);
			
			if (!initialized) init();
			else {
				this.refreshMetadata();
				//xmlEditor.setObservation(obs);
				pointingEditor.setSlice((ObservationPointingSlice)obs.getPointing());
				//sequencesEditor.setObsPor((ObservationPor)obs.getCommanding());
				itlEditor.setObsPor((ObservationPor)obs.getCommanding());
			}
		}
		@Override
		public void observationChanged(ObservationChangeEvent event) {
			this.refreshMetadata();
			//xmlEditor.setObservation(obs);
			
		}
		@Override
		public void scheduleChanged() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void metadataChanged(ObservationChangeEvent event) {
			this.refreshMetadata();
			//xmlEditor.setObservation(obs);
			
		}
		@Override
		public void pointingChanged(ObservationChangeEvent event) {
			//xmlEditor.setObservation(obs);
			//pointingEditor.setSlice(obs.getPointing());
			
		}
		@Override
		public void commandingChanged(ObservationChangeEvent event) {
			//sequencesEditor.setObsPor((ObservationPor)obs.getCommanding());
			
		}
}
