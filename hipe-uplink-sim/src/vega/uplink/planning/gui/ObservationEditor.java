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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationUtil;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrUtils;
import vega.uplink.pointing.gui.PtrXmlEditor;
import vega.uplink.pointing.gui.xmlutils.XMLEditorKit;

public class ObservationEditor extends AbstractVariableEditorComponent<Observation> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Observation obs;
	
	JEditorPane editor;
	boolean initialized=false;
	JLabel classLabel;
	public ObservationEditor(){
		super(new BorderLayout());
		obs=new Observation(new Date(),new Date());
		//init();

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

		buttonsPanel.add(button,BorderLayout.EAST);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		this.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(XMLEditorKit.XML_MIME_TYPE, new XMLEditorKit());
		editor.setContentType(XMLEditorKit.XML_MIME_TYPE);
		//editor.setPreferredSize(new Dimension(100,100));
		JScrollPane scroll=new JScrollPane(editor,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		//this.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, scroll),BorderLayout.CENTER);

		initialized=true;
		//System.out.println(this.getParent().getLayout());
	}
	
	public void setObservation(Observation obs){
		boolean oldListen=Observation.LISTEN;
		Observation.LISTEN=false;
		this.obs=obs;
		
		if (!initialized) init();
		classLabel.setText(obs.getClass().getName());
		editor.setText(obs.toXml());
		//System.out.println(obs.toXml());
		Observation.LISTEN=oldListen;
		//editor.setText("hola\n\nn\n\n\nadios\n\n\n\n\n\nque tal\n\n\n\n\n\n");
		
	}
    public boolean makeEditorContent() {
    	//setGlobalScroll(false);
    	//this.setGlobalScroll(true, false);
    	setObservation(getValue());
    	this.setGlobalScroll(false);
    	//this.setPreferredSize(new Dimension(100,100));
    	//System.out.println(this.getParent().getLayout());
    	return true;
    }
    /*public void setGlobalScroll(boolean horizontalEnabled, boolean verticalEnabled){
    	super.setGlobalScroll(horizontalEnabled,verticalEnabled);
    }*/


	
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
	protected Class<? extends Observation> getVariableType() {
		return Observation.class;
		// TODO Auto-generated method stub
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }

}
