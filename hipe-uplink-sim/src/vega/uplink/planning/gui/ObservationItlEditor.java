package vega.uplink.planning.gui;

import herschel.ia.gui.apps.components.util.BottomPanel;
import herschel.ia.gui.kernel.parts.AbstractVariableEditorComponent;
import herschel.ia.jconsole.util.TextLineNumber;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


import vega.IconResources;
import vega.uplink.commanding.itl.ItlParser;
import vega.uplink.commanding.itl.gui.ITLEditorKit;
import vega.uplink.planning.Observation;
import vega.uplink.planning.ObservationChangeEvent;
import vega.uplink.planning.ObservationListener;
import vega.uplink.planning.ObservationPor;

public class ObservationItlEditor extends AbstractVariableEditorComponent<ObservationPor> implements ObservationListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Observation obs;
	JEditorPane editor;
	boolean initialized;
	public ObservationItlEditor(){
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
            			ObservationPor temppor = (ObservationPor) (ItlParser.itlToObs(text, obs.getObsStartDate(), obs.getObsEndDate())).getCommanding();
						temppor.setObservation(obs.copy());
						getPor().regenerate(temppor);
						editor.setText(obs.toItl());
            		}catch(Exception e2){
						JOptionPane.showMessageDialog(ObservationItlEditor.this,
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
		editor = new JEditorPane();
		editor.setEditorKitForContentType(ITLEditorKit.ITL_MIME_TYPE, new ITLEditorKit());
		editor.setContentType(ITLEditorKit.ITL_MIME_TYPE);
		JScrollPane scroll=new JScrollPane(editor);
		scroll.setRowHeaderView(new TextLineNumber(editor));

		global.add(scroll,BorderLayout.CENTER);
		this.add(new BottomPanel(this, global));
		editor.setText(obs.toItl());
		
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
		editor.setText(obs.toItl());
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

