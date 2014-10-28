package vega.uplink.pointing.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import vega.uplink.pointing.PointingElement;
import vega.uplink.pointing.gui.xmlutils.XMLTextEditor;

public class PointingElementXMLEditor extends AbstractVariableEditorComponent<PointingElement> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	PointingElement pointingMetadata;
	XMLTextEditor editor;
	public PointingElementXMLEditor(){
		super(new BorderLayout());
		//this.setGlobalScroll(false, false);
	}
	public void init(){
		//this.setGlobalScroll(false, false);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.LINE_AXIS));
		JLabel classLabel=new JLabel(pointingMetadata.getClass().toString());
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
					Node node = (Node) doc;
					PointingElement pe = PointingElement.readFrom(node.getFirstChild());
					//System.out.println(pe.toXml(0));
					pointingMetadata.regenerate(pe);
					editor.setText(pointingMetadata.toXml(0));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(PointingElementXMLEditor.this,
						    e1.getMessage(),
						    "Error",
						    JOptionPane.ERROR_MESSAGE);
					// TODO Auto-generated catch block
					e1.printStackTrace();
					editor.setText(pointingMetadata.toXml(0));
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
		
		
		/*Container contentPane = getContentPane();
		contentPane.add(listPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.PAGE_END);*/
		JPanel global=new JPanel(new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new XMLTextEditor();
		editor.setText(pointingMetadata.toXml(0));
		global.add(new JScrollPane(editor),BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		//this.add(editor);
	}
	
	public void setPointingMetadata(PointingElement pMetadata){
		pointingMetadata=pMetadata;
		init();
	}
    public boolean makeEditorContent() {
    	setPointingMetadata(getValue());
    	return true;
    }


	
	public Icon getComponentIcon() {
		// TODO Auto-generated method stub
        try {
            URL resource = PointingElementXMLEditor.class.getResource("/vega/vega.gif");
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends PointingElement> getVariableType() {
		return PointingElement.class;
		// TODO Auto-generated method stub
	}


}
