package vega.uplink.pointing.gui;

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
	//XMLTextEditor editor;
	JEditorPane editor;
	public PtrXmlEditor(){
		super(new BorderLayout());
		//super.setGlobalScroll(false, false);
	}
	public void init(){
		//super.setGlobalScroll(false, false);
		//JPanel panel=new JPanel();
		//this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		//this.setLayout(new BorderLayout());
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		//topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
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
					//Node node = (Node) doc;
					//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
					Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
				
					//System.out.println(pe.toXml(0));
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
					//IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
					//iae.initCause(e1);
					//throw(iae);
				} 
    		 
    			
                //Execute when button is pressed
                //System.out.println("You clicked the button");
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
					//Node node = (Node) doc;
					//PointingElement pe = PointingElement.readFrom(node.getFirstChild());
					Ptr tempPtr = PtrUtils.readPTRfromDoc(doc);
				
					//System.out.println(pe.toXml(0));
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
						// TODO Auto-generated catch block
						e1.printStackTrace();
						editor.setText(ptr.toXml());
						//IllegalArgumentException iae = new IllegalArgumentException(e1.getMessage());
						//iae.initCause(e1);
						//throw(iae);
					} 
	    		 
	    			
	                //Execute when button is pressed
	                //System.out.println("You clicked the button");
	            }
	        });
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		buttonsPanel.add(buttonSave,BorderLayout.WEST);
		
        //topPanel.add(buttonSave);
        //topPanel.add(Box.createHorizontalGlue());
		buttonsPanel.add(button,BorderLayout.EAST);
		topPanel.add(buttonsPanel,BorderLayout.WEST);
        //topPanel.add(button);
        //topPanel.add(buttonSave);

        //topPanel.add(Box.createHorizontalGlue());
		topPanel.add(classLabel,BorderLayout.EAST);
		//topPanel.add(classLabel);
		//topPanel.add(Box.createHorizontalGlue());
	//	topPanel.add(button);
		/*Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);*/
		this.add(topPanel,BorderLayout.NORTH);
		editor = new JEditorPane();
		editor.setEditorKitForContentType(XMLEditorKit.XML_MIME_TYPE, new XMLEditorKit());
		editor.setContentType(XMLEditorKit.XML_MIME_TYPE);
		//editor = new XMLTextEditor();
	       //editor.setMinimumSize(new Dimension(100, 30));
	        //editor.setPreferredSize(new Dimension(100, 60));
		editor.setText(ptr.toXml());
		//JScrollPane scroll=new JScrollPane(editor);
        //scroll.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        //scroll.setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
		//scroll.add(editor);
		//panel.add(scroll);
		this.add(editor,BorderLayout.CENTER);
        //this.add(scroll);
		//this.add(scroll,BorderLayout.CENTER);
		//this.setGlobalScroll(false, false);
		//this.add(panel);

		
		/*XMLTextEditor editor = new XMLTextEditor();
		editor.setText(ptr.toXml());
		this.add(editor);*/
	}
	
	public void setPtr(Ptr ptr){
		this.ptr=ptr;
		init();
	}
    public boolean makeEditorContent() {
    	//setGlobalScroll(false);
    	//this.setGlobalScroll(true, false);
    	setPtr(getValue());
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
	protected Class<? extends Ptr> getVariableType() {
		return Ptr.class;
		// TODO Auto-generated method stub
	}
	
    public final void setGlobalScroll(boolean enabled) {
    
	
        setGlobalScroll(enabled, enabled); // horizontal scroll bar not disabled: HCSS-13801
    }



}
