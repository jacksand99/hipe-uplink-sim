package vega.uplink.pointing.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.share.swing.Components;

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

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
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
import org.w3c.dom.Node;

import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.Ptr;
import vega.uplink.pointing.PtrChecker;
import vega.uplink.pointing.PtrUtils;
//import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.gui.xmlutils.XMLEditorKit;
import vega.uplink.pointing.gui.xmlutils.XMLTextEditor;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;

public class PtrXmlEditor extends AbstractVariableEditorComponent<Ptr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Ptr ptr;
	JEditorPane editor;
	public PtrXmlEditor(){
		super(new BorderLayout());
	}
	public void init(){
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JLabel classLabel=new JLabel(ptr.getName());
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
					Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
					ptr.regenerate(tempPtr);
					String warnings = PtrChecker.checkPtr(ptr);
					
					editor.setText(ptr.toXml());
					if (!warnings.equals("")) {
						JOptionPane.showMessageDialog(PtrXmlEditor.this,
								warnings,
							    "Warning",
							    JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(PtrXmlEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					// TODO Auto-generated catch block
					e1.printStackTrace();
					editor.setText(ptr.toXml());
				} 
            }
        });
		JButton buttonSave=new JButton("Save PTR file");
	       buttonSave.addActionListener(new ActionListener() {
	        	 
	            public void actionPerformed(ActionEvent e)
	            {
	            try{
	            	String text = editor.getText();

	    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    			InputStream stream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
	    			Document doc;

					doc = dBuilder.parse(stream);
					doc.getDocumentElement().normalize();
					Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
				
					ptr.regenerate(tempPtr);
					String warnings = PtrChecker.checkPtr(ptr);
					
					editor.setText(ptr.toXml());
					if (!warnings.equals("")) {
						JOptionPane.showMessageDialog(PtrXmlEditor.this,
								warnings,
							    "Warning",
							    JOptionPane.WARNING_MESSAGE);
					}

	            	PtrUtils.savePTR(ptr);
	            } catch (Exception e1) {
						JOptionPane.showMessageDialog(PtrXmlEditor.this,
							    e1.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e1.printStackTrace();
						editor.setText(ptr.toXml());
					} 
	    		 
	    			
	            }
	        });
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonsPanel.add(buttonSave,BorderLayout.WEST);
		
		buttonsPanel.add(button,BorderLayout.EAST);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(XMLEditorKit.XML_MIME_TYPE, new XMLEditorKit());
		editor.setContentType(XMLEditorKit.XML_MIME_TYPE);
		editor.setText(ptr.toXml());
		JScrollPane scroll=new JScrollPane(editor);
		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		
	}
	
	public void setPtr(Ptr ptr){
		this.ptr=ptr;
		init();
	}
    public boolean makeEditorContent() {
    	setPtr(getValue());
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
	protected Class<? extends Ptr> getVariableType() {
		return Ptr.class;
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }



}
