package vega.uplink.planning.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import vega.IconResources;
import vega.hipe.gui.xmlutils.XMLEditorKit;
import vega.uplink.Properties;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.planning.Schedule;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.gui.PtrXmlEditor;

public class ObservationEditor extends AbstractVariableEditorComponent<Observation> implements ObservationListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger LOG = Logger.getLogger(ObservationEditor.class.getName());

	Observation obs;
	
	JEditorPane editor;
	boolean initialized=false;
	JLabel classLabel;
	public ObservationEditor(){
		super(new BorderLayout());
		obs=new Observation(new Date(),new Date());

	}
	public void init(){
		JPanel topPanel=new JPanel(new BorderLayout());
		JPanel buttonsPanel=new JPanel(new BorderLayout());
		classLabel=new JLabel();
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        button.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	String text = editor.getText();
				try {
	    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
	    			Document doc;

					doc = dBuilder.parse(stream);
					doc.getDocumentElement().normalize();
					Observation tempObs = ObservationUtil.readObservationFromDoc(doc);
				
					obs.regenerate(tempObs);
					String warnings="";
					editor.setText(obs.toXml());
					if (!warnings.equals("")) {
						JOptionPane.showMessageDialog(ObservationEditor.this,
								warnings,
							    "Warning",
							    JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(ObservationEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					e1.printStackTrace();
					editor.setText(obs.toXml());
				} 
    		 
    			
            }
        });

		button.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		JButton saveButton=new JButton("Save");
		saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		saveButton.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	saveAs();
    			
            }
        });

		saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);

		
		
		buttonsPanel.add(button,BorderLayout.EAST);
		buttonsPanel.add(saveButton,BorderLayout.WEST);
		
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		this.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(XMLEditorKit.XML_MIME_TYPE, new XMLEditorKit());
		editor.setContentType(XMLEditorKit.XML_MIME_TYPE);
		JScrollPane scroll=new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(new BottomPanel(this, scroll),BorderLayout.CENTER);

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
	          
	          int option = chooser.showSaveDialog(ObservationEditor.this);
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
					JOptionPane.showMessageDialog(ObservationEditor.this,
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
		if (this.obs!=null) this.obs.removeObservationListener(this);
		obs.addObservationListener(this);
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.obs=obs;
		
		if (!initialized) init();
		classLabel.setText(obs.getClass().getName());
		editor.setText(obs.toXml());
		Observation.LISTEN=oldListen;
		
	}
    public boolean makeEditorContent() {
    	setObservation(getValue());
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
	protected Class<? extends Observation> getVariableType() {
		return Observation.class;
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }
	@Override
	public void observationChanged(ObservationChangeEvent event) {
		setObservation(obs);
		
	}
	@Override
	public void scheduleChanged() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		setObservation(obs);
		
	}
	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		LOG.info("Listenerd change in pointing");
		setObservation(obs);
		
	}
	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		setObservation(obs);
		
	}

}
