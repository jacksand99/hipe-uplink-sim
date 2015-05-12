package vega.uplink.planning.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.jconsole.util.TextLineNumber;

import java.awt.BorderLayout;
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

import vega.IconResources;
import vega.hipe.gui.xmlutils.XMLTextEditor;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.ObservationPor;
import vega.uplink.planning.ObservationUtil;

public class ObservationPorXmlEditor extends AbstractVariableEditorComponent<ObservationPor> implements ObservationListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Observation obs;
	XMLTextEditor editor;
	boolean initialized;
	public ObservationPorXmlEditor(){
		super(new BorderLayout());
		initialized=false;
	}
	public ObservationPor getPor(){
		return (ObservationPor) obs.getCommanding();
	}
	public void init(){
		JPanel topPanel=new JPanel();
		topPanel.setLayout(new BorderLayout());
		JPanel buttonsPanel=new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JLabel classLabel=new JLabel(ObservationPor.class.getName());
		classLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton button=new JButton("Finish Edditing");
		button.setAlignmentX(Component.LEFT_ALIGNMENT);
		
        button.addActionListener(new ActionListener() {
        	 
            public void actionPerformed(ActionEvent e)
            {
            	String text = editor.getText();
            		try{
		    			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		    			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		    			String tempText="<por>\n"+text+"</por>\n";
		    			InputStream stream = new ByteArrayInputStream(tempText.getBytes(StandardCharsets.UTF_8));
		    			Document doc;
	
						doc = dBuilder.parse(stream);
						doc.getDocumentElement().normalize();
						ObservationPor temppor = ObservationUtil.readObsPorfromDoc(doc);
						temppor.setObservation(obs.copy());
						getPor().regenerate(temppor);
						editor.setText(getPor().toObsXml(0));
            		}catch(Exception e2){
						JOptionPane.showMessageDialog(ObservationPorXmlEditor.this,
							    e2.getMessage(),
							    "Error",
							    JOptionPane.ERROR_MESSAGE);
						e2.printStackTrace();
						editor.setText(getPor().toObsXml(0));
           			
            		}
    			
            }
        });

		button.setAlignmentX(Component.LEFT_ALIGNMENT);

		buttonsPanel.add(button,BorderLayout.CENTER);

		topPanel.add(buttonsPanel,BorderLayout.WEST);
		topPanel.add(classLabel,BorderLayout.EAST);
		JPanel global = new JPanel (new BorderLayout());
		global.add(topPanel,BorderLayout.NORTH);
		editor = new XMLTextEditor();

		JScrollPane scroll=new JScrollPane(editor);
		scroll.setRowHeaderView(new TextLineNumber(editor));

		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		editor.setText(getPor().toObsXml(0));
		
		global.add(new JScrollPane(editor));
		this.add(new BottomPanel(this, global));
		initialized=true;
	}
	
	public void setObsPor(ObservationPor por){
		if (this.obs!=null) obs.removeObservationListener(this);
		obs=por.getObservation();
		if (!initialized){
			init();
		}
		
		obs.addObservationListener(this);
		editor.setText(getPor().toObsXml(0));
	}
    public boolean makeEditorContent() {
    	setObsPor(getValue());
    	return true;
    }


	
	public Icon getComponentIcon() {
        try {
            URL resource = ObservationPorXmlEditor.class.getResource(IconResources.HUS_ICON);
            BufferedImage imageIcon = ImageIO.read(resource);
            return new ImageIcon(imageIcon);
    } catch (IOException e) {
           
            e.printStackTrace();
            return null;
    }
	}
	@Override
	protected Class<? extends ObservationPor> getVariableType() {
		return ObservationPor.class;
		// TODO Auto-generated method stub
	}
	@Override
	public void observationChanged(ObservationChangeEvent event) {
		this.setObsPor(getPor());
		
	}
	@Override
	public void scheduleChanged() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void metadataChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void pointingChanged(ObservationChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void commandingChanged(ObservationChangeEvent event) {
		this.setObsPor(getPor());
		
	}


}
