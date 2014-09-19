package vega.uplink.pointing.gui;

import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;

import java.awt.Component;
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
import vega.uplink.pointing.gui.xmleditor.XMLTextEditor;

public class PtrXmlEditor extends AbstractVariableEditorComponent<Ptr> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Ptr ptr;
	XMLTextEditor editor;
	public PtrXmlEditor(){
		super();
		//this.setGlobalScroll(false, false);
	}
	public void init(){
		//this.setGlobalScroll(false, false);
		//JPanel panel=new JPanel();
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
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
        topPanel.add(button);
        topPanel.add(Box.createHorizontalGlue());
		topPanel.add(classLabel);
		//topPanel.add(Box.createHorizontalGlue());
	//	topPanel.add(button);
		/*Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);*/
		this.add(topPanel);
		editor = new XMLTextEditor();
		editor.setText(ptr.toXml());
		//JScrollPane scroll=new JScrollPane(editor);
		//scroll.add(editor);
		//panel.add(scroll);
		this.add(editor);
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
		// TODO Auto-generated method stub
	}


}
